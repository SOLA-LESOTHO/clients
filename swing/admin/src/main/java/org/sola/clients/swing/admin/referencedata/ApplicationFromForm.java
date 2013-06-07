/*
 * Copyright 2013 Food and Agriculture Organization of the United Nations (FAO).
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
package org.sola.clients.swing.admin.referencedata;

import org.sola.clients.beans.referencedata.ApplicationFormBean;
import org.sola.clients.beans.referencedata.ApplicationFormWithBinaryBean;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

public class ApplicationFromForm extends ContentPanel {

    private boolean closeOnSave;
    private ApplicationFormWithBinaryBean appFormBean;

    /**
     * Default constructor
     */
    public ApplicationFromForm(ApplicationFormBean appFormBean, boolean saveOnAction, boolean closeOnSave) {
        this.closeOnSave = closeOnSave;
        initComponents();
        setAppFormBean(appFormBean);
    }

    public boolean isCloseOnSave() {
        return closeOnSave;
    }

    public void setCloseOnSave(boolean closeOnSave) {
        this.closeOnSave = closeOnSave;
        customizePanel();
    }

    public ApplicationFormWithBinaryBean getAppFormBean() {
        return applicationFormPanel.getBean();
    }

    public void setAppFormBean(ApplicationFormBean appFormBean) {
        if(appFormBean==null){
            this.appFormBean = new ApplicationFormWithBinaryBean();
        } else {
            this.appFormBean = ApplicationFormWithBinaryBean.getApplicationForm(appFormBean.getCode());
        }
        applicationFormPanel.setBean(this.appFormBean);
        customizePanel();
    }

    private void customizePanel() {
        if (closeOnSave) {
            btnSave.setText(MessageUtility.getLocalizedMessage(
                    ClientMessage.GENERAL_LABELS_SAVE_AND_CLOSE).getMessage());
        } else {
            btnSave.setText(MessageUtility.getLocalizedMessage(
                    ClientMessage.GENERAL_LABELS_SAVE).getMessage());
        }
    }

    private void save() {
        boolean isSaved = false;

        if (applicationFormPanel.save(true)) {
            MessageUtility.displayMessage(ClientMessage.ADMIN_REFDATA_SAVED,
                    new String[]{applicationFormPanel.getBean().getTranslatedDisplayValue()});
            isSaved = true;
        }

        if (isSaved) {
            firePropertyChange(ReferenceDataPanelForm.REFDATA_SAVED_PROPERTY, false, true);
            if (closeOnSave) {
                close();
            } else {
                customizePanel();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel1 = new org.sola.clients.swing.ui.HeaderPanel();
        applicationFormPanel = new org.sola.clients.swing.ui.referencedata.ApplicationFormPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnSave = new org.sola.clients.swing.common.buttons.BtnSave();

        setHeaderPanel(headerPanel1);

        headerPanel1.setTitleText("Application form");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnSave.setText("Save & Close");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSave);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(applicationFormPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(applicationFormPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.ui.referencedata.ApplicationFormPanel applicationFormPanel;
    private org.sola.clients.swing.common.buttons.BtnSave btnSave;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
