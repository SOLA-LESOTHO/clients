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
package org.sola.clients.beans.administrative;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractTransactionedBean;
import org.sola.clients.beans.administrative.validation.LeaseBeanCheck;
import org.sola.clients.beans.cadastre.CadastreObjectBean;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.messaging.ClientMessage;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.EntityAction;
import org.sola.webservices.transferobjects.administrative.LeaseTO;
import org.sola.webservices.transferobjects.cadastre.CadastreObjectTO;

/**
 * Represents lease object, used for the lease preparation service.
 */
@LeaseBeanCheck
public class LeaseBean extends AbstractTransactionedBean {
    public static final String LEASE_NUMBER_PROPERTY = "leaseNumber";
    public static final String MARITAL_STATUS_PROPERTY = "maritalStatus";
    public static final String LESSEE_ADDRESS_PROPERTY = "lesseeAddress";
    public static final String CADASTRE_OBJECT_PROPERTY = "cadastreObject";
    public static final String GROUND_RENT_PROPERTY = "groundRent";
    public static final String START_DATE_PROPERTY = "startDate";
    public static final String EXPIRATION_DATE_PROPERTY = "expirationDate";
    public static final String LESSEES_PROPERTY = "lessees";
    public static final String LEASE_SPECIAL_CONDITION_LIST_PROPERTY = "leaseSpecialConditionList";
    public static final String EXECUTION_DATE_PROPERTY = "executionDate";
    public static final String STAMP_DUTY_PROPERTY = "stampDuty";
    public static final String SELECTED_CONDITION_PROPERTY = "selectedCondition";
    
    @NotEmpty(message=ClientMessage.LEASE_LEASE_NUMBER_IS_IMPTY, payload=Localized.class)
    private String leaseNumber;
    private String maritalStatus;
    @NotEmpty(message=ClientMessage.LEASE_LESSEE_ADDRESS_IS_IMPTY, payload=Localized.class)
    private String lesseeAddress;
    @Valid
    @NotNull(message = ClientMessage.BAUNIT_SELECT_PARCEL, payload = Localized.class)
    private CadastreObjectBean cadastreObject;
    @NotNull(message = ClientMessage.LEASE_GROUND_RENT_IS_IMPTY, payload = Localized.class)
    private BigDecimal groundRent;
    @NotNull(message = ClientMessage.LEASE_START_DATE_IS_IMPTY, payload = Localized.class)
    private Date startDate;
    @NotNull(message = ClientMessage.LEASE_EXPIRATION_DATE_IS_IMPTY, payload = Localized.class)
    private Date expirationDate;
    private SolaList<PartySummaryBean> lessees;
    private SolaList<LeaseSpecialConditionBean> leaseSpecialConditionList;
    private Date executionDate;
    private BigDecimal stampDuty;
    transient private LeaseSpecialConditionBean selectedCondition;
    
    public LeaseBean(){
        super();
        lessees = new SolaList<PartySummaryBean>();
        leaseSpecialConditionList = new SolaList<LeaseSpecialConditionBean>();
    }

    public CadastreObjectBean getCadastreObject() {
        return cadastreObject;
    }

    public void setCadastreObject(CadastreObjectBean cadastreObject) {
        CadastreObjectBean oldValue = this.cadastreObject;
        this.cadastreObject = cadastreObject;
        propertySupport.firePropertyChange(CADASTRE_OBJECT_PROPERTY, oldValue, this.cadastreObject);
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        Date oldValue = this.executionDate;
        this.executionDate = executionDate;
        propertySupport.firePropertyChange(EXECUTION_DATE_PROPERTY, oldValue, this.executionDate);
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        Date oldValue = this.expirationDate;
        this.expirationDate = expirationDate;
        propertySupport.firePropertyChange(EXPIRATION_DATE_PROPERTY, oldValue, this.expirationDate);
    }

    public BigDecimal getGroundRent() {
        return groundRent;
    }

    public void setGroundRent(BigDecimal groundRent) {
        BigDecimal oldValue = this.groundRent;
        this.groundRent = groundRent;
        propertySupport.firePropertyChange(GROUND_RENT_PROPERTY, oldValue, this.groundRent);
    }

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String leaseNumber) {
        String oldValue = this.leaseNumber;
        this.leaseNumber = leaseNumber;
        propertySupport.firePropertyChange(LEASE_NUMBER_PROPERTY, oldValue, this.leaseNumber);
    }

    public ObservableList<LeaseSpecialConditionBean> getFilteredLeaseSpecialConditionList() {
        return leaseSpecialConditionList.getFilteredList();
    }
    
    public SolaList<LeaseSpecialConditionBean> getLeaseSpecialConditionList() {
        return leaseSpecialConditionList;
    }

    public void setLeaseSpecialConditionList(SolaList<LeaseSpecialConditionBean> leaseSpecialConditionList) {
        this.leaseSpecialConditionList = leaseSpecialConditionList;
    }

    public String getLesseeAddress() {
        return lesseeAddress;
    }

    public void setLesseeAddress(String lesseeAddress) {
        String oldValue = this.lesseeAddress;
        this.lesseeAddress = lesseeAddress;
        propertySupport.firePropertyChange(LESSEE_ADDRESS_PROPERTY, oldValue, this.lesseeAddress);
    }

    @Size(min=1, message=ClientMessage.LEASE_LESSEE_LIST_IS_IMPTY, payload=Localized.class)
    public ObservableList<PartySummaryBean> getFilteredLessees() {
        return lessees.getFilteredList();
    }
    
    public SolaList<PartySummaryBean> getLessees() {
        return lessees;
    }

    public void setLessees(SolaList<PartySummaryBean> lessees) {
        this.lessees = lessees;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        String oldValue = this.maritalStatus;
        this.maritalStatus = maritalStatus;
        propertySupport.firePropertyChange(MARITAL_STATUS_PROPERTY, oldValue, this.maritalStatus);
    }

    public BigDecimal getStampDuty() {
        return stampDuty;
    }

    public void setStampDuty(BigDecimal stampDuty) {
        BigDecimal oldValue = this.stampDuty;
        this.stampDuty = stampDuty;
        propertySupport.firePropertyChange(STAMP_DUTY_PROPERTY, oldValue, this.stampDuty);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        Date oldValue = this.startDate;
        this.startDate = startDate;
        propertySupport.firePropertyChange(START_DATE_PROPERTY, oldValue, this.startDate);
    }

    public LeaseSpecialConditionBean getSelectedCondition() {
        return selectedCondition;
    }

    public void setSelectedCondition(LeaseSpecialConditionBean selectedCondition) {
        this.selectedCondition = selectedCondition;
        propertySupport.firePropertyChange(SELECTED_CONDITION_PROPERTY, null, this.selectedCondition);
    }
    
    /** Adds new special condition to the list. */
    public void addLeaseSpecialCondition(String conditionText){
        LeaseSpecialConditionBean condition = new LeaseSpecialConditionBean();
        condition.setLeaseId(this.getId());
        condition.setConditionText(conditionText);
        getLeaseSpecialConditionList().addAsNew(condition);
    }
    
    /** Removes selected lease condition. */
    public void removeSelectedCondition(){
        if(selectedCondition!=null){
            getLeaseSpecialConditionList().safeRemove(selectedCondition, EntityAction.DELETE);
        }
    }
    
    /** Saves current bean. */
    public boolean save(String serviceId){
        LeaseTO lease = TypeConverters.BeanToTrasferObject(this, LeaseTO.class);
        TypeConverters.TransferObjectToBean(WSManager.getInstance()
                .getAdministrative().saveLease(lease, serviceId), LeaseBean.class, this);
        return true;
    }
    
    /** Returns lease by ID. */
    public static LeaseBean getLease(String id){
        return TypeConverters.TransferObjectToBean(WSManager.getInstance()
                .getAdministrative().getLease(id), LeaseBean.class, null);
    }
    
    /** Returns leases by service ID. */
    public static List<LeaseBean> getLeasesByServiceId(String serviceId){
        return TypeConverters.TransferObjectListToBeanList(WSManager.getInstance()
                .getAdministrative().getLeasesByServiceId(serviceId), LeaseBean.class, null);
    }
    
    /** Calculates ground rent fee for attached CadastreObject. */
    public void calculateGroundRent(){
        setGroundRent(LeaseBean.calculateGroundRent(getCadastreObject()));
    }
    
    /** Calculates ground rent fee for the given CadastreObject. */
    public static BigDecimal calculateGroundRent(CadastreObjectBean co){
        if(co==null){
            return BigDecimal.ZERO;
        }
        return WSManager.getInstance().getAdministrative().calculateGroundRent(
                TypeConverters.BeanToTrasferObject(co, CadastreObjectTO.class));
    }
}
