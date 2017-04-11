/**
 * $LastChangedBy: Andre $
 * $LastChangedDate: 2016-07-07 09:50:33 +0200 (Thu, 07 Jul 2016) $
 * $LastChangedRevision: 994 $
 */

package com.stellr.util.users;

/**
 *
 * @author Stuart Kemp
 */
public class UserGroups {
    private Integer groupId;
    private String groupName;
    private String groupDescription;
    private Boolean assigned;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public Boolean getAssigned() {
        return assigned;
    }

    public void setAssigned(Boolean assigned) {
        this.assigned = assigned;
    }
    
    
}
