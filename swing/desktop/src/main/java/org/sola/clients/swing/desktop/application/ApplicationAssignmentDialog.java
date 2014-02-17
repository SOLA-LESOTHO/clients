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
package org.sola.clients.swing.desktop.application;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationSearchResultBean;
import org.sola.clients.beans.security.*;
import org.sola.common.RolesConstants;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * This dialog form is used to assign application to the user or unassign it
 * from him. <br />{@link UsersListBean} is used to bind the data on the form.
 */
public class ApplicationAssignmentDialog extends javax.swing.JDialog {

    public static final String ASSIGNMENT_CHANGED = "assignmentChanged";
    private List<ApplicationSearchResultBean> applications;

    /**
     * Default constructor
     *
     * @param applications List of applications to assign or unassign
     */
    public ApplicationAssignmentDialog(List<ApplicationSearchResultBean> applications,
            java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.applications = applications;        
        initComponents();
        groupsList.loadGroups(true);
        cbxGroups.setSelectedIndex(0);
        appStages.loadList(true);
        cbxAction.setSelectedIndex(0);
        usersList.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(UserSearchAdvancedResultListBean.SELECTED_USER_PROPERTY)) {
                    customizeUserButtons((UserSearchAdvancedResultBean) evt.getNewValue());
                }
            }
        });
        customizeUserButtons(null);
        customizeForm();
        
    }
    
    private void customizeUserButtons(UserSearchAdvancedResultBean userSearchResult) {
        cbxUsers.setEnabled(userSearchResult != null);
    }
    
    /**Searches users with the given criteria.*/
    private void searchUsers() {
        usersList.searchUsers(userSearchParams);
        if (usersList.getUsersList().size() < 1) {
            MessageUtility.displayMessage(ClientMessage.ADMIN_USERS_NO_FOUND);
        }
    }

    private void customizeForm() {
        cbxUsers.setEnabled(false);
        cbxGroups.setEnabled(false);

        if (applications == null || applications.size() < 1) {
            btnAssign.setEnabled(false);
            return;
        }

        usersList.setSelectedUserById(SecurityBean.getCurrentUser().getId());
        cbxUsers.setEnabled(SecurityBean.isInRole(RolesConstants.APPLICATION_ASSIGN_TO_OTHERS));
        cbxGroups.setEnabled(SecurityBean.isInRole(RolesConstants.APPLICATION_ASSIGN_TO_OTHERS));
        btnAssign.setEnabled(SecurityBean.isInRole(RolesConstants.APPLICATION_ASSIGN_TO_OTHERS));

        if (usersList.getSelectedUser() != null && !btnAssign.isEnabled()
                && usersList.getSelectedUser().getId().equals(SecurityBean.getCurrentUser().getId())) {
            btnAssign.setEnabled(SecurityBean.isInRole(RolesConstants.APPLICATION_ASSIGN_TO_YOURSELF));
        }
    }

    /**
     * Assign applications
     */
    private void assign() {  
        
        if (usersList.getSelectedUser() == null) {
            MessageUtility.displayMessage(ClientMessage.APPLICATION_NOSEL_USER);
            return;
        }
        for (Iterator<ApplicationSearchResultBean> it = applications.iterator(); it.hasNext();) {
            ApplicationSearchResultBean app = it.next();   
            String selectedStageCode = appStages.getSelectedApplicationStageType().getCode();                      
            ApplicationBean.assignUser(app, usersList.getSelectedUser().getId(), selectedStageCode);                        
        }

        MessageUtility.displayMessage(ClientMessage.APPLICATION_ASSIGNED);
        this.firePropertyChange(ASSIGNMENT_CHANGED, false, true);
        this.dispose();      
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        groupsList = new org.sola.clients.beans.security.GroupSummaryListBean();
        userSearchParams = new org.sola.clients.beans.security.UserSearchParamsBean();
        usersList = new org.sola.clients.beans.security.UserSearchAdvancedResultListBean();
        appStages = new org.sola.clients.beans.referencedata.ApplicationStageTypeListBean();
        appBean = new org.sola.clients.beans.application.ApplicationBean();
        stageTypeBean = new org.sola.clients.beans.referencedata.ApplicationStageTypeBean();
        jToolBar1 = new javax.swing.JToolBar();
        btnAssign = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbxUsers = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cbxGroups = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cbxAction = new javax.swing.JComboBox();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/application/Bundle"); // NOI18N
        setTitle(bundle.getString("ApplicationAssignmentDialog.title")); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnAssign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/assign.png"))); // NOI18N
        btnAssign.setText(bundle.getString("ApplicationAssignmentDialog.btnAssign.text")); // NOI18N
        btnAssign.setFocusable(false);
        btnAssign.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAssignActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAssign);

        jLabel1.setText(bundle.getString("ApplicationAssignmentDialog.jLabel1.text")); // NOI18N

        cbxUsers.setEnabled(false);
        cbxUsers.setName(bundle.getString("ApplicationAssignmentDialog.cbxUsers.name")); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${usersList}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, usersList, eLProperty, cbxUsers);
        bindingGroup.addBinding(jComboBoxBinding);
        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, usersList, org.jdesktop.beansbinding.ELProperty.create("${selectedUser}"), cbxUsers, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(cbxUsers, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxUsers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 36, Short.MAX_VALUE))
        );

        cbxUsers.getAccessibleContext().setAccessibleName(bundle.getString("ApplicationAssignmentDialog.cbxUsers.AccessibleContext.accessibleName")); // NOI18N

        jLabel2.setText(bundle.getString("ApplicationAssignmentDialog.jLabel2.text")); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${groupSummaryList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, groupsList, eLProperty, cbxGroups);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, userSearchParams, org.jdesktop.beansbinding.ELProperty.create("${groupBean}"), cbxGroups, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        cbxGroups.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxGroupsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(cbxGroups, javax.swing.GroupLayout.Alignment.TRAILING, 0, 179, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 55, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addContainerGap(24, Short.MAX_VALUE)
                    .addComponent(cbxGroups, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(25, Short.MAX_VALUE)))
        );

        jLabel3.setText(bundle.getString("ApplicationAssignmentDialog.jLabel3.text")); // NOI18N

        cbxAction.setName(bundle.getString("ApplicationAssignmentDialog.cbxAction.name")); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${applicationStageTypes}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appStages, eLProperty, cbxAction);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, appStages, org.jdesktop.beansbinding.ELProperty.create("${selectedApplicationStageType}"), cbxAction, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        cbxAction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxActionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 268, Short.MAX_VALUE))
            .addComponent(cbxAction, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 36, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAssignActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAssignActionPerformed
        assign();
    }//GEN-LAST:event_btnAssignActionPerformed

    private void cbxGroupsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxGroupsActionPerformed
        searchUsers();
    }//GEN-LAST:event_cbxGroupsActionPerformed

    private void cbxActionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxActionActionPerformed
        //
    }//GEN-LAST:event_cbxActionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.beans.application.ApplicationBean appBean;
    private org.sola.clients.beans.referencedata.ApplicationStageTypeListBean appStages;
    private javax.swing.JButton btnAssign;
    private javax.swing.JComboBox cbxAction;
    private javax.swing.JComboBox cbxGroups;
    private javax.swing.JComboBox cbxUsers;
    private org.sola.clients.beans.security.GroupSummaryListBean groupsList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JToolBar jToolBar1;
    private org.sola.clients.beans.referencedata.ApplicationStageTypeBean stageTypeBean;
    private org.sola.clients.beans.security.UserSearchParamsBean userSearchParams;
    private org.sola.clients.beans.security.UserSearchAdvancedResultListBean usersList;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
