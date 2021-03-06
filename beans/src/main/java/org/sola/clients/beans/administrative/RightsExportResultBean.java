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
package org.sola.clients.beans.administrative;

import java.math.BigDecimal;
import java.util.Date;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.referencedata.IdTypeBean;

/**
 *
 */
public class RightsExportResultBean extends AbstractBindingBean {
    public static final String IS_CHECKED_PROPERTY = "checked";
    
    private boolean checked;
    private String baUnitId;
    private String rightStatus;
    private Date rightStatusDate;
    private BigDecimal parcelArea;
    private String rightId;
    private String rightType;
    private Date rightRegistrationDate;
    private Date startDate;
    private Date executionDate;
    private String rightRegistrationNumber;
    private Date rightExpirationDate;
    private BigDecimal groundRent;
    private BigDecimal serviceFee;
    private BigDecimal stampDuty;
    private BigDecimal transferDuty;
    private BigDecimal registrationFee;
    private BigDecimal personalLevy;
    private BigDecimal landUsable;
    private String landUseCode;
    private String leaseNumber;
    private String rightTrackingNumber;
    private String rightHolders;
    private String payeeId;
    private String payeeName;
    private String payeeLastName;
    private String payeeGender;
    private String payeeAddress;
    private String payeePhone;
    private String payeeMobile;
    private String payeeEmail;
    private String payeeIdNumber;
    private String payeeIdTypeCode;
    private Date payeeBirthDate;
    private String parcelNumber;
       
    public RightsExportResultBean(){
        super();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        boolean oldValue = this.checked;
        this.checked = checked;
        propertySupport.firePropertyChange(IS_CHECKED_PROPERTY, oldValue, this.checked);
    }

    public String getPayeeIdTypeName() {
        String idTypeName = "";
        if(getPayeeIdTypeCode()!=null){
            IdTypeBean idBean = CacheManager.getBeanByCode(CacheManager.getIdTypes(), getPayeeIdTypeCode());
            if(idBean!=null){
                idTypeName = idBean.getDisplayValue();
            }
        }
        return idTypeName;
    }

    public String getPayeeFullName() {
        String fullName = "";
        if(getPayeeName()!=null){
            fullName = getPayeeName();
        }
        if(getPayeeLastName()!=null && !getPayeeLastName().isEmpty()){
            fullName = fullName + " " + getPayeeLastName();
        }
        return fullName;
    }
    
    public String getParcelAreaFormatted(){
        String areaFormatted = "";
        if(getParcelArea()!=null){
            areaFormatted = "with an area of " + getParcelArea().toPlainString() + " m2";
        }
        return areaFormatted;
    }

    public BigDecimal getParcelArea() {
        if(parcelArea==null){
            return BigDecimal.ZERO;
        }
        return parcelArea;
    }

    public void setParcelArea(BigDecimal parcelArea) {
        this.parcelArea = parcelArea;
    }
  
    public String getBaUnitId() {
        return baUnitId;
    }

    public void setBaUnitId(String baUnitId) {
        this.baUnitId = baUnitId;
    }
    
    public String getRightholdersFormatted(){
        String formattedOwners = getRightHolders();

        if(formattedOwners!=null){
            String[] tmpOwners = formattedOwners.split(",");
            int lng = tmpOwners.length;
            
            for(int i = 0; i<lng; i++){
                if(i==0){
                    formattedOwners = tmpOwners[i];
                } else if(i == 3) {
                    if(lng>4){
                        formattedOwners = formattedOwners + ", " + tmpOwners[i] + ", and others";
                    } else {
                        formattedOwners = formattedOwners + ", and " + tmpOwners[i];
                    }
                    break;
                } else {
                    formattedOwners = formattedOwners + ", " + tmpOwners[i];
                }
            }
        }
        return formattedOwners;
    }

    public String getParcelNumber() {
        return parcelNumber;
    }

    public void setParcelNumber(String parcelNumber) {
        this.parcelNumber = parcelNumber;
    }

    public String getPayeeAddress() {
        return payeeAddress;
    }

    public void setPayeeAddress(String payeeAddress) {
        this.payeeAddress = payeeAddress;
    }

    public String getPayeeEmail() {
        return payeeEmail;
    }

    public void setPayeeEmail(String payeeEmail) {
        this.payeeEmail = payeeEmail;
    }

    public String getPayeeGender() {
        return payeeGender;
    }

    public void setPayeeGender(String payeeGender) {
        this.payeeGender = payeeGender;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeIdTypeCode() {
        return payeeIdTypeCode;
    }

    public void setPayeeIdTypeCode(String payeeIdTypeCode) {
        this.payeeIdTypeCode = payeeIdTypeCode;
    }

    public String getPayeeLastName() {
        return payeeLastName;
    }

    public void setPayeeLastName(String payeeLastName) {
        this.payeeLastName = payeeLastName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeePhone() {
        return payeePhone;
    }

    public void setPayeePhone(String payeePhone) {
        this.payeePhone = payeePhone;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public BigDecimal getGroundRent() {
        if(groundRent==null){
            return BigDecimal.ZERO;
        }
        return groundRent;
    }

    public void setGroundRent(BigDecimal groundRent) {
        this.groundRent = groundRent;
    }

    public BigDecimal getServiceFee() {
        if(serviceFee==null){
            return BigDecimal.ZERO;
        }
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getLandUsable() {
        if(landUsable==null){
            return BigDecimal.ZERO;
        }
        return landUsable;
    }

    public void setLandUsable(BigDecimal landUsable) {
        this.landUsable = landUsable;
    }

    public String getLandUseCode() {
        return landUseCode;
    }

    public void setLandUseCode(String landUseCode) {
        this.landUseCode = landUseCode;
    }

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String leaseNumber) {
        this.leaseNumber = leaseNumber;
    }

    public BigDecimal getPersonalLevy() {
        if(personalLevy==null){
            return BigDecimal.ZERO;
        }
        return personalLevy;
    }

    public void setPersonalLevy(BigDecimal personalLevy) {
        this.personalLevy = personalLevy;
    }

    public BigDecimal getRegistrationFee() {
        if(registrationFee==null){
            return BigDecimal.ZERO;
        }
        return registrationFee;
    }

    public void setRegistrationFee(BigDecimal registrationFee) {
        this.registrationFee = registrationFee;
    }

    public BigDecimal getStampDuty() {
        if(stampDuty==null){
            return BigDecimal.ZERO;
        }
        return stampDuty;
    }

    public void setStampDuty(BigDecimal stampDuty) {
        this.stampDuty = stampDuty;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public BigDecimal getTransferDuty() {
        if(transferDuty==null){
            return BigDecimal.ZERO;
        }
        return transferDuty;
    }

    public void setTransferDuty(BigDecimal transferDuty) {
        this.transferDuty = transferDuty;
    }

    public Date getRightExpirationDate() {
        return rightExpirationDate;
    }

    public void setRightExpirationDate(Date rightExpirationDate) {
        this.rightExpirationDate = rightExpirationDate;
    }

    public String getRightHolders() {
        return rightHolders;
    }

    public void setRightHolders(String rightHolders) {
        this.rightHolders = rightHolders;
    }

    public Date getRightRegistrationDate() {
        return rightRegistrationDate;
    }

    public void setRightRegistrationDate(Date rightRegistrationDate) {
        this.rightRegistrationDate = rightRegistrationDate;
    }

    public String getRightRegistrationNumber() {
        return rightRegistrationNumber;
    }

    public void setRightRegistrationNumber(String rightRegistrationNumber) {
        this.rightRegistrationNumber = rightRegistrationNumber;
    }

    public String getRightStatus() {
        return rightStatus;
    }

    public void setRightStatus(String rightStatus) {
        this.rightStatus = rightStatus;
    }

    public String getRightTrackingNumber() {
        return rightTrackingNumber;
    }

    public void setRightTrackingNumber(String rightTrackingNumber) {
        this.rightTrackingNumber = rightTrackingNumber;
    }
    
    public String getRightType() {
        return rightType;
    }

    public void setRightType(String rightType) {
        this.rightType = rightType;
    }

    public String getRightId() {
        return rightId;
    }

    public void setRightId(String rightId) {
        this.rightId = rightId;
    }

    public Date getRightStatusDate() {
        return rightStatusDate;
    }

    public void setRightStatusDate(Date rightStatusDate) {
        this.rightStatusDate = rightStatusDate;
    }

    public Date getPayeeBirthDate() {
        return payeeBirthDate;
    }

    public void setPayeeBirthDate(Date payeeBirthDate) {
        this.payeeBirthDate = payeeBirthDate;
    }

    public String getPayeeIdNumber() {
        return payeeIdNumber;
    }

    public void setPayeeIdNumber(String payeeIdNumber) {
        this.payeeIdNumber = payeeIdNumber;
    }
}