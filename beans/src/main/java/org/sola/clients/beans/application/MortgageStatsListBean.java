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
package org.sola.clients.beans.application;

import java.util.List;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.casemanagement.LodgementViewParamsTO;
import org.sola.webservices.transferobjects.casemanagement.MortgageStatsViewTO;

/**
 *
 * @author Charlizza
 */
public class MortgageStatsListBean extends AbstractBindingBean{
    
    public static final String SELECTED_MORTGAGE_STATS_PROPERTY = "selectedMortgages";
    private SolaObservableList<MortgageStatsBean> mortgagestatsList;
    private MortgageStatsBean selectedMortgageStatss;

    public MortgageStatsListBean() {
        super();
        mortgagestatsList = new SolaObservableList<MortgageStatsBean>();
    }

    public SolaObservableList<MortgageStatsBean> getMortgageStatss() {
        return mortgagestatsList;
    }

    public MortgageStatsBean getSelectedMortgageStatss() {
        return selectedMortgageStatss;
    }
    
    public void setSelectedCertificates(MortgageStatsBean selectedMortgageStatss) {
        MortgageStatsBean oldValue = this.selectedMortgageStatss;
        this.selectedMortgageStatss = selectedMortgageStatss;
        propertySupport.firePropertyChange(SELECTED_MORTGAGE_STATS_PROPERTY, oldValue, this.selectedMortgageStatss);
    }

    /**
     * Returns collection of {@link ParcelNumberListingListBean} objects. This
     * method is used by Jasper report designer to extract properties of
     * application bean to help design a report.
     */
    public static java.util.Collection generateCollection() {
        java.util.Vector collection = new java.util.Vector();
        MortgageStatsListBean bean = new MortgageStatsListBean();
        collection.add(bean);
        return collection;
    }

    /** Passes from date and to date search criteria. */
    public void passParameter(LodgementViewParamsBean params) {
        
        LodgementViewParamsTO paramsTO = TypeConverters.BeanToTrasferObject(params,
                LodgementViewParamsTO.class);
        
        List<MortgageStatsViewTO> mortgagestatsTO =
                WSManager.getInstance().getCaseManagementService().getMortgageStatsView(paramsTO);

        TypeConverters.TransferObjectListToBeanList(mortgagestatsTO,
                MortgageStatsBean.class, (List) mortgagestatsList);
    }
    
}
