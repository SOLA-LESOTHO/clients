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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.clients.swing.bulkoperations.spatialobjects;

import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.swing.bulkoperations.beans.SpatialAttributeBean;
import org.sola.clients.swing.bulkoperations.beans.SpatialDestinationBean;

/**
 * The panel that defines the UI for the spatial destination type cadastre object.
 * 
 * @author Elton Manoku
 */
public class SpatialDestinationCadastreObjectPanel 
extends javax.swing.JPanel implements ISpatialDestinationUI{

    public static String PANEL_NAME = "SpatialDestinationCadastreObject";
    
    private ObservableList<SpatialAttributeBean> sourceAttributes =
            new SolaObservableList<SpatialAttributeBean>();
    /**
     * Creates new form SpatialDestinationCadastreObjectPanel
     */
    public SpatialDestinationCadastreObjectPanel() {
        initComponents();
        this.setName(PANEL_NAME);
    }

    @Override
    public ObservableList<SpatialAttributeBean> getSourceAttributes() {
        return sourceAttributes;
    }
    
    @Override
    public SpatialDestinationBean getBean(){
        return bean;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        bean = new org.sola.clients.swing.bulkoperations.beans.SpatialDestinationCadastreObjectBean();
        cadastreObjectTypeList = new org.sola.clients.beans.referencedata.CadastreObjectTypeListBean();
        txtNameLastPart = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        cmbNameFirstPart = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        chkGenerateFirstPart = new javax.swing.JCheckBox();
        cmbOfficialArea = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        cmbCadastreObjectType = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, bean, org.jdesktop.beansbinding.ELProperty.create("${nameLastPart}"), txtNameLastPart, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/bulkoperations/spatialobjects/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("SpatialDestinationCadastreObjectPanel.jLabel1.text")); // NOI18N

        cmbNameFirstPart.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${sourceAttributes}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, cmbNameFirstPart);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, bean, org.jdesktop.beansbinding.ELProperty.create("${nameFirstPart}"), cmbNameFirstPart, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        jLabel2.setText(bundle.getString("SpatialDestinationCadastreObjectPanel.jLabel2.text")); // NOI18N

        chkGenerateFirstPart.setText(bundle.getString("SpatialDestinationCadastreObjectPanel.chkGenerateFirstPart.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, bean, org.jdesktop.beansbinding.ELProperty.create("${generateFirstPart}"), chkGenerateFirstPart, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        chkGenerateFirstPart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkGenerateFirstPartActionPerformed(evt);
            }
        });

        cmbOfficialArea.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${sourceAttributes}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, cmbOfficialArea);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, bean, org.jdesktop.beansbinding.ELProperty.create("${officialArea}"), cmbOfficialArea, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        jLabel3.setText(bundle.getString("SpatialDestinationCadastreObjectPanel.jLabel3.text")); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${cadastreObjectTypeList}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectTypeList, eLProperty, cmbCadastreObjectType);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, bean, org.jdesktop.beansbinding.ELProperty.create("${cadastreObjectType}"), cmbCadastreObjectType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        jLabel4.setText(bundle.getString("SpatialDestinationCadastreObjectPanel.jLabel4.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(cmbCadastreObjectType, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtNameLastPart, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(chkGenerateFirstPart))
                    .addComponent(jLabel1))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(cmbNameFirstPart, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbOfficialArea, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNameLastPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbNameFirstPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkGenerateFirstPart)
                    .addComponent(cmbOfficialArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCadastreObjectType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void chkGenerateFirstPartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkGenerateFirstPartActionPerformed
        cmbNameFirstPart.setEnabled(!chkGenerateFirstPart.isSelected());
    }//GEN-LAST:event_chkGenerateFirstPartActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.bulkoperations.beans.SpatialDestinationCadastreObjectBean bean;
    private org.sola.clients.beans.referencedata.CadastreObjectTypeListBean cadastreObjectTypeList;
    private javax.swing.JCheckBox chkGenerateFirstPart;
    private javax.swing.JComboBox cmbCadastreObjectType;
    private javax.swing.JComboBox cmbNameFirstPart;
    private javax.swing.JComboBox cmbOfficialArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField txtNameLastPart;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
