/*
 * $Id$ 
 * $Author$
 * &Date$
 * 
 * $Rev$
 * 
 * $LastChangedBy$ 
 * $LastChangedRevision$
 * $LastChangedDate$
 * 
 * $URL$
 * 
 * (C) 2016 Stellr Singapore Pte. Ltd  All Rights Reserved 
 * 
 */
package com.stellr.util;

/**Enum to hold all possible values for generic cpa properties
 *
 * @author Stuart Kemp
 */
public enum GCPAEnum {
  
    HOSTNAME(".hostname"),
    PORT(".port"),
    PRODUCT(".product"),
    SCHEMA(".schema"),
    CLIENT_AUTH(".clientauth"),
    CONNECTION_TIMEOUT(".connecttimeout"),
    TYPE(".type"),
    URL(".url"),
    RX_TIMEOUT(".rxtimeout"),
    KEY_FILE(".keyfile"),
    KEY_FILE_PWD(".keyfilepwd"),
    KEY_ALIAS(".keyalias"),
    TRUSTED_STORE(".trustedstore"),
    CARD_PAN(".cardpan"),
    DIG_PAN(".digpan"),
    POR_PAN(".porpan"),
    MID_CAT(".midcat");
    
    private String value;
    
    GCPAEnum(String value){
        this.value = value;
    }
    
    public String value(){
        return value;
    }
}
