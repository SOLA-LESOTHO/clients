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
import java.util.List;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;

/**
 *
 * @author nmafereka
 */
public class PayRateBean  extends AbstractBindingBean {

    private static final String LAND_USE_CODE_PROPERTY = "landUseCode";

    private static final String LAND_GRADE_CODE_PROPERTY = "landGradeCode";

    private static final String PAY_PERIOD_CODE_PROPERTY = "payPeriodCode";

    private static final String RATE_AMOUNT_PROPERTY = "rateAmount";

    private static final String CALCULATION_TYPE_CODE_PROPERTY = "calculationTypeCode";

    private String landUseCode;

    private String landGradeCode;

    private int payPeriodCode;

    private BigDecimal rateAmount;

    private String calculationTypeCode;
    
    public PayRateBean(){
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

    public String getLandGradeCode() {
        return landGradeCode;
    }

    public void setLandGradeCode(String landGradeCode) {
        String oldValue = this.landGradeCode;
        this.landGradeCode = landGradeCode;
        propertySupport.firePropertyChange(LAND_GRADE_CODE_PROPERTY, oldValue, this.landGradeCode);
    }

    public int getPayPeriodCode() {
        return payPeriodCode;
    }

    public void setPayPeriodCode(int payPeriodCode) {
        int oldValue = this.payPeriodCode;
        this.payPeriodCode = payPeriodCode;
        propertySupport.firePropertyChange(PAY_PERIOD_CODE_PROPERTY, oldValue, this.payPeriodCode);
    }

    public BigDecimal getRateAmount() {
        return rateAmount;
    }

    public void setRateAmount(BigDecimal rateAmount) {
        BigDecimal oldValue = this.rateAmount;
        this.rateAmount = rateAmount;
        propertySupport.firePropertyChange(RATE_AMOUNT_PROPERTY, 
                                                oldValue, this.rateAmount);

    }

    public String getCalculationTypeCode() {
        return calculationTypeCode;
    }

    public void setCalculationTypeCode(String calculationTypeCode) {
        String oldValue = this.calculationTypeCode;
        this.calculationTypeCode = calculationTypeCode;
        propertySupport.firePropertyChange(CALCULATION_TYPE_CODE_PROPERTY, 
                                            oldValue, this.calculationTypeCode);

        
    }
    
    /**
     * Returns list of BA Units, created by the given service.
     *
     * 
     * @param billingPeriod
     * @param registrationYear
     * @param landUseCode
     * @param groundRentZone
     * @return 
     */
    public static List<PayRateBean> getPayRate(int billingPeriod, int registrationYear,
						String landUseCode, String groundRentZone) {
        return TypeConverters.TransferObjectListToBeanList(
                WSManager.getInstance().getBilling().getPayRate(
                        billingPeriod, registrationYear,
                            landUseCode, groundRentZone),
                                PayRateBean.class, null);
    }
    
    

}
