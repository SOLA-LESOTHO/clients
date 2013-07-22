/**
 * ******************************************************************************************
 * Copyright (C) 2012 - Food and Agriculture Organization of the United Nations (FAO).
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,this list
 *       of conditions and the following disclaimer.
 *    2. Redistributions in binary form must reproduce the above copyright notice,this list
 *       of conditions and the following disclaimer in the documentation and/or other
 *       materials provided with the distribution.
 *    3. Neither the name of FAO nor the names of its contributors may be used to endorse or
 *       promote products derived from this software without specific prior written permission.
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

package org.sola.clients.swing.gis.beans;

import com.vividsolutions.jts.geom.Geometry;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.geotools.swing.extended.util.GeometryUtility;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.address.AddressBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.cadastre.SpatialValueAreaBean;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.referencedata.*;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.messaging.ClientMessage;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.EntityAction;

/**
 * Defines a cadastre object bean.
 * 
 * @author Elton Manoku
 */
public class CadastreObjectBean extends SpatialBean {
    
    public static String NAME_FIRST_PART_PROPERTY = "nameFirstpart";
    public static final String ADDRESS_LIST_PROPERTY = "addressList";
    public static final String SELECTED_ADDRESS_PROPERTY = "selectedAddress";
    public static final String OFFICIAL_AREA_SIZE_PROPERTY = "officialAreaSize";
    public static final String OFFICIAL_AREA_PROPERTY = "officialArea";
    public static final String NAME_LAST_PART_PROPERTY = "nameLastpart";
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
    public static final String CALCULATED_AREA_SIZE_PROPERTY = "calculatedArea";
    public static final String STATUS_CODE_PROPERTY = "statusCode";
    public static final String STATUS_PROPERTY = "status";
    public static final String ADDRESS_STRING_PROPERTY = "addressString";
    public static final String ROAD_CLAS_TYPE_PROPERTY = "roadClassType";
    public static final String ROAD_CLASS_CODE_PROPERTY = "roadClassCode";
    
    private String id;
    private byte[] geomPolygon;
    private SolaList<SpatialValueAreaBean> spatialValueAreaList;
    private SolaList<AddressBean> addressList;
    private transient AddressBean selectedAddress;
    private Date approvalDatetime;
    private Date historicDatetime;
    private String sourceReference;
    private BigDecimal valuationAmount;
    private String nameFirstpart;
    private String nameLastpart;
    private CadastreObjectTypeBean cadastreObjectType;
    private Date surveyDate;
    private PartySummaryBean surveyor;
    private String remarks;
    private LandGradeTypeBean landGradeType;
    private RoadClassTypeBean roadClassType;
    private BigDecimal surveyFee;
    private String valuationZone;
    private RegistrationStatusTypeBean status;
    
    /**
     * Creates a cadastre object bean
     */
    public CadastreObjectBean(){
        super();
        generateId();
        addressList = new SolaList<AddressBean>();
        spatialValueAreaList = new SolaList<SpatialValueAreaBean>();
    }

    /** 
     * Generates new ID for the cadastre object 
     */
    public final void generateId(){
        setId(UUID.randomUUID().toString());
    }
    
    @Override
    public void setFeatureGeom(Geometry geometryValue) {
        super.setFeatureGeom(geometryValue);
        this.setGeomPolygon(GeometryUtility.getWkbFromGeometry(geometryValue));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getNameFirstpart() {
        return nameFirstpart;
    }

    /**
     * Sets the Name first part. It fires the change event to notify the corresponding feature
     * for the change.
     * 
     * @param nameFirstpart 
     */
    public void setNameFirstpart(String nameFirstpart) {
        String oldValue = this.nameFirstpart;
        this.nameFirstpart = nameFirstpart;
        propertySupport.firePropertyChange(NAME_FIRST_PART_PROPERTY, oldValue, this.nameFirstpart);
    }

    public String getNameLastpart() {
        return nameLastpart;
    }

    public void setNameLastpart(String nameLastpart) {
        String oldValue = this.nameLastpart;
        this.nameLastpart = nameLastpart;
        propertySupport.firePropertyChange(NAME_LAST_PART_PROPERTY, oldValue, this.nameLastpart);
    }

    public byte[] getGeomPolygon() {
        return geomPolygon;
    }

    /**
     * Sets the geometry of the cadastre object. If the feature related geometry is not present,
     * it is also set.
     * 
     * @param geomPolygon 
     */
    public void setGeomPolygon(byte[] geomPolygon) {
        this.geomPolygon = geomPolygon.clone();
        if (getFeatureGeom() == null){
            super.setFeatureGeom(GeometryUtility.getGeometryFromWkb(geomPolygon));
        }
    }
   
    /** Looks for officialArea code in the list of areas. */
    @NotNull(message=ClientMessage.CHECK_NOTNULL_AREA, payload=Localized.class)
    public BigDecimal getOfficialAreaSize(){
        return getArea(SpatialValueAreaBean.CODE_OFFICIAL_AREA);
    }

    /** Sets officialArea code. */
    public void setOfficialAreaSize(BigDecimal area){
        setArea(area, SpatialValueAreaBean.CODE_OFFICIAL_AREA, OFFICIAL_AREA_SIZE_PROPERTY);
    }
    
    /** Synonym method for <code>getOfficialAreaSize()</code> */
    public BigDecimal getOfficialArea(){
        return getOfficialAreaSize();
    }
    
    /** Synonym method for <code>setOfficialAreaSize(BigDecimal area)</code> */
    public void setOfficialArea(BigDecimal area){
        setOfficialAreaSize(area);
        propertySupport.firePropertyChange(OFFICIAL_AREA_PROPERTY, null, getOfficialArea());
    }
    
    /** Returns calculated area based on feature geometry. </code> */
    public BigDecimal getCalculatedArea(){
        return getArea(SpatialValueAreaBean.CODE_CALCULATED_AREA);
    }
    
    /** Synonym method for <code>setOfficialAreaSize(BigDecimal area)</code> */
    public void setCalculatedArea(BigDecimal area){
        setArea(area, SpatialValueAreaBean.CODE_CALCULATED_AREA, CALCULATED_AREA_SIZE_PROPERTY);
    }
    
    private BigDecimal getArea(String areaCode){
        if(getSpatialValueAreaFiletredList()==null || getSpatialValueAreaFiletredList().size() < 1){
            return null;
        }
        for(SpatialValueAreaBean areaBean : getSpatialValueAreaFiletredList()){
            if(areaBean.getTypeCode()!=null && areaBean.getTypeCode().equals(areaCode)){
                return areaBean.getSize();
            }
        }
        return null;
    }
    
    private void setArea(BigDecimal area, String areaCode, String evtName){
        for(SpatialValueAreaBean areaBean : getSpatialValueAreaFiletredList()){
            if(areaBean.getTypeCode()!=null && areaBean.getTypeCode().equals(areaCode)){
                // Delete area if provided value is null
                if(area == null){
                    areaBean.setEntityAction(EntityAction.DELETE);
                } else {
                    areaBean.setSize(area);
                }
                propertySupport.firePropertyChange(evtName, null, area);
                return;
            }
        }
        
        // Official area not found, add new if provided area not null
        if(area!=null){
            SpatialValueAreaBean areaBean = new SpatialValueAreaBean();
            areaBean.setSize(area);
            areaBean.setTypeCode(areaCode);
            areaBean.setSpatialUnitId(this.getId());
            getSpatialValueAreaList().addAsNew(areaBean);
        }
        propertySupport.firePropertyChange(evtName, null, area);
    }
        
    @Valid
    public ObservableList<AddressBean> getAddressFilteredList() {
        return addressList.getFilteredList();
    }
    
    public SolaList<AddressBean> getAddressList() {
        return addressList;
    }

    public void setAddressList(SolaList<AddressBean> addressList) {
        this.addressList = addressList;
        propertySupport.firePropertyChange(ADDRESS_STRING_PROPERTY, null, getAddressString());
    }

    /** Returns merged string of addresses. */
    public String getAddressString(){
        String address = "";
        if(getAddressFilteredList()!=null){
            for (AddressBean addressBean : getAddressFilteredList()){
                if(addressBean.getDescription()!=null && !addressBean.getDescription().isEmpty()){
                    if(address.isEmpty()){
                        address = addressBean.getDescription();
                    } else {
                        address = address + "; " + addressBean.getDescription();
                    }
                }
            }
        }
        return address;
    }
    
    public AddressBean getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressBean selectedAddress) {
        AddressBean oldValue = this.selectedAddress;
        this.selectedAddress = selectedAddress;
        propertySupport.firePropertyChange(SELECTED_ADDRESS_PROPERTY, oldValue, this.selectedAddress);
    }

    public SolaList<SpatialValueAreaBean> getSpatialValueAreaList() {
        return spatialValueAreaList;
    }

    @Valid
    public ObservableList<SpatialValueAreaBean> getSpatialValueAreaFiletredList() {
        return spatialValueAreaList.getFilteredList();
    }
    
    public void setSpatialValueAreaList(SolaList<SpatialValueAreaBean> spatialValueAreaList) {
        this.spatialValueAreaList = spatialValueAreaList;
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
        if(cadastreObjectType==null){
            this.cadastreObjectType = new CadastreObjectTypeBean();
        } else {
            this.cadastreObjectType = cadastreObjectType;
        }
        propertySupport.firePropertyChange(CADASTRE_OBJECT_TYPE_PROPERTY, null, this.cadastreObjectType);
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

    public PartySummaryBean getSurveyor() {
        return surveyor;
    }

    public void setSurveyor(PartySummaryBean value) {
        surveyor = value;
        propertySupport.firePropertyChange(SURVEYOR_PROPERTY, null, value);
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
    
    public RoadClassTypeBean getRoadClassType(){
        return roadClassType;
    }
    
    public void setRoadClassType(RoadClassTypeBean roadClassType){
        if (roadClassType == null){
            this.roadClassType = new RoadClassTypeBean();
        }else{
            this.roadClassType = roadClassType;
        }
        propertySupport.firePropertyChange(ROAD_CLAS_TYPE_PROPERTY, null, roadClassType);
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
                            CacheManager.getRoadClassType(),roadClassCode));
        
        propertySupport.firePropertyChange(ROAD_CLASS_CODE_PROPERTY, oldValue, roadClassCode);
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

    public String getValuationZone() {
        return valuationZone;
    }

    public void setValuationZone(String valuationZone) {
        String oldValue = this.valuationZone;
        this.valuationZone = valuationZone;
        propertySupport.firePropertyChange(VALUATION_ZONE_PROPERTY, oldValue, this.valuationZone);
    }

    public String getStatusCode() {
        if (status != null) {
            return status.getCode();
        } else {
            return null;
        }
    }

    public void setStatusCode(String statusCode) {
        String oldValue = null;
        if (status != null) {
            oldValue = status.getCode();
        }
        if (WSManager.getInstance().getReferenceDataService() != null) {
            setStatus(CacheManager.getBeanByCode(
                    CacheManager.getRegistrationStatusTypes(), statusCode));
            propertySupport.firePropertyChange(STATUS_CODE_PROPERTY, oldValue, statusCode);
        }
    }

    public RegistrationStatusTypeBean getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatusTypeBean status) {
        if (status == null) {
            this.status = new RegistrationStatusTypeBean();
        } else {
            this.status = status;
        }
        propertySupport.firePropertyChange(STATUS_PROPERTY, null, this.status);
    }
    
    public String getParcelCode(){
        return toString();
    }
    
    /** 
     * Fires property change events for the fields which values derived based 
     * on other fields values (e.g. official area size).
     */
    public void fireCalculatedFieldsUpdate(){
        propertySupport.firePropertyChange(OFFICIAL_AREA_SIZE_PROPERTY, null, getOfficialAreaSize());
        propertySupport.firePropertyChange(OFFICIAL_AREA_PROPERTY, null, getOfficialArea());
        propertySupport.firePropertyChange(CALCULATED_AREA_SIZE_PROPERTY, null, getCalculatedArea());
        propertySupport.firePropertyChange(ADDRESS_STRING_PROPERTY, null, getAddressString());
    }
    
    @Override
    public String toString() {
        return String.format("%s-%s",this.nameFirstpart, this.nameLastpart);
    }    
}
