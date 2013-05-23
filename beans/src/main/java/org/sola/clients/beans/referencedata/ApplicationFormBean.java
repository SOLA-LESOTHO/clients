package org.sola.clients.beans.referencedata;

import org.sola.clients.beans.AbstractCodeBean;
import org.sola.webservices.transferobjects.referencedata.ApplicationFormTO;

/**
 * Represents reference data object of the <b>client_type</b> table. Could be
 * populated from the {@link ApplicationFormTO} object.<br /> 
 */
public class ApplicationFormBean extends AbstractCodeBean {
    
    public static final String CONTENT_PROPERTY = "content";
    
    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
    
    public ApplicationFormBean() {
        super();
    }  
}

