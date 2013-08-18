/**
 * ******************************************************************************************
 * Copyright (c) 2013 Food and Agriculture Organization of the United Nations
 * (FAO) and the Lesotho Land Administration Authority (LAA). All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the names of FAO, the LAA nor the names of
 * its contributors may be used to endorse or promote products derived from this
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
        if (value.getPersonalLevy() != null && value.getPersonalLevy().compareTo(BigDecimal.ZERO) != 0) {
            if (value.getPersonalLevy().compareTo(BigDecimal.ONE) < 0
                    || value.getPersonalLevy().compareTo(BigDecimal.valueOf(2L)) >= 0) {
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

        if (!RrrBean.CODE_SUBLEASE.equals(value.getTypeCode())) {
            // Check lessees to be of the same type and one lessee to be accountHolder role (payor). This
            // check is not required for subleases
            int accountHolders = 0;

            if (value.getFilteredRightHolderList().size() > 0) {
                String lesseeType = value.getFilteredRightHolderList().get(0).getTypeCode();
                for (PartyBean lessee : value.getFilteredRightHolderList()) {

                    // Check account holder role
                    if (lessee.getFilteredRoleList() != null && lessee.getFilteredRoleList().size() > 0) {
                        for (PartyRoleBean role : lessee.getFilteredRoleList()) {
                            if (role.getRoleCode().equals(PartyRoleBean.CODE_ACCOUNT_HOLDER_PROPERTY)) {
                                accountHolders += 1;
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

            if (accountHolders != 1) {
                result = false;
                context.buildConstraintViolationWithTemplate(
                        (MessageUtility.getLocalizedMessageText(
                        ClientMessage.LEASE_LESSEE_ACCOUNT_HOLDER_MISSING))).addConstraintViolation();
            }
        }
        return result;
    }
}
