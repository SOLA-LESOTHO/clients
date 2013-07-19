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
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Bindings;
import org.sola.clients.beans.administrative.*;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.cadastre.CadastreObjectBean;
import org.sola.clients.beans.cadastre.CadastreObjectSummaryBean;
import org.sola.clients.beans.referencedata.*;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.swing.common.LafManager;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.desktop.cadastre.CadastreObjectSearchForm;
import org.sola.clients.swing.gis.ui.controlsbundle.ControlsBundleForBaUnit;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.administrative.BaUnitSearchPanel;
import org.sola.clients.swing.ui.cadastre.CadastreObjectsDialog;
import org.sola.clients.swing.ui.cadastre.CadastreObjectsSearchPanel;
import org.sola.clients.swing.ui.renderers.*;
import org.sola.common.RolesConstants;
import org.sola.common.StringUtility;
import org.sola.common.WindowUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * This form is used to manage property object ({
 *
 * @codeBaUnit}). {@link BaUnitBean} is used to bind data on the form.
 */
public class PropertyPanel extends ContentPanel {

    private ApplicationBean applicationBean;
    private ApplicationServiceBean applicationService;
    private String baUnitID;
    private ControlsBundleForBaUnit mapControl = null;
    private boolean readOnly = false;
    java.util.ResourceBundle resourceBundle;
    public BaUnitBean whichBaUnitSelected;

    /**
     * Creates {@link BaUnitBean} used to bind form components.
     */
    private BaUnitBean createBaUnitBean() {
        if (baUnitBean1 == null) {
            baUnitBean1 = new BaUnitBean();
        } else {
            baUnitID = baUnitBean1.getId();
        }
        return baUnitBean1;
    }

    /**
     * Form constructor.
     *
     * @param applicationBean {@link ApplicationBean} instance, used to get list
     * of documents.
     * @param applicationService {@link ApplicationServiceBean} instance, used
     * to determine what actions should be taken on this form.
     * @param BaUnitBean Instance of {@link BaUnitBean}, used to bind data on
     * the form.
     * @param readOnly If true, opens form in read only mode.
     */
    public PropertyPanel(ApplicationBean applicationBean,
            ApplicationServiceBean applicationService,
            BaUnitBean baUnitBean, boolean readOnly) {
        this.baUnitBean1 = baUnitBean;
        this.readOnly = readOnly || !SecurityBean.isInRole(RolesConstants.ADMINISTRATIVE_BA_UNIT_SAVE);
        this.applicationBean = applicationBean;
        this.applicationService = applicationService;
        resourceBundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/administrative/Bundle");
        initComponents();
        postInit();
    }

    /**
     * Makes post initialization tasks.
     */
    private void postInit() {
        org.jdesktop.beansbinding.Binding binding = Bindings.createAutoBinding(AutoBinding.UpdateStrategy.READ_WRITE,
                baUnitBean1, org.jdesktop.beansbinding.ELProperty.create("${cadastreObject}"),
                cadastreObject, org.jdesktop.beansbinding.BeanProperty.create("cadastreObjectBean"), "parcelDetailsBinding");
        bindingGroup.addBinding(binding);
        bindingGroup.bind();

        customizeForm();

        rrrTypes.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(RrrTypeListBean.SELECTED_RRR_TYPE_PROPERTY)) {
                    customizeCreateRightButton();
                }
            }
        });

        baUnitBean1.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(BaUnitBean.SELECTED_RIGHT_PROPERTY)) {
                    customizeRightsButtons((RrrBean) evt.getNewValue());
                } else if (evt.getPropertyName().equals(BaUnitBean.SELECTED_HISTORIC_RIGHT_PROPERTY)) {
                    customizeHistoricRightsViewButton();
                } else if (evt.getPropertyName().equals(BaUnitBean.SELECTED_BA_UNIT_NOTATION_PROPERTY)) {
                    customizeNotationButtons((BaUnitNotationBean) evt.getNewValue());
                } else if (evt.getPropertyName().equals(BaUnitBean.SELECTED_PARENT_BA_UNIT_PROPERTY)) {
                    customizeParentPropertyButtons();
                } else if (evt.getPropertyName().equals(BaUnitBean.SELECTED_CHILD_BA_UNIT_PROPERTY)) {
                    customizeChildPropertyButtons();
                }

            }
        });
        saveBaUnitState();
    }

    /**
     * Runs form customization, to restrict certain actions, bind listeners on
     * the {@link BaUnitBean} and other components.
     */
    private void customizeForm() {
        setFormHeader();
        btnSave.setEnabled(!readOnly);
        cadastreObject.setReadOnly(readOnly || !SecurityBean.isInRole(RolesConstants.ADMINISTRATIVE_MANAGE_LEASE));
        cadastreObject.setLockCadastreFields(true);
        customizeRightsButtons(null);
        customizeNotationButtons(null);
        customizeRightTypesList();
        customizeParcelButtons();
        customizeParentPropertyButtons();
        customizeChildPropertyButtons();
        customizeHistoricRightsViewButton();
    }

    private void setFormHeader() {
        if (baUnitBean1.getNameFirstpart() != null && baUnitBean1.getNameLastpart() != null) {
            headerPanel.setTitleText(String.format(
                    resourceBundle.getString("PropertyPanel.existingProperty.Text"),
                    baUnitBean1.getNameFirstpart(), baUnitBean1.getNameLastpart()));
        } else {
            headerPanel.setTitleText(resourceBundle.getString("PropertyPanel.newProperty.Text"));
        }

        if (applicationBean != null && applicationService != null) {
            headerPanel.setTitleText(String.format("%s, %s",
                    headerPanel.getTitleText(),
                    String.format(resourceBundle.getString("PropertyPanel.applicationInfo.Text"),
                    applicationService.getRequestType().getDisplayValue(), applicationBean.getNr())));
        }
    }

    /**
     * Search for parent property objects.
     */
    private void searchParentProperties() {
        SolaTask t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_PROPERTYSEARCH));
                BaUnitSearchForm form = new BaUnitSearchForm();
                form.getSearchPanel().setShowSelectButton(true);
                form.getSearchPanel().addPropertyChangeListener(new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if(evt.getPropertyName().equals(BaUnitSearchPanel.BAUNIT_SELECTED)){
                            BaUnitSearchResultBean result = (BaUnitSearchResultBean)evt.getNewValue();
                            if(!result.getStatusCode().equals(StatusConstants.HISTORIC)){
                                MessageUtility.displayMessage(ClientMessage.BAUNIT_MUST_HAVE_HISTORIC_STATUS);
                            } else {
                                baUnitBean1.addParentBaUnit(result);
                                getMainContentPanel().closePanel(MainContentPanel.CARD_BAUNIT_SEARCH);
                            }
                        }
                    }
                });
                
                getMainContentPanel().addPanel(form, MainContentPanel.CARD_BAUNIT_SEARCH, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    /**
     * Enables or disables "open", "add" and "remove" buttons for the parent
     * Properties list.
     */
    private void customizeParentPropertyButtons() {
        boolean enabled = !readOnly;
        if (baUnitBean1 == null || (baUnitBean1.getStatusCode() != null
                && !baUnitBean1.getStatusCode().equals(StatusConstants.PENDING))) {
            enabled = false;
        }

        btnOpenParent.setEnabled(baUnitBean1.getSelectedParentBaUnit() != null);
        btnAddParent.setEnabled(enabled);
        btnRemoveParent.setEnabled(enabled && btnOpenParent.isEnabled());

        menuOpenParentBaUnit.setEnabled(btnOpenParent.isEnabled());
        menuAddParentBaUnit.setEnabled(btnAddParent.isEnabled());
        menuRemoveParentBaUnit.setEnabled(btnRemoveParent.isEnabled());
    }

    /**
     * Enables or disables "open" button for the child Properties list.
     */
    private void customizeChildPropertyButtons() {
        btnOpenChild.setEnabled(baUnitBean1.getSelectedChildBaUnit() != null);
        menuOpenChildBaUnit.setEnabled(btnOpenChild.isEnabled());
    }

    private void customizeHistoricRightsViewButton() {
        btnViewHistoricRight.setEnabled(baUnitBean1.getSelectedHistoricRight() != null);
    }

    /**
     * Enables or disables notation buttons, depending on the form state.
     */
    private void customizeNotationButtons(BaUnitNotationBean notation) {
        if (notation == null || !notation.getStatusCode().equals(StatusConstants.PENDING)
                || notation.getBaUnitId() == null
                || !notation.getBaUnitId().equals(baUnitBean1.getId())
                || notation.isLocked() || readOnly) {
            btnRemoveNotation.setEnabled(false);
        } else {
            btnRemoveNotation.setEnabled(true);
        }
        btnAddNotation.setEnabled(!readOnly);
        menuRemoveNotation.setEnabled(btnRemoveNotation.isEnabled());
    }

    /**
     * Enables or disables parcel buttons, depending on the form state and
     * selection in the list of parcel.
     */
    private void customizeParcelButtons() {
        // Allows manage parcel selection only for users with lease management right and if BaUnit state is pending
        boolean enabled = !readOnly
                && SecurityBean.isInRole(RolesConstants.ADMINISTRATIVE_MANAGE_LEASE)
                && (StringUtility.isEmpty(baUnitBean1.getStatusCode())
                || baUnitBean1.getStatusCode().equals(StatusConstants.PENDING));
        btnSearchParcel.setEnabled(enabled);
        btnAddParcelFromApplication.setEnabled(enabled);
    }

    /**
     * Enables or disables combobox list of right types, depending on the form
     * state.
     */
    private void customizeRightTypesList() {
        cbxRightType.setSelectedIndex(-1);
        rrrTypes.setSelectedRrrType(null);

        if (!readOnly && isActionAllowed(RrrTypeActionConstants.NEW)) {
            cbxRightType.setEnabled(true);

            // Restrict selection of right type by application service
            if (applicationService != null && applicationService.getRequestType() != null
                    && applicationService.getRequestType().getRrrTypeCode() != null) {

                // Check if the sublease mortgage right should be avaialble to the user
                boolean allowRightTypeSelection = false;
                if (isSubleaseMortgageAllowed(applicationService.getRequestType().getRrrTypeCode())) {
                    rrrTypes.restrictCodes(applicationService.getRequestType().getRrrTypeCode(),
                            RrrBean.CODE_SUBLEASE_MORTGAGE);
                    allowRightTypeSelection = true;
                }
                rrrTypes.setSelectedRightByCode(applicationService.getRequestType().getRrrTypeCode());
                if (rrrTypes.getSelectedRrrType() != null) {
                    cbxRightType.setEnabled(allowRightTypeSelection);
                }
            }
        } else {
            cbxRightType.setEnabled(false);
        }
        customizeCreateRightButton();
    }

    /**
     * Determines if the sublease mortgage right should be included in the list
     * of right types available to create/vary/cancel.
     *
     * @param serviceRrrTypeCode The rrr type targeted by the service
     * @return true if the service is for a mortgage and this property has a
     * sublease right on it.
     */
    private boolean isSubleaseMortgageAllowed(String serviceRrrTypeCode) {
        boolean result = false;
        if (RrrBean.CODE_MORTGAGE.equals(serviceRrrTypeCode)) {
            for (RrrBean bean : baUnitBean1.getRrrFilteredList()) {
                if (RrrBean.CODE_SUBLEASE.equals(bean.getTypeCode())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Enables or disables button for creating new right, depending on the form
     * state.
     */
    private void customizeCreateRightButton() {
        RrrTypeBean selectedRrrType = rrrTypes.getSelectedRrrType();
        boolean enabled = selectedRrrType != null && selectedRrrType.getCode() != null
                && !readOnly && isActionAllowed(RrrTypeActionConstants.NEW);
        if (enabled) {
            // Check if lease already in the list
            if (selectedRrrType.getCode().equals(RrrBean.CODE_LEASE)) {
                for (RrrBean rrr : baUnitBean1.getRrrFilteredList()) {
                    if (StringUtility.empty(rrr.getTypeCode()).equals(RrrBean.CODE_LEASE)) {
                        enabled = false;
                        break;
                    }
                }
            }
        }
        btnCreateRight.setEnabled(enabled);
    }

    /**
     * Enables or disables buttons for managing list of rights, depending on the
     * form state, selected right and it's state.
     */
    private void customizeRightsButtons(RrrBean rrrBean) {
        btnEditRight.setEnabled(false);
        btnRemoveRight.setEnabled(false);
        btnChangeRight.setEnabled(false);
        btnExtinguish.setEnabled(false);
        btnViewRight.setEnabled(rrrBean != null);

        if (rrrBean != null && !rrrBean.isLocked() && !readOnly) {
            boolean isPending = rrrBean.getStatusCode().equals(StatusConstants.PENDING);

            // Control pending state and allowed types of RRR for edit/remove buttons
            if (isPending && isRightTypeAllowed(rrrBean.getTypeCode())) {
                btnEditRight.setEnabled(true);
                btnRemoveRight.setEnabled(true);
            }

            // Control the record state, duplication of pending records,
            // allowed action and allowed type of RRR.
            if (rrrBean.getStatusCode().equals(StatusConstants.CURRENT)
                    && !baUnitBean1.isPendingRrrExists(rrrBean)
                    && isRightTypeAllowed(rrrBean.getTypeCode())) {
                if (isActionAllowed(RrrTypeActionConstants.VARY)) {
                    btnChangeRight.setEnabled(true);
                }
                if (isActionAllowed(RrrTypeActionConstants.CANCEL)) {
                    btnExtinguish.setEnabled(true);
                }
            }
        }

        menuEditRight.setEnabled(btnEditRight.isEnabled());
        menuRemoveRight.setEnabled(btnRemoveRight.isEnabled());
        menuVaryRight.setEnabled(btnChangeRight.isEnabled());
        menuExtinguishRight.setEnabled(btnExtinguish.isEnabled());
        menuViewRight.setEnabled(btnViewRight.isEnabled());
    }

    /**
     * Checks if certain action is allowed on the form.
     */
    private boolean isActionAllowed(String action) {
        boolean result = true;

        if (RrrTypeActionConstants.CANCEL.equalsIgnoreCase(action)) {
            // Default to false if the action is cancel as cannot have cancel and vary/new actions
            // supported by the same service.
            result = false;
        }

        if (applicationService != null && applicationService.getRequestType() != null
                && applicationService.getRequestType().getTypeActionCode() != null) {
            result = applicationService.getRequestType().getTypeActionCode().equalsIgnoreCase(action);
        }
        return result;
    }

    /**
     * Checks what type of rights are allowed to create/manage on the form.
     */
    private boolean isRightTypeAllowed(String rrrTypeCode) {
        boolean result = true;

        if (rrrTypeCode != null && applicationService != null
                && applicationService.getRequestType() != null
                && applicationService.getRequestType().getRrrTypeCode() != null) {
            result = applicationService.getRequestType().getRrrTypeCode().equalsIgnoreCase(rrrTypeCode);
            if (!result && isSubleaseMortgageAllowed(applicationService.getRequestType().getRrrTypeCode())) {
                result = RrrBean.CODE_SUBLEASE_MORTGAGE.equals(rrrTypeCode);
            }
        }
        return result;
    }

    /**
     * Returns true if cadastre object attached on the BaUnit, otherwise false.
     *
     * @param showMessage Indicates whether to show a warning message if
     * cadastre object isn't attached.
     */
    private boolean isCadastreObjectExists(boolean showMessage) {
        if (baUnitBean1.getCadastreObject() != null
                && !baUnitBean1.getCadastreObject().toString().equals("")) {
            return true;
        }
        if (showMessage) {
            MessageUtility.displayMessage(ClientMessage.BAUNIT_SELECT_PARCEL);
            tabsMain.setSelectedComponent(pnlPlot);
        }
        return false;
    }

    /**
     * Removes selected right from the list of rights.
     */
    private void removeRight() {
        if (baUnitBean1.getSelectedRight() != null
                && MessageUtility.displayMessage(ClientMessage.CONFIRM_DELETE_RECORD) == MessageUtility.BUTTON_ONE) {
            baUnitBean1.removeSelectedRight();
            customizeCreateRightButton();
        }
    }

    /**
     * Opens appropriate right form for editing.
     */
    private void editRight() {
        if (baUnitBean1.getSelectedRight() != null) {
            openRightForm(baUnitBean1.getSelectedRight(), RrrBean.RRR_ACTION.EDIT);
        }
    }

    /**
     * Opens appropriate right form to extinguish selected right.
     */
    private void extinguishRight() {
        if (baUnitBean1.getSelectedRight() != null) {
            openRightForm(baUnitBean1.getSelectedRight(), RrrBean.RRR_ACTION.CANCEL);
        }
    }

    /**
     * Opens appropriate right form to create new right.
     */
    private void createRight() {
        openRightForm(null, RrrBean.RRR_ACTION.NEW);
    }

    /**
     * Opens appropriate right form to vary selected right.
     */
    private void varyRight() {
        if (baUnitBean1.getSelectedRight() != null) {
            openRightForm(baUnitBean1.getSelectedRight(), RrrBean.RRR_ACTION.VARY);
        }
    }

    /**
     * Adds new notation on the BaUnit.
     */
    private void addNotation() {
        if (baUnitBean1.addBaUnitNotation(txtNotationText.getText())) {
            txtNotationText.setText(null);
        }
    }

    /**
     * Removes selected notation.
     */
    private void removeNotation() {
        if (MessageUtility.displayMessage(ClientMessage.CONFIRM_DELETE_RECORD) == MessageUtility.BUTTON_ONE) {
            baUnitBean1.removeSelectedBaUnitNotation();
        }
    }

    /**
     * Opens right form, depending on given {@link RrrBean} and action.
     *
     * @param rrrBean {@link RrrBean} instance to figure out what form to open
     * and pass this bean as a parameter.
     * @param action {@link RrrBean#RRR_ACTION} is passed to the right form for
     * further form customization.
     */
    private void openRightForm(RrrBean rrrBean, RrrBean.RRR_ACTION action) {
        if (!isCadastreObjectExists(true)) {
            return;
        }

        if (action == RrrBean.RRR_ACTION.NEW && rrrTypes.getSelectedRrrType() == null) {
            return;
        }

        if (rrrBean == null) {
            rrrBean = new RrrBean();
            rrrBean.setTypeCode(rrrTypes.getSelectedRrrType().getCode());
        }

        PropertyChangeListener rightFormListener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                // Add new RRR
                if (evt.getNewValue() != null) {
                    baUnitBean1.addRrr((RrrBean) evt.getNewValue());
                    tableRights.clearSelection();
                    customizeCreateRightButton();
                }
            }
        };

        ContentPanel panel;
        String cardName = MainContentPanel.CARD_SIMPLE_RIGHT;
        String rrrCode = rrrBean.getRrrType().getCode();

        if (rrrCode.equals(RrrBean.CODE_MORTGAGE) || rrrCode.equals(RrrBean.CODE_SUBLEASE_MORTGAGE)) {
            panel = new MortgagePanel(rrrBean, applicationBean, applicationService, action);
            cardName = MainContentPanel.CARD_MORTGAGE;
        } else if (rrrCode.equalsIgnoreCase(RrrBean.CODE_LEASE)) {
            panel = new LeasePanel(baUnitBean1, rrrBean, applicationBean, applicationService, action,
                    mapControl);
            cardName = MainContentPanel.CARD_LEASE;
        } else if (rrrCode.equalsIgnoreCase(RrrBean.CODE_SUBLEASE)) {
            panel = new LeasePanel(baUnitBean1, rrrBean, applicationBean, applicationService, action, null);
            cardName = MainContentPanel.CARD_SUBLEASE;
        } else if (rrrCode.equalsIgnoreCase(RrrBean.CODE_AGRI_ACTIVITY)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_COMMON_OWNERSHIP)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_CUSTOMARY_TYPE)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_FIREWOOD)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_FISHING)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_GRAZING)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_OCCUPATION)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_OWNERSHIP_ASSUMED)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_SUPERFICIES)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_TENANCY)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_USUFRUCT)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_WATERRIGHTS)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_ADMIN_PUBLIC_SERVITUDE)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_MONUMENT)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_LIFE_ESTATE)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_SERVITUDE)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_CAVEAT)) {
            panel = new SimpleRightholderPanel(rrrBean, applicationBean, applicationService, action);
            cardName = MainContentPanel.CARD_SIMPLE_OWNERSHIP;
        } else if (rrrCode.equalsIgnoreCase(RrrBean.CODE_OWNERSHIP)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_STATE_OWNERSHIP)
                || rrrCode.equalsIgnoreCase(RrrBean.CODE_APARTMENT)) {
            panel = new OwnershipPanel(rrrBean, applicationBean, applicationService, action);
            cardName = MainContentPanel.CARD_OWNERSHIP;
        } else {
            panel = new SimpleRightPanel(rrrBean, applicationBean, applicationService, action);
        }

        panel.addPropertyChangeListener(SimpleRightPanel.UPDATED_RRR, rightFormListener);
        getMainContentPanel().addPanel(panel, cardName, true);
    }

    private void saveBaUnit(final boolean showMessage, final boolean closeOnSave) {

        if (baUnitBean1.validate(true).size() > 0) {
            return;
        }

        if (!baUnitBean1.isValid()) {
            return;
        }

        SolaTask<Void, Void> t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));

                if (baUnitID != null && !baUnitID.equals("")) {
                    baUnitBean1.saveBaUnit(applicationService.getId());
                } else {
                    baUnitBean1.createBaUnit(applicationService.getId());
                }
                if (closeOnSave) {
                    close();
                }
                return null;
            }

            @Override
            public void taskDone() {
                if (showMessage) {
                    MessageUtility.displayMessage(ClientMessage.BAUNIT_SAVED);
                }
                saveBaUnitState();

            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void openParcelSearch() {
        CadastreObjectSearchForm form = new CadastreObjectSearchForm();
        form.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(CadastreObjectsSearchPanel.SELECTED_CADASTRE_OBJECT)) {
                    baUnitBean1.setCadastreObject(CadastreObjectBean.getCadastreObject(
                            ((CadastreObjectSummaryBean) evt.getNewValue()).getId()));
                }
            }
        });
        getMainContentPanel().addPanel(form, MainContentPanel.CARD_PARCEL_SEARCH, true);
    }

    /**
     * Opens property form in read only mode for a given BaUnit.
     */
    private void openPropertyForm(final RelatedBaUnitInfoBean relatedBaUnit) {
        if (relatedBaUnit != null && relatedBaUnit.getRelatedBaUnit() != null) {
            SolaTask t = new SolaTask<Void, Void>() {

                @Override
                public Void doTask() {
                    setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_PROPERTY));
                    BaUnitBean baUnitBean = BaUnitBean.getBaUnitsById(relatedBaUnit.getRelatedBaUnitId());
                    PropertyPanel propertyPnl = new PropertyPanel(null, null, baUnitBean, true);
                    getMainContentPanel().addPanel(propertyPnl,
                            MainContentPanel.CARD_PROPERTY_PANEL + "_"
                            + relatedBaUnit.getRelatedBaUnit().getId(), true);
                    return null;
                }
            };
            TaskManager.getInstance().runTask(t);
        }
    }

    private void saveBaUnitState() {
        MainForm.saveBeanState(baUnitBean1);
    }

    @Override
    protected boolean panelClosing() {
        if (btnSave.isEnabled() && MainForm.checkSaveBeforeClose(baUnitBean1)) {
            saveBaUnit(true, true);
            return false;
        }
        return true;
    }

    private void openApplicationParcelsForm() {
        CadastreObjectsDialog form = new CadastreObjectsDialog(
                applicationBean.getCadastreObjectFilteredList(), MainForm.getInstance(), true);
        WindowUtility.centerForm(form);
        form.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(CadastreObjectsDialog.SELECT_CADASTRE_OBJECT)) {
                    baUnitBean1.setCadastreObject(CadastreObjectBean.getCadastreObject(
                            ((CadastreObjectSummaryBean) evt.getNewValue()).getId()));
                }
            }
        });
        form.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        baUnitBean1 = createBaUnitBean();
        baUnitRrrTypes = new org.sola.clients.beans.referencedata.RrrTypeListBean();
        rrrTypes = new org.sola.clients.beans.referencedata.RrrTypeListBean();
        popupRights = new javax.swing.JPopupMenu();
        menuVaryRight = new javax.swing.JMenuItem();
        menuExtinguishRight = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        menuViewRight = new javax.swing.JMenuItem();
        menuEditRight = new javax.swing.JMenuItem();
        menuRemoveRight = new javax.swing.JMenuItem();
        popupNotations = new javax.swing.JPopupMenu();
        menuRemoveNotation = new javax.swing.JMenuItem();
        popupParentBaUnits = new javax.swing.JPopupMenu();
        menuOpenParentBaUnit = new javax.swing.JMenuItem();
        menuAddParentBaUnit = new javax.swing.JMenuItem();
        menuRemoveParentBaUnit = new javax.swing.JMenuItem();
        popupChildBaUnits = new javax.swing.JPopupMenu();
        menuOpenChildBaUnit = new javax.swing.JMenuItem();
        jToolBar5 = new javax.swing.JToolBar();
        btnSave = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jLabel4 = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabsMain = new javax.swing.JTabbedPane();
        pnlPlot = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnAddParcelFromApplication = new javax.swing.JButton();
        btnSearchParcel = new javax.swing.JButton();
        cadastreObject = new org.sola.clients.swing.ui.cadastre.ParcelPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableRights = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel16 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        cbxRightType = new javax.swing.JComboBox();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 0), new java.awt.Dimension(10, 32767));
        btnCreateRight = new javax.swing.JButton();
        btnChangeRight = new javax.swing.JButton();
        btnExtinguish = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnViewRight = new javax.swing.JButton();
        btnEditRight = new javax.swing.JButton();
        btnRemoveRight = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        jToolBar8 = new javax.swing.JToolBar();
        jLabel8 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        btnViewHistoricRight = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        tableRightsHistory = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableOwnership = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableNotations = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jToolBar3 = new javax.swing.JToolBar();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 0), new java.awt.Dimension(5, 32767));
        btnAddNotation = new javax.swing.JButton();
        btnRemoveNotation = new javax.swing.JButton();
        txtNotationText = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        pnlPriorProperties = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jToolBar6 = new javax.swing.JToolBar();
        jLabel6 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnOpenParent = new javax.swing.JButton();
        btnAddParent = new javax.swing.JButton();
        btnRemoveParent = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableParentBaUnits = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jPanel5 = new javax.swing.JPanel();
        jToolBar7 = new javax.swing.JToolBar();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        btnOpenChild = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tableChildBaUnits = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        mapPanel = new javax.swing.JPanel();
        headerPanel = new org.sola.clients.swing.ui.HeaderPanel();

        popupRights.setName("popupRights"); // NOI18N

        menuVaryRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/vary.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/administrative/Bundle"); // NOI18N
        menuVaryRight.setText(bundle.getString("PropertyPanel.menuVaryRight.text")); // NOI18N
        menuVaryRight.setName("menuVaryRight"); // NOI18N
        menuVaryRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuVaryRightActionPerformed(evt);
            }
        });
        popupRights.add(menuVaryRight);

        menuExtinguishRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/flag.png"))); // NOI18N
        menuExtinguishRight.setText(bundle.getString("PropertyPanel.menuExtinguishRight.text")); // NOI18N
        menuExtinguishRight.setToolTipText(bundle.getString("PropertyPanel.menuExtinguishRight.toolTipText")); // NOI18N
        menuExtinguishRight.setName("menuExtinguishRight"); // NOI18N
        menuExtinguishRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExtinguishRightActionPerformed(evt);
            }
        });
        popupRights.add(menuExtinguishRight);

        jSeparator5.setName("jSeparator5"); // NOI18N
        popupRights.add(jSeparator5);

        menuViewRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        menuViewRight.setText(bundle.getString("PropertyPanel.menuViewRight.text")); // NOI18N
        menuViewRight.setName("menuViewRight"); // NOI18N
        popupRights.add(menuViewRight);

        menuEditRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/document--pencil.png"))); // NOI18N
        menuEditRight.setText(bundle.getString("PropertyPanel.menuEditRight.text")); // NOI18N
        menuEditRight.setName("menuEditRight"); // NOI18N
        menuEditRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditRightActionPerformed(evt);
            }
        });
        popupRights.add(menuEditRight);

        menuRemoveRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        menuRemoveRight.setText(bundle.getString("PropertyPanel.menuRemoveRight.text")); // NOI18N
        menuRemoveRight.setName("menuRemoveRight"); // NOI18N
        menuRemoveRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveRightActionPerformed(evt);
            }
        });
        popupRights.add(menuRemoveRight);

        popupNotations.setName("popupNotations"); // NOI18N

        menuRemoveNotation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        menuRemoveNotation.setText(bundle.getString("PropertyPanel.menuRemoveNotation.text")); // NOI18N
        menuRemoveNotation.setName("menuRemoveNotation"); // NOI18N
        menuRemoveNotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveNotationActionPerformed(evt);
            }
        });
        popupNotations.add(menuRemoveNotation);

        popupParentBaUnits.setName("popupParentBaUnits"); // NOI18N

        menuOpenParentBaUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        menuOpenParentBaUnit.setText(bundle.getString("PropertyPanel.menuOpenParentBaUnit.text")); // NOI18N
        menuOpenParentBaUnit.setName("menuOpenParentBaUnit"); // NOI18N
        menuOpenParentBaUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenParentBaUnitActionPerformed(evt);
            }
        });
        popupParentBaUnits.add(menuOpenParentBaUnit);

        menuAddParentBaUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        menuAddParentBaUnit.setText(bundle.getString("PropertyPanel.menuAddParentBaUnit.text")); // NOI18N
        menuAddParentBaUnit.setName("menuAddParentBaUnit"); // NOI18N
        menuAddParentBaUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddParentBaUnitActionPerformed(evt);
            }
        });
        popupParentBaUnits.add(menuAddParentBaUnit);

        menuRemoveParentBaUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        menuRemoveParentBaUnit.setText(bundle.getString("PropertyPanel.menuRemoveParentBaUnit.text")); // NOI18N
        menuRemoveParentBaUnit.setName("menuRemoveParentBaUnit"); // NOI18N
        menuRemoveParentBaUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveParentBaUnitActionPerformed(evt);
            }
        });
        popupParentBaUnits.add(menuRemoveParentBaUnit);

        popupChildBaUnits.setName("popupChildBaUnits"); // NOI18N

        menuOpenChildBaUnit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        menuOpenChildBaUnit.setText(bundle.getString("PropertyPanel.menuOpenChildBaUnit.text")); // NOI18N
        menuOpenChildBaUnit.setName("menuOpenChildBaUnit"); // NOI18N
        menuOpenChildBaUnit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenChildBaUnitActionPerformed(evt);
            }
        });
        popupChildBaUnits.add(menuOpenChildBaUnit);

        setHeaderPanel(headerPanel);
        setHelpTopic(bundle.getString("PropertyPanel.helpTopic")); // NOI18N
        setName("Form"); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jToolBar5.setFloatable(false);
        jToolBar5.setRollover(true);
        jToolBar5.setName("jToolBar5"); // NOI18N

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/save.png"))); // NOI18N
        btnSave.setText(bundle.getString("PropertyPanel.btnSave.text")); // NOI18N
        btnSave.setToolTipText(bundle.getString("PropertyPanel.btnSave.toolTipText")); // NOI18N
        btnSave.setName("btnSave"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jToolBar5.add(btnSave);

        jSeparator7.setName(bundle.getString("PropertyPanel.jSeparator7.name")); // NOI18N
        jToolBar5.add(jSeparator7);

        jLabel4.setText(bundle.getString("PropertyPanel.jLabel4.text")); // NOI18N
        jLabel4.setName(bundle.getString("PropertyPanel.jLabel4.name")); // NOI18N
        jToolBar5.add(jLabel4);

        lblStatus.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblStatus.setName(bundle.getString("PropertyPanel.lblStatus.name")); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitBean1, org.jdesktop.beansbinding.ELProperty.create("${status.displayValue}"), lblStatus, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jToolBar5.add(lblStatus);

        jScrollPane6.setBorder(null);
        jScrollPane6.setName("jScrollPane6"); // NOI18N

        tabsMain.setName("tabsMain"); // NOI18N
        tabsMain.setPreferredSize(new java.awt.Dimension(494, 300));

        pnlPlot.setName("pnlPlot"); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        btnAddParcelFromApplication.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAddParcelFromApplication.setText(bundle.getString("PropertyPanel.btnAddParcelFromApplication.text_1")); // NOI18N
        btnAddParcelFromApplication.setFocusable(false);
        btnAddParcelFromApplication.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        btnAddParcelFromApplication.setName(bundle.getString("PropertyPanel.btnAddParcelFromApplication.name_1")); // NOI18N
        btnAddParcelFromApplication.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddParcelFromApplicationActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAddParcelFromApplication);

        btnSearchParcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnSearchParcel.setText(bundle.getString("PropertyPanel.btnSearchParcel.text_1")); // NOI18N
        btnSearchParcel.setFocusable(false);
        btnSearchParcel.setName(bundle.getString("PropertyPanel.btnSearchParcel.name_1")); // NOI18N
        btnSearchParcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchParcelActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSearchParcel);

        cadastreObject.setName(bundle.getString("PropertyPanel.cadastreObject.name")); // NOI18N

        org.jdesktop.layout.GroupLayout pnlPlotLayout = new org.jdesktop.layout.GroupLayout(pnlPlot);
        pnlPlot.setLayout(pnlPlotLayout);
        pnlPlotLayout.setHorizontalGroup(
            pnlPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlPlotLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnlPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                    .add(cadastreObject, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlPlotLayout.setVerticalGroup(
            pnlPlotLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlPlotLayout.createSequentialGroup()
                .addContainerGap()
                .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cadastreObject, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabsMain.addTab(bundle.getString("PropertyPanel.pnlPlot.TabConstraints.tabTitle"), pnlPlot); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N

        jPanel16.setName(bundle.getString("PropertyPanel.jPanel16.name")); // NOI18N
        jPanel16.setLayout(new java.awt.GridLayout(2, 1, 0, 10));

        jPanel15.setName(bundle.getString("PropertyPanel.jPanel15.name_3")); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        tableRights.setComponentPopupMenu(popupRights);
        tableRights.setName("tableRights"); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${rrrFilteredList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitBean1, eLProperty, tableRights);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rrrType.displayValue}"));
        columnBinding.setColumnName("Rrr Type.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registrationNumber}"));
        columnBinding.setColumnName("Registration Number");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registrationDate}"));
        columnBinding.setColumnName("Registration Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rightholdersString}"));
        columnBinding.setColumnName("Rightholders String");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${notation.notationText}"));
        columnBinding.setColumnName("Notation.notation Text");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${status.displayValue}"));
        columnBinding.setColumnName("Status.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitBean1, org.jdesktop.beansbinding.ELProperty.create("${selectedRight}"), tableRights, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        tableRights.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRightsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableRights);
        tableRights.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableRights.getColumnModel().getColumn(0).setMaxWidth(100);
        tableRights.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("PropertyPanel.tableRights.columnModel.title0")); // NOI18N
        tableRights.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableRights.getColumnModel().getColumn(1).setMaxWidth(100);
        tableRights.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("PropertyPanel.tableRights.columnModel.title3")); // NOI18N
        tableRights.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableRights.getColumnModel().getColumn(2).setMaxWidth(120);
        tableRights.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("PropertyPanel.tableRights.columnModel.title1")); // NOI18N
        tableRights.getColumnModel().getColumn(2).setCellRenderer(new DateTimeRenderer());
        tableRights.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("PropertyPanel.tableRights.columnModel.title5")); // NOI18N
        tableRights.getColumnModel().getColumn(3).setCellRenderer(new CellDelimitedListRenderer(", "));
        tableRights.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("PropertyPanel.tableRights.columnModel.title4_1")); // NOI18N
        tableRights.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableRights.getColumnModel().getColumn(5).setMaxWidth(100);
        tableRights.getColumnModel().getColumn(5).setHeaderValue(bundle.getString("PropertyPanel.tableRights.columnModel.title2")); // NOI18N

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setName("jToolBar2"); // NOI18N

        jLabel16.setText(bundle.getString("PropertyPanel.jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N
        jToolBar2.add(jLabel16);

        filler1.setName("filler1"); // NOI18N
        jToolBar2.add(filler1);

        cbxRightType.setLightWeightPopupEnabled(false);
        cbxRightType.setMaximumSize(new java.awt.Dimension(170, 20));
        cbxRightType.setName("cbxRightType"); // NOI18N
        cbxRightType.setRenderer(new SimpleComboBoxRenderer("getDisplayValue"));

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${rrrTypeBeanList}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrTypes, eLProperty, cbxRightType);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rrrTypes, org.jdesktop.beansbinding.ELProperty.create("${selectedRrrType}"), cbxRightType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        jToolBar2.add(cbxRightType);

        filler2.setName("filler2"); // NOI18N
        jToolBar2.add(filler2);

        btnCreateRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/create.png"))); // NOI18N
        btnCreateRight.setText(bundle.getString("PropertyPanel.btnCreateRight.text")); // NOI18N
        btnCreateRight.setName("btnCreateRight"); // NOI18N
        btnCreateRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateRightActionPerformed(evt);
            }
        });
        jToolBar2.add(btnCreateRight);

        btnChangeRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/vary.png"))); // NOI18N
        btnChangeRight.setText(bundle.getString("PropertyPanel.btnChangeRight.text")); // NOI18N
        btnChangeRight.setName("btnChangeRight"); // NOI18N
        btnChangeRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangeRightActionPerformed(evt);
            }
        });
        jToolBar2.add(btnChangeRight);

        btnExtinguish.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/flag.png"))); // NOI18N
        btnExtinguish.setText(bundle.getString("PropertyPanel.btnExtinguish.text")); // NOI18N
        btnExtinguish.setToolTipText(bundle.getString("PropertyPanel.btnExtinguish.toolTipText")); // NOI18N
        btnExtinguish.setName("btnExtinguish"); // NOI18N
        btnExtinguish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExtinguishActionPerformed(evt);
            }
        });
        jToolBar2.add(btnExtinguish);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar2.add(jSeparator1);

        btnViewRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        btnViewRight.setText(bundle.getString("PropertyPanel.btnViewRight.text")); // NOI18N
        btnViewRight.setFocusable(false);
        btnViewRight.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnViewRight.setName("btnViewRight"); // NOI18N
        btnViewRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewRightActionPerformed(evt);
            }
        });
        jToolBar2.add(btnViewRight);

        btnEditRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/document--pencil.png"))); // NOI18N
        btnEditRight.setText(bundle.getString("PropertyPanel.btnEditRight.text")); // NOI18N
        btnEditRight.setName("btnEditRight"); // NOI18N
        btnEditRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditRightActionPerformed(evt);
            }
        });
        jToolBar2.add(btnEditRight);

        btnRemoveRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        btnRemoveRight.setText(bundle.getString("PropertyPanel.btnRemoveRight.text")); // NOI18N
        btnRemoveRight.setToolTipText(bundle.getString("PropertyPanel.btnRemoveRight.toolTipText")); // NOI18N
        btnRemoveRight.setName("btnRemoveRight"); // NOI18N
        btnRemoveRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveRightActionPerformed(evt);
            }
        });
        jToolBar2.add(btnRemoveRight);

        org.jdesktop.layout.GroupLayout jPanel15Layout = new org.jdesktop.layout.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
            .add(jToolBar2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel15Layout.createSequentialGroup()
                .add(jToolBar2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
        );

        jPanel16.add(jPanel15);

        jPanel17.setName(bundle.getString("PropertyPanel.jPanel17.name")); // NOI18N

        jToolBar8.setFloatable(false);
        jToolBar8.setRollover(true);
        jToolBar8.setName(bundle.getString("PropertyPanel.jToolBar8.name")); // NOI18N

        jLabel8.setText(bundle.getString("PropertyPanel.jLabel8.text")); // NOI18N
        jLabel8.setName(bundle.getString("PropertyPanel.jLabel8.name")); // NOI18N
        jToolBar8.add(jLabel8);

        jSeparator6.setName(bundle.getString("PropertyPanel.jSeparator6.name")); // NOI18N
        jToolBar8.add(jSeparator6);

        btnViewHistoricRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        btnViewHistoricRight.setText(bundle.getString("PropertyPanel.btnViewHistoricRight.text")); // NOI18N
        btnViewHistoricRight.setFocusable(false);
        btnViewHistoricRight.setName(bundle.getString("PropertyPanel.btnViewHistoricRight.name")); // NOI18N
        btnViewHistoricRight.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnViewHistoricRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewHistoricRightActionPerformed(evt);
            }
        });
        jToolBar8.add(btnViewHistoricRight);

        jScrollPane8.setName(bundle.getString("PropertyPanel.jScrollPane8.name")); // NOI18N

        tableRightsHistory.setName(bundle.getString("PropertyPanel.tableRightsHistory.name")); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${rrrHistoricList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitBean1, eLProperty, tableRightsHistory);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rrrType.displayValue}"));
        columnBinding.setColumnName("Rrr Type.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registrationNumber}"));
        columnBinding.setColumnName("Registration Number");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${registrationDate}"));
        columnBinding.setColumnName("Registration Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${rightholdersString}"));
        columnBinding.setColumnName("Rightholders String");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${notation.notationText}"));
        columnBinding.setColumnName("Notation.notation Text");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${status.displayValue}"));
        columnBinding.setColumnName("Status.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitBean1, org.jdesktop.beansbinding.ELProperty.create("${selectedHistoricRight}"), tableRightsHistory, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        tableRightsHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableRightsHistoryMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tableRightsHistory);
        tableRightsHistory.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableRightsHistory.getColumnModel().getColumn(0).setMaxWidth(100);
        tableRightsHistory.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("PropertyPanel.tableRightsHistory.columnModel.title0")); // NOI18N
        tableRightsHistory.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableRightsHistory.getColumnModel().getColumn(1).setMaxWidth(100);
        tableRightsHistory.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("PropertyPanel.tableRightsHistory.columnModel.title3")); // NOI18N
        tableRightsHistory.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableRightsHistory.getColumnModel().getColumn(2).setMaxWidth(120);
        tableRightsHistory.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("PropertyPanel.tableRightsHistory.columnModel.title1")); // NOI18N
        tableRightsHistory.getColumnModel().getColumn(2).setCellRenderer(new DateTimeRenderer());
        tableRightsHistory.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("PropertyPanel.tableRightsHistory.columnModel.title5")); // NOI18N
        tableRightsHistory.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("PropertyPanel.tableRightsHistory.columnModel.title4")); // NOI18N
        tableRightsHistory.getColumnModel().getColumn(5).setPreferredWidth(100);
        tableRightsHistory.getColumnModel().getColumn(5).setMaxWidth(100);
        tableRightsHistory.getColumnModel().getColumn(5).setHeaderValue(bundle.getString("PropertyPanel.tableRightsHistory.columnModel.title2")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel17Layout = new org.jdesktop.layout.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jToolBar8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jScrollPane8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel17Layout.createSequentialGroup()
                .add(jToolBar8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
        );

        jPanel16.add(jPanel17);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel16, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel16, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabsMain.addTab(bundle.getString("PropertyPanel.jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        tableOwnership.setName("tableOwnership"); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${primaryRight.filteredRightHolderList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitBean1, eLProperty, tableOwnership);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fullName}"));
        columnBinding.setColumnName("Full Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane5.setViewportView(tableOwnership);
        tableOwnership.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("PropertyPanel.tableOwnership.columnModel.title0_1")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabsMain.addTab(bundle.getString("PropertyPanel.jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        tableNotations.setComponentPopupMenu(popupNotations);
        tableNotations.setName("tableNotations"); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${allBaUnitNotationList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitBean1, eLProperty, tableNotations);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${notationText}"));
        columnBinding.setColumnName("Notation Text");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${referenceNr}"));
        columnBinding.setColumnName("Reference Nr");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${changeTime}"));
        columnBinding.setColumnName("Change Time");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitBean1, org.jdesktop.beansbinding.ELProperty.create("${selectedBaUnitNotation}"), tableNotations, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane4.setViewportView(tableNotations);
        tableNotations.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("PropertyPanel.tableNotations.columnModel.title1")); // NOI18N
        tableNotations.getColumnModel().getColumn(1).setPreferredWidth(100);
        tableNotations.getColumnModel().getColumn(1).setMaxWidth(100);
        tableNotations.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("PropertyPanel.tableNotations.columnModel.title0")); // NOI18N
        tableNotations.getColumnModel().getColumn(2).setPreferredWidth(120);
        tableNotations.getColumnModel().getColumn(2).setMaxWidth(120);
        tableNotations.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("PropertyPanel.tableNotations.columnModel.title3")); // NOI18N
        tableNotations.getColumnModel().getColumn(2).setCellRenderer(new DateTimeRenderer());

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);
        jToolBar3.setName("jToolBar3"); // NOI18N

        filler4.setName("filler4"); // NOI18N
        jToolBar3.add(filler4);

        btnAddNotation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAddNotation.setText(bundle.getString("PropertyPanel.btnAddNotation.text")); // NOI18N
        btnAddNotation.setName("btnAddNotation"); // NOI18N
        btnAddNotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNotationActionPerformed(evt);
            }
        });
        jToolBar3.add(btnAddNotation);

        btnRemoveNotation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        btnRemoveNotation.setText(bundle.getString("PropertyPanel.btnRemoveNotation.text")); // NOI18N
        btnRemoveNotation.setName("btnRemoveNotation"); // NOI18N
        btnRemoveNotation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveNotationActionPerformed(evt);
            }
        });
        jToolBar3.add(btnRemoveNotation);

        txtNotationText.setMinimumSize(new java.awt.Dimension(150, 20));
        txtNotationText.setName("txtNotationText"); // NOI18N

        jLabel15.setText(bundle.getString("PropertyPanel.jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jLabel15)
                        .add(9, 9, 9)
                        .add(txtNotationText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jToolBar3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 139, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jToolBar3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(txtNotationText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel15)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabsMain.addTab(bundle.getString("PropertyPanel.jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        pnlPriorProperties.setName("pnlPriorProperties"); // NOI18N

        jPanel14.setName("jPanel14"); // NOI18N
        jPanel14.setLayout(new java.awt.GridLayout(2, 1, 0, 15));

        jPanel8.setName("jPanel8"); // NOI18N

        jToolBar6.setFloatable(false);
        jToolBar6.setRollover(true);
        jToolBar6.setName("jToolBar6"); // NOI18N

        jLabel6.setFont(LafManager.getInstance().getLabFontBold());
        jLabel6.setText(bundle.getString("PropertyPanel.jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N
        jToolBar6.add(jLabel6);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jToolBar6.add(jSeparator3);

        btnOpenParent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        btnOpenParent.setText(bundle.getString("PropertyPanel.btnOpenParent.text")); // NOI18N
        btnOpenParent.setToolTipText(bundle.getString("PropertyPanel.btnOpenParent.toolTipText")); // NOI18N
        btnOpenParent.setFocusable(false);
        btnOpenParent.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOpenParent.setName("btnOpenParent"); // NOI18N
        btnOpenParent.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenParent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenParentActionPerformed(evt);
            }
        });
        jToolBar6.add(btnOpenParent);

        btnAddParent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAddParent.setText(bundle.getString("PropertyPanel.btnAddParent.text")); // NOI18N
        btnAddParent.setFocusable(false);
        btnAddParent.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAddParent.setName("btnAddParent"); // NOI18N
        btnAddParent.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddParent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddParentActionPerformed(evt);
            }
        });
        jToolBar6.add(btnAddParent);

        btnRemoveParent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        btnRemoveParent.setText(bundle.getString("PropertyPanel.btnRemoveParent.text")); // NOI18N
        btnRemoveParent.setFocusable(false);
        btnRemoveParent.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRemoveParent.setName("btnRemoveParent"); // NOI18N
        btnRemoveParent.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveParent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveParentActionPerformed(evt);
            }
        });
        jToolBar6.add(btnRemoveParent);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        tableParentBaUnits.setComponentPopupMenu(popupParentBaUnits);
        tableParentBaUnits.setName("tableParentBaUnits"); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredParentBaUnits}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitBean1, eLProperty, tableParentBaUnits);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${relatedBaUnit.code}"));
        columnBinding.setColumnName("Related Ba Unit.code");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${baUnitRelType.displayValue}"));
        columnBinding.setColumnName("Ba Unit Rel Type.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${relatedBaUnit.status.displayValue}"));
        columnBinding.setColumnName("Related Ba Unit.status.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitBean1, org.jdesktop.beansbinding.ELProperty.create("${selectedParentBaUnit}"), tableParentBaUnits, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane3.setViewportView(tableParentBaUnits);
        tableParentBaUnits.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("PropertyPanel.tableParentBaUnits.columnModel.title1_1")); // NOI18N
        tableParentBaUnits.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("PropertyPanel.jTable1.columnModel.title4")); // NOI18N
        tableParentBaUnits.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("PropertyPanel.jTable1.columnModel.title3_1")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jToolBar6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(jToolBar6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel8);

        jPanel5.setName("jPanel5"); // NOI18N

        jToolBar7.setFloatable(false);
        jToolBar7.setRollover(true);
        jToolBar7.setName("jToolBar7"); // NOI18N

        jLabel3.setFont(LafManager.getInstance().getLabFontBold());
        jLabel3.setText(bundle.getString("PropertyPanel.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        jToolBar7.add(jLabel3);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jToolBar7.add(jSeparator2);

        btnOpenChild.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        btnOpenChild.setText(bundle.getString("PropertyPanel.btnOpenChild.text")); // NOI18N
        btnOpenChild.setFocusable(false);
        btnOpenChild.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnOpenChild.setName("btnOpenChild"); // NOI18N
        btnOpenChild.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnOpenChild.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenChildActionPerformed(evt);
            }
        });
        jToolBar7.add(btnOpenChild);

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        tableChildBaUnits.setComponentPopupMenu(popupChildBaUnits);
        tableChildBaUnits.setName("tableChildBaUnits"); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredChildBaUnits}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitBean1, eLProperty, tableChildBaUnits);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${relatedBaUnit.code}"));
        columnBinding.setColumnName("Related Ba Unit.code");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${baUnitRelType.displayValue}"));
        columnBinding.setColumnName("Ba Unit Rel Type.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${relatedBaUnit.status.displayValue}"));
        columnBinding.setColumnName("Related Ba Unit.status.display Value");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, baUnitBean1, org.jdesktop.beansbinding.ELProperty.create("${selectedChildBaUnit}"), tableChildBaUnits, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane7.setViewportView(tableChildBaUnits);
        tableChildBaUnits.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("PropertyPanel.tableChildBaUnits.columnModel.title1_1")); // NOI18N
        tableChildBaUnits.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("PropertyPanel.tableChildBaUnits.columnModel.title3_1")); // NOI18N
        tableChildBaUnits.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("PropertyPanel.tableChildBaUnits.columnModel.title4")); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jToolBar7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
            .add(jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jToolBar7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel5);

        org.jdesktop.layout.GroupLayout pnlPriorPropertiesLayout = new org.jdesktop.layout.GroupLayout(pnlPriorProperties);
        pnlPriorProperties.setLayout(pnlPriorPropertiesLayout);
        pnlPriorPropertiesLayout.setHorizontalGroup(
            pnlPriorPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlPriorPropertiesLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlPriorPropertiesLayout.setVerticalGroup(
            pnlPriorPropertiesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnlPriorPropertiesLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabsMain.addTab(bundle.getString("PropertyPanel.pnlPriorProperties.TabConstraints.tabTitle"), pnlPriorProperties); // NOI18N

        mapPanel.setName("mapPanel"); // NOI18N
        mapPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                mapPanelComponentShown(evt);
            }
        });

        org.jdesktop.layout.GroupLayout mapPanelLayout = new org.jdesktop.layout.GroupLayout(mapPanel);
        mapPanel.setLayout(mapPanelLayout);
        mapPanelLayout.setHorizontalGroup(
            mapPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 697, Short.MAX_VALUE)
        );
        mapPanelLayout.setVerticalGroup(
            mapPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 405, Short.MAX_VALUE)
        );

        tabsMain.addTab(bundle.getString("PropertyPanel.mapPanel.TabConstraints.tabTitle"), mapPanel); // NOI18N

        jScrollPane6.setViewportView(tabsMain);

        headerPanel.setName("headerPanel"); // NOI18N
        headerPanel.setTitleText(bundle.getString("PropertyPanel.headerPanel.titleText")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(headerPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
            .add(jToolBar5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 722, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane6)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(headerPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jToolBar5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                .addContainerGap())
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
    saveBaUnit(true, false);
    customizeForm();
}//GEN-LAST:event_btnSaveActionPerformed

    private void tableRightsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRightsMouseClicked
        if (evt.getClickCount() > 1 && baUnitBean1.getSelectedRight() != null) {
            openRightForm(baUnitBean1.getSelectedRight(), RrrBean.RRR_ACTION.VIEW);
        }
    }//GEN-LAST:event_tableRightsMouseClicked

private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
    if (this.mapControl == null) {
        this.mapControl = new ControlsBundleForBaUnit();
        if (applicationBean != null) {
            this.mapControl.setApplicationId(this.applicationBean.getId());
        }
        this.mapPanel.setLayout(new BorderLayout());
        this.mapPanel.add(this.mapControl, BorderLayout.CENTER);
    }
}//GEN-LAST:event_formComponentShown

    private void btnAddParentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddParentActionPerformed
        searchParentProperties();
    }//GEN-LAST:event_btnAddParentActionPerformed

    private void btnRemoveParentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveParentActionPerformed
        baUnitBean1.removeSelectedParentBaUnit();
    }//GEN-LAST:event_btnRemoveParentActionPerformed

    private void btnOpenParentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenParentActionPerformed
        openPropertyForm(baUnitBean1.getSelectedParentBaUnit());
    }//GEN-LAST:event_btnOpenParentActionPerformed

    private void btnOpenChildActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenChildActionPerformed
        openPropertyForm(baUnitBean1.getSelectedChildBaUnit());
    }//GEN-LAST:event_btnOpenChildActionPerformed

    private void menuOpenParentBaUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenParentBaUnitActionPerformed
        btnOpenParentActionPerformed(evt);
    }//GEN-LAST:event_menuOpenParentBaUnitActionPerformed

    private void menuAddParentBaUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddParentBaUnitActionPerformed
        btnAddParentActionPerformed(evt);
    }//GEN-LAST:event_menuAddParentBaUnitActionPerformed

    private void menuRemoveParentBaUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveParentBaUnitActionPerformed
        btnRemoveParentActionPerformed(evt);
    }//GEN-LAST:event_menuRemoveParentBaUnitActionPerformed

    private void menuOpenChildBaUnitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenChildBaUnitActionPerformed
        btnOpenChildActionPerformed(evt);
    }//GEN-LAST:event_menuOpenChildBaUnitActionPerformed

    private void btnViewRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewRightActionPerformed
        if (baUnitBean1.getSelectedRight() != null) {
            openRightForm(baUnitBean1.getSelectedRight(), RrrBean.RRR_ACTION.VIEW);
        }
    }//GEN-LAST:event_btnViewRightActionPerformed

    private void menuVaryRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuVaryRightActionPerformed
        varyRight();
    }//GEN-LAST:event_menuVaryRightActionPerformed

    private void btnChangeRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangeRightActionPerformed
        varyRight();
    }//GEN-LAST:event_btnChangeRightActionPerformed

    private void menuExtinguishRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExtinguishRightActionPerformed
        extinguishRight();
    }//GEN-LAST:event_menuExtinguishRightActionPerformed

    private void btnExtinguishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExtinguishActionPerformed
        extinguishRight();
    }//GEN-LAST:event_btnExtinguishActionPerformed

    private void menuEditRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditRightActionPerformed
        editRight();
    }//GEN-LAST:event_menuEditRightActionPerformed

    private void btnEditRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditRightActionPerformed
        editRight();
    }//GEN-LAST:event_btnEditRightActionPerformed

    private void menuRemoveRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveRightActionPerformed
        removeRight();
    }//GEN-LAST:event_menuRemoveRightActionPerformed

    private void btnRemoveRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveRightActionPerformed
        removeRight();
    }//GEN-LAST:event_btnRemoveRightActionPerformed

    private void btnCreateRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateRightActionPerformed
        createRight();
    }//GEN-LAST:event_btnCreateRightActionPerformed

    private void menuRemoveNotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveNotationActionPerformed
        removeNotation();
    }//GEN-LAST:event_menuRemoveNotationActionPerformed

    private void btnRemoveNotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveNotationActionPerformed
        removeNotation();
    }//GEN-LAST:event_btnRemoveNotationActionPerformed

    private void btnAddNotationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNotationActionPerformed
        addNotation();
    }//GEN-LAST:event_btnAddNotationActionPerformed

    private void mapPanelComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_mapPanelComponentShown
        this.mapControl.setCadastreObject(baUnitBean1.getCadastreObject());
    }//GEN-LAST:event_mapPanelComponentShown

    private void btnViewHistoricRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewHistoricRightActionPerformed
        if (baUnitBean1.getSelectedHistoricRight() != null) {
            openRightForm(baUnitBean1.getSelectedHistoricRight(), RrrBean.RRR_ACTION.VIEW);
        }
    }//GEN-LAST:event_btnViewHistoricRightActionPerformed

    private void tableRightsHistoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableRightsHistoryMouseClicked
        if (evt.getClickCount() > 1 && baUnitBean1.getSelectedHistoricRight() != null) {
            openRightForm(baUnitBean1.getSelectedHistoricRight(), RrrBean.RRR_ACTION.VIEW);
        }
    }//GEN-LAST:event_tableRightsHistoryMouseClicked

    private void btnSearchParcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchParcelActionPerformed
        openParcelSearch();
    }//GEN-LAST:event_btnSearchParcelActionPerformed

    private void btnAddParcelFromApplicationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddParcelFromApplicationActionPerformed
        openApplicationParcelsForm();
    }//GEN-LAST:event_btnAddParcelFromApplicationActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.beans.administrative.BaUnitBean baUnitBean1;
    private org.sola.clients.beans.referencedata.RrrTypeListBean baUnitRrrTypes;
    private javax.swing.JButton btnAddNotation;
    private javax.swing.JButton btnAddParcelFromApplication;
    private javax.swing.JButton btnAddParent;
    private javax.swing.JButton btnChangeRight;
    private javax.swing.JButton btnCreateRight;
    private javax.swing.JButton btnEditRight;
    private javax.swing.JButton btnExtinguish;
    private javax.swing.JButton btnOpenChild;
    private javax.swing.JButton btnOpenParent;
    private javax.swing.JButton btnRemoveNotation;
    private javax.swing.JButton btnRemoveParent;
    private javax.swing.JButton btnRemoveRight;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearchParcel;
    private javax.swing.JButton btnViewHistoricRight;
    private javax.swing.JButton btnViewRight;
    private org.sola.clients.swing.ui.cadastre.ParcelPanel cadastreObject;
    private javax.swing.JComboBox cbxRightType;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler4;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JToolBar jToolBar6;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JToolBar jToolBar8;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JPanel mapPanel;
    private javax.swing.JMenuItem menuAddParentBaUnit;
    private javax.swing.JMenuItem menuEditRight;
    private javax.swing.JMenuItem menuExtinguishRight;
    private javax.swing.JMenuItem menuOpenChildBaUnit;
    private javax.swing.JMenuItem menuOpenParentBaUnit;
    private javax.swing.JMenuItem menuRemoveNotation;
    private javax.swing.JMenuItem menuRemoveParentBaUnit;
    private javax.swing.JMenuItem menuRemoveRight;
    private javax.swing.JMenuItem menuVaryRight;
    private javax.swing.JMenuItem menuViewRight;
    private javax.swing.JPanel pnlPlot;
    private javax.swing.JPanel pnlPriorProperties;
    private javax.swing.JPopupMenu popupChildBaUnits;
    private javax.swing.JPopupMenu popupNotations;
    private javax.swing.JPopupMenu popupParentBaUnits;
    private javax.swing.JPopupMenu popupRights;
    private org.sola.clients.beans.referencedata.RrrTypeListBean rrrTypes;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableChildBaUnits;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableNotations;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableOwnership;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableParentBaUnits;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableRights;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableRightsHistory;
    private javax.swing.JTabbedPane tabsMain;
    private javax.swing.JTextField txtNotationText;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
