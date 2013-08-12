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

import java.util.List;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.search.CadastreObjectSearchParamsTO;
import org.sola.webservices.transferobjects.search.CadastreObjectSearchResultTO;

/**
 * List of cadastre objects
 */
public class CadastreObjectSearchResultListBean extends AbstractBindingBean {

    public static final String SELECTED_CADASTRE_OBJECT = "selectedCadastreObject";
    SolaObservableList<CadastreObjectSearchResultBean> cadastreObjects;
    CadastreObjectSearchResultBean selectedCadastreObject;

    public CadastreObjectSearchResultListBean() {
        super();
        cadastreObjects = new SolaObservableList<CadastreObjectSearchResultBean>();
    }

    public SolaObservableList<CadastreObjectSearchResultBean> getCadastreObjects() {
        return cadastreObjects;
    }

    public CadastreObjectSearchResultBean getSelectedCadastreObject() {
        return selectedCadastreObject;
    }

    public void setSelectedCadastreObject(CadastreObjectSearchResultBean selectedCadastreObject) {
        this.selectedCadastreObject = selectedCadastreObject;
        propertySupport.firePropertyChange(SELECTED_CADASTRE_OBJECT, null, this.selectedCadastreObject);
    }

    /**
     * Searches cadastre objects with the given parameters.
     */
    public void search(CadastreObjectSearchParamsBean params) {
        if (params == null) {
            return;
        }

        List< CadastreObjectSearchResultTO> result = WSManager.getInstance().getSearchService()
                .searchCadastreObjects(TypeConverters.BeanToTrasferObject(params,
                CadastreObjectSearchParamsTO.class));
 
        TypeConverters.TransferObjectListToBeanList(result, CadastreObjectSearchResultBean.class,
                (List) getCadastreObjects());
    }
}
