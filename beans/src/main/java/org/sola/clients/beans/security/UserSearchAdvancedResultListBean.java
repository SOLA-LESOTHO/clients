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
package org.sola.clients.beans.security;

import java.util.Iterator;
import java.util.List;
import org.jdesktop.observablecollections.ObservableList;
import org.sola.clients.beans.AbstractBindingBean;
import org.sola.clients.beans.controls.SolaObservableList;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.services.boundary.wsclients.WSManager;
import org.sola.webservices.transferobjects.search.UserSearchAdvancedResultTO;
import org.sola.webservices.transferobjects.search.UserSearchParamsTO;
import org.sola.webservices.transferobjects.search.UserSearchResultTO;

/**
 * Holds the list of {@link UserSearchAdvancedResultBean} objects and used to 
 * populate comboboxes or listboxes controls.
 */
public class UserSearchAdvancedResultListBean extends AbstractBindingBean {
    
    public static final String SELECTED_USER_PROPERTY = "selectedUser";
    private SolaObservableList<UserSearchAdvancedResultBean> usersList;
    private UserSearchAdvancedResultBean selectedUser;
    
    /** Creates object instance. */
    public UserSearchAdvancedResultListBean(){
        super();
        usersList = new SolaObservableList<UserSearchAdvancedResultBean>();
    }

    public UserSearchAdvancedResultBean getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(UserSearchAdvancedResultBean selectedUser) {
        this.selectedUser = selectedUser;
        propertySupport.firePropertyChange(SELECTED_USER_PROPERTY, null, this.selectedUser);
    }

    public ObservableList<UserSearchAdvancedResultBean> getUsersList() {
        return usersList;
    }
    
    /** Populates the list of users with active users. */
    private void loadActiveUsers() {
        if (WSManager.getInstance().getSearchService() != null) {
            List<UserSearchResultTO> userListTO = WSManager.getInstance().getSearchService().getActiveUsers();
            TypeConverters.TransferObjectListToBeanList(userListTO, UserSearchResultBean.class, (List) usersList);
        }
    }
    
    /** 
     * Searches users by the given parameters and populates list. 
     * @param searchParam Search criteria.
     */
    public void searchUsers(UserSearchParamsBean searchParam){
        usersList.clear();
        if (WSManager.getInstance().getSearchService() != null && searchParam!=null) {
            List<UserSearchAdvancedResultTO> userListTO = WSManager.getInstance()
                    .getSearchService().searchUsers(TypeConverters
                    .BeanToTrasferObject(searchParam, UserSearchParamsTO.class));
            TypeConverters.TransferObjectListToBeanList(userListTO, 
                    UserSearchAdvancedResultBean.class, (List) usersList);
        }
    }
    
    public void setSelectedUserById(String userId) {
        if (usersList != null) {
            for (Iterator<UserSearchAdvancedResultBean> it = usersList.iterator(); it.hasNext();) {
                UserSearchAdvancedResultBean user = it.next();
                if (user.getId().equals(userId)) {
                    selectedUser = user;
                    break;
                }
            }
        }
        propertySupport.firePropertyChange(SELECTED_USER_PROPERTY, null, selectedUser);
    }
}
