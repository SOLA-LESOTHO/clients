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
package org.sola.clients.swing.admin.security;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
public class UsersManagementPanel extends ContentPanel {

    /** Creates new form UsersManagementPanel */
    public UsersManagementPanel() {
        initComponents();
        groupsList.loadGroups(true);
        comboGroups.setSelectedIndex(0);
        userSearchResultList.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(UserSearchAdvancedResultListBean.SELECTED_USER_PROPERTY)) {
                    customizeUserButtons((UserSearchAdvancedResultBean) evt.getNewValue());
                }
            }
        });
        customizeUserButtons(null);
    }
    
    /** 
     * Enables or disables user management buttons, depending on selection in 
     * the groups table and user rights. 
     */
    private void customizeUserButtons(UserSearchAdvancedResultBean userSearchResult) {
        btnEditUser.setEnabled(userSearchResult != null);
        btnRemoveUser.setEnabled(userSearchResult != null);
        btnSetPassword.setEnabled(userSearchResult != null);
        menuEditUser.setEnabled(btnEditUser.isEnabled());
        menuRemoveUser.setEnabled(btnRemoveUser.isEnabled());
        menuSetPassword.setEnabled(btnSetPassword.isEnabled());
    }

    /** Shows user panel. */
    private void showUser(final UserBean userBean) {
        UserPanelForm panel = new UserPanelForm(userBean, true, userBean != null, false);
        panel.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(UserPanelForm.USER_SAVED_PROPERTY)) {
                    if (userBean == null) {
                        ((UserPanelForm) evt.getSource()).setUserBean(null);
                    } else {
                        searchUsers();
                    }
                }
            }
        });
        getMainContentPanel().addPanel(panel, MainContentPanel.CARD_ADMIN_USER, true);
    }

    /** Shows password panel. */
    private void showPasswordPanel(String userName) {
        UserPasswordPanelForm panel = new UserPasswordPanelForm(userName);
        getMainContentPanel().addPanel(panel, MainContentPanel.CARD_ADMIN_USER_PASSWORD, true);
    }

    /** Searches users with the given criteria. */
    private void searchUsers() {
        userSearchResultList.searchUsers(userSearchParams);
        if (userSearchResultList.getUsersList().size() < 1) {
            MessageUtility.displayMessage(ClientMessage.ADMIN_USERS_NO_FOUND);
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
        pnlHeader = new org.sola.clients.swing.ui.HeaderPanel();
        pnlSearchCriteria = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtFirstName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        comboGroups = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        toolbarUsers = new javax.swing.JToolBar();
        btnAddUser = new javax.swing.JButton();
        btnEditUser = new javax.swing.JButton();
        btnSetPassword = new javax.swing.JButton();
        btnRemoveUser = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableUsers = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();

        popupUsers.setName("popupUsers"); // NOI18N

        menuAddUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/admin/security/Bundle"); // NOI18N
        menuAddUser.setText(bundle.getString("UsersManagementPanel.menuAddUser.text")); // NOI18N
        menuAddUser.setName("menuAddUser"); // NOI18N
        menuAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddUserActionPerformed(evt);
            }
        });
        popupUsers.add(menuAddUser);

        menuEditUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        menuEditUser.setText(bundle.getString("UsersManagementPanel.menuEditUser.text")); // NOI18N
        menuEditUser.setName("menuEditUser"); // NOI18N
        menuEditUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditUserActionPerformed(evt);
            }
        });
        popupUsers.add(menuEditUser);

        menuSetPassword.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/lock--pencil.png"))); // NOI18N
        menuSetPassword.setText(bundle.getString("UsersManagementPanel.menuSetPassword.text")); // NOI18N
        menuSetPassword.setName("menuSetPassword"); // NOI18N
        menuSetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSetPasswordActionPerformed(evt);
            }
        });
        popupUsers.add(menuSetPassword);

        menuRemoveUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        menuRemoveUser.setText(bundle.getString("UsersManagementPanel.menuRemoveUser.text")); // NOI18N
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
        pnlHeader.setTitleText(bundle.getString("UsersManagementPanel.pnlHeader.titleText")); // NOI18N

        pnlSearchCriteria.setName("pnlSearchCriteria"); // NOI18N
        pnlSearchCriteria.setLayout(new java.awt.GridLayout(1, 4, 15, 0));

        jPanel2.setName(bundle.getString("UsersManagementPanel.jPanel2.name")); // NOI18N

        jLabel2.setText(bundle.getString("UsersManagementPanel.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        txtUsername.setName("txtUsername"); // NOI18N
        txtUsername.setNextFocusableComponent(txtFirstName);

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, userSearchParams, org.jdesktop.beansbinding.ELProperty.create("${userName}"), txtUsername, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 53, Short.MAX_VALUE))
            .addComponent(txtUsername)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlSearchCriteria.add(jPanel2);

        jPanel3.setName(bundle.getString("UsersManagementPanel.jPanel3.name")); // NOI18N

        txtFirstName.setName("txtFirstName"); // NOI18N
        txtFirstName.setNextFocusableComponent(txtLastName);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, userSearchParams, org.jdesktop.beansbinding.ELProperty.create("${firstName}"), txtFirstName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel1.setText(bundle.getString("UsersManagementPanel.jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 51, Short.MAX_VALUE))
            .addComponent(txtFirstName)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlSearchCriteria.add(jPanel3);

        jPanel4.setName(bundle.getString("UsersManagementPanel.jPanel4.name")); // NOI18N

        jLabel3.setText(bundle.getString("UsersManagementPanel.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        txtLastName.setName("txtLastName"); // NOI18N
        txtLastName.setNextFocusableComponent(comboGroups);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, userSearchParams, org.jdesktop.beansbinding.ELProperty.create("${lastName}"), txtLastName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 52, Short.MAX_VALUE))
            .addComponent(txtLastName)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlSearchCriteria.add(jPanel4);

        jPanel5.setName(bundle.getString("UsersManagementPanel.jPanel5.name")); // NOI18N

        jLabel4.setText(bundle.getString("UsersManagementPanel.jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        comboGroups.setName("comboGroups"); // NOI18N
        comboGroups.setNextFocusableComponent(btnSearch);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${groupSummaryList}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, groupsList, eLProperty, comboGroups);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, userSearchParams, org.jdesktop.beansbinding.ELProperty.create("${groupBean}"), comboGroups, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
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

        jPanel1.setName(bundle.getString("UsersManagementPanel.jPanel1.name")); // NOI18N

        jLabel5.setText(bundle.getString("UsersManagementPanel.jLabel5.text")); // NOI18N
        jLabel5.setName(bundle.getString("UsersManagementPanel.jLabel5.name")); // NOI18N

        btnSearch.setText(bundle.getString("UsersManagementPanel.btnSearch.text")); // NOI18N
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

        toolbarUsers.setFloatable(false);
        toolbarUsers.setRollover(true);
        toolbarUsers.setName("toolbarUsers"); // NOI18N

        btnAddUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAddUser.setText(bundle.getString("UsersManagementPanel.btnAddUser.text")); // NOI18N
        btnAddUser.setFocusable(false);
        btnAddUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAddUser.setName("btnAddUser"); // NOI18N
        btnAddUser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddUserActionPerformed(evt);
            }
        });
        toolbarUsers.add(btnAddUser);

        btnEditUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        btnEditUser.setText(bundle.getString("UsersManagementPanel.btnEditUser.text")); // NOI18N
        btnEditUser.setFocusable(false);
        btnEditUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditUser.setName("btnEditUser"); // NOI18N
        btnEditUser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditUserActionPerformed(evt);
            }
        });
        toolbarUsers.add(btnEditUser);

        btnSetPassword.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/lock--pencil.png"))); // NOI18N
        btnSetPassword.setText(bundle.getString("UsersManagementPanel.btnSetPassword.text")); // NOI18N
        btnSetPassword.setFocusable(false);
        btnSetPassword.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSetPassword.setName("btnSetPassword"); // NOI18N
        btnSetPassword.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSetPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetPasswordActionPerformed(evt);
            }
        });
        toolbarUsers.add(btnSetPassword);

        btnRemoveUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        btnRemoveUser.setText(bundle.getString("UsersManagementPanel.btnRemoveUser.text")); // NOI18N
        btnRemoveUser.setFocusable(false);
        btnRemoveUser.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRemoveUser.setName("btnRemoveUser"); // NOI18N
        btnRemoveUser.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveUserActionPerformed(evt);
            }
        });
        toolbarUsers.add(btnRemoveUser);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableUsers.setComponentPopupMenu(popupUsers);
        tableUsers.setName("tableUsers"); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${usersList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, userSearchResultList, eLProperty, tableUsers);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${userName}"));
        columnBinding.setColumnName("Username");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${firstName}"));
        columnBinding.setColumnName("First name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${lastName}"));
        columnBinding.setColumnName("Last name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${description}"));
        columnBinding.setColumnName("Description");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${groupsList}"));
        columnBinding.setColumnName("Groups");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${active}"));
        columnBinding.setColumnName("Active");
        columnBinding.setColumnClass(Boolean.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, userSearchResultList, org.jdesktop.beansbinding.ELProperty.create("${selectedUser}"), tableUsers, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(tableUsers);
        tableUsers.getColumnModel().getColumn(3).setCellRenderer(new TableCellTextAreaRenderer());
        tableUsers.getColumnModel().getColumn(4).setCellRenderer(new TableCellTextAreaRenderer());
        tableUsers.getColumnModel().getColumn(5).setMaxWidth(50);

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
                    .addComponent(toolbarUsers, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(toolbarUsers, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchUsers();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAddUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddUserActionPerformed
        addUser();
    }//GEN-LAST:event_btnAddUserActionPerformed

    private void btnEditUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditUserActionPerformed
        editUser();
    }//GEN-LAST:event_btnEditUserActionPerformed

    private void btnSetPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetPasswordActionPerformed
        editPassword();
    }//GEN-LAST:event_btnSetPasswordActionPerformed

    private void btnRemoveUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveUserActionPerformed
        removeUser();
    }//GEN-LAST:event_btnRemoveUserActionPerformed

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
        removeUser();
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

    public void removeUser() {
        if (userSearchResultList.getSelectedUser() != null) {
            if(userSearchResultList.getSelectedUser().getUserName().equals(SecurityBean.getCurrentUser().getUserName())){
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
    private javax.swing.JButton btnAddUser;
    private javax.swing.JButton btnEditUser;
    private javax.swing.JButton btnRemoveUser;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSetPassword;
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
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableUsers;
    private javax.swing.JToolBar toolbarUsers;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtUsername;
    private org.sola.clients.beans.security.UserSearchParamsBean userSearchParams;
    private org.sola.clients.beans.security.UserSearchAdvancedResultListBean userSearchResultList;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
