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
package org.sola.clients.beans.administrative.validation;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.sola.clients.beans.administrative.DisputeBean;
import org.sola.clients.beans.administrative.validation.DisputeCheck;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;

public class DisputeValidator implements ConstraintValidator<DisputeCheck, DisputeBean> {

    @Override
    public void initialize(DisputeCheck constraintAnnotation) {
    }

    @Override
    public boolean isValid(DisputeBean disputeBean, ConstraintValidatorContext context) {
        if (disputeBean == null) {
            return true;
        }

        boolean result = true;
        context.disableDefaultConstraintViolation();

        if (disputeBean.getCaseType() == null) {
            result = false;

            context.buildConstraintViolationWithTemplate(
                    MessageUtility.getLocalizedMessageText(
                    ClientMessage.DISPUTE_CHOOSE_RIGHT_FUNCTIONALITY)).addConstraintViolation();
        }
        
        if (disputeBean.getCaseType() != null) {
            
            if (disputeBean.getCaseType().equals("Dispute")) {
                if (disputeBean.getDisputeCategory().getCode()== (null)) {

                    result = false;

                    context.buildConstraintViolationWithTemplate(
                            MessageUtility.getLocalizedMessageText(
                            ClientMessage.DISPUTE_CAPTURE_CATEGORY_OR_TYPE_FIRST)).addConstraintViolation();

                } else if (disputeBean.getDisputeType().getCode() == null) {

                    result = false;

                    context.buildConstraintViolationWithTemplate(
                            MessageUtility.getLocalizedMessageText(
                            ClientMessage.DISPUTE_CAPTURE_CATEGORY_OR_TYPE_FIRST)).addConstraintViolation();
                    
                } else if (disputeBean.getPlotLocation() == null){
                    
                    result = false;

                    context.buildConstraintViolationWithTemplate(
                            MessageUtility.getLocalizedMessageText(
                            ClientMessage.DISPUTE_LOCATION_REQUIRED)).addConstraintViolation();
                } else if (disputeBean.getDisputeDescription() == null) {
                    
                    result = false;

                    context.buildConstraintViolationWithTemplate(
                            MessageUtility.getLocalizedMessageText(
                            ClientMessage.DISPUTE_DESCRIPTION)).addConstraintViolation();
                    
                }

            } else if (disputeBean.getCaseType().equals("Court Process")){
                if (disputeBean.getActionRequired() == null){

                    result = false;

                    context.buildConstraintViolationWithTemplate(
                            MessageUtility.getLocalizedMessageText(
                            ClientMessage.DISPUTE_COURT_ACTION_REQUIRED)).addConstraintViolation(); 
                }
            }
        }

        return result;
    }
}
