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
package org.sola.clients.beans.application;

import org.sola.clients.beans.AbstractBindingBean;

/**
 *
 * @author ntsane
 */
public class StatisticsSummaryBean extends AbstractBindingBean{

    public StatisticsSummaryBean(){
        super();
    }
    
    private String requestType;
    private String requestCategory;
    private int groupIndex;
    private int lodgedApplications;
    private int cancelledApplications;
    private int completedApplications;
    
    
    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestCategory() {
        return requestCategory;
    }

    public void setRequestCategory(String requestCategory) {
        this.requestCategory = requestCategory;
    }

    public int getGroupIndex() {
        return groupIndex;
    }

    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    public int getLodgedApplications() {
        return lodgedApplications;
    }

    public void setLodgedApplications(int lodgedApplications) {
        this.lodgedApplications = lodgedApplications;
    }

    public int getCancelledApplications() {
        return cancelledApplications;
    }

    public void setCancelledApplications(int cancelledApplications) {
        this.cancelledApplications = cancelledApplications;
    }

    public int getCompletedApplications() {
        return completedApplications;
    }

    public void setCompletedApplications(int completedApplications) {
        this.completedApplications = completedApplications;
    }
    
    /**
     * Returns collection of {@link WorkSummaryBean} objects. This method is
     * used by Jasper report designer to extract properties of application bean
     * to help design a report.
     */
    public static java.util.Collection generateCollection() {
        java.util.Vector collection = new java.util.Vector();
        StatisticsSummaryBean bean = new StatisticsSummaryBean();
        collection.add(bean);
        return collection;
    }

}
