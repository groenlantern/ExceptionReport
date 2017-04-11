package crudsFSF;

import crudsFSF.util.JsfUtil;
import java.io.Serializable;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Named;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;
import org.apache.commons.lang3.time.DateUtils;
 
/**
 *
 * @author Jean-Pierre Erasmus
 */
@Named("ExceptionReportController")
@SessionScoped
public class ExceptionReportController extends HttpServlet implements Serializable {
    private static final long serialVersionUID = 2101906307121572365L;

    /**
     * 
     */
    @Resource(name="java:app/haloReplicateDB")
    private DataSource dataSourceGridController;
    /**
     * 
     */
    private List<ReportExceptionItems> items = null;
    private List<ReportExceptionItems> filteredItems = null;
    private ReportExceptionItems selected;
    
    //From and To Dates
    private Date fromDateObj;
    private Date toDateObj;

    private int totalExceptions;
    private String topSeverity;
    
    private int showRows = 20;
    private String resultMessage = "";
    
    /**
     * 
     * @return
     * @throws Exception 
     */
    public Connection getMySqlConnection() throws Exception {
         return dataSourceGridController.getConnection();
    }
    
    /**
     * Constructor
     */
    public ExceptionReportController() {    
        //Blank Item Array 
        items = new ArrayList<>();
     
        //Setup date range 
        fromDateObj =  getTimeStampFromDate(new Date(), 0,0,0);
        toDateObj =  getTimeStampFromDate(new Date(), 23,59,59);        
        
        //Default for testing defaultDATE
       // fromDateObj =  getDateFromString("2014-01-01", 0,0,0);                   
        totalExceptions = 0;
        topSeverity = "";
        
     
    }

    /**
     * 
     */
    @PostConstruct
    @Override
    public void init() {
        loadItems();
    }
    
    /**
     * 
     * @param value
     * @param filter
     * @param locale
     * @return 
     */
    public boolean filterByDate(Object value, Object filter, Locale locale) {
        if( filter == null ) {
            return true;
        }

        if( value == null ) {
            return false;
        }

        return DateUtils.truncatedEquals((Date) filter, (Date) value, Calendar.DATE);
    }
    
    /**
     * 
     * @return 
     */
    public ReportExceptionItems getSelected() {
        return selected;
    }

    /**
     * 
     * @param selected 
     */
    public void setSelected(ReportExceptionItems selected) {
        
        this.selected = selected;
    }

    /**
     * 
     */
    protected void setEmbeddableKeys() {
    }

    /**
     * 
     */
    protected void initializeEmbeddableKey() {
    }

    /**
     * 
     * @return 
     */
    public List<ReportExceptionItems> getFilteredItems() {
        
        return filteredItems;
    }

    /**
     * 
     * @param filteredItems 
     */
    public void setFilteredItems(List<ReportExceptionItems> filteredItems) {
       
        this.filteredItems = filteredItems;
    }

    /**
     * 
     * @return 
     */
    public ReportExceptionItems prepareCreate() {   
        return selected;
    }

    /**
     * 
     */
    public void create() {
    }

    /**
     * 
     */
    public void update() {        
    }

    /**
     * 
     */
    @Override
    public void destroy() {
        
    }
    
     /**
     * 
     * @param dateValue
     * @param tHH
     * @param tMM
     * @param tSS
     * @return 
     */    
    private Timestamp getTimeStampFromDate(Date dateValue, int tHH, int tMM, int tSS) { 
            int dYYYY;
            int dMM;
            int dDD;            
            
            try { 
                //Extract date details
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateValue);
                                
                dYYYY = cal.get(Calendar.YEAR);
                dMM = cal.get(Calendar.MONTH);
                dDD = cal.get(Calendar.DAY_OF_MONTH);                

            } catch (Exception e1) { 
                
                
                //default to today
                Calendar baseCalendar = Calendar.getInstance(); 
                
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
     * 
     * @param dateYYYYmmDD
     * @param tHH
     * @param tMM
     * @param tSS
     * @return 
     */    
    private Timestamp getDateFromString(String dateYYYYmmDD, int tHH, int tMM, int tSS) { 
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
     * 
     */
    public void loadItems() {                
        System.out.println("loadItems Report");
        
        //Connection details    
        Connection conn = null;
        CallableStatement cs;
        ResultSet rs;

        try {
            ReportExceptionItems singleItem;
            
            items = new ArrayList<>();
   
            conn = getMySqlConnection();
                                 
            String getCategoriesTotalsProc = "{  CALL getTXNExceptionsReportDatesMain(? , ?, ?  )  }";
            
          
            cs = conn.prepareCall(getCategoriesTotalsProc);
            cs.setTimestamp(1,  this.getTimeStampFromDate(getFromDateObj(), 0,0,0) );
            cs.setTimestamp(2, this.getTimeStampFromDate(getToDateObj(), 23,59,59) );
            
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);

            cs.execute();
            
            if ( cs.getString(3) != null && !cs.getString(3).trim().isEmpty()) { 
                resultMessage = cs.getString(3);
            }
            
            rs = cs.getResultSet();
            
            // Fetch each row from the result set
            while (rs.next()) {
                singleItem = new ReportExceptionItems();
                singleItem.setDoneWhen(rs.getTimestamp("donewhen") );
                singleItem.setReportExceptionCategory(rs.getString("category") );
                singleItem.setReportExceptionDescription(rs.getString("description") );
                singleItem.setReportExceptionMerchant( rs.getString("merchantid") );
                singleItem.setReportExceptionRRN(rs.getString("rrn") );
                singleItem.setReportExceptionTUI(rs.getString("transactionuniqueidentifier") );
                singleItem.setReportExceptionTxnExId(rs.getLong("txnexceptionid") );
                singleItem.setSeverity( rs.getString("severity") );
                
                singleItem.setTransactionid( rs.getLong("transactionid") );
                singleItem.setProcessingcode(rs.getString("processingcode"));
                singleItem.setTransactionstate(rs.getString("transactionstate"));
                singleItem.setPrimaryaccountnumber(rs.getString("primaryaccountnumber"));
                singleItem.setTransactionamount(rs.getString("transactionamount"));
                singleItem.setResponsecode(rs.getString("responsecode"));
                singleItem.setSecondaryrespcode(rs.getString("secondaryrespcode"));
                singleItem.setProductid(rs.getString("productid"));
                singleItem.setProductstatus(rs.getString("productstatus"));
                
                items.add(singleItem);            
            }            
            
            if ( resultMessage != null && !resultMessage.trim().isEmpty()) { 
                throw new Exception(resultMessage);
            }
            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            
            Logger.getLogger(ExceptionReportController.class.getName()).log(Level.SEVERE, null, ex);       

            Throwable cause = ex.getCause();                
            String msg = "";
            
            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            JsfUtil.addErrorMessage("Exception : " + msg + " : " + ex.getMessage()  );                
            
        } finally { 
            try {
                if ( conn != null ) conn.close();
            } catch (SQLException ex) {
            }
        }            
    } 

    /**
     * 
     * @return 
     */
    public List<ReportExceptionItems> getItems() {               
        System.out.println("getItems Report");
        
        return items;
    } 

    /**
     * @return the fromDateObj
     */
    public Date getFromDateObj() {
        return fromDateObj;
    }

    /**
     * @param fromDateObj the fromDateObj to set
     */
    public void setFromDateObj(Date fromDateObj) {        
        this.fromDateObj = fromDateObj;
        
        loadItems();
    }

    /**
     * @return the toDateObj
     */
    public Date getToDateObj() {
        return toDateObj;
    }

    /**
     * @param toDateObj the toDateObj to set
     */
    public void setToDateObj(Date toDateObj) {
        this.toDateObj = toDateObj;
        
        loadItems();
    }

    /**
     * @return the totalExceptions
     */
    public int getTotalExceptions() {
        Connection conn = null;
        
        try  {
            conn = getMySqlConnection();
            
            //Stored procedures for totals
            String totalProc = "{ call getTXNExceptionTotal(?, ?, ? ) }";
            
            //----------------------------------------------------------------------------------------------------------            
            //Get Total Exceptions Count
            CallableStatement cs = conn.prepareCall(totalProc);
            cs.setTimestamp(1,  this.getTimeStampFromDate(getFromDateObj(), 0,0,0) );
            cs.setTimestamp(2, this.getTimeStampFromDate(getToDateObj(), 23,59,59) );
            
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);

            cs.execute();
            
            if ( cs.getString(3) != null && !cs.getString(3).trim().isEmpty()) { 
                resultMessage = cs.getString(3);
            }
            
            ResultSet rs = cs.getResultSet();

            // Fetch first row from the result set - should only be one - total exceptions
            if (rs.next()) {
                totalExceptions = Integer.parseInt( rs.getString(1) ) ;
            }            

            if ( resultMessage != null && !resultMessage.trim().isEmpty()) { 
                throw new Exception(resultMessage);
            }
            
        } catch (Exception ex) {
            totalExceptions = 0;
            System.out.println(ex.getMessage() );
             
            Logger.getLogger(ExceptionReportController.class.getName()).log(Level.SEVERE, null, ex);
            
            Throwable cause = ex.getCause();                
            String msg = "";
            
            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            JsfUtil.addErrorMessage("Exception : " + msg + " : " + ex.getMessage()  );                
            
        } finally { 
            try {
                conn.close();
            } catch (SQLException ex) {
            }
        }
        
        return totalExceptions;
    }

    /**
     * @return the topSeverity
     */
    public String getTopSeverity() {
        Connection conn = null;
        
        try  {
            conn = getMySqlConnection();
            
            //Stored procedures for totals
            String totalProc = "{ call getTXNExceptionHighestSeverity(?, ?, ? ) }";
            
            //----------------------------------------------------------------------------------------------------------            
            //Get Total Exceptions Count
            CallableStatement cs = conn.prepareCall(totalProc);
            cs.setTimestamp(1,  this.getTimeStampFromDate(getFromDateObj(), 0,0,0) );
            cs.setTimestamp(2, this.getTimeStampFromDate(getToDateObj(), 23,59,59) );
            
            cs.registerOutParameter(3, java.sql.Types.VARCHAR);

            cs.execute();
            
            if ( cs.getString(3) != null && !cs.getString(3).trim().isEmpty()) { 
                resultMessage = cs.getString(3);
            }
            
            ResultSet rs = cs.getResultSet();

            // Fetch first row from the result set - should only be one - total exceptions
            if (rs.next()) {
                topSeverity =   rs.getString(1) ;
            }            
            
            if ( resultMessage != null && !resultMessage.trim().isEmpty()) { 
                throw new Exception(resultMessage);
            }
            
        } catch (Exception ex) {
            System.out.println("Error Top Severity " + ex.getMessage());
            
            topSeverity = "";
            Logger.getLogger(ExceptionReportController.class.getName()).log(Level.SEVERE, null, ex);
            
            Throwable cause = ex.getCause();                
            String msg = "";
            
            if (cause != null) {
                msg = cause.getLocalizedMessage();
            }

            JsfUtil.addErrorMessage("Exception : " + msg + " : " + ex.getMessage()  );                
            
        } finally { 
            try {
                conn.close();
            } catch (SQLException ex) {
            }
        }
        return topSeverity;
    }

    /**
     * @param totalExceptions the totalExceptions to set
     */
    public void setTotalExceptions(int totalExceptions) {
        this.totalExceptions = totalExceptions;
    }
   
    /**
     * @param topSeverity the topSeverity to set
     */
    public void setTopSeverity(String topSeverity) {
        this.topSeverity = topSeverity;
    }

    public DataSource getDataSourceGridController() {
        return dataSourceGridController;
    }

    public void setDataSourceGridController(DataSource dataSourceGridController) {
        this.dataSourceGridController = dataSourceGridController;
    }

    public int getShowRows() {
        return showRows;
    }

    public void setShowRows(int showRows) {
        this.showRows = showRows;
    }

    /**
     * @param items the items to set
     */
    public void setItems(List<ReportExceptionItems> items) {
        this.items = items;
    }

    /**
     * 
     */
    @FacesConverter(forClass = ReportExceptionItems.class)
    public static class ReportExceptionItemsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object value) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

}
