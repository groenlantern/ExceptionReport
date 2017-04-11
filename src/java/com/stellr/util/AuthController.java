/**
 * $LastChangedBy: Andre $
 * $LastChangedDate: 2016-07-07 09:50:33 +0200 (Thu, 07 Jul 2016) $
 * $LastChangedRevision: 994 $
 */
package com.stellr.util;


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author stu
 */
@ManagedBean (name = "authController")
@RequestScoped
public class AuthController {
    private static Logger log = Logger.getLogger(AuthController.class.getName());
  
  public String logout() {
    String result="/index?faces-redirect=true";
    
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
    
    try {
      request.logout();
    } catch (ServletException e) {
      log.log(Level.WARNING, "Failed to logout user!", e);
      result = "/loginError?faces-redirect=true";
    }
    
    return result;
  }
}


