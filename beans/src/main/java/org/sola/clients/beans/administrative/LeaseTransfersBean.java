/*
 * Copyright 2014 Food and Agriculture Organization of the United Nations (FAO).
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

import org.sola.clients.beans.AbstractIdBean;

/**
 *
 * @author Charlizza
 */
public class LeaseTransfersBean extends AbstractIdBean{
    
    private String serviceName;
    
    private Integer serviceCount;
    
    private Integer maleCount;
    
    private Integer femaleCount;
    
    private Integer entityCount;
    
    private Integer jointCount;
    
    private double totalAmount;
    
    private double stampDuty;
    
    private double transferDuty;
    
    private double registrationFee;

    public LeaseTransfersBean() {
        
        super();
    }

    public Integer getJointCount() {
        return jointCount;
    }

    public void setJointCount(Integer jointCount) {
        this.jointCount = jointCount;
    }
    
    public Integer getEntityCount() {
        return entityCount;
    }

    public void setEntityCount(Integer entityCount) {
        this.entityCount = entityCount;
    }

    public Integer getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(Integer femaleCount) {
        this.femaleCount = femaleCount;
    }

    public Integer getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(Integer maleCount) {
        this.maleCount = maleCount;
    }

    public double getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(double registrationFee) {
        this.registrationFee = registrationFee;
    }

    public Integer getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(Integer serviceCount) {
        this.serviceCount = serviceCount;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getStampDuty() {
        return stampDuty;
    }

    public void setStampDuty(double stampDuty) {
        this.stampDuty = stampDuty;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public double getTransferDuty() {
        return transferDuty;
    }

    public void setTransferDuty(double transferDuty) {
        this.transferDuty = transferDuty;
    }
    
}
