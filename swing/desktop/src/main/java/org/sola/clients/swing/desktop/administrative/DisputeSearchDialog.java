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
package org.sola.clients.swing.desktop.administrative;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JFormattedTextField;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.clients.beans.administrative.DisputeSearchResultBean;
import org.sola.clients.beans.administrative.DisputeSearchResultListBean;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.reports.ReportManager;
import java.util.Date;
import net.sf.jasperreports.engine.JasperPrint;
import org.sola.clients.swing.ui.reports.ReportViewerForm;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.common.DateUtility;

public class DisputeSearchDialog extends javax.swing.JDialog {
   
    public static final String SELECTED_DISPUTE_SEARCH_RESULT = "selectedDisputeSearchResult";
    private String typeofCase;
    private ObservableList<DisputeSearchResultBean> printingBean;
    private String dateFromStr;
    private String dateToStr;
    private Date startDate;
    private Date endDate;
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");//public final static String SELECTED_DISPUTE = "selectedDispute";

    //private List<DisputeSearchResultBean> disputes;
    /**
     * Creates new form DisputeSearchDialog
     */
    
     public DisputeSearchDialog() {
        initComponents();
    }
    public DisputeSearchDialog(ObservableList<DisputeSearchResultBean> disputes,
            java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.printingBean = disputes;
        initComponents();
 
        disputeSearchResultList.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(DisputeSearchResultListBean.SELECTED_DISPUTE_SEARCH_RESULT_PROPERTY)) {
                    firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                }
            }
        });
    }

    public DisputeSearchResultBean getSelectedSearchResult() {
        return disputeSearchResultList.getSelectedDisputeSearchResult();
    }

    private void clearSearchResults() {
        disputeSearchParams.setNr(null);
        disputeSearchParams.setPlotNumber(null);
        disputeSearchParams.setLeaseNumber(null);
        disputeSearchParams.setLodgementDateFrom(null);
        disputeSearchParams.setLodgementDateTo(null);
        disputeSearchParams.setCompletionDateFrom(null);
        disputeSearchParams.setCompletionDateTo(null);
        disputeSearchResultList.getDisputeSearchResults().clear();
        btnCourtProcess.setSelected(false);
        btnDisputeMode.setSelected(false);
        disputeSearchParams.setCaseType(null);
        lblSearchResultCount.setText("0");
        dbxReportsList.setSelectedIndex(-1);
    }

    private void fireEvent(String eventName) {
        if (disputeSearchResultList.getSelectedDisputeSearchResult() != null) {
            firePropertyChange(eventName, null, disputeSearchResultList.getSelectedDisputeSearchResult());
            this.setVisible(false);
        }
    }
    
     private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }
     
        private void switchModeRole(boolean isDispute) {
        if (isDispute) {
            btnCourtProcess.setSelected(false);
            typeofCase = "Dispute";
            disputeSearchParams.setCaseType(typeofCase);

        } else {

            btnDisputeMode.setSelected(false);
            typeofCase = "Court Process";
            disputeSearchParams.setCaseType(typeofCase);
        }
    }
            private int dateDiff(Date startDate, Date endDate) {
        int numDays = 0;

        Long startDateLong = startDate.getTime();
        Long endDateLong = endDate.getTime();

        if (startDate != null && endDate != null) {
            numDays = DateUtility.getDaysDiff(startDateLong, endDateLong);
        } else {
            numDays = 0;
        }
        return numDays;
    }

    private void printReports() {
        Date dateFrom = disputeSearchParams.getLodgementDateFrom();
        Date dateTo = disputeSearchParams.getLodgementDateTo();
        int test = 0;


        if (dateFrom != null) {
            dateFromStr = df.format(dateFrom);
        } else {
            dateFromStr = "";
        }

        if (dateTo != null) {
            dateToStr = df.format(dateTo);
        } else {
            dateToStr = "";
        }


        if (dbxReportsList.getSelectedIndex() == 0) {
            showReport(ReportManager.getDisputeConfirmationReport(disputeSearchResultList.getSelectedDisputeSearchResult()));
        } else if (dbxReportsList.getSelectedIndex() == 1) {
            printStatistical();
        } else if (dbxReportsList.getSelectedIndex() == 2) {
            printMonthlyReport();
        }
//        ArrayList<DisputeSearchResultBean> list = new ArrayList<DisputeSearchResultBean>();
//        list.add(printingBean); 

        //showReport(ReportManager.getDisputeMonthlyStatus(printingBean));
//       if  (typeofCase != null) {
//           showReport(ReportManager.getDisputeMonthlyReport(printingBean, typeofCase));
//       }
//       
    }

    private void showReport(JasperPrint report) {
        ReportViewerForm form = new ReportViewerForm(report);
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }

    private void printStatistical() {
        int sizePrBeanList = printingBean.size();
        int numDisputes = 0;
        int numDays = 0;
        int averageDays = 0;

        for (int i = 0; i < sizePrBeanList; i++) {
            if (printingBean.get(i).getCaseType().equals("Dispute")) {
                numDisputes = numDisputes + 1;

                if (printingBean.get(i).getCompletiondate() == null) {
                    numDays = numDays + (dateDiff(printingBean.get(i).getLodgementDate(),
                            DateUtility.now()));
                } else if (printingBean.get(i).getCompletiondate() != null) {
                    numDays = numDays + (dateDiff(printingBean.get(i).getLodgementDate(),
                            printingBean.get(i).getCompletiondate()));
                }
            }
        }

        averageDays =  numDays / numDisputes;

        showReport(ReportManager.getDisputeStatisticsReport(printingBean,
                dateFromStr,
                dateToStr,
                Integer.toString(numDisputes),
                Integer.toString(averageDays)));
    }

    private void printMonthlyReport() {

        int sizePrBeanList = printingBean.size();
        int numDisputes = 0;
        String numDisputesString = null;
        int numCourtCases = 0;
        int pendingDisputes = 0;
        int completeDisputes = 0;
        int pendingCourtCases = 0;
        int completeCourtCases = 0;
        int numPrimaryRespond = 0;
        int sporadic = 0;
        int regular = 0;
        int unregistered = 0;
        int numPrimaryRespondPending = 0;

        for (int i = 0; i < sizePrBeanList; i++) {
            if (printingBean.get(i).getCaseType().equals("Dispute")) {
                numDisputes = numDisputes + 1;

                if (printingBean.get(i).getStatusCode().equals("pending")) {
                    pendingDisputes = pendingDisputes + 1;
                }
                if (printingBean.get(i).getStatusCode().equals("resolved")) {
                    completeDisputes = completeDisputes + 1;
                }
                if (printingBean.get(i).getDisputeCategoryCode().equals("sporadic")) {
                    sporadic = sporadic + 1;
                }
                if (printingBean.get(i).getDisputeCategoryCode().equals("regularization")) {
                    regular = regular + 1;
                }
                if (printingBean.get(i).getDisputeCategoryCode().equals("unregistered")) {
                    unregistered = unregistered + 1;
                }


            }
            if (printingBean.get(i).getCaseType().equals("Court Process")) {
                numCourtCases = numCourtCases + 1;

                if (printingBean.get(i).getStatusCode().equals("pending")) {
                    pendingCourtCases = pendingCourtCases + 1;
                }
                if (printingBean.get(i).getStatusCode().equals("resolved")) {
                    completeCourtCases = completeCourtCases + 1;
                }
                if (printingBean.get(i).isPrimaryRespondent() == true) {
                    numPrimaryRespond = numPrimaryRespond + 1;
                }
                if (printingBean.get(i).isPrimaryRespondent() == true && printingBean.get(i).getStatusCode().equals("pending")) {
                    numPrimaryRespondPending = numPrimaryRespondPending + 1;
                }

            }
        }
        showReport(ReportManager.getDisputeMonthlyReport(printingBean,
                disputeSearchParams.getLodgementDateFrom(),
                disputeSearchParams.getLodgementDateTo(),
                Integer.toString(numDisputes),
                Integer.toString(pendingDisputes),
                Integer.toString(completeDisputes),
                Integer.toString(sporadic),
                Integer.toString(regular),
                Integer.toString(unregistered),
                Integer.toString(numCourtCases),
                Integer.toString(pendingCourtCases),
                Integer.toString(completeCourtCases),
                Integer.toString(numPrimaryRespond),
                Integer.toString(numPrimaryRespondPending)));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        disputeSearchParams = new org.sola.clients.beans.administrative.DisputeSearchParamsBean();
        disputeSearchResultList = new org.sola.clients.beans.administrative.DisputeSearchResultListBean();
        disputeReportsList = new org.sola.clients.beans.referencedata.DisputeReportsListBean();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtdisputeNr = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtplotNr = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtleaseNr = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        btnClear = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtlodgementFrom = new javax.swing.JFormattedTextField();
        btnLodgementFrom = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtlodgementTo = new javax.swing.JFormattedTextField();
        btnlodgementTo = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtcompletionFrom = new javax.swing.JFormattedTextField();
        btncompletionFrom = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtcompletionTo = new javax.swing.JFormattedTextField();
        btncompletionTo = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        btnCourtProcess = new javax.swing.JRadioButton();
        btnDisputeMode = new javax.swing.JRadioButton();
        jPanel13 = new javax.swing.JPanel();
        dbxReportsList = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btnPrint = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnselectDispute = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton7 = new javax.swing.JButton();
        lblSearchResultCount = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/administrative/Bundle"); // NOI18N
        setName(bundle.getString("DisputeSearchDialog.name")); // NOI18N

        jPanel1.setName(bundle.getString("DisputeSearchDialog.jPanel1.name")); // NOI18N

        jPanel2.setName(bundle.getString("DisputeSearchDialog.jPanel2.name")); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(1, 3, 10, 5));

        jPanel5.setName(bundle.getString("DisputeSearchDialog.jPanel5.name")); // NOI18N

        jLabel1.setText(bundle.getString("DisputeSearchDialog.jLabel1.text")); // NOI18N
        jLabel1.setName(bundle.getString("DisputeSearchDialog.jLabel1.name")); // NOI18N

        txtdisputeNr.setName(bundle.getString("DisputeSearchDialog.txtdisputeNr.name")); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeSearchParams, org.jdesktop.beansbinding.ELProperty.create("${nr}"), txtdisputeNr, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtdisputeNr, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtdisputeNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel5);

        jPanel6.setName(bundle.getString("DisputeSearchDialog.jPanel6.name")); // NOI18N

        jLabel2.setText(bundle.getString("DisputeSearchDialog.jLabel2.text")); // NOI18N
        jLabel2.setName(bundle.getString("DisputeSearchDialog.jLabel2.name")); // NOI18N

        txtplotNr.setName(bundle.getString("DisputeSearchDialog.txtplotNr.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeSearchParams, org.jdesktop.beansbinding.ELProperty.create("${plotNumber}"), txtplotNr, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 158, Short.MAX_VALUE))
            .addComponent(txtplotNr, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtplotNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 15, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel6);

        jPanel7.setName(bundle.getString("DisputeSearchDialog.jPanel7.name")); // NOI18N

        jLabel3.setText(bundle.getString("DisputeSearchDialog.jLabel3.text")); // NOI18N
        jLabel3.setName(bundle.getString("DisputeSearchDialog.jLabel3.name")); // NOI18N

        txtleaseNr.setName(bundle.getString("DisputeSearchDialog.txtleaseNr.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeSearchParams, org.jdesktop.beansbinding.ELProperty.create("${leaseNumber}"), txtleaseNr, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 148, Short.MAX_VALUE))
            .addComponent(txtleaseNr)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addComponent(txtleaseNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.add(jPanel7);

        jPanel11.setName(bundle.getString("DisputeSearchDialog.jPanel11.name")); // NOI18N

        btnClear.setText(bundle.getString("DisputeSearchDialog.btnClear.text")); // NOI18N
        btnClear.setName(bundle.getString("DisputeSearchDialog.btnClear.name")); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnSearch.setText(bundle.getString("DisputeSearchDialog.btnSearch.text")); // NOI18N
        btnSearch.setName(bundle.getString("DisputeSearchDialog.btnSearch.name")); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(btnClear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );

        jPanel3.setName(bundle.getString("DisputeSearchDialog.jPanel3.name")); // NOI18N
        jPanel3.setLayout(new java.awt.GridLayout(2, 2, 5, 5));

        jPanel4.setName(bundle.getString("DisputeSearchDialog.jPanel4.name")); // NOI18N

        jLabel6.setText(bundle.getString("DisputeSearchDialog.jLabel6.text")); // NOI18N
        jLabel6.setName(bundle.getString("DisputeSearchDialog.jLabel6.name")); // NOI18N

        txtlodgementFrom.setName(bundle.getString("DisputeSearchDialog.txtlodgementFrom.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeSearchParams, org.jdesktop.beansbinding.ELProperty.create("${lodgementDateFrom}"), txtlodgementFrom, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnLodgementFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnLodgementFrom.setName(bundle.getString("DisputeSearchDialog.btnLodgementFrom.name")); // NOI18N
        btnLodgementFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLodgementFromActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addContainerGap(181, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(txtlodgementFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLodgementFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtlodgementFrom))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(btnLodgementFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel4);

        jPanel8.setName(bundle.getString("DisputeSearchDialog.jPanel8.name")); // NOI18N

        jLabel4.setText(bundle.getString("DisputeSearchDialog.jLabel4.text")); // NOI18N
        jLabel4.setName(bundle.getString("DisputeSearchDialog.jLabel4.name")); // NOI18N

        txtlodgementTo.setName(bundle.getString("DisputeSearchDialog.txtlodgementTo.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeSearchParams, org.jdesktop.beansbinding.ELProperty.create("${lodgementDateTo}"), txtlodgementTo, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnlodgementTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnlodgementTo.setName(bundle.getString("DisputeSearchDialog.btnlodgementTo.name")); // NOI18N
        btnlodgementTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlodgementToActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 150, Short.MAX_VALUE))
                    .addComponent(txtlodgementTo))
                .addGap(18, 18, 18)
                .addComponent(btnlodgementTo, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtlodgementTo))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(btnlodgementTo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel8);

        jPanel9.setName(bundle.getString("DisputeSearchDialog.jPanel9.name")); // NOI18N

        jLabel5.setText(bundle.getString("DisputeSearchDialog.jLabel5.text")); // NOI18N
        jLabel5.setName(bundle.getString("DisputeSearchDialog.jLabel5.name")); // NOI18N

        txtcompletionFrom.setName(bundle.getString("DisputeSearchDialog.txtcompletionFrom.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeSearchParams, org.jdesktop.beansbinding.ELProperty.create("${completionDateFrom}"), txtcompletionFrom, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btncompletionFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btncompletionFrom.setName(bundle.getString("DisputeSearchDialog.btncompletionFrom.name")); // NOI18N
        btncompletionFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncompletionFromActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 134, Short.MAX_VALUE))
                    .addComponent(txtcompletionFrom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btncompletionFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btncompletionFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcompletionFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel3.add(jPanel9);

        jPanel10.setName(bundle.getString("DisputeSearchDialog.jPanel10.name")); // NOI18N

        jLabel7.setText(bundle.getString("DisputeSearchDialog.jLabel7.text")); // NOI18N
        jLabel7.setName(bundle.getString("DisputeSearchDialog.jLabel7.name")); // NOI18N

        txtcompletionTo.setName(bundle.getString("DisputeSearchDialog.txtcompletionTo.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeSearchParams, org.jdesktop.beansbinding.ELProperty.create("${completionDateTo}"), txtcompletionTo, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btncompletionTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btncompletionTo.setName(bundle.getString("DisputeSearchDialog.btncompletionTo.name")); // NOI18N
        btncompletionTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncompletionToActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 148, Short.MAX_VALUE))
                    .addComponent(txtcompletionTo))
                .addGap(18, 18, 18)
                .addComponent(btncompletionTo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btncompletionTo, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcompletionTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel3.add(jPanel10);

        jPanel12.setName(bundle.getString("DisputeSearchDialog.jPanel12.name")); // NOI18N

        btnCourtProcess.setText(bundle.getString("DisputeSearchDialog.btnCourtProcess.text")); // NOI18N
        btnCourtProcess.setName(bundle.getString("DisputeSearchDialog.btnCourtProcess.name")); // NOI18N
        btnCourtProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCourtProcessActionPerformed(evt);
            }
        });

        btnDisputeMode.setText(bundle.getString("DisputeSearchDialog.btnDisputeMode.text")); // NOI18N
        btnDisputeMode.setName(bundle.getString("DisputeSearchDialog.btnDisputeMode.name")); // NOI18N
        btnDisputeMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDisputeModeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCourtProcess, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                    .addComponent(btnDisputeMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDisputeMode)
                .addGap(28, 28, 28)
                .addComponent(btnCourtProcess)
                .addGap(17, 17, 17))
        );

        jPanel13.setName(bundle.getString("DisputeSearchDialog.jPanel13.name")); // NOI18N

        dbxReportsList.setName(bundle.getString("DisputeSearchDialog.dbxReportsList.name")); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${disputeReportsListBean}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeReportsList, eLProperty, dbxReportsList);
        bindingGroup.addBinding(jComboBoxBinding);

        jLabel8.setText(bundle.getString("DisputeSearchDialog.jLabel8.text")); // NOI18N
        jLabel8.setName(bundle.getString("DisputeSearchDialog.jLabel8.name")); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dbxReportsList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dbxReportsList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jToolBar1.setBorder(null);
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName(bundle.getString("DisputeSearchDialog.jToolBar1.name")); // NOI18N

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/print.png"))); // NOI18N
        btnPrint.setText(bundle.getString("DisputeSearchDialog.btnPrint.text")); // NOI18N
        btnPrint.setFocusable(false);
        btnPrint.setName(bundle.getString("DisputeSearchDialog.btnPrint.name")); // NOI18N
        btnPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrint);

        jSeparator1.setName(bundle.getString("DisputeSearchDialog.jSeparator1.name")); // NOI18N
        jToolBar1.add(jSeparator1);

        btnselectDispute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/select.png"))); // NOI18N
        btnselectDispute.setText(bundle.getString("DisputeSearchDialog.btnselectDispute.text")); // NOI18N
        btnselectDispute.setFocusable(false);
        btnselectDispute.setName(bundle.getString("DisputeSearchDialog.btnselectDispute.name")); // NOI18N
        btnselectDispute.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnselectDispute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnselectDisputeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnselectDispute);

        jSeparator2.setName(bundle.getString("DisputeSearchDialog.jSeparator2.name")); // NOI18N
        jToolBar1.add(jSeparator2);

        jButton7.setText(bundle.getString("DisputeSearchDialog.jButton7.text")); // NOI18N
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setName(bundle.getString("DisputeSearchDialog.jButton7.name")); // NOI18N
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton7);

        lblSearchResultCount.setName(bundle.getString("DisputeSearchDialog.lblSearchResultCount.name")); // NOI18N
        jToolBar1.add(lblSearchResultCount);

        jScrollPane1.setName(bundle.getString("DisputeSearchDialog.jScrollPane1.name")); // NOI18N

        jTable1.setName(bundle.getString("DisputeSearchDialog.jTable1.name")); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${disputeSearchResults}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeSearchResultList, eLProperty, jTable1);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${nr}"));
        columnBinding.setColumnName("Nr");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${plotNumber}"));
        columnBinding.setColumnName("Plot Number");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${leaseNumber}"));
        columnBinding.setColumnName("Lease Number");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeSearchResultList, org.jdesktop.beansbinding.ELProperty.create("${selectedDisputeSearchResult}"), jTable1, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("DisputeSearchDialog.jTable1.columnModel.title0_1")); // NOI18N
        jTable1.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("DisputeSearchDialog.jTable1.columnModel.title1_1")); // NOI18N
        jTable1.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("DisputeSearchDialog.jTable1.columnModel.title2_1")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 12, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearSearchResults();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_PROPERTY_SEARCHING));
                disputeSearchResultList.search(disputeSearchParams);
                printingBean = disputeSearchResultList.getDisputeSearchResults();
                return null;
            }

            @Override
            public void taskDone() {
                lblSearchResultCount.setText(Integer.toString(disputeSearchResultList.getDisputeSearchResults().size()));
                if (disputeSearchResultList.getDisputeSearchResults().size() < 1) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_NO_RESULTS);
                } else if (disputeSearchResultList.getDisputeSearchResults().size() > 100) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_TOO_MANY_RESULTS, new String[]{"100"});
                }
            }
        };
        TaskManager.getInstance().runTask(t);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnLodgementFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLodgementFromActionPerformed
        showCalendar(txtlodgementFrom);
    }//GEN-LAST:event_btnLodgementFromActionPerformed

    private void btnlodgementToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlodgementToActionPerformed
        showCalendar(txtlodgementTo);
    }//GEN-LAST:event_btnlodgementToActionPerformed

    private void btncompletionFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncompletionFromActionPerformed
        showCalendar(txtcompletionFrom);
    }//GEN-LAST:event_btncompletionFromActionPerformed

    private void btncompletionToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncompletionToActionPerformed
        showCalendar(txtcompletionTo);
    }//GEN-LAST:event_btncompletionToActionPerformed

    private void btnDisputeModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDisputeModeActionPerformed
        switchModeRole(true);
    }//GEN-LAST:event_btnDisputeModeActionPerformed

    private void btnCourtProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCourtProcessActionPerformed
        switchModeRole(false);
    }//GEN-LAST:event_btnCourtProcessActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        printReports();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnselectDisputeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnselectDisputeActionPerformed
        fireEvent(SELECTED_DISPUTE_SEARCH_RESULT);
    }//GEN-LAST:event_btnselectDisputeActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JRadioButton btnCourtProcess;
    private javax.swing.JRadioButton btnDisputeMode;
    private javax.swing.JButton btnLodgementFrom;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btncompletionFrom;
    private javax.swing.JButton btncompletionTo;
    private javax.swing.JButton btnlodgementTo;
    private javax.swing.JButton btnselectDispute;
    private javax.swing.JComboBox dbxReportsList;
    private org.sola.clients.beans.referencedata.DisputeReportsListBean disputeReportsList;
    private org.sola.clients.beans.administrative.DisputeSearchParamsBean disputeSearchParams;
    private org.sola.clients.beans.administrative.DisputeSearchResultListBean disputeSearchResultList;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblSearchResultCount;
    private javax.swing.JFormattedTextField txtcompletionFrom;
    private javax.swing.JFormattedTextField txtcompletionTo;
    private javax.swing.JTextField txtdisputeNr;
    private javax.swing.JTextField txtleaseNr;
    private javax.swing.JFormattedTextField txtlodgementFrom;
    private javax.swing.JFormattedTextField txtlodgementTo;
    private javax.swing.JTextField txtplotNr;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
