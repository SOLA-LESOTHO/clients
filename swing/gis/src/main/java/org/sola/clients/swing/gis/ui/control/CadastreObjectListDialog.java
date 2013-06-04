package org.sola.clients.swing.gis.ui.control;

import org.sola.clients.swing.gis.beans.CadastreObjectListBean;

/**
 * Shows list of new cadastre objects and allows to change them
 */
public class CadastreObjectListDialog extends javax.swing.JDialog {
    private CadastreObjectListBean listBean;
    private String applicationNumber;
    
    private CadastreObjectListPanel createListControl(){
        return new CadastreObjectListPanel(listBean, applicationNumber);
    }
    
    /**
     * Creates new form CadastreObjectListDialog
     */
    public CadastreObjectListDialog(CadastreObjectListBean listBean, String applicationNumber, 
            java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.listBean = listBean;
        this.applicationNumber = applicationNumber;
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
