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
import java.util.Calendar;
import java.util.Date;
import javax.validation.constraints.Size;
import org.apache.commons.lang.time.DateFormatUtils;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractTransactionedBean;
import org.sola.clients.beans.administrative.validation.ConsentBeanCheck;
import org.sola.clients.beans.administrative.validation.MortgageValidationGroup;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.messaging.ClientMessage;

/**
 *
 * @author Tokelo
 */
@ConsentBeanCheck
public final class ConsentBean extends AbstractTransactionedBean {

    public static final String REGISTRATION_DATE_PROPERTY = "registrationDate";
    private BaUnitBean baUnit;
    private RrrBean rightholderRrr;
    private String conditionText;
    private SolaList<PartyBean> rightHolderList;
    private SolaList<PartyBean> recipientList;
    private String leaseNumber;
    private BigDecimal serviceFee;
    private String receiptNumber;
    private String recipients;
    private String recipientsMaritalStatus;
    private String rightholders;
    private String rightholdersMaritalStatus;
    private String freeText;
    private String transactionType;
    private String lodgingDate;
    private String serviceName;
    private String parcelAddress;
    private Date consentDate;
    private String registrationDate;
    private String registrationDay;
    private String expirationDate;

    public ConsentBean() {
        super();
        rightHolderList = new SolaList();
        baUnit = new BaUnitBean();
        rightholderRrr = new RrrBean();
        consentDate = new Date();
        this.setExpirationDate(consentDate);
        expirationDate = getExpirationDate();
    }

    public void setConditionText(String conditionText) {
        if (conditionText == null) {
            conditionText = "";
        }
        this.conditionText = conditionText;
    }

    public String getConditionText() {
        if (conditionText == null) {
            conditionText = "";
        }
        return conditionText;
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

    @Size(min = 1, groups = {MortgageValidationGroup.class}, message = ClientMessage.CHECK_SIZE_RIGHTHOLDERLIST, payload = Localized.class)
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

    public SolaList<PartyBean> getRecipientList() {
        return recipientList;
    }

    public void setRecipientList(SolaList<PartyBean> recipientList) {
        this.recipientList = recipientList;
    }

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String leaseNumber) {
        if (leaseNumber == null) {
            leaseNumber = "";
        }
        this.leaseNumber = leaseNumber;
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

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getRecipients() {
        String lessess = "";
        if (this.getRecipientList() != null && this.getRecipientList().size() > 0) {
            for (PartySummaryBean party : this.getRecipientList()) {
                if (lessess.equals("")) {
                    lessess = party.getFullName().toUpperCase();
                } else {
                    lessess = lessess + " and " + party.getFullName().toUpperCase();
                }
            }
        }
        return lessess;
    }

    public void setRecipients(String recipients) {
        if (recipients == null) {
            recipients = "";
        }
        this.recipients = recipients;
    }

    public String getRightHolders() {
        String lessors = "";
        if (this.getRightHolderList() != null && this.getRightHolderList().size() > 0) {
            for (PartySummaryBean party : this.getRightHolderList()) {
                if (lessors.equals("")) {
                    lessors = party.getFullName().toUpperCase();
                } else {
                    lessors = lessors + " and " + party.getFullName().toUpperCase();
                }
            }
        }
        return lessors;
    }

    public void setRightholders(String rightholders) {
        if (rightholders == null) {
            rightholders = "";
        }
        this.rightholders = rightholders;
    }

    public String getRecipientsMaritalStatus() {
        String maritalStatus = "";
        if (this.getRecipientList() != null && this.getRecipientList().size() > 0) {
            for (PartySummaryBean party : this.getRecipientList()) {
                if (maritalStatus.equals("")) {
                    maritalStatus = party.getPartyBean().getLegalType();
                } else {
                    maritalStatus = maritalStatus + ", " + party.getPartyBean().getLegalType();
                }
            }
        }
        return maritalStatus.toUpperCase();
    }

    public void setRecipientsMaritalStatus(String recipientsMaritalStatus) {
        if (recipientsMaritalStatus == null) {
            recipientsMaritalStatus = "";
        }
        this.recipientsMaritalStatus = recipientsMaritalStatus;
    }

    public String getRightholdersMaritalStatus() {
        String maritalStatus = "";
        if (this.getRightHolderList() != null && this.getRightHolderList().size() > 0) {
            for (PartySummaryBean party : this.getRightHolderList()) {
                if (maritalStatus.equals("")) {
                    maritalStatus = party.getPartyBean().getLegalType();
                } else {
                    maritalStatus = maritalStatus + ", " + party.getPartyBean().getLegalType();
                }
            }
        }
        return maritalStatus.toUpperCase();
    }

    public void setRightholdersMaritalStatus(String rightholdersMaritalStatus) {
        if (rightholdersMaritalStatus == null) {
            rightholdersMaritalStatus = "";
        }
        this.rightholdersMaritalStatus = rightholdersMaritalStatus;
    }

    public String getFreeText() {
        return freeText;
    }

    public void setFreeText(String freeText) {
        if (freeText == null) {
            freeText = "";
        }
        this.freeText = freeText;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        if (transactionType == null) {
            transactionType = "";
        }
        this.transactionType = transactionType;
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
        this.parcelAddress = parcelAddress;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = DateFormatUtils.format(registrationDate, "MMMMM yyyy");
    }

    public String getRegistrationDay() {
        return registrationDay;
    }

    public void setRegistrationDay(Date registrationDay) {
        this.registrationDay = DateFormatUtils.format(registrationDay, "dd");
    }

    public Date getConsentDate() {
        return consentDate;
    }

    public void setConsentDay(Date consentDate) {
        this.consentDate = consentDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    /*
     * Expiration date for Consent Application is always the next 31st of March
     */
    public void setExpirationDate(Date expirationDate) {
        Integer currentYear = Integer.parseInt(DateFormatUtils.format(expirationDate, "yyyy"));
        Integer currentMonth = Integer.parseInt(DateFormatUtils.format(expirationDate, "M"));
        
        if (currentMonth > 3){
            currentYear = currentYear + 1;
        }
        
        Calendar oldExpirationDate = Calendar.getInstance();
        oldExpirationDate.set(currentYear, 2, 31);
        
        Date newExpirationDate = oldExpirationDate.getTime();
        this.expirationDate = DateFormatUtils.format(newExpirationDate, "d MMMMM yyyy");

    }
}
