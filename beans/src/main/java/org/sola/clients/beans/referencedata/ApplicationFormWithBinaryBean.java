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

import javax.validation.constraints.NotNull;
import org.sola.clients.beans.cache.CacheManager;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.beans.validation.Localized;
import org.sola.common.FileUtility;
import org.sola.common.messaging.ClientMessage;
import org.sola.services.boundary.wsclients.WSManager;

/**
 * Extends {@link ApplicationFormBean} with binary content of the form.
 */
public class ApplicationFormWithBinaryBean extends ApplicationFormBean {
    @NotNull(message = ClientMessage.CHECK_NOTNULL_FILE, payload=Localized.class)
    private byte[] content;
    private String extension;
    
    public ApplicationFormWithBinaryBean(){
        super();
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
    
    /** Returns application form by provided code. */
    public static ApplicationFormWithBinaryBean getApplicationForm(String code){
        String key = CacheManager.APPLICATION_FORM_PREFIX_KEY + code;
        ApplicationFormWithBinaryBean bean = (ApplicationFormWithBinaryBean) CacheManager.get(key);
        if(bean == null){
            // Load from server and save into cache
            bean = TypeConverters.TransferObjectToBean(
            WSManager.getInstance().getReferenceDataService().getApplicationForm(code),
            ApplicationFormWithBinaryBean.class, null);
            if(bean!=null){
                CacheManager.add(key, bean);
            }
        }
        return bean;
    }
    
    /** Opens application form. */
    public void openApplicationForm(){
        if(getContent() != null){
            // open file
            FileUtility.openFile(getContent(), getCode() + "." + getExtension());
        }
    }
    
    /** Opens application form by provided code. */
    public static void openApplicationForm(String code){
        ApplicationFormWithBinaryBean bean = getApplicationForm(code);
        if(bean != null){
            // open file
            FileUtility.openFile(bean.getContent(), code + "." + bean.getExtension());
        }
    }
}
