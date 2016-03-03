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
import org.sola.clients.beans.AbstractBindingBean;

/**
 *
 * @author nmafereka
 */
public class PaymentScheduleBean  extends AbstractBindingBean {

    private static final String BILL_NUMBER_PROPERTY = "billNumber";

    private static final String BILLING_PERIOD_PROPERTY = "billingPeriod";

    private static final String LEASE_NUMBER_PROPERTY = "leaseNumber";

    private static final String LAND_USE_CODE_PROPERTY = "landUseCode";

    private static final String GROUND_RENT_ZONE_PROPERTY = "groundRentZone";

    private static final String LEASED_AREA_PROPERTY = "leasedArea";

    private static final String PAY_PERIOD_CODE_PROPERTY = "payPeriodCode";

    private static final String CALCULATION_METHOD_PROPERTY = "calculationMethod";

    private static final String IMPOSE_PENALTY_PROPERTY = "imposePenalty";

    private static final String COMPLIANCE_CODE_PROPERTY = "complianceCode";
    
     private static final String STATUS_CODE_PROPERTY = "statusCode";

    private String billingPeriod;

    private String billNumber;

    private String leaseNumber;

    private String landUseCode;

    private String groundRentZone;

    private BigDecimal leasedArea;

    private String payPeriodCode;

    private String calculationMethod;

    private int imposePenalty;

    private int complianceCode;

    private String statusCode;
    
    public PaymentScheduleBean(){
        super();
    }

    public String getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(String billingPeriod) {
        String oldValue = this.billingPeriod;
        this.billingPeriod = billingPeriod;
        propertySupport.firePropertyChange(BILLING_PERIOD_PROPERTY, oldValue, this.billingPeriod);
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
      	String oldValue = this.billNumber;
        this.billNumber = billNumber;
        propertySupport.firePropertyChange(BILL_NUMBER_PROPERTY, oldValue, this.billNumber);  
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

    public String getPayPeriodCode() {
        return payPeriodCode;
    }

    public void setPayPeriodCode(String payPeriodCode) {
	String oldValue = this.payPeriodCode;
        this.payPeriodCode = payPeriodCode;
        propertySupport.firePropertyChange(PAY_PERIOD_CODE_PROPERTY, oldValue, this.payPeriodCode);        
    }

    public String getCalculationMethod() {
        return calculationMethod;
    }

    public void setCalculationMethod(String calculationMethod) {
	String oldValue = this.calculationMethod;
        this.calculationMethod = calculationMethod;
        propertySupport.firePropertyChange(CALCULATION_METHOD_PROPERTY, oldValue, this.calculationMethod);
    }

    public int getImposePenalty() {
        return imposePenalty;
    }

    public void setImposePenalty(int imposePenalty) {
	int oldValue = this.imposePenalty;
        this.imposePenalty = imposePenalty;
        propertySupport.firePropertyChange(IMPOSE_PENALTY_PROPERTY, oldValue, this.imposePenalty);        
    }

    public int getComplianceCode() {
        return complianceCode;
    }

    public void setComplianceCode(int complianceCode) {
        int oldValue = this.complianceCode;
        this.complianceCode = complianceCode;
        propertySupport.firePropertyChange(COMPLIANCE_CODE_PROPERTY, oldValue, this.complianceCode);       
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
	String oldValue = this.statusCode;
        this.statusCode = statusCode;
        propertySupport.firePropertyChange(STATUS_CODE_PROPERTY, oldValue, this.statusCode);        
    }
    
    

}
