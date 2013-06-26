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
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.application.ApplicationBean;
import org.sola.clients.beans.application.ApplicationServiceBean;
import org.sola.clients.beans.party.PartySummaryBean;
import org.sola.common.DateUtility;
import org.sola.common.NumberToWords;
import org.sola.common.StringUtility;

/**
 * Provides data to be used for generating reports.
 */
public class LeaseReportBean extends AbstractBindingBean {

    private ApplicationBean application;
    private ApplicationServiceBean service;
    private LeaseBean lease;
    private String freeText;

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
    public LeaseReportBean(LeaseBean lease, ApplicationBean application, ApplicationServiceBean service) {
        super();
        this.lease = lease;
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

    public LeaseBean getLease() {
        if (lease == null) {
            lease = new LeaseBean();
        }
        return lease;
    }

    public void setLease(LeaseBean lease) {
        if (lease == null) {
            lease = new LeaseBean();
        }
        this.lease = lease;
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
        if (getLease().getCadastreObject() != null) {
            return getLease().getCadastreObject().toString();
        }
        return "";
    }

    /**
     * Shortcut for the parcel type.
     */
    public String getParcelType() {
        if (getLease().getCadastreObject() != null) {
            if (getLease().getCadastreObject().getCadastreObjectType() != null) {
                return getLease().getCadastreObject().getCadastreObjectType().toString();
            }
        }
        return "";
    }

    /**
     * Shortcut for the parcel official area.
     */
    public String getParcelOfficialArea() {
        if (getLease().getCadastreObject() != null) {
            if (getLease().getCadastreObject().getOfficialAreaSize() != null) {
                return getLease().getCadastreObject().getOfficialAreaSize().toPlainString();
            }
        }
        return "";
    }

    /**
     * Shortcut for the parcel land use.
     */
    public String getParcelLandUse() {
        if (getLease().getCadastreObject() != null) {
            if (getLease().getCadastreObject().getLandUseType() != null) {
                return getLease().getCadastreObject().getLandUseType().toString().toUpperCase();
            }
        }
        return "";
    }

    /**
     * Shortcut for the parcel address.
     */
    public String getParcelAddress() {
        if (getLease().getCadastreObject() != null) {
            return getLease().getCadastreObject().getAddressString().toUpperCase();
        }
        return "";
    }

    /**
     * Shortcut for the first parcel map reference number.
     */
    public String getParcelMapRef() {
        if (getLease().getCadastreObject() != null && getLease().getCadastreObject().getSourceReference() != null) {
            return getLease().getCadastreObject().getSourceReference();
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
     * Shortcut to lessee address.
     */
    public String getLesseeAddress() {
        if (getLease().getLesseeAddress() != null) {
            return getLease().getLesseeAddress().toUpperCase();
        } else {
            return "";
        }
    }

    /**
     * Shortcut to lessees marital status.
     */
    public String getLesseeMaritalStatus() {
        if (getLease().getMaritalStatus() != null) {
            return getLease().getMaritalStatus();
        } else {
            return "";
        }
    }

    /**
     * Shortcut to lessees names.
     */
    public String getLessees() {
        String lessess = "";
        if (getLease().getLessees() != null && getLease().getLessees().size() > 0) {
            for (PartySummaryBean party : getLease().getLessees()) {
                if (lessess.equals("")) {
                    lessess = party.getFullName();
                } else {
                    lessess = lessess + " AND " + party.getFullName();
                }
            }
        }
        return lessess.toUpperCase();
    }
    
    /** Returns true is lessee is private, otherwise false */
    public boolean isLesseePrivate(){
        boolean result = true;
        if(getLease().getLessees()!=null && getLease().getLessees().size()>0){
            result = getLease().getLessees().get(0).getTypeCode().equalsIgnoreCase("naturalPerson");
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
        int result = 0;
        if (lease.getStartDate() != null && lease.getExpirationDate() != null) {
            Calendar startDateCal = Calendar.getInstance();
            Calendar expirationDateCal = Calendar.getInstance();
            startDateCal.setTime(lease.getStartDate());
            expirationDateCal.setTime(lease.getExpirationDate());
            if (startDateCal.before(expirationDateCal)) {
                result = expirationDateCal.get(Calendar.YEAR) - startDateCal.get(Calendar.YEAR);
            }
        }
        return String.valueOf(result);
    }
    
    /**
     * Calculates and returns lease term in years transformed into words.
     */
    public String getLeaseTermWord() {
        NumberToWords.DefaultProcessor processor = new NumberToWords.DefaultProcessor();
        return processor.getName(getLeaseTerm());
    }
}
