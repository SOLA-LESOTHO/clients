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
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.webservices.transferobjects.search.ApplicationStageSearchParamsTO;

/**
 * Contains properties used as the parameters to search users.
 * Could be populated from the {@link ApplicationStageSearchParamsTO} object.<br />
 */
public class ApplicationStageSearchParamsBean extends AbstractBindingBean {
    
    public static final String GROUP_ID_PROPERTY = "groupId";
    public static final String DEPARTMENT_ID_PROPERTY = "departmentId";
    public static final String GROUP_BEAN_PROPERTY = "groupBean";
    public static final String DEPARTMENT_BEAN_PROPERTY = "deaprtmentBean";
    public static final String CODE_PROPERTY = "code";
    public static final String DISPLAY_VALUE_PROPERTY = "displayValue";
    
    private String groupId;
    private String departmentId;
    private String code;
    private String displayValue;  
    private GroupSummaryBean groupBean;
    private DepartmentSummaryBean departmentBean;
    
    public ApplicationStageSearchParamsBean(){
        super();
    }

    public DepartmentSummaryBean getDepartmentBean() {
        return departmentBean;
    }

    public void setDepartmentBean(DepartmentSummaryBean departmentBean) {
        this.departmentBean = departmentBean;
        propertySupport.firePropertyChange(DEPARTMENT_BEAN_PROPERTY, null, this.departmentBean);
        if(departmentBean!=null){
            setDepartmentId(departmentBean.getId());
        }else{
            setDepartmentId(null);
        }
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        String oldValue = this.departmentId;
        this.departmentId = departmentId;
        propertySupport.firePropertyChange(DEPARTMENT_ID_PROPERTY, oldValue, this.departmentId);
    }    

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        String oldValue = this.groupId;
        this.groupId = groupId;
        propertySupport.firePropertyChange(GROUP_ID_PROPERTY, oldValue, this.groupId);
    }


    public GroupSummaryBean getGroupBean() {
        return groupBean;
    }

    public void setGroupBean(GroupSummaryBean groupBean) {
        this.groupBean = groupBean;
        propertySupport.firePropertyChange(GROUP_BEAN_PROPERTY, null, this.groupBean);
        if(groupBean!=null){
            setGroupId(groupBean.getId());
        }else{
            setGroupId(null);
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        String oldValue = this.code;
        this.code = code;
        propertySupport.firePropertyChange(CODE_PROPERTY, oldValue, this.code);
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        String oldValue = this.displayValue;
        this.displayValue = displayValue;
        propertySupport.firePropertyChange(DISPLAY_VALUE_PROPERTY, oldValue, this.code);
    }
    
    
}
