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
 */
/**
 *
 * LAA Addition thoriso
 */
package org.sola.clients.beans.administrative;

import java.util.Date;
import java.util.UUID;
import org.sola.clients.beans.AbstractTransactionedBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.beans.referencedata.TypeActionBean;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.administrative.DisputeTO;

/**
 *
 * LAA Additions thoriso
 */
public class DisputeBean extends AbstractTransactionedBean {

    public static final String NR_PROPERTY = "nr";
    public static final String LODGEMENT_DATE_PROPERTY = "lodgementDate";
    public static final String COMPLETION_DATE_PROPERTY = "completiondate";
    public static final String DISPUTE_CATEGORY_PROPERTY = "disputeCategory";
    public static final String DISPUTE_TYPE_PROPERTY = "disputeType";
    public static final String DISPUTE_STATUS_CODE_PROPERTY = "disputeStatusCode";
    public static final String APPLICATION_ID_PROPERTY = "applicationId";
    public static final String RRR_ID_PROPERTY = "rrrId";
    public static final String PLOT_LOCATION_PROPERTY = "plotLocation";
    public static final String CADASTRE_OBJECT_ID_PROPERTY = "cadastreObjectId";
    public static final String PENDING_ACTION_CODE_PROPERTY = "pendingActionCode";
    public static final String PENDING_ACTION_PROPERTY = "pendingTypeAction";
    public static final String SELECTED_CATEGORY_PROPERTY = "selectedCategory";
    public static final String SELECTED_TYPE_PROPERTY = "selectedType";
    public static final String SELECTED_ACTION_PROPERTY = "selectedAction";
    public static final String SELECTED_AUTHORITY_PROPERTY = "selectedAuthority";
    public static final String USER_ID_PROPERTY = "userid";
    
    private String nr;
    private Date lodgementDate;
    private Date completiondate;
    private String disputeCategory;
    private String disputeType;
    private String disputeStatusCode;
    private String applicationId;
    private String rrrId;
    private String plotLocation;
    private String cadastreObjectId;
    private TypeActionBean pendingTypeAction;
    private String userId;

    public DisputeBean() {
        super();
    }

    public void clean() {
        //this.setId(UUID.randomUUID().toString());
        this.setApplicationId(null);
        this.setCadastreObjectId(null);
        this.setLodgementDate(null);
        this.setCompletiondate(null);
        this.setDisputeCategory(null);
        this.setDisputeType(null);
        this.setDisputeStatusCode(null);
        this.setNr(null);
        this.setRrrId(null);
        this.setPlotLocation(null);

    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String value) {
        String old = applicationId;
        applicationId = value;
        propertySupport.firePropertyChange(APPLICATION_ID_PROPERTY, old, applicationId);
    }

    public String getCadastreObjectId() {
        return cadastreObjectId;
    }

    public void setCadastreObjectId(String value) {
        String old = cadastreObjectId;
        cadastreObjectId = value;
        propertySupport.firePropertyChange(CADASTRE_OBJECT_ID_PROPERTY, old, cadastreObjectId);

    }

    public Date getCompletiondate() {
        return completiondate;
    }

    public void setCompletiondate(Date value) {
        Date old = completiondate;
        completiondate = value;
        propertySupport.firePropertyChange(COMPLETION_DATE_PROPERTY, old, completiondate);
    }

    public String getDisputeCategory() {
        return disputeCategory;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        String oldValue = this.userId;
        this.userId = userId;
        propertySupport.firePropertyChange(USER_ID_PROPERTY, oldValue, this.userId);
    }

    public void setDisputeCategory(String value) {
        String old = disputeCategory;
        disputeCategory = value;
        propertySupport.firePropertyChange(DISPUTE_CATEGORY_PROPERTY, old, disputeCategory);
    }

    public String getDisputeType() {
        return disputeType;
    }

    public void setDisputeType(String value) {
        String old = disputeType;
        disputeType = value;
        propertySupport.firePropertyChange(DISPUTE_TYPE_PROPERTY, old, disputeType);
    }

    public Date getLodgementDate() {
        return lodgementDate;
    }

    public void setLodgementDate(Date value) {
        Date old = lodgementDate;
        lodgementDate = value;
        propertySupport.firePropertyChange(LODGEMENT_DATE_PROPERTY, old, lodgementDate);
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String value) {
        String old = nr;
        nr = value;
        propertySupport.firePropertyChange(NR_PROPERTY, old, nr);
    }

    public String getPlotLocation() {
        return plotLocation;
    }

    public void setPlotLocation(String value) {
        String old = plotLocation;
        plotLocation = value;
        propertySupport.firePropertyChange(PLOT_LOCATION_PROPERTY, old, plotLocation);
    }

    public String getRrrId() {
        return rrrId;
    }

    public void setRrrId(String value) {
        String old = rrrId;
        rrrId = value;
        propertySupport.firePropertyChange(RRR_ID_PROPERTY, old, rrrId);
    }

    public String getDisputeStatusCode() {
        return disputeStatusCode;
    }

    public void setDisputeStatusCode(String value) {
        String old = disputeStatusCode;
        disputeStatusCode = value;
        propertySupport.firePropertyChange(DISPUTE_STATUS_CODE_PROPERTY, old, disputeStatusCode);
    }
/*
    public String getPendingActionCode() {
        return getPendingTypeAction().getCode();
    }

    public void setPendingActionCode(String pendingActionCode) {
        String oldValue = null;
        if (getPendingTypeAction() != null) {
            oldValue = getPendingTypeAction().getCode();
        }
        setPendingTypeAction(CacheManager.getBeanByCode(
                CacheManager.getTypeActions(), pendingActionCode));
        propertySupport.firePropertyChange(PENDING_ACTION_CODE_PROPERTY, oldValue, pendingActionCode);
    }

    public TypeActionBean getPendingTypeAction() {
        if (this.pendingTypeAction == null) {
            this.pendingTypeAction = new TypeActionBean();
        }
        return pendingTypeAction;
    }

    public void setPendingTypeAction(TypeActionBean pendingTypeAction) {
        this.pendingTypeAction = pendingTypeAction;
        if (this.pendingTypeAction == null) {
            this.pendingTypeAction = new TypeActionBean();
        }
        this.setJointRefDataBean(this.pendingTypeAction, pendingTypeAction, PENDING_ACTION_PROPERTY);
    }
*/
    public boolean createDispute() {
        DisputeTO dispute = TypeConverters.BeanToTrasferObject(this, DisputeTO.class);
        dispute = WSManager.getInstance().getAdministrative().createDispute(dispute);
        TypeConverters.TransferObjectToBean(dispute, DisputeBean.class, this);
        return true;
    }

    public boolean saveDispute() {
        DisputeTO dispute = TypeConverters.BeanToTrasferObject(this, DisputeTO.class);
        dispute = WSManager.getInstance().getAdministrative().saveDispute(dispute);
        TypeConverters.TransferObjectToBean(dispute, DisputeBean.class, this);
        return true;
    }

    /**
     * Returns Dispute by ID.
     *
     * @param disputeId The ID of Dispute to return.
     */
    public static DisputeBean getDisputeById(String disputeId) {
        return TypeConverters.TransferObjectToBean(
                WSManager.getInstance().getAdministrative().getDisputeById(disputeId),
                DisputeBean.class, null);
    }

    /**
     * Returns Dispute by NR.
     *
     * @param disputeNr The Nr of Dispute to return.
     */
    public static DisputeBean getDisputeByNr(String disputeNr) {
        return TypeConverters.TransferObjectToBean(
                WSManager.getInstance().getAdministrative().getDisputeByNr(disputeNr),
                DisputeBean.class, null);
    }

    /**
     * Returns Dispute by User.
     *
     * @param disputeUser The User of Dispute to return.
     */
    public static DisputeBean getDisputeByUser(String user) {
        return TypeConverters.TransferObjectToBean(
                WSManager.getInstance().getAdministrative().getDisputeByUser(user),
                DisputeBean.class, null);
    }

    /**
     * Returns client bean, related to lodged user.
     */
    public static DisputeBean getDispute() {
        DisputeTO disputeTO = WSManager.getInstance().getAdministrative().getDispute();
        return TypeConverters.TransferObjectToBean(disputeTO, DisputeBean.class, null);
    }
}
