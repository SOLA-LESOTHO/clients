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
package org.sola.clients.beans.cache;

import java.util.HashMap;

/** 
 * Cache object class with simple methods to store cached objects and values 
 * during the user's session.
 */
public class Cache {
    
    private HashMap cache;
    
    /** Initializes {@link HashMap} object to keep cache data.*/
    Cache()
    {
        cache = new HashMap();
    }
    
    /** Puts object into the cache.*/
    public void put(String name, Object obj)
    {
        cache.put(name, obj);
    }
    
    /** Checks if object exists in the cache.*/
    public boolean contains(String name)
    {
        return cache.containsKey(name);
    }
    
    /** Returns cached object.*/
    public Object get(String name)
    {
        if(cache.containsKey(name))
            return cache.get(name);
        else
            return null;
    }
    
    /** Removes object from the cache. */
    public void remove(String name)
    {
        if(cache.containsKey(name))
            cache.remove(name);
    }
    
    /** Clears all values from the cache. */
    public void clear()
    {
        cache.clear();
    }
}
