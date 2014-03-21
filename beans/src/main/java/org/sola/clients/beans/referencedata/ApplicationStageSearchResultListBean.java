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
import java.util.Iterator;
import java.util.List;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaCodeList;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.search.ApplicationStageSearchParamsTO;
import org.sola.webservices.transferobjects.search.ApplicationStageSearchResultTO;

/**
 * Holds the list of {@link ApplicationStageSearchResultBean} objects and used to 
 * populate comboboxes or listboxes controls.
 */
public class ApplicationStageSearchResultListBean extends AbstractBindingBean {
    
    public static final String SELECTED_APPSTAGE_PROPERTY = "selectedApplicationStage";
    private SolaObservableList<ApplicationStageSearchResultBean> applicationStageList;
    private ApplicationStageSearchResultBean selectedApplicationStage;
    
    /** Creates object instance. */
    public ApplicationStageSearchResultListBean(){
        super();
        applicationStageList = new SolaObservableList<ApplicationStageSearchResultBean>();
    }          

    public ApplicationStageSearchResultBean getSelectedApplicationStage() {
        return selectedApplicationStage;
    }

    public void setSelectedApplicationStage(ApplicationStageSearchResultBean selectedApplicationStage) {
        this.selectedApplicationStage = selectedApplicationStage;
        propertySupport.firePropertyChange(SELECTED_APPSTAGE_PROPERTY, null, this.selectedApplicationStage);
    }

    public ObservableList<ApplicationStageSearchResultBean> getApplicationStageList() {
        return applicationStageList;
    }
    
    
    /** 
     * Searches application stages by the given parameters and populates list. 
     * @param searchParam Search criteria.
     */
    public void getApplicationStages(ApplicationStageSearchParamsBean searchParam){
        applicationStageList.clear();
        if (WSManager.getInstance().getSearchService() != null && searchParam!=null) {
            List<ApplicationStageSearchResultTO> ApplicationStageListTO = WSManager.getInstance()
                    .getSearchService().getApplicationStages(TypeConverters
                    .BeanToTrasferObject(searchParam, ApplicationStageSearchParamsTO.class));
            TypeConverters.TransferObjectListToBeanList(ApplicationStageListTO, 
                    ApplicationStageSearchResultBean.class, (List) applicationStageList);
        }
    }
    
    public void setSelectedApplicationStageByCode(String appCode) {
        if (applicationStageList != null) {
            for (Iterator<ApplicationStageSearchResultBean> it = applicationStageList.iterator(); it.hasNext();) {
                ApplicationStageSearchResultBean appStage = it.next();
                if (appStage.getCode().equals(appCode)) {
                    selectedApplicationStage = appStage;
                    break;
                }
            }
        }
        propertySupport.firePropertyChange(SELECTED_APPSTAGE_PROPERTY, null, selectedApplicationStage);
    }
}
