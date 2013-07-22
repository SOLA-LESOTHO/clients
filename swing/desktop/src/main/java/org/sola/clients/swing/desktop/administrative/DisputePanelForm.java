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
/**
 *
 * LAA Additions thoriso
 */
package org.sola.clients.swing.desktop.administrative;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.sola.clients.beans.administrative.DisputeBean;
import org.sola.clients.swing.ui.cadastre.ParcelDialog;
import org.sola.clients.beans.administrative.DisputePartyBean;
import org.sola.clients.beans.administrative.DisputesCommentsBean;
import org.sola.clients.swing.desktop.cadastre.SearchParcelDialog;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.referencedata.DisputeCategoryListBean;
import org.sola.clients.beans.referencedata.DisputeTypeListBean;
import org.sola.clients.beans.referencedata.DisputeActionListBean;
import org.sola.clients.beans.referencedata.OtherAuthoritiesListBean;
import org.sola.clients.beans.digitalarchive.DocumentBean;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.source.SourceBean;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.renderers.FormattersFactory;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.common.WindowUtility;
import java.awt.event.MouseEvent;
import java.util.List;
import org.sola.clients.beans.administrative.*;
import org.sola.clients.swing.common.LafManager;
import org.sola.clients.swing.desktop.party.PartyPanelForm;
import org.sola.clients.beans.cadastre.CadastreObjectBean;
import org.sola.clients.swing.ui.administrative.DisputeSearchPanel;
import javax.swing.JOptionPane;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.source.SourceListBean;
import org.sola.clients.swing.ui.source.AddDocumentForm;
import org.sola.clients.swing.ui.source.DocumentsPanel;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.observablecollections.ObservableListListener;
import org.sola.clients.swing.ui.party.PartySearchPanel;
import org.sola.clients.swing.desktop.party.DispPartyForm;

public class DisputePanelForm extends ContentPanel {

    public static final String SELECT_PARTY_PROPERTY = "selectParty";
    public static final String CREATE_NEW_PARTY_PROPERTY = "createNewParty";
    public static final String EDIT_PARTY_PROPERTY = "editParty";
    public static final String REMOVE_PARTY_PROPERTY = "removeParty";
    public static final String VIEW_PARTY_PROPERTY = "viewParty";
    public static final String VIEW_DOCUMENT = "viewDocument";
    public static final String EDIT_DOCUMENT = "editDocument";
    private ApplicationServiceBean applicationService;
    private DisputePartyBean disPartyBean;
    private CadastreObjectBean cadastreObjectBean;
    private DisputeSearchResultBean disputeSearchResultBean;
    public DocumentBean archiveDocument;
    private PartyBean partyBean;
    private String disputeID;
    private String partyRole;
    private String user;
    private boolean readOnly = false;
    static String complainantString = "Complainant";
    static String respondentString = "Respondent";
    private SourceBean document;
    private DisputeSearchPanel disputeSearchPanel;
    private DisputeSearchDialog disputeSearchDialog;
    private PartyPanelForm partyPanelForm;
    private AddDocumentForm addDocumentForm;
    private ApplicationBean applicationBean;
    private boolean allowAddingOfNewDocuments = true;
    private SolaList<SourceBean> sourceList;
    private DispPartyForm dispPartyForm;

    
    public DisputePanelForm() {
        initComponents();
        clearForm();

    }

    public DisputePanelForm(DisputeBean dispute) {
        this.disputeBean1 = dispute;
        initComponents();
    }

    private void createNewDispute() {
        disputeBean1.setId(null);
        disputeBean1.setLodgementDate(null);
        disputeBean1.setNr(null);
        disputeBean1.setStatusCode(null);
        disputeBean1.setPlotNumber(null);
        disputeBean1.setPlotLocation(null);
        disputeBean1.setLeaseNumber(null);
        disputeBean1.setDisputeCategory(null);
        disputeBean1.setDisputeType(null);
        disputesCommentsBean1.setUpdateDate(null);
        disputesCommentsBean1.setDisputeAction(null);
        disputesCommentsBean1.setOtherAuthorities(null);
        disputesCommentsBean1.setComments(null);
        disputesCommentsBean1.setDisputeNr(null);
        disputesCommentsBean1.setId(null);
        disputePartyBean1.setId(null);
        disputePartyBean1.setDisputeNr(null);
        disputePartyBean1.setPartyId(null);
        disputePartyBean1.setPartyRole(null);
        disputePartyBean1.setPartyName(null);
        saveDisputeState();
    }

    private void LoadCommentsList() {
        disputeBean1.getDisputeCommentsList().addObservableListListener(new ObservableListListener() {

            @Override
            public void listElementsAdded(ObservableList ol, int i, int i1) {
                //disputeBean1.updateCheckList(disputeBean1.getDisputeCommentsList());
            }

            @Override
            public void listElementsRemoved(ObservableList list, int index, List oldElements) {
                //disputeBean1.updateCheckList(disputeBean1.getDisputeCommentsList());
            }

            @Override
            public void listElementReplaced(ObservableList list, int index, Object oldElement) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void listElementPropertyChanged(ObservableList list, int index) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
    }

    
    private void RefreshScreen() {
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/administrative/Bundle");
        disputeID = disputeBean1.getNr();
        if (disputeID != null) {
            pnlHeader.setTitleText(bundle.getString("DisputePanelForm.pnlHeader.titleText") + " #" + disputeID);
            disputesCommentsBean1.setDisputeNr(disputeID);
            LoadCommentsList();
            disputeBean1.getDisputeCommentsList();
        }
    }

    private void NewComments() {
        //if (disputeID != null && !disputeID.equals("")) {
//            disputesCommentsBean1.setUpdateDate(null);
//            disputesCommentsBean1.setDisputeAction(null);
//            disputesCommentsBean1.setOtherAuthorities(null);
//            disputesCommentsBean1.setComments(null);
//            disputesCommentsBean1.setDisputeNr(disputeID);
//            disputesCommentsBean1.setId(null);
        disputesCommentsBean1 = new DisputesCommentsBean();
        txtDisputeComments.setText("");
        saveDisputeCommentsState();
        // }
    }

    private DisputeBean createDisputeBean() {
        if (disputeBean1 == null) {
            disputeBean1 = new DisputeBean();
        }
        return disputeBean1;
    }

    private void saveDisputeState() {
        MainForm.saveBeanState(disputeBean1);
    }

    private void saveDisputeCommentsState() {
        MainForm.saveBeanState(disputesCommentsBean1);
    }

    private void CustomizeDisputeScreen() {
        if (disputeBean1 != null && !disputeBean1.isNew()) {
            RefreshScreen();
        }
    }

    private void completeDispute() {
        if (disputeID != null && disputeBean1.getId() != null) {
            disputeBean1.setStatusCode("Resolved");
            disputeBean1.saveDispute();
        }


    }

    private void saveDispute(final boolean showMessage, final boolean closeOnSave) {

        if (disputeBean1.validate(true).size() > 0) {
            return;
        }

        SolaTask<Void, Void> t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));


                if (disputeID != null && !disputeID.equals("")) {

                    disputeBean1.saveDispute();
                } else {
                    disputeBean1.createDispute();
                }
                if (closeOnSave) {
                    close();
                }

                return null;
            }

            @Override
            public void taskDone() {
                if (showMessage) {
                    CustomizeDisputeScreen();
                    MessageUtility.displayMessage(ClientMessage.DISPUTE_SAVED);
                }
                saveDisputeState();

            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void AddPlot() {
        ParcelDialog form = new ParcelDialog(null, true, null, true);
        WindowUtility.centerForm(form);
        form.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ParcelDialog.SELECTED_PARCEL)) {
                    //cadastreObjectBean1.addAddress((AddressBean) evt.getNewValue());
                }
            }
        });
        form.setVisible(true);
    }

    private void SaveComments(final boolean showMessage) {

        if (disputesCommentsBean1.validate(true).size() > 0) {
            return;
        }

        SolaTask<Void, Void> t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_SAVING));

                if (disputeID != null && !disputeID.equals("")) {
                    disputesCommentsBean1.setDisputeNr(disputeID);
                    disputesCommentsBean1.setComments(txtDisputeComments.getText());
                    disputesCommentsBean1.saveDisputeComments();
                    disputeBean1.addDisputeComment(disputesCommentsBean1);
                }
                return null;
            }

            @Override
            public void taskDone() {
                if (showMessage) {
                    //CustomizeDisputeScreen();
                    MessageUtility.displayMessage(ClientMessage.DISPUTE_COMMENTS_SAVED);
                }
                disputesCommentsBean1 = new DisputesCommentsBean();
                saveDisputeCommentsState();
            }
        };
        TaskManager.getInstance().runTask(t);

    }

    private void removeComment() {
        disputeBean1.removeSelectedComment();
    }

    public SourceBean getDocument() {
        if (document == null) {
            document = new SourceBean();
        }
        return document;
    }

    public boolean validateDocument(boolean showMessage) {
        return getDocument().validate(showMessage).size() < 1;
    }


    private void SearchPlot() {
        SearchParcelDialog form = new SearchParcelDialog(null, true);
        WindowUtility.centerForm(form);
        form.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(SearchParcelDialog.SELECTED_PARCEL)) {

                    CadastreObjectBean cadastreObject = (CadastreObjectBean) evt.getNewValue();

                    disputeBean1.addChosenPlot(cadastreObject);
                }
            }
        });
        form.setVisible(true);
    }

    private void searchParty() {
        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_PERSON_SEARCHING));
                partySearchResult.search(partySearchParams);
                return null;
            }

            @Override
            public void taskDone() {
                lblSearchResultNumber.setText(Integer.toString(partySearchResult.getPartySearchResults().size()));
                if (partySearchResult.getPartySearchResults().size() > 100) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_TOO_MANY_RESULTS, new String[]{"100"});
                } else if (partySearchResult.getPartySearchResults().size() < 1) {
                    MessageUtility.displayMessage(ClientMessage.SEARCH_NO_RESULTS);
                }
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void firePartyEvent(String propertyName) {
        if (partySearchResult.getSelectedPartySearchResult() != null) {
            firePropertyChange(propertyName, null,
                    PartyBean.getParty(partySearchResult.getSelectedPartySearchResult().getId()));
            //initPartyComponents();
        }
    }

    private void selectParty() {
        if (partySearchResult.getSelectedPartySearchResult() != null && disputeID != null && partyRole != null) {
            disputePartyBean1.addChosenParty(PartyBean.getParty(partySearchResult.getSelectedPartySearchResult().getId()), disputeID, partyRole);
            disputePartyBean1.saveDisputeParty();
            disputeBean1.addDisputeParty(disputePartyBean1);
            MessageUtility.displayMessage(ClientMessage.DISPUTE_PARTY_SAVED);
            disputePartyBean1 = new DisputePartyBean();
        }
    }

    private void viewParty() {
        firePartyEvent(VIEW_PARTY_PROPERTY);
    }

    private void clearForm() {
        // cbxPartyTypes.setSelectedIndex(-1);
        //cbxRoles.setSelectedIndex(-1);
        // txtName.setText(null);
    }

    private class DisputePartyFormListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            if (evt.getNewValue() != null) {
                disputeSearchResultBean = (DisputeSearchResultBean) evt.getNewValue();

                disputeBean1.assignDispute(disputeSearchResultBean);
            }
        }
    }

    private void addParty() {

        if (dispPartyForm != null) {
            dispPartyForm.dispose();
        }

        dispPartyForm = new DispPartyForm(null, true);
        dispPartyForm.setLocationRelativeTo(this);
        DisputePartyFormListener listener = new DisputePartyFormListener();
        dispPartyForm.setVisible(true);
    }

    private void editParty() {
        firePartyEvent(EDIT_PARTY_PROPERTY);

        firePropertyChange(EDIT_PARTY_PROPERTY, null,
                PartyBean.getParty(partySearchResult.getSelectedPartySearchResult().getId()));
    }

    private void removeParty() {
        disputeBean1.removeSelectedParty();
    }

    private void switchPartyRole(boolean isComplainant) {
        partyRole = null;
        if (isComplainant) {
            partyRole = complainantString;
            btnrespondent.setSelected(false);

        } else {
            partyRole = respondentString;
            btncomplainant.setSelected(false);
        }
    }

    private void searchDispute() {

        if (disputeSearchDialog != null) {
            disputeSearchDialog.dispose();
        }

        disputeSearchDialog = new DisputeSearchDialog(null, null, true);
        disputeSearchDialog.setLocationRelativeTo(this);

        DisputeSearchFormListener listener = new DisputeSearchFormListener();
        disputeSearchDialog.addPropertyChangeListener(DisputeSearchResultListBean.SELECTED_DISPUTE_SEARCH_RESULT_PROPERTY, listener);
        disputeSearchDialog.setVisible(true);
        disputeSearchDialog.removePropertyChangeListener(DisputeSearchResultListBean.SELECTED_DISPUTE_SEARCH_RESULT_PROPERTY, listener);
        RefreshScreen();

    }

    private DisputeCategoryListBean createDisputeCategory() {
        if (disputeCategory == null) {
            String categoryCode = null;
            if (disputeBean1 != null && disputeBean1.getDisputeCategoryCode() != null) {
                categoryCode = disputeBean1.getDisputeCategoryCode();
            }
            disputeCategory = new DisputeCategoryListBean();
        }

        return disputeCategory;
    }

    private DisputeTypeListBean createDisputeType() {
        if (disputeType == null) {
            String typeCode = null;
            if (disputeBean1 != null && disputeBean1.getDisputeTypeCode() != null) {
                typeCode = disputeBean1.getDisputeTypeCode();
            }
            disputeType = new DisputeTypeListBean();
        }

        return disputeType;
    }

    private DisputeActionListBean createDisputeAction() {
        if (disputeAction == null) {
            String actionCode = null;
            if (disputesCommentsBean1 != null && disputesCommentsBean1.getDisputeActionCode() != null) {
                actionCode = disputesCommentsBean1.getDisputeActionCode();
            }
            disputeAction = new DisputeActionListBean();
        }

        return disputeAction;
    }

    private OtherAuthoritiesListBean createOtherAuthorities() {
        if (otherAuthorities == null) {
            String authoritiesCode = null;
            if (disputesCommentsBean1 != null && disputesCommentsBean1.getOtherAuthoritiesCode() != null) {
                authoritiesCode = disputesCommentsBean1.getOtherAuthoritiesCode();
            }
            otherAuthorities = new OtherAuthoritiesListBean();
        }

        return otherAuthorities;
    }

    private class DisputeSearchFormListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            if (evt.getNewValue() != null) {
                disputeSearchResultBean = (DisputeSearchResultBean) evt.getNewValue();

                disputeBean1.assignDispute(disputeSearchResultBean);
            }
        }
    }

    /**
     * Attach file to the selected source.
     */
    private void attachDocument(PropertyChangeEvent e) {
        SourceBean document = null;
        if (e.getPropertyName().equals(AddDocumentForm.SELECTED_SOURCE)
                && e.getNewValue() != null) {
            document = (SourceBean) e.getNewValue();
            documentsPanel.addDocument(document);
            document.setReferenceNr(disputeID);
            document.save();
        }
    }

    /**
     * Adds new source into the list.
     */
    private void addDocument(String RefNo) {
        if (addDocumentForm != null) {
            addDocumentForm.dispose();
        }

        PropertyChangeListener listener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {
                attachDocument(e);
            }
        };


        addDocumentForm = new AddDocumentForm(applicationBean, null, true);
        addDocumentForm.setLocationRelativeTo(this);
        addDocumentForm.addPropertyChangeListener(SourceListBean.SELECTED_SOURCE_PROPERTY, listener);
        addDocumentForm.allowAddingOfNewDocuments(allowAddingOfNewDocuments);
        addDocumentForm.setVisible(true);
        addDocumentForm.removePropertyChangeListener(SourceListBean.SELECTED_SOURCE_PROPERTY, listener);
    }

    private void viewDocument() {
        firePropertyChange(VIEW_DOCUMENT, null, documentsPanel.getSourceListBean().getSelectedSource());
    }

    private void editDocument() {
        firePropertyChange(EDIT_DOCUMENT, null, documentsPanel.getSourceListBean().getSelectedSource());
    }

    /**
     * Opens file attached to the selected source.
     */
    private void viewAttachment() {
        documentsPanel.viewAttachment();
    }

    /**
     * Removes selected document.
     */
    private void removeDocument() {
        documentsPanel.removeSelectedDocument();
    }

    /**
     * Creates new instance of {@link DocumentsPanel}.
     */
    private DocumentsPanel createDocumentsPanel() {
        DocumentsPanel panel;
        if (sourceList != null) {
            panel = new DocumentsPanel(sourceList);
        } else {
            panel = new DocumentsPanel();
        }

        panel.getSourceListBean().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(SourceListBean.SELECTED_SOURCE_PROPERTY)) {
                    //customizeButtons((SourceBean)evt.getNewValue());
                }
            }
        });

        return panel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        disputeBean1 = createDisputeBean();
        disputeAction = createDisputeAction();
        disputeCategory = createDisputeCategory();
        disputeStatus = new org.sola.clients.beans.referencedata.DisputeStatusListBean();
        disputeType = createDisputeType();
        otherAuthorities = createOtherAuthorities();
        cadastreObjectBean1 = new org.sola.clients.beans.cadastre.CadastreObjectBean();
        partyRoleTypes = new org.sola.clients.beans.referencedata.PartyRoleTypeListBean();
        idType = new org.sola.clients.beans.referencedata.IdTypeListBean();
        genderTypes = new org.sola.clients.beans.referencedata.GenderTypeListBean();
        communicationTypes = new org.sola.clients.beans.referencedata.CommunicationTypeListBean();
        disputesCommentsBean1 = new org.sola.clients.beans.administrative.DisputesCommentsBean();
        partySearchParams = new org.sola.clients.beans.party.PartySearchParamsBean();
        partySearchResult = new org.sola.clients.beans.party.PartySearchResultListBean();
        cadastreObjectSearch = new org.sola.clients.beans.cadastre.CadastreObjectSearchResultListBean();
        disputePartyBean1 = new org.sola.clients.beans.administrative.DisputePartyBean();
        jToolBar1 = new javax.swing.JToolBar();
        btnnewDispute = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnSaveDispute = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnsearchDispute = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        tabGeneralInfo = new javax.swing.JPanel();
        groupPanel1 = new org.sola.clients.swing.ui.GroupPanel();
        jToolBar3 = new javax.swing.JToolBar();
        btnnewDisputeComment = new javax.swing.JButton();
        btnAddComment1 = new javax.swing.JButton();
        btnRemoveDisputeComment = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtupdateDate = new javax.swing.JFormattedTextField();
        jPanel21 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        dbxdisputeAction1 = new javax.swing.JComboBox();
        jPanel22 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        dbxotherAuthorities1 = new javax.swing.JComboBox();
        jPanel26 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDisputeComments = new javax.swing.JEditorPane();
        jPanel38 = new javax.swing.JPanel();
        groupPanel2 = new org.sola.clients.swing.ui.GroupPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        lblPlotNumber = new javax.swing.JLabel();
        txtcadastreId = new javax.swing.JTextField();
        btnSearchPlot = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblPlotLocation = new javax.swing.JLabel();
        txtplotLocation = new javax.swing.JTextField();
        jPanel39 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        dbxdisputeCategory = new javax.swing.JComboBox();
        lblLeaseCategory1 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        dbxdisputeType = new javax.swing.JComboBox();
        jPanel42 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtrrrId = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        pnlSearch = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        txtName1 = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        btnClear = new javax.swing.JButton();
        btnSearch = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        btncomplainant = new javax.swing.JRadioButton();
        btnrespondent = new javax.swing.JRadioButton();
        jPanel10 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableSearchResults = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();
        jToolBar4 = new javax.swing.JToolBar();
        btnView = new javax.swing.JButton();
        btnSelect = new javax.swing.JButton();
        separator1 = new javax.swing.JToolBar.Separator();
        btnAddParty = new javax.swing.JButton();
        btnRemoveParty = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel9 = new javax.swing.JLabel();
        lblSearchResultNumber = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jToolBar2 = new javax.swing.JToolBar();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        toolbarSeparator = new javax.swing.JToolBar.Separator();
        btnView1 = new javax.swing.JButton();
        btnViewAttachmanet = new javax.swing.JButton();
        documentsPanel = createDocumentsPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtdisputeNumber = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        lblLodgementDate = new javax.swing.JLabel();
        txtlodgementDate = new javax.swing.JFormattedTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtstatus = new javax.swing.JTextField();
        pnlHeader = new org.sola.clients.swing.ui.HeaderPanel();

        setHeaderPanel(pnlHeader);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/desktop/administrative/Bundle"); // NOI18N
        setName(bundle.getString("DisputePanelForm.name")); // NOI18N

        jToolBar1.setBorder(null);
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName(bundle.getString("DisputePanelForm.jToolBar1.name")); // NOI18N

        btnnewDispute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/new.png"))); // NOI18N
        btnnewDispute.setText(bundle.getString("DisputePanelForm.btnnewDispute.text")); // NOI18N
        btnnewDispute.setFocusable(false);
        btnnewDispute.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnnewDispute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnewDisputeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnnewDispute);

        jSeparator1.setName(bundle.getString("DisputePanelForm.jSeparator1.name")); // NOI18N
        jToolBar1.add(jSeparator1);

        btnSaveDispute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/save.png"))); // NOI18N
        btnSaveDispute.setText(bundle.getString("DisputePanelForm.btnSaveDispute.text")); // NOI18N
        btnSaveDispute.setToolTipText(bundle.getString("DisputePanelForm.btnSaveDispute.toolTipText")); // NOI18N
        btnSaveDispute.setFocusable(false);
        btnSaveDispute.setName(bundle.getString("DisputePanelForm.btnSaveDispute.name")); // NOI18N
        btnSaveDispute.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSaveDispute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveDisputeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSaveDispute);

        jSeparator3.setName(bundle.getString("DisputePanelForm.jSeparator3.name")); // NOI18N
        jToolBar1.add(jSeparator3);

        btnsearchDispute.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnsearchDispute.setText(bundle.getString("DisputePanelForm.btnsearchDispute.text")); // NOI18N
        btnsearchDispute.setFocusable(false);
        btnsearchDispute.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnsearchDispute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsearchDisputeActionPerformed(evt);
            }
        });
        jToolBar1.add(btnsearchDispute);

        jSeparator4.setName(bundle.getString("DisputePanelForm.jSeparator4.name")); // NOI18N
        jToolBar1.add(jSeparator4);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/lock.png"))); // NOI18N
        jButton2.setText(bundle.getString("DisputePanelForm.jButton2.text")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jPanel1.setName(bundle.getString("DisputePanelForm.jPanel1.name")); // NOI18N

        jTabbedPane1.setName(bundle.getString("DisputePanelForm.jTabbedPane1.name")); // NOI18N

        tabGeneralInfo.setName(bundle.getString("DisputePanelForm.tabGeneralInfo.name")); // NOI18N

        groupPanel1.setTitleText(bundle.getString("DisputePanelForm.groupPanel1.titleText")); // NOI18N

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);
        jToolBar3.setName(bundle.getString("DisputePanelForm.jToolBar3.name")); // NOI18N

        btnnewDisputeComment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/new.png"))); // NOI18N
        btnnewDisputeComment.setText(bundle.getString("DisputePanelForm.btnnewDisputeComment.text")); // NOI18N
        btnnewDisputeComment.setFocusable(false);
        btnnewDisputeComment.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnnewDisputeComment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnewDisputeCommentActionPerformed(evt);
            }
        });
        jToolBar3.add(btnnewDisputeComment);

        btnAddComment1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAddComment1.setText(bundle.getString("DisputePanelForm.btnAddComment1.text")); // NOI18N
        btnAddComment1.setFocusable(false);
        btnAddComment1.setName(bundle.getString("DisputePanelForm.btnAddComment1.name")); // NOI18N
        btnAddComment1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddComment1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddComment1ActionPerformed(evt);
            }
        });
        jToolBar3.add(btnAddComment1);

        btnRemoveDisputeComment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        btnRemoveDisputeComment.setText(bundle.getString("DisputePanelForm.btnRemoveDisputeComment.text")); // NOI18N
        btnRemoveDisputeComment.setFocusable(false);
        btnRemoveDisputeComment.setName(bundle.getString("DisputePanelForm.btnRemoveDisputeComment.name")); // NOI18N
        btnRemoveDisputeComment.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveDisputeComment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveDisputeCommentActionPerformed(evt);
            }
        });
        jToolBar3.add(btnRemoveDisputeComment);

        jPanel6.setName(bundle.getString("DisputePanelForm.jPanel6.name")); // NOI18N
        jPanel6.setLayout(new java.awt.GridLayout(1, 3, 10, 5));

        jLabel4.setText(bundle.getString("DisputePanelForm.jLabel4.text")); // NOI18N

        txtupdateDate.setEditable(false);
        txtupdateDate.setText(bundle.getString("DisputePanelForm.txtupdateDate.text")); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputesCommentsBean1, org.jdesktop.beansbinding.ELProperty.create("${updateDate}"), txtupdateDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 203, Short.MAX_VALUE))
                    .addComponent(txtupdateDate))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtupdateDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel7);

        jLabel7.setText(bundle.getString("DisputePanelForm.jLabel7.text")); // NOI18N
        jLabel7.setName(bundle.getString("DisputePanelForm.jLabel7.name")); // NOI18N

        dbxdisputeAction1.setName(bundle.getString("DisputePanelForm.dbxdisputeAction1.name")); // NOI18N

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${disputeActionListBean}");
        org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeAction, eLProperty, dbxdisputeAction1);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputesCommentsBean1, org.jdesktop.beansbinding.ELProperty.create("${disputeAction}"), dbxdisputeAction1, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 157, Short.MAX_VALUE))
                    .addComponent(dbxdisputeAction1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dbxdisputeAction1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel21);

        jLabel6.setText(bundle.getString("DisputePanelForm.jLabel6.text")); // NOI18N
        jLabel6.setName(bundle.getString("DisputePanelForm.jLabel6.name")); // NOI18N

        dbxotherAuthorities1.setName(bundle.getString("DisputePanelForm.dbxotherAuthorities1.name")); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${otherAuthoritiesListBean}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, otherAuthorities, eLProperty, dbxotherAuthorities1);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputesCommentsBean1, org.jdesktop.beansbinding.ELProperty.create("${otherAuthorities}"), dbxotherAuthorities1, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                    .addComponent(dbxotherAuthorities1, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dbxotherAuthorities1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.add(jPanel22);

        jPanel26.setLayout(new java.awt.GridLayout(2, 1, 5, 10));

        jLabel8.setText(bundle.getString("DisputePanelForm.jLabel8.text")); // NOI18N
        jLabel8.setName(bundle.getString("DisputePanelForm.jLabel8.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputesCommentsBean1, org.jdesktop.beansbinding.ELProperty.create("${comments}"), txtDisputeComments, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jScrollPane3.setViewportView(txtDisputeComments);

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jScrollPane3)))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel26.add(jPanel37);

        groupPanel2.setTitleText(bundle.getString("DisputePanelForm.groupPanel2.titleText")); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredDisputeCommentsList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeBean1, eLProperty, jTable2);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${updateDate}"));
        columnBinding.setColumnName("Update Date");
        columnBinding.setColumnClass(java.util.Date.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${comments}"));
        columnBinding.setColumnName("Comments");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeBean1, org.jdesktop.beansbinding.ELProperty.create("${selectedComment}"), jTable2, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane4.setViewportView(jTable2);
        jTable2.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("DisputePanelForm.jTable2.columnModel.title0_1")); // NOI18N
        jTable2.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("DisputePanelForm.jTable2.columnModel.title1_1")); // NOI18N

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(groupPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 731, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(groupPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel26.add(jPanel38);

        jPanel8.setLayout(new java.awt.GridLayout(2, 2, 10, 5));

        lblPlotNumber.setText(bundle.getString("DisputePanelForm.lblPlotNumber.text")); // NOI18N
        lblPlotNumber.setName(bundle.getString("DisputePanelForm.lblPlotNumber.name")); // NOI18N

        txtcadastreId.setName(bundle.getString("DisputePanelForm.txtcadastreId.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeBean1, org.jdesktop.beansbinding.ELProperty.create("${plotNumber}"), txtcadastreId, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        txtcadastreId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcadastreIdActionPerformed(evt);
            }
        });

        btnSearchPlot.setText(bundle.getString("DisputePanelForm.btnSearchPlot.text")); // NOI18N
        btnSearchPlot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchPlotActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(lblPlotNumber)
                        .addGap(0, 302, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSearchPlot)))
                .addContainerGap())
            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel23Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(txtcadastreId, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                    .addGap(101, 101, 101)))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPlotNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSearchPlot)
                .addContainerGap(16, Short.MAX_VALUE))
            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel23Layout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addComponent(txtcadastreId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(18, Short.MAX_VALUE)))
        );

        jPanel8.add(jPanel23);

        lblPlotLocation.setText(bundle.getString("DisputePanelForm.lblPlotLocation.text")); // NOI18N
        lblPlotLocation.setName(bundle.getString("DisputePanelForm.lblPlotLocation.name")); // NOI18N

        txtplotLocation.setName(bundle.getString("DisputePanelForm.txtplotLocation.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeBean1, org.jdesktop.beansbinding.ELProperty.create("${plotLocation}"), txtplotLocation, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblPlotLocation)
                        .addGap(0, 299, Short.MAX_VALUE))
                    .addComponent(txtplotLocation))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lblPlotLocation)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtplotLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel3);

        jPanel39.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${disputeCategoryListBean}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeCategory, eLProperty, dbxdisputeCategory);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeBean1, org.jdesktop.beansbinding.ELProperty.create("${disputeCategory}"), dbxdisputeCategory, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        lblLeaseCategory1.setText(bundle.getString("DisputePanelForm.lblLeaseCategory1.text")); // NOI18N
        lblLeaseCategory1.setName(bundle.getString("DisputePanelForm.lblLeaseCategory1.name")); // NOI18N

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dbxdisputeCategory, 0, 167, Short.MAX_VALUE)
                    .addComponent(lblLeaseCategory1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel40Layout.createSequentialGroup()
                .addComponent(lblLeaseCategory1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dbxdisputeCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 35, Short.MAX_VALUE))
        );

        jPanel39.add(jPanel40);

        jLabel3.setText(bundle.getString("DisputePanelForm.jLabel3.text")); // NOI18N

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${disputeTypeListBean}");
        jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings.createJComboBoxBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeType, eLProperty, dbxdisputeType);
        bindingGroup.addBinding(jComboBoxBinding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeBean1, org.jdesktop.beansbinding.ELProperty.create("${disputeType}"), dbxdisputeType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dbxdisputeType, 0, 167, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel41Layout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dbxdisputeType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 35, Short.MAX_VALUE))
        );

        jPanel39.add(jPanel41);

        jPanel8.add(jPanel39);

        jLabel5.setText(bundle.getString("DisputePanelForm.jLabel5.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeBean1, org.jdesktop.beansbinding.ELProperty.create("${leaseNumber}"), txtrrrId, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel42Layout = new javax.swing.GroupLayout(jPanel42);
        jPanel42.setLayout(jPanel42Layout);
        jPanel42Layout.setHorizontalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
                    .addComponent(txtrrrId))
                .addContainerGap())
        );
        jPanel42Layout.setVerticalGroup(
            jPanel42Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel42Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtrrrId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 35, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel42);

        javax.swing.GroupLayout tabGeneralInfoLayout = new javax.swing.GroupLayout(tabGeneralInfo);
        tabGeneralInfo.setLayout(tabGeneralInfoLayout);
        tabGeneralInfoLayout.setHorizontalGroup(
            tabGeneralInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(groupPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(tabGeneralInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        tabGeneralInfoLayout.setVerticalGroup(
            tabGeneralInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabGeneralInfoLayout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(bundle.getString("DisputePanelForm.tabGeneralInfo.TabConstraints.tabTitle"), tabGeneralInfo); // NOI18N

        jPanel27.setMinimumSize(new java.awt.Dimension(300, 200));

        pnlSearch.setMinimumSize(new java.awt.Dimension(300, 300));

        jPanel14.setLayout(new java.awt.GridLayout(1, 3, 10, 5));

        jLabel11.setText(bundle.getString("DisputePanelForm.jLabel11.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, partySearchParams, org.jdesktop.beansbinding.ELProperty.create("${name}"), txtName1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addContainerGap(215, Short.MAX_VALUE))
            .addComponent(txtName1)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel17);

        btnClear.setText(bundle.getString("DisputePanelForm.btnClear.text")); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnSearch.setText(bundle.getString("DisputePanelForm.btnSearch.text")); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear)
                    .addComponent(btnSearch))
                .addContainerGap())
        );

        jPanel14.add(jPanel15);

        jLabel13.setText(bundle.getString("DisputePanelForm.jLabel13.text")); // NOI18N

        jLabel14.setText(bundle.getString("DisputePanelForm.jLabel14.text")); // NOI18N

        btncomplainant.setText(bundle.getString("DisputePanelForm.btncomplainant.text")); // NOI18N
        btncomplainant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncomplainantActionPerformed(evt);
            }
        });

        btnrespondent.setText(bundle.getString("DisputePanelForm.btnrespondent.text")); // NOI18N
        btnrespondent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnrespondentActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(btncomplainant, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnrespondent, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel16Layout.createSequentialGroup()
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncomplainant)
                    .addComponent(btnrespondent))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel14.add(jPanel16);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${partySearchResults}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, partySearchResult, eLProperty, tableSearchResults);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fullName}"));
        columnBinding.setColumnName("Full Name");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${typeCode}"));
        columnBinding.setColumnName("Type Code");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, partySearchResult, org.jdesktop.beansbinding.ELProperty.create("${selectedPartySearchResult}"), tableSearchResults, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        tableSearchResults.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSearchResultsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableSearchResults);
        tableSearchResults.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("DisputePanelForm.tableSearchResults.columnModel.title0")); // NOI18N
        tableSearchResults.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("DisputePanelForm.tableSearchResults.columnModel.title1")); // NOI18N

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        btnView.setText(bundle.getString("DisputePanelForm.btnView.text")); // NOI18N
        btnView.setFocusable(false);
        btnView.setHideActionText(true);
        btnView.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnView.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        jToolBar4.add(btnView);

        btnSelect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/select.png"))); // NOI18N
        btnSelect.setText(bundle.getString("DisputePanelForm.btnSelect.text")); // NOI18N
        btnSelect.setFocusable(false);
        btnSelect.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSelect.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectActionPerformed(evt);
            }
        });
        jToolBar4.add(btnSelect);
        jToolBar4.add(separator1);

        btnAddParty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAddParty.setText(bundle.getString("DisputePanelForm.btnAddParty.text")); // NOI18N
        btnAddParty.setFocusable(false);
        btnAddParty.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAddParty.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddParty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddPartyActionPerformed(evt);
            }
        });
        jToolBar4.add(btnAddParty);

        btnRemoveParty.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        btnRemoveParty.setText(bundle.getString("DisputePanelForm.btnRemoveParty.text")); // NOI18N
        btnRemoveParty.setFocusable(false);
        btnRemoveParty.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRemoveParty.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveParty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemovePartyActionPerformed(evt);
            }
        });
        jToolBar4.add(btnRemoveParty);
        jToolBar4.add(jSeparator2);

        jLabel9.setText(bundle.getString("DisputePanelForm.jLabel9.text")); // NOI18N
        jToolBar4.add(jLabel9);

        lblSearchResultNumber.setFont(LafManager.getInstance().getLabFontBold());
        lblSearchResultNumber.setText(bundle.getString("DisputePanelForm.lblSearchResultNumber.text")); // NOI18N
        jToolBar4.add(lblSearchResultNumber);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE))
                .addGap(2, 2, 2))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredDisputePartyList}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeBean1, eLProperty, jTable1, "");
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${partyId}"));
        columnBinding.setColumnName("Party Id");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${partyRole}"));
        columnBinding.setColumnName("Party Role");
        columnBinding.setColumnClass(String.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeBean1, org.jdesktop.beansbinding.ELProperty.create("${selectedParty}"), jTable1, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("DisputePanelForm.jTable1.columnModel.title0_1")); // NOI18N
        jTable1.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("DisputePanelForm.jTable1.columnModel.title1_1")); // NOI18N

        javax.swing.GroupLayout pnlSearchLayout = new javax.swing.GroupLayout(pnlSearch);
        pnlSearch.setLayout(pnlSearchLayout);
        pnlSearchLayout.setHorizontalGroup(
            pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        pnlSearchLayout.setVerticalGroup(
            pnlSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSearchLayout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(163, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(bundle.getString("DisputePanelForm.jPanel9.TabConstraints.tabTitle"), jPanel9); // NOI18N

        jPanel5.setName(bundle.getString("DisputePanelForm.jPanel5.name")); // NOI18N

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAdd.setText(bundle.getString("DisputePanelForm.btnAdd.text")); // NOI18N
        btnAdd.setFocusable(false);
        btnAdd.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jToolBar2.add(btnAdd);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        btnEdit.setText(bundle.getString("DisputePanelForm.btnEdit.text")); // NOI18N
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jToolBar2.add(btnEdit);

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        btnRemove.setText(bundle.getString("DisputePanelForm.btnRemove.text")); // NOI18N
        btnRemove.setFocusable(false);
        btnRemove.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        jToolBar2.add(btnRemove);
        jToolBar2.add(toolbarSeparator);

        btnView1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        btnView1.setText(bundle.getString("DisputePanelForm.btnView1.text")); // NOI18N
        btnView1.setFocusable(false);
        btnView1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnView1ActionPerformed(evt);
            }
        });
        jToolBar2.add(btnView1);

        btnViewAttachmanet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/folder-open-document.png"))); // NOI18N
        btnViewAttachmanet.setText(bundle.getString("DisputePanelForm.btnViewAttachmanet.text")); // NOI18N
        btnViewAttachmanet.setFocusable(false);
        btnViewAttachmanet.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnViewAttachmanet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewAttachmanetActionPerformed(evt);
            }
        });
        jToolBar2.add(btnViewAttachmanet);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
            .addComponent(documentsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(documentsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(373, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(bundle.getString("DisputePanelForm.jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        jPanel2.setName(bundle.getString("DisputePanelForm.jPanel2.name")); // NOI18N
        jPanel2.setLayout(new java.awt.GridLayout(1, 3, 10, 5));

        jLabel1.setText(bundle.getString("DisputePanelForm.jLabel1.text")); // NOI18N
        jLabel1.setName(bundle.getString("DisputePanelForm.jLabel1.name")); // NOI18N

        txtdisputeNumber.setEditable(false);
        txtdisputeNumber.setName(bundle.getString("DisputePanelForm.txtdisputeNumber.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeBean1, org.jdesktop.beansbinding.ELProperty.create("${nr}"), txtdisputeNumber, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 166, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(txtdisputeNumber)
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtdisputeNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.add(jPanel4);

        lblLodgementDate.setText(bundle.getString("DisputePanelForm.lblLodgementDate.text")); // NOI18N
        lblLodgementDate.setName(bundle.getString("DisputePanelForm.lblLodgementDate.name")); // NOI18N

        txtlodgementDate.setEditable(false);
        txtlodgementDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtlodgementDate.setName(bundle.getString("DisputePanelForm.txtlodgementDate.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeBean1, org.jdesktop.beansbinding.ELProperty.create("${lodgementDate}"), txtlodgementDate, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(lblLodgementDate)
                        .addGap(0, 163, Short.MAX_VALUE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(txtlodgementDate)
                        .addContainerGap())))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(lblLodgementDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtlodgementDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel19);

        jLabel2.setText(bundle.getString("DisputePanelForm.jLabel2.text")); // NOI18N
        jLabel2.setName(bundle.getString("DisputePanelForm.jLabel2.name")); // NOI18N

        txtstatus.setEditable(false);
        txtstatus.setName(bundle.getString("DisputePanelForm.txtstatus.name")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, disputeBean1, org.jdesktop.beansbinding.ELProperty.create("${statusCode}"), txtstatus, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 158, Short.MAX_VALUE))
                    .addComponent(txtstatus, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtstatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel20);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 609, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlHeader.setName(bundle.getString("DisputePanelForm.pnlHeader.name")); // NOI18N
        pnlHeader.setTitleText(bundle.getString("DisputePanelForm.pnlHeader.titleText")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(23, 23, 23))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveDisputeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveDisputeActionPerformed
        saveDispute(true, false);
    }//GEN-LAST:event_btnSaveDisputeActionPerformed

    private void txtcadastreIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcadastreIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtcadastreIdActionPerformed

    private void btnSearchPlotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchPlotActionPerformed
        SearchPlot();
    }//GEN-LAST:event_btnSearchPlotActionPerformed

    private void btnAddComment1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddComment1ActionPerformed
        SaveComments(true);
    }//GEN-LAST:event_btnAddComment1ActionPerformed

    private void btnnewDisputeCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnewDisputeCommentActionPerformed
        NewComments();
    }//GEN-LAST:event_btnnewDisputeCommentActionPerformed

    private void tableSearchResultsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableSearchResultsMouseClicked
        if (evt.getClickCount() > 1 && evt.getButton() == MouseEvent.BUTTON1) {
            viewParty();
        }
    }//GEN-LAST:event_tableSearchResultsMouseClicked

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        viewParty();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectActionPerformed
        selectParty();
    }//GEN-LAST:event_btnSelectActionPerformed

    private void btnAddPartyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddPartyActionPerformed
        addParty();
    }//GEN-LAST:event_btnAddPartyActionPerformed

    private void btnRemovePartyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemovePartyActionPerformed
        removeParty();
    }//GEN-LAST:event_btnRemovePartyActionPerformed

    private void btnsearchDisputeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsearchDisputeActionPerformed
        searchDispute();
    }//GEN-LAST:event_btnsearchDisputeActionPerformed

    private void btnRemoveDisputeCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveDisputeCommentActionPerformed
        removeComment();
    }//GEN-LAST:event_btnRemoveDisputeCommentActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (disputeID != null && !disputeID.equals("")) {
            addDocument(disputeID);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        editDocument();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        removeDocument();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnView1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnView1ActionPerformed
        viewDocument();
    }//GEN-LAST:event_btnView1ActionPerformed

    private void btnViewAttachmanetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewAttachmanetActionPerformed
        viewAttachment();
    }//GEN-LAST:event_btnViewAttachmanetActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchParty();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btncomplainantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncomplainantActionPerformed
        switchPartyRole(true);
    }//GEN-LAST:event_btncomplainantActionPerformed

    private void btnrespondentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnrespondentActionPerformed
        switchPartyRole(false);
    }//GEN-LAST:event_btnrespondentActionPerformed

    private void btnnewDisputeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnewDisputeActionPerformed
        createNewDispute();
    }//GEN-LAST:event_btnnewDisputeActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        completeDispute();
    }//GEN-LAST:event_jButton2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAddComment1;
    private javax.swing.JButton btnAddParty;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnRemoveDisputeComment;
    private javax.swing.JButton btnRemoveParty;
    private javax.swing.JButton btnSaveDispute;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSearchPlot;
    private javax.swing.JButton btnSelect;
    private javax.swing.JButton btnView;
    private javax.swing.JButton btnView1;
    private javax.swing.JButton btnViewAttachmanet;
    private javax.swing.JRadioButton btncomplainant;
    private javax.swing.JButton btnnewDispute;
    private javax.swing.JButton btnnewDisputeComment;
    private javax.swing.JRadioButton btnrespondent;
    private javax.swing.JButton btnsearchDispute;
    private org.sola.clients.beans.cadastre.CadastreObjectBean cadastreObjectBean1;
    private org.sola.clients.beans.cadastre.CadastreObjectSearchResultListBean cadastreObjectSearch;
    private org.sola.clients.beans.referencedata.CommunicationTypeListBean communicationTypes;
    private javax.swing.JComboBox dbxdisputeAction1;
    public javax.swing.JComboBox dbxdisputeCategory;
    public javax.swing.JComboBox dbxdisputeType;
    private javax.swing.JComboBox dbxotherAuthorities1;
    private org.sola.clients.beans.referencedata.DisputeActionListBean disputeAction;
    private org.sola.clients.beans.administrative.DisputeBean disputeBean1;
    private org.sola.clients.beans.referencedata.DisputeCategoryListBean disputeCategory;
    private org.sola.clients.beans.administrative.DisputePartyBean disputePartyBean1;
    private org.sola.clients.beans.referencedata.DisputeStatusListBean disputeStatus;
    private org.sola.clients.beans.referencedata.DisputeTypeListBean disputeType;
    private org.sola.clients.beans.administrative.DisputesCommentsBean disputesCommentsBean1;
    private org.sola.clients.swing.ui.source.DocumentsPanel documentsPanel;
    private org.sola.clients.beans.referencedata.GenderTypeListBean genderTypes;
    private org.sola.clients.swing.ui.GroupPanel groupPanel1;
    private org.sola.clients.swing.ui.GroupPanel groupPanel2;
    private org.sola.clients.beans.referencedata.IdTypeListBean idType;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JLabel lblLeaseCategory1;
    private javax.swing.JLabel lblLodgementDate;
    private javax.swing.JLabel lblPlotLocation;
    private javax.swing.JLabel lblPlotNumber;
    private javax.swing.JLabel lblSearchResultNumber;
    private org.sola.clients.beans.referencedata.OtherAuthoritiesListBean otherAuthorities;
    private org.sola.clients.beans.referencedata.PartyRoleTypeListBean partyRoleTypes;
    private org.sola.clients.beans.party.PartySearchParamsBean partySearchParams;
    private org.sola.clients.beans.party.PartySearchResultListBean partySearchResult;
    private org.sola.clients.swing.ui.HeaderPanel pnlHeader;
    private javax.swing.JPanel pnlSearch;
    private javax.swing.JToolBar.Separator separator1;
    private javax.swing.JPanel tabGeneralInfo;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tableSearchResults;
    private javax.swing.JToolBar.Separator toolbarSeparator;
    private javax.swing.JEditorPane txtDisputeComments;
    private javax.swing.JTextField txtName1;
    private javax.swing.JTextField txtcadastreId;
    private javax.swing.JTextField txtdisputeNumber;
    private javax.swing.JFormattedTextField txtlodgementDate;
    private javax.swing.JTextField txtplotLocation;
    private javax.swing.JTextField txtrrrId;
    private javax.swing.JTextField txtstatus;
    private javax.swing.JFormattedTextField txtupdateDate;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
