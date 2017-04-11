/**
 * $LastChangedBy: Andre $
 * $LastChangedDate: 2016-07-07 09:50:33 +0200 (Thu, 07 Jul 2016) $
 * $LastChangedRevision: 994 $
 */

package com.stellr.util.users;

import java.util.List;

/**
 * Class to hold and display User Info and Assigned Group Information
 * Used purely as datasource for jsf view
 *
 * @author Stuart Kemp
 */
public class UserRights {
    //string list of allocated user rights
    private String userGroupNames;
    private List<Integer> userMembership;
    private String username;
    private String firstname;
    private String lastname;

    public String getUserGroupNames() {
        return userGroupNames;
    }

    public void setUserGroupNames(String userGroupNames) {
        this.userGroupNames = userGroupNames;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<Integer> getUserMembership() {
        return userMembership;
    }

    public void setUserMembership(List<Integer> userMembership) {
        this.userMembership = userMembership;
    }
    
    
    
}
