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
package org.sola.clients.beans.referencedata;

import org.sola.clients.beans.security.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.webservices.transferobjects.EntityAction;

/**
 * Holds list of {@link ApplicationStageGroupHelperBean} objects for binding on the form.
 */
public class ApplicationStageGroupHelperListBean extends AbstractBindingBean {
    
    private class ApplicationStageGroupHelperListener implements PropertyChangeListener, Serializable{
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(evt.getPropertyName().equals(ApplicationStageGroupHelperBean.IS_IN_APPLICATION_STAGE_GROUPS_PROPERTY) && 
                    evt.getNewValue() != evt.getOldValue() && triggerApplicationStageGroupUpdates){
                
                if(appStageGroups !=null){
                    ApplicationStageGroupHelperBean appStageGroupHelper = (ApplicationStageGroupHelperBean)evt.getSource();
                    boolean isInList = false;
                    
                    for(ApplicationStageGroupBean appStageGroup : appStageGroups){
                        if(appStageGroup.getGroupId().equals(appStageGroupHelper.getGroupSummary().getId())){
                            isInList = true;
                            if(!appStageGroupHelper.isInApplicationStageGroups()){
                                appStageGroups.safeRemove(appStageGroup, EntityAction.DELETE);
                            }
                            break;
                        }
                    }
                    
                    if(!isInList && appStageGroupHelper.isInApplicationStageGroups()){
                        ApplicationStageGroupBean appStageGroup = new ApplicationStageGroupBean(appStageGroupHelper.getGroupSummary().getId());
                        appStageGroups.add(appStageGroup);
                    }
                }
            }
        }
    }
    
    private SolaObservableList<ApplicationStageGroupHelperBean> appStageGroupHelpers;
    private SolaList<ApplicationStageGroupBean> appStageGroups;
    private boolean triggerApplicationStageGroupUpdates = true;
    
    public ApplicationStageGroupHelperListBean() {
        super();
        appStageGroupHelpers = new SolaObservableList<ApplicationStageGroupHelperBean>();
        ApplicationStageGroupHelperListener listener = new  ApplicationStageGroupHelperListener();
        
        GroupSummaryListBean groupsSummaryList = new GroupSummaryListBean();
        groupsSummaryList.loadGroups(false);
        
        for(GroupSummaryBean groupSummary : groupsSummaryList.getGroupSummaryList()){
            ApplicationStageGroupHelperBean appStageGroupHelper = new ApplicationStageGroupHelperBean(false, groupSummary);
            appStageGroupHelper.addPropertyChangeListener(listener);
            appStageGroupHelpers.add(appStageGroupHelper);
        }
    }

    public ObservableList<ApplicationStageGroupHelperBean> getApplicationStageGroupHelpers() {
        return appStageGroupHelpers;
    }

    /** Returns list of {@link ApplicationStageGroupBean}, if it is set. */
    public SolaList<ApplicationStageGroupBean> getApplicationStageGroups() {
        return appStageGroups;
    }

    /** 
     * Sets list of {@link ApplicationStageGroupBean}, which is managed, based on checks of 
     * the groups in the list of {@link ApplicationStageGroupHelperBean}. 
     */
    public void setApplicationStageGroups(SolaList<ApplicationStageGroupBean> appStageGroups) {
        this.appStageGroups = appStageGroups;
        setChecks(appStageGroups);
    }
    
    /** Sets or removes checks from the groups, based on provided {@link ApplicationStageGroupBean} object. */
    public void setChecks(SolaList<ApplicationStageGroupBean> appStageGroups) {
        triggerApplicationStageGroupUpdates=false;
        for (ApplicationStageGroupHelperBean appStageGroupHelper : appStageGroupHelpers) {

            appStageGroupHelper.setInApplicationStageGroups(false);

            if (appStageGroups != null) {
                for (ApplicationStageGroupBean appStageGroup : appStageGroups) {
                    if (appStageGroupHelper.getGroupSummary().getId()
                            .equals(appStageGroup.getGroupId())) {
                        appStageGroupHelper.setInApplicationStageGroups(true);
                    }
                }
            }
        }
        triggerApplicationStageGroupUpdates=true;
    }
}
