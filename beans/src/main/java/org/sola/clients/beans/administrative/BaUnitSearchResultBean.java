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

import java.util.Date;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.referencedata.RegistrationStatusTypeBean;
import org.sola.common.StringUtility;

/**
 * Represents BA unit search result.
 */
public class BaUnitSearchResultBean extends AbstractBindingBean {
    public static final String ID_PROPERTY = "id";
    public static final String NAME_FIRST_PART_PROPERTY = "nameFirstPart";
    public static final String NAME_LAST_PART_PROPERTY = "nameLastPart";
    public static final String STATUS_CODE_PROPERTY = "statusCode";
    public static final String REGISTRATION_STATUS_PROPERTY = "registrationStatus";
    public static final String RIGHTHOLDERS_PROPERTY = "rightholders";
    public static final String REGISTRATION_NUMBER_PROPERTY = "registrationNumber";
    public static final String REGISTRATION_DATE_PROPERTY = "registrationDate";
    public static final String LEASE_NUMBER_PROPERTY = "leaseNumber";
    public static final String LAND_USE_CODE_PROPERTY = "landUseCode";
    
    private String id;
    private String nameFirstPart;
    private String nameLastPart;
    private RegistrationStatusTypeBean registrationStatus;
    private String rightholders;
    private String registrationNumber;
    private Date registrationDate;
    private Date originalRegistrationDate;
    private String leaseNumber;
    private String landUseCode;
    
    public BaUnitSearchResultBean(){
        super();
    }

    public String getLandUseCode() {
        return landUseCode;
    }

    public void setLandUseCode(String landUseCode) {
        String oldValue = this.landUseCode;
        this.landUseCode = landUseCode;
        propertySupport.firePropertyChange(LAND_USE_CODE_PROPERTY, oldValue, this.landUseCode);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        String oldValue = this.id;
        this.id = id;
        propertySupport.firePropertyChange(ID_PROPERTY, oldValue, this.id);
    }

    public String getNameFirstPart() {
        return nameFirstPart;
    }

    public void setNameFirstPart(String nameFirstPart) {
        String oldValue = this.nameFirstPart;
        this.nameFirstPart = nameFirstPart;
        propertySupport.firePropertyChange(NAME_FIRST_PART_PROPERTY, oldValue, this.nameFirstPart);
    }

    public String getNameLastPart() {
        return nameLastPart;
    }

    public void setNameLastPart(String nameLastPart) {
        String oldValue = this.nameLastPart;
        this.nameLastPart = nameLastPart;
        propertySupport.firePropertyChange(NAME_LAST_PART_PROPERTY, oldValue, this.nameLastPart);
    }

    public String getRightholders() {
        return rightholders;
    }

    public void setRightholders(String rightholders) {
        String oldValue = this.rightholders;
        this.rightholders = rightholders;
        propertySupport.firePropertyChange(RIGHTHOLDERS_PROPERTY, oldValue, this.rightholders);
    }

    public String getStatusCode() {
        return getRegistrationStatus().getCode();
    }

    public void setStatusCode(String statusCode) {
        String oldValue = getStatusCode();
        setRegistrationStatus(CacheManager.getBeanByCode(CacheManager.getRegistrationStatusTypes(), statusCode));
        propertySupport.firePropertyChange(STATUS_CODE_PROPERTY, oldValue, statusCode);
    }

    public RegistrationStatusTypeBean getRegistrationStatus() {
        if(registrationStatus == null){
            registrationStatus = new RegistrationStatusTypeBean();
        }
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatusTypeBean registrationStatus) {
        this.setJointRefDataBean(getRegistrationStatus(), registrationStatus, REGISTRATION_STATUS_PROPERTY);
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        Date oldValue = this.registrationDate;
        this.registrationDate = registrationDate;
        propertySupport.firePropertyChange(REGISTRATION_DATE_PROPERTY, oldValue, this.registrationDate);
    }

    public Date getOriginalRegistrationDate() {
        return originalRegistrationDate;
    }

    public void setOriginalRegistrationDate(Date originalRegistrationDate) {
        this.originalRegistrationDate = originalRegistrationDate;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        String oldValue = this.registrationNumber;
        this.registrationNumber = registrationNumber;
        propertySupport.firePropertyChange(REGISTRATION_NUMBER_PROPERTY, oldValue, this.registrationNumber);
    }
    
    public String getBaUnitNumber(){
        String number = getNameFirstPart();
        if(StringUtility.isEmpty(number)){
            number = getNameLastPart();
        } else if(!StringUtility.isEmpty(getNameLastPart())) {
            number = number + " - " + getNameLastPart();
        }
        return number;
    }

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String leaseNumber) {
        String oldValue = this.leaseNumber;
        this.leaseNumber = leaseNumber;
        propertySupport.firePropertyChange(LEASE_NUMBER_PROPERTY, oldValue, this.leaseNumber);
    }
}
