/*
 * Copyright 2014 Food and Agriculture Organization of the United Nations (FAO).
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
package org.sola.clients.beans.application;

import java.util.List;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.casemanagement.ApplicationStagesViewTO;
import org.sola.webservices.transferobjects.casemanagement.LodgementViewParamsTO;

/**
 *
 * @author Charlizza
 */
public class ApplicationStagesViewListBean extends AbstractBindingBean{
    
    public static final String SELECTED_APPLICATION_STAGES_PROPERTY = "selectedApplicationStages";
    private SolaObservableList<ApplicationStagesViewBean> applicationStagesList;
    private ApplicationStagesViewBean selectedApplicationStages;

    public ApplicationStagesViewListBean() {
        super();
        applicationStagesList = new SolaObservableList<ApplicationStagesViewBean>();
    }

    public SolaObservableList<ApplicationStagesViewBean> getApplicationStages() {
        return applicationStagesList;
    }

    public ApplicationStagesViewBean getSelectedApplicationStages() {
        return selectedApplicationStages;
    }
    
    public void setSelectedApplicationStages(ApplicationStagesViewBean selectedApplicationStages) {
        ApplicationStagesViewBean oldValue = this.selectedApplicationStages;
        this.selectedApplicationStages = selectedApplicationStages;
         propertySupport.firePropertyChange(SELECTED_APPLICATION_STAGES_PROPERTY, oldValue, this.selectedApplicationStages);
    }

    /**
     * Returns collection of {@link ParcelNumberListingListBean} objects. This
     * method is used by Jasper report designer to extract properties of
     * application bean to help design a report.
     */
    public static java.util.Collection generateCollection() {
        java.util.Vector collection = new java.util.Vector();
        ApplicationStagesViewListBean bean = new ApplicationStagesViewListBean();
        collection.add(bean);
        return collection;
    }

    //      /** Passes from date and to date search criteria. */
    public void passParameter(LodgementViewParamsBean params) {
        
        LodgementViewParamsTO paramsTO = TypeConverters.BeanToTrasferObject(params,
                LodgementViewParamsTO.class);
        
        List<ApplicationStagesViewTO>  applicationStagesTO =
                WSManager.getInstance().getCaseManagementService().getApplicationStagesView(paramsTO);

        TypeConverters.TransferObjectListToBeanList(applicationStagesTO,
                ApplicationStagesViewBean.class, (List) applicationStagesList);
    }
    
}
