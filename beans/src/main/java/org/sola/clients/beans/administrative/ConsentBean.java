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
package org.sola.clients.beans.administrative;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang.time.DateFormatUtils;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractTransactionedBean;
import org.sola.clients.beans.administrative.validation.MortgageValidationGroup;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.referencedata.TransactionTypeBean;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.NumberToWords;
import org.sola.common.messaging.ClientMessage;
import org.sola.webservices.transferobjects.administrative.ConsentTO;
import org.sola.services.boundary.wsclients.WSManager;

public final class ConsentBean extends AbstractTransactionedBean {

    public static final String REGISTRATION_DATE_PROPERTY = "registrationDate";
    public static final String LEASE_NUMBER_PROPERTY = "leaseNumber";
    public static final String PARCEL_LOCATION_PROPERTY = "parcelAddress";
    
    private BaUnitBean baUnit;
    private RrrBean rightholderRrr;
    private SolaList<PartyBean> rightHolderList;
    private String leaseNumber;
    private BigDecimal serviceFee;
    private String receiptNumber;
    private Date receiptDate;
    private String lodgingDate;
    private String serviceName;
    private String parcelAddress;
    @NotNull(message=ClientMessage.CONSENT_SELECT_TRANSACTION_TYPE, payload = Localized.class)
    private TransactionTypeBean transactionType;
    @NotNull(message=ClientMessage.CONSENT_PROVIDE_REGDATE, payload = Localized.class)
    private Date registrationDate;
    @NotNull(message=ClientMessage.CONSENT_PROVIDE_EXPIRATION, payload = Localized.class)
    private Date expirationDate;
    private BigDecimal amount;
    private String specialConditions;
    private SolaList<PartyBean> transfereeList;
    
    public ConsentBean() {
        super();
        rightHolderList = new SolaList<PartyBean>();
        transfereeList = new SolaList<PartyBean>();
        baUnit = new BaUnitBean();
        rightholderRrr = new RrrBean();
    }

    public BaUnitBean getBaUnit() {
        if (baUnit == null) {
            baUnit = new BaUnitBean();
        }
        return baUnit;
    }

    public void setBaUnit(BaUnitBean baUnit) {
        if (baUnit == null) {
            baUnit = new BaUnitBean();
        }
        this.baUnit = baUnit;
    }

    @Size(min = 1, message = ClientMessage.CHECK_SIZE_RIGHTHOLDERLIST, payload = Localized.class)
    public ObservableList<PartyBean> getFilteredRightHolderList() {
        return rightHolderList.getFilteredList();
    }

    public SolaList<PartyBean> getRightHolderList() {
        return rightHolderList;
    }

    public void setRightHolderList(SolaList<PartyBean> rightHolderList) {
        this.rightHolderList = rightHolderList;
    }

    public RrrBean getRightholderRrr() {
        return rightholderRrr;
    }

    public void setRightholderRrr(RrrBean rightholderRrr) {
        this.rightholderRrr = rightholderRrr;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getAmountInWords(){
        return NumberToWords.getFullAmountString(getAmount());
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSpecialConditions() {
        return specialConditions;
    }

    public void setSpecialConditions(String specialConditions) {
        this.specialConditions = specialConditions;
    }

    public void setTransactionTypeCode(String transactionTypeCode){
        String oldValue = null;
        if (transactionType != null) {
            oldValue = transactionType.getCode();
        }
        setTransactionType(CacheManager.getBeanByCode(
                CacheManager.getTransactionTypes(), transactionTypeCode));
    }
    
    public String getTransactionTypeCode(){
        if(getTransactionType()!=null){
            return getTransactionType().getCode();
        } 
        return null;
    }
    
    public String getTransactionTypeName(){
        if(getTransactionType()!=null){
            return getTransactionType().getDisplayValue();
        } 
        return "";
    }
    
    public TransactionTypeBean getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionTypeBean transactionType) {
        this.transactionType = transactionType;
    }

    @Size(min = 1, message = ClientMessage.CONSENT_PROVIDE_RECIPIENT, payload = Localized.class)
    public ObservableList<PartyBean> getTransfereeListFiltered() {
        return transfereeList.getFilteredList();
    }
    
    public SolaList<PartyBean> getTransfereeList() {
        return transfereeList;
    }

    public void setTransfereeList(SolaList<PartyBean> transfereeList) {
        this.transfereeList = transfereeList;
    }

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String leaseNumber) {
        if (leaseNumber == null) {
            leaseNumber = "";
        }
        String old = this.leaseNumber;
        this.leaseNumber = leaseNumber;
        propertySupport.firePropertyChange(LEASE_NUMBER_PROPERTY, old, this.leaseNumber);
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        if (receiptNumber == null) {
            receiptNumber = "";
        }
        this.receiptNumber = receiptNumber;
    }

    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        if (receiptDate == null) {
            receiptDate = new Date();
        }
        this.receiptDate = receiptDate;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getRecipients() {
        String lessess = "";
        if (getTransfereeListFiltered() != null && getTransfereeListFiltered().size() > 0) {
            for (PartySummaryBean party : this.getTransfereeListFiltered()) {
                if (lessess.equals("")) {
                    lessess = party.getFullName().toUpperCase();
                } else {
                    lessess = lessess + " AND " + party.getFullName().toUpperCase();
                }
            }
        }
        return lessess;
    }

    public String getRightHolders() {
        String lessors = "";
        if (this.getRightHolderList() != null && this.getRightHolderList().size() > 0) {
            for (PartySummaryBean party : this.getRightHolderList()) {
                if (lessors.equals("")) {
                    lessors = party.getFullName().toUpperCase();
                } else {
                    lessors = lessors + " AND " + party.getFullName().toUpperCase();
                }
            }
        }
        return lessors;
    }

    public String getRecipientsMaritalStatus() {
        String maritalStatus = "";
        if (getTransfereeListFiltered() != null && getTransfereeListFiltered().size() > 0) {
            for (PartySummaryBean party : getTransfereeListFiltered()) {
                if (maritalStatus.equals("")) {
                    maritalStatus = party.getPartyBean().getLegalType();
                }
            }
        }
        return maritalStatus.toUpperCase();
    }

    public String getRightholdersMaritalStatus() {
        String maritalStatus = "";
        if (this.getRightHolderList() != null && this.getRightHolderList().size() > 0) {
            for (PartySummaryBean party : this.getRightHolderList()) {
                if (maritalStatus.equals("")) {
                    maritalStatus = party.getPartyBean().getLegalType();
                }
            }
        }
        return maritalStatus.toUpperCase();
    }

    public String getLodgingDate() {
        return lodgingDate;
    }

    public void setLodgingDate(Date lodgingDate) {
        if (lodgingDate == null) {
            lodgingDate = new Date();
        }
        this.lodgingDate = DateFormatUtils.format(lodgingDate, "d MMMMM yyyy");
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        if (serviceName == null) {
            serviceName = "";
        }
        this.serviceName = serviceName;
    }

    public String getParcelAddress() {
        return parcelAddress;
    }

    public void setParcelAddress(String parcelAddress) {
        if (parcelAddress == null) {
            parcelAddress = "";
        }
        String old = this.parcelAddress;
        this.parcelAddress = parcelAddress;
        propertySupport.firePropertyChange(PARCEL_LOCATION_PROPERTY, old, this.parcelAddress);
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationDay() {
        if(getRegistrationDate()!=null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(getRegistrationDate());
            return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        }
        return "";
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    /*
     * Calculates expiration date starting from the given date. Consent Application is always the next 31st of March
     */
    public void calculateExpirationDate(Date startDate) {
        if(startDate == null){
            return;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        
        int currentYear = cal.get(Calendar.YEAR);

        if (cal.get(Calendar.MONTH) > 3) {
            currentYear += 1;
        }

        cal.set(currentYear, 2, 31);
        setExpirationDate(cal.getTime());
    }
    
    /** 
     * Saves consent letter. 
     * @param serviceId ID of the service, triggered consent saving.
     */    
    public void save(String serviceId){
        ConsentTO consentTO = TypeConverters.BeanToTrasferObject(this, ConsentTO.class);
        consentTO = WSManager.getInstance().getAdministrative().saveConsent(serviceId, consentTO);
        TypeConverters.TransferObjectToBean(consentTO, ConsentBean.class, this);
    }
    
    /** 
     * Returns consent letter by service ID. 
     * @param serviceId ID of the service under which consent was saved.
     */    
    public static ConsentBean getConsentByServiceId(String serviceId){
        ConsentTO consentTO = WSManager.getInstance().getAdministrative().getConsentByServiceId(serviceId);
        return TypeConverters.TransferObjectToBean(consentTO, ConsentBean.class, null);
    }
}
