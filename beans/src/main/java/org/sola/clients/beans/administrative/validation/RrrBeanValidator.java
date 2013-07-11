/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
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
package org.sola.clients.beans.administrative.validation;

import java.math.BigDecimal;
import java.text.DateFormat;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.sola.clients.beans.administrative.RrrBean;
import org.sola.clients.beans.source.SourceBean;
import org.sola.common.DateUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Validates RrrBeans to ensure they contain the necessary data for the specific
 * Rrr type. {@link RrrBean}.
 */
public class RrrBeanValidator implements ConstraintValidator<RrrBeanCheck, RrrBean> {

    @Override
    public void initialize(RrrBeanCheck a) {
    }

    @Override
    public boolean isValid(RrrBean rrrBean, ConstraintValidatorContext constraintContext) {
        boolean result = true;

        // Sublease RRR
        if (RrrBean.CODE_SUBLEASE.equals(rrrBean.getTypeCode())) {
            // Check the sublease expiry date is before the lease expirty date
            if (rrrBean.getLeaseExpiryDate() != null && rrrBean.getExpirationDate() != null
                    && rrrBean.getExpirationDate().after(rrrBean.getLeaseExpiryDate())) {
                result = false;
                constraintContext.buildConstraintViolationWithTemplate(
                        (MessageUtility.getLocalizedMessageText(
                        ClientMessage.CHECK_SUBLEASE_EXPIRY, new Object[]{
                    DateUtility.getDateString(rrrBean.getLeaseExpiryDate(), DateFormat.MEDIUM)}))).addConstraintViolation();
            }

            // Subplot validation is done by the SubleasePanel as the bean must not
            // have a dependency on the GIS package. 
            if (!rrrBean.isSubplotValid()) {
                result = false;
                constraintContext.buildConstraintViolationWithTemplate(
                        (MessageUtility.getLocalizedMessageText(
                        ClientMessage.CHECK_SUBLEASE_PLOT))).addConstraintViolation();
            }
        }

        // Sublease Mortgage RRR
        if (RrrBean.CODE_SUBLEASE_MORTGAGE.equals(rrrBean.getTypeCode())) {
            // Verify the sublease mortgage has consent from the lessee
            boolean found = false;
            for (SourceBean bean : rrrBean.getSourceList()) {
                if (SourceBean.CODE_LESSEE_CONSENT.equals(bean.getTypeCode())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                result = false;
                constraintContext.buildConstraintViolationWithTemplate(
                        (MessageUtility.getLocalizedMessageText(
                        ClientMessage.CHECK_SUBLEASE_MORTGAGE_CONSENT))).addConstraintViolation();
            }
        }
        
        //Mortgage
        if (RrrBean.CODE_MORTGAGE.equals(rrrBean.getTypeCode())){
            // Check mortgage ranking
            if(rrrBean.getMortgageRanking()!=null && rrrBean.getMortgageRanking().compareTo(Integer.valueOf(0))!=0){
                if(rrrBean.getMortgageRanking().compareTo(Integer.valueOf(0))<0 || 
                    rrrBean.getMortgageRanking().compareTo(Integer.valueOf(10))>=0){
                result = false;
                constraintContext.buildConstraintViolationWithTemplate(
                        (MessageUtility.getLocalizedMessageText(
                        ClientMessage.MORTGAGE_RANK_RANGE_ERROR))).addConstraintViolation();
                }
            }
        }
        return result;
    }
}
