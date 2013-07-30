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
    
    public ArrayList<String> getDisplayValues() {
       
        ListIterator<LegalTypeBean> iterator =  legalTypeListBean.listIterator();
        
        ArrayList<String> dv = new ArrayList();
        while(iterator.hasNext()){
            dv.add( iterator.next().getDisplayValue());
        }
        
        return dv;
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

