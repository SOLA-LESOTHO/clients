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
package org.sola.clients.swing.ui.referencedata;

import java.io.File;
import javax.swing.JFileChooser;
import org.sola.clients.beans.AbstractCodeBean;
import org.sola.clients.beans.referencedata.ApplicationFormWithBinaryBean;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.common.FileUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.webservices.transferobjects.referencedata.ApplicationFormWithBinaryTO;
import org.sola.webservices.transferobjects.referencedata.RrrTypeTO;

public class ApplicationFormPanel extends javax.swing.JPanel {

    private ApplicationFormWithBinaryBean bean;
    
    /**
     * Creates new form ApplicationFormPanel
     */
    public ApplicationFormPanel() {
        initComponents();
    }
    
    public ApplicationFormPanel(ApplicationFormWithBinaryBean bean) {
        if(bean==null){
            this.bean = new ApplicationFormWithBinaryBean();
        } else {
            this.bean = bean;
        }
        initComponents();
        postInit();
    }
    
    private void postInit(){
        setupRefBean();
        customizeOpenButton();
    }

    private void customizeOpenButton(){
        btnOpen.setEnabled(bean.getContent()!=null);
    }
    
    private void setupRefBean(){
        referenceDataPanel.setReferenceDataBean(ApplicationFormWithBinaryBean.class, ApplicationFormWithBinaryTO.class, bean);
    }
    
    public ApplicationFormWithBinaryBean getBean() {
        return bean;
    }

    public void setBean(ApplicationFormWithBinaryBean bean) {
        if (bean != null) {
            this.bean = bean;
        } else {
            this.bean = new ApplicationFormWithBinaryBean();
        }
        setupRefBean();
        customizeOpenButton();
        firePropertyChange("bean", null, this.bean);
    }
    
    private void openApplicationForm() {
        if (bean.getContent()!=null) {
            SolaTask t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_APPFORM));
                    bean.openApplicationForm();
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }
    
    private void attach(){
        final JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            bean.setExtension(FileUtility.getFileExtension(file));
            bean.setContent(FileUtility.getFileBinary(file.getAbsolutePath()));
        }
        customizeOpenButton();
    }
    
    /** Calls saving procedure of RRR type data object. */
    public boolean save(boolean showMessage) {
        return referenceDataPanel.save(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        referenceDataPanel = new org.sola.clients.swing.ui.referencedata.ReferenceDataPanel();
        jPanel1 = new javax.swing.JPanel();
        btnOpen = new javax.swing.JButton();
        btnAttachFile = new javax.swing.JButton();

        btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document-text.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/ui/referencedata/Bundle"); // NOI18N
        btnOpen.setText(bundle.getString("ApplicationFormPanel.btnOpen.text")); // NOI18N
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenActionPerformed(evt);
            }
        });

        btnAttachFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/attachment.png"))); // NOI18N
        btnAttachFile.setText(bundle.getString("ApplicationFormPanel.btnAttachFile.text")); // NOI18N
        btnAttachFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAttachFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnOpen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAttachFile)
                .addGap(0, 61, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnOpen)
                .addComponent(btnAttachFile))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(referenceDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(referenceDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenActionPerformed
        openApplicationForm();
    }//GEN-LAST:event_btnOpenActionPerformed

    private void btnAttachFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAttachFileActionPerformed
        attach();
    }//GEN-LAST:event_btnAttachFileActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAttachFile;
    private javax.swing.JButton btnOpen;
    private javax.swing.JPanel jPanel1;
    private org.sola.clients.swing.ui.referencedata.ReferenceDataPanel referenceDataPanel;
    // End of variables declaration//GEN-END:variables
}
