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

import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.clients.beans.party.PartyBean;
import org.sola.clients.beans.party.PartySearchResultBean;
import org.sola.webservices.transferobjects.casemanagement.PartyTO;
import org.sola.webservices.transferobjects.administrative.DisputePartyTO;

public class DisputePartyBean extends PartyBean {

    public static final String ID_PROPERTY = "id";
    public static final String DISPUTE_NR_PROPERTY = "disputeNr";
    public static final String PARTY_ROLE_PROPERTY = "partyRole";
    public static final String PARTY_NAME_PROPERTY = "partyName";
    public static final String PARTY_ID_PROPERTY = "partyId";
    private String id;
    private String disputeNr;
    private String partyRole;
    private String partyId;
    private String partyName;
    private PartyBean partyBean;
    private PartySearchResultBean partyResultBean;
    private PartyTO partyTO;

    public DisputePartyBean() {
        super();
    }

    public void clean() {
        this.setId(null);
        this.setDisputeNr(null);
        this.setPartyId(null);
        this.setPartyName(null);
        this.setPartyRole(null);

    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String value) {
        String old = partyName;
        partyName = value;
        propertySupport.firePropertyChange(PARTY_NAME_PROPERTY, old, partyName);
    }

    public String getDisputeNr() {
        return disputeNr;
    }

    public void setDisputeNr(String value) {
        String old = disputeNr;
        disputeNr = value;
        propertySupport.firePropertyChange(DISPUTE_NR_PROPERTY, old, disputeNr);
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String value) {
        String old = partyId;
        partyId = value;
        propertySupport.firePropertyChange(PARTY_ID_PROPERTY, old, partyId);
    }

    public String getPartyRole() {
        return partyRole;
    }

    public void setPartyRole(String value) {
        String old = partyRole;
        partyRole = value;
        propertySupport.firePropertyChange(PARTY_ROLE_PROPERTY, old, partyRole);
    }

    public void addChosenParty(PartySearchResultBean partyResultBean, String disputeId) {
        if (partyResultBean != null) {
            setPartyName(partyResultBean.getName());
            setDisputeNr(disputeId);
            setPartyId(partyResultBean.getId());
        }
    }
     public void addNewParty(PartyBean partyBean, String disputeId) {
        if (partyBean != null) {
            setPartyName(partyBean.getName());
            setDisputeNr(disputeId);
            setPartyId(partyBean.getId());
        }
    }

    public boolean saveDisputeParty() {
        DisputePartyTO disputeParty = TypeConverters.BeanToTrasferObject(this, DisputePartyTO.class);
        disputeParty = WSManager.getInstance().getAdministrative().saveDisputeParty(disputeParty);
        TypeConverters.TransferObjectToBean(disputeParty, DisputePartyBean.class, this);
        return true;
    }
}
