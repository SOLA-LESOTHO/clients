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
package org.sola.clients.beans.referencedata;

import org.sola.clients.beans.security.*;
import org.hibernate.validator.constraints.NotEmpty;
import org.sola.clients.beans.AbstractIdBean;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.messaging.ClientMessage;

/** 
 * Represents summary object of <b>applicationStage</b>. 
 * Could be populated from the {@link ApplicationStageSummaryTO} object.<br />
 * For more information on the properties of the <code>applicationStage</code> 
 * see data dictionary <b>System</b> schema.
 */
public class ApplicationStageSummaryBean extends AbstractIdBean {

    public final static String DESCRIPTION_PROPERTY = "description";
    public final static String CODE_PROPERTY = "code";
    public final static String DISPLAY_VALUE_PROPERTY = "displayValue";
    public final static String DEPARTMENT_PROPERTY = "department";
    public final static String GROUP_LIST_PROPERTY = "groupList";
    
    private String description;
    @NotEmpty(message= ClientMessage.CHECK_NOTNULL_CODE, payload=Localized.class)
    private String code;
    @NotEmpty(message= ClientMessage.CHECK_NOTNULL_DISPLAYVALUE, payload=Localized.class)
    private String displayValue;
    
    private String department;
    private String groupList;

    public ApplicationStageSummaryBean() {
        super();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String value) {
        String oldValue = code;
        code = value;
        propertySupport.firePropertyChange(CODE_PROPERTY, oldValue, value);
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String value) {
        String oldValue = displayValue;
        displayValue = value;
        propertySupport.firePropertyChange(DISPLAY_VALUE_PROPERTY, oldValue, value);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        String oldValue = description;
        description = value;
        propertySupport.firePropertyChange(DESCRIPTION_PROPERTY, oldValue, value);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String value) {
        String oldValue = department;
        department = value;
        propertySupport.firePropertyChange(DEPARTMENT_PROPERTY, oldValue, value);
    }

    public String getGroupList() {
        return groupList;
    }

    public void setGroupList(String value) {
        String oldValue = groupList;
        groupList = value;
        propertySupport.firePropertyChange(GROUP_LIST_PROPERTY, oldValue, value);
    }
    
    
}
