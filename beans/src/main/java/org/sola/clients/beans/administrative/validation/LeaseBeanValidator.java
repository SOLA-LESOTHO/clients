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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.sola.clients.beans.administrative.LeaseBean;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.common.StringUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Used to validate {@link LeaseBean}
 */
public class LeaseBeanValidator implements ConstraintValidator<LeaseBeanCheck, LeaseBean> {

    @Override
    public void initialize(LeaseBeanCheck constraintAnnotation) {
    }

    @Override
    public boolean isValid(LeaseBean value, ConstraintValidatorContext context) {
        boolean result = true;
        // Check lease number and parcel number to be the same
        if (!StringUtility.isEmpty(value.getLeaseNumber()) && value.getCadastreObject() != null
                && !value.getLeaseNumber().equals(value.getCadastreObject().toString())) {
            result = false;
            context.buildConstraintViolationWithTemplate(
                    (MessageUtility.getLocalizedMessageText(
                    ClientMessage.LEASE_NUMBER_AND_PARCEL_CODE_DOESNT_MATCH))).addConstraintViolation();
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

        // Check lessees to be of the same type
        if (value.getFilteredLessees().size() > 0) {
            String lesseeType = value.getFilteredLessees().get(0).getTypeCode();
            for (PartySummaryBean lessee : value.getFilteredLessees()) {
                if (!StringUtility.empty(lessee.getTypeCode()).equals(lesseeType)) {
                    result = false;
                    context.buildConstraintViolationWithTemplate(
                            (MessageUtility.getLocalizedMessageText(
                            ClientMessage.LEASE_LESSEES_TYPE_DIFFERENT))).addConstraintViolation();
                }
                lesseeType = lessee.getTypeCode();
            }
        }

        return result;
    }
}