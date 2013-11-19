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

import java.util.LinkedList;
import java.util.List;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractIdBean;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.webservices.transferobjects.casemanagement.LodgementViewParamsTO;
import org.sola.webservices.transferobjects.casemanagement.StatisticsSummaryTO;
import org.sola.services.boundary.wsclients.WSManager;

/**
 *
 * @author ntsane
 */
public class StatisticalReportBean  extends AbstractIdBean {
    
    private ObservableList<StatisticsSummaryBean> statisticsSummaryList;

    public StatisticalReportBean() {
         statisticsSummaryList = ObservableCollections.observableList(new LinkedList<StatisticsSummaryBean>());
    }
    
    public void loadStatisticSummary(LodgementViewParamsBean params){
                LodgementViewParamsTO paramsTO = TypeConverters.BeanToTrasferObject(params,
                LodgementViewParamsTO.class);
        List<StatisticsSummaryTO> statisticsSummaryTOList =
                WSManager.getInstance().getCaseManagementService().getStatisticsSummary(paramsTO);

        TypeConverters.TransferObjectListToBeanList(statisticsSummaryTOList,
                StatisticsSummaryBean.class, (List) statisticsSummaryList);
    }
    
    public ObservableList<StatisticsSummaryBean> getStatisticsSummaryList() {
        return statisticsSummaryList;
    }

    public void setStatisticsSummaryList(ObservableList<StatisticsSummaryBean> statisticsSummaryList) {
        this.statisticsSummaryList = statisticsSummaryList;
    }
    
    public static java.util.Collection generateCollection() {
        java.util.Vector collection = new java.util.Vector();
        StatisticalReportBean bean = new StatisticalReportBean();
        collection.add(bean);
        return collection;
    }
}
