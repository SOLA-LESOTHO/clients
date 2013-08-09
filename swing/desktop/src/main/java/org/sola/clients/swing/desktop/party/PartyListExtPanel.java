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
package org.sola.clients.swing.desktop.party;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.party.PartyListPanel;
import org.sola.clients.swing.ui.party.PartySearchPanel;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.webservices.transferobjects.EntityAction;

/**
 * Extends {@link PartyListPanel} to handle buttons events and open appropriate
 * forms.
 */
public class PartyListExtPanel extends PartyListPanel {

    public PartyListExtPanel() {
        //this(null);
    }

    public PartyListExtPanel(SolaList<PartyBean> partyList) {
        super(partyList);
        addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(PartyListPanel.SEARCH_PARTY)) {
                    openSearch();
                } else if (evt.getPropertyName().equals(PartyListPanel.VIEW_PARTY)) {
                    openView((PartyBean) evt.getNewValue());
                } else if (evt.getPropertyName().equals(PartyListPanel.EDIT_PARTY)) {
                    openCreateEdit((PartyBean) evt.getNewValue());
                } else if (evt.getPropertyName().equals(PartyListPanel.REMOVE_PARTY)) {
                } else if (evt.getPropertyName().equals(PartyListPanel.ADD_PARTY)) {
                    openCreateEdit(null);
                }
            }
        });
    }

    private void openSearch() {
        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_PERSONSEARCH));
                final PartySearchPanelForm form = new PartySearchPanelForm();
                form.getPartySearchPanel().setShowSelectButton(true);
                form.getPartySearchPanel().addPropertyChangeListener(new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals(PartySearchPanel.SELECT_PARTY_PROPERTY)) {
                            PartyBean selectedParty = (PartyBean) evt.getNewValue();
                            if (getPersonList().contains(selectedParty)) {
                                PartyBean existingParty = getPersonList().get(getPersonList().indexOf(selectedParty));
                                if (existingParty.getEntityAction() != null && existingParty.getEntityAction() == EntityAction.DISASSOCIATE) {
                                    existingParty.setEntityAction(null);
                                    getPersonList().filter();
                                }
                            } else {
                                getPersonList().addAsNew(selectedParty);
                            }
                            form.close();
                        }
                    }
                });
                openContentPanel(form, MainContentPanel.CARD_SEARCH_PERSONS);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void openContentPanel(ContentPanel form, String cardName) {
        MainForm.getInstance().getMainContentPanel().addPanel(form, cardName, true);
    }

    private void openView(PartyBean party) {
        openContentPanel(new PartyPanelForm(false, party, true, true), MainContentPanel.CARD_PERSON);
    }

    private void openCreateEdit(PartyBean party) {
        final boolean isNew = party == null;
        PartyPanelForm form = new PartyPanelForm(true, party, false, true);
        form.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(PartyPanelForm.PARTY_SAVED)) {
                    PartyBean savedParty = ((PartyPanelForm) evt.getSource()).getParty();
                    if (isNew) {
                        getPersonList().addAsNew(savedParty);
                    } else {
                        getPersonList().set(getPersonList().indexOf(savedParty), savedParty);
                    }
                }
            }
        });
        openContentPanel(form, MainContentPanel.CARD_PERSON);
    }
}
