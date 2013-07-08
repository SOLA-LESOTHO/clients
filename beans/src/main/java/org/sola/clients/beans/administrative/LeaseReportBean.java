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
package org.sola.clients.beans.administrative;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.codehaus.stax2.ri.typed.NumberUtil;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.cadastre.CadastreObjectBean;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.common.DateUtility;
import org.sola.common.NumberToWords;
import org.sola.common.NumberUtility;
import org.sola.common.StringUtility;

/**
 * Provides data to be used for generating reports.
 */
public class LeaseReportBean extends AbstractBindingBean {

    private ApplicationBean application;
    private ApplicationServiceBean service;
    private RrrBean lease;
    private String freeText;
    private CadastreObjectBean cadastreObject;

    /**
     * Default constructor.
     */
    public LeaseReportBean() {
        super();
    }

    /**
     * Class constructor with initial values for BaUnit, RRR, Application and
     * ApplicationService object.
     */
    public LeaseReportBean(RrrBean lease, CadastreObjectBean cadastreObject, ApplicationBean application, ApplicationServiceBean service) {
        super();
        this.lease = lease;
        this.cadastreObject = cadastreObject;
        this.application = application;
        this.service = service;
    }

    public String getFreeText() {
        if (freeText == null) {
            freeText = "";
        }
        return freeText;
    }

    public void setFreeText(String freeText) {
        if (freeText == null) {
            freeText = "";
        }
        this.freeText = freeText;
    }

    public RrrBean getLease() {
        if (lease == null) {
            lease = new RrrBean();
        }
        return lease;
    }

    public void setLease(RrrBean lease) {
        if (lease == null) {
            lease = new RrrBean();
        }
        this.lease = lease;
    }

    public CadastreObjectBean getCadastreObject() {
        if (cadastreObject == null) {
            cadastreObject = new CadastreObjectBean();
        }
        return cadastreObject;
    }

    public void setCadastreObject(CadastreObjectBean cadastreObject) {
        this.cadastreObject = cadastreObject;
    }

    public ApplicationBean getApplication() {
        if (application == null) {
            application = new ApplicationBean();
        }
        return application;
    }

    public void setApplication(ApplicationBean application) {
        if (application == null) {
            application = new ApplicationBean();
        }
        this.application = application;
    }

    public ApplicationServiceBean getService() {
        if (service == null) {
            service = new ApplicationServiceBean();
        }
        return service;
    }

    public void setService(ApplicationServiceBean service) {
        if (service == null) {
            service = new ApplicationServiceBean();
        }
        this.service = service;
    }

    /**
     * Shortcut for application number.
     */
    public String getApplicationNumber() {
        return StringUtility.empty(getApplication().getNr());
    }

    /**
     * Shortcut for application date, converted to string.
     */
    public String getApplicationDate() {
        return DateUtility.getShortDateString(getApplication().getLodgingDatetime(), true);
    }

    /**
     * Shortcut for applicant's full name.
     */
    public String getApplicantName() {
        if (getApplication().getContactPerson() != null && getApplication().getContactPerson().getFullName() != null) {
            return getApplication().getContactPerson().getFullName();
        }
        return "";
    }

    /**
     * Shortcut for service name.
     */
    public String getServiceName() {
        if (getService() != null && getService().getRequestType() != null
                && getService().getRequestType().getDisplayValue() != null) {
            return getService().getRequestType().getDisplayValue();
        }
        return "";
    }

    /**
     * Shortcut for the parcel first/last name parts.
     */
    public String getParcelCode() {
        if (getCadastreObject() != null) {
            return getCadastreObject().toString();
        }
        return "";
    }

    /**
     * Shortcut for the parcel type.
     */
    public String getParcelType() {
        if (getCadastreObject() != null) {
            if (getCadastreObject().getCadastreObjectType() != null) {
                return getCadastreObject().getCadastreObjectType().toString();
            }
        }
        return "";
    }

    /**
     * Shortcut for the parcel official area.
     */
    public String getParcelOfficialArea() {
        if (getCadastreObject() != null) {
            if (getCadastreObject().getOfficialAreaSize() != null) {
                return getCadastreObject().getOfficialAreaSize().toPlainString();
            }
        }
        return "";
    }

    /**
     * Shortcut for the parcel land use.
     */
    public String getParcelLandUse() {
        if (getLease().getLandUseType() != null) {
            return getLease().getLandUseType().getDisplayValue().toUpperCase();
        }
        return "";
    }

    /**
     * Shortcut for the parcel address.
     */
    public String getParcelAddress() {
        if (getCadastreObject() != null) {
            return getCadastreObject().getAddressString().toUpperCase();
        }
        return "";
    }

    /**
     * Shortcut for the first parcel map reference number.
     */
    public String getParcelMapRef() {
        if (getCadastreObject() != null && getCadastreObject().getSourceReference() != null) {
            return getCadastreObject().getSourceReference();
        }
        return "";
    }

    /**
     * Shortcut for the lease registration number.
     */
    public String getLeaseRegistrationNumber() {
        if (getLease().getLeaseNumber() != null) {
            return getLease().getLeaseNumber();
        }
        return "";
    }

    /**
     * Shortcut for the lease execution date in MEDIUM format without time.
     */
    public String getLeaseExecutionDate() {
        return DateUtility.getMediumDateString(getLease().getExecutionDate(), false);
    }

    /**
     * Shortcut for the lease execution day.
     */
    public String getLeaseExecutionDay() {
        if (getLease().getExecutionDate() == null) {
            return "";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(getLease().getExecutionDate());
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Shortcut for the lease execution year and month.
     */
    public String getLeaseExecutionMonthAndYear() {
        if (getLease().getExecutionDate() == null) {
            return "";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(getLease().getExecutionDate());
        return new SimpleDateFormat("MMMMM").format(cal.getTime()) + " " + String.valueOf(cal.get(Calendar.YEAR));
    }

    /**
     * Shortcut for the lease expiration date in MEDIUM format without time.
     */
    public String getLeaseExpirationDate() {
        return DateUtility.getMediumDateString(getLease().getExpirationDate(), false);
    }

    /**
     * Shortcut for the lease expiration day.
     */
    public String getLeaseExpirationDay() {
        if (getLease().getExpirationDate() == null) {
            return "";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(getLease().getExpirationDate());
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Shortcut for the lease expiration year and month.
     */
    public String getLeaseExpirationMonthAndYear() {
        if (getLease().getExpirationDate() == null) {
            return "";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(getLease().getExpirationDate());
        return new SimpleDateFormat("MMMMM").format(cal.getTime()) + " " + String.valueOf(cal.get(Calendar.YEAR));
    }

    /**
     * Shortcut for the lease start date in MEDIUM format without time.
     */
    public String getLeaseStartDate() {
        return DateUtility.getMediumDateString(getLease().getStartDate(), false);
    }

    /**
     * Shortcut for the lease start day.
     */
    public String getLeaseStartDay() {
        if (getLease().getStartDate() == null) {
            return "";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(getLease().getStartDate());
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Shortcut for the lease start year and month.
     */
    public String getLeaseStartMonthAndYear() {
        if (getLease().getStartDate() == null) {
            return "";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(getLease().getStartDate());
        return new SimpleDateFormat("MMMMM").format(cal.getTime()) + " " + String.valueOf(cal.get(Calendar.YEAR));
    }

    /**
     * Shortcut for the ground rent.
     */
    public String getGroundRent() {
        if (getLease().getGroundRent() != null && getLease().getGroundRent().compareTo(BigDecimal.ZERO) > 0) {
            return "M" + getLease().getGroundRent().toPlainString();
        } else {
            return "NIL";
        }
    }
    
    /**
     * Calculated remaining ground rent.
     */
    public String getGroundRentRemaining() {
        if (getLease().getGroundRentRemaining() > 0) {
            return "M" + String.valueOf(getLease().getGroundRentRemaining());
        } else {
            return "NIL";
        }
    }

    /**
     * Shortcut to lessee address.
     */
    public String getLesseeAddress() {
        String address = "";
        if (getLease().getRightHolderList().size() > 0) {
            for (PartyBean party : getLease().getFilteredRightHolderList()) {
                if (party.getAddress() != null && !StringUtility.empty(party.getAddress().getDescription()).equals("")) {
                    address = party.getAddress().getDescription();
                    break;
                }
            }
        }
        return address.toUpperCase();
    }

    /**
     * Shortcut to lessees marital status.
     */
    public String getLesseeMaritalStatus() {
        String legalStatus = "";
        if (getLease().getRightHolderList().size() > 0) {
            for (PartyBean party : getLease().getFilteredRightHolderList()) {
                if (!StringUtility.empty(party.getLegalType()).equals("")) {
                    legalStatus = party.getLegalType();
                    break;
                }
            }
        }
        return legalStatus.toUpperCase();
    }

    /**
     * Shortcut to lessees names.
     */
    public String getLessees() {
        String lessess = "";
        if (getLease().getFilteredRightHolderList() != null && getLease().getFilteredRightHolderList().size() > 0) {
            for (PartyBean party : getLease().getFilteredRightHolderList()) {
                if (lessess.equals("")) {
                    lessess = party.getFullName();
                } else {
                    lessess = lessess + " AND " + party.getFullName();
                }
            }
        }
        return lessess.toUpperCase();
    }

    /**
     * Returns true is lessee is private, otherwise false
     */
    public boolean isLesseePrivate() {
        boolean result = true;
        if (getLease().getRightHolderList() != null && getLease().getRightHolderList().size() > 0) {
            result = getLease().getRightHolderList().get(0).getTypeCode().equalsIgnoreCase("naturalPerson");
        }
        return result;
    }

    /**
     * Shortcut to lessees and marital status.
     */
    public String getLesseesAndMaritalStatus() {
        String result = getLessees();
        if (!result.equals("") && !getLesseeMaritalStatus().equals("")) {
            result = result + " - " + getLesseeMaritalStatus();
        }
        return result;
    }

    /**
     * Calculates and returns lease term in years.
     */
    public String getLeaseTerm() {
        return String.valueOf(getLease().getLeaseTerm());
    }

    /**
     * Calculates and returns lease term in years transformed into words.
     */
    public String getLeaseTermWord() {
        NumberToWords.DefaultProcessor processor = new NumberToWords.DefaultProcessor();
        return processor.getName(getLeaseTerm());
    }
}
