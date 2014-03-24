/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sola.clients.beans.application;

import java.util.List;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.casemanagement.CustomerServicesViewTO;
import org.sola.webservices.transferobjects.casemanagement.LodgementViewParamsTO;

/**
 *
 * @author Charlizza
 */
public class CustomerServicesViewListBean extends AbstractBindingBean{
    
    public static final String SELECTED_CUSTOMER_SERVICES_PROPERTY = "selectedCustomerServices";
    private SolaObservableList<CustomerServicesViewBean> customerServicesList;
    private CustomerServicesViewBean selectedCustomerServices;

    public CustomerServicesViewListBean() {
        super();
        customerServicesList = new SolaObservableList<CustomerServicesViewBean>();
    }

    public SolaObservableList<CustomerServicesViewBean> getCustomerServices() {
        return customerServicesList;
    }

    public CustomerServicesViewBean getSelectedCustomerServices() {
        return selectedCustomerServices;
    }
    
    public void setSelectedCustomerServices(CustomerServicesViewBean selectedCustomerServices) {
        CustomerServicesViewBean oldValue = this.selectedCustomerServices;
        this.selectedCustomerServices = selectedCustomerServices;
         propertySupport.firePropertyChange(SELECTED_CUSTOMER_SERVICES_PROPERTY, oldValue, this.selectedCustomerServices);
    }

    /**
     * Returns collection of {@link ParcelNumberListingListBean} objects. This
     * method is used by Jasper report designer to extract properties of
     * application bean to help design a report.
     */
    public static java.util.Collection generateCollection() {
        java.util.Vector collection = new java.util.Vector();
        CustomerServicesViewListBean bean = new CustomerServicesViewListBean();
        collection.add(bean);
        return collection;
    }

    //      /** Passes from date and to date search criteria. */
    public void passParameter(LodgementViewParamsBean params) {
        
        LodgementViewParamsTO paramsTO = TypeConverters.BeanToTrasferObject(params,
                LodgementViewParamsTO.class);
        
        List<CustomerServicesViewTO>  customerServicesTO =
                WSManager.getInstance().getCaseManagementService().getCustomerServicesView(paramsTO);

        TypeConverters.TransferObjectListToBeanList(customerServicesTO,
                CustomerServicesViewBean.class, (List) customerServicesList);
    }
    
}
