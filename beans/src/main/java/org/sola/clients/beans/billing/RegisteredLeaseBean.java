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
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;

/**
 *
 * @author nmafereka
 */
public class RegisteredLeaseBean extends AbstractBindingBean {

    private static final String LEASE_NUMBER_PROPERTY = "leaseNumber";

    private static final String LAND_USE_CODE_PROPERTY = "landUseCode";

    private static final String GROUND_RENT_ZONE_PROPERTY = "groundRentZone";

    private static final String LEASED_AREA_PROPERTY = "leasedArea";

    private static final String REGISTRATION_DATE_PROPERTY = "registrationDate";

    private static final String LAST_PAYMENT_DATE_PROPERTY = "lastPaymentDate";

    private static final String COMPLIANCE_CODE_PROPERTY = "complianceCode";

    private String leaseNumber;

    private String landUseCode;

    private String groundRentZone;

    private BigDecimal leasedArea;

    private Date registrationDate;

    private Date lastPaymentDate;

    private int complianceCode;

    public RegisteredLeaseBean() {
        super();
    }

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String leaseNumber) {
        String oldValue = this.leaseNumber;
        this.leaseNumber = leaseNumber;
        propertySupport.firePropertyChange(LEASE_NUMBER_PROPERTY, oldValue, this.leaseNumber);
    }

    public String getLandUseCode() {
        return landUseCode;
    }

    public void setLandUseCode(String landUseCode) {
        String oldValue = this.landUseCode;
        this.landUseCode = landUseCode;
        propertySupport.firePropertyChange(LAND_USE_CODE_PROPERTY, oldValue, this.landUseCode);
    }

    public String getGroundRentZone() {
        return groundRentZone;
    }

    public void setGroundRentZone(String groundRentZone) {
        String oldValue = this.groundRentZone;
        this.groundRentZone = groundRentZone;
        propertySupport.firePropertyChange(GROUND_RENT_ZONE_PROPERTY, oldValue, this.groundRentZone);
    }

    public BigDecimal getLeasedArea() {
        return leasedArea;
    }

    public void setLeasedArea(BigDecimal leasedArea) {
        BigDecimal oldValue = this.leasedArea;
        this.leasedArea = leasedArea;
        propertySupport.firePropertyChange(LEASED_AREA_PROPERTY, oldValue, this.leasedArea);
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        Date oldValue = this.registrationDate;
        this.registrationDate = registrationDate;
        propertySupport.firePropertyChange(REGISTRATION_DATE_PROPERTY, oldValue, this.registrationDate);
    }

    public Date getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(Date lastPaymentDate) {
        Date oldValue = this.lastPaymentDate;
        this.lastPaymentDate = lastPaymentDate;
        propertySupport.firePropertyChange(LAST_PAYMENT_DATE_PROPERTY, oldValue, this.lastPaymentDate);
    }

    public int getComplianceCode() {
        return complianceCode;
    }

    public void setComplianceCode(int complianceCode) {
        int oldValue = this.complianceCode;
        this.complianceCode = complianceCode;
        propertySupport.firePropertyChange(COMPLIANCE_CODE_PROPERTY, oldValue, this.complianceCode);
    }

    public static RegisteredLeaseBean getLease(String leaseId) {
        return TypeConverters.TransferObjectToBean(
                WSManager.getInstance().getBilling().getLease(leaseId),
                RegisteredLeaseBean.class, null);
    }

}
