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
package org.sola.clients.beans.referencedata;

import java.util.ArrayList;
import java.util.ListIterator;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaCodeList;
import org.sola.clients.beans.controls.SolaObservableList;

/**
 * Holds list of {@link LegalTypeBean} objects.
 */
public class LegalTypeListBean extends AbstractBindingListBean {
    
    public static final String SELECTED_GENDERTYPE_PROPERTY = "selectedLegalType";
    private SolaCodeList<LegalTypeBean> legalTypeListBean;
    private LegalTypeBean selectedLegalTypeBean;
    
    public LegalTypeListBean() {
        this(false);
    }
    
    /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public LegalTypeListBean(boolean createDummy) {
        this(createDummy, (String) null);
    }
    
    /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     * @param excludedCodes Codes, which should be skipped while filtering.
     */
    public LegalTypeListBean(boolean createDummy, String ... excludedCodes) {
        super();
        legalTypeListBean = new SolaCodeList<LegalTypeBean>(excludedCodes);
        loadList(createDummy);
    }
    
    /** 
     * Loads list of {@link LegalTypeBean}.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public final void loadList(boolean createDummy) {
        loadCodeList(LegalTypeBean.class, legalTypeListBean, 
                CacheManager.getLegalTypes(), createDummy);
    }
    
    public ObservableList<LegalTypeBean> getLegalTypeList() {
        return legalTypeListBean.getFilteredList();
    }
    
    /** 
     * Loads list of {@link LegalTypeBean.getDisplayValues()}.
     * 
     */
    public ArrayList<String> getDisplayValues() {
       
        ListIterator<LegalTypeBean> iterator =  legalTypeListBean.listIterator();
        
        ArrayList<String> displayVls = new ArrayList();
        while(iterator.hasNext()){
            displayVls.add( iterator.next().getDisplayValue());
        }
        
        return displayVls;
    }
    
    public void setExcludedCodes(String ... codes){
        legalTypeListBean.setExcludedCodes(codes);
    }
    
    public LegalTypeBean getSelectedLegalType() {
        return selectedLegalTypeBean;
    }

    public void setSelectedLegalType(LegalTypeBean value) {
        selectedLegalTypeBean = value;
        propertySupport.firePropertyChange(SELECTED_GENDERTYPE_PROPERTY, null, value);
    }

}

