/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
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
package org.sola.clients.swing.desktop.administrative;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import javax.swing.JFormattedTextField;
import javax.validation.groups.Default;
import org.geotools.swing.extended.util.GeometryUtility;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Bindings;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.administrative.RrrBean;
import org.sola.clients.beans.administrative.validation.LeaseValidationGroup;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.cadastre.CadastreObjectBean;
import org.sola.clients.beans.cadastre.CadastreObjectSummaryBean;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.referencedata.StatusConstants;
import org.sola.clients.swing.common.LafManager;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.desktop.party.PartyPanelForm;
import org.sola.clients.swing.desktop.party.PartySearchPanelForm;
import org.sola.clients.swing.desktop.source.DocumentsManagementExtPanel;
import org.sola.clients.swing.gis.ui.controlsbundle.ControlsBundleForBaUnit;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.renderers.FormattersFactory;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.desktop.cadastre.CadastreObjectSearchForm;
import org.sola.clients.swing.desktop.party.PartyListExtPanel;
import org.sola.clients.swing.ui.cadastre.CadastreObjectsSearchPanel;

/**
 * Form for managing simple ownership right. {@link RrrBean} is used to bind the
 * data on the form.
 */
public class SubleasePanel extends ContentPanel {

    private ApplicationBean applicationBean;
    private ApplicationServiceBean appService;
    private RrrBean.RRR_ACTION rrrAction;
    private BaUnitBean baUnit;
    private ControlsBundleForBaUnit mapControl;
    private boolean zoomedToPlot = false;
    public static final String UPDATED_RRR = "updatedRRR";

    private PartyListExtPanel createPartyListPanel() {
        return new PartyListExtPanel(rrrBean.getRightHolderList());
    }
    
    private DocumentsManagementExtPanel createDocumentsPanel() {
        if (rrrBean == null) {
            rrrBean = new RrrBean();
        }
        if (applicationBean == null) {
            applicationBean = new ApplicationBean();
        }

        boolean allowEdit = true;
        if (rrrAction == RrrBean.RRR_ACTION.VIEW) {
            allowEdit = false;
        }

        DocumentsManagementExtPanel panel = new DocumentsManagementExtPanel(
                rrrBean.getSourceList(), applicationBean, allowEdit);
        return panel;
    }

    private RrrBean CreateRrrBean() {
        if (rrrBean == null) {
            rrrBean = new RrrBean();
        }
        return rrrBean;
    }

    /**
     * Creates new form SimpleOwhershipPanel
     */
    public SubleasePanel(BaUnitBean baUnit, RrrBean rrrBean, ApplicationBean applicationBean,
            ApplicationServiceBean applicationService, RrrBean.RRR_ACTION rrrAction) {
        this.baUnit = baUnit;
        this.applicationBean = applicationBean;
        this.appService = applicationService;
        this.rrrAction = rrrAction;
        prepareRrrBean(rrrBean, rrrAction);
        initComponents();
        postInit();
    }

    private void postInit() {

        // Load the cadastre object if not already set
        if (rrrBean.getCadastreObjectId() != null && rrrBean.getCadastreObject() == null) {
            rrrBean.setCadastreObject(CadastreObjectBean.getCadastreObject(
                    rrrBean.getCadastreObjectId()));
            zoomedToPlot = false;
        }

        // Create the binding between the Parcel Details panel and the Cadastre Object on the rrrBean
        org.jdesktop.beansbinding.Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
                rrrBean, org.jdesktop.beansbinding.ELProperty.create("${cadastreObject}"),
                parcelPanel1, org.jdesktop.beansbinding.BeanProperty.create("cadastreObjectBean"), "parcelDetailsBinding");
        bindingGroup.addBinding(binding);
        bindingGroup.bind();

        customizeForm();
        saveRrrState();
    }

    private void customizeForm() {
        headerPanel.setTitleText(rrrBean.getRrrType().getDisplayValue());
        if (rrrAction == RrrBean.RRR_ACTION.NEW) {
            btnSave.setText(MessageUtility.getLocalizedMessage(
                    ClientMessage.GENERAL_LABELS_CREATE_AND_CLOSE).getMessage());

            if (rrrBean.getRegistrationNumber() == null
                    || rrrBean.getRegistrationNumber().trim().isEmpty()) {
                rrrBean.setRegistrationNumber(generateSubleaseNumber());
            }
        }
        if (rrrAction == RrrBean.RRR_ACTION.CANCEL) {
            btnSave.setText(MessageUtility.getLocalizedMessage(
                    ClientMessage.GENERAL_LABELS_TERMINATE_AND_CLOSE).getMessage());
        }

        if (rrrAction != RrrBean.RRR_ACTION.EDIT && rrrAction != RrrBean.RRR_ACTION.VIEW
                && appService != null) {
            // Set default noation text from the selected application service
            txtNotationText.setText(appService.getRequestType().getNotationTemplate());
        }

        boolean enabled = rrrAction != RrrBean.RRR_ACTION.VIEW;

        btnSave.setEnabled(enabled);
        txtNotationText.setEnabled(enabled);
        txtRegDatetime.setEnabled(enabled);
        btnRegistrationDate.setEnabled(enabled);
        txtRegistrationNumber.setEnabled(enabled);
        txtNotationText.setEnabled(enabled);
        txtExpirationDate.setEnabled(enabled);
        btnExpirationDate.setEnabled(enabled);
        txtRent.setEnabled(enabled);
        txtDueDate.setEnabled(enabled);
        btnDueDate.setEnabled(enabled);
        btnAddSubplot.setEnabled(enabled);
        btnRemoveSubplot.setEnabled(enabled);
        partyList.setReadOnly(!enabled);
    }

    private void prepareRrrBean(RrrBean rrrBean, RrrBean.RRR_ACTION rrrAction) {
        if (rrrBean == null) {
            this.rrrBean = new RrrBean();
            this.rrrBean.setStatusCode(StatusConstants.PENDING);
        } else {
            this.rrrBean = rrrBean.makeCopyByAction(rrrAction);
        }

        if (!this.rrrBean.isPrimary()) {
            this.rrrBean.setPrimary(true);
        }
    }

    /**
     * Determines the expiry date for the primary lease. Used for validation of
     * the sublease expiry date.
     *
     * @return
     */
    private Date getLeaseExpiryDate() {
        Date result = null;
        // Check for any pending leases first as the expiry date on the
        // pending lease will be the one that gets applied at registration
        for (RrrBean bean : baUnit.getRrrFilteredList()) {
            if (RrrBean.CODE_LEASE.equals(bean.getTypeCode())
                    && StatusConstants.PENDING.equals(bean.getStatusCode())) {
                result = bean.getExpirationDate();
                break;
            }
        }
        // Check for the current lease
        if (result == null) {
            for (RrrBean bean : baUnit.getRrrFilteredList()) {
                if (RrrBean.CODE_LEASE.equals(bean.getTypeCode())
                        && StatusConstants.CURRENT.equals(bean.getStatusCode())) {
                    result = bean.getExpirationDate();
                    break;
                }
            }
        }
        return result;
    }

    private boolean saveRrr() {

        if (rrrBean.getLeaseExpiryDate() == null) {
            // Set data for the sublease expiration date validation
            rrrBean.setLeaseExpiryDate(getLeaseExpiryDate());
        }

        if (rrrBean.getCadastreObject() != null && baUnit.getCadastreObject() != null) {
            // Check the subplot area is within the area of the lease. Must be done
            // here as the validation bean must not have a dependency to GeometryUtility.
            rrrBean.setSubplotValid(
                    GeometryUtility.getGeometryFromWkb(baUnit.getCadastreObject().getGeomPolygon()).buffer(1).contains(
                    GeometryUtility.getGeometryFromWkb(rrrBean.getCadastreObject().getGeomPolygon())));
        } else {
            rrrBean.setSubplotValid(true);
        }

        if (rrrBean.validate(true, Default.class, LeaseValidationGroup.class).size() < 1) {
            firePropertyChange(UPDATED_RRR,
                    null, rrrBean);
            close();

            return true;
        }
        return false;
    }

    private void saveRrrState() {
        MainForm.saveBeanState(rrrBean);
    }

    @Override
    protected boolean panelClosing() {
        if (btnSave.isEnabled() && MainForm.checkSaveBeforeClose(rrrBean)) {
            return saveRrr();
        }
        return true;
    }

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    /**
     * Determines the sublease number based on the number of subleases that are
     * already on the lease.
     *
     * @return
     */
    private String generateSubleaseNumber() {
        String result = null;
        if (baUnit.getCadastreObject().toString() != null) {
            int count = 0;

            for (RrrBean bean : baUnit.getRrrFilteredList()) {
                if (RrrBean.CODE_SUBLEASE.equals(bean.getTypeCode())) {
                    count++;
                }
            }
            count++;
            result = String.format("%s-%s", baUnit.getCadastreObject().toString(),
                    String.format("%03d", count));
        }

        return result;
    }

    /**
     * Opens the CO Search form so the user can search for the subplot parcel
     */
    private void addSubplot() {
        CadastreObjectSearchForm form = new CadastreObjectSearchForm();
        form.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(CadastreObjectsSearchPanel.SELECTED_CADASTRE_OBJECT)) {
                    rrrBean.setCadastreObject(CadastreObjectBean.getCadastreObject(
                            ((CadastreObjectSummaryBean) evt.getNewValue()).getId()));

                    if (rrrBean.getCadastreObject() != null) {
                        rrrBean.setCadastreObjectId(rrrBean.getCadastreObject().getId());
                    } else {
                        rrrBean.setCadastreObjectId(null);
                    }
                    zoomedToPlot = false;
                }
            }
        });
        getMainContentPanel().addPanel(form, MainContentPanel.CARD_PARCEL_SEARCH, true);
    }

    /**
     * Clears the selected subplot for the sublease
     */
    private void removeSubplot() {
        rrrBean.setCadastreObject(null);
        rrrBean.setCadastreObjectId(null);
        zoomedToPlot = false;
    }

    /**
     * Focuses the map on the subplot if one exists otherwise zooms the map to
     * the area of the lease.
     */
    private void zoomToPlot() {
        if (!zoomedToPlot && rrrBean.getCadastreObject() != null) {
            // Highlight the subplot on the map
            this.mapControl.setCadastreObject(rrrBean.getCadastreObject());
        } else if (!zoomedToPlot && baUnit.getCadastreObject() != null) {
            // Zoom to the area of the lease, but do not highlight anything
            // as there is no subplot defined (i.e. remove the lease area 
            // after the zoom to action. 
            this.mapControl.setCadastreObject(baUnit.getCadastreObject());
            this.mapControl.setCadastreObject(null);
            this.mapControl.refresh(true);
        }
        zoomedToPlot = true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        rrrBean = CreateRrrBean();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnSave = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        mainTab = new javax.swing.JTabbedPane();
        subleaseTabPanel = new javax.swing.JPanel();
        rrrDetailsPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtRegDatetime = new javax.swing.JFormattedTextField();
        btnRegistrationDate = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtRegistrationNumber = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtExpirationDate = new javax.swing.JFormattedTextField();
        btnExpirationDate = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtRent = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtDueDate = new javax.swing.JFormattedTextField();
        btnDueDate = new javax.swing.JButton();
        notationPanel = new javax.swing.JPanel();
        txtNotationText = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        rightHoldersPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        groupPanel1 = new org.sola.clients.swing.ui.GroupPanel();
        partyList = createPartyListPanel();
        jPanel3 = new javax.swing.JPanel();
        groupPanel2 = new org.sola.clients.swing.ui.GroupPanel();
        documentsManagementPanel = createDocumentsPanel();
        subplotTabPanel = new javax.swing.JPanel();
        parcelPanel1 = new org.sola.clients.swing.ui.cadastre.ParcelPanel();
        jToolBar3 = new javax.swing.JToolBar();
        btnAddSubplot = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnRemoveSubplot = new org.sola.clients.swing.common.buttons.BtnRemove();
        mapTabPanel = new javax.swing.JPanel();

        setHeaderPanel(headerPanel);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/administrative/Bundle"); // NOI18N
        headerPanel.setTitleText(bundle.getString("SimpleOwhershipPanel.headerPanel.titleText")); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/save.png"))); // NOI18N
        btnSave.setText(bundle.getString("SimpleOwhershipPanel.btnSave.text")); // NOI18N
        btnSave.setFocusable(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSave);
        jToolBar1.add(filler1);
        jToolBar1.add(jSeparator2);

        jLabel1.setText(bundle.getString("SimpleOwhershipPanel.jLabel1.text")); // NOI18N
        jToolBar1.add(jLabel1);

        lblStatus.setFont(LafManager.getInstance().getLabFontBold());

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${status.displayValue}"), lblStatus, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jToolBar1.add(lblStatus);

        rrrDetailsPanel.setLayout(new java.awt.GridLayout(1, 5, 15, 0));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel2.setText(bundle.getString("SimpleOwhershipPanel.jLabel2.text")); // NOI18N

        txtRegDatetime.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${registrationDate}"), txtRegDatetime, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnRegistrationDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnRegistrationDate.setText(bundle.getString("SubleasePanel.btnRegistrationDate.text")); // NOI18N
        btnRegistrationDate.setBorder(null);
        btnRegistrationDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrationDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                    .addComponent(txtRegDatetime))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRegistrationDate))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtRegDatetime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrationDate)))
        );

        rrrDetailsPanel.add(jPanel5);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel8.setText(bundle.getString("SubleasePanel.jLabel8.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${registrationNumber}"), txtRegistrationNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtRegistrationNumber)
            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRegistrationNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 14, Short.MAX_VALUE))
        );

        rrrDetailsPanel.add(jPanel13);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel5.setText(bundle.getString("SubleasePanel.jLabel5.text")); // NOI18N

        txtExpirationDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtExpirationDate.setText(bundle.getString("SubleasePanel.txtExpirationDate.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${expirationDate}"), txtExpirationDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnExpirationDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnExpirationDate.setText(bundle.getString("SubleasePanel.btnExpirationDate.text")); // NOI18N
        btnExpirationDate.setBorder(null);
        btnExpirationDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpirationDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                    .addComponent(txtExpirationDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnExpirationDate))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnExpirationDate)
                    .addComponent(txtExpirationDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(338, 338, 338))
        );

        rrrDetailsPanel.add(jPanel7);

        jLabel6.setText(bundle.getString("SubleasePanel.jLabel6.text")); // NOI18N

        txtRent.setFormatterFactory(FormattersFactory.getInstance().getDecimalFormatterFactory());
        txtRent.setText(bundle.getString("SubleasePanel.txtRent.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${amount}"), txtRent, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtRent)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        rrrDetailsPanel.add(jPanel8);

        jLabel4.setText(bundle.getString("SubleasePanel.jLabel4.text")); // NOI18N

        txtDueDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtDueDate.setText(bundle.getString("SubleasePanel.txtDueDate.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${dueDate}"), txtDueDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnDueDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnDueDate.setText(bundle.getString("SubleasePanel.btnDueDate.text")); // NOI18N
        btnDueDate.setBorder(null);
        btnDueDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDueDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                    .addComponent(txtDueDate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDueDate))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDueDate)
                    .addComponent(txtDueDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        rrrDetailsPanel.add(jPanel6);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrBean, org.jdesktop.beansbinding.ELProperty.create("${notation.notationText}"), txtNotationText, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel3.setText(bundle.getString("SimpleOwhershipPanel.jLabel3.text")); // NOI18N

        javax.swing.GroupLayout notationPanelLayout = new javax.swing.GroupLayout(notationPanel);
        notationPanel.setLayout(notationPanelLayout);
        notationPanelLayout.setHorizontalGroup(
            notationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtNotationText)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        notationPanelLayout.setVerticalGroup(
            notationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notationPanelLayout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNotationText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        rightHoldersPanel.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

        groupPanel1.setTitleText(bundle.getString("SimpleOwhershipPanel.groupPanel1.titleText")); // NOI18N

        partyList.setShowSearchButton(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(groupPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
            .addComponent(partyList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(partyList, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
        );

        rightHoldersPanel.add(jPanel2);

        groupPanel2.setTitleText(bundle.getString("SimpleOwhershipPanel.groupPanel2.titleText")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(groupPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
            .addComponent(documentsManagementPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(groupPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(documentsManagementPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
        );

        rightHoldersPanel.add(jPanel3);

        javax.swing.GroupLayout subleaseTabPanelLayout = new javax.swing.GroupLayout(subleaseTabPanel);
        subleaseTabPanel.setLayout(subleaseTabPanelLayout);
        subleaseTabPanelLayout.setHorizontalGroup(
            subleaseTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subleaseTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subleaseTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(notationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rrrDetailsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(subleaseTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(subleaseTabPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(rightHoldersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        subleaseTabPanelLayout.setVerticalGroup(
            subleaseTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subleaseTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rrrDetailsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(notationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(267, Short.MAX_VALUE))
            .addGroup(subleaseTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subleaseTabPanelLayout.createSequentialGroup()
                    .addGap(134, 134, 134)
                    .addComponent(rightHoldersPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        mainTab.addTab(bundle.getString("SubleasePanel.subleaseTabPanel.TabConstraints.tabTitle"), subleaseTabPanel); // NOI18N

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        btnAddSubplot.setLabel(bundle.getString("SubleasePanel.btnAddSubplot.label")); // NOI18N
        btnAddSubplot.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddSubplot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSubplotActionPerformed(evt);
            }
        });
        jToolBar3.add(btnAddSubplot);

        btnRemoveSubplot.setLabel(bundle.getString("SubleasePanel.btnRemoveSubplot.label")); // NOI18N
        btnRemoveSubplot.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveSubplot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveSubplotActionPerformed(evt);
            }
        });
        jToolBar3.add(btnRemoveSubplot);

        javax.swing.GroupLayout subplotTabPanelLayout = new javax.swing.GroupLayout(subplotTabPanel);
        subplotTabPanel.setLayout(subplotTabPanelLayout);
        subplotTabPanelLayout.setHorizontalGroup(
            subplotTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subplotTabPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subplotTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
                    .addComponent(parcelPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE))
                .addContainerGap())
        );
        subplotTabPanelLayout.setVerticalGroup(
            subplotTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subplotTabPanelLayout.createSequentialGroup()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(parcelPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainTab.addTab(bundle.getString("SubleasePanel.subplotTabPanel.TabConstraints.tabTitle"), subplotTabPanel); // NOI18N

        mapTabPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                mapTabPanelComponentShown(evt);
            }
        });

        javax.swing.GroupLayout mapTabPanelLayout = new javax.swing.GroupLayout(mapTabPanel);
        mapTabPanel.setLayout(mapTabPanelLayout);
        mapTabPanelLayout.setHorizontalGroup(
            mapTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 742, Short.MAX_VALUE)
        );
        mapTabPanelLayout.setVerticalGroup(
            mapTabPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 394, Short.MAX_VALUE)
        );

        mainTab.addTab(bundle.getString("SubleasePanel.mapTabPanel.TabConstraints.tabTitle_1"), mapTabPanel); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainTab)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainTab)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        saveRrr();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnRegistrationDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrationDateActionPerformed
        showCalendar(txtRegDatetime);
    }//GEN-LAST:event_btnRegistrationDateActionPerformed

    private void btnExpirationDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpirationDateActionPerformed
        showCalendar(txtExpirationDate);
    }//GEN-LAST:event_btnExpirationDateActionPerformed

    private void btnDueDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDueDateActionPerformed
        showCalendar(txtDueDate);
    }//GEN-LAST:event_btnDueDateActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        if (this.mapControl == null) {
            this.mapControl = new ControlsBundleForBaUnit();
            if (applicationBean != null) {
                this.mapControl.setApplicationId(this.applicationBean.getId());
            }
            this.mapTabPanel.setLayout(new BorderLayout());
            this.mapTabPanel.add(this.mapControl, BorderLayout.CENTER);
        }
    }//GEN-LAST:event_formComponentShown

    private void mapTabPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_mapTabPanelComponentShown
        zoomToPlot();
    }//GEN-LAST:event_mapTabPanelComponentShown

    private void btnAddSubplotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSubplotActionPerformed
        addSubplot();
    }//GEN-LAST:event_btnAddSubplotActionPerformed

    private void btnRemoveSubplotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveSubplotActionPerformed
        removeSubplot();
    }//GEN-LAST:event_btnRemoveSubplotActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.common.buttons.BtnAdd btnAddSubplot;
    private javax.swing.JButton btnDueDate;
    private javax.swing.JButton btnExpirationDate;
    private javax.swing.JButton btnRegistrationDate;
    private org.sola.clients.swing.common.buttons.BtnRemove btnRemoveSubplot;
    private javax.swing.JButton btnSave;
    private org.sola.clients.swing.desktop.source.DocumentsManagementExtPanel documentsManagementPanel;
    private javax.swing.Box.Filler filler1;
    private org.sola.clients.swing.ui.GroupPanel groupPanel1;
    private org.sola.clients.swing.ui.GroupPanel groupPanel2;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTabbedPane mainTab;
    private javax.swing.JPanel mapTabPanel;
    private javax.swing.JPanel notationPanel;
    private org.sola.clients.swing.ui.cadastre.ParcelPanel parcelPanel1;
    private org.sola.clients.swing.desktop.party.PartyListExtPanel partyList;
    private javax.swing.JPanel rightHoldersPanel;
    private org.sola.clients.beans.administrative.RrrBean rrrBean;
    private javax.swing.JPanel rrrDetailsPanel;
    private javax.swing.JPanel subleaseTabPanel;
    private javax.swing.JPanel subplotTabPanel;
    private javax.swing.JFormattedTextField txtDueDate;
    private javax.swing.JFormattedTextField txtExpirationDate;
    private javax.swing.JTextField txtNotationText;
    private javax.swing.JFormattedTextField txtRegDatetime;
    private javax.swing.JTextField txtRegistrationNumber;
    private javax.swing.JFormattedTextField txtRent;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
