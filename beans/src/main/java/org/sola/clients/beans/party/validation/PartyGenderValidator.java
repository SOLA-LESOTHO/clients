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
package org.sola.clients.beans.party.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.sola.clients.beans.party.PartyBean;

/**
 *
 * @author Charlizza
 */
public class PartyGenderValidator implements ConstraintValidator<PartyGenderCheck, PartyBean> {

    @Override
    public void initialize(PartyGenderCheck constraintAnnotation) {
    }

    @Override
    public boolean isValid(PartyBean partyBean, ConstraintValidatorContext context) {
        if(partyBean == null){
            return true;
        }
        if(partyBean.getIdTypeCode() != null && (partyBean.getGenderType() == null || 
                partyBean.getGenderCode().length()<1)){
            return false;
        } else {
            return true;
        }
    }
    
}