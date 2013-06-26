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
package org.sola.clients.swing.desktop.administrative;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import net.sf.jasperreports.engine.JasperPrint;
import org.geotools.map.extended.layer.ExtendedLayer;
import org.geotools.swing.extended.exception.InitializeMapException;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Bindings;
import org.sola.clients.beans.administrative.LeaseBean;
import org.sola.clients.beans.administrative.LeaseReportBean;
import org.sola.clients.beans.administrative.LeaseSpecialConditionBean;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.cadastre.CadastreObjectBean;
import org.sola.clients.beans.cadastre.CadastreObjectSummaryBean;
import org.sola.clients.beans.referencedata.StatusConstants;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.desktop.cadastre.CadastreObjectSearchForm;
import org.sola.clients.swing.desktop.party.PartyListExtPanel;
import org.sola.clients.swing.gis.ui.control.MapFeatureImageGenerator;
import org.sola.clients.swing.gis.ui.controlsbundle.ControlsBundleForBaUnit;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.cadastre.CadastreObjectsDialog;
import org.sola.clients.swing.ui.cadastre.CadastreObjectsSearchPanel;
import org.sola.clients.swing.ui.renderers.FormattersFactory;
import org.sola.clients.swing.ui.reports.FreeTextDialog;
import org.sola.clients.swing.ui.reports.ReportViewerForm;
import org.sola.common.RolesConstants;
import org.sola.common.StringUtility;
import org.sola.common.WindowUtility;
import org.sola.common.logging.LogUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Allows to manage leases at the preparation stage.
 */
public class LeasePreparationForm extends ContentPanel {

    private LeaseBean lease;
    private boolean readOnly = false;
    private ApplicationServiceBean serviceBean;
    private ApplicationBean application;
    private ControlsBundleForBaUnit mapControl = null;

    private PartyListExtPanel createPartyListPanel() {
        return new PartyListExtPanel(lease.getLessees());
    }

    /**
     * Form constructor.
     */
    public LeasePreparationForm(LeaseBean lease, ApplicationBean application, ApplicationServiceBean serviceBean, boolean readOnly) {
        this.application = application;
        this.readOnly = readOnly || !SecurityBean.isInRole(RolesConstants.ADMINISTRATIVE_PREPARE_LEASE);
        this.serviceBean = serviceBean;

        if (lease == null) {
            this.lease = new LeaseBean();
        } else {
            this.lease = lease;
        }
        initComponents();
        postInit();
        saveLeaseState();
    }

    private void postInit() {
        org.jdesktop.beansbinding.Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
                lease, org.jdesktop.beansbinding.ELProperty.create("${cadastreObject}"),
                parcelPanel, org.jdesktop.beansbinding.BeanProperty.create("cadastreObjectBean"), "parcelDetailsBinding");
        bindingGroup.addBinding(binding);
        bindingGroup.bind();

        lease.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(LeaseBean.SELECTED_CONDITION_PROPERTY)) {
                    customizeConditionButtons();
                }
            }
        });
        customizeForm();
    }

    private void customizeForm() {
        boolean enabled = !readOnly
                && SecurityBean.isInRole(RolesConstants.ADMINISTRATIVE_PREPARE_LEASE)
                && (StringUtility.empty(lease.getStatusCode()).equals("")
                || lease.getStatusCode().equals(StatusConstants.PENDING));

        btnSave.setEnabled(enabled);
        btnPrintLease.setEnabled(enabled);
        btnPrintLeaseOffer.setEnabled(enabled);
        btnPrintRejectionLetter.setEnabled(enabled);
        btnSelectFromApplication.setEnabled(enabled);
        btnSearchParcel.setEnabled(enabled);
        parcelPanel.setReadOnly(!enabled);

        txtExpirationDate.setEnabled(enabled);
        txtGroundRent.setEnabled(enabled);
        btnCalculateGroundRent.setEnabled(enabled);
        txtLeaseNumber.setEnabled(enabled);
        txtStampDuty.setEnabled(enabled);
        txtStartDate.setEnabled(enabled);
        txtMaritalStatus.setEnabled(enabled);
        txtLesseeAddress.setEnabled(enabled);
        partyList.setReadOnly(!enabled);
        customizeConditionButtons();
    }

    private void customizeConditionButtons() {
        boolean editable = !readOnly
                && SecurityBean.isInRole(RolesConstants.ADMINISTRATIVE_PREPARE_LEASE)
                && (StringUtility.empty(lease.getStatusCode()).equals("")
                || lease.getStatusCode().equals(StatusConstants.PENDING));

        boolean enabled = editable && lease.getSelectedCondition() != null;

        btnAddCodition.setEnabled(editable);
        btnEditCondition.setEnabled(enabled);
        btnRemoveCondition.setEnabled(enabled);

        menuAddCondition.setEnabled(btnAddCodition.isEnabled());
        menuEditCondition.setEnabled(btnEditCondition.isEnabled());
        menuRemoveCondition.setEnabled(btnRemoveCondition.isEnabled());
    }

    private void setupLeaseBean(LeaseBean lease) {
        if (lease == null) {
            this.lease = new LeaseBean();
        } else {
            this.lease = lease;
        }
        firePropertyChange("lease", null, this.lease);
        customizeForm();
    }

    public LeaseBean getLease() {
        return lease;
    }

    public void setLease(LeaseBean lease) {
        setupLeaseBean(lease);
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        customizeForm();
    }

    private void openParcelSearch() {
        CadastreObjectSearchForm form = new CadastreObjectSearchForm();
        form.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(CadastreObjectsSearchPanel.SELECTED_CADASTRE_OBJECT)) {
                    lease.setCadastreObject(CadastreObjectBean.getCadastreObject(
                            ((CadastreObjectSummaryBean) evt.getNewValue()).getId()));
                    setCadastreObjectOnTheMap();
                }
            }
        });
        getMainContentPanel().addPanel(form, MainContentPanel.CARD_PARCEL_SEARCH, true);
    }

    private void setCadastreObjectOnTheMap() {
        mapControl.setCadastreObject(lease.getCadastreObject());
    }

    private void calculateGroundRent() {
        if (lease.getCadastreObject() == null) {
            MessageUtility.displayMessage(ClientMessage.LEASE_SELECT_PLOT);
            return;
        }

        SolaTask<Void, Void> t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));
                lease.calculateGroundRent();
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void addCondition() {
        LeaseSpecialConditionDialog form = new LeaseSpecialConditionDialog(null, null, true);
        form.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(LeaseSpecialConditionDialog.LEASE_CONDITION_SAVED)) {
                    lease.getLeaseSpecialConditionList().addAsNew((LeaseSpecialConditionBean) evt.getNewValue());
                }
            }
        });
        form.setVisible(true);
    }

    private void editCondition() {
        if (lease.getSelectedCondition() == null) {
            return;
        }

        LeaseSpecialConditionDialog form = new LeaseSpecialConditionDialog(
                (LeaseSpecialConditionBean) lease.getSelectedCondition().copy(), null, true);
        form.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(LeaseSpecialConditionDialog.LEASE_CONDITION_SAVED)) {
                    lease.getSelectedCondition().copyFromObject((LeaseSpecialConditionBean) evt.getNewValue());
                }
            }
        });
        form.setVisible(true);
    }

    private void removeCondition() {
        if (MessageUtility.displayMessage(ClientMessage.CONFIRM_REMOVE_RECORD) == MessageUtility.BUTTON_ONE) {
            lease.removeSelectedCondition();
        }
    }

    private LeaseReportBean prepareReportBean() {
        return new LeaseReportBean(lease, application, serviceBean);
    }

    private void printLease() {
        // Call save before print
        if (lease.validate(true).size() < 1) {
            SolaTask<Void, Void> t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));
                    lease.save(serviceBean.getId());
                    return null;
                }

                @Override
                public void taskDone() {
                    saveLeaseState();
                    final LeaseReportBean reportBean = prepareReportBean();
                    if (reportBean != null) {
                        showReport(ReportManager.getLeaseReport(reportBean, createMapImage()));
                    }
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }
    
    private void printOfferLetter() {
        final LeaseReportBean reportBean = prepareReportBean();
        if (reportBean != null) {
            showReport(ReportManager.getLeaseOfferReport(reportBean));
        }
    }

    private void printRejectionLetter() {
        final LeaseReportBean reportBean = prepareReportBean();
        if (reportBean != null) {
            // Show free text form
            FreeTextDialog form = new FreeTextDialog(
                    MessageUtility.getLocalizedMessageText(ClientMessage.BAUNIT_LEASE_REJECTION_REASON_TITLE),
                    null, MainForm.getInstance(), true);
            WindowUtility.centerForm(form);

            form.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(FreeTextDialog.TEXT_TO_SAVE)) {
                        reportBean.setFreeText((String) evt.getNewValue());
                    }
                }
            });
            form.setVisible(true);
            showReport(ReportManager.getLeaseRejectionReport(reportBean));
        }
    }
    
    /**
     * Opens {@link ReportViewerForm} to display report.
     */
    private void showReport(JasperPrint report) {
        ReportViewerForm form = new ReportViewerForm(report);
        form.setLocationRelativeTo(this);
        form.setVisible(true);
    }

    /**
     * Uses the mapControl from the Property form to render the lease parcel on
     * top of the map to create as an image for the lease report. The context
     * information shown in the image (i.e. abutting parcels, roads, rivers,
     * zoom scale, etc) can be changed by the user by modifying the map control
     * on the Property form.
     *
     * @return The file name for the generated image or NULL if the map control
     * is not set.
     */
    private String createMapImage() {
        String result = null;
        if (lease.getCadastreObject() != null && mapControl != null && mapControl.getMap() != null) {
            try {
                // Remove any temporary objects from the map and turn off any layers
                // that should not be displayed on the Location diagram such as the parcel nodes layer
                // and the grid layer
                mapControl.setCadastreObject(null);
                ExtendedLayer nodesLayer = mapControl.getMap().getSolaLayers().get("parcel-nodes");
                if (nodesLayer != null) {
                    nodesLayer.setVisible(false);
                }
                ExtendedLayer gridLayer = mapControl.getMap().getSolaLayers().get("grid");
                if (gridLayer != null) {
                    gridLayer.setVisible(false);
                }

                MapFeatureImageGenerator generator = new MapFeatureImageGenerator(mapControl.getMap());

                String parcelLabel = lease.getCadastreObject().toString();

                result = generator.getFeatureImage(
                        lease.getCadastreObject().getGeomPolygon(),
                        parcelLabel, null,
                        MapFeatureImageGenerator.IMAGE_FORMAT_PNG);

            } catch (InitializeMapException mapEx) {
                LogUtility.log("Unable to initialize MapFeaureImageGenerator", mapEx);
            }
        }
        return result;
    }

    private void openApplicationParcelsForm() {
        CadastreObjectsDialog form = new CadastreObjectsDialog(
                application.getCadastreObjectFilteredList(), MainForm.getInstance(), true);
        WindowUtility.centerForm(form);
        form.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(CadastreObjectsDialog.SELECT_CADASTRE_OBJECT)) {
                    lease.setCadastreObject(CadastreObjectBean.getCadastreObject(
                            ((CadastreObjectSummaryBean) evt.getNewValue()).getId()));
                    setCadastreObjectOnTheMap();
                }
            }
        });
        form.setVisible(true);
    }

    private void saveLease(final boolean showMessage, final boolean closeOnSave) {
        if (lease.validate(true).size() < 1) {
            SolaTask<Void, Void> t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));
                    lease.save(serviceBean.getId());
                    if (closeOnSave) {
                        close();
                    }
                    return null;
                }

                @Override
                public void taskDone() {
                    if (showMessage) {
                        MessageUtility.displayMessage(ClientMessage.LEASE_SAVED_SUCCESSFULLY);
                    }
                    saveLeaseState();
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }

    @Override
    protected boolean panelClosing() {
        if (btnSave.isEnabled() && MainForm.checkSaveBeforeClose(lease)) {
            saveLease(true, true);
            return false;
        }
        return true;
    }

    private void saveLeaseState() {
        MainForm.saveBeanState(lease);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        popupConditions = new javax.swing.JPopupMenu();
        menuAddCondition = new org.sola.clients.swing.common.menuitems.MenuAdd();
        menuEditCondition = new org.sola.clients.swing.common.menuitems.MenuEdit();
        menuRemoveCondition = new org.sola.clients.swing.common.menuitems.MenuRemove();
        headerPanel1 = new org.sola.clients.swing.ui.HeaderPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnSave = new org.sola.clients.swing.common.buttons.BtnSave();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnPrintLease = new org.sola.clients.swing.common.buttons.BtnPrint();
        btnPrintLeaseOffer = new org.sola.clients.swing.common.buttons.BtnPrint();
        btnPrintRejectionLetter = new org.sola.clients.swing.common.buttons.BtnPrint();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        parcelPanel = new org.sola.clients.swing.ui.cadastre.ParcelPanel();
        jToolBar2 = new javax.swing.JToolBar();
        btnSelectFromApplication = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnSearchParcel = new org.sola.clients.swing.common.buttons.BtnSearch();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtLeaseNumber = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtStartDate = new javax.swing.JFormattedTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        txtExpirationDate = new javax.swing.JFormattedTextField();
        jPanel10 = new javax.swing.JPanel();
        txtGroundRent = new javax.swing.JFormattedTextField();
        btnCalculateGroundRent = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        txtStampDuty = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        partyList = createPartyListPanel();
        groupPanel1 = new org.sola.clients.swing.ui.GroupPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtMaritalStatus = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLesseeAddress = new javax.swing.JTextArea();
        jPanel13 = new javax.swing.JPanel();
        jToolBar3 = new javax.swing.JToolBar();
        btnAddCodition = new org.sola.clients.swing.common.buttons.BtnAdd();
        btnEditCondition = new org.sola.clients.swing.common.buttons.BtnEdit();
        btnRemoveCondition = new org.sola.clients.swing.common.buttons.BtnRemove();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableWithDefaultStyles1 = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        pnlMap = new javax.swing.JPanel();

        menuAddCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddConditionActionPerformed(evt);
            }
        });
        popupConditions.add(menuAddCondition);

        menuEditCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditConditionActionPerformed(evt);
            }
        });
        popupConditions.add(menuEditCondition);

        menuRemoveCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveConditionActionPerformed(evt);
            }
        });
        popupConditions.add(menuRemoveCondition);

        setHeaderPanel(headerPanel1);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/administrative/Bundle"); // NOI18N
        headerPanel1.setTitleText(bundle.getString("LeasePreparationForm.headerPanel1.titleText")); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSave);
        jToolBar1.add(jSeparator1);

        btnPrintLease.setText(bundle.getString("LeasePreparationForm.btnPrintLease.text")); // NOI18N
        btnPrintLease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintLeaseActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrintLease);

        btnPrintLeaseOffer.setText(bundle.getString("LeasePreparationForm.btnPrintLeaseOffer.text")); // NOI18N
        btnPrintLeaseOffer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintLeaseOfferActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrintLeaseOffer);

        btnPrintRejectionLetter.setText(bundle.getString("LeasePreparationForm.btnPrintRejectionLetter.text")); // NOI18N
        btnPrintRejectionLetter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintRejectionLetterActionPerformed(evt);
            }
        });
        jToolBar1.add(btnPrintRejectionLetter);
        jToolBar1.add(jSeparator2);

        jLabel1.setText(bundle.getString("LeasePreparationForm.jLabel1.text")); // NOI18N
        jToolBar1.add(jLabel1);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${lease.status.displayValue}"), lblStatus, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jToolBar1.add(lblStatus);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnSelectFromApplication.setText(bundle.getString("LeasePreparationForm.btnSelectFromApplication.text")); // NOI18N
        btnSelectFromApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectFromApplicationActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSelectFromApplication);

        btnSearchParcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchParcelActionPerformed(evt);
            }
        });
        jToolBar2.add(btnSearchParcel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(parcelPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(parcelPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 366, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab(bundle.getString("LeasePreparationForm.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel8.setLayout(new java.awt.GridLayout(2, 3, 15, 10));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel3.setText(bundle.getString("LeasePreparationForm.jLabel3.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${lease.leaseNumber}"), txtLeaseNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 123, Short.MAX_VALUE))
            .addComponent(txtLeaseNumber)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLeaseNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel8.add(jPanel4);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel5.setText(bundle.getString("LeasePreparationForm.jLabel5.text")); // NOI18N

        txtStartDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtStartDate.setText(bundle.getString("LeasePreparationForm.txtStartDate.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${lease.startDate}"), txtStartDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(0, 141, Short.MAX_VALUE))
            .addComponent(txtStartDate)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel8.add(jPanel6);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel8.setText(bundle.getString("LeasePreparationForm.jLabel8.text")); // NOI18N

        txtExpirationDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtExpirationDate.setText(bundle.getString("LeasePreparationForm.txtExpirationDate.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${lease.expirationDate}"), txtExpirationDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addGap(0, 117, Short.MAX_VALUE))
            .addComponent(txtExpirationDate)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtExpirationDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel8.add(jPanel9);

        txtGroundRent.setFormatterFactory(FormattersFactory.getInstance().getDecimalFormatterFactory());
        txtGroundRent.setText(bundle.getString("LeasePreparationForm.txtGroundRent.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${lease.groundRent}"), txtGroundRent, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        btnCalculateGroundRent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calculator.png"))); // NOI18N
        btnCalculateGroundRent.setText(bundle.getString("LeasePreparationForm.btnCalculateGroundRent.text")); // NOI18N
        btnCalculateGroundRent.setToolTipText(bundle.getString("LeasePreparationForm.btnCalculateGroundRent.toolTipText")); // NOI18N
        btnCalculateGroundRent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateGroundRentActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel6.setText(bundle.getString("LeasePreparationForm.jLabel6.text")); // NOI18N

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(0, 130, Short.MAX_VALUE))
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(txtGroundRent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCalculateGroundRent, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnCalculateGroundRent, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtGroundRent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel8.add(jPanel10);

        txtStampDuty.setFormatterFactory(FormattersFactory.getInstance().getDecimalFormatterFactory());
        txtStampDuty.setText(bundle.getString("LeasePreparationForm.txtStampDuty.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${lease.stampDuty}"), txtStampDuty, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        jLabel9.setText(bundle.getString("LeasePreparationForm.jLabel9.text")); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtStampDuty, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtStampDuty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel8.add(jPanel7);

        partyList.setShowSearchButton(false);

        groupPanel1.setTitleText(bundle.getString("LeasePreparationForm.groupPanel1.titleText")); // NOI18N

        jPanel11.setLayout(new java.awt.GridLayout(1, 2, 15, 0));

        jLabel2.setText(bundle.getString("LeasePreparationForm.jLabel2.text")); // NOI18N

        txtMaritalStatus.setColumns(20);
        txtMaritalStatus.setRows(4);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${lease.maritalStatus}"), txtMaritalStatus, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jScrollPane2.setViewportView(txtMaritalStatus);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel3);

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/red_asterisk.gif"))); // NOI18N
        jLabel4.setText(bundle.getString("LeasePreparationForm.jLabel4.text")); // NOI18N

        txtLesseeAddress.setColumns(20);
        txtLesseeAddress.setRows(4);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${lease.lesseeAddress}"), txtLesseeAddress, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(txtLesseeAddress);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
        );

        jPanel11.add(jPanel5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(groupPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(partyList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(partyList, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab(bundle.getString("LeasePreparationForm.jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        btnAddCodition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCoditionActionPerformed(evt);
            }
        });
        jToolBar3.add(btnAddCodition);
        jToolBar3.add(btnEditCondition);

        btnRemoveCondition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveConditionActionPerformed(evt);
            }
        });
        jToolBar3.add(btnRemoveCondition);

        jTableWithDefaultStyles1.setComponentPopupMenu(popupConditions);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${lease.filteredLeaseSpecialConditionList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, jTableWithDefaultStyles1);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${conditionText}"));
        columnBinding.setColumnName("Condition Text");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${lease.selectedCondition}"), jTableWithDefaultStyles1, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane3.setViewportView(jTableWithDefaultStyles1);
        jTableWithDefaultStyles1.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("LeasePreparationForm.jTableWithDefaultStyles1.columnModel.title0_1")); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab(bundle.getString("LeasePreparationForm.jPanel13.TabConstraints.tabTitle"), jPanel13); // NOI18N

        pnlMap.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                pnlMapComponentShown(evt);
            }
        });

        javax.swing.GroupLayout pnlMapLayout = new javax.swing.GroupLayout(pnlMap);
        pnlMap.setLayout(pnlMapLayout);
        pnlMapLayout.setHorizontalGroup(
            pnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 665, Short.MAX_VALUE)
        );
        pnlMapLayout.setVerticalGroup(
            pnlMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 424, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab(bundle.getString("LeasePreparationForm.pnlMap.TabConstraints.tabTitle"), pnlMap); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchParcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchParcelActionPerformed
        openParcelSearch();
    }//GEN-LAST:event_btnSearchParcelActionPerformed

    private void btnCalculateGroundRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateGroundRentActionPerformed
        calculateGroundRent();
    }//GEN-LAST:event_btnCalculateGroundRentActionPerformed

    private void btnAddCoditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCoditionActionPerformed
        addCondition();
    }//GEN-LAST:event_btnAddCoditionActionPerformed

    private void btnRemoveConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveConditionActionPerformed
        removeCondition();
    }//GEN-LAST:event_btnRemoveConditionActionPerformed

    private void menuAddConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddConditionActionPerformed
        addCondition();
    }//GEN-LAST:event_menuAddConditionActionPerformed

    private void menuEditConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditConditionActionPerformed
        editCondition();
    }//GEN-LAST:event_menuEditConditionActionPerformed

    private void menuRemoveConditionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveConditionActionPerformed
        removeCondition();
    }//GEN-LAST:event_menuRemoveConditionActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        saveLease(true, false);
        customizeForm();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnSelectFromApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectFromApplicationActionPerformed
        openApplicationParcelsForm();
    }//GEN-LAST:event_btnSelectFromApplicationActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        if (mapControl == null) {
            mapControl = new ControlsBundleForBaUnit();
            if (application != null) {
                mapControl.setApplicationId(application.getId());
            }
            pnlMap.setLayout(new BorderLayout());
            pnlMap.add(mapControl, BorderLayout.CENTER);
        }
    }//GEN-LAST:event_formComponentShown

    private void pnlMapComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_pnlMapComponentShown
        setCadastreObjectOnTheMap();
    }//GEN-LAST:event_pnlMapComponentShown

    private void btnPrintLeaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintLeaseActionPerformed
        printLease();
    }//GEN-LAST:event_btnPrintLeaseActionPerformed

    private void btnPrintLeaseOfferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintLeaseOfferActionPerformed
        printOfferLetter();
    }//GEN-LAST:event_btnPrintLeaseOfferActionPerformed

    private void btnPrintRejectionLetterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintRejectionLetterActionPerformed
        printRejectionLetter();
    }//GEN-LAST:event_btnPrintRejectionLetterActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.common.buttons.BtnAdd btnAddCodition;
    private javax.swing.JButton btnCalculateGroundRent;
    private org.sola.clients.swing.common.buttons.BtnEdit btnEditCondition;
    private org.sola.clients.swing.common.buttons.BtnPrint btnPrintLease;
    private org.sola.clients.swing.common.buttons.BtnPrint btnPrintLeaseOffer;
    private org.sola.clients.swing.common.buttons.BtnPrint btnPrintRejectionLetter;
    private org.sola.clients.swing.common.buttons.BtnRemove btnRemoveCondition;
    private org.sola.clients.swing.common.buttons.BtnSave btnSave;
    private org.sola.clients.swing.common.buttons.BtnSearch btnSearchParcel;
    private org.sola.clients.swing.common.buttons.BtnAdd btnSelectFromApplication;
    private org.sola.clients.swing.ui.GroupPanel groupPanel1;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane2;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles jTableWithDefaultStyles1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JLabel lblStatus;
    private org.sola.clients.swing.common.menuitems.MenuAdd menuAddCondition;
    private org.sola.clients.swing.common.menuitems.MenuEdit menuEditCondition;
    private org.sola.clients.swing.common.menuitems.MenuRemove menuRemoveCondition;
    private org.sola.clients.swing.ui.cadastre.ParcelPanel parcelPanel;
    private org.sola.clients.swing.desktop.party.PartyListExtPanel partyList;
    private javax.swing.JPanel pnlMap;
    private javax.swing.JPopupMenu popupConditions;
    private javax.swing.JFormattedTextField txtExpirationDate;
    private javax.swing.JFormattedTextField txtGroundRent;
    private javax.swing.JTextField txtLeaseNumber;
    private javax.swing.JTextArea txtLesseeAddress;
    private javax.swing.JTextArea txtMaritalStatus;
    private javax.swing.JFormattedTextField txtStampDuty;
    private javax.swing.JFormattedTextField txtStartDate;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
