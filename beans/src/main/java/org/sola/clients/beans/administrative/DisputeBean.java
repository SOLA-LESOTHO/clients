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
import org.sola.clients.beans.AbstractTransactionedBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.administrative.DisputeTO;
import org.sola.clients.beans.referencedata.DisputeCategoryBean;
import org.sola.clients.beans.referencedata.DisputeTypeBean;

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
    public static final String STATUS_CODE_PROPERTY = "statusCode";
    public static final String RRR_ID_PROPERTY = "rrrId";
    public static final String PLOT_LOCATION_PROPERTY = "plotLocation";
    public static final String CADASTRE_OBJECT_ID_PROPERTY = "cadastreObjectId";
    public static final String SELECTED_CATEGORY_PROPERTY = "selectedCategory";
    public static final String SELECTED_TYPE_PROPERTY = "selectedType";

    public static final String USER_ID_PROPERTY = "userid";
    
    
    private String nr;
    private Date lodgementDate;
    private Date completiondate;
    private DisputeCategoryBean disputeCategory;
    private DisputeTypeBean disputeType;
    private String statusCode;
    private String rrrId;
    private String plotLocation;
    private String cadastreObjectId;
    private String userId;

    public DisputeBean() {
        super();
    }

    public void clean() {
        this.setCadastreObjectId(null);
        this.setLodgementDate(null);
        this.setCompletiondate(null);
        this.setDisputeCategory(null);
        this.setDisputeType(null);
        this.setStatusCode(null);
        this.setNr(null);
        this.setRrrId(null);
        this.setPlotLocation(null);

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

    public String getDisputeCategoryCode() {
        if (disputeCategory != null) {
            return disputeCategory.getCode();
        } else {
            return null;
        }
    }

    public void setDisputeCategoryCode(String disputeCategoryCode) {
        String oldValue = null;
        if (disputeCategory != null) {
            oldValue = disputeCategory.getCode();

            return;

        }
        setDisputeCategory(CacheManager.getBeanByCode(
                CacheManager.getDisputeCategory(), disputeCategoryCode));

        propertySupport.firePropertyChange(DISPUTE_CATEGORY_PROPERTY,
                oldValue, disputeCategoryCode);
    }

    public DisputeCategoryBean getDisputeCategory() {
        return disputeCategory;
    }

    public void setDisputeCategory(DisputeCategoryBean disputeCategory) {
        if (this.disputeCategory == null) {
            this.disputeCategory = new DisputeCategoryBean();
        }
        this.setJointRefDataBean(this.disputeCategory, disputeCategory, DISPUTE_CATEGORY_PROPERTY);
    }

   
    
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        String oldValue = this.userId;
        this.userId = userId;
        propertySupport.firePropertyChange(USER_ID_PROPERTY, oldValue, this.userId);
    }

    
    public String getDisputeTypeCode() {
        if (disputeType != null) {
            return disputeType.getCode();
        } else {
            return null;
        }
    }
    
    public void setDisputeTypeCode(String disputeTypeCode) {
        String oldValue = null;
        if (disputeType !=null) {
            oldValue = disputeType.getCode();
            return;
        }
        
        setDisputeType(CacheManager.getBeanByCode(
                CacheManager.getDisputeType(), disputeTypeCode));
        propertySupport.firePropertyChange(DISPUTE_TYPE_PROPERTY, oldValue, disputeTypeCode);
    }
    
     public DisputeTypeBean getDisputeType() {
        return disputeType;
    }

     public void setDisputeType(DisputeTypeBean disputeType) {
        if (this.disputeType == null) {
            this.disputeType = new DisputeTypeBean();
        }
         this.setJointRefDataBean(this.disputeType, disputeType, DISPUTE_TYPE_PROPERTY);
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String value) {
        String old = statusCode;
        statusCode = value;
        propertySupport.firePropertyChange(STATUS_CODE_PROPERTY, old, statusCode);
    }
    
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
