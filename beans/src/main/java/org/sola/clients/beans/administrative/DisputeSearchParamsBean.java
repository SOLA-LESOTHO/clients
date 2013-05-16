
/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations
 * (FAO). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the name of FAO nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 * package org.sola.clients.beans.administrative;
 *
 * /**
 *
 * LAA Addition thoriso
 */
import java.util.Date;
import org.sola.clients.beans.AbstractBindingBean;

public class DisputeSearchParamsBean extends AbstractBindingBean {

    public static final String DISP_NR_PROPERTY = "nr";
    public static final String LEASE_NR_PROPERTY = "leaseNr";
    public static final String PLOT_NR_PROPERTY = "plotNr";
    public static final String LODGEMENT_DATE_FROM_PROPERTY = "lodgeDateFrom";
    public static final String LODGEMENT_DATE_TO_PROPERTY = "lodgementDateTo";
    public static final String COMPLETION_DATE_FROM_PROPERTY = "completionDateFrom";
    public static final String COMPLETION_DATE_TO_PROPERTY = "completionDateTo";
    
    
    private String nr;
    private Date lodgementDateFrom;
    private Date lodgementDateTo;
    private Date completionDateFrom;
    private Date completionDateTo;
    private String leaseNumber;
    private String plotNumber;

    public Date getCompletionDateFrom() {
        return completionDateFrom;
    }

    /*
     * Date old = this.fromRecordationDate; this.fromRecordationDate =
     * fromRecordationDate;
     * propertySupport.firePropertyChange(FROM_RECORDATION_DATE_PROPERTY, old,
     * this.fromRecordationDate)
     */
    public void setCompletionDateFrom(Date completionDateFrom) {
        Date old = this.completionDateFrom;
        this.completionDateFrom = completionDateFrom;
        propertySupport.firePropertyChange(COMPLETION_DATE_FROM_PROPERTY, old, this.completionDateFrom);
    }

    public Date getCompletionDateTo() {
        return completionDateTo;
    }

    public void setCompletionDateTo(Date completionDateTo) {
        Date old = this.completionDateTo;
        this.completionDateTo = completionDateTo;
        propertySupport.firePropertyChange(COMPLETION_DATE_TO_PROPERTY, old, this.completionDateTo);
    }

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String leaseNumber) {
        String old = this.leaseNumber;
        this.leaseNumber = leaseNumber;
        propertySupport.firePropertyChange(LEASE_NR_PROPERTY, old, this.leaseNumber);
    }

    public Date getLodgementDateFrom() {
        return lodgementDateFrom;
    }

    public void setLodgementDateFrom(Date lodgementDateFrom) {
        Date old = this.lodgementDateFrom;
        this.lodgementDateFrom = lodgementDateFrom;
        propertySupport.firePropertyChange(LODGEMENT_DATE_FROM_PROPERTY, old, this.lodgementDateFrom);
    }

    public Date getLodgementDateTo() {
        return lodgementDateTo;
    }

    public void setLodgementDateTo(Date lodgementDateTo) {
        Date old = this.lodgementDateTo;
        this.lodgementDateTo = lodgementDateTo;
        propertySupport.firePropertyChange(LODGEMENT_DATE_TO_PROPERTY, old, this.lodgementDateTo);
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        String old = this.nr;
        this.nr = nr;
        propertySupport.firePropertyChange(DISP_NR_PROPERTY, old, this.nr);
    }

    public String getPlotNumber() {
        return plotNumber;
    }

    public void setPlotNumber(String plotNumber) {
        String old = this.plotNumber;
        this.plotNumber = plotNumber;
        propertySupport.firePropertyChange(PLOT_NR_PROPERTY, old, this.plotNumber);
    }
}
