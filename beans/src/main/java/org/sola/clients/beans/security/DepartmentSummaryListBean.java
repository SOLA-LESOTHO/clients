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

import java.util.List;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;

/**
 *
 * @author Charlizza
 */
public class DepartmentSummaryListBean extends AbstractBindingBean {
    public static final String SELECTED_DEPARTMENT_PROPERTY = "selectedDepartment";
    
    private SolaObservableList<DepartmentSummaryBean> departmentSummaryList;
    private DepartmentSummaryBean selectedDepartment;
    
    public DepartmentSummaryListBean() {
        super();
        departmentSummaryList = new SolaObservableList<DepartmentSummaryBean>();
        int size = departmentSummaryList.size();
    }

    public final void loadDepartments(boolean createDummyDepartment){
        departmentSummaryList.clear();
        if (WSManager.getInstance() != null && WSManager.getInstance().getAdminService() != null) {
            TypeConverters.TransferObjectListToBeanList(WSManager.getInstance()
                    .getAdminService().getDepartmentsSummary(), 
                    DepartmentSummaryBean.class, (List)departmentSummaryList);
        }
        if(createDummyDepartment){
            DepartmentSummaryBean department = new DepartmentSummaryBean();
            department.setId(null);
            departmentSummaryList.add(0, department);
        }
    }
    
    public ObservableList<DepartmentSummaryBean> getDepartmentSummaryList() {
        return departmentSummaryList;
    }

    public DepartmentSummaryBean getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(DepartmentSummaryBean selectedDepartment) {
        DepartmentSummaryBean oldValue = this.selectedDepartment;
        this.selectedDepartment = selectedDepartment;
        propertySupport.firePropertyChange(SELECTED_DEPARTMENT_PROPERTY, oldValue, this.selectedDepartment);
    }
}
