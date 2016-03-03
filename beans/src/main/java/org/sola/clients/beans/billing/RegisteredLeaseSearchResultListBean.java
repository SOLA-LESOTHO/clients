/*
 * Copyright 2016 Food and Agriculture Organization of the United Nations (FAO).
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
package org.sola.clients.beans.billing;

import java.util.List;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;

/**
 *
 * @author nmafereka
 */
public class RegisteredLeaseSearchResultListBean {

    public static final String SELECTED_LEASE_SEARCH_RESULT_PROPERTY = "selectedLeaseSearchResult";
    private SolaObservableList<RegisteredLeaseSearchResultBean> leaseSearchResults;
    private RegisteredLeaseSearchResultBean selectedLeaseSearchResult;

    public RegisteredLeaseSearchResultListBean() {
        super();
    }

    public ObservableList<RegisteredLeaseSearchResultBean> getLeaseSearchResults() {
        if (leaseSearchResults == null) {
            leaseSearchResults = new SolaObservableList<RegisteredLeaseSearchResultBean>();
        }
        return leaseSearchResults;
    }

    public RegisteredLeaseSearchResultBean getSelectedRegisteredLeaseSearchResult() {
        return selectedLeaseSearchResult;
    }

    public void setSelectedRegisteredLeaseSearchResult(RegisteredLeaseSearchResultBean selectedRegisteredLeaseSearchResult) {
        this.selectedLeaseSearchResult = selectedRegisteredLeaseSearchResult;
    }
    
    public void search(String leaseNumber){
        getLeaseSearchResults().clear();
//        TypeConverters.TransferObjectListToBeanList(
//                WSManager.getInstance().getBilling().getLease(leaseNumber), 
//                RegisteredLeaseSearchResultBean.class, (List)getLeaseSearchResults());
    }

}
