/**
 * $LastChangedBy: Stuart $
 * $LastChangedDate: 2016-10-19 16:25:59 +0200 (Wed, 19 Oct 2016) $
 * $LastChangedRevision: 1577 $
 */

package com.stellr.util;

import java.math.BigDecimal;

/**
 *
 * @author Andre Labuschagne
 */
public class Timezone {
    private BigDecimal dvalue;
    private Integer ivalue;
    private String label;

    /**
     * Default constructor, the d and i values are still null and the label is
     * an empty string
     */
    public Timezone() {
        label = "";
    }
    
    /**
     * Constructor: sets the label and d value >:-)
     * The i value is still null
     * @param d
     * @param s 
     */
    public Timezone(BigDecimal d, String s) {
        this.dvalue = d;
        this.label = s;
    }
    
    /**
     * Constructor: sets the label and i value
     * The d value is still null
     * @param i
     * @param s 
     */
    public Timezone(Integer i, String s) {
        this.ivalue = i;
        this.label = s;
    }
    
    /**
     * Constructor: sets the label, d and i value
     * @param d
     * @param i
     * @param s 
     */
    public Timezone(BigDecimal d, Integer i, String s) {
        this.dvalue = d;
        this.ivalue = i;
        this.label = s;
    }

    Timezone(double d, String string) {
        this.dvalue = BigDecimal.valueOf(d);
        this.label = string;
    }
    
    /**
     * @return the dvalue
     */
    public BigDecimal getDvalue() {
        return dvalue;
    }

    /**
     * @param dvalue the dvalue to set
     */
    public void setDvalue(BigDecimal dvalue) {
        this.dvalue = dvalue;
    }

    /**
     * @return the ivalue
     */
    public Integer getIvalue() {
        return ivalue;
    }

    /**
     * @param ivalue the ivalue to set
     */
    public void setIvalue(Integer ivalue) {
        this.ivalue = ivalue;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
