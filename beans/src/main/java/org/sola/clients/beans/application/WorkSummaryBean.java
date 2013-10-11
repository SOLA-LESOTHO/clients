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
 * Bean containing work summary information for the Lodgement Report
 *
 */
public class WorkSummaryBean extends AbstractBindingBean {

    private String serviceType;
    private String serviceCategory;
    private int inProgressFrom;
    private int onRequisitionFrom;
    private int lodged;
    private int requisitioned;
    private int registered;
    private int cancelled;
    private int withdrawn;
    private int inProgressTo;
    private int onRequisitionTo;
    private int overdue;
    private String overdueApplications;
    private String onRequisitionApplications;

    public WorkSummaryBean() {
        super();
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public int getLodged() {
        return lodged;
    }

    public void setLodged(int lodged) {
        this.lodged = lodged;
    }

    public int getOverdue() {
        return overdue;
    }

    public void setOverdue(int overdue) {
        this.overdue = overdue;
    }

    public String getOverdueApplications() {
        return overdueApplications;
    }

    public void setOverdueApplications(String overdueApplications) {
        this.overdueApplications = overdueApplications;
    }

    public int getRegistered() {
        return registered;
    }

    public void setRegistered(int registered) {
        this.registered = registered;
    }

    public int getRequisitioned() {
        return requisitioned;
    }

    public void setRequisitioned(int requisitioned) {
        this.requisitioned = requisitioned;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }

    public void setServiceCategory(String serviceCategory) {
        this.serviceCategory = serviceCategory;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getWithdrawn() {
        return withdrawn;
    }

    public void setWithdrawn(int withdrawn) {
        this.withdrawn = withdrawn;
    }

    public String getOnRequisitionApplications() {
        return onRequisitionApplications;
    }

    public void setOnRequisitionApplications(String onRequisitionApplications) {
        this.onRequisitionApplications = onRequisitionApplications;
    }

    public int getInProgressFrom() {
        return inProgressFrom;
    }

    public void setInProgressFrom(int inProgressFrom) {
        this.inProgressFrom = inProgressFrom;
    }

    public int getInProgressTo() {
        return inProgressTo;
    }

    public void setInProgressTo(int inProgressTo) {
        this.inProgressTo = inProgressTo;
    }

    public int getOnRequisitionFrom() {
        return onRequisitionFrom;
    }

    public void setOnRequisitionFrom(int onRequisitionFrom) {
        this.onRequisitionFrom = onRequisitionFrom;
    }

    public int getOnRequisitionTo() {
        return onRequisitionTo;
    }

    public void setOnRequisitionTo(int onRequisitionTo) {
        this.onRequisitionTo = onRequisitionTo;
    }

    /**
     * Returns collection of {@link WorkSummaryBean} objects. This method is
     * used by Jasper report designer to extract properties of application bean
     * to help design a report.
     */
    public static java.util.Collection generateCollection() {
        java.util.Vector collection = new java.util.Vector();
        WorkSummaryBean bean = new WorkSummaryBean();
        collection.add(bean);
        return collection;
    }
}
