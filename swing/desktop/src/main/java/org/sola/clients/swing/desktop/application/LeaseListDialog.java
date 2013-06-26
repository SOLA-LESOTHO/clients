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
package org.sola.clients.swing.desktop.application;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.sola.clients.beans.administrative.LeaseBean;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.controls.SolaObservableList;

/**
 * Dialog for selecting lease.
 */
public class LeaseListDialog extends javax.swing.JDialog {
    
    public static final String SELECTED_LEASE = "selectedLease";
    public static final String LEASE_SELECTED = "leaseSelected";
    private SolaObservableList<LeaseBean> leases;
    private LeaseBean selectedLease;
    
    /**
     * Default constructor.
     */
    public LeaseListDialog(java.awt.Frame parent, SolaObservableList<LeaseBean> leases, boolean modal) {
        super(parent, modal);
        this.leases = leases;
        initComponents();
        this.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(SELECTED_LEASE)){
                    customizeButtons();
                }
            }
        });
        customizeButtons();
    }

    private void customizeButtons(){
        btnSelect.setEnabled(selectedLease!=null);
        menuSelect.setEnabled(btnSelect.isEnabled());
    }

    public SolaObservableList<LeaseBean> getLeases() {
        return leases;
    }

    public void setLeases(SolaObservableList<LeaseBean> leases) {
        this.leases = leases;
    }
    
    public LeaseBean getSelectedLease() {
        return selectedLease;
    }

    public void setSelectedLease(LeaseBean selectedLease) {
        this.selectedLease = selectedLease;
        firePropertyChange(SELECTED_LEASE, null, this.selectedLease);
    }
    
    public void select(){
        if(selectedLease!=null){
            firePropertyChange(LEASE_SELECTED, null, selectedLease);
            dispose();
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        popupLeases = new javax.swing.JPopupMenu();
        menuSelect = new org.sola.clients.swing.common.menuitems.MenuSelect();
        jToolBar1 = new javax.swing.JToolBar();
        btnSelect = new org.sola.clients.swing.common.buttons.BtnSelect();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableWithDefaultStyles1 = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();

        popupLeases.add(menuSelect);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.add(btnSelect);

        jTableWithDefaultStyles1.setComponentPopupMenu(popupLeases);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${leases}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, jTableWithDefaultStyles1);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${leaseNumber}"));
        columnBinding.setColumnName("Lease Number");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${startDate}"));
        columnBinding.setColumnName("Start Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${expirationDate}"));
        columnBinding.setColumnName("Expiration Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${groundRent}"));
        columnBinding.setColumnName("Ground Rent");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${stampDuty}"));
        columnBinding.setColumnName("Stamp Duty");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${status.displayValue}"));
        columnBinding.setColumnName("Status.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${selectedLease}"), jTableWithDefaultStyles1, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(jTableWithDefaultStyles1);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/application/Bundle"); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("LeaseListDialog.jTableWithDefaultStyles1.columnModel.title0_1")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("LeaseListDialog.jTableWithDefaultStyles1.columnModel.title1_1")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("LeaseListDialog.jTableWithDefaultStyles1.columnModel.title2_1")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("LeaseListDialog.jTableWithDefaultStyles1.columnModel.title3_1")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("LeaseListDialog.jTableWithDefaultStyles1.columnModel.title4")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(5).setHeaderValue(bundle.getString("LeaseListDialog.jTableWithDefaultStyles1.columnModel.title5")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.common.buttons.BtnSelect btnSelect;
    private javax.swing.JScrollPane jScrollPane1;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles jTableWithDefaultStyles1;
    private javax.swing.JToolBar jToolBar1;
    private org.sola.clients.swing.common.menuitems.MenuSelect menuSelect;
    private javax.swing.JPopupMenu popupLeases;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
