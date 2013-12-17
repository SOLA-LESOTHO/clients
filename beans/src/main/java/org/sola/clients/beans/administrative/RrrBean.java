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
package org.sola.clients.beans.administrative;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractTransactionedBean;
import org.sola.clients.beans.administrative.validation.*;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.cadastre.CadastreObjectBean;
import org.sola.clients.beans.controls.SolaList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.clients.beans.referencedata.*;
import org.sola.clients.beans.source.SourceBean;
import org.sola.clients.beans.validation.Localized;
import org.sola.clients.beans.validation.NoDuplicates;
import org.sola.common.NumberUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.webservices.transferobjects.administrative.LeaseFeeTO;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.administrative.RrrTO;
import org.sola.webservices.transferobjects.EntityAction;
import org.sola.webservices.transferobjects.cadastre.CadastreObjectTO;

/**
 * Contains properties and methods to manage <b>RRR</b> object of the domain
 * model. Could be populated from the {@link RrrTO} object.
 */
@RrrBeanCheck
@LeaseBeanCheck(groups={LeaseValidationGroup.class})
public class RrrBean extends AbstractTransactionedBean {

    public enum RRR_ACTION {

        NEW, VARY, CANCEL, EDIT, VIEW;
    }
    public static final String CODE_OWNERSHIP = "ownership";
    public static final String CODE_APARTMENT = "apartment";
    public static final String CODE_STATE_OWNERSHIP = "stateOwnership";
    public static final String CODE_MORTGAGE = "mortgage";
    public static final String CODE_AGRI_ACTIVITY = "agriActivity";
    public static final String CODE_COMMON_OWNERSHIP = "commonOwnership";
    public static final String CODE_CUSTOMARY_TYPE = "customaryType";
    public static final String CODE_FIREWOOD = "firewood";
    public static final String CODE_FISHING = "fishing";
    public static final String CODE_GRAZING = "grazing";
    public static final String CODE_LEASE = "lease";
    public static final String CODE_OCCUPATION = "occupation";
    public static final String CODE_OWNERSHIP_ASSUMED = "ownershipAssumed";
    public static final String CODE_SUPERFICIES = "superficies";
    public static final String CODE_TENANCY = "tenancy";
    public static final String CODE_USUFRUCT = "usufruct";
    public static final String CODE_WATERRIGHTS = "waterrights";
    public static final String CODE_ADMIN_PUBLIC_SERVITUDE = "adminPublicServitude";
    public static final String CODE_MONUMENT = "monument";
    public static final String CODE_LIFE_ESTATE = "lifeEstate";
    public static final String CODE_CAVEAT = "caveat";
    public static final String CODE_DEED = "regnDeeds";
    public static final String CODE_SERVITUDE = "servitude";
    public static final String CODE_CONSENT = "consentApplication";
    public static final String CODE_SUBLEASE = "subLease";
    public static final String CODE_SUBLEASE_MORTGAGE = "subLeaseMortgage";
    public static final String BA_UNIT_ID_PROPERTY = "baUnitId";
    public static final String TYPE_CODE_PROPERTY = "typeCode";
    public static final String RRR_TYPE_PROPERTY = "rrrType";
    public static final String EXPIRATION_DATE_PROPERTY = "expirationDate";
    public static final String LEASE_TERM_PROPERTY = "leaseTerm";
    public static final String SHARE_PROPERTY = "share";
    public static final String AMOUNT_PROPERTY = "amount";
    public static final String MORTGAGE_INTEREST_RATE_PROPERTY = "mortgageInterestRate";
    public static final String MORTGAGE_RANKING_PROPERTY = "mortgageRanking";
    public static final String MORTGAGE_TYPE_CODE_PROPERTY = "mortgageTypeCode";
    public static final String MORTGAGE_TYPE_PROPERTY = "mortgageType";
    public static final String NOTATION_PROPERTY = "notation";
    public static final String IS_PRIMARY_PROPERTY = "isPrimary";
    public static final String FIRST_RIGHTHOLDER_PROPERTY = "firstRightholder";
    public static final String SELECTED_SHARE_PROPERTY = "selectedShare";
    public static final String SELECTED_PROPERTY = "selected";
    public static final String SELECTED_RIGHTHOLDER_PROPERTY = "selectedRightHolder";
    public static final String DUE_DATE_PROPERTY = "dueDate";
    public static final String REGISTRATION_NUMBER_PROPERTY = "registrationNumber";
    public static final String STATUS_CHANGE_DATE_PROPERTY = "statusChangeDate";
    public static final String CADASTRE_OBJECT_PROPERTY = "cadastreObject";
    public static final String START_DATE_PROPERTY = "startDate";
    public static final String EXECUTION_DATE_PROPERTY = "executionDate";
    public static final String LEASE_NUMBER_PROPERTY = "leaseNumber";
    public static final String STAMP_DUTY_PROPERTY = "stampDuty";
    public static final String TRANSFER_DUTY_PROPERTY = "transferDuty";
    public static final String REGISTRATION_FEE_PROPERTY = "registrationFee";
    public static final String LEASE_SPECIAL_CONDITION_LIST_PROPERTY = "leaseSpecialConditionList";
    public static final String SELECTED_SPECIAL_CONDITION_PROPERTY = "selectedSpecialCondition";
    public static final String GROUND_RENT_PROPERTY = "groundRent";
    public static final String LAND_USE_TYPE_PROPERTY = "landUseType";
    public static final String LAND_USE_CODE_PROPERTY = "landUseCode";
    public static final String LAND_USABLE_PROPERTY = "landUsable";
    public static final String PERSONAL_LEVY_PROPERTY = "personalLevy";
    public static final String MORTGAGE_TERM_PROPERTY = "mortgageTerm";
    public static final String SERVICE_FEE_PROPERTY = "serviceFee";
    
    private String baUnitId;
    private String nr;
    @Past(message = ClientMessage.CHECK_REGISTRATION_DATE, payload = Localized.class)
    @NotNull(message = ClientMessage.CHECK_NOTNULL_REGDATE, payload = Localized.class,
            groups = {RrrValidationGroup.class})
    private Date registrationDate;
    private String transactionId;
    @NotNull(message = ClientMessage.CHECK_NOTNULL_EXPIRATION, payload = Localized.class,
            groups = {MortgageValidationGroup.class, LeaseValidationGroup.class})
    @Future(message = ClientMessage.CHECK_FUTURE_EXPIRATION, payload = Localized.class,
            groups = {MortgageValidationGroup.class, LeaseValidationGroup.class})
    private Date expirationDate;
    @NotNull(message = ClientMessage.CHECK_NOTNULL_MORTGAGEAMOUNT, payload = Localized.class, groups = {MortgageValidationGroup.class})
    private BigDecimal amount;
    private Date dueDate;
    //@NotNull(message = ClientMessage.CHECK_NOTNULL_MORTAGAETYPE, payload = Localized.class, groups = {MortgageValidationGroup.class})
    private MortgageTypeBean mortgageType;
    private BigDecimal mortgageInterestRate;
    private Integer mortgageRanking;
    private Double share;
    private SolaList<SourceBean> sourceList;
    @Valid
    private SolaList<RrrShareBean> rrrShareList;
    private RrrTypeBean rrrType;
    private BaUnitNotationBean notation;
    private boolean primary = false;
    @Valid
    private SolaList<PartyBean> rightHolderList;
    private transient RrrShareBean selectedShare;
    private transient boolean selected;
    private transient PartyBean selectedRightholder;
    @NotEmpty(message = ClientMessage.CHECK_NOTNULL_REGNUMBER, 
            groups={RrrValidationGroup.class}, payload = Localized.class)
    private String registrationNumber;
    private Date statusChangeDate;
    private CadastreObjectBean cadastreObject;
    private String cadastreObjectId;
    // Used to hold the lease expiry date when validating the expirationDate for a sublease
    private transient Date leaseExpiryDate;
    // Used to indicate if the subplot is vithin the area of the lease plot. 
    private transient boolean subplotValid = true;

    @NotNull(message = ClientMessage.LEASE_START_DATE_IS_IMPTY, groups={LeaseValidationGroup.class}, payload = Localized.class)
    private Date startDate;

    //@NotNull(message = ClientMessage.LEASE_EXECUTION_DATE_IS_IMPTY, groups={LeaseValidationGroup.class}, payload = Localized.class)
    //@Past(message = ClientMessage.LEASE_EXECUTION_DATE_IS_IN_FUTURE, groups={LeaseValidationGroup.class}, payload = Localized.class)
    private Date executionDate;
    
    @NotEmpty(message=ClientMessage.LEASE_LEASE_NUMBER_IS_IMPTY, groups={LeaseValidationGroup.class}, payload=Localized.class)
    private String leaseNumber;
    
    @NotNull(message = ClientMessage.LEASE_GROUND_RENT_IS_IMPTY, groups={LeaseValidationGroup.class}, payload = Localized.class)
    private BigDecimal groundRent;
    
    @NotNull(message = ClientMessage.LEASE_LAND_USABLE_IS_IMPTY, groups={LeaseValidationGroup.class}, payload = Localized.class)
    @Range(min=1, max=100, message=ClientMessage.LEASE_LAND_USABLE_ERROR, 
            groups={LeaseValidationGroup.class}, payload = Localized.class)
    private BigDecimal landUsable = BigDecimal.valueOf(100L);
    
    @NotNull(message = ClientMessage.LEASE_PERSONAL_LEVY_IS_IMPTY, groups={LeaseValidationGroup.class}, payload = Localized.class)
    private BigDecimal personalLevy = BigDecimal.ONE;
    
    private BigDecimal stampDuty;
    private BigDecimal transferDuty;
    private BigDecimal registrationFee;
    private BigDecimal serviceFee;
    private SolaList<LeaseSpecialConditionBean> leaseSpecialConditionList;
    private transient LeaseSpecialConditionBean selectedSpecialCondition;
    @NotNull(message = ClientMessage.LEASE_LAND_USE_IS_NULL, groups={LeaseValidationGroup.class}, payload = Localized.class)
    private LandUseTypeBean landUseType;
    private Integer mortgageTerm = Integer.valueOf(0);
    public RrrBean() {
        super();
        sourceList = new SolaList();
        rrrShareList = new SolaList();
        rightHolderList = new SolaList();
        notation = new BaUnitNotationBean();
        leaseSpecialConditionList = new SolaList<LeaseSpecialConditionBean>();
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
    
    public LandUseTypeBean getLandUseType() {
        return landUseType;
    }

    public void setLandUseType(LandUseTypeBean landUseType) {
        if(landUseType==null){
            this.landUseType = new LandUseTypeBean();
        }else{
            this.landUseType = landUseType;
        }
        propertySupport.firePropertyChange(LAND_USE_TYPE_PROPERTY, null, this.landUseType);
    }
    
    public void setFirstRightholder(PartyBean rightholder) {
        if (rightHolderList.size() > 0) {
            rightHolderList.set(0, rightholder);
        } else {
            rightHolderList.add(rightholder);
        }
        propertySupport.firePropertyChange(FIRST_RIGHTHOLDER_PROPERTY, null, rightholder);
    }

    public PartySummaryBean getFirstRightHolder() {
        if (rightHolderList != null && rightHolderList.size() > 0) {
            return rightHolderList.get(0);
        } else {
            return null;
        }
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        boolean oldValue = this.primary;
        this.primary = primary;
        propertySupport.firePropertyChange(IS_PRIMARY_PROPERTY, oldValue, primary);
    }

    @Length(max = 1000, message =  ClientMessage.CHECK_FIELD_INVALID_LENGTH_NOTATION, 
            groups = {RrrValidationGroup.class}, payload=Localized.class)
    @NotEmpty(message= ClientMessage.CHECK_NOTNULL_NOTATION, 
            groups = {RrrValidationGroup.class}, payload=Localized.class)
    public String getNotationText(){
        return getNotation().getNotationText();
    }
    
    public BaUnitNotationBean getNotation() {
        return notation;
    }

    public void setNotation(BaUnitNotationBean notation) {
        this.notation = notation;
        propertySupport.firePropertyChange(NOTATION_PROPERTY, null, notation);
    }

    public String getMortgageTypeCode() {
        if (mortgageType != null) {
            return mortgageType.getCode();
        } else {
            return null;
        }
    }

    public void setMortgageTypeCode(String mortgageTypeCode) {
        String oldValue = null;
        if (mortgageType != null) {
            oldValue = mortgageType.getCode();
        }
        setMortgageType(CacheManager.getBeanByCode(
                CacheManager.getMortgageTypes(), mortgageTypeCode));
        propertySupport.firePropertyChange(MORTGAGE_TYPE_CODE_PROPERTY,
                oldValue, mortgageTypeCode);
    }

    public MortgageTypeBean getMortgageType() {
        return mortgageType;
    }

    public void setMortgageType(MortgageTypeBean mortgageType) {
        if (this.mortgageType == null) {
            this.mortgageType = new MortgageTypeBean();
        }
        this.setJointRefDataBean(this.mortgageType, mortgageType, MORTGAGE_TYPE_PROPERTY);
    }

    public String getBaUnitId() {
        return baUnitId;
    }

    public void setBaUnitId(String baUnitId) {
        String oldValue = this.baUnitId;
        this.baUnitId = baUnitId;
        propertySupport.firePropertyChange(BA_UNIT_ID_PROPERTY, oldValue, baUnitId);
    }

    public String getTypeCode() {
        if (rrrType != null) {
            return rrrType.getCode();
        } else {
            return null;
        }
    }

    public void setTypeCode(String typeCode) {
        String oldValue = null;
        if (rrrType != null) {
            oldValue = rrrType.getCode();
        }
        setRrrType(CacheManager.getBeanByCode(
                CacheManager.getRrrTypes(), typeCode));
        propertySupport.firePropertyChange(TYPE_CODE_PROPERTY, oldValue, typeCode);
    }

    public RrrTypeBean getRrrType() {
        return rrrType;
    }

    public void setRrrType(RrrTypeBean rrrType) {
        if (this.rrrType == null) {
            this.rrrType = new RrrTypeBean();
        }
        this.setJointRefDataBean(this.rrrType, rrrType, RRR_TYPE_PROPERTY);
    }

    /**
     * Calculates and returns lease term in years.
     */
    public int getLeaseTerm() {
        int result = 0;
        if (getStartDate() != null && getExpirationDate() != null) {
            Calendar startDateCal = Calendar.getInstance();
            Calendar expirationDateCal = Calendar.getInstance();
            startDateCal.setTime(getStartDate());
            expirationDateCal.setTime(getExpirationDate());
            if (startDateCal.before(expirationDateCal)) {
                result = expirationDateCal.get(Calendar.YEAR) - startDateCal.get(Calendar.YEAR);
            }
        }
        return result;
    }
    
    /** Calculates lease expiration date based on provided lease terms in years. */
    public void setLeaseTerm(int years){
        if(getStartDate()!=null){
            Calendar startDateCal = Calendar.getInstance();
            startDateCal.setTime(getStartDate());
            startDateCal.add(Calendar.YEAR, years);
            startDateCal.add(Calendar.DAY_OF_MONTH, -1);
            setExpirationDate(startDateCal.getTime());
        }
    }
    
    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        Date oldValue = this.expirationDate;
        this.expirationDate = expirationDate;
        propertySupport.firePropertyChange(EXPIRATION_DATE_PROPERTY, oldValue, this.expirationDate);
        propertySupport.firePropertyChange(LEASE_TERM_PROPERTY, null, getLeaseTerm());
    }

    public Date getLeaseExpiryDate() {
        return leaseExpiryDate;
    }

    public void setLeaseExpiryDate(Date leaseExpiryDate) {
        this.leaseExpiryDate = leaseExpiryDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        BigDecimal oldValue = this.amount;
        this.amount = amount;
        propertySupport.firePropertyChange(AMOUNT_PROPERTY, oldValue, this.amount);
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date nextDueDate) {
        Date oldValue = this.dueDate;
        this.dueDate = nextDueDate;
        propertySupport.firePropertyChange(DUE_DATE_PROPERTY, oldValue, this.dueDate);
    }

    public BigDecimal getMortgageInterestRate() {
        return mortgageInterestRate;
    }

    public void setMortgageInterestRate(BigDecimal mortgageInterestRate) {
        this.mortgageInterestRate = mortgageInterestRate;
    }

    public Integer getMortgageRanking() {
        return mortgageRanking;
    }

    public void setMortgageRanking(Integer mortgageRanking) {
        this.mortgageRanking = mortgageRanking;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        String oldValue = this.registrationNumber;
        this.registrationNumber = registrationNumber;
        propertySupport.firePropertyChange(REGISTRATION_NUMBER_PROPERTY, oldValue, this.registrationNumber);
    }

    public Date getStatusChangeDate() {
        return statusChangeDate;
    }

    public void setStatusChangeDate(Date statusChangeDate) {
        Date oldValue = this.statusChangeDate;
        this.statusChangeDate = statusChangeDate;
        propertySupport.firePropertyChange(STATUS_CHANGE_DATE_PROPERTY, oldValue, this.statusChangeDate);
    }

    public Double getShare() {
        return share;
    }

    public void setShare(Double share) {
        this.share = share;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public SolaList<SourceBean> getSourceList() {
        return sourceList;
    }

    @Size(min = 1, message = ClientMessage.CHECK_SIZE_SOURCELIST, 
            groups={RrrValidationGroup.class}, payload = Localized.class)
    @NoDuplicates(message = ClientMessage.CHECK_NODUPLICATED_SOURCELIST, 
            groups={RrrValidationGroup.class}, payload = Localized.class)
    public ObservableList<SourceBean> getFilteredSourceList() {
        return sourceList.getFilteredList();
    }

    public void setSourceList(SolaList<SourceBean> sourceList) {
        this.sourceList = sourceList;
    }

    public SolaList<RrrShareBean> getRrrShareList() {
        return rrrShareList;
    }

    @Valid
    @TotalShareSize(message = ClientMessage.CHECK_TOTALSHARE_RRRSHARELIST, payload = Localized.class)
    public ObservableList<RrrShareBean> getFilteredRrrShareList() {
        return rrrShareList.getFilteredList();
    }

    public void setRrrShareList(SolaList<RrrShareBean> rrrShareList) {
        this.rrrShareList = rrrShareList;
    }

    public void removeSelectedRrrShare() {
        // Issue #256 Unlink Party from RRR when removing share. 
        if (selectedShare != null && rrrShareList != null) {
            if (getRightHolderList().size() > 0) {
                ListIterator<PartyBean> it = getRightHolderList().listIterator();
                while (it.hasNext()) {
                    getRightHolderList().safeRemove(it.next(), EntityAction.DISASSOCIATE);
                }
            }
            rrrShareList.safeRemove(selectedShare, EntityAction.DELETE);
        }
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        Date oldValue = this.executionDate;
        this.executionDate = executionDate;
        propertySupport.firePropertyChange(EXECUTION_DATE_PROPERTY, oldValue, this.executionDate);
    }

    public BigDecimal getGroundRent() {
        return groundRent;
    }

    /**
     * Calculated remaining ground rent.
     */
    public double getGroundRentRemaining() {
        double rent = 0;
        if (getGroundRent() != null && getGroundRent().compareTo(BigDecimal.ZERO) > 0) {
            double remainingMonths = 12 - Calendar.getInstance().get(Calendar.MONTH) + 4;
            rent = NumberUtility.roundDouble((remainingMonths/12)*getGroundRent().doubleValue(), 2);
        } 
        return rent;
    }
    
    public void setGroundRent(BigDecimal groundRent) {
        BigDecimal oldValue = this.groundRent;
        this.groundRent = groundRent;
        propertySupport.firePropertyChange(GROUND_RENT_PROPERTY, oldValue, this.groundRent);
    }

    public String getLeaseNumber() {
        return leaseNumber;
    }

    public void setLeaseNumber(String leaseNumber) {
        String oldValue = this.leaseNumber;
        this.leaseNumber = leaseNumber;
        propertySupport.firePropertyChange(LEASE_NUMBER_PROPERTY, oldValue, this.leaseNumber);
    }
    
    public ObservableList<LeaseSpecialConditionBean> getFilteredLeaseSpecialConditionList() {
        return leaseSpecialConditionList.getFilteredList();
    }

    public SolaList<LeaseSpecialConditionBean> getLeaseSpecialConditionList() {
        return leaseSpecialConditionList;
    }

    public void setLeaseSpecialConditionList(SolaList<LeaseSpecialConditionBean> leaseSpecialConditionList) {
        this.leaseSpecialConditionList = leaseSpecialConditionList;
    }

    public BigDecimal getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(BigDecimal registrationFee) {
        BigDecimal oldValue = this.registrationFee;
        this.registrationFee = registrationFee;
        propertySupport.firePropertyChange(REGISTRATION_FEE_PROPERTY, oldValue, this.registrationFee);
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        BigDecimal oldValue = this.serviceFee;
        this.serviceFee = serviceFee;
         propertySupport.firePropertyChange(SERVICE_FEE_PROPERTY, oldValue, this.serviceFee);
    }

    public LeaseSpecialConditionBean getSelectedSpecialCondition() {
        return selectedSpecialCondition;
    }

    public void setSelectedSpecialCondition(LeaseSpecialConditionBean selectedSpecialCondition) {
        this.selectedSpecialCondition = selectedSpecialCondition;
        propertySupport.firePropertyChange(SELECTED_SPECIAL_CONDITION_PROPERTY, null, this.selectedSpecialCondition);
    }

    public BigDecimal getStampDuty() {
        return stampDuty;
    }

    public void setStampDuty(BigDecimal stampDuty) {
        BigDecimal oldValue = this.stampDuty;
        this.stampDuty = stampDuty;
        propertySupport.firePropertyChange(STAMP_DUTY_PROPERTY, oldValue, this.stampDuty);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        Date oldValue = this.startDate;
        this.startDate = startDate;
        propertySupport.firePropertyChange(START_DATE_PROPERTY, oldValue, this.startDate);
        propertySupport.firePropertyChange(LEASE_TERM_PROPERTY, null, getLeaseTerm());
    }

    public BigDecimal getTransferDuty() {
        return transferDuty;
    }

    public void setTransferDuty(BigDecimal transferDuty) {
        BigDecimal oldValue = this.transferDuty;
        this.transferDuty = transferDuty;
        propertySupport.firePropertyChange(TRANSFER_DUTY_PROPERTY, oldValue, this.transferDuty);
    }

    public PartyBean getSelectedRightHolder() {
        return selectedRightholder;
    }

    public void setSelectedRightHolder(PartyBean selectedRightholder) {
        this.selectedRightholder = selectedRightholder;
        propertySupport.firePropertyChange(SELECTED_RIGHTHOLDER_PROPERTY, null, this.selectedRightholder);
    }

    public SolaList<PartyBean> getRightHolderList() {
        return rightHolderList;
    }

    @Size(min = 1, groups = {MortgageValidationGroup.class}, message = ClientMessage.CHECK_SIZE_RIGHTHOLDERLIST, payload = Localized.class)
    public ObservableList<PartyBean> getFilteredRightHolderList() {
        return rightHolderList.getFilteredList();
    }

    /**
     * Shortcut to rightholders names.
     */
    public String getRightholdersString() {
        String rightholders = "";
        if (getFilteredRightHolderList() != null && getFilteredRightHolderList().size() > 0) {
            for (PartyBean party : getFilteredRightHolderList()) {
                if (rightholders.equals("")) {
                    rightholders = party.getFullName();
                } else {
                    rightholders = rightholders + ", " + party.getFullName();
                }
            }
        }
        return rightholders;
    }
    
    @Size(min = 1, groups = {SimpleOwnershipValidationGroup.class, LeaseValidationGroup.class},
            message = ClientMessage.CHECK_SIZE_OWNERSLIST, payload = Localized.class)
    private ObservableList<PartyBean> getFilteredOwnersList() {
        return rightHolderList.getFilteredList();
    }

    public void setRightHolderList(SolaList<PartyBean> rightHolderList) {
        this.rightHolderList = rightHolderList;
    }

    public RrrShareBean getSelectedShare() {
        return selectedShare;
    }

    public void setSelectedShare(RrrShareBean selectedShare) {
        RrrShareBean oldValue = this.selectedShare;
        this.selectedShare = selectedShare;
        propertySupport.firePropertyChange(SELECTED_SHARE_PROPERTY, oldValue, this.selectedShare);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        boolean oldValue = this.selected;
        this.selected = selected;
        propertySupport.firePropertyChange(SELECTED_PROPERTY, oldValue, this.selected);
    }

    public void removeSelectedRightHolder() {
        if (selectedRightholder != null) {
            getRightHolderList().safeRemove(selectedRightholder, EntityAction.DISASSOCIATE);
        }
    }

    public BigDecimal getLandUsable() {
        return landUsable;
    }

    public void setLandUsable(BigDecimal landUsable) {
        BigDecimal oldValue = this.landUsable;
        this.landUsable = landUsable;
        propertySupport.firePropertyChange(LAND_USABLE_PROPERTY, oldValue, this.landUsable);
    }

    public BigDecimal getPersonalLevy() {
        return personalLevy;
    }

    public void setPersonalLevy(BigDecimal personalLevy) {
        BigDecimal oldValue = this.personalLevy;
        this.personalLevy = personalLevy;
        propertySupport.firePropertyChange(PERSONAL_LEVY_PROPERTY, oldValue, this.personalLevy);
    }

    public CadastreObjectBean getCadastreObject() {
        return cadastreObject;
    }

    public void setCadastreObject(CadastreObjectBean cadastreObject) {
        this.cadastreObject = cadastreObject;
        propertySupport.firePropertyChange(CADASTRE_OBJECT_PROPERTY, null, this.cadastreObject);
    }

    public String getCadastreObjectId() {
        return cadastreObjectId;
    }

    public void setCadastreObjectId(String cadastreObjectId) {
        this.cadastreObjectId = cadastreObjectId;
    }

    public boolean isSubplotValid() {
        return subplotValid;
    }

    public void setSubplotValid(boolean subplotValid) {
        this.subplotValid = subplotValid;
    }

    public Integer getMortgageTerm() {
        return mortgageTerm;
    }

    public void setMortgageTerm(Integer mortgageTerm) {
        Integer oldValue = this.mortgageTerm;
        this.mortgageTerm = mortgageTerm;
        propertySupport.firePropertyChange(MORTGAGE_TERM_PROPERTY, oldValue, this.mortgageTerm);
       if((getRegistrationDate() != null) && (mortgageTerm.compareTo(Integer.valueOf(0)) > 0)){
            Calendar startDateCal = Calendar.getInstance();
            startDateCal.setTime(getRegistrationDate());
            startDateCal.add(Calendar.YEAR, mortgageTerm);
            setExpirationDate(startDateCal.getTime());
        }
    }

    public void addOrUpdateRightholder(PartyBean rightholder) {
        if (rightholder != null && rightHolderList != null) {
            if (rightHolderList.contains(rightholder)) {
                rightHolderList.set(rightHolderList.indexOf(rightholder), rightholder);
            } else {
                rightHolderList.addAsNew(rightholder);
            }
        }
    }

    public RrrBean makeCopyByAction(RRR_ACTION rrrAction) {
        RrrBean copy = this;

        if (rrrAction == RrrBean.RRR_ACTION.NEW) {
            copy.setStatusCode(StatusConstants.PENDING);
        }

        if (rrrAction == RRR_ACTION.VARY || rrrAction == RRR_ACTION.CANCEL) {
            // Make a copy of current bean with new ID
            copy = this.copy();
            copy.resetIdAndVerion(true, false);
            copy.setRegistrationDate(null);
            copy.setRegistrationNumber(null);
            copy.setStampDuty(null);
            copy.setRegistrationFee(null);
            copy.setTransferDuty(null);
            copy.getNotation().setNotationText(null);
            copy.getSourceList().clear();
        }

        if (rrrAction == RRR_ACTION.EDIT) {
            // Make a copy of current bean preserving all data
            copy = this.copy();
        }

        return copy;
    }

    /** Removes selected lease condition. */
    public void removeSelectedCondition(){
        if(selectedSpecialCondition!=null){
            getLeaseSpecialConditionList().safeRemove(selectedSpecialCondition, EntityAction.DELETE);
        }
    }
    
    /** Calculates lease fees for attached CadastreObject. */
    public void calculateLeaseFees(CadastreObjectBean cadastreObject){
        
         LeaseFeeBean leaseFee;
         
         leaseFee = calcLeaseFees(cadastreObject, this);
         
         setGroundRent(leaseFee.getGroundRent());
         
         setRegistrationFee(leaseFee.getRegistrationFee());
         
         setServiceFee(leaseFee.getServiceFee());
         
         setStampDuty(leaseFee.getStampDuty());
        
    }
    
    public static LeaseFeeBean calcLeaseFees(CadastreObjectBean co, RrrBean rrr){
        
        LeaseFeeTO leaseFee = null;
        
        LeaseFeeBean leaseFeeBean = new LeaseFeeBean();
        
        leaseFee =  WSManager.getInstance().getAdministrative().calculateLeaseFees(
                        TypeConverters.BeanToTrasferObject(co, CadastreObjectTO.class),
                        TypeConverters.BeanToTrasferObject(rrr, RrrTO.class));
        
        TypeConverters.TransferObjectToBean(leaseFee, LeaseFeeBean.class, leaseFeeBean);
        
        return leaseFeeBean;
    }
    
    /**
     * Generates new ID, RowVerion and RowID.
     *
     * @param resetChildren If true, will change ID fields also for child
     * objects.
     * @param removeBaUnitId If true, will set <code>BaUnitId</code> to null.
     */
    public void resetIdAndVerion(boolean resetChildren, boolean removeBaUnitId) {
        generateId();
        resetVersion();
        setTransactionId(null);
        setStatusCode(StatusConstants.PENDING);
        if (removeBaUnitId) {
            setBaUnitId(null);
        }
        if (resetChildren) {
            for (RrrShareBean shareBean : getRrrShareList()) {
                shareBean.resetVersion();
                shareBean.setRrrId(getId());
            }
            getNotation().generateId();
            getNotation().resetVersion();
            if (removeBaUnitId) {
                getNotation().setBaUnitId(null);
            }
        }
    }
}
