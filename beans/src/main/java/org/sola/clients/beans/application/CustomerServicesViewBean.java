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
public class CustomerServicesViewBean extends AbstractIdBean{
    
    private String application;
    
    private Integer lodged;
    
    private Integer queried;
    
    private Integer awaitingcollection;
    
    private Integer collected;

    public CustomerServicesViewBean() {
        super();
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public Integer getAwaitingcollection() {
        if( awaitingcollection == null)
            return 0;
        return awaitingcollection;
    }

    public void setAwaitingcollection(Integer awaiting_collection) {
        this.awaitingcollection = awaiting_collection;
    }

    public Integer getCollected() {
        if( collected == null)
            return 0;
        return collected;
    }

    public void setCollected(Integer collected) {
        this.collected = collected;
    }

    public Integer getLodged() {
        if( lodged == null)
            return 0;
        return lodged;
    }

    public void setLodged(Integer lodged) {
        this.lodged = lodged;
    }

    public Integer getQueried() {
        if( queried == null)
            return 0;
        return queried;
    }

    public void setQueried(Integer queried) {
        this.queried = queried;
    } 
    
    
}
