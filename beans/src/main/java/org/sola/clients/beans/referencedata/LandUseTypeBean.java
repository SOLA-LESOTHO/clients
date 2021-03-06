/**
 * ******************************************************************************************
 * Copyright (c) 2013 Food and Agriculture Organization of the United Nations
 * (FAO) and the Lesotho Land Administration Authority (LAA). All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,this
 * list of conditions and the following disclaimer. 2. Redistributions in binary
 * form must reproduce the above copyright notice,this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. 3. Neither the names of FAO, the LAA nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT,STRICT LIABILITY,OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * *********************************************************************************************
 */
package org.sola.clients.beans.referencedata;

import org.sola.clients.beans.AbstractCodeBean;

/**
 * Represents reference data object of the <b>cadastre_object_type</b> table.
 * Could be populated from the {@link LandUseTypeTO} object.<br /> For more
 * information see data dictionary <b>Cadastre</b> schema.
 */
public class LandUseTypeBean extends AbstractCodeBean {

    public static final String CODE_RESIDENTIAL = "residential";
    public static final String CODE_COMMERCIAL = "commercial";
    public static final String CODE_INDUSTRIAL = "industrial";
    public static final String CODE_AGRICULTURAL = "agricultural";
    public static final String CODE_OTHER_AGRIC = "agricOther";
    public static final String CODE_INTENSE_AGRIC = "agricIntensive";
    public static final String CODE_IRRIGATED_AGRIC = "agricIrrigated";
    public static final String CODE_NON_IRRIGATED_AGRIC = "agricNonIrrigated";
    public static final String CODE_RANGE_GRAZING = "agricRangeGrazing";

    public LandUseTypeBean() {
        super();
    }

    public Boolean isAgriculturalUse(String landUseCode) {
        
        if (landUseCode.equals(CODE_AGRICULTURAL)){
            return true;
        }
        if (landUseCode.equals(CODE_RANGE_GRAZING)) {
            return true;
        }

        if (landUseCode.equals(CODE_OTHER_AGRIC)) {
            return true;
        }

        if (landUseCode.equals(CODE_INTENSE_AGRIC)) {
            return true;
        }

        if (landUseCode.equals(CODE_IRRIGATED_AGRIC)) {
            return true;
        }

        return landUseCode.equals(CODE_NON_IRRIGATED_AGRIC);
    }

}
