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

import org.sola.clients.beans.referencedata.ApplicationStageSearchResultBean;
import org.sola.clients.swing.ui.renderers.TableCellTextAreaRenderer;

/**
 * Displays {@link ApplicationStageSearchResultBean} information and allows to edit it.
 */
public class ApplicationStagePanel extends javax.swing.JPanel {

    private ApplicationStageSearchResultBean appStage;

    /** Default constructor. */
    public ApplicationStagePanel() {
        this(null);
    }

    /** 
     * Constructs panel with predefined {@link ApplicationStageSearchResultBean} instance. 
     * @param appStage {@link ApplicationStageSearchResultBean} instance to bind on the form.
     */
    public ApplicationStagePanel(ApplicationStageSearchResultBean appStage) {
        setupAppStageBean(appStage);
        initComponents();
        postInit();
    }

    /** Setup {@link ApplicationStageSearchResultBean} object, used to bind data on the form. */
    private void setupAppStageBean(ApplicationStageSearchResultBean appStage) {
        if (appStage != null) {
            this.appStage = appStage;
        } else {
            this.appStage = new ApplicationStageSearchResultBean();
        }
        firePropertyChange("appStage", null, this.appStage);
    }

    /** Runs post initialization tasks. */
    private void postInit() {
        appStageGroupHelperList.setApplicationStageGroups(this.appStage.getApplicationStageGroups());
        pnlPassword.setVisible(getAppStage().isNew());
        passwordBean.setPassword(null);
        passwordBean.setPasswordConfirmation(null);
        txtDisplayValue.setEnabled(getAppStage().isNew());
    }

    /** Returns {@link ApplicationStageSearchResultBean} object, bound on the form. */
    public ApplicationStageSearchResultBean getAppStage() {
        return appStage;
    }

    /** Sets {@link ApplicationStageSearchResultBean} object to bind on the form. */
    public void setAppStage(ApplicationStageSearchResultBean appStage) {
        setupAppStageBean(appStage);
        postInit();
    }

    /** Disables or enables panel components. */
    public void lockForm(boolean isLocked) {
        txtDescription.setEnabled(!isLocked);
        txtCode.setEnabled(!isLocked);
        txtStatus.setEnabled(!isLocked);
    }
    

    /** Saves appStage. */
    public boolean saveAppStage(boolean showMessage) {
        //appStage.save();
        appStageGroupHelperList.setChecks(appStage.getApplicationStageGroups());
        return true;                
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        passwordBean = new org.sola.clients.beans.security.PasswordBean();
        appStageGroupHelperList = new org.sola.clients.beans.referencedata.ApplicationStageGroupHelperListBean();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCode = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDisplayValue = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtStatus = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        pnlGroups = new javax.swing.JPanel();
        groupPanel1 = new org.sola.clients.swing.ui.GroupPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableGroups = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        pnlPassword = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();

        setAlignmentX(0.0F);
        setAlignmentY(0.0F);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jPanel5.setName("jPanel5"); // NOI18N

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/ui/referencedata/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("ApplicationStagePanel.jLabel1.text_1")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        txtCode.setName("txtCode"); // NOI18N
        txtCode.setNextFocusableComponent(txtStatus);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel5.setText(bundle.getString("ApplicationStagePanel.jLabel5.text_1")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        txtDisplayValue.setName("txtDisplayValue"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(txtCode, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .addComponent(txtDisplayValue, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .addComponent(jLabel5))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDisplayValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel5);

        jPanel4.setName("jPanel4"); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel2.setText(bundle.getString("ApplicationStagePanel.jLabel2.text_1")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        txtStatus.setName("txtStatus"); // NOI18N
        txtStatus.setNextFocusableComponent(txtDisplayValue);

        jLabel4.setText(bundle.getString("ApplicationStagePanel.jLabel4.text_1")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        txtDescription.setColumns(20);
        txtDescription.setLineWrap(true);
        txtDescription.setRows(2);
        txtDescription.setName("txtDescription"); // NOI18N
        jScrollPane1.setViewportView(txtDescription);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .addComponent(jLabel4))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                .addGap(11, 11, 11))
        );

        jPanel1.add(jPanel4);

        jPanel9.setName("jPanel9"); // NOI18N
        jPanel9.setLayout(new java.awt.BorderLayout());

        pnlGroups.setName("pnlGroups"); // NOI18N

        groupPanel1.setName("groupPanel1"); // NOI18N
        groupPanel1.setTitleText(bundle.getString("ApplicationStagePanel.groupPanel1.titleText_1")); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        tableGroups.setAlignmentX(0.0F);
        tableGroups.setAlignmentY(0.0F);
        tableGroups.setName("tableGroups"); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${userGroupHelpers}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, new org.sola.clients.beans.security.UserGroupHelperListBean(), eLProperty, tableGroups);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${inUserGroups}"));
        columnBinding.setColumnName("In User Groups");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${groupSummary.name}"));
        columnBinding.setColumnName("Group Summary.name");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${groupSummary.description}"));
        columnBinding.setColumnName("Group Summary.description");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane2.setViewportView(tableGroups);
        tableGroups.getColumnModel().getColumn(0).setMaxWidth(30);
        tableGroups.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("ApplicationStagePanel.tableGroups.columnModel.title0_1")); // NOI18N
        tableGroups.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableGroups.getColumnModel().getColumn(1).setMaxWidth(300);
        tableGroups.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("ApplicationStagePanel.tableGroups.columnModel.title1_1")); // NOI18N
        tableGroups.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("ApplicationStagePanel.tableGroups.columnModel.title2_1")); // NOI18N
        tableGroups.getColumnModel().getColumn(2).setCellRenderer(new TableCellTextAreaRenderer());

        javax.swing.GroupLayout pnlGroupsLayout = new javax.swing.GroupLayout(pnlGroups);
        pnlGroups.setLayout(pnlGroupsLayout);
        pnlGroupsLayout.setHorizontalGroup(
            pnlGroupsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGroupsLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addGap(10, 10, 10))
            .addGroup(pnlGroupsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(groupPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlGroupsLayout.setVerticalGroup(
            pnlGroupsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGroupsLayout.createSequentialGroup()
                .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel9.add(pnlGroups, java.awt.BorderLayout.CENTER);

        pnlPassword.setName("pnlPassword"); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(1, 0));

        jPanel7.setName("jPanel7"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 455, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel7);

        javax.swing.GroupLayout pnlPasswordLayout = new javax.swing.GroupLayout(pnlPassword);
        pnlPassword.setLayout(pnlPasswordLayout);
        pnlPasswordLayout.setHorizontalGroup(
            pnlPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 455, Short.MAX_VALUE)
            .addGroup(pnlPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlPasswordLayout.setVerticalGroup(
            pnlPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 61, Short.MAX_VALUE)
            .addGroup(pnlPasswordLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPasswordLayout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jPanel9.add(pnlPassword, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.beans.referencedata.ApplicationStageGroupHelperListBean appStageGroupHelperList;
    private org.sola.clients.swing.ui.GroupPanel groupPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private org.sola.clients.beans.security.PasswordBean passwordBean;
    private javax.swing.JPanel pnlGroups;
    private javax.swing.JPanel pnlPassword;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableGroups;
    private javax.swing.JTextField txtCode;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtDisplayValue;
    private javax.swing.JTextField txtStatus;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
