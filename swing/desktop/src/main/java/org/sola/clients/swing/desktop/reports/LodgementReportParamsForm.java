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
package org.sola.clients.swing.desktop.reports;

import java.awt.ComponentOrientation;
import java.util.Date;
import java.util.Locale;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JasperPrint;
import org.sola.clients.beans.application.LodgementBean;
import org.sola.clients.beans.application.ServiceReportBean;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.ui.reports.ReportViewerForm;
import org.sola.clients.swing.ui.renderers.FormattersFactory;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author RizzoM
 */
public class LodgementReportParamsForm extends javax.swing.JDialog {

    /**
     * Creates new form LodgementReportParamsForm
     */
    public LodgementReportParamsForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

      private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchParams = new org.sola.clients.beans.application.LodgementViewParamsBean();
        serviceReportBean1 = new org.sola.clients.beans.application.ServiceReportBean();
        jPanel6 = new javax.swing.JPanel();
        labFrom = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnShowCalendarFrom = new javax.swing.JButton();
        txtFromDate = new javax.swing.JFormattedTextField();
        btnFromDate = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btnShowCalendarTo = new javax.swing.JButton();
        txtToDate = new javax.swing.JFormattedTextField();
        btnToDate = new javax.swing.JButton();
        labTo = new javax.swing.JLabel();
        viewReport = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/reports/Bundle"); // NOI18N
        setTitle(bundle.getString("LodgementReportParamsForm.title")); // NOI18N

        java.util.ResourceBundle bundle1 = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/application/Bundle"); // NOI18N
        labFrom.setText(bundle1.getString("ApplicationSearchPanel.labFrom.text")); // NOI18N

        btnShowCalendarFrom.setText(bundle1.getString("ApplicationSearchPanel.btnShowCalendarFrom.text")); // NOI18N
        btnShowCalendarFrom.setBorder(null);
        btnShowCalendarFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCalendarFromActionPerformed(evt);
            }
        });

        txtFromDate.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtFromDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtFromDate.setToolTipText(bundle.getString("LodgementReportParamsForm.txtFromDate.toolTipText")); // NOI18N
        txtFromDate.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtFromDate.setHorizontalAlignment(JTextField.LEADING);

        btnFromDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnFromDate.setText(bundle.getString("LodgementReportParamsForm.btnFromDate.text")); // NOI18N
        btnFromDate.setBorder(null);
        btnFromDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(txtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShowCalendarFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnFromDate)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(txtFromDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(btnShowCalendarFrom)
                .addComponent(btnFromDate))
        );

        btnShowCalendarTo.setText(bundle1.getString("ApplicationSearchPanel.btnShowCalendarTo.text")); // NOI18N
        btnShowCalendarTo.setBorder(null);
        btnShowCalendarTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowCalendarToActionPerformed(evt);
            }
        });

        txtToDate.setFont(new java.awt.Font("Tahoma", 0, 12));
        txtToDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtToDate.setToolTipText(bundle.getString("LodgementReportParamsForm.txtToDate.toolTipText")); // NOI18N
        txtToDate.setComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        txtToDate.setHorizontalAlignment(JTextField.LEADING);

        btnToDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnToDate.setText(bundle.getString("LodgementReportParamsForm.btnToDate.text")); // NOI18N
        btnToDate.setBorder(null);
        btnToDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(txtToDate, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnShowCalendarTo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnToDate)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtToDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnShowCalendarTo)
            .addComponent(btnToDate)
        );

        labTo.setText(bundle1.getString("ApplicationSearchPanel.labTo.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labTo)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(labTo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        viewReport.setText(bundle.getString("LodgementReportParamsForm.viewReport.text")); // NOI18N
        viewReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewReportActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labFrom)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewReport)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(labFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(viewReport)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnShowCalendarFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCalendarFromActionPerformed
        showCalendar(txtFromDate);
    }//GEN-LAST:event_btnShowCalendarFromActionPerformed

    private void btnShowCalendarToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowCalendarToActionPerformed
        showCalendar(txtToDate);
    }//GEN-LAST:event_btnShowCalendarToActionPerformed
       /** Opens {@link ReportViewerForm} to display report.*/
    private void showReport(JasperPrint report) {
        ReportViewerForm form = new ReportViewerForm(report);
        form.setVisible(true);
        form.setAlwaysOnTop(true);
    }
    private void viewReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewReportActionPerformed
         boolean dateFilled = false;
        Date tmpFrom;
        Date tmpTo;
        if (txtFromDate.getValue() == null) {
            MessageUtility.displayMessage(ClientMessage.CHECK_NOTNULL_DATEFROM);
            dateFilled = false;
            return;
        } else {
            tmpFrom = (Date) txtFromDate.getValue();
            dateFilled = true;
            searchParams.setFromDate(tmpFrom);
        }
        if (txtToDate.getValue() == null) {
            MessageUtility.displayMessage(ClientMessage.CHECK_NOTNULL_DATETO);
            dateFilled = true;
            return;
        } else {
            tmpTo = (Date) txtToDate.getValue();
            searchParams.setToDate(tmpTo);
        }

        if (dateFilled) {
            final Date tmpFromFinal = tmpFrom;
            final Date tmpToFinal = tmpTo;
            SolaTask<Void, Void> t = new SolaTask<Void, Void>() {
                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_GENERATING_REPORT));
                    // Refresh the details of the baUnit to make sure the latest details are used
                    serviceReportBean1.loadWorkSummary(searchParams);
                    return null;
                }

                @Override
                protected void taskDone() {
                    showReport(ReportManager.getLodgementReport(serviceReportBean1, tmpFromFinal, tmpToFinal));
                }
            };
            TaskManager.getInstance().runTask(t);
        }
        this.dispose();
    }//GEN-LAST:event_viewReportActionPerformed

    private void btnToDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToDateActionPerformed
        showCalendar(txtToDate);
    }//GEN-LAST:event_btnToDateActionPerformed

    private void btnFromDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromDateActionPerformed
        showCalendar(txtFromDate);
    }//GEN-LAST:event_btnFromDateActionPerformed

  
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFromDate;
    private javax.swing.JButton btnShowCalendarFrom;
    private javax.swing.JButton btnShowCalendarTo;
    private javax.swing.JButton btnToDate;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel labFrom;
    private javax.swing.JLabel labTo;
    private org.sola.clients.beans.application.LodgementViewParamsBean searchParams;
    private org.sola.clients.beans.application.ServiceReportBean serviceReportBean1;
    private javax.swing.JFormattedTextField txtFromDate;
    private javax.swing.JFormattedTextField txtToDate;
    private javax.swing.JButton viewReport;
    // End of variables declaration//GEN-END:variables
}
