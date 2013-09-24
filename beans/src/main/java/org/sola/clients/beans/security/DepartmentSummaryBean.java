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

import org.hibernate.validator.constraints.NotEmpty;
import org.sola.clients.beans.AbstractIdBean;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.messaging.ClientMessage;

/**
 *
 * @author Charlizza
 */
public class DepartmentSummaryBean extends AbstractIdBean {
    public final static String NAME_PROPERTY = "name";
    public static final String DESCRIPTION_PROPERTY = "description";
    
    @NotEmpty(message= ClientMessage.CHECK_NOTNULL_DEPARTMENTNAME, payload=Localized.class)
    private String name;
    private String description;
    
    public DepartmentSummaryBean(){
        super();
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String oldValue = this.description;
        this.description = description;
        propertySupport.firePropertyChange(DESCRIPTION_PROPERTY, oldValue, this.description);
    }
    
        public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldValue = this.name;
        this.name = name;
        propertySupport.firePropertyChange(NAME_PROPERTY, oldValue, this.name);
    }
    
    @Override
    public String toString(){
        if(name!=null){
            return name;
        }else{
            return "";
        }
    }
}
