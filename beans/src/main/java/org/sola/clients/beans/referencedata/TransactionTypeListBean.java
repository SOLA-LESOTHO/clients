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
package org.sola.clients.beans.referencedata;

import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaCodeList;

/**
 * Holds list of {@link TransactionTypeBean} objects.
 */
public class TransactionTypeListBean extends AbstractBindingListBean {
    public static final String SELECTED_TRANSACTION_TYPE_PROPERTY = "selectedTransactionType";
    private SolaCodeList<TransactionTypeBean> transactionTypes;
    private TransactionTypeBean selectedTransactionType;
    
    public TransactionTypeListBean(){
        this(false);
    }
    
    /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public TransactionTypeListBean(boolean createDummy) {
        this(createDummy, (String) null);
    }
    
    /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     * @param excludedCodes Codes, which should be skipped while filtering.
     */
    public TransactionTypeListBean(boolean createDummy, String ... excludedCodes) {
        super();
        transactionTypes = new SolaCodeList<TransactionTypeBean>(excludedCodes);
        loadList(createDummy);
    }
    
    /** 
     * Loads list of {@link TransactionTypeBean}.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public final void loadList(boolean createDummy) {
        loadCodeList(TransactionTypeBean.class, transactionTypes, CacheManager.getTransactionTypes(), createDummy);
    }

    public ObservableList<TransactionTypeBean> getTransactionTypes() {
        return transactionTypes.getFilteredList();
    }

    public void setExcludedCodes(String ... codes){
        transactionTypes.setExcludedCodes(codes);
    }
    
    public TransactionTypeBean getSelectedTransactionType() {
        return selectedTransactionType;
    }

    public void getSelectedTransactionType(TransactionTypeBean selectedTransactionType) {
        TransactionTypeBean oldValue = this.selectedTransactionType;
        this.selectedTransactionType = selectedTransactionType;
        propertySupport.firePropertyChange(SELECTED_TRANSACTION_TYPE_PROPERTY, oldValue, this.selectedTransactionType);
    }
}
