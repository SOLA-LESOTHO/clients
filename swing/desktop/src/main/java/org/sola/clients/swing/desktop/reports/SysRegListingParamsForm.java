/*
 * Copyright 2012 Food and Agriculture Organization of the United Nations (FAO).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sola.clients.swing.desktop.reports;

import java.awt.ComponentOrientation;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.sola.clients.beans.digitalarchive.DocumentBean;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.desktop.ReportViewerForm;
import org.sola.clients.swing.desktop.source.DocumentForm;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.common.FileUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author RizzoM
 */
public class SysRegListingParamsForm extends javax.swing.JDialog {

    private String location;
    private String tmpLocation = "";
    private String report;
    private static String cachePath = System.getProperty("user.home") + "/sola/cache/documents/";

    /**
     * Creates new form SysRegListingParamsForm
     */
    public SysRegListingParamsForm(java.awt.Frame parent, boolean modal, String report) {
        super(parent, modal);
        initComponents();
        this.report = report;
        this.setTitle("Report " + report);
    }

    /**
     * Opens {@link ReportViewerForm} to display report.
     */
    private void showReport(JasperPrint report) {
        ReportViewerForm form = new ReportViewerForm(report);
        form.setVisible(true);
        form.setAlwaysOnTop(true);
        try {
            postProcessReport(report);
        } catch (Exception ex) {
            Logger.getLogger(SysRegListingParamsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
//        try {
//            saveDocument("");
//        } catch (Exception ex) {
//            Logger.getLogger(SysRegListingParamsForm.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }

    protected void postProcessReport(JasperPrint populatedReport) throws Exception {

        System.out.println("Inside postProcessReport");

        System.out.println("start download");
        Date currentdate = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyy");
        String reportdate = formatter.format(currentdate);
        Date recDate = (Date) txtFromDate.getValue();
        String location = this.tmpLocation.replace(" ", "_");
        String reportTogenerate = this.report + "_" +location + "_" + reportdate + ".pdf";

        JRPdfExporter exporterPdf = new JRPdfExporter();

        exporterPdf.setParameter(JRXlsExporterParameter.JASPER_PRINT, populatedReport);
        exporterPdf.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporterPdf.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
        //exporterPdf.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
        exporterPdf.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, cachePath + reportTogenerate);

        exporterPdf.exportReport();
        FileUtility.saveFileFromStream(null, reportTogenerate);

        System.out.println("End download");
        saveDocument(reportTogenerate, recDate, reportdate);
    }

    private void saveDocument(String fileName, Date recDate, String subDate) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        String reportdate = formatter.format(recDate);
        documentPanel.browseAttachment.setText(fileName);
        documentPanel.cbxDocType.setSelectedIndex(12);
        documentPanel.txtDocRefNumber.setText(reportdate);
        documentPanel.txtDocRecordDate.setText(reportdate);
        documentPanel.txtDocRecordDate.setValue(txtFromDate.getValue());

        DocumentBean document = new DocumentBean();
        File file = new File(cachePath + fileName);
        document = DocumentBean.createDocumentFromLocalFile(file);
        documentPanel.archiveDocument = document;

        documentPanel.btnOk.doClick();
        documentPanel.saveDocument();

    }

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        parcelNumberListingListBean = new org.sola.clients.beans.systematicregistration.ParcelNumberListingListBean();
        cadastreObjectBean = new org.sola.clients.beans.cadastre.CadastreObjectBean();
        ownerNameListingListBean = new org.sola.clients.beans.systematicregistration.OwnerNameListingListBean();
        stateLandListingListBean = new org.sola.clients.beans.systematicregistration.StateLandListingListBean();
        sourceBean = new org.sola.clients.beans.source.SourceBean();
        documentPanel = new org.sola.clients.swing.ui.source.DocumentPanel();
        labHeader = new javax.swing.JLabel();
        labNotificationFrom = new javax.swing.JLabel();
        txtFromDate = new javax.swing.JFormattedTextField();
        labNotificationTo = new javax.swing.JLabel();
        txtToDate = new javax.swing.JFormattedTextField();
        viewReport = new javax.swing.JButton();
        labLocation = new javax.swing.JLabel();
        btnShowCalendarFrom = new javax.swing.JButton();
        btnShowCalendarTo = new javax.swing.JButton();
        cadastreObjectSearch = new org.sola.clients.swing.ui.cadastre.LocationSearch();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        labHeader.setBackground(new java.awt.Color(255, 153, 0));
        labHeader.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        labHeader.setForeground(new java.awt.Color(255, 255, 255));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/reports/Bundle"); // NOI18N
        labHeader.setText(bundle.getString("SysRegListingParamsForm.labHeader.text")); // NOI18N
        labHeader.setOpaque(true);

        labNotificationFrom.setText(bundle.getString("SysRegListingParamsForm.labNotificationFrom.text")); // NOI18N

        txtFromDate.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtFromDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        txtFromDate.setToolTipText(bundle.getString("SysRegListingParamsForm.txtFromDate.toolTipText")); // NOI18N
        txtFromDate.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtFromDate.setHorizontalAlignment(JTextField.LEADING);

        labNotificationTo.setText(bundle.getString("SysRegListingParamsForm.labNotificationTo.text")); // NOI18N

        txtToDate.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtToDate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getDateInstance(java.text.DateFormat.SHORT))));
        txtToDate.setToolTipText(bundle.getString("SysRegListingParamsForm.txtToDate.toolTipText")); // NOI18N
        txtToDate.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtToDate.setHorizontalAlignment(JTextField.LEADING);

        viewReport.setText(bundle.getString("SysRegListingParamsForm.viewReport.text")); // NOI18N
        viewReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewReportActionPerformed(evt);
            }
        });

        labLocation.setText(bundle.getString("SysRegListingParamsForm.labLocation.text")); // NOI18N

        btnShowCalendarFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnShowCalendarFrom.setText(bundle.getString("SysRegListingParamsForm.btnShowCalendarFrom.text")); // NOI18N
        btnShowCalendarFrom.setBorder(null);
        btnShowCalendarFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCalendarFromActionPerformed(evt);
            }
        });

        btnShowCalendarTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnShowCalendarTo.setText(bundle.getString("SysRegListingParamsForm.btnShowCalendarTo.text")); // NOI18N
        btnShowCalendarTo.setBorder(null);
        btnShowCalendarTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCalendarToActionPerformed(evt);
            }
        });

        cadastreObjectSearch.setText(bundle.getString("SysRegListingParamsForm.cadastreObjectSearch.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(labHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(labLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(labNotificationFrom)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(txtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(btnShowCalendarFrom)))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(21, 21, 21)
                                            .addComponent(labNotificationTo))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addComponent(txtToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(cadastreObjectSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(viewReport)
                            .addComponent(btnShowCalendarTo))))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labHeader)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labNotificationFrom)
                    .addComponent(labNotificationTo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labLocation))
                    .addComponent(txtToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnShowCalendarTo)
                    .addComponent(btnShowCalendarFrom))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewReport)
                    .addComponent(cadastreObjectSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(152, Short.MAX_VALUE))
        );

        labHeader.getAccessibleContext().setAccessibleName(bundle.getString("SysRegListingParamsForm.labHeader.text")); // NOI18N
        labNotificationFrom.getAccessibleContext().setAccessibleName(bundle.getString("SysRegListingParamsForm.labNotificationFrom.text")); // NOI18N
        labNotificationTo.getAccessibleContext().setAccessibleName(bundle.getString("SysRegListingParamsForm.labNotificationTo.text")); // NOI18N
        viewReport.getAccessibleContext().setAccessibleName(bundle.getString("SysRegListingParamsForm.viewReport.text")); // NOI18N
        labLocation.getAccessibleContext().setAccessibleName(bundle.getString("SysRegListingParamsForm.labLocation.text")); // NOI18N

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void viewReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewReportActionPerformed
        boolean dateFilled = false;
        Date tmpFrom;
        Date tmpTo;
        String reportRequested = "subTitlePN";
        if (cadastreObjectSearch.getSelectedElement() != null) {
            this.location = cadastreObjectSearch.getSelectedElement().toString();
            tmpLocation = (this.location.substring(this.location.indexOf("/") + 1).trim());
        } else {
            MessageUtility.displayMessage(ClientMessage.CHECK_SELECT_LOCATION);
            return;
        }
        if (txtFromDate.getValue() == null) {
            MessageUtility.displayMessage(ClientMessage.CHECK_NOTNULL_DATEFROM);
            dateFilled = false;
            return;
        } else {
            tmpFrom = (Date) txtFromDate.getValue();
            dateFilled = true;
        }
        if (txtToDate.getValue() == null) {
            MessageUtility.displayMessage(ClientMessage.CHECK_NOTNULL_DATETO);
            dateFilled = true;
            return;
        } else {
            tmpTo = (Date) txtToDate.getValue();
        }

        if (dateFilled) {

            if (this.report.contentEquals("Owners")) {
                ownerNameListingListBean.passParameter(tmpLocation);
                showReport(ReportManager.getSysRegPubDisOwnerNameReport(ownerNameListingListBean, tmpFrom, tmpTo, tmpLocation, reportRequested));
            }
            if (this.report.contentEquals("StateLand")) {
                stateLandListingListBean.passParameter(tmpLocation);
                showReport(ReportManager.getSysRegPubDisStateLandReport(stateLandListingListBean, tmpFrom, tmpTo, tmpLocation, reportRequested));
            }
            if (this.report.contentEquals("ParcelNumber")) {
                parcelNumberListingListBean.passParameter(tmpLocation);
                showReport(ReportManager.getSysRegPubDisParcelNameReport(parcelNumberListingListBean, tmpFrom, tmpTo, tmpLocation, reportRequested));
            }
            this.dispose();
        }
    }//GEN-LAST:event_viewReportActionPerformed

    private void btnShowCalendarFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCalendarFromActionPerformed
        showCalendar(txtFromDate);
    }//GEN-LAST:event_btnShowCalendarFromActionPerformed

    private void btnShowCalendarToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCalendarToActionPerformed
        showCalendar(txtToDate);
    }//GEN-LAST:event_btnShowCalendarToActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnShowCalendarFrom;
    private javax.swing.JButton btnShowCalendarTo;
    private org.sola.clients.beans.cadastre.CadastreObjectBean cadastreObjectBean;
    private org.sola.clients.swing.ui.cadastre.LocationSearch cadastreObjectSearch;
    private org.sola.clients.swing.ui.source.DocumentPanel documentPanel;
    private javax.swing.JLabel labHeader;
    private javax.swing.JLabel labLocation;
    private javax.swing.JLabel labNotificationFrom;
    private javax.swing.JLabel labNotificationTo;
    private org.sola.clients.beans.systematicregistration.OwnerNameListingListBean ownerNameListingListBean;
    private org.sola.clients.beans.systematicregistration.ParcelNumberListingListBean parcelNumberListingListBean;
    private org.sola.clients.beans.source.SourceBean sourceBean;
    private org.sola.clients.beans.systematicregistration.StateLandListingListBean stateLandListingListBean;
    private javax.swing.JFormattedTextField txtFromDate;
    private javax.swing.JFormattedTextField txtToDate;
    private javax.swing.JButton viewReport;
    // End of variables declaration//GEN-END:variables
}
