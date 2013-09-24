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
package org.sola.clients.beans.security;

import javax.validation.constraints.Size;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.messaging.ClientMessage;
import org.sola.webservices.transferobjects.security.DepartmentTO;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.EntityAction;

/**
 *
 * @author Charlizza
 */
public class DepartmentBean extends DepartmentSummaryBean {
    
    public static final String DEPARTMENT_USERS_PROPERTY = "departmentUsers";
    
    
    
    private SolaList<UserBean> departmentUsers;
    
    public DepartmentBean(){
        super();
        departmentUsers=new SolaList<UserBean>();
    }

    public SolaList<UserBean> getDepartmentUsers() {
        return departmentUsers;
    }

    public void setDepartmentUsers(SolaList<UserBean> departmentUsers) {
        this.departmentUsers = departmentUsers;
        propertySupport.firePropertyChange(DEPARTMENT_USERS_PROPERTY, null, this.departmentUsers);
    }
    
    /** Saves department. */
    public void save(){
        DepartmentTO departmentTO = TypeConverters.BeanToTrasferObject(this, DepartmentTO.class);
        departmentTO = WSManager.getInstance().getAdminService().saveDepartment(departmentTO);
        TypeConverters.TransferObjectToBean(departmentTO, DepartmentBean.class, this);
    }
    
    /** Returns department by the given ID */
    public static DepartmentBean getDepartment(String departmentId){
        return TypeConverters.TransferObjectToBean(WSManager.getInstance()
                .getAdminService().getDepartment(departmentId), DepartmentBean.class, null);
    }
    
    /** Removes department */
    public static void removeDepartment(String departmentId){
        DepartmentTO departmentTO = WSManager.getInstance().getAdminService().getDepartment(departmentId);
        departmentTO.setEntityAction(EntityAction.DELETE);
        WSManager.getInstance().getAdminService().saveDepartment(departmentTO);
    }
}
