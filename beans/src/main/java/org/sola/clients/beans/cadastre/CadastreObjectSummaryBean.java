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
package org.sola.clients.beans.cadastre;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.sola.clients.beans.AbstractTransactionedBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.referencedata.CadastreObjectTypeBean;
import org.sola.clients.beans.referencedata.LandGradeTypeBean;
import org.sola.clients.beans.referencedata.RoadClassTypeBean;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.messaging.ClientMessage;

/**
 * Summary bean for cadaster objects
 */
public class CadastreObjectSummaryBean extends AbstractTransactionedBean {
    public static final String TYPE_CODE_PROPERTY = "typeCode";
    public static final String APPROVAL_DATETIME_PROPERTY = "approvalDatetime";
    public static final String HISTORIC_DATETIME_PROPERTY = "historicDatetime";
    public static final String SOURCE_REFERENCE_PROPERTY = "sourceReference";
    public static final String NAME_FIRSTPART_PROPERTY = "nameFirstpart";
    public static final String NAME_LASTPART_PROPERTY = "nameLastpart";
    public static final String CADASTRE_OBJECT_TYPE_PROPERTY = "cadastreObjectType";
    public static final String VALUATION_AMOUNT_PROPERTY = "valuationAmount";
    public static final String SURVEYOR_PROPERTY = "surveyor";
    public static final String SURVEY_DATE_PROPERTY = "surveyDate";
    public static final String REMARKS_PROPERTY = "remarks";
    public static final String SURVEY_FEE_PROPERTY = "surveyFee";
    public static final String LAND_GRADE_TYPE_PROPERTY = "landGradeType";
    public static final String LAND_GRADE_CODE_PROPERTY = "landGradeCode";
    public static final String VALUATION_ZONE_PROPERTY = "valuationZone";
    public static final String ROAD_CLASS_TYPE_PROPERTY = "roadClassType";
    public static final String ROAD_CLASS_CODE_PROPERTY = "roadClassCode";

    
    private Date approvalDatetime;
    private Date historicDatetime;
    @Length(max = 100, message =  ClientMessage.CHECK_FIELD_INVALID_LENGTH_SRCREF, payload=Localized.class)
    private String sourceReference;
    private BigDecimal valuationAmount;
    @Length(max = 20, message =  ClientMessage.CHECK_FIELD_INVALID_LENGTH_FIRSTPART, payload=Localized.class)
    @NotEmpty(message =  ClientMessage.CHECK_NOTNULL_CADFIRSTPART, payload=Localized.class)
    private String nameFirstpart;
    @Length(max = 50, message =  ClientMessage.CHECK_FIELD_INVALID_LENGTH_LASTPART, payload=Localized.class)
    @NotEmpty(message =  ClientMessage.CHECK_NOTNULL_CADLASTPART, payload=Localized.class)
    private String nameLastpart;
    @NotNull(message =  ClientMessage.CHECK_NOTNULL_CADOBJTYPE, payload=Localized.class)
    private CadastreObjectTypeBean cadastreObjectType;
    private Date surveyDate;
    private PartySummaryBean surveyor;
    private String remarks;
    @Valid
    @NotNull(message=ClientMessage.CHECK_LAND_GRADE_NOT_NULL, payload=Localized.class)
    private LandGradeTypeBean landGradeType;
    @Valid
    @NotNull(message=ClientMessage.CHECK_ROAD_ACCESS_NOT_NULL, payload=Localized.class)
    private RoadClassTypeBean roadClassType;
    private BigDecimal surveyFee;
    private String valuationZone;
    private boolean hasLease;
    private boolean hasDispute;
    
    public CadastreObjectSummaryBean(){
        super();
    }
    
    public Date getApprovalDatetime() {
        return approvalDatetime;
    }

    public void setApprovalDatetime(Date approvalDatetime) {
        Date oldValue = approvalDatetime;
        this.approvalDatetime = approvalDatetime;
        propertySupport.firePropertyChange(APPROVAL_DATETIME_PROPERTY,
                oldValue, approvalDatetime);
    }

    public Date getHistoricDatetime() {
        return historicDatetime;
    }

    public void setHistoricDatetime(Date historicDatetime) {
        Date oldValue = historicDatetime;
        this.historicDatetime = historicDatetime;
        propertySupport.firePropertyChange(HISTORIC_DATETIME_PROPERTY,
                oldValue, historicDatetime);
    }

    public String getNameFirstpart() {
        return nameFirstpart;
    }

    public void setNameFirstpart(String nameFirstpart) {
        String oldValue = nameFirstpart;
        this.nameFirstpart = nameFirstpart;
        propertySupport.firePropertyChange(NAME_FIRSTPART_PROPERTY,
                oldValue, nameFirstpart);
    }

    public String getNameLastpart() {
        return nameLastpart;
    }

    public void setNameLastpart(String nameLastpart) {
        String oldValue = nameLastpart;
        this.nameLastpart = nameLastpart;
        propertySupport.firePropertyChange(NAME_LASTPART_PROPERTY,
                oldValue, nameLastpart);
    }

    public String getSourceReference() {
        return sourceReference;
    }

    public void setSourceReference(String sourceReference) {
        String oldValue = sourceReference;
        this.sourceReference = sourceReference;
        propertySupport.firePropertyChange(SOURCE_REFERENCE_PROPERTY,
                oldValue, sourceReference);
    }

    public String getTypeCode() {
        if (cadastreObjectType != null) {
            return cadastreObjectType.getCode();
        } else {
            return null;
        }
    }

    public void setTypeCode(String typeCode) {
        String oldValue = null;
        if (cadastreObjectType != null) {
            oldValue = cadastreObjectType.getCode();
        }
        setCadastreObjectType(CacheManager.getBeanByCode(
                CacheManager.getCadastreObjectTypes(), typeCode));
        propertySupport.firePropertyChange(TYPE_CODE_PROPERTY, oldValue, typeCode);
    }

    public CadastreObjectTypeBean getCadastreObjectType() {
        return cadastreObjectType;
    }

    public void setCadastreObjectType(CadastreObjectTypeBean cadastreObjectType) {
        if(this.cadastreObjectType==null){
            this.cadastreObjectType = new CadastreObjectTypeBean();
        }
        this.setJointRefDataBean(this.cadastreObjectType, cadastreObjectType, CADASTRE_OBJECT_TYPE_PROPERTY);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        String oldValue = this.remarks;
        this.remarks = remarks;
        propertySupport.firePropertyChange(REMARKS_PROPERTY, oldValue, this.remarks);
    }

    public boolean isHasLease() {
        return hasLease;
    }

    public void setHasLease(boolean hasLease) {
        this.hasLease = hasLease;
    }

    public boolean isHasDispute() {
        return hasDispute;
    }

    public void setHasDispute(boolean hasDispute) {
        this.hasDispute = hasDispute;
    }
    
    public Date getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(Date surveyDate) {
        Date oldValue = this.surveyDate;
        this.surveyDate = surveyDate;
        propertySupport.firePropertyChange(SURVEY_DATE_PROPERTY, oldValue, this.surveyDate);
    }

    public PartySummaryBean getSurveyor() {
        return surveyor;
    }

    public void setSurveyor(PartySummaryBean value) {        
        PartySummaryBean oldValue = this.surveyor;
        this.surveyor = value;
        propertySupport.firePropertyChange(SURVEYOR_PROPERTY, oldValue, this.surveyor);
    }

    public BigDecimal getValuationAmount() {
        return valuationAmount;
    }

    public void setValuationAmount(BigDecimal valuationAmount) {
        BigDecimal oldValue = this.valuationAmount;
        this.valuationAmount = valuationAmount;
        propertySupport.firePropertyChange(VALUATION_AMOUNT_PROPERTY, oldValue, this.valuationAmount);
    }

    public BigDecimal getSurveyFee() {
        return surveyFee;
    }

    public void setSurveyFee(BigDecimal surveyFee) {
        BigDecimal oldValue = this.surveyFee;
        this.surveyFee = surveyFee;
        propertySupport.firePropertyChange(SURVEY_FEE_PROPERTY, oldValue, this.surveyFee);
    }
    
    public LandGradeTypeBean getLandGradeType() {
        return landGradeType;
    }

    public void setLandGradeType(LandGradeTypeBean landGradeType) {
        if (landGradeType == null) {
            this.landGradeType = new LandGradeTypeBean();
        } else {
            this.landGradeType = landGradeType;
        }
        propertySupport.firePropertyChange(LAND_GRADE_TYPE_PROPERTY, null, this.landGradeType);
    }

    public String getLandGradeCode() {
        if (landGradeType != null) {
            return landGradeType.getCode();
        } else {
            return null;
        }
    }

    public void setLandGradeCode(String landGradeCode) {
        String oldValue = null;
        if (landGradeType != null) {
            oldValue = landGradeType.getCode();
        }
        setLandGradeType(CacheManager.getBeanByCode(
                CacheManager.getLandGradeTypes(), landGradeCode));
        propertySupport.firePropertyChange(LAND_GRADE_CODE_PROPERTY, oldValue, landGradeCode);
    }
    
    public RoadClassTypeBean getRoadClassType(){
        return roadClassType;
    }
    
    public void setRoadClassType(RoadClassTypeBean roadClassType){
        if (roadClassType == null){
           this.roadClassType = new RoadClassTypeBean();
        }else
        {
            this.roadClassType = roadClassType;
        }
    }
    
    public String getRoadClassCode(){
        if (roadClassType != null){
            return roadClassType.getCode();
        }else
        {
            return null;
        }
    }
    
    public void setRoadClassCode(String roadClassCode){
        String oldValue = null;
        if (roadClassType != null){
            oldValue = roadClassType.getCode();
        }
        setRoadClassType(CacheManager.getBeanByCode(
                            CacheManager.getRoadClassType(), roadClassCode));
        propertySupport.firePropertyChange(ROAD_CLASS_CODE_PROPERTY, 
                                            oldValue, roadClassCode);
    }

    public String getValuationZone() {
        return valuationZone;
    }

    public void setValuationZone(String valuationZone) {
        String oldValue = this.valuationZone;
        this.valuationZone = valuationZone;
        propertySupport.firePropertyChange(VALUATION_ZONE_PROPERTY, oldValue, this.valuationZone);
    }
    
    public String getCode() {
        String result = "";
        if(nameFirstpart!=null){
            result = nameFirstpart;
            if(nameLastpart!=null){
                result += "-" + nameLastpart;
            }
        }
        return result;
    }
    
    @Override
    public String toString() {
        return getCode();
    }
}
