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
package org.sola.clients.swing.admin.security;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.sola.clients.beans.security.DepartmentBean;
import org.sola.clients.beans.security.DepartmentSummaryBean;
import org.sola.clients.beans.security.DepartmentSummaryListBean;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * @author Charlizza
 */
public class DepartmentsManagementPanel extends ContentPanel {

    /** Default constructor. */
    public DepartmentsManagementPanel() {
        initComponents();
        departmentSummaryList.loadDepartments(false);
        departmentSummaryList.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(DepartmentSummaryListBean.SELECTED_DEPARTMENT_PROPERTY)) {
                    customizeDepartmentButtons((DepartmentSummaryBean) evt.getNewValue());
                }
            }
        });
        customizeDepartmentButtons(null);
    }

    /** 
     * Enables or disables department management buttons, depending on selection in 
     * the departments table and user rights. 
     */
    private void customizeDepartmentButtons(DepartmentSummaryBean departmentSummaryBean) {
        btnRemoveDepartment.setEnabled(departmentSummaryBean != null);
        btnEditDepartment.setEnabled(departmentSummaryBean != null);
        //menuEdit.setEnabled(btnEditDepartment.isEnabled());
        //menuRemove.setEnabled(btnRemoveDepartment.isEnabled());
    }

    /** Shows department panel. */
    private void showDepartment(final DepartmentBean department) {
        DepartmentPanelForm panel = new DepartmentPanelForm(department, true, department != null, false);
        panel.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(DepartmentPanelForm.DEPARTMENT_SAVED_PROPERTY)) {
                    if (department == null) {
                        ((DepartmentPanelForm) evt.getSource()).setDepartmentBean(null);
                    }
                    departmentSummaryList.loadDepartments(false);
                }
            }
        });
        getMainContentPanel().addPanel(panel, MainContentPanel.CARD_ADMIN_DEPARTMENT, true);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        departmentSummaryList = new org.sola.clients.beans.security.DepartmentSummaryListBean();
        headerPanel1 = new org.sola.clients.swing.ui.HeaderPanel();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnRemoveDepartment = new javax.swing.JButton();
        btnAddDepartment = new javax.swing.JButton();
        btnEditDepartment = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableWithDefaultStyles1 = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();

        setCloseOnHide(true);
        setHeaderPanel(headerPanel1);

        headerPanel1.setTitleText("Departments");

        jToolBar1.setRollover(true);
        jToolBar1.setOpaque(false);

        btnRemoveDepartment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/admin/security/Bundle"); // NOI18N
        btnRemoveDepartment.setText(bundle.getString("GroupsManagementPanel.btnRemoveGroup.text")); // NOI18N
        btnRemoveDepartment.setFocusable(false);
        btnRemoveDepartment.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRemoveDepartment.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnRemoveDepartment.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveDepartment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveDepartmentActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRemoveDepartment);

        btnAddDepartment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAddDepartment.setText(bundle.getString("GroupsManagementPanel.btnAddGroup.text")); // NOI18N
        btnAddDepartment.setFocusable(false);
        btnAddDepartment.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAddDepartment.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnAddDepartment.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddDepartment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDepartmentActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAddDepartment);

        btnEditDepartment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        btnEditDepartment.setText(bundle.getString("GroupsManagementPanel.btnEditGroup.text")); // NOI18N
        btnEditDepartment.setFocusable(false);
        btnEditDepartment.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnEditDepartment.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnEditDepartment.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnEditDepartment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditDepartmentActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEditDepartment);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${departmentSummaryList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, departmentSummaryList, eLProperty, jTableWithDefaultStyles1);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${name}"));
        columnBinding.setColumnName("Name");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${description}"));
        columnBinding.setColumnName("Description");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, departmentSummaryList, org.jdesktop.beansbinding.ELProperty.create("${selectedDepartment}"), jTableWithDefaultStyles1, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(jTableWithDefaultStyles1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRemoveDepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveDepartmentActionPerformed
        removeDepartment();
    }//GEN-LAST:event_btnRemoveDepartmentActionPerformed

    private void btnAddDepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDepartmentActionPerformed
        addDepartment();
    }//GEN-LAST:event_btnAddDepartmentActionPerformed

    private void btnEditDepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditDepartmentActionPerformed
        editDepartment();
    }//GEN-LAST:event_btnEditDepartmentActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddDepartment;
    private javax.swing.JButton btnEditDepartment;
    private javax.swing.JButton btnRemoveDepartment;
    private org.sola.clients.beans.security.DepartmentSummaryListBean departmentSummaryList;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles jTableWithDefaultStyles1;
    private javax.swing.JToolBar jToolBar1;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    private void removeDepartment() {
        if (departmentSummaryList.getSelectedDepartment() != null
                && MessageUtility.displayMessage(ClientMessage.ADMIN_CONFIRM_DELETE_DEPARTMENT)
                == MessageUtility.BUTTON_ONE) {
            DepartmentBean.removeDepartment(departmentSummaryList.getSelectedDepartment().getId());
            departmentSummaryList.loadDepartments(false);
        }
    }

    private void addDepartment() {
        showDepartment(null);
    }

    private void editDepartment() {
        if (departmentSummaryList.getSelectedDepartment() != null) {
            showDepartment(DepartmentBean.getDepartment(departmentSummaryList.getSelectedDepartment().getId()));
        }
    }
}
