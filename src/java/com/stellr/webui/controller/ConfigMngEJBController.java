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
import stellr.beans.ConfigManagerRemote;

/**
 *
 * @author stu
 */
@Named("configMngEJBController")
@SessionScoped
@DependsOn("ConfigMngr")
public class ConfigMngEJBController implements Serializable{
    private static final long serialVersionUID = 5929257420040021252L;
    
    public void refresh(){
        ConfigManagerRemote bean = lookupConfigMangerBeanRemote();
        if(bean != null) {
            bean.LoadData();
            JsfUtil.addSuccessMessage("Config Manager EJB refreshed");
        } else {
            JsfUtil.addErrorMessage("Could not load Config Manager EJB");
        }
    }
    
    private static final Logger LOG = Logger.getLogger(ConfigMngEJBController.class.getName());
    
    private ConfigManagerRemote lookupConfigMangerBeanRemote() {
        try {
            Context c = new InitialContext();
            return (ConfigManagerRemote) c.lookup("java:global/ConfigMngr/ConfigMngr-ejb/ConfigManager!stellr.beans.ConfigManagerRemote");
        } catch (NoSuchEJBException | NamingException ex) {
            LOG.log(Level.WARNING, "Could not get config manager: {0}", ex.toString());
            return null;
        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Could not get config manager: {0}", ex.toString());
            return null;
        }
    }
}
