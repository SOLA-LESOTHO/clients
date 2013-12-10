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
package org.sola.clients.beans.application;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.referencedata.RequestCategoryTypeBean;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.messaging.ClientMessage;
import org.sola.webservices.transferobjects.casemanagement.LodgementViewParamsTO;

/** 
 * Contains properties used as the parameters to search applications.
 * Could be populated from the {@link ApplicationSearchParamsTO} object.<br />
 */

/**
 *
 * @author RizzoM
 */
public class LodgementViewParamsBean  extends AbstractBindingBean {
    
    public static final String FROM_DATE_PROPERTY = "fromDate";
    public static final String TO_DATE_PROPERTY = "toDate";
    public static final String REQUEST_CATEGORY_PROPERTY = "requestCategory";
    public static final String REQUEST_CATEGORY_CODE_PROPERTY = "requestCategoryCode";
    
  
//    @NotNull(message = ClientMessage.CHECK_NOTNULL_EXPIRATION, payload = Localized.class)
    private Date fromDate;
//    @NotNull(message = ClientMessage.CHECK_NOTNULL_EXPIRATION, payload = Localized.class)
    private Date toDate;
    
    private RequestCategoryTypeBean requestCategory;
    
    private String requestCategoryCode;
    
    public LodgementViewParamsBean() {
        super();
    }

    public RequestCategoryTypeBean getRequestCategory() {
        return requestCategory;
    }

    public void setRequestCategory(RequestCategoryTypeBean requestCategory) {
        if( requestCategory == null){
            setRequestCategoryCode(null);
        }
        else{
            RequestCategoryTypeBean old = this.requestCategory;
            this.requestCategory = requestCategory;
            setRequestCategoryCode(requestCategory.getCode());
            propertySupport.firePropertyChange(REQUEST_CATEGORY_PROPERTY, old, requestCategory);
        }     
    }

    public String getRequestCategoryCode() {
        return requestCategoryCode;
    }

    public void setRequestCategoryCode(String requestCategoryCode) {
        this.requestCategoryCode = requestCategoryCode;
    }
    
    public Date getFromDate() {
        return fromDate;
    }

//    @NotNull(message = ClientMessage.CHECK_NOTNULL_EXPIRATION, payload = Localized.class)
    public void setFromDate(Date value) {
        Date oldValue = fromDate;
        fromDate = value;
        propertySupport.firePropertyChange(FROM_DATE_PROPERTY, oldValue, value);
    }

    
    public Date getToDate() {
        return toDate;
    }

//    @NotNull(message = ClientMessage.CHECK_NOTNULL_EXPIRATION, payload = Localized.class)
    public void setToDate(Date value) {
        Date oldValue = toDate;
        toDate = value;
        propertySupport.firePropertyChange(TO_DATE_PROPERTY, oldValue, value);
    }
    
}



