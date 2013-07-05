package org.sola.clients.swing.ui.party;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.security.SecurityBean;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.common.RolesConstants;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.webservices.transferobjects.EntityAction;

/**
 * Shows list of persons and allows to call view, search, add, remove actions.
 */
public class PartyListPanel extends javax.swing.JPanel {

    public static final String SEARCH_PARTY = "searchParty";
    public static final String VIEW_PARTY = "viewParty";
    public static final String EDIT_PARTY = "editParty";
    public static final String REMOVE_PARTY = "removeParty";
    public static final String ADD_PARTY = "addParty";
    public static final String SELECTED_PERSON_PROPERTY = "selectedPerson";
    
    private SolaList<PartyBean> personList;
    private PartyBean selectedPerson;
    private boolean readOnly = false;
    
    /**
     * Default constructor.
     */
    public PartyListPanel() {
        this(null);
    }

    /**
     * Panel constructor with list of persons argument.
     */
    public PartyListPanel(SolaList<PartyBean> personList) {
        if(personList==null){
            this.personList = new SolaList<PartyBean>();
        } else {
            this.personList = personList;
        }
        initComponents();
        postInit();
    }
    
    private void setupPersonList(SolaList<PartyBean> personList){
        if(personList == null){
            this.personList = new SolaList<PartyBean>();
        } else {
            this.personList = personList;
        }
        firePropertyChange("personList", null, this.personList);
        firePropertyChange("filteredPersonList", null, this.personList.getFilteredList());
    }
    
    private void postInit(){
        addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals(SELECTED_PERSON_PROPERTY)){
                    customizeButtons();
                }
            }
        });
        customizeButtons();
    }
    
    private void customizeButtons(){
        boolean enabled = selectedPerson!=null;
        boolean hasPartySaveRole = SecurityBean.isInRole(RolesConstants.PARTY_SAVE);
        btnEdit.setEnabled(enabled && !readOnly && hasPartySaveRole);
        btnView.setEnabled(enabled);
        btnRemove.setEnabled(enabled && !readOnly);
        btnSearch.setEnabled(!readOnly);
        btnAdd.setEnabled(!readOnly && hasPartySaveRole);
        
        menuEdit.setEnabled(btnSearch.isEnabled());
        menuView.setEnabled(btnView.isEnabled());
        menuRemove.setEnabled(btnRemove.isEnabled());
        menuSearch.setEnabled(btnSearch.isEnabled());
        menuAdd.setEnabled(btnAdd.isEnabled());
    }
    
    public SolaList<PartyBean> getPersonList() {
        return personList;
    }

    public ObservableList<PartyBean> getFilteredPersonList() {
        return personList.getFilteredList();
    }
    
    public void setPersonList(SolaList<PartyBean> personList) {
        setupPersonList(personList);
    }

    public PartyBean getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(PartyBean selectedPerson) {
        PartyBean oldValue = this.selectedPerson;
        this.selectedPerson = selectedPerson;
        firePropertyChange(SELECTED_PERSON_PROPERTY, oldValue, this.selectedPerson);
    }

    public boolean isShowSearchButton() {
        return btnSearch.isVisible();
    }

    public void setShowSearchButton(boolean show) {
        btnSearch.setVisible(show);
    }

    public boolean isShowViewButton() {
        return btnView.isVisible();
    }

    public void setShowViewButton(boolean show) {
        btnView.setVisible(show);
    }

    public boolean isShowEditButton() {
        return btnEdit.isVisible();
    }

    public void setShowEditButton(boolean show) {
        btnEdit.setVisible(show);
    }

    public boolean isShowRemoveButton() {
        return btnRemove.isVisible();
    }

    public void setShowRemoveButton(boolean show) {
        btnRemove.setVisible(show);
    }

    public boolean isShowAddButton() {
        return btnAdd.isVisible();
    }

    public void setShowAddButton(boolean show) {
        btnAdd.setVisible(show);
    }
    
    public boolean isReadOnly(){
        return readOnly;
    }
    
    public void setReadOnly(boolean readOnly){
        this.readOnly = readOnly;
        customizeButtons();
    }
    
    private void fireGetParty(final String evtName) {
        if(selectedPerson==null){
            return;
        }
        firePropertyChange(evtName, null, selectedPerson);
    }
    
    private void searchParty(){
        firePropertyChange(SEARCH_PARTY, false, true);
    }
    
    private void addParty(){
        firePropertyChange(ADD_PARTY, false, true);
    }
    
    private void removeParty(){
        if(selectedPerson!=null){
            PartySummaryBean partyTmp = selectedPerson;
            getPersonList().safeRemove(selectedPerson, EntityAction.DISASSOCIATE);
            firePropertyChange(REMOVE_PARTY, null, partyTmp);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        popUpPersons = new javax.swing.JPopupMenu();
        menuSearch = new javax.swing.JMenuItem();
        menuAdd = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenuItem();
        menuView = new javax.swing.JMenuItem();
        menuRemove = new javax.swing.JMenuItem();
        jToolBar1 = new javax.swing.JToolBar();
        btnSearch = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnView = new javax.swing.JButton();
        btnRemove = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablePersons = new org.sola.clients.swing.common.controls.JTableWithDefaultStyles();

        menuSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/ui/party/Bundle"); // NOI18N
        menuSearch.setText(bundle.getString("PartyListPanel.menuSearch.text")); // NOI18N
        menuSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSearchActionPerformed(evt);
            }
        });
        popUpPersons.add(menuSearch);

        menuAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        menuAdd.setText(bundle.getString("PartyListPanel.menuAdd.text")); // NOI18N
        menuAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddActionPerformed(evt);
            }
        });
        popUpPersons.add(menuAdd);

        menuEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        menuEdit.setText(bundle.getString("PartyListPanel.menuEdit.text")); // NOI18N
        menuEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuEditActionPerformed(evt);
            }
        });
        popUpPersons.add(menuEdit);

        menuView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        menuView.setText(bundle.getString("PartyListPanel.menuView.text")); // NOI18N
        menuView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuViewActionPerformed(evt);
            }
        });
        popUpPersons.add(menuView);

        menuRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        menuRemove.setText(bundle.getString("PartyListPanel.menuRemove.text")); // NOI18N
        menuRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveActionPerformed(evt);
            }
        });
        popUpPersons.add(menuRemove);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/search.png"))); // NOI18N
        btnSearch.setText(bundle.getString("PartyListPanel.btnSearch.text")); // NOI18N
        btnSearch.setFocusable(false);
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSearch);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/add.png"))); // NOI18N
        btnAdd.setText(bundle.getString("PartyListPanel.btnAdd.text")); // NOI18N
        btnAdd.setFocusable(false);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAdd);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/pencil.png"))); // NOI18N
        btnEdit.setText(bundle.getString("PartyListPanel.btnEdit.text")); // NOI18N
        btnEdit.setFocusable(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        jToolBar1.add(btnEdit);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/view.png"))); // NOI18N
        btnView.setText(bundle.getString("PartyListPanel.btnView.text")); // NOI18N
        btnView.setFocusable(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        jToolBar1.add(btnView);

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/remove.png"))); // NOI18N
        btnRemove.setText(bundle.getString("PartyListPanel.btnRemove.text")); // NOI18N
        btnRemove.setFocusable(false);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRemove);

        tablePersons.setComponentPopupMenu(popUpPersons);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${filteredPersonList}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, tablePersons);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${fullName}"));
        columnBinding.setColumnName("Full Name");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${selectedPerson}"), tablePersons, org.jdesktop.beansbinding.BeanProperty.create("selectedElement"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(tablePersons);
        tablePersons.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("PartyListPanel.tablePersons.columnModel.title0_1")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchParty();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        addParty();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        fireGetParty(EDIT_PARTY);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        fireGetParty(VIEW_PARTY);
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        removeParty();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void menuSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSearchActionPerformed
        searchParty();
    }//GEN-LAST:event_menuSearchActionPerformed

    private void menuAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddActionPerformed
        addParty();
    }//GEN-LAST:event_menuAddActionPerformed

    private void menuEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuEditActionPerformed
        fireGetParty(EDIT_PARTY);
    }//GEN-LAST:event_menuEditActionPerformed

    private void menuViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuViewActionPerformed
        fireGetParty(VIEW_PARTY);
    }//GEN-LAST:event_menuViewActionPerformed

    private void menuRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveActionPerformed
        removeParty();
    }//GEN-LAST:event_menuRemoveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnView;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem menuAdd;
    private javax.swing.JMenuItem menuEdit;
    private javax.swing.JMenuItem menuRemove;
    private javax.swing.JMenuItem menuSearch;
    private javax.swing.JMenuItem menuView;
    private javax.swing.JPopupMenu popUpPersons;
    private org.sola.clients.swing.common.controls.JTableWithDefaultStyles tablePersons;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
