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
package org.sola.clients.beans.administrative;

import java.util.List;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.application.LodgementViewParamsBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.casemanagement.LodgementViewParamsTO;
import org.sola.webservices.transferobjects.casemanagement.LeaseTransfersTO;

/**
 *
 * @author Charlizza
 */
public class LeaseTransfersListBean extends AbstractBindingBean{
    
    public static final String SELECTED_LEASE_TRANSFERS_PROPERTY = "selectedTransfers";
    private SolaObservableList<LeaseTransfersBean> leaseTransfersList;
    private LeaseTransfersBean selectedLeaseTransfers;

    public LeaseTransfersListBean() {
        super();
        leaseTransfersList = new SolaObservableList<LeaseTransfersBean>();
    }

    public SolaObservableList<LeaseTransfersBean> getLeaseTransfers() {
        return leaseTransfersList;
    }

    public LeaseTransfersBean getSelectedLeaseTransfers() {
        return selectedLeaseTransfers;
    }
    
    public void setSelectedCertificates(LeaseTransfersBean selectedLeaseTransfers) {
        LeaseTransfersBean oldValue = this.selectedLeaseTransfers;
        this.selectedLeaseTransfers = selectedLeaseTransfers;
        propertySupport.firePropertyChange(SELECTED_LEASE_TRANSFERS_PROPERTY, oldValue, this.selectedLeaseTransfers);
    }

    /**
     * Returns collection of {@link ParcelNumberListingListBean} objects. This
     * method is used by Jasper report designer to extract properties of
     * application bean to help design a report.
     */
    public static java.util.Collection generateCollection() {
        java.util.Vector collection = new java.util.Vector();
        LeaseTransfersListBean bean = new LeaseTransfersListBean();
        collection.add(bean);
        return collection;
    }

    /** Passes from date and to date search criteria. */
    public void passParameter(LodgementViewParamsBean params) {
        
        LodgementViewParamsTO paramsTO = TypeConverters.BeanToTrasferObject(params,
                LodgementViewParamsTO.class);
        
        List<LeaseTransfersTO> leaseTransfersTO =
                WSManager.getInstance().getCaseManagementService().getLeaseTransfers(paramsTO);

        TypeConverters.TransferObjectListToBeanList(leaseTransfersTO,
                LeaseTransfersBean.class, (List) leaseTransfersList);
    }
     
}
