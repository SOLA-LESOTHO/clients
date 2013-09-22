/**
 * ******************************************************************************************
 * Copyright (c) 2013 Food and Agriculture Organization of the United Nations
 * (FAO) and the Lesotho Land Administration Authority (LAA). All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the names of FAO, the LAA nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.clients.swing.desktop.administrative;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import javax.swing.JFormattedTextField;
import net.sf.jasperreports.engine.JasperPrint;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.administrative.ConsentBean;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.desktop.party.PartyListExtPanel;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.renderers.FormattersFactory;
import org.sola.clients.swing.ui.reports.FreeTextDialog;
import org.sola.clients.swing.ui.reports.ReportViewerForm;
import org.sola.common.DateUtility;
import org.sola.common.WindowUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Allows to create, save and generate consent letters
 */
public class ConsentPanel extends ContentPanel {

    private ApplicationBean appBean;
    private ApplicationServiceBean appService;
    public static final String UPDATED_RRR = "updatedRRR";

    private PartyListExtPanel createPartyListPanel(String role) {
        if (role.equals("rightholder")) {
            return new PartyListExtPanel(consentBean1.getRightHolderList());
        } else {
            return new PartyListExtPanel(consentBean1.getTransfereeList());
        }
    }

    private ConsentBean createConsentBean(){
        if(consentBean1 == null){
            return new ConsentBean();
        }
        return consentBean1;
    }
    
    /**
     * Creates new form ConsentPanel
     */
    public ConsentPanel(ConsentBean consentBean, BaUnitBean baUnitBean, 
            ApplicationBean appBean, ApplicationServiceBean appService) {
        this.appBean = appBean;
        this.appService = appService;

        prepareConsentBean(consentBean, baUnitBean);
        initComponents();
        postInit();
    }

    private void postInit() {
        customizeForm();
        saveState();
    }

    private void customizeForm() {
        headerPanel2.setTitleText(appService.getRequestType().getDisplayValue() 
                + ": " + consentBean1.getLeaseNumber());
        boolean enabled = false;
        if (consentBean1.getParcelAddress() == null || consentBean1.getParcelAddress().trim().isEmpty()) {
            enabled = true;
        }
        txtPropertyLocation.setEnabled(enabled);
    }

    private void prepareConsentBean(ConsentBean consentBean, BaUnitBean baUnit) {
        if (consentBean == null || consentBean.isNew()) {
            consentBean1 = new ConsentBean();
            consentBean1.setRegistrationDate(new Date());
            consentBean1.calculateExpirationDate(new Date());
        } else {
            consentBean1 = consentBean;
        }
        
        consentBean1.setBaUnit(baUnit);
        consentBean1.setLeaseNumber(baUnit.getName());

        consentBean1.setRightholderRrr(baUnit.getPrimaryRight());
        consentBean1.setRightHolderList(consentBean1.getRightholderRrr().getRightHolderList());
        consentBean1.setServiceFee(appService.getBaseFee());
        consentBean1.setReceiptNumber(appBean.getReceiptRef());
        consentBean1.setReceiptDate(appBean.getReceiptDate());
        consentBean1.setLodgingDate(appBean.getLodgingDatetime());

        if (consentBean1.getBaUnit().getCadastreObject() != null) {
            String address = consentBean1.getBaUnit().getCadastreObject().getAddressString();
            consentBean1.setParcelAddress(address);
        }
    }

    private void saveState() {
        MainForm.saveBeanState(consentBean1);
    }

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    private void printConsentCertificate() {
        if (consentBean1.validate(true).size() > 0) {
            return;
        }
        showReport(ReportManager.getConsentReport(consentBean1));
    }

    private void printConsentRejectionLetter() {
        if (consentBean1 != null) {
            // Show free text form
            FreeTextDialog form = new FreeTextDialog(
                    MessageUtility.getLocalizedMessageText(ClientMessage.GENERAL_LABELS_REJECTION_REASON),
                    null, MainForm.getInstance(), true);
            WindowUtility.centerForm(form);
            final String[] freeText = {""};

            form.addPropertyChangeListener(new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(FreeTextDialog.TEXT_TO_SAVE)) {
                        freeText[0] = ((String) evt.getNewValue());
                    }
                }
            });
            form.setVisible(true);
            if(freeText[0] == null || freeText[0] == ""){
                freeText[0] = "[NONE]";
            }
            showReport(ReportManager.getConsentRejectionReport(consentBean1, appBean, freeText[0]));
        }
    }

    /**
     * Opens {@link ReportViewerForm} to display report.
     */
    private void showReport(JasperPrint report) {
        ReportViewerForm form = new ReportViewerForm(report);
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }

    @Override
    protected boolean panelClosing() {
        if (btnSave.isEnabled() && MainForm.checkSaveBeforeClose(consentBean1)) {
            save(true);
            return false;
        }
        return true;
    }

    private void save(final boolean close) {
        if (consentBean1.validate(true).size() > 0) {
            return;
        }
        
        SolaTask<Void, Void> t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));
                consentBean1.save(appService.getId());
                saveState();
                return null;
            }

            @Override
            protected void taskDone() {
                if (close) {
                    close();
                } else {
                    MessageUtility.displayMessage(ClientMessage.CONSENT_SAVED);
                }
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        consentBean1 = createConsentBean();
        transactionTypeListBean1 = new org.sola.clients.beans.referencedata.TransactionTypeListBean();
        headerPanel2 = new org.sola.clients.swing.ui.HeaderPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnSave = new org.sola.clients.swing.common.buttons.BtnSave();
        btnPrint = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnPrintRejection = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        groupPanel3 = new org.sola.clients.swing.ui.GroupPanel();
        rightHolderListPanel = createPartyListPanel("rightholder");
        jPanel12 = new javax.swing.JPanel();
        groupPanel2 = new org.sola.clients.swing.ui.GroupPanel();
        recipientListPanel = createPartyListPanel("recipient");
        jPanel7 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtLeaseNumber = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtRegistrationDate = new javax.swing.JFormattedTextField();
        btnSubmissionDateFrom = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtExpirationDate = new javax.swing.JFormattedTextField();
        btnSubmissionDateFrom1 = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txtTransactionType = new javax.swing.JComboBox();
        jPanel15 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtRent = new javax.swing.JFormattedTextField();
        jPanel14 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtConditionText = new javax.swing.JTextArea();
        jPanel13 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtPropertyLocation = new javax.swing.JTextArea();

        setHeaderPanel(headerPanel2);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setAlignmentX(0.0F);

        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSave);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/print.png"))); // NOI18N
        btnPrint.setText("Print Certificate");
        btnPrint.setFocusable(false);
        btnPrint.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrint);
        jToolBar1.add(jSeparator1);

        btnPrintRejection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/print.png"))); // NOI18N
        btnPrintRejection.setText("Print Rejection Letter");
        btnPrintRejection.setFocusable(false);
        btnPrintRejection.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnPrintRejection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintRejectionActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrintRejection);

        jPanel1.setLayout(new java.awt.GridLayout(1, 2, 15, 15));

        jPanel11.setEnabled(false);
        jPanel11.setPreferredSize(new java.awt.Dimension(462, 125));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/administrative/Bundle"); // NOI18N
        groupPanel3.setTitleText(bundle.getString("SimpleOwhershipPanel.groupPanel1.titleText")); // NOI18N

        rightHolderListPanel.setReadOnly(true);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(rightHolderListPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(groupPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(groupPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rightHolderListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel11);

        groupPanel2.setTitleText("Transferee");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(groupPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE)
            .addComponent(recipientListPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(groupPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(recipientListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel12);

        jPanel7.setLayout(new java.awt.GridLayout(1, 5, 15, 15));

        jLabel1.setText("Lease Number");

        txtLeaseNumber.setEditable(false);
        txtLeaseNumber.setEnabled(false);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, consentBean1, org.jdesktop.beansbinding.ELProperty.create("${leaseNumber}"), txtLeaseNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtLeaseNumber)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 56, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLeaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel7.add(jPanel6);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel2.setText("Registration Date");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, consentBean1, org.jdesktop.beansbinding.ELProperty.create("${registrationDate}"), txtRegistrationDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnSubmissionDateFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnSubmissionDateFrom.setText(bundle.getString("LeasePanel.btnSubmissionDateFrom.text")); // NOI18N
        btnSubmissionDateFrom.setBorder(null);
        btnSubmissionDateFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmissionDateFromActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(txtRegistrationDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSubmissionDateFrom))
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 26, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSubmissionDateFrom)
                    .addComponent(txtRegistrationDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel7.add(jPanel8);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel4.setText("Expiration Date");

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, consentBean1, org.jdesktop.beansbinding.ELProperty.create("${expirationDate}"), txtExpirationDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnSubmissionDateFrom1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnSubmissionDateFrom1.setText(bundle.getString("LeasePanel.btnSubmissionDateFrom.text")); // NOI18N
        btnSubmissionDateFrom1.setBorder(null);
        btnSubmissionDateFrom1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubmissionDateFrom1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                    .addComponent(txtExpirationDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSubmissionDateFrom1))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSubmissionDateFrom1)
                    .addComponent(txtExpirationDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel7.add(jPanel10);

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel9.setText("Transaction Type");

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${transactionTypes}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, transactionTypeListBean1, eLProperty, txtTransactionType);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, consentBean1, org.jdesktop.beansbinding.ELProperty.create("${transactionType}"), txtTransactionType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(0, 27, Short.MAX_VALUE))
            .addComponent(txtTransactionType, 0, 0, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTransactionType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel7.add(jPanel16);

        jLabel5.setText("Consideration Amount");

        txtRent.setFormatterFactory(FormattersFactory.getInstance().getDecimalFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, consentBean1, org.jdesktop.beansbinding.ELProperty.create("${amount}"), txtRent, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(0, 18, Short.MAX_VALUE))
            .addComponent(txtRent)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel7.add(jPanel15);

        jLabel7.setText("Special Conditions");

        txtConditionText.setColumns(20);
        txtConditionText.setRows(5);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, consentBean1, org.jdesktop.beansbinding.ELProperty.create("${specialConditions}"), txtConditionText, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jScrollPane2.setViewportView(txtConditionText);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane2)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2))
        );

        jLabel3.setText("Property Location");

        txtPropertyLocation.setColumns(20);
        txtPropertyLocation.setRows(5);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, consentBean1, org.jdesktop.beansbinding.ELProperty.create("${parcelAddress}"), txtPropertyLocation, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(txtPropertyLocation);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        printConsentCertificate();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnPrintRejectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintRejectionActionPerformed
        printConsentRejectionLetter();
    }//GEN-LAST:event_btnPrintRejectionActionPerformed

    private void btnSubmissionDateFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmissionDateFromActionPerformed
        showCalendar(txtRegistrationDate);
    }//GEN-LAST:event_btnSubmissionDateFromActionPerformed

    private void btnSubmissionDateFrom1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmissionDateFrom1ActionPerformed
        showCalendar(txtExpirationDate);
    }//GEN-LAST:event_btnSubmissionDateFrom1ActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save(false);
    }//GEN-LAST:event_btnSaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnPrintRejection;
    private org.sola.clients.swing.common.buttons.BtnSave btnSave;
    private javax.swing.JButton btnSubmissionDateFrom;
    private javax.swing.JButton btnSubmissionDateFrom1;
    private org.sola.clients.beans.administrative.ConsentBean consentBean1;
    private org.sola.clients.swing.ui.GroupPanel groupPanel2;
    private org.sola.clients.swing.ui.GroupPanel groupPanel3;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private org.sola.clients.swing.desktop.party.PartyListExtPanel recipientListPanel;
    private org.sola.clients.swing.desktop.party.PartyListExtPanel rightHolderListPanel;
    private org.sola.clients.beans.referencedata.TransactionTypeListBean transactionTypeListBean1;
    private javax.swing.JTextArea txtConditionText;
    private javax.swing.JFormattedTextField txtExpirationDate;
    private javax.swing.JTextField txtLeaseNumber;
    private javax.swing.JTextArea txtPropertyLocation;
    private javax.swing.JFormattedTextField txtRegistrationDate;
    private javax.swing.JFormattedTextField txtRent;
    private javax.swing.JComboBox txtTransactionType;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
