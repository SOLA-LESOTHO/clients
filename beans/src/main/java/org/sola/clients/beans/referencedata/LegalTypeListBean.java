package org.sola.clients.beans.referencedata;

import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingListBean;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.controls.SolaCodeList;

/**
 * Holds list of {@link LegalTypeBean} objects.
 */
public class LegalTypeListBean extends AbstractBindingListBean {
    
    public static final String SELECTED_LEGAL_TYPE_PROPERTY = "selectedLegalType";
    private SolaCodeList<LegalTypeBean> legalTypes;
    private LegalTypeBean selectedLegalType;
    
    /** Default constructor. */
    public LegalTypeListBean(){
        this(false);
    }
    
    /** 
     * Creates object instance.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public LegalTypeListBean(boolean createDummy) {
        super();
        legalTypes = new SolaCodeList<LegalTypeBean>();
        loadList(createDummy);
    }
    
    /** 
     * Loads list of {@link LegalTypeBean}.
     * @param createDummy Indicates whether to add empty object on the list.
     */
    public final void loadList(boolean createDummy) {
        loadCodeList(LegalTypeBean.class, legalTypes, 
                CacheManager.getLegalTypes(), createDummy);
    }

    public ObservableList<LegalTypeBean> getLegalTypes() {
        return legalTypes.getFilteredList();
    }

    public LegalTypeBean getSelectedLegalType() {
        return selectedLegalType;
    }

    public void setSelectedLegalType(LegalTypeBean selectedLegalType) {
        this.selectedLegalType = selectedLegalType;
        LegalTypeBean oldValue = this.selectedLegalType;
        this.selectedLegalType = selectedLegalType;
        propertySupport.firePropertyChange(SELECTED_LEGAL_TYPE_PROPERTY, 
                oldValue, this.selectedLegalType);
    }
}

