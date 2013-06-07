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

