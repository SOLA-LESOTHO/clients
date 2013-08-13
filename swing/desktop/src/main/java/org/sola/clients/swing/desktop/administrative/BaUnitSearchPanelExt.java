/**
 * ******************************************************************************************
 * Copyright (c) 2013 Food and Agriculture Organization of the United Nations
 * (FAO) and the Lesotho Land Administration Authority (LAA). All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the names of FAO, the LAA nor the names of
 * its contributors may be used to endorse or promote products derived from this
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.sola.clients.beans.administrative.BaUnitBean;
import org.sola.clients.beans.administrative.BaUnitSearchResultBean;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.desktop.DashBoardPanel;
import org.sola.clients.swing.desktop.MainForm;
import org.sola.clients.swing.desktop.application.ApplicationPanel;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.MainContentPanel;
import org.sola.clients.swing.ui.administrative.BaUnitSearchPanel;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Extends {@link BaUnitSearchPanel} to handle open method
 */
public class BaUnitSearchPanelExt extends BaUnitSearchPanel {

    public BaUnitSearchPanelExt() {
        super();
        addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(BaUnitSearchPanel.BAUNIT_OPEN)) {
                    BaUnitSearchResultBean searchResult = (BaUnitSearchResultBean) evt.getNewValue();
                    if (searchResult != null) {
                        openPropertyForm(searchResult.getId());
                    }
                } else if (evt.getPropertyName().equals(BaUnitSearchPanel.BAUNIT_CORRECTION)) {
                    makeCorrectionApplication((BaUnitSearchResultBean) evt.getNewValue());
                }
            }
        });
    }

    private void makeCorrectionApplication(final BaUnitSearchResultBean searchResult) {
        SolaTask t = new SolaTask<Void, Void>() {
            ApplicationBean newApp;
            
            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_APP_CREATE_NEW));
                newApp = ApplicationBean.createCorrectionApp(searchResult);
                
                MainForm.getInstance().openDashBoard(false);
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_DASHBOARD_REFRESH));
                ((DashBoardPanel)MainForm.getInstance().getMainContentPanel()
                        .getPanel(MainContentPanel.CARD_DASHBOARD)).refreshAssignedApplicationsList();
                
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_APP));
                ApplicationPanel form = new ApplicationPanel(newApp);
                MainForm.getInstance().getMainContentPanel().addPanel(form, MainContentPanel.CARD_APPLICATION, true);
                form.selectServicesTab();
                return null;
            }

            @Override
            protected void taskFailed(Throwable e) {
                MessageUtility.displayMessage(ClientMessage.APPLICATION_FAILED_TO_CREATE_NEW);
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    private void openPropertyForm(final String baUnitId) {
        SolaTask t = new SolaTask<Void, Void>() {

            @Override
            public Void doTask() {
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_OPEN_PROPERTY));
                setMessage(MessageUtility.getLocalizedMessageText(ClientMessage.PROGRESS_MSG_BA_UNIT_GETTING));
                BaUnitBean baUnitBean = BaUnitBean.getBaUnitsById(baUnitId);
                PropertyPanel propertyPanel = new PropertyPanel(null, null, baUnitBean, true);
                MainForm.getInstance().getMainContentPanel().addPanel(propertyPanel, MainContentPanel.CARD_PROPERTY_PANEL, true);
                return null;
            }
        };
        TaskManager.getInstance().runTask(t);
    }
}
