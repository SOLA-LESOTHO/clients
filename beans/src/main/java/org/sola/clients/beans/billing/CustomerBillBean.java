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

import java.math.BigDecimal;
import java.util.Date;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.EntityAction;
import org.sola.webservices.transferobjects.billing.CustomerBillTO;

/**
 *
 * @author nmafereka
 */
public class CustomerBillBean extends AbstractBindingBean {

    private static final String BILL_NUMBER_PROPERTY = "billNumber";

    private static final String BILL_STATUS_PROPERTY = "billStatus";

    private static final String BILL_DATE_PROPERTY = "billDate";

    private static final String BILL_PERIOD_PROPERTY = "billPeriod";

    private static final String LEASE_NUMBER_PROPERTY = "leaseNumber";

    //private static final String BILL_ITEM_LIST_PROPERTY = "billItemList";

    private static final String SELECTED_ITEM_PROPERTY = "selectedItem";

    private String billNumber;

    private String billStatus;

    private Date billDate;

    private String billPeriod;

    private String leaseNumber;

    private SolaList<PaymentScheduleBean> billItems;

    private transient PaymentScheduleBean selectedItem;

    public CustomerBillBean() {
        super();
        billItems = new SolaList<PaymentScheduleBean>();
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        String oldValue = this.billNumber;
        this.billNumber = billNumber;
        propertySupport.firePropertyChange(BILL_NUMBER_PROPERTY, oldValue, this.billNumber);
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        String oldValue = this.billStatus;
        this.billStatus = billStatus;
        propertySupport.firePropertyChange(BILL_STATUS_PROPERTY, oldValue, this.billStatus);
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        Date oldValue = this.billDate;
        this.billDate = billDate;
        propertySupport.firePropertyChange(BILL_DATE_PROPERTY, oldValue, this.billDate);
    }

    public String getBillPeriod() {
        return billPeriod;
    }

    public void setBillPeriod(String billPeriod) {
        String oldValue = this.billPeriod;
        this.billPeriod = billPeriod;
        propertySupport.firePropertyChange(BILL_PERIOD_PROPERTY, oldValue, this.billPeriod);
    }

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String leaseNumber) {
        String oldValue = this.leaseNumber;
        this.leaseNumber = leaseNumber;
        propertySupport.firePropertyChange(LEASE_NUMBER_PROPERTY, oldValue, this.leaseNumber);
    }

    public SolaList<PaymentScheduleBean> getBillItems() {
        return billItems;
    }

    public ObservableList<PaymentScheduleBean> getFilteredItems() {
        return billItems.getFilteredList();
    }

    public PaymentScheduleBean getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(PaymentScheduleBean selectedItem) {
        PaymentScheduleBean oldValue = this.selectedItem;
        this.selectedItem = selectedItem;
        propertySupport.firePropertyChange(SELECTED_ITEM_PROPERTY, oldValue, this.selectedItem);
    }

    /**
     * Adds new Bill Item person into the list.
     *
     * @param billItem
     */
    public void addItem(PaymentScheduleBean billItem) {
        if (billItem != null) {
            billItems.addAsNew(billItem);
        }
    }

    /**
     * Removes selected bill item person from the list.
     */
    public void removeSelectedItem() {
        if (selectedItem != null) {
            billItems.safeRemove(selectedItem, EntityAction.DELETE);
        }
    }
    
    /** Saves bill object. */
    public void save(){
        CustomerBillTO billTO = TypeConverters.BeanToTrasferObject(this, CustomerBillTO.class);
        billTO = WSManager.getInstance().getBilling().saveCustomerBill(billTO);
        TypeConverters.TransferObjectToBean(billTO, CustomerBillBean.class, this);
    }
   
    public static BigDecimal calculateGroundRent(BigDecimal leasedArea, BigDecimal groundRentRate,
                                            String calculationMethod, int complianceCode) {
        return 
                WSManager.getInstance().getBilling().calculateGroundRent(
                        leasedArea, groundRentRate, 
                                    calculationMethod, complianceCode);
    }
    
    public static boolean isPeriodBilliable(int paymentPeriod, int lastPaymentYear){
	return WSManager.getInstance().getBilling().isPeriodBilliable(paymentPeriod, 
                                                                        lastPaymentYear);
	}

}
