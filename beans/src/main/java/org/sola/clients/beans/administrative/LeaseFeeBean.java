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
import org.sola.clients.beans.AbstractBindingBean;


public class LeaseFeeBean extends AbstractBindingBean {

    public static final String GROUND_RENT_PROPERTY = "groundRent";
    public static final String SERVICE_FEE_PROPERTY = "serviceFee";
    public static final String STAMP_DUTY_PROPERTY = "stampDuty";
    public static final String TRANSFER_DUTY_PROPERTY = "transferDuty";
    public static final String REGISTRATION_FEE_PROPERTY = "registrationFee";
    
    private BigDecimal groundRent;
    private BigDecimal registrationFee;
    private BigDecimal serviceFee;
    private BigDecimal stampDuty;
    private BigDecimal transferDuty;

    public BigDecimal getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(BigDecimal registrationFee) {
        BigDecimal oldValue = this.registrationFee;
        this.registrationFee = registrationFee;
        propertySupport.firePropertyChange(REGISTRATION_FEE_PROPERTY, oldValue, this.registrationFee);
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        BigDecimal oldValue = this.serviceFee;
        this.serviceFee = serviceFee;
        propertySupport.firePropertyChange(SERVICE_FEE_PROPERTY, oldValue, this.serviceFee);
    }

    public BigDecimal getStampDuty() {
        return stampDuty;
    }

    public void setStampDuty(BigDecimal stampDuty) {
        BigDecimal oldValue = this.stampDuty;
        this.stampDuty = stampDuty;
        propertySupport.firePropertyChange(STAMP_DUTY_PROPERTY, oldValue, this.stampDuty);
    }

    public BigDecimal getTransferDuty() {
        return transferDuty;
    }

    public void setTransferDuty(BigDecimal transferDuty) {
        BigDecimal oldValue = this.transferDuty;
        this.transferDuty = transferDuty;
        propertySupport.firePropertyChange(TRANSFER_DUTY_PROPERTY, oldValue, this.transferDuty);
    }

    public BigDecimal getGroundRent() {
        return groundRent;
    }

    public void setGroundRent(BigDecimal groundRent) {
        BigDecimal oldValue = this.groundRent;
        this.groundRent = groundRent;
        propertySupport.firePropertyChange(GROUND_RENT_PROPERTY, oldValue, this.groundRent);
    }
}
