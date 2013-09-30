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
package org.sola.clients.swing.gis.ui.control;

import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.gis.beans.AbstractListSpatialBean;
import org.sola.clients.swing.gis.beans.CadastreObjectBean;
import org.sola.clients.swing.gis.beans.CadastreObjectListBean;
import org.sola.clients.swing.gis.beans.SpatialBean;
import org.sola.clients.swing.ui.reports.ReportViewerForm;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * A User Interface component that handles the management of the cadastre
 * objects.
 *
 * @author Elton Manoku
 */
public class CadastreObjectListPanel extends javax.swing.JPanel {

    private CadastreObjectListBean theBean = null;
    private final String CARD_PARCEL = "CARD_PARCEL";
    private final String CARD_LIST = "CARD_LIST";
    private String applicationNumber;
    private String applicantName;
    
    /**
     * This constructor must be used to initialize the bean.
     */
    public CadastreObjectListPanel(CadastreObjectListBean bean, String applicationNumber, String applicantName) {
        this.theBean = bean;
        this.applicationNumber = applicationNumber;
        this.applicantName = applicantName;
        initComponents();
        // Add a listner to the bean property of selected bean
        theBean.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(AbstractListSpatialBean.SELECTED_BEAN_PROPERTY)) {
                    customizeButtons((SpatialBean) evt.getNewValue());
                }
            }
        });
        customizeButtons(null);
    }

    private void showParcelPanel(boolean show) {
        if (show) {
            ((CardLayout) pnlCards.getLayout()).show(pnlCards, CARD_PARCEL);
        } else {
            ((CardLayout) pnlCards.getLayout()).show(pnlCards, CARD_LIST);
        }
    }

    /**
     * It changes the availability of buttons based in the selected bean
     *
     * @param selectedSource
     */
    private void customizeButtons(SpatialBean selectedSource) {
        boolean enabled = selectedSource != null;
        cmdRemove.setEnabled(enabled);
        btnEdit.setEnabled(enabled);
        btnPrint.setEnabled(enabled);
        menuEdit.setEnabled(btnEdit.isEnabled());
        menuRemove.setEnabled(cmdRemove.isEnabled());
        menuPrint.setEnabled(btnPrint.isEnabled());
    }

    /**
     * This constructor is only for the designer.
     */
    public CadastreObjectListPanel() {
        initComponents();
    }

    /**
     * It creates the bean. It is called from the generated code.
     *
     * @return
     */
    private CadastreObjectListBean createBean() {
        if (this.theBean == null) {
            return new CadastreObjectListBean();
        }
        return this.theBean;
    }

    private void edit() {
        if (cadastreObjectListBean.getSelectedBean() == null) {
            return;
        }
        parcelPanel.setCadastreObjectBean(convertBean((CadastreObjectBean)cadastreObjectListBean.getSelectedBean()));
        showParcelPanel(true);
    }

    private org.sola.clients.beans.cadastre.CadastreObjectBean convertBean(CadastreObjectBean co){
        org.sola.clients.beans.cadastre.CadastreObjectBean bean =
                new org.sola.clients.beans.cadastre.CadastreObjectBean();
        bean.copyFromObject(co);
        // Fix problem with list area list duplications
        bean.getSpatialValueAreaList().clear();
        bean.getSpatialValueAreaList().addAll(((CadastreObjectBean) 
                cadastreObjectListBean.getSelectedBean()).getSpatialValueAreaList());

        parcelPanel.setCadastreObjectBean(bean);
        return bean;
    }
    
    private void print(){
        print(convertBean((CadastreObjectBean)cadastreObjectListBean.getSelectedBean()));
    }
    
    private void print(org.sola.clients.beans.cadastre.CadastreObjectBean co) {
        if (cadastreObjectListBean.getSelectedBean() == null) {
            return;
        }
        ReportViewerForm form = new ReportViewerForm(
                ReportManager.getSurveyReport(co, applicationNumber, applicantName));
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }

    private void remove() {
        if (cadastreObjectListBean.getSelectedBean() != null
                && MessageUtility.displayMessage(ClientMessage.CONFIRM_DELETE_RECORD) == MessageUtility.BUTTON_ONE) {
            cadastreObjectListBean.getBeanList().remove(cadastreObjectListBean.getSelectedBean());
            cadastreObjectListBean.setSelectedBean(null);
        }
    }

    private void save() {
        if (cadastreObjectListBean.getSelectedBean() == null) {
            return;
        }
            
        parcelPanel.commitEdits();
        if (parcelPanel.getCadastreObjectBean().validate(true).size() <= 0) {
            cadastreObjectListBean.getSelectedBean().copyFromObject(parcelPanel.getCadastreObjectBean());
            // Fix problems with SOLA lists after mapping
            ((CadastreObjectBean)cadastreObjectListBean.getSelectedBean()).getSpatialValueAreaList().clear();
            ((CadastreObjectBean)cadastreObjectListBean.getSelectedBean()).getSpatialValueAreaList()
                    .addAll(parcelPanel.getCadastreObjectBean().getSpatialValueAreaList());
            ((CadastreObjectBean)cadastreObjectListBean.getSelectedBean()).getAddressList().filter();
            
            ((CadastreObjectBean)cadastreObjectListBean.getSelectedBean()).fireCalculatedFieldsUpdate();
            showParcelPanel(false);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        cadastreObjectListBean = createBean();
        popupParcels = new javax.swing.JPopupMenu();
        menuEdit = new org.sola.clients.swing.common.menuitems.MenuEdit();
        menuRemove = new org.sola.clients.swing.common.menuitems.MenuRemove();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuPrint = new javax.swing.JMenuItem();
        pnlCards = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnEdit = new org.sola.clients.swing.common.buttons.BtnEdit();
        cmdRemove = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnPrint = new org.sola.clients.swing.common.buttons.BtnPrint();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableWithDefaultStyles1 = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jPanel3 = new javax.swing.JPanel();
        parcelPanel = new org.sola.clients.swing.ui.cadastre.ParcelPanel();
        jToolBar2 = new javax.swing.JToolBar();
        btnSave = new org.sola.clients.swing.common.buttons.BtnSave();
        btnCancel = new org.sola.clients.swing.common.buttons.BtnCancel();

        menuEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditActionPerformed(evt);
            }
        });
        popupParcels.add(menuEdit);

        menuRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveActionPerformed(evt);
            }
        });
        popupParcels.add(menuRemove);
        popupParcels.add(jSeparator2);

        menuPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/print.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/gis/ui/control/Bundle"); // NOI18N
        menuPrint.setText(bundle.getString("CadastreObjectListPanel.menuPrint.text")); // NOI18N
        menuPrint.setActionCommand(bundle.getString("CadastreObjectListPanel.menuPrint.actionCommand")); // NOI18N
        menuPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuPrintActionPerformed(evt);
            }
        });
        popupParcels.add(menuPrint);

        setMinimumSize(new java.awt.Dimension(450, 180));
        setPreferredSize(new java.awt.Dimension(450, 180));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        pnlCards.setLayout(new java.awt.CardLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEdit);

        cmdRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        cmdRemove.setText(bundle.getString("CadastreObjectListPanel.cmdRemove.text")); // NOI18N
        cmdRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdRemoveActionPerformed(evt);
            }
        });
        jToolBar1.add(cmdRemove);
        jToolBar1.add(jSeparator1);

        btnPrint.setText(bundle.getString("CadastreObjectListPanel.btnPrint.text")); // NOI18N
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrint);

        jTableWithDefaultStyles1.setComponentPopupMenu(popupParcels);
        jTableWithDefaultStyles1.setPreferredSize(new java.awt.Dimension(450, 100));

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${beanList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectListBean, eLProperty, jTableWithDefaultStyles1);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${parcelCode}"));
        columnBinding.setColumnName("Parcel Code");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${officialArea}"));
        columnBinding.setColumnName("Official Area");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${valuationAmount}"));
        columnBinding.setColumnName("Valuation Amount");
        columnBinding.setColumnClass(java.math.BigDecimal.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${addressString}"));
        columnBinding.setColumnName("Address String");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${status.displayValue}"));
        columnBinding.setColumnName("Status.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, cadastreObjectListBean, org.jdesktop.beansbinding.ELProperty.create("${selectedBean}"), jTableWithDefaultStyles1, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane2.setViewportView(jTableWithDefaultStyles1);
        jTableWithDefaultStyles1.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("CadastreObjectListPanel.jTableWithDefaultStyles1.columnModel.title0_1")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("CadastreObjectListPanel.jTableWithDefaultStyles1.columnModel.title1_1")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("CadastreObjectListPanel.jTableWithDefaultStyles1.columnModel.title3_1")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("CadastreObjectListPanel.jTableWithDefaultStyles1.columnModel.title4")); // NOI18N
        jTableWithDefaultStyles1.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("CadastreObjectListPanel.jTableWithDefaultStyles1.columnModel.title5")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlCards.add(jPanel1, "CARD_LIST");

        parcelPanel.setReadOnly(false);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnSave.setText(bundle.getString("CadastreObjectListPanel.btnSave.text")); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSave);

        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        jToolBar2.add(btnCancel);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(parcelPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 548, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(parcelPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                .addGap(10, 10, 10))
        );

        pnlCards.add(jPanel3, "CARD_PARCEL");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlCards, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdRemoveActionPerformed
        remove();
    }//GEN-LAST:event_cmdRemoveActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        edit();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        print();
    }//GEN-LAST:event_btnPrintActionPerformed

    private void menuEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditActionPerformed
        edit();
    }//GEN-LAST:event_menuEditActionPerformed

    private void menuRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveActionPerformed
        remove();
    }//GEN-LAST:event_menuRemoveActionPerformed

    private void menuPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuPrintActionPerformed
        print();
    }//GEN-LAST:event_menuPrintActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        showParcelPanel(false);
    }//GEN-LAST:event_formComponentShown

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        showParcelPanel(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.common.buttons.BtnCancel btnCancel;
    private org.sola.clients.swing.common.buttons.BtnEdit btnEdit;
    private org.sola.clients.swing.common.buttons.BtnPrint btnPrint;
    private org.sola.clients.swing.common.buttons.BtnSave btnSave;
    private org.sola.clients.swing.gis.beans.CadastreObjectListBean cadastreObjectListBean;
    private javax.swing.JButton cmdRemove;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles jTableWithDefaultStyles1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private org.sola.clients.swing.common.menuitems.MenuEdit menuEdit;
    private javax.swing.JMenuItem menuPrint;
    private org.sola.clients.swing.common.menuitems.MenuRemove menuRemove;
    private org.sola.clients.swing.ui.cadastre.ParcelPanel parcelPanel;
    private javax.swing.JPanel pnlCards;
    private javax.swing.JPopupMenu popupParcels;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
