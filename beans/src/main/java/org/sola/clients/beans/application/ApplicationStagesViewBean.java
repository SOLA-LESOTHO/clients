/*
 * Copyright 2014 Food and Agriculture Organization of the United Nations (FAO).
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

import org.sola.clients.beans.AbstractIdBean;

/**
 *
 * @author Charlizza
 */
public class ApplicationStagesViewBean extends AbstractIdBean{
    
    private String application;
    
    private Integer appLodged;
    
    private Integer toBeProcessed;
    
    private Integer missingPlot;
    
    private Integer areaMismatch;
    
    private Integer queried;
    
    private Integer bindDraft;
    
    private Integer checkDraft;
    
    private Integer logDraft;
    
    private Integer executiveToSign;
    
    private Integer awaitingApproval;
    
    private Integer customerToSign;
    
    private Integer toBeArchived;
    
    private Integer callCustomer;
    
    private Integer collectedByCustomer;
    
    private Integer toBeRegistered;

    public ApplicationStagesViewBean() {
        super();
    }

    public Integer getAppLodged() {
        if (appLodged == null)
            return 0;
        return appLodged;
    }

    public void setAppLodged(Integer appLodged) {
        this.appLodged = appLodged;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Integer getAreaMismatch() {
        if (areaMismatch == null)
            return 0;
        return areaMismatch;
    }

    public void setAreaMismatch(Integer areaMismatch) {
        this.areaMismatch = areaMismatch;
    }

    public Integer getAwaitingApproval() {
        if (awaitingApproval == null)
            return 0;
        return awaitingApproval;
    }

    public void setAwaitingApproval(Integer awaitingApproval) {
        this.awaitingApproval = awaitingApproval;
    }

    public Integer getBindDraft() {
        if (bindDraft == null)
            return 0;
        return bindDraft;
    }

    public void setBindDraft(Integer bindDraft) {
        this.bindDraft = bindDraft;
    }

    public Integer getCallCustomer() {
        if (callCustomer == null)
            return 0;
        return callCustomer;
    }

    public void setCallCustomer(Integer callCustomer) {
        this.callCustomer = callCustomer;
    }

    public Integer getCheckDraft() {
        if (checkDraft == null)
            return 0;
        return checkDraft;
    }

    public void setCheckDraft(Integer checkDraft) {
        this.checkDraft = checkDraft;
    }

    public Integer getCollectedByCustomer() {
        if (collectedByCustomer == null)
            return 0;
        return collectedByCustomer;
    }

    public void setCollectedByCustomer(Integer collectedByCustomer) {
        this.collectedByCustomer = collectedByCustomer;
    }

    public Integer getCustomerToSign() {
        if (customerToSign == null)
            return 0;
        return customerToSign;
    }

    public void setCustomerToSign(Integer customerToSign) {
        this.customerToSign = customerToSign;
    }

    public Integer getExecutiveToSign() {
        if (executiveToSign == null)
            return 0;
        return executiveToSign;
    }

    public void setExecutiveToSign(Integer executiveToSign) {
        this.executiveToSign = executiveToSign;
    }

    public Integer getLogDraft() {
        if (logDraft == null)
            return 0;
        return logDraft;
    }

    public void setLogDraft(Integer logDraft) {
        this.logDraft = logDraft;
    }

    public Integer getMissingPlot() {
        if (missingPlot == null)
            return 0;
        return missingPlot;
    }

    public void setMissingPlot(Integer missingPlot) {
        this.missingPlot = missingPlot;
    }

    public Integer getQueried() {
        if (queried == null)
            return 0;
        return queried;
    }

    public void setQueried(Integer queried) {
        this.queried = queried;
    }

    public Integer getToBeArchived() {
        if (toBeArchived == null)
            return 0;
        return toBeArchived;
    }

    public void setToBeArchived(Integer toBeArchived) {
        this.toBeArchived = toBeArchived;
    }

    public Integer getToBeProcessed() {
        if (toBeProcessed == null)
            return 0;
        return toBeProcessed;
    }

    public void setToBeProcessed(Integer toBeProcessed) {
        this.toBeProcessed = toBeProcessed;
    }

    public Integer getToBeRegistered() {
        if (toBeRegistered == null)
            return 0;
        return toBeRegistered;
    }

    public void setToBeRegistered(Integer toBeRegistered) {
        this.toBeRegistered = toBeRegistered;
    }
     
}
