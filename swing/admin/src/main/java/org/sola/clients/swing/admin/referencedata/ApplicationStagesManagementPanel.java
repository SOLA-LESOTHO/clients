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
package org.sola.clients.swing.admin.referencedata;

import org.sola.clients.swing.admin.security.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.sola.clients.beans.referencedata.ApplicationStageSearchResultBean;
import org.sola.clients.beans.referencedata.ApplicationStageSearchResultListBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.beans.security.UserBean;
import org.sola.clients.beans.security.UserSearchAdvancedResultBean;
import org.sola.clients.beans.security.UserSearchAdvancedResultListBean;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.renderers.TableCellTextAreaRenderer;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Allows to manage users of the application.
 */
public class ApplicationStagesManagementPanel extends ContentPanel {

    /**
     * Creates new form UsersManagementPanel
     */
    public ApplicationStagesManagementPanel() {
        initComponents();
        groupsList.loadGroups(true);
        comboGroups.setSelectedIndex(0);
        applicationStageSearchResultList.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ApplicationStageSearchResultListBean.SELECTED_APPSTAGE_PROPERTY)) {
                    customizeUserButtons((ApplicationStageSearchResultBean) evt.getNewValue());
                }
            }
        });
        customizeUserButtons(null);
    }

    /**
     * Enables or disables application stages management buttons, depending on
     * selection in the groups table and application stages rights.
     */
    private void customizeUserButtons(ApplicationStageSearchResultBean applicationStageSearchResult) {
        //btnEditStage.setEnabled(applicationStageSearchResult != null);
        //btnRemoveStage.setEnabled(applicationStageSearchResult != null);
        //menuEditUser.setEnabled(btnEditStage.isEnabled());
        //menuRemoveUser.setEnabled(btnRemoveStage.isEnabled());
        
        btnEditStage.setEnabled(false);
        btnRemoveStage.setEnabled(false);
        menuEditUser.setEnabled(false);
        menuRemoveUser.setEnabled(false);
        btnAddStage.setEnabled(false);
    }

    /**
     * Shows user panel.
     */
    private void showUser(final UserBean userBean) {
        UserPanelForm panel = new UserPanelForm(userBean, true, userBean != null, false);
        panel.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(UserPanelForm.USER_SAVED_PROPERTY)) {
                    if (userBean == null) {
                        ((UserPanelForm) evt.getSource()).setUserBean(null);
                    } else {
                        searchAppStages();
                    }
                }
            }
        });
        getMainContentPanel().addPanel(panel, MainContentPanel.CARD_ADMIN_USER, true);
    }

    /**
     * Shows password panel.
     */
    private void showPasswordPanel(String userName) {
        UserPasswordPanelForm panel = new UserPasswordPanelForm(userName);
        getMainContentPanel().addPanel(panel, MainContentPanel.CARD_ADMIN_USER_PASSWORD, true);
    }

    /**
     * Searches application stages with the given criteria.
     */
    private void searchAppStages() {
        applicationStageSearchResultList.getApplicationStages(applicationStageSearchParams);
        if (applicationStageSearchResultList.getApplicationStageList().size() < 1) {
            MessageUtility.displayMessage(ClientMessage.ADMIN_APPLICATION_STAGE_NOT_FOUND);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        popupUsers = new javax.swing.JPopupMenu();
        menuAddUser = new javax.swing.JMenuItem();
        menuEditUser = new javax.swing.JMenuItem();
        menuSetPassword = new javax.swing.JMenuItem();
        menuRemoveUser = new javax.swing.JMenuItem();
        groupsList = new org.sola.clients.beans.security.GroupSummaryListBean();
        userSearchParams = new org.sola.clients.beans.security.UserSearchParamsBean();
        userSearchResultList = new org.sola.clients.beans.security.UserSearchAdvancedResultListBean();
        applicationStageSearchResultList = new org.sola.clients.beans.referencedata.ApplicationStageSearchResultListBean();
        applicationStageSearchResult = new org.sola.clients.beans.referencedata.ApplicationStageSearchResultBean();
        applicationStageSearchParams = new org.sola.clients.beans.referencedata.ApplicationStageSearchParamsBean();
        pnlHeader = new org.sola.clients.swing.ui.HeaderPanel();
        pnlSearchCriteria = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCode = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtStatus = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtDisplayValue = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        comboGroups = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        toolbarAppStages = new javax.swing.JToolBar();
        btnAddStage = new javax.swing.JButton();
        btnEditStage = new javax.swing.JButton();
        btnRemoveStage = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAppStages = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();

        popupUsers.setName("popupUsers"); // NOI18N

        menuAddUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/admin/referencedata/Bundle"); // NOI18N
        menuAddUser.setText(bundle.getString("ApplicationStagesManagementPanel.menuAddUser.text_1")); // NOI18N
        menuAddUser.setName("menuAddUser"); // NOI18N
        menuAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddUserActionPerformed(evt);
            }
        });
        popupUsers.add(menuAddUser);

        menuEditUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        menuEditUser.setText(bundle.getString("ApplicationStagesManagementPanel.menuEditUser.text_1")); // NOI18N
        menuEditUser.setName("menuEditUser"); // NOI18N
        menuEditUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditUserActionPerformed(evt);
            }
        });
        popupUsers.add(menuEditUser);

        menuSetPassword.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/lock--pencil.png"))); // NOI18N
        menuSetPassword.setText(bundle.getString("ApplicationStagesManagementPanel.menuSetPassword.text_1")); // NOI18N
        menuSetPassword.setName("menuSetPassword"); // NOI18N
        menuSetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSetPasswordActionPerformed(evt);
            }
        });
        popupUsers.add(menuSetPassword);

        menuRemoveUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        menuRemoveUser.setText(bundle.getString("ApplicationStagesManagementPanel.menuRemoveUser.text_1")); // NOI18N
        menuRemoveUser.setName("menuRemoveUser"); // NOI18N
        menuRemoveUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveUserActionPerformed(evt);
            }
        });
        popupUsers.add(menuRemoveUser);

        setHeaderPanel(pnlHeader);
        setMinimumSize(new java.awt.Dimension(200, 200));

        pnlHeader.setName("pnlHeader"); // NOI18N
        pnlHeader.setTitleText(bundle.getString("ApplicationStagesManagementPanel.pnlHeader.titleText_1")); // NOI18N

        pnlSearchCriteria.setName("pnlSearchCriteria"); // NOI18N
        pnlSearchCriteria.setLayout(new java.awt.GridLayout(1, 4, 15, 0));

        jPanel2.setName(bundle.getString("ApplicationStagesManagementPanel.jPanel2.name_1")); // NOI18N

        jLabel2.setText(bundle.getString("ApplicationStagesManagementPanel.jLabel2.text_1")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        txtCode.setName("txtCode"); // NOI18N
        txtCode.setNextFocusableComponent(txtStatus);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, userSearchParams, org.jdesktop.beansbinding.ELProperty.create("${userName}"), txtCode, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 76, Short.MAX_VALUE))
            .addComponent(txtCode)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlSearchCriteria.add(jPanel2);

        jPanel3.setName(bundle.getString("ApplicationStagesManagementPanel.jPanel3.name_1")); // NOI18N

        txtStatus.setName("txtStatus"); // NOI18N
        txtStatus.setNextFocusableComponent(txtDisplayValue);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, userSearchParams, org.jdesktop.beansbinding.ELProperty.create("${firstName}"), txtStatus, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel1.setText(bundle.getString("ApplicationStagesManagementPanel.jLabel1.text_1")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 74, Short.MAX_VALUE))
            .addComponent(txtStatus)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlSearchCriteria.add(jPanel3);

        jPanel4.setName(bundle.getString("ApplicationStagesManagementPanel.jPanel4.name_1")); // NOI18N

        jLabel3.setText(bundle.getString("ApplicationStagesManagementPanel.jLabel3.text_1")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        txtDisplayValue.setName("txtDisplayValue"); // NOI18N
        txtDisplayValue.setNextFocusableComponent(comboGroups);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, userSearchParams, org.jdesktop.beansbinding.ELProperty.create("${lastName}"), txtDisplayValue, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 42, Short.MAX_VALUE))
            .addComponent(txtDisplayValue)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDisplayValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlSearchCriteria.add(jPanel4);

        jPanel5.setName(bundle.getString("ApplicationStagesManagementPanel.jPanel5.name_1")); // NOI18N

        jLabel4.setText(bundle.getString("ApplicationStagesManagementPanel.jLabel4.text_1")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        comboGroups.setName("comboGroups"); // NOI18N
        comboGroups.setNextFocusableComponent(btnSearch);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${groupSummaryList}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, groupsList, eLProperty, comboGroups);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, applicationStageSearchParams, org.jdesktop.beansbinding.ELProperty.create("${groupBean}"), comboGroups, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 67, Short.MAX_VALUE))
            .addComponent(comboGroups, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(comboGroups, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlSearchCriteria.add(jPanel5);

        jPanel1.setName(bundle.getString("ApplicationStagesManagementPanel.jPanel1.name_1")); // NOI18N

        jLabel5.setText(bundle.getString("ApplicationStagesManagementPanel.jLabel5.text_1")); // NOI18N
        jLabel5.setName(bundle.getString("ApplicationStagesManagementPanel.jLabel5.name_1")); // NOI18N

        btnSearch.setText(bundle.getString("ApplicationStagesManagementPanel.btnSearch.text_1")); // NOI18N
        btnSearch.setName("btnSearch"); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch))
        );

        toolbarAppStages.setFloatable(false);
        toolbarAppStages.setRollover(true);
        toolbarAppStages.setName("toolbarAppStages"); // NOI18N

        btnAddStage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAddStage.setText(bundle.getString("ApplicationStagesManagementPanel.btnAddStage.text_1")); // NOI18N
        btnAddStage.setFocusable(false);
        btnAddStage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAddStage.setName("btnAddStage"); // NOI18N
        btnAddStage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddStage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddStageActionPerformed(evt);
            }
        });
        toolbarAppStages.add(btnAddStage);

        btnEditStage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        btnEditStage.setText(bundle.getString("ApplicationStagesManagementPanel.btnEditStage.text_1")); // NOI18N
        btnEditStage.setFocusable(false);
        btnEditStage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditStage.setName("btnEditStage"); // NOI18N
        btnEditStage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditStage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditStageActionPerformed(evt);
            }
        });
        toolbarAppStages.add(btnEditStage);

        btnRemoveStage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        btnRemoveStage.setText(bundle.getString("ApplicationStagesManagementPanel.btnRemoveStage.text_1")); // NOI18N
        btnRemoveStage.setFocusable(false);
        btnRemoveStage.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRemoveStage.setName("btnRemoveStage"); // NOI18N
        btnRemoveStage.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveStage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveStageActionPerformed(evt);
            }
        });
        toolbarAppStages.add(btnRemoveStage);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableAppStages.setComponentPopupMenu(popupUsers);
        tableAppStages.setName("tableAppStages"); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${applicationStageList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, applicationStageSearchResultList, eLProperty, tableAppStages);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${code}"));
        columnBinding.setColumnName("Code");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${displayValue}"));
        columnBinding.setColumnName("Display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${description}"));
        columnBinding.setColumnName("Description");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${groupList}"));
        columnBinding.setColumnName("Group List");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, applicationStageSearchResultList, org.jdesktop.beansbinding.ELProperty.create("${selectedApplicationStage}"), tableAppStages, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(tableAppStages);
        tableAppStages.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("ApplicationStagesManagementPanel.tableAppStages.columnModel.title0")); // NOI18N
        tableAppStages.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("ApplicationStagesManagementPanel.tableAppStages.columnModel.title1")); // NOI18N
        tableAppStages.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("ApplicationStagesManagementPanel.tableUsers.columnModel.title3")); // NOI18N
        tableAppStages.getColumnModel().getColumn(2).setCellRenderer(new TableCellTextAreaRenderer());
        tableAppStages.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("ApplicationStagesManagementPanel.tableUsers.columnModel.title4")); // NOI18N
        tableAppStages.getColumnModel().getColumn(3).setCellRenderer(new TableCellTextAreaRenderer());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnlSearchCriteria, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(toolbarAppStages, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlSearchCriteria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(toolbarAppStages, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchAppStages();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAddStageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddStageActionPerformed
        addUser();
    }//GEN-LAST:event_btnAddStageActionPerformed

    private void btnEditStageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditStageActionPerformed
        editUser();
    }//GEN-LAST:event_btnEditStageActionPerformed

    private void btnRemoveStageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveStageActionPerformed
        removeAppStage();
    }//GEN-LAST:event_btnRemoveStageActionPerformed

    private void menuAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddUserActionPerformed
        addUser();
    }//GEN-LAST:event_menuAddUserActionPerformed

    private void menuEditUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditUserActionPerformed
        editUser();
    }//GEN-LAST:event_menuEditUserActionPerformed

    private void menuSetPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSetPasswordActionPerformed
        editPassword();
    }//GEN-LAST:event_menuSetPasswordActionPerformed

    private void menuRemoveUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveUserActionPerformed
        removeAppStage();
    }//GEN-LAST:event_menuRemoveUserActionPerformed

    private void addUser() {
        showUser(null);
    }

    private void editUser() {
        if (userSearchResultList.getSelectedUser() != null) {
            showUser(UserBean.getUser(userSearchResultList.getSelectedUser().getUserName()));
        }
    }

    private void editPassword() {
        if (userSearchResultList.getSelectedUser() != null) {
            showPasswordPanel(userSearchResultList.getSelectedUser().getUserName());
        }
    }

    public void removeAppStage() {
        if (userSearchResultList.getSelectedUser() != null) {
            if (userSearchResultList.getSelectedUser().getUserName().equals(SecurityBean.getCurrentUser().getUserName())) {
                MessageUtility.displayMessage(ClientMessage.ADMIN_CURRENT_USER_DELETE_ERROR);
                return;
            }
            if (MessageUtility.displayMessage(ClientMessage.ADMIN_CONFIRM_DELETE_USER)
                    == MessageUtility.BUTTON_ONE) {
                UserBean.removeUser(userSearchResultList.getSelectedUser().getUserName());
                userSearchResultList.getUsersList().remove(userSearchResultList.getSelectedUser());
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.beans.referencedata.ApplicationStageSearchParamsBean applicationStageSearchParams;
    private org.sola.clients.beans.referencedata.ApplicationStageSearchResultBean applicationStageSearchResult;
    private org.sola.clients.beans.referencedata.ApplicationStageSearchResultListBean applicationStageSearchResultList;
    private javax.swing.JButton btnAddStage;
    private javax.swing.JButton btnEditStage;
    private javax.swing.JButton btnRemoveStage;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox comboGroups;
    private org.sola.clients.beans.security.GroupSummaryListBean groupsList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem menuAddUser;
    private javax.swing.JMenuItem menuEditUser;
    private javax.swing.JMenuItem menuRemoveUser;
    private javax.swing.JMenuItem menuSetPassword;
    private org.sola.clients.swing.ui.HeaderPanel pnlHeader;
    private javax.swing.JPanel pnlSearchCriteria;
    private javax.swing.JPopupMenu popupUsers;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableAppStages;
    private javax.swing.JToolBar toolbarAppStages;
    private javax.swing.JTextField txtCode;
    private javax.swing.JTextField txtDisplayValue;
    private javax.swing.JTextField txtStatus;
    private org.sola.clients.beans.security.UserSearchParamsBean userSearchParams;
    private org.sola.clients.beans.security.UserSearchAdvancedResultListBean userSearchResultList;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
