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
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaCodeList;

/**
 * Holds list of {@link ApplicationFormBean} objects.
 */
public class ApplicationFormListBean extends AbstractBindingListBean {
    
    public static final String SELECTED_APPLICATION_FORM_PROPERTY = "selectedApplicationForm";
    private SolaCodeList<ApplicationFormBean> ApplicationForms;
    private ApplicationFormBean selectedApplicationForm;
    
    /** Default constructor. */
    public ApplicationFormListBean(){
        this(false);
    }
    
    /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public ApplicationFormListBean(boolean createDummy) {
        super();
        ApplicationForms = new SolaCodeList<ApplicationFormBean>();
        loadList(createDummy);
    }
    
    /** 
     * Loads list of {@link ApplicationFormBean}.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public final void loadList(boolean createDummy) {
        loadCodeList(ApplicationFormBean.class, ApplicationForms, 
                CacheManager.getApplicationForms(), createDummy);
    }

    public ObservableList<ApplicationFormBean> getApplicationForms() {
        return ApplicationForms.getFilteredList();
    }

    public ApplicationFormBean getSelectedApplicationForm() {
        return selectedApplicationForm;
    }

    public void setSelectedApplicationForm(ApplicationFormBean selectedApplicationForm) {
        ApplicationFormBean oldValue = this.selectedApplicationForm;
        this.selectedApplicationForm = selectedApplicationForm;
        propertySupport.firePropertyChange(SELECTED_APPLICATION_FORM_PROPERTY, 
                oldValue, this.selectedApplicationForm);
    }
}

