/**
 * ******************************************************************************************
 * Copyright (c) 2013 Food and Agriculture Organization of the United Nations (FAO)
 * and the Lesotho Land Administration Authority (LAA). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the names of FAO, the LAA nor the names of its contributors may be used to
 *       endorse or promote products derived from this software without specific prior
 * 	  written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.clients.reports;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import org.apache.commons.lang.time.DateFormatUtils;
import org.sola.clients.beans.administrative.*;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.administrative.LeaseReportBean;
import org.sola.clients.beans.administrative.DisputeBean;
import org.sola.clients.beans.administrative.DisputeSearchResultBean;
import org.sola.clients.beans.administrative.RrrReportBean;
import org.sola.clients.beans.application.*;
import org.sola.clients.beans.cadastre.CadastreObjectBean;
import org.sola.clients.beans.system.BrReportBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.beans.system.BrListBean;
import org.sola.clients.beans.systematicregistration.*;
import org.sola.common.logging.LogUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Provides methods to generate and display various reports.
 */
public class ReportManager {

    /**
     * Generates and displays <b>Lodgement notice</b> report for the new
     * application.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getLodgementNoticeReport(ApplicationBean appBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("today", new Date());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        ApplicationBean[] beans = new ApplicationBean[1];
        beans[0] = appBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        inputParameters.put("IMAGE_SCRITTA_GREEN", ReportManager.class.getResourceAsStream("/images/sola/caption_green.png"));
        inputParameters.put("WHICH_CALLER", "N");

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/ApplicationPrintingForm.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Lodgement notice</b> report for the new
     * application.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getDisputeConfirmationReport(DisputeBean dispBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("LODGEMENTDATE",dispBean.getLodgementDate());
        inputParameters.put("DISPUTE_CATEGORY",dispBean.getDisputeCategory().getDisplayValue());
        inputParameters.put("DISPUTE_TYPE",dispBean.getDisputeType().getDisplayValue());
        DisputeBean[] beans = new DisputeBean[1];
        beans[0] = dispBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        inputParameters.put("IMAGE_SCRITTA_GREEN", ReportManager.class.getResourceAsStream("/images/sola/caption_green.png"));
        
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/DisputeConfirmation.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
    
     public static JasperPrint getDisputeMonthlyStatus(List<DisputeSearchResultBean> disputeSearchResultBean) {
        //List<DisputeBean> dispBeans
        HashMap inputParameters = new HashMap();
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        DisputeSearchResultBean[] beans =  disputeSearchResultBean.toArray(new DisputeSearchResultBean[0]); 
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        inputParameters.put("IMAGE_SCRITTA_GREEN", ReportManager.class.getResourceAsStream("/images/sola/caption_green.png"));
        
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/DisputeMonthlyStatus.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
     
     public static JasperPrint getDisputeMonthlyReport(List<DisputeSearchResultBean> disputeSearchResultBean,
             Date startDate,
             Date endDate,
             String numDisputes,
             String pendingDisputes,
             String completeDisputes,
             String sporadic,
             String regular,
             String unregistered,
             String numCourtCases,
             String pendingCourtCases,
             String completeCourtCases,
             String primaryRespond,
             String numPrimaryRespondPending) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("NUM_DISPUTES", numDisputes);
        inputParameters.put("PENDING_DISPUTES", pendingDisputes);
        inputParameters.put("COMPLETE_DISPUTES", completeDisputes);
        inputParameters.put("SPORADIC_DISPUTES", sporadic);
        inputParameters.put("REGULAR_DISPUTES", regular);
        inputParameters.put("UNREG_DISPUTES", unregistered);
        inputParameters.put("NUM_COURT_CASES", numCourtCases);
        inputParameters.put("PENDING_COURT", pendingCourtCases);
        inputParameters.put("COMPLETE_COURT", completeCourtCases);
        inputParameters.put("PRIMARY_RESPOND", primaryRespond);
        inputParameters.put("PRIMARY_RESPOND_PENDING", completeCourtCases);
        //inputParameters.put("USER", );
        DisputeSearchResultBean[] beans =  disputeSearchResultBean.toArray(new DisputeSearchResultBean[0]); 
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        inputParameters.put("IMAGE_SCRITTA_GREEN", ReportManager.class.getResourceAsStream("/images/sola/caption_green.png"));
        
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/DisputeMonthlyReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
     
     public static JasperPrint getDisputeStatisticsReport(List<DisputeSearchResultBean> disputeSearchResultBean,
             String startDate,
             String endDate,
             String numDisputes,
             String numCases,
             String averageDisputeDays,
             String averageCourtDays,
             String numPendingDisputes,
             String numPendingCourt,
             String numClosedDisputes,
             String numClosedCourt) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("START_DATE", startDate);
        inputParameters.put("END_DATE", endDate);
        inputParameters.put("NUM_DISPUTES", numDisputes);
        inputParameters.put("NUM_COURT", numCases);
        inputParameters.put("AVRG_DAYS_DISPUTES", averageDisputeDays);
        inputParameters.put("AVRG_DAYS_COURT", averageCourtDays);
        inputParameters.put("NUM_PENDING_DISPUTES", numPendingDisputes);
        inputParameters.put("NUM_PENDING_COURT", numPendingCourt);
        inputParameters.put("NUM_CLOSED_DISPUTES", numClosedDisputes);
        inputParameters.put("NUM_CLOSED_COURT", numClosedCourt);
        DisputeSearchResultBean[] beans =  disputeSearchResultBean.toArray(new DisputeSearchResultBean[0]); 
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        inputParameters.put("IMAGE_SCRITTA_GREEN", ReportManager.class.getResourceAsStream("/images/sola/caption_green.png"));
        
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/DisputeStatistics.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
    /**
     * Generates and displays <b>Application m report</b>.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getApplicationStatusReport(ApplicationBean appBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("today", new Date());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        ApplicationBean[] beans = new ApplicationBean[1];
        beans[0] = appBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/ApplicationStatusReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>BA Unit</b> report.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getBaUnitReport(BaUnitBean baUnitBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        BaUnitBean[] beans = new BaUnitBean[1];
        beans[0] = baUnitBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/BaUnitReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Lease rejection</b> report.
     *
     * @param reportBean RRR report bean containing all required information to
     * build the report.
     */
    public static JasperPrint getLeaseRejectionReport(LeaseReportBean reportBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        LeaseReportBean[] beans = new LeaseReportBean[1];
        beans[0] = reportBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/lease/LeaseRefuseLetter.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Lease offer</b> report.
     *
     * @param reportBean RRR report bean containing all required information to
     * build the report.
     */
    public static JasperPrint getLeaseOfferReport(LeaseReportBean reportBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        LeaseReportBean[] beans = new LeaseReportBean[1];
        beans[0] = reportBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/lease/LeaseOfferReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
         
    /**
     * Generates and displays <b>Lease</b> report.
     *
     * @param reportBean RRR report bean containing all required information to
     * build the report.
     */
    public static JasperPrint getLeaseReport(LeaseReportBean reportBean, String mapImageFileName, String reportName) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("MAP_IMAGE", mapImageFileName);
        LeaseReportBean[] beans = new LeaseReportBean[1];
        beans[0] = reportBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        String reportPath ="/reports/lease/" + reportName;
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream(reportPath),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
    
    /**
     * Generates and displays <b>Lease Surrender</b> report.
     *
     * @param reportBean RRR report bean containing all required information to
     * build the report.
     */
    public static JasperPrint getLeaseSurrenderReport(LeaseReportBean reportBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        LeaseReportBean[] beans = new LeaseReportBean[1];
        beans[0] = reportBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/lease/SurrenderLeaseReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
    
    /**
     * Generates and displays <b>Lease Vary</b> report.
     *
     * @param reportBean RRR report bean containing all required information to
     * build the report.
     */
    public static JasperPrint getLeaseVaryReport(LeaseReportBean reportBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        LeaseReportBean[] beans = new LeaseReportBean[1];
        beans[0] = reportBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/lease/VaryLeaseReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
    
    /**
     * Generates and displays <b>Lease Vary</b> report.
     *
     * @param reportBean RRR report bean containing all required information to
     * build the report.
     */
    public static JasperPrint getSuccessionReport(LeaseReportBean reportBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        LeaseReportBean[] beans = new LeaseReportBean[1];
        beans[0] = reportBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/lease/SuccessionReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }    

    /**
     * Generates and displays <b>Application payment receipt</b>.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getApplicationFeeReport(ApplicationBean appBean) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("today", new Date());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        ApplicationBean[] beans = new ApplicationBean[1];
        beans[0] = appBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        inputParameters.put("IMAGE_SCRITTA_GREEN", ReportManager.class.getResourceAsStream("/images/sola/caption_orange.png"));
        inputParameters.put("WHICH_CALLER", "R");

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/ApplicationPrintingForm.jasper"), inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Survey report</b>.
     *
     * @param co CadastreObjectBean containing data for the report.
     * @param appNumber Application number
     */
    public static JasperPrint getSurveyReport(CadastreObjectBean co, String appNumber, String applicantName) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("APP_NUMBER", appNumber);
        inputParameters.put("ROAD_CLASS", co.getRoadClassType().getTranslatedDisplayValue());
        inputParameters.put("VALUATION_ZONE", co.getLandGradeType().getTranslatedDisplayValue());
        inputParameters.put("APPLICANT_NAME", applicantName);
        CadastreObjectBean[] beans = new CadastreObjectBean[1];
        beans[0] = co;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/map/SurveyFormS10.jasper"), inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Consent Report</b>.
     *
     * @param appNumber Application number
     */
    public static JasperPrint getConsentReport(ConsentBean consentBean) {

        HashMap inputParameters = new HashMap();
        
        if(consentBean.getSubleaseRate() == null || consentBean.getSubleaseRate().equals("")) 
            inputParameters.put("SUBLEASE_RATE", "");
        else
            inputParameters.put("SUBLEASE_RATE", consentBean.getSubleaseRate());
                
        inputParameters.put("DUE_DATE", DateFormatUtils.format(consentBean.getExpirationDate(), "d MMMMM yyyy"));
        inputParameters.put("CONDITION_TEXT", consentBean.getSpecialConditions());
        
        inputParameters.put("CONSIDERATION_AMOUNT", consentBean.getAmountInWords());
        inputParameters.put("TRANSACTION_TYPE", consentBean.getTransactionTypeName());

        ConsentBean[] beans = new ConsentBean[1];        
        beans[0] = consentBean;
        
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/ConsentReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Consent offer</b> report.
     *
     * @param consentBean RRR report bean containing all required information to
     * build the report.
     */
    public static JasperPrint getConsentRejectionReport(ConsentBean consentBean, ApplicationBean appBean, String freeText) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName()); 
        inputParameters.put("APPLICANT_NAME", appBean.getContactPerson().getFullName());
        inputParameters.put("APPLICATION_NUMBER", appBean.getApplicationNumberFormatted());
        inputParameters.put("TRANSACTION_TYPE", consentBean.getTransactionTypeName());
        inputParameters.put("FREE_TEXT", freeText);
        
        ConsentBean[] beans = new ConsentBean[1];        
        beans[0] = consentBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/ConsentRejectionLetter.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }   
    
    /**
     * Generates and displays <b>BR Report</b>.
     */
    public static JasperPrint getBrReport() {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("today", new Date());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        BrListBean brList = new BrListBean();
        brList.FillBrs();
        int sizeBrList = brList.getBrBeanList().size();

        BrReportBean[] beans = new BrReportBean[sizeBrList];
        for (int i = 0; i < sizeBrList; i++) {
            beans[i] = brList.getBrBeanList().get(i);
            if (beans[i].getFeedback() != null) {
                String feedback = beans[i].getFeedback();
                if(feedback.indexOf("::::")>-1){
                    feedback = feedback.substring(0, feedback.indexOf("::::"));
                }
                beans[i].setFeedback(feedback);
            }

            if (i > 0) {
                String idPrev = beans[i - 1].getId();
                String technicalTypeCodePrev = beans[i - 1].getTechnicalTypeCode();
                String id = beans[i].getId();
                String technicalTypeCode = beans[i].getTechnicalTypeCode();


                if (id.equals(idPrev)
                        && technicalTypeCode.equals(technicalTypeCodePrev)) {

                    beans[i].setId("");
                    beans[i].setBody("");
                    beans[i].setDescription("");
                    beans[i].setFeedback("");
                    beans[i].setTechnicalTypeCode("");
                }
            }
        }

        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/BrReport.jasper"), inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>BR VAlidaction Report</b>.
     */
    public static JasperPrint getBrValidaction() {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("today", new Date());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getUserName());
        BrListBean brList = new BrListBean();
        brList.FillBrs();
        int sizeBrList = brList.getBrBeanList().size();
        BrReportBean[] beans = new BrReportBean[sizeBrList];
        for (int i = 0; i < sizeBrList; i++) {
            beans[i] = brList.getBrBeanList().get(i);

        }
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/BrValidaction.jasper"), inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>BA Unit</b> report.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getLodgementReport(ServiceReportBean lodgementBean, Date dateFrom, Date dateTo, String department) {
        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        inputParameters.put("DEPARTMENT", department);
        ServiceReportBean[] beans = new ServiceReportBean[1];
        beans[0] = lodgementBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/LodgementReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            LogUtility.log(LogUtility.getStackTraceAsString(ex), Level.SEVERE);
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
    
    /**
     * Generates and displays <b>BA Unit</b> report.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getLodgementReport2(LodgementBean lodgementBean, Date dateFrom, Date dateTo) {
        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        LodgementBean[] beans = new LodgementBean[1];
        beans[0] = lodgementBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/LodgementReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>SolaPrintReport</b> for the map.
     *
     * @param layoutId String This is the id of the report. It is used to
     * identify the report file.
     * @param dataBean Object containing data for the report. it can be replaced
     * with appropriate bean if needed
     * @param mapImageLocation String this is the location of the map to be
     * passed as MAP_IMAGE PARAMETER to the report. It is necessary for
     * visualizing the map
     * @param scalebarImageLocation String this is the location of the scalebar
     * to be passed as SCALE_IMAGE PARAMETER to the report. It is necessary for
     * visualizing the scalebar
     */
    public static JasperPrint getSolaPrintReport(String layoutId, Object dataBean,
            String mapImageLocation, String scalebarImageLocation) throws IOException {

        // Image Location of the north-arrow image
        String navigatorImage = "/images/sola/north-arrow.png";
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("MAP_IMAGE", mapImageLocation);
        inputParameters.put("SCALE_IMAGE", scalebarImageLocation);
        inputParameters.put("NAVIGATOR_IMAGE",
                ReportManager.class.getResourceAsStream(navigatorImage));
        inputParameters.put("LAYOUT", layoutId);
        inputParameters.put("INPUT_DATE",
                DateFormat.getInstance().format(Calendar.getInstance().getTime()));


        //This will be the bean containing data for the report. 
        //it is the data source for the report
        //it must be replaced with appropriate bean if needed
        Object[] beans = new Object[1];
        beans[0] = dataBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        // this generates the report. 
        // NOTICE THAT THE NAMING CONVENTION IS TO PRECEED "SolaPrintReport.jasper"
        // WITH THE LAYOUT NAME. SO IT MUST BE PRESENT ONE REPORT FOR EACH LAYOUT FORMAT
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream(
                    "/reports/map/" + layoutId + ".jasper"), inputParameters, jds);
            return jasperPrint;
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    public static JasperPrint getMapPublicDisplayReport(
            String layoutId, String areaDescription, String notificationPeriod,
            String mapImageLocation, String scalebarImageLocation) throws IOException {

        // Image Location of the north-arrow image
        String navigatorImage = "/images/sola/north-arrow.png";
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER_NAME", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("MAP_IMAGE", mapImageLocation);
        inputParameters.put("SCALE_IMAGE", scalebarImageLocation);
        inputParameters.put("NAVIGATOR_IMAGE",
                ReportManager.class.getResourceAsStream(navigatorImage));
        inputParameters.put("LAYOUT", layoutId);
        inputParameters.put("INPUT_DATE",
                DateFormat.getInstance().format(Calendar.getInstance().getTime()));
        inputParameters.put("AREA_DESCRIPTION", areaDescription);
        inputParameters.put("PERIOD_DESCRIPTION", notificationPeriod);


        //This will be the bean containing data for the report. 
        //it is the data source for the report
        //it must be replaced with appropriate bean if needed
        Object[] beans = new Object[1];
        beans[0] = new Object();
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream(
                    "/reports/map/" + layoutId + ".jasper"), inputParameters, jds);
            return jasperPrint;
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Systematic registration Public display
     * report</b>.
     *
     * @param parcelnumberList List Parcel list bean containing data for the
     * report.
     *
     */
    public static JasperPrint getSysRegPubDisParcelNameReport(ParcelNumberListingListBean parcelnumberList,
            Date dateFrom, Date dateTo, String location, String subReport) {
        HashMap inputParameters = new HashMap();
//	Date currentdate = new Date(System.currentTimeMillis());
//        inputParameters.put("CURRENT_DATE", currentdate);
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROM_DATE", dateFrom);
        inputParameters.put("TO_DATE", dateTo);
        inputParameters.put("LOCATION", location);
        inputParameters.put("SUB_REPORT", subReport);
        ParcelNumberListingListBean[] beans = new ParcelNumberListingListBean[1];
        beans[0] = parcelnumberList;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegPubDisParcelName.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Systematic registration Public display
     * report</b>.
     *
     * @param ownernameList List Parcel list bean containing data for the
     * report.
     *
     */
    public static JasperPrint getSysRegPubDisOwnerNameReport(OwnerNameListingListBean ownernameList,
            Date dateFrom, Date dateTo, String location, String subReport) {
        HashMap inputParameters = new HashMap();
//	Date currentdate = new Date(System.currentTimeMillis());
//        inputParameters.put("CURRENT_DATE", currentdate);
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROM_DATE", dateFrom);
        inputParameters.put("TO_DATE", dateTo);
        inputParameters.put("LOCATION", location);
        inputParameters.put("SUB_REPORT", subReport);
        OwnerNameListingListBean[] beans = new OwnerNameListingListBean[1];
        beans[0] = ownernameList;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegPubDisOwners.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Systematic registration Public display
     * report</b>.
     *
     * @param ownernameList List Parcel list bean containing data for the
     * report.
     *
     */
    public static JasperPrint getSysRegPubDisStateLandReport(StateLandListingListBean statelandList,
            Date dateFrom, Date dateTo, String location, String subReport) {
        HashMap inputParameters = new HashMap();
//	Date currentdate = new Date(System.currentTimeMillis());
//        inputParameters.put("CURRENT_DATE", currentdate);
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROM_DATE", dateFrom);
        inputParameters.put("TO_DATE", dateTo);
        inputParameters.put("LOCATION", location);
        inputParameters.put("SUB_REPORT", subReport);
        StateLandListingListBean[] beans = new StateLandListingListBean[1];
        beans[0] = statelandList;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegPubDisStateLand.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>Systematic registration Certificates
     * report</b>.
     *
     * @param certificatesList List Parcel list bean containing data for the
     * report.
     *
     */
    public static JasperPrint getSysRegCertificatesReport(BaUnitBean baUnitBean, String location) {
        HashMap inputParameters = new HashMap();
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());
        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("LOCATION", location);
        inputParameters.put("AREA", location);
        BaUnitBean[] beans = new BaUnitBean[1];
        beans[0] = baUnitBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);

        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegCertificates.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    /**
     * Generates and displays <b>BA Unit</b> report.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getSysRegManagementReport(SysRegManagementBean managementBean, Date dateFrom, Date dateTo, String nameLastpart) {
        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        inputParameters.put("AREA", nameLastpart);
        SysRegManagementBean[] beans = new SysRegManagementBean[1];
        beans[0] = managementBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegMenagement.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

//      /**
//     * Generates and displays <b>Sys Reg Status</b> report.
//     *
//     * @param appBean Application bean containing data for the report.
//     */
    public static JasperPrint getSysRegStatusReport(SysRegStatusBean statusBean, Date dateFrom, Date dateTo, String nameLastpart) {

        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        inputParameters.put("AREA", nameLastpart);
        SysRegStatusBean[] beans = new SysRegStatusBean[1];
        beans[0] = statusBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegStatus.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }

    //      /**
//     * Generates and displays <b>Sys Reg Progress</b> report.
//     *
//     * @param appBean Application bean containing data for the report.
//     */
    public static JasperPrint getSysRegProgressReport(SysRegProgressBean progressBean, Date dateFrom, Date dateTo, String nameLastpart) {

        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        inputParameters.put("AREA", nameLastpart);
        SysRegProgressBean[] beans = new SysRegProgressBean[1];
        beans[0] = progressBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/SysRegProgress.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
    
    /**
     * Generates and displays <b>BA Unit</b> report.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getResponseTimeReport(ResponseListBean responseBean, Date dateFrom, Date dateTo, String department) {
        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        inputParameters.put("DEPARTMENT", department);
        ResponseListBean[] beans = new ResponseListBean[1];
        beans[0] = responseBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/ResponseTimeReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            LogUtility.log(LogUtility.getStackTraceAsString(ex), Level.SEVERE);
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }  
    
    /**
     * Generates and displays <b>BA Unit</b> report.
     *
     * @param appBean Application bean containing data for the report.
     */
    public static JasperPrint getApplicationStagesReport(ApplicationStagesViewListBean appStagesBean, Date dateFrom, Date dateTo, String department) {
        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        inputParameters.put("DEPARTMENT", department);
        ApplicationStagesViewListBean[] beans = new ApplicationStagesViewListBean[1];
        beans[0] = appStagesBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/ApplicationStagesReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            LogUtility.log(LogUtility.getStackTraceAsString(ex), Level.SEVERE);
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }  
    
    public static JasperPrint getLeaseServicesReport(LeaseServicesViewListBean mortgageBean, Date dateFrom, Date dateTo) {
        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        LeaseServicesViewListBean[] beans = new LeaseServicesViewListBean[1];
        beans[0] = mortgageBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/LeasingServicesReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            LogUtility.log(LogUtility.getStackTraceAsString(ex), Level.SEVERE);
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
    
    public static JasperPrint getCustomerServicesReport(CustomerServicesViewListBean mortgageBean, Date dateFrom, Date dateTo) {
        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        CustomerServicesViewListBean[] beans = new CustomerServicesViewListBean[1];
        beans[0] = mortgageBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/CustomerServicesReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            LogUtility.log(LogUtility.getStackTraceAsString(ex), Level.SEVERE);
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
    
    public static JasperPrint getLeaseTransfersReport(LeaseTransfersListBean leaseTransfersBean, Date dateFrom, Date dateTo) {
        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        LeaseTransfersListBean[] beans = new LeaseTransfersListBean[1];
        beans[0] = leaseTransfersBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/LeaseTransfersReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            LogUtility.log(LogUtility.getStackTraceAsString(ex), Level.SEVERE);
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    }
    
     /**
     * Generates and displays <b>Monthly Statistics</b> report.
     *
     * @param statistical View List bean containing data for the report.
     */
    public static JasperPrint getStatisticalReport(StatisticalReportBean statisticalViewListBean, Date dateFrom, Date dateTo, String department) {
        HashMap inputParameters = new HashMap();
        Date currentdate = new Date(System.currentTimeMillis());
        inputParameters.put("REPORT_LOCALE", Locale.getDefault());

        inputParameters.put("CURRENT_DATE", currentdate);

        inputParameters.put("USER", SecurityBean.getCurrentUser().getFullUserName());
        inputParameters.put("FROMDATE", dateFrom);
        inputParameters.put("TODATE", dateTo);
        inputParameters.put("DEPARTMENT", department);
        StatisticalReportBean[] beans = new StatisticalReportBean[1];
        beans[0] = statisticalViewListBean;
        JRDataSource jds = new JRBeanArrayDataSource(beans);
        try {
            return JasperFillManager.fillReport(
                    ReportManager.class.getResourceAsStream("/reports/StatisticalReport.jasper"),
                    inputParameters, jds);
        } catch (JRException ex) {
            LogUtility.log(LogUtility.getStackTraceAsString(ex), Level.SEVERE);
            MessageUtility.displayMessage(ClientMessage.REPORT_GENERATION_FAILED,
                    new Object[]{ex.getLocalizedMessage()});
            return null;
        }
    } 
    
}
