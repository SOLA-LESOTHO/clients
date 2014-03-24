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
public class LeaseServicesViewBean extends AbstractIdBean{
    
    private String application;
   
    private Integer tobeprocessed;
    
    private Integer inprogress;
   
    private Integer queried;
   
    private Integer cancelled;
    
    private Integer processed;
   
    private Integer approved;
    
    private Integer overdue;

    public LeaseServicesViewBean() {
        
        super();
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    public void setCancelled(Integer cancelled) {
        this.cancelled = cancelled;
    }

    public void setInprogress(Integer in_progress) {
        this.inprogress = in_progress;
    }

    public void setOverdue(Integer overdue) {
        this.overdue = overdue;
    }

    public void setProcessed(Integer processed) {
        this.processed = processed;
    }

    public void setQueried(Integer queried) {
        this.queried = queried;
    }

    public void setTobeprocessed(Integer to_be_processed) {
        this.tobeprocessed = to_be_processed;
    }
   
    public String getApplication() {
        return application;
    }

    public Integer getApproved() {
        
        if ( approved == null)
            return 0;
        return approved;
    }

    public Integer getCancelled() {
        
        if ( cancelled == null)
            return 0;
        return cancelled;
    }

    public Integer getInprogress() {
        if ( inprogress == null)
            return 0;
        return inprogress;
    }

    public Integer getOverdue() {
        
        if ( overdue == null)
            return 0;
        return overdue;
    }

    public Integer getProcessed() {
        
        if ( processed == null)
            return 0;
        return processed;
    }

    public Integer getQueried() {
        
        if ( queried == null)
            return 0;
        return queried;
    }

    public Integer getTobeprocessed() {
        
        if ( tobeprocessed == null)
            return 0;
        return tobeprocessed;
    }
   
}
