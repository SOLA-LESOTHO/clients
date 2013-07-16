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
package org.sola.clients.beans.administrative.validation;

import java.math.BigDecimal;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.sola.clients.beans.administrative.RrrBean;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.party.PartyRoleBean;
import org.sola.common.StringUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Used to validate {@link LeaseBean}
 */
public class LeaseBeanValidator implements ConstraintValidator<LeaseBeanCheck, RrrBean> {

    @Override
    public void initialize(LeaseBeanCheck constraintAnnotation) {
    }

    @Override
    public boolean isValid(RrrBean value, ConstraintValidatorContext context) {
        boolean result = true;

        // Check personal levy factor
        if(value.getPersonalLevy()!=null && value.getPersonalLevy().compareTo(BigDecimal.ZERO)!=0){
            if(value.getPersonalLevy().compareTo(BigDecimal.ONE)<0 || 
                    value.getPersonalLevy().compareTo(BigDecimal.valueOf(2L))>=0){
                result = false;
                context.buildConstraintViolationWithTemplate(
                        (MessageUtility.getLocalizedMessageText(
                        ClientMessage.LEASE_PERSONAL_LEVY_RANGE_ERROR))).addConstraintViolation();
            }
        }
        
        // Check start and expiration dates
        if (value.getStartDate() != null && value.getExpirationDate() != null) {
            if (value.getStartDate().after(value.getExpirationDate())) {
                result = false;
                context.buildConstraintViolationWithTemplate(
                        (MessageUtility.getLocalizedMessageText(
                        ClientMessage.LEASE_START_DATE_GRATER_THAN_EXPIRATION_DATE))).addConstraintViolation();
            }
        }

        // Check lessees to be of the same type and one lessee to be accountHolder role (payor)
        int accountHolders = 0;
        
        if (value.getFilteredRightHolderList().size() > 0) {
            String lesseeType = value.getFilteredRightHolderList().get(0).getTypeCode();
            for (PartyBean lessee : value.getFilteredRightHolderList()) {
                
                // Check account holder role
                if(lessee.getFilteredRoleList()!=null && lessee.getFilteredRoleList().size()>0){
                    for(PartyRoleBean role : lessee.getFilteredRoleList()){
                        if(role.getRoleCode().equals(PartyRoleBean.CODE_ACCOUNT_HOLDER_PROPERTY)){
                            accountHolders+=1;
                        }
                    }
                }
                
                if (!StringUtility.empty(lessee.getTypeCode()).equals(lesseeType)) {
                    result = false;
                    context.buildConstraintViolationWithTemplate(
                            (MessageUtility.getLocalizedMessageText(
                            ClientMessage.LEASE_LESSEES_TYPE_DIFFERENT))).addConstraintViolation();
                }
                lesseeType = lessee.getTypeCode();
            }
        }
        
        if(accountHolders!=1){
            result = false;
            context.buildConstraintViolationWithTemplate(
                    (MessageUtility.getLocalizedMessageText(
                    ClientMessage.LEASE_LESSEE_ACCOUNT_HOLDER_MISSING))).addConstraintViolation();
        }
        
        return result;
    }
}
