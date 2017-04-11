/**
 * $LastChangedBy: Andre $
 * $LastChangedDate: 2016-07-07 09:50:33 +0200 (Thu, 07 Jul 2016) $
 * $LastChangedRevision: 994 $
 */

package com.stellr.webui.controller;

import crudsFSF.util.JsfUtil;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.DependsOn;
import javax.ejb.NoSuchEJBException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import stellr.log.LogManagerRemote;

/**
 *
 * @author stu
 */
@Named("loggerEJBController")
@SessionScoped
@DependsOn({"ConfigMngr", "Logger"})
public class LoggerEJBController implements Serializable{
    private static final long serialVersionUID = 1971378451676489330L;
    public void refresh(){
        LogManagerRemote bean = lookupLogMangerBeanRemote();
        if(bean != null) {
            bean.load();
            JsfUtil.addSuccessMessage("Logging Manager EJB refresh");
        } else {
            JsfUtil.addErrorMessage("Could not load Logging Manager EJB");
        }
    }
    
    private static final Logger LOG = Logger.getLogger(LoggerEJBController.class.getName());
    
    private static LogManagerRemote lookupLogMangerBeanRemote() {
        try {
            Context c = new InitialContext();
            LogManagerRemote lmr = (LogManagerRemote) c.lookup("java:global/Logger/Logger-ejb/LoggerManager!stellr.log.LogManagerRemote");
            lmr.load();
            return lmr;
        } catch (NoSuchEJBException | NamingException ex) {
            LOG.log(Level.WARNING, "Could not get logging manager: {0}", ex.toString());
            return null;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Could not get logging manager: {0}", ex.toString());
            return null;
        }
    }
}
