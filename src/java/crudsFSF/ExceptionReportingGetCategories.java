package crudsFSF;

import crudsFSF.util.JsfUtil;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
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
@WebServlet(name = "ExceptionReportingGetCategories", urlPatterns = {"/ExceptionReportingGetCategories"})
public class ExceptionReportingGetCategories extends HttpServlet {

    @Resource(name="app/haloReplicateDB")
    private DataSource dataSource;
    
    private String resultMessage = "";
    
     /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @return 
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    public static Connection getMySqlConnection() throws Exception {
        String driver = "org.gjt.mm.mysql.Driver";
        String url = "jdbc:mysql://localhost/halo161";
        String username = "root";
        String password = "axiz1234";

        Class.forName(driver);
        Connection conn = DriverManager.getConnection(url, username, password);
         return conn;
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try (Connection conn = getMySqlConnection()) {
            List<String> nameList = new LinkedList<>();
            List<String> cntList = new LinkedList<>();

            String simpleProc = "{ call getTXNExceptionCategories( ? ) }";

            CallableStatement cs = conn.prepareCall(simpleProc);

            cs.registerOutParameter(1, java.sql.Types.VARCHAR);

            cs.execute();
            
            if ( cs.getString(1) != null && !cs.getString(1).trim().isEmpty()) { 
                resultMessage = cs.getString(1);
            }
            
            ResultSet rs = cs.getResultSet();

            // Fetch each row from the result set
            while (rs.next()) {
                cntList.add( rs.getString(1) );
                nameList.add( rs.getString(2) );
            }

            String[] nameArr = new String[nameList.size()];
            nameArr = nameList.toArray(nameArr);

            String[] cntArr = new String[cntList.size()];
            cntArr = cntList.toArray(cntArr);

            JsonArrayBuilder jarr = Json.createArrayBuilder();

            for (int i = 0; i < nameArr.length; i++) {
                jarr.add(Json.createObjectBuilder()
                        .add("name", nameArr[i])
                        .add("cnt", cntArr[i]));
            }

            JsonArray jsonArrayObj = jarr.build();

            String jsonString = jsonArrayObj.toString();

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonString);

            if ( resultMessage != null && !resultMessage.trim().isEmpty()) { 
                throw new Exception(resultMessage);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage() );
             
            Logger.getLogger(ExceptionReportingGetCategories.class.getName()).log(Level.SEVERE, null, ex);

            Throwable cause = ex.getCause();                
            String msg = "";
            
            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            JsfUtil.addErrorMessage("Exception : " + msg + " : " + ex.getMessage()  );                
            
            response.sendRedirect("index.html?Success=Could Not get Categories!");
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
