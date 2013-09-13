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
package org.sola.clients.swing.ui.administrative;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.sola.clients.beans.administrative.BaUnitSearchResultBean;
import org.sola.clients.beans.administrative.BaUnitSearchResultListBean;
import org.sola.clients.beans.referencedata.LandUseTypeListBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.swing.common.LafManager;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.ui.renderers.TableCellTextAreaRenderer;
import org.sola.common.RolesConstants;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Allows to search BA Units.
 */
public class BaUnitSearchPanel extends javax.swing.JPanel {

    public static final String BAUNIT_OPEN = "baUnitOpen";
    public static final String BAUNIT_SELECTED = "baUnitSelected";
    public static final String BAUNIT_CORRECTION = "baCorrection";
    private boolean readOnly = false;
    
    /**
     * Default constructor.
     */
    public BaUnitSearchPanel() {
        initComponents();

        customieButtons(null);
        baUnitSearchResults.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(BaUnitSearchResultListBean.SELECTED_BAUNIT_SEARCH_RESULT_PROPERTY)) {
                    firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                    customieButtons((BaUnitSearchResultBean) evt.getNewValue());
                }
            }
        });
        // Make correction button invisible by default
        setShowCorrectionButton(false);
    }
    
    private LandUseTypeListBean createLandUseTypes(){
        if(landUseTypes == null)
            landUseTypes = new LandUseTypeListBean(true);
        
        return landUseTypes;
    }
    private void customieButtons(BaUnitSearchResultBean searchResult) {
        boolean enabled = searchResult != null && !readOnly;
        boolean canCorrect = SecurityBean.isInRole(RolesConstants.APPLICATION_CORRECTION_SERVICE_START);
        
        btnOpenBaUnit.setEnabled(enabled);
        btnSelect.setEnabled(enabled);
        btnCorrection.setEnabled(enabled && canCorrect);
        menuOpenBaUnit.setEnabled(btnOpenBaUnit.isEnabled());
        menuCorrection.setEnabled(btnCorrection.isEnabled());
        menuSelect.setEnabled(btnSelect.isEnabled());
    }

    /**
     * Indicates whether open button is shown or not.
     */
    public boolean isShowOpenButton() {
        return btnOpenBaUnit.isVisible();
    }

    /**
     * Sets visibility of open button.
     */
    public void setShowOpenButton(boolean show) {
        btnOpenBaUnit.setVisible(show);
        menuOpenBaUnit.setVisible(show);
        separator1.setVisible(show || btnSelect.isVisible() || btnCorrection.isVisible());
    }
    
    /**
     * Indicates whether correction button is shown or not.
     */
    public boolean isShowCorrectionButton() {
        return btnCorrection.isVisible();
    }

    /**
     * Sets visibility of correction button.
     */
    public void setShowCorrectionButton(boolean show) {
        btnCorrection.setVisible(show);
        menuCorrection.setVisible(show);
        separator1.setVisible(show || btnSelect.isVisible() || btnOpenBaUnit.isVisible());
    }
    
    /**
     * Indicates whether Select button is shown or not.
     */
    public boolean isShowSelectButton() {
        return btnSelect.isVisible();
    }

    /**
     * Sets visibility of Select button.
     */
    public void setShowSelectButton(boolean show) {
        btnSelect.setVisible(show);
        menuSelect.setVisible(show);
        separator1.setVisible(show || btnOpenBaUnit.isVisible() || btnCorrection.isVisible());
    }

    /** Returns true if panel in read only mode. */
    public boolean isReadOnly() {
        return readOnly;
    }

    /** Sets readOnly mode for the panel. */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        customieButtons(null);
    }

    /**
     * Returns selected search result.
     */
    public BaUnitSearchResultBean getSelectedSearchResult() {
        return baUnitSearchResults.getSelectedBaUnitSearchResult();
    }

    private void clearSearchResults(){
        baUnitSearchParams.setNameFirstPart(null);
        baUnitSearchParams.setNameLastPart(null);
        baUnitSearchParams.setOwnerName(null);
        baUnitSearchParams.setLeaseNumber(null);
        baUnitSearchParams.setLandUseCode(null);
        baUnitSearchResults.getBaUnitSearchResults().clear();
        lblSearchResultCount.setText("0");
    }
    
    private void fireEvent(String eventName) {
        if (baUnitSearchResults.getSelectedBaUnitSearchResult() != null) {
            firePropertyChange(eventName, null, baUnitSearchResults.getSelectedBaUnitSearchResult());
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        baUnitSearchResults = new org.sola.clients.beans.administrative.BaUnitSearchResultListBean();
        baUnitSearchParams = new org.sola.clients.beans.administrative.BaUnitSearchParamsBean();
        popUpSearchResults = new javax.swing.JPopupMenu();
        menuOpenBaUnit = new javax.swing.JMenuItem();
        menuCorrection = new javax.swing.JMenuItem();
        menuSelect = new javax.swing.JMenuItem();
        landUseTypes = createLandUseTypes();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNameFirstPart = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNameLastPart = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtLeaseNumber = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtRightholder = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        cbxLandUse = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSearchResults = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jToolBar1 = new javax.swing.JToolBar();
        btnOpenBaUnit = new javax.swing.JButton();
        btnCorrection = new javax.swing.JButton();
        btnSelect = new org.sola.clients.swing.common.buttons.BtnSelect();
        separator1 = new javax.swing.JToolBar.Separator();
        lblSearchResult = new javax.swing.JLabel();
        lblSearchResultCount = new javax.swing.JLabel();

        popUpSearchResults.setName("popUpSearchResults"); // NOI18N

        menuOpenBaUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/ui/administrative/Bundle"); // NOI18N
        menuOpenBaUnit.setText(bundle.getString("BaUnitSearchPanel.menuOpenBaUnit.text")); // NOI18N
        menuOpenBaUnit.setName("menuOpenBaUnit"); // NOI18N
        menuOpenBaUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenBaUnitActionPerformed(evt);
            }
        });
        popUpSearchResults.add(menuOpenBaUnit);

        menuCorrection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        menuCorrection.setText(bundle.getString("BaUnitSearchPanel.menuCorrection.text")); // NOI18N
        menuCorrection.setName(bundle.getString("BaUnitSearchPanel.menuCorrection.name")); // NOI18N
        menuCorrection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCorrectionActionPerformed(evt);
            }
        });
        popUpSearchResults.add(menuCorrection);

        menuSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/select.png"))); // NOI18N
        menuSelect.setText(bundle.getString("BaUnitSearchPanel.menuSelect.text")); // NOI18N
        menuSelect.setName(bundle.getString("BaUnitSearchPanel.menuSelect.name")); // NOI18N
        menuSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSelectActionPerformed(evt);
            }
        });
        popUpSearchResults.add(menuSelect);

        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setLayout(new java.awt.GridLayout(1, 3, 15, 0));

        jPanel1.setName("jPanel1"); // NOI18N

        jLabel1.setText(bundle.getString("BaUnitSearchPanel.jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        txtNameFirstPart.setName("txtNameFirstPart"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitSearchParams, org.jdesktop.beansbinding.ELProperty.create("${nameFirstPart}"), txtNameFirstPart, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addContainerGap())
            .addComponent(txtNameFirstPart)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNameFirstPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel1);

        jPanel3.setName("jPanel3"); // NOI18N

        jLabel3.setText(bundle.getString("BaUnitSearchPanel.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        txtNameLastPart.setName("txtNameLastPart"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitSearchParams, org.jdesktop.beansbinding.ELProperty.create("${nameLastPart}"), txtNameLastPart, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addContainerGap(17, Short.MAX_VALUE))
            .addComponent(txtNameLastPart)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNameLastPart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel3);

        jPanel8.setName(bundle.getString("BaUnitSearchPanel.jPanel8.name")); // NOI18N

        jLabel6.setText(bundle.getString("BaUnitSearchPanel.jLabel6.text")); // NOI18N
        jLabel6.setName(bundle.getString("BaUnitSearchPanel.jLabel6.name")); // NOI18N

        txtLeaseNumber.setName(bundle.getString("BaUnitSearchPanel.txtLeaseNumber.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitSearchParams, org.jdesktop.beansbinding.ELProperty.create("${leaseNumber}"), txtLeaseNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(0, 4, Short.MAX_VALUE))
            .addComponent(txtLeaseNumber)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLeaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 55, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel8);

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel2.setText(bundle.getString("BaUnitSearchPanel.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        txtRightholder.setName("txtRightholder"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitSearchParams, org.jdesktop.beansbinding.ELProperty.create("${ownerName}"), txtRightholder, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addContainerGap(47, Short.MAX_VALUE))
            .addComponent(txtRightholder, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRightholder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel2);

        jPanel9.setName(bundle.getString("BaUnitSearchPanel.jPanel9.name")); // NOI18N

        cbxLandUse.setName(bundle.getString("BaUnitSearchPanel.cbxLandUse.name")); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${codeValues}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, landUseTypes, eLProperty, cbxLandUse);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitSearchParams, org.jdesktop.beansbinding.ELProperty.create("${landUseCode}"), cbxLandUse, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        jLabel7.setText(bundle.getString("BaUnitSearchPanel.jLabel7.text")); // NOI18N
        jLabel7.setName(bundle.getString("BaUnitSearchPanel.jLabel7.name")); // NOI18N

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addContainerGap(35, Short.MAX_VALUE))
            .addComponent(cbxLandUse, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxLandUse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel9);

        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setLayout(new java.awt.GridLayout(1, 2, 10, 0));

        jPanel7.setName(bundle.getString("BaUnitSearchPanel.jPanel7.name")); // NOI18N

        jLabel5.setText(bundle.getString("BaUnitSearchPanel.jLabel5.text")); // NOI18N
        jLabel5.setName(bundle.getString("BaUnitSearchPanel.jLabel5.name")); // NOI18N

        btnClear.setText(bundle.getString("BaUnitSearchPanel.btnClear.text")); // NOI18N
        btnClear.setName(bundle.getString("BaUnitSearchPanel.btnClear.name")); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear)
                .addGap(0, 52, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel7);

        jPanel6.setName(bundle.getString("BaUnitSearchPanel.jPanel6.name")); // NOI18N

        jLabel4.setText(bundle.getString("BaUnitSearchPanel.jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        btnSearch.setText(bundle.getString("BaUnitSearchPanel.btnSearch.text")); // NOI18N
        btnSearch.setName("btnSearch"); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 27, Short.MAX_VALUE))
            .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch)
                .addGap(0, 52, Short.MAX_VALUE))
        );

        jPanel5.add(jPanel6);

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tableSearchResults.setComponentPopupMenu(popUpSearchResults);
        tableSearchResults.setName("tableSearchResults"); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${baUnitSearchResults}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitSearchResults, eLProperty, tableSearchResults);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${landUseCode}"));
        columnBinding.setColumnName("Land Use Code");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rightholders}"));
        columnBinding.setColumnName("Rightholders");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${leaseNumber}"));
        columnBinding.setColumnName("Lease Number");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${originalRegistrationDate}"));
        columnBinding.setColumnName("Original Registration Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registrationStatus.displayValue}"));
        columnBinding.setColumnName("Registration Status.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitSearchResults, org.jdesktop.beansbinding.ELProperty.create("${selectedBaUnitSearchResult}"), tableSearchResults, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        tableSearchResults.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSearchResultsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableSearchResults);
        tableSearchResults.getColumnModel().getColumn(0).setPreferredWidth(120);
        tableSearchResults.getColumnModel().getColumn(0).setMaxWidth(120);
        tableSearchResults.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("BaUnitSearchPanel.tableSearchResults.columnModel.title1")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("BaUnitSearchPanel.tableSearchResults.columnModel.title3")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(1).setCellRenderer(new TableCellTextAreaRenderer());
        tableSearchResults.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableSearchResults.getColumnModel().getColumn(2).setMaxWidth(120);
        tableSearchResults.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("BaUnitSearchPanel.tableSearchResults.columnModel.title1_1")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(3).setPreferredWidth(100);
        tableSearchResults.getColumnModel().getColumn(3).setMaxWidth(120);
        tableSearchResults.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("BaUnitSearchPanel.tableSearchResults.columnModel.title4_1")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(4).setPreferredWidth(100);
        tableSearchResults.getColumnModel().getColumn(4).setMaxWidth(120);
        tableSearchResults.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("BaUnitSearchPanel.tableSearchResults.columnModel.title4")); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        btnOpenBaUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        btnOpenBaUnit.setText(bundle.getString("BaUnitSearchPanel.btnOpenBaUnit.text")); // NOI18N
        btnOpenBaUnit.setFocusable(false);
        btnOpenBaUnit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOpenBaUnit.setName("btnOpenBaUnit"); // NOI18N
        btnOpenBaUnit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenBaUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenBaUnitActionPerformed(evt);
            }
        });
        jToolBar1.add(btnOpenBaUnit);

        btnCorrection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        btnCorrection.setText(bundle.getString("BaUnitSearchPanel.btnCorrection.text")); // NOI18N
        btnCorrection.setFocusable(false);
        btnCorrection.setName(bundle.getString("BaUnitSearchPanel.btnCorrection.name")); // NOI18N
        btnCorrection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorrectionActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCorrection);

        btnSelect.setName(bundle.getString("BaUnitSearchPanel.btnSelect.name")); // NOI18N
        btnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSelect);

        separator1.setName("separator1"); // NOI18N
        jToolBar1.add(separator1);

        lblSearchResult.setText(bundle.getString("BaUnitSearchPanel.lblSearchResult.text")); // NOI18N
        lblSearchResult.setName("lblSearchResult"); // NOI18N
        jToolBar1.add(lblSearchResult);

        lblSearchResultCount.setFont(LafManager.getInstance().getLabFontBold());
        lblSearchResultCount.setText(bundle.getString("BaUnitSearchPanel.lblSearchResultCount.text")); // NOI18N
        lblSearchResultCount.setName("lblSearchResultCount"); // NOI18N
        jToolBar1.add(lblSearchResultCount);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 492, Short.MAX_VALUE)
                .addGap(7, 7, 7)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents
    
    
     public void clickFind() {
        btnSearchActionPerformed(null);
    }
    
    
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_PROPERTY_SEARCHING));
                baUnitSearchResults.search(baUnitSearchParams);
                return null;
            }

            @Override
            public void taskDone() {
                lblSearchResultCount.setText(Integer.toString(baUnitSearchResults.getBaUnitSearchResults().size()));
                if (baUnitSearchResults.getBaUnitSearchResults().size() < 1) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_NO_RESULTS);
                } else if (baUnitSearchResults.getBaUnitSearchResults().size() > 100) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_TOO_MANY_RESULTS, new String[]{"100"});
                }
            }
        };
        TaskManager.getInstance().runTask(t);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnOpenBaUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenBaUnitActionPerformed
        fireEvent(BAUNIT_OPEN);
    }//GEN-LAST:event_btnOpenBaUnitActionPerformed

    private void menuOpenBaUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenBaUnitActionPerformed
        fireEvent(BAUNIT_OPEN);
    }//GEN-LAST:event_menuOpenBaUnitActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearSearchResults();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectActionPerformed
        fireEvent(BAUNIT_SELECTED);
    }//GEN-LAST:event_btnSelectActionPerformed

    private void menuSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSelectActionPerformed
        fireEvent(BAUNIT_SELECTED);
    }//GEN-LAST:event_menuSelectActionPerformed

    private void btnCorrectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorrectionActionPerformed
        fireEvent(BAUNIT_CORRECTION);
    }//GEN-LAST:event_btnCorrectionActionPerformed

    private void menuCorrectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCorrectionActionPerformed
        fireEvent(BAUNIT_CORRECTION);
    }//GEN-LAST:event_menuCorrectionActionPerformed

    private void tableSearchResultsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSearchResultsMouseClicked
        if(evt.getClickCount()==2 && btnOpenBaUnit.isEnabled() && btnOpenBaUnit.isVisible()){
            fireEvent(BAUNIT_OPEN);
        }
    }//GEN-LAST:event_tableSearchResultsMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.beans.administrative.BaUnitSearchParamsBean baUnitSearchParams;
    private org.sola.clients.beans.administrative.BaUnitSearchResultListBean baUnitSearchResults;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnCorrection;
    private javax.swing.JButton btnOpenBaUnit;
    private javax.swing.JButton btnSearch;
    private org.sola.clients.swing.common.buttons.BtnSelect btnSelect;
    private javax.swing.JComboBox cbxLandUse;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private org.sola.clients.beans.referencedata.LandUseTypeListBean landUseTypes;
    private javax.swing.JLabel lblSearchResult;
    private javax.swing.JLabel lblSearchResultCount;
    private javax.swing.JMenuItem menuCorrection;
    private javax.swing.JMenuItem menuOpenBaUnit;
    private javax.swing.JMenuItem menuSelect;
    private javax.swing.JPopupMenu popUpSearchResults;
    private javax.swing.JToolBar.Separator separator1;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableSearchResults;
    private javax.swing.JTextField txtLeaseNumber;
    private javax.swing.JTextField txtNameFirstPart;
    private javax.swing.JTextField txtNameLastPart;
    private javax.swing.JTextField txtRightholder;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
