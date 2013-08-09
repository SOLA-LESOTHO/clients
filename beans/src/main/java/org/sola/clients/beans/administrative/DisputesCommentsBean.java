/**
 * ******************************************************************************************
 * Copyright (c) 2013 Food and Agriculture Organization of the United Nations (FAO)
 * and the Lesotho Land Administration Authority (LAA). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the names of FAO, the LAA nor the names of its contributors may be used to
 *       endorse or promote products derived from this software without specific prior
 * 	  written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,STRICT LIABILITY,OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
/**
 *
 * LAA Addition thoriso
 */
package org.sola.clients.beans.administrative;

import java.util.Date;
import org.sola.clients.beans.AbstractTransactionedBean;
import org.sola.clients.beans.administrative.validation.DisputeCommentsCheck;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.administrative.DisputeCommentsTO;
import org.sola.clients.beans.referencedata.DisputeActionBean;
import org.sola.clients.beans.referencedata.OtherAuthoritiesBean;

@DisputeCommentsCheck
public class DisputesCommentsBean extends DisputeBean {

    public static final String DISPUTE_NR_PROPERTY = "disputeNr";
    public static final String UPDATE_DATE_PROPERTY = "updateDate";
    public static final String DISPUTE_ACTION_PROPERTY = "disputeAction";
    public static final String OTHER_AUTHORITIES_PROPERTY = "otherAuthorities";
    public static final String COMMENTS = "commnets";
    public static final String UPDATED_BY = "updatedBy";
    public static final String SELECTED_AUTHORITY_PROPERTY = "selectedAuthority";
    public static final String SELECTED_COMMENTS_PROPERTY = "selectedComments";
    
    
    private String disputeNr;
    private Date updateDate;
    private DisputeActionBean disputeActionCode;
    private OtherAuthoritiesBean otherAuthoritiesCode;
    private String comments;
    private String updatedBy;
    private DisputeBean disputeBean;

    public DisputesCommentsBean() {
        super();
    }

    public void clean() {
        this.setDisputeNr(null);
        this.setUpdateDate(null);
        this.setDisputeActionCode(null);
        this.setOtherAuthoritiesCode(null);
        this.setComments(null);
        this.setUpdatedBy(null);
        
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String value) {
        String old = comments;
        comments = value;
        propertySupport.firePropertyChange(COMMENTS, old, comments);
    }

    public String getDisputeNr() {
        return disputeNr;
    }

    public void setDisputeNr(String value) {
            String old = disputeNr;
            disputeNr = value;
            propertySupport.firePropertyChange(DISPUTE_NR_PROPERTY, old, disputeNr);
       
    }

    public String getDisputeActionCode() {
        if (disputeActionCode != null) {
            return disputeActionCode.getCode();
        } else {
            return null;
        }
    }

    public void setDisputeActionCode(String disputeActionCode) {
        String oldValue = null;
        if (this.disputeActionCode != null) {
            oldValue = this.disputeActionCode.getCode();
            return;
        }
        setDisputeAction(CacheManager.getBeanByCode(
                CacheManager.getDisputeAction(), disputeActionCode));

        propertySupport.firePropertyChange(DISPUTE_ACTION_PROPERTY,
                oldValue, disputeActionCode);
    }

    public DisputeActionBean getDisputeAction() {
        if (this.disputeActionCode == null) {
            this.disputeActionCode = new DisputeActionBean();
        }
        return disputeActionCode;
    }

    public void setDisputeAction(DisputeActionBean disputeAction) {
        if (this.disputeActionCode == null) {
            this.disputeActionCode = new DisputeActionBean();
        }
        this.setJointRefDataBean(this.disputeActionCode, disputeAction, DISPUTE_ACTION_PROPERTY);
    }

    public String getOtherAuthoritiesCode() {
        if (this.otherAuthoritiesCode != null) {
            return this.otherAuthoritiesCode.getCode();
        } else {
            return null;
        }
    }

    public void setOtherAuthoritiesCode(String otherAuthoritiesCode) {
        String oldValue = null;
        if (this.otherAuthoritiesCode != null) {
            oldValue = this.otherAuthoritiesCode.getCode();
            return;
        }
        setOtherAuthorities(CacheManager.getBeanByCode(
                CacheManager.getOtherAuthorities(), otherAuthoritiesCode));

        propertySupport.firePropertyChange(OTHER_AUTHORITIES_PROPERTY,
                oldValue, otherAuthoritiesCode);
    }

    public OtherAuthoritiesBean getOtherAuthorities() {
        if (this.otherAuthoritiesCode == null) {
            this.otherAuthoritiesCode = new OtherAuthoritiesBean();
        }
        return otherAuthoritiesCode;
    }

    public void setOtherAuthorities(OtherAuthoritiesBean otherAuthorities) {
        if (this.otherAuthoritiesCode == null) {
            this.otherAuthoritiesCode = new OtherAuthoritiesBean();
        }
        this.setJointRefDataBean(this.otherAuthoritiesCode, otherAuthorities, OTHER_AUTHORITIES_PROPERTY);
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date value) {
        Date old = updateDate;
        updateDate = value;
        propertySupport.firePropertyChange(UPDATE_DATE_PROPERTY, old, updateDate);
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String value) {
        String old = updatedBy;
        updatedBy = value;
        propertySupport.firePropertyChange(UPDATED_BY, old, updatedBy);
    }

    public boolean saveDisputeComments() {
        DisputeCommentsTO disputeComments = TypeConverters.BeanToTrasferObject(this, DisputeCommentsTO.class);
        disputeComments = WSManager.getInstance().getAdministrative().saveDisputeComments(disputeComments);
        TypeConverters.TransferObjectToBean(disputeComments, DisputesCommentsBean.class, this);
        return true;
    }

    public static DisputesCommentsBean getDisputeCommentsById(String disputeCommentsId) {
        return TypeConverters.TransferObjectToBean(
                WSManager.getInstance().getAdministrative().getDisputeCommentsById(disputeCommentsId),
                DisputesCommentsBean.class, null);
    }
}
