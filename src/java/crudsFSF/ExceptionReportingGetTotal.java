package crudsFSF;

import crudsFSF.util.JsfUtil;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 *
 * @author Jean-Pierre Erasmus
 */
@WebServlet(name = "ExceptionReportingGetTotal", urlPatterns = {"/ExceptionReportingGetTotal"})
public class ExceptionReportingGetTotal extends HttpServlet {

    @Resource(name="java:app/haloReplicateDB")
    private DataSource dataSourceLocal;
    
    private String resultMessage = "";
    
    /**
     * 
     * @return
     * @throws Exception 
     */
    public Connection getMySqlConnection() throws Exception {
         return dataSourceLocal.getConnection();
    }

    /**
     * 
     * @param dateYYYYmmDD
     * @param tHH
     * @param tMM
     * @param tSS
     * @return 
     */    
    public Timestamp getDateFromString(String dateYYYYmmDD, int tHH, int tMM, int tSS) { 
            int dYYYY;
            int dMM;
            int dDD;
            Calendar baseCalendar = Calendar.getInstance(); 
            
            try { 
                //Extract date details
                String[] fromaDateArray = dateYYYYmmDD.split("-");
                dYYYY = Integer.parseInt( fromaDateArray[0] );
                dMM = Integer.parseInt( fromaDateArray[1] ) - 1;
                dDD = Integer.parseInt( fromaDateArray[2] );
            } catch (Exception e1) { 
                //default to today
                dYYYY = baseCalendar.get(Calendar.YEAR);
                dMM = baseCalendar.get(Calendar.MONTH); // Note: zero based!
                dDD = baseCalendar.get(Calendar.DAY_OF_MONTH);
            }
            
            //Build date from details
            Calendar dCalendar = new GregorianCalendar(dYYYY,dMM,dDD,tHH,tMM,tSS);
            
            //return timeStamp
            return (new Timestamp( dCalendar.getTime().getTime() ));
    }
    
       /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request
     * @param response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //variables
        String fromDateStr = "";
        String toDateStr = "";
        String totalExcep = "0";
        String topSeverity = "0";
        
        //Get Dates from parameters via frontend        
        Enumeration <String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            
            String paramName = parameterNames.nextElement();
            String value = request.getParameter(paramName);
            
            if ( paramName.trim().equals("from_date")) { 
                fromDateStr = value.trim();
            }
            if ( paramName.trim().equals("to_date")) { 
                toDateStr = value.trim();
            }
        }
        
        //Get From and To Dates
        Timestamp fromDate = getDateFromString(fromDateStr, 0, 0, 0);
        Timestamp toDate = getDateFromString(toDateStr, 23,59,59);
        
        //Load Main Totals, totals by category and totals by severity
        Connection conn = null;
        
        try  {
            conn = getMySqlConnection();
            
            //Stored procedures for totals
            String totalProc = "{ call getTXNExceptionTotal(?, ?, ? ) }";
            String severTopProc = "{ call getTXNExceptionHighestSeverity(?, ?, ? ) }";
            
            //----------------------------------------------------------------------------------------------------------            
            //Get Total Exceptions Count
            CallableStatement cs = conn.prepareCall(totalProc);
            cs.setTimestamp(1, fromDate );
            cs.setTimestamp(2, toDate );
            
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);

            cs.execute();
            
            if ( cs.getString(3) != null && !cs.getString(3).trim().isEmpty()) { 
                resultMessage = ": 1 : " + cs.getString(3);
            }
            
            ResultSet rs = cs.getResultSet();

            // Fetch first row from the result set - should only be one - total exceptions
            if (rs.next()) {
                totalExcep = rs.getString(1);
            }            
            //--------------------------------------------------------------------------------------------
            
            //Get Top Severity 
            cs = conn.prepareCall(severTopProc);
            cs.setTimestamp(1, fromDate );
            cs.setTimestamp(2, toDate );
            
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);

            cs.execute();
            
            if ( cs.getString(3) != null && !cs.getString(3).trim().isEmpty()) { 
                resultMessage += " : 2 : " + cs.getString(3);
            }
            
            rs = cs.getResultSet();

            if (rs.next()) {
                topSeverity = rs.getString(1);
            }
            //--------------------------------------------------------------------------------------            
           //Get Category totals for pie Chart 
            
            List<String> catNameList = new LinkedList<>();
            List<String> catCntList = new LinkedList<>();

            String getCategoriesTotalsProc = "{ call getTXNExceptionCategoriesDate( ? , ? , ? ) }";

            cs = conn.prepareCall(getCategoriesTotalsProc);
            cs.setTimestamp(1, fromDate );
            cs.setTimestamp(2, toDate );
            
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);

            cs.execute();
            
            if ( cs.getString(3) != null && !cs.getString(3).trim().isEmpty()) { 
                resultMessage += " : 3 : " + cs.getString(3);
            }
            
            rs = cs.getResultSet();

            // Fetch each row from the result set
            while (rs.next()) {
                catCntList.add( rs.getString(1) );
                catNameList.add( rs.getString(2) );
            }

            //Build JSON Array from data list
            String[] catNameArr = new String[catNameList.size()];
            catNameArr = catNameList.toArray(catNameArr);
            String[] catCntArr = new String[catCntList.size()];
            catCntArr = catCntList.toArray(catCntArr);

            JsonArrayBuilder jarr = Json.createArrayBuilder();

            for (int i = 0; i < catNameArr.length; i++) {
                jarr.add(Json.createObjectBuilder()
                        .add("name", catNameArr[i])
                        .add("cnt", catCntArr[i]));
            }

            JsonArray jsonArrayObj = jarr.build();

            //---------------------------------------------------------------------            
           //Get Severity totals for pie Chart             
           
            List<String> severityNameList = new LinkedList<>();
            List<String> severityCntList = new LinkedList<>();

            String getSeverityTotalsProc = "{ call getTXNExceptionseverityDates( ? , ?, ?  ) }";

            System.out.println("fromDate " + fromDate );
            System.out.println("toDate " + toDate );
            
            cs = conn.prepareCall(getSeverityTotalsProc);
            cs.setTimestamp(1, fromDate );
            cs.setTimestamp(2, toDate );
            
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);

            cs.execute();
            
            if ( cs.getString(3) != null && !cs.getString(3).trim().isEmpty()) { 
                resultMessage += " : 4 : " + cs.getString(3);
            }
            
            rs = cs.getResultSet();

            // Fetch each row from the result set
            while (rs.next()) {
                severityCntList.add( rs.getString(1) );
                severityNameList.add( rs.getString(2) );
            }

            String[] sevNameArr = new String[severityNameList.size()];
            sevNameArr = severityNameList.toArray(sevNameArr);
            String[] sevCntArr = new String[severityCntList.size()];
            sevCntArr = severityCntList.toArray(sevCntArr);

            //Severity JSON Array
            JsonArrayBuilder sevJarr = Json.createArrayBuilder();

            for (int i = 0; i < sevNameArr.length; i++) {
                sevJarr.add(Json.createObjectBuilder()
                        .add("name", sevNameArr[i])
                        .add("cnt", sevCntArr[i]));
            }

            JsonArray sevJsonArrayObj = sevJarr.build();

            //---------------------------------------------------------------------            
            
            //Build Json Object for Result            
            JsonObject totalObject = Json.createObjectBuilder()
                                        .add("totalexc", totalExcep)                //Total Exceptions
                                        .add("topseverity", topSeverity)            //Top Severity
                                        .add("totCategoriesArray", jsonArrayObj)    //Category Totals
                                        .add("totSeverityArray", sevJsonArrayObj)   //Severity Totals
                                     .build();

            String jsonString = totalObject.toString();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonString);
            
            if ( resultMessage != null && !resultMessage.trim().isEmpty()) { 
                throw new Exception(resultMessage);
            }
            
        } catch (Exception ex) {
            System.out.println("exExceptionReportingGetTotal!  " + ex);
            
            Logger.getLogger(ExceptionReportingGetTotal.class.getName()).log(Level.SEVERE, null, ex);

            Throwable cause = ex.getCause();                
            String msg = "";
            
            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            JsfUtil.addErrorMessage("Exception : " + msg + " : " + ex.getMessage()  );                
            
            response.sendRedirect("index.html?Success=Could Not get ExceptionReportingGetTotal!");
        } finally { 
            try {
                conn.close();
            } catch (SQLException ex) {
            }
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
