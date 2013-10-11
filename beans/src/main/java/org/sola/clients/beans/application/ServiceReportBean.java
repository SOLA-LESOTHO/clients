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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractIdBean;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.webservices.transferobjects.casemanagement.LodgementViewParamsTO;
import org.sola.webservices.transferobjects.casemanagement.LodgementViewTO;
import org.sola.webservices.transferobjects.casemanagement.WorkSummaryTO;
import org.sola.services.boundary.wsclients.WSManager;

/**
 * Contains summary properties of the LodgementView object. Could be populated
 * from the {@link LodgementViewTO} object.<br /> For more information see UC
 * <b>Lodgement Report</b> schema.
 */
public class ServiceReportBean extends AbstractIdBean {

    private ObservableList<WorkSummaryBean> workSummaryList;
    private ObservableList<WorkSummaryBean> overdueList;
    private ObservableList<WorkSummaryBean> requisitionList;
    private Map<String, Integer> totals = new HashMap<String, Integer>();
    public static final String LODGED_SUBCATEGORY = ".lodged";
    public static final String REGISTERED_SUBCATEGORY = ".registered";
    public static final String CANCELLED_SUBCATEGORY = ".cancelled";
    public static final String REQUISITIONED_SUBCATEGORY = ".requisitioned";
    public static final String WITHDRAWN_SUBCATEGORY = ".withdrawn";
    public static final String OVERDUE_SUBCATEGORY = ".overdue";
    public static final String IN_PROGRESS_FROM_SUBCATEGORY = ".inProgressFrom";
    public static final String ON_REQUISITION_FROM_SUBCATEGORY = ".onRequisitionFrom";
    public static final String IN_PROGRESS_TO_SUBCATEGORY = ".inProgressTo";
    public static final String ON_REQUISITION_TO_SUBCATEGORY = ".onRequisitionTo";

    public ServiceReportBean() {
        workSummaryList = ObservableCollections.observableList(new LinkedList<WorkSummaryBean>());
        overdueList = ObservableCollections.observableList(new LinkedList<WorkSummaryBean>());
        requisitionList = ObservableCollections.observableList(new LinkedList<WorkSummaryBean>());
    }

    /**
     * Returns collection of {@link ApplicationBean} objects. This method is
     * used by Jasper report designer to extract properties of application bean
     * to help design a report.
     */
    public static java.util.Collection generateCollection() {
        java.util.Vector collection = new java.util.Vector();
        LodgementBean bean = new LodgementBean();
        collection.add(bean);
        return collection;
    }

    /**
     * Retrieve the list of WorkSummary records and add them to the
     * LodgementBean
     *
     * @param params
     */
    public void loadWorkSummary(LodgementViewParamsBean params) {
        LodgementViewParamsTO paramsTO = TypeConverters.BeanToTrasferObject(params,
                LodgementViewParamsTO.class);
        List<WorkSummaryTO> workSummaryTOList =
                WSManager.getInstance().getCaseManagementService().getWorkSummary(paramsTO);

        TypeConverters.TransferObjectListToBeanList(workSummaryTOList,
                WorkSummaryBean.class, (List) workSummaryList);

        for (WorkSummaryBean bean : workSummaryList) {
            if (bean.getOverdue() > 0) {
                overdueList.add(bean);
            }
            if (bean.getOnRequisitionTo() > 0) {
                requisitionList.add(bean);
            }
            // Get the summary totals by service category. Need to add these
            // manually because JasperReports can't do the job properly. 
            String category = bean.getServiceCategory() + LODGED_SUBCATEGORY;
            totals.put(category, getCategoryTotal(category) + bean.getLodged());
            category = bean.getServiceCategory() + REGISTERED_SUBCATEGORY;
            totals.put(category, getCategoryTotal(category) + bean.getRegistered());
            category = bean.getServiceCategory() + CANCELLED_SUBCATEGORY;
            totals.put(category, getCategoryTotal(category) + bean.getCancelled());
            category = bean.getServiceCategory() + WITHDRAWN_SUBCATEGORY;
            totals.put(category, getCategoryTotal(category) + bean.getWithdrawn());
            category = bean.getServiceCategory() + REQUISITIONED_SUBCATEGORY;
            totals.put(category, getCategoryTotal(category) + bean.getRequisitioned());
            category = bean.getServiceCategory() + OVERDUE_SUBCATEGORY;
            totals.put(category, getCategoryTotal(category) + bean.getOverdue());
            category = bean.getServiceCategory() + IN_PROGRESS_FROM_SUBCATEGORY;
            totals.put(category, getCategoryTotal(category) + bean.getInProgressFrom());
            category = bean.getServiceCategory() + IN_PROGRESS_TO_SUBCATEGORY;
            totals.put(category, getCategoryTotal(category) + bean.getInProgressTo());
            category = bean.getServiceCategory() + ON_REQUISITION_FROM_SUBCATEGORY;
            totals.put(category, getCategoryTotal(category) + bean.getOnRequisitionFrom());
            category = bean.getServiceCategory() + ON_REQUISITION_TO_SUBCATEGORY;
            totals.put(category, getCategoryTotal(category) + bean.getOnRequisitionTo());
        }
    }

    private int getCategoryTotal(String category) {
        int result = 0;
        if (totals.containsKey(category)) {
            result = totals.get(category);
        }
        return result;
    }

    public ObservableList<WorkSummaryBean> getWorkSummaryList() {
        return workSummaryList;
    }

    public void setWorkSummaryList(ObservableList<WorkSummaryBean> workSummaryList) {
        this.workSummaryList = workSummaryList;
    }

    public ObservableList<WorkSummaryBean> getOverdueList() {
        return overdueList;
    }

    public void setOverdueList(ObservableList<WorkSummaryBean> overdueList) {
        this.overdueList = overdueList;
    }

    public ObservableList<WorkSummaryBean> getRequisitionList() {
        return requisitionList;
    }

    public void setRequisitionList(ObservableList<WorkSummaryBean> requisitionList) {
        this.requisitionList = requisitionList;
    }

    public Map<String, Integer> getTotals() {
        return totals;
    }

    public void setTotals(Map<String, Integer> totals) {
        this.totals = totals;
    }
}
