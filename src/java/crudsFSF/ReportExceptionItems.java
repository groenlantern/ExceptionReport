/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crudsFSF;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Jean-Pierre
 */
public class ReportExceptionItems implements Serializable {
    public String reportExceptionDescription = "";
    public String reportExceptionMerchant = "";
    public String reportExceptionCategory = "";
    public String reportExceptionTUI = "";
    public String reportExceptionRRN = "";
    public long   reportExceptionTxnExId;
    public String severity;
    private int    severityNumeric;
    
    public Timestamp doneWhen;    

    public long      transactionid;
    public String    processingcode;
    public String    transactionstate;
    public String    primaryaccountnumber;
    public String    transactionamount;
    public String    responsecode;
    public String    secondaryrespcode;
    public String    productid;
    public String    productstatus;

    public long getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(long transactionid) {
        this.transactionid = transactionid;
    }

    public String getProcessingcode() {
        return processingcode;
    }

    public void setProcessingcode(String processingcode) {
        this.processingcode = processingcode;
    }

    public String getTransactionstate() {
        return transactionstate;
    }

    public void setTransactionstate(String transactionstate) {
        this.transactionstate = transactionstate;
    }

    public String getPrimaryaccountnumber() {
        return primaryaccountnumber;
    }

    public void setPrimaryaccountnumber(String primaryaccountnumber) {
        this.primaryaccountnumber = primaryaccountnumber;
    }

    public String getTransactionamount() {
        return transactionamount;
    }

    public void setTransactionamount(String transactionamount) {
        this.transactionamount = transactionamount;
    }

    public String getResponsecode() {
        return responsecode;
    }

    public void setResponsecode(String responsecode) {
        this.responsecode = responsecode;
    }

    public String getSecondaryrespcode() {
        return secondaryrespcode;
    }

    public void setSecondaryrespcode(String secondaryrespcode) {
        this.secondaryrespcode = secondaryrespcode;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductstatus() {
        return productstatus;
    }

    public void setProductstatus(String productstatus) {
        this.productstatus = productstatus;
    }
    
    /**
     * @return the reportExceptionDescription
     */
    public String getReportExceptionDescription() {
        return reportExceptionDescription;
    }

    /**
     * @param reportExceptionDescription the reportExceptionDescription to set
     */
    public void setReportExceptionDescription(String reportExceptionDescription) {
        this.reportExceptionDescription = reportExceptionDescription;
    }

    /**
     * @return the reportExceptionMerchant
     */
    public String getReportExceptionMerchant() {
        return reportExceptionMerchant;
    }

    /**
     * @param reportExceptionMerchant the reportExceptionMerchant to set
     */
    public void setReportExceptionMerchant(String reportExceptionMerchant) {
        this.reportExceptionMerchant = reportExceptionMerchant;
    }

    /**
     * @return the reportExceptionCategory
     */
    public String getReportExceptionCategory() {
        return reportExceptionCategory;
    }

    /**
     * @param reportExceptionCategory the reportExceptionCategory to set
     */
    public void setReportExceptionCategory(String reportExceptionCategory) {
        this.reportExceptionCategory = reportExceptionCategory;
    }

    /**
     * @return the reportExceptionTUI
     */
    public String getReportExceptionTUI() {
        return reportExceptionTUI;
    }

    /**
     * @param reportExceptionTUI the reportExceptionTUI to set
     */
    public void setReportExceptionTUI(String reportExceptionTUI) {
        this.reportExceptionTUI = reportExceptionTUI;
    }

    /**
     * @return the reportExceptionRRN
     */
    public String getReportExceptionRRN() {
        return reportExceptionRRN;
    }

    /**
     * @param reportExceptionRRN the reportExceptionRRN to set
     */
    public void setReportExceptionRRN(String reportExceptionRRN) {
        this.reportExceptionRRN = reportExceptionRRN;
    }

    /**
     * @return the reportExceptionTxnExId
     */
    public long getReportExceptionTxnExId() {
        return reportExceptionTxnExId;
    }

    /**
     * @param reportExceptionTxnExId the reportExceptionTxnExId to set
     */
    public void setReportExceptionTxnExId(long reportExceptionTxnExId) {
        this.reportExceptionTxnExId = reportExceptionTxnExId;
    }

    /**
     * @return the severity
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * @param severity the severity to set
     */
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    /**
     * @return the doneWhen
     */
    public Timestamp getDoneWhen() {
        return doneWhen;
    }

    /**
     * @param doneWhen the doneWhen to set
     */
    public void setDoneWhen(Timestamp doneWhen) {
        this.doneWhen = doneWhen;
    }

    /**
     * @return the severityNumeric
     */
    public int getSeverityNumeric() {
        return Integer.parseInt(severity);
    }

    /**
     * @param severityNumeric the severityNumeric to set
     */
    public void setSeverityNumeric(int severityNumeric) {
        this.severityNumeric = severityNumeric;
    }

}
