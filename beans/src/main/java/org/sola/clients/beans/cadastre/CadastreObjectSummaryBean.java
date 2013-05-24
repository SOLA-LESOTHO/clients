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
package org.sola.clients.beans.cadastre;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.sola.clients.beans.AbstractTransactionedBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.referencedata.CadastreObjectTypeBean;
import org.sola.clients.beans.referencedata.LandGradeTypeBean;
import org.sola.clients.beans.referencedata.LandUseTypeBean;
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
    public static final String LAND_USE_TYPE_PROPERTY = "landUseType";
    public static final String LAND_USE_CODE_PROPERTY = "landUseCode";
    public static final String VALUATION_AMOUNT_PROPERTY = "valuationAmount";
    public static final String SURVEYOR_PROPERTY = "surveyor";
    public static final String SURVEY_DATE_PROPERTY = "surveyDate";
    public static final String REMARKS_PROPERTY = "remarks";
    public static final String SURVEY_FEE_PROPERTY = "surveyFee";
    public static final String LAND_GRADE_TYPE_PROPERTY = "landGradeType";
    public static final String LAND_GRADE_CODE_PROPERTY = "landGradeCode";
<<<<<<< HEAD
    public static final String VALUATION_ZONE_PROPERTY = "valuationZone";
=======

>>>>>>> 587f1622221110d454087957baa2f84beced9132
    
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
    private String surveyor;
    private String remarks;
    private LandUseTypeBean landUseType;
    private LandGradeTypeBean landGradeType;
    private BigDecimal surveyFee;
    private String valuationZone;
    
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
    public String getLandUseCode() {
        if (landUseType != null) {
            return landUseType.getCode();
        } else {
            return null;
        }
    }

    public void setLandUseCode(String landUseCode) {
        String oldValue = null;
        if (landUseType != null) {
            oldValue = landUseType.getCode();
        }
        setLandUseType(CacheManager.getBeanByCode(
                CacheManager.getLandUseTypes(), landUseCode));
        propertySupport.firePropertyChange(LAND_USE_CODE_PROPERTY, oldValue, landUseCode);
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
     public LandUseTypeBean getLandUseType() {
        return landUseType;
    }

    public void setLandUseType(LandUseTypeBean landUseType) {
        if(this.landUseType==null){
            this.landUseType = new LandUseTypeBean();
        }
        this.setJointRefDataBean(this.landUseType, landUseType, LAND_USE_TYPE_PROPERTY);
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        String oldValue = this.remarks;
        this.remarks = remarks;
        propertySupport.firePropertyChange(REMARKS_PROPERTY, oldValue, this.remarks);
    }

    public Date getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(Date surveyDate) {
        Date oldValue = this.surveyDate;
        this.surveyDate = surveyDate;
        propertySupport.firePropertyChange(SURVEY_DATE_PROPERTY, oldValue, this.surveyDate);
    }

    public String getSurveyor() {
        return surveyor;
    }

    public void setSurveyor(String surveyor) {
        String oldValue = this.surveyor;
        this.surveyor = surveyor;
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
        if (this.landGradeType == null) {
            this.landGradeType = new LandGradeTypeBean();
        }
        this.setJointRefDataBean(this.landGradeType, landGradeType, LAND_GRADE_TYPE_PROPERTY);
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

    public String getValuationZone() {
        return valuationZone;
    }

    public void setValuationZone(String valuationZone) {
        String oldValue = this.valuationZone;
        this.valuationZone = valuationZone;
        propertySupport.firePropertyChange(VALUATION_ZONE_PROPERTY, oldValue, this.valuationZone);
    }
    
    
    
    @Override
    public String toString() {
        String result = "";
        if(nameFirstpart!=null){
            result = nameFirstpart;
            if(nameLastpart!=null){
                result += "-" + nameLastpart;
            }
        }
        return result;
    }
}
