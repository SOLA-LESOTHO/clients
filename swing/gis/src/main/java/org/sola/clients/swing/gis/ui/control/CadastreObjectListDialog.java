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
package org.sola.clients.swing.gis.ui.control;

import org.sola.clients.swing.gis.beans.CadastreObjectListBean;

/**
 * Shows list of new cadastre objects and allows to change them
 */
public class CadastreObjectListDialog extends javax.swing.JDialog {
    private CadastreObjectListBean listBean;
    private String applicationNumber;
    private String applicantName;
    
    private CadastreObjectListPanel createListControl(){
        return new CadastreObjectListPanel(listBean, applicationNumber, applicantName);
    }
    
    /**
     * Creates new form CadastreObjectListDialog
     */
    public CadastreObjectListDialog(CadastreObjectListBean listBean, String applicationNumber,
            String applicantName, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.listBean = listBean;
        this.applicationNumber = applicationNumber;
        this.applicantName = applicantName;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cadastreObjectListPanel1 = createListControl();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/gis/ui/control/Bundle"); // NOI18N
        setTitle(bundle.getString("CadastreObjectListDialog.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(670, 478));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cadastreObjectListPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 670, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cadastreObjectListPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.sola.clients.swing.gis.ui.control.CadastreObjectListPanel cadastreObjectListPanel1;
    // End of variables declaration//GEN-END:variables
}
