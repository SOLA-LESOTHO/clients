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

import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaCodeList;

/**
 * Holds the list of {@link LandGradeTypeBean} objects.
 */
public class LandGradeTypeListBean extends AbstractBindingListBean {

    public static final String SELECTED_LAND_GRADE_TYPE_PROPERTY = "selectedLandGradeType";
    private SolaCodeList<LandGradeTypeBean> landGradeTypeList;
    private LandGradeTypeBean selectedLandGradeType;

    /**
     * Initializes object's instance and populates {@link ObservableList}&lt;
     * {@link LandGradeTypeBean} &gt; with values from the cache.
     */
    public LandGradeTypeListBean() {
        this(false);
    }

    /**
     * Creates object instance.
     *
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public LandGradeTypeListBean(boolean createDummy) {
        this(createDummy, (String) null);
    }

    /**
     * Creates object instance.
     *
     * @param createDummy Indicates whether to add empty object on the list.
     * @param excludedCodes Codes, which should be skipped while filtering.
     */
    public LandGradeTypeListBean(boolean createDummy, String... excludedCodes) {
        super();
        landGradeTypeList = new SolaCodeList<LandGradeTypeBean>(excludedCodes);
        loadList(createDummy);
    }

    /**
     * Loads list of {@link LandGradeTypeBean}.
     *
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public final void loadList(boolean createDummy) {
        loadCodeList(LandGradeTypeBean.class, landGradeTypeList, CacheManager.getLandGradeTypes(), createDummy);
    }

    public void setExcludedCodes(String ... codes){
        landGradeTypeList.setExcludedCodes(codes);
    }
    
    public ObservableList<LandGradeTypeBean> getLandGradeTypeList() {
        return landGradeTypeList.getFilteredList();
    }

    public LandGradeTypeBean getSelectedLandGradeType() {
        return selectedLandGradeType;
    }

    public void setSelectedLandGradeType(LandGradeTypeBean selectedLandGradeType) {
        this.selectedLandGradeType = selectedLandGradeType;
        propertySupport.firePropertyChange(SELECTED_LAND_GRADE_TYPE_PROPERTY,
                null, selectedLandGradeType);
    }
}