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
