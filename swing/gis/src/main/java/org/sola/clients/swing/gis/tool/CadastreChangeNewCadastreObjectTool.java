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
package org.sola.clients.swing.gis.tool;

import com.vividsolutions.jts.geom.Geometry;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.Geometries;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.extended.util.Messaging;
import org.opengis.feature.simple.SimpleFeature;
import org.sola.clients.beans.converters.GenericTranslatorListener;
import org.sola.clients.beans.converters.TypeConverters;
import org.sola.clients.swing.gis.beans.CadastreObjectBean;
import org.sola.clients.swing.gis.layer.CadastreChangeNewCadastreObjectLayer;
import org.sola.clients.swing.ui.cadastre.ParcelDialog;
import org.sola.clients.swing.ui.cadastre.ParcelPanel;
import org.sola.common.mapping.MappingManager;
import org.sola.common.messaging.GisMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Tool that is used to draw new cadastre objects during the cadastre change.
 *
 * @author rizzom
 */
public class CadastreChangeNewCadastreObjectTool
        extends CadastreChangeEditAbstractTool implements TargetCadastreObjectTool {

    public final static String NAME = "new-parcel";
    private String toolTip = MessageUtility.getLocalizedMessage(
            GisMessage.CADASTRE_CHANGE_TOOLTIP_NEW_PARCEL).getMessage();
    private String cadastreObjectType;

    /**
     * Constructor
     *
     * @param newCadastreObjectLayer The layer where the new cadastre objects
     * are maintained
     */
    public CadastreChangeNewCadastreObjectTool(
            CadastreChangeNewCadastreObjectLayer newCadastreObjectLayer) {
        this.setToolName(NAME);
        this.setIconImage("resources/new-parcel.png");
        this.setToolTip(toolTip);
        this.setGeometryType(Geometries.POLYGON);
        this.layer = newCadastreObjectLayer;
    }

    @Override
    public void setCadastreObjectType(String cadastreObjectType) {
        this.cadastreObjectType = cadastreObjectType;
    }

    /**
     * If a new click is done while creating a cadastre object, it has to snap
     * to a point. Because the only layer used as snaptarget is the
     * NewSurveyPointLayer the only points are the survey points.
     *
     * @param ev
     */
    @Override
    public void onMouseClicked(MapMouseEvent ev) {
        if (ev.getButton() == java.awt.event.MouseEvent.BUTTON1
                && this.getSnappedTarget() != SNAPPED_TARGET_TYPE.Vertex) {
            Messaging.getInstance().show(GisMessage.CADASTRE_CHANGE_NEW_CO_MUST_SNAP);
            return;
        }
        super.onMouseClicked(ev);
    }

    /**
     * It means a vertex of a cadastre object cannot be changed from this tool.
     * It must be changed by changing the vertices in the NewSurveyPointLayer.
     *
     * @param mousePositionInMap
     * @return
     */
    @Override
    protected SimpleFeature treatChangeVertex(DirectPosition2D mousePositionInMap) {
        return null;
    }

    @Override
    public SimpleFeature addFeature(Geometry geometry) {
        org.sola.clients.beans.cadastre.CadastreObjectBean formBean = new org.sola.clients.beans.cadastre.CadastreObjectBean();
        formBean.setNameFirstpart(getLayer().getLastPart());
        formBean.setNameLastpart(getLayer().getNameFirstPart());
        if(geometry!=null){
            formBean.setOfficialAreaSize(new BigDecimal(geometry.getArea()));
            formBean.setCalculatedArea(new BigDecimal(geometry.getArea()));
        }
        ParcelDialog form = new ParcelDialog(formBean, false, null, true);
        
        final CadastreObjectBean[] beans = new CadastreObjectBean[1];
        
        // AM - Multi-SRID change
        // Need to explicitly set the SRID of the map on the geometry. 
        final Geometry geom = this.layer.setSridOnGeometry(geometry); 

        form.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals(ParcelDialog.SELECTED_PARCEL)) {
                    // Convert between CadastreObject in the GIS project and CadastreObject in the Clients Beans
                    org.sola.clients.beans.cadastre.CadastreObjectBean bean =
                            (org.sola.clients.beans.cadastre.CadastreObjectBean) evt.getNewValue();
                    CadastreObjectBean bean2 = MappingManager.getMapper().map(bean, CadastreObjectBean.class);
                    // Fix problem with list area list duplications
                    bean2.getSpatialValueAreaList().clear();
                    bean2.getSpatialValueAreaList().addAll(bean.getSpatialValueAreaList());
                    
                    bean2.setFeatureGeom(geom);
                    getLayer().getBeanList().add(bean2);
                    beans[0] = bean2;
                }
            }
        });
        
        form.setVisible(true);
        if (beans[0] != null) {
            return getLayer().getFeatureByCadastreObjectId(beans[0].getId());
        }
        return null;
    }

    /**
     * Gets the layer where the new cadastre objects are added.
     *
     * @return
     */
    private CadastreChangeNewCadastreObjectLayer getLayer() {
        return (CadastreChangeNewCadastreObjectLayer) this.layer;
    }
}
