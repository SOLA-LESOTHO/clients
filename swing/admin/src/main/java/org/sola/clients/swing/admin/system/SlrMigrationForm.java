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
package org.sola.clients.swing.admin.system;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JFormattedTextField;
import javax.swing.Timer;
import org.sola.clients.swing.common.controls.CalendarForm;
import org.sola.clients.swing.common.tasks.SolaTask;
import org.sola.clients.swing.common.tasks.TaskManager;
import org.sola.clients.swing.ui.ContentPanel;
import org.sola.clients.swing.ui.renderers.FormattersFactory;
import org.sola.common.messaging.ClientMessage;
import org.sola.common.messaging.MessageUtility;
import org.sola.services.boundary.wsclients.WSManager;

/**
 * Form that can be used to transfer and load data from the SLR database into
 * the SOLA database.
 *
 * @author soladev
 */
public class SlrMigrationForm extends ContentPanel {

    /**
     * Creates new form SlrMigrationForm
     */
    public SlrMigrationForm() {
        initComponents();
        customizeForm();
    }

    private void customizeForm() {
        txtAdjudicationArea.setEnabled(true);
        txtModifiedFrom.setEnabled(true);
        btnModifiedFrom.setEnabled(true);
        txtModifiedTo.setEnabled(true);
        btnModifiedTo.setEnabled(true);
        txtProgressMsg.setEditable(false);
        txtRegistrationDate.setEnabled(false);
        btnRegistrationDate.setEnabled(false);
        cbxRegisteredOnly.setEnabled(true);
        cbxMakeCurrent.setEnabled(false);
        if (rbParcelTransfer.isSelected()) {
            cbxRegisteredOnly.setEnabled(false);
        }
        if (rbLeaseTransfer.isSelected()) {
            txtRegistrationDate.setEnabled(true);
            btnRegistrationDate.setEnabled(true);
            if (txtRegistrationDate.getValue() == null) {
                txtRegistrationDate.setValue(new GregorianCalendar(2013, GregorianCalendar.AUGUST, 15).getTime());
            }
        }
        if (rbLeaseAndPartyLoad.isSelected()) {
            cbxMakeCurrent.setEnabled(true);
        }
    }

    private void showCalendar(JFormattedTextField dateField) {
        CalendarForm calendar = new CalendarForm(null, true, dateField);
        calendar.setVisible(true);
    }

    private void refreshProgressMessage() {
        txtProgressMsg.setText("");
        String progressMsg = WSManager.getInstance().getAdminService().getSlrMigrationProgressMessage();
        txtProgressMsg.setText(progressMsg);
    }

    /**
     * Runs the transfer process as a background task
     */
    private void executeTransfer() {
        final String result[] = {""};
        txtProgressMsg.setText("");

        // Create a timer to refresh the progress message every 10 seconds
        final Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                refreshProgressMessage();
            }
        });
        timer.start();

        SolaTask<Void, Void> t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                if (rbSourceTransfer.isSelected()) {
                    setMessage("Transfering source records from SLR database...");
                    result[0] = WSManager.getInstance().getAdminService().transferSlrSource(
                            txtAdjudicationArea.getText(), cbxRegisteredOnly.isSelected(),
                            (Date) txtModifiedFrom.getValue(), (Date) txtModifiedTo.getValue());
                } else if (rbParcelTransfer.isSelected()) {
                    setMessage("Transfering parcel records from SLR database...");
                    result[0] = WSManager.getInstance().getAdminService().transferSlrParcel(
                            txtAdjudicationArea.getText(), (Date) txtModifiedFrom.getValue(),
                            (Date) txtModifiedTo.getValue());
                } else if (rbPartyTransfer.isSelected()) {
                    setMessage("Transfering party records from SLR database...");
                    result[0] = WSManager.getInstance().getAdminService().transferSlrParty(
                            txtAdjudicationArea.getText(), cbxRegisteredOnly.isSelected(),
                            (Date) txtModifiedFrom.getValue(), (Date) txtModifiedTo.getValue());
                } else if (rbLeaseTransfer.isSelected()) {
                    setMessage("Transfering lease records from SLR database...");
                    result[0] = WSManager.getInstance().getAdminService().transferSlrLease(
                            (Date) txtRegistrationDate.getValue(), txtAdjudicationArea.getText(),
                            cbxRegisteredOnly.isSelected(), (Date) txtModifiedFrom.getValue(),
                            (Date) txtModifiedTo.getValue());
                }
                return null;
            }

            @Override
            public void taskDone() {
                timer.stop();
                txtProgressMsg.setText(result[0]);
            }
        };
        TaskManager.getInstance().runTask(t);


    }

    /**
     * Runs the load process as a background task
     */
    private void executeLoad() {
        final String result[] = {""};
        txtProgressMsg.setText("");

        if (rbLeaseAndPartyLoad.isSelected() && cbxMakeCurrent.isSelected()) {
            int btn = MessageUtility.displayMessage(ClientMessage.SLR_MIGRATION_WARNING);
            if (btn != MessageUtility.BUTTON_ONE) {
                return;
            }
        }

        // Create a timer to refresh the progress message every 10 seconds
        final Timer timer = new Timer(10000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                refreshProgressMessage();
            }
        });
        timer.start();

        SolaTask<Void, Void> t = new SolaTask<Void, Void>() {
            @Override
            public Void doTask() {
                if (rbSourceLoad.isSelected()) {
                    setMessage("Loading source...");
                    result[0] = WSManager.getInstance().getAdminService().loadSource();
                } else if (rbParcelLoad.isSelected()) {
                    setMessage("Loading parcels...");
                    result[0] = WSManager.getInstance().getAdminService().loadParcel();
                } else if (rbLeaseAndPartyLoad.isSelected()) {
                    setMessage("Loading leases and parties...");
                    result[0] = WSManager.getInstance().getAdminService().loadLeaseAndParty(cbxMakeCurrent.isSelected());
                } else if (rbRrrSourceLinkLoad.isSelected()) {
                    setMessage("Loading rrr source link...");
                    result[0] = WSManager.getInstance().getAdminService().loadRrrSourceLink();
                }
                return null;
            }

            @Override
            public void taskDone() {
                timer.stop();
                txtProgressMsg.setText(result[0]);
            }
        };
        TaskManager.getInstance().runTask(t);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgTransfer = new javax.swing.ButtonGroup();
        bgLoad = new javax.swing.ButtonGroup();
        headerPanel1 = new org.sola.clients.swing.ui.HeaderPanel();
        jPanel1 = new javax.swing.JPanel();
        groupPanel1 = new org.sola.clients.swing.ui.GroupPanel();
        jToolBar1 = new javax.swing.JToolBar();
        rbSourceTransfer = new javax.swing.JRadioButton();
        rbParcelTransfer = new javax.swing.JRadioButton();
        rbPartyTransfer = new javax.swing.JRadioButton();
        rbLeaseTransfer = new javax.swing.JRadioButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        btnTransfer = new javax.swing.JButton();
        pnlTransfer = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtAdjudicationArea = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtRegistrationDate = new javax.swing.JFormattedTextField();
        jPanel10 = new javax.swing.JPanel();
        btnRegistrationDate = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cbxRegisteredOnly = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        txtModifiedFrom = new javax.swing.JFormattedTextField();
        jPanel17 = new javax.swing.JPanel();
        btnModifiedFrom = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtModifiedTo = new javax.swing.JFormattedTextField();
        jPanel15 = new javax.swing.JPanel();
        btnModifiedTo = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        groupPanel2 = new org.sola.clients.swing.ui.GroupPanel();
        jToolBar2 = new javax.swing.JToolBar();
        rbSourceLoad = new javax.swing.JRadioButton();
        rbParcelLoad = new javax.swing.JRadioButton();
        rbLeaseAndPartyLoad = new javax.swing.JRadioButton();
        rbRrrSourceLinkLoad = new javax.swing.JRadioButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        cbxMakeCurrent = new javax.swing.JCheckBox();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        btnTransfer1 = new javax.swing.JButton();
        groupPanel3 = new org.sola.clients.swing.ui.GroupPanel();
        jToolBar3 = new javax.swing.JToolBar();
        btnRefresh = new org.sola.clients.swing.common.buttons.BtnRefresh();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtProgressMsg = new javax.swing.JTextArea();

        setHeaderPanel(headerPanel1);

        headerPanel1.setRequestFocusEnabled(false);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/sola/clients/swing/admin/system/Bundle"); // NOI18N
        headerPanel1.setTitleText(bundle.getString("SlrMigrationForm.headerPanel1.titleText")); // NOI18N

        groupPanel1.setTitleText(bundle.getString("SlrMigrationForm.groupPanel1.titleText")); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        bgTransfer.add(rbSourceTransfer);
        rbSourceTransfer.setSelected(true);
        rbSourceTransfer.setText(bundle.getString("SlrMigrationForm.rbSourceTransfer.text")); // NOI18N
        rbSourceTransfer.setFocusable(false);
        rbSourceTransfer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        rbSourceTransfer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        rbSourceTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSourceTransferActionPerformed(evt);
            }
        });
        jToolBar1.add(rbSourceTransfer);

        bgTransfer.add(rbParcelTransfer);
        rbParcelTransfer.setText(bundle.getString("SlrMigrationForm.rbParcelTransfer.text")); // NOI18N
        rbParcelTransfer.setFocusable(false);
        rbParcelTransfer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        rbParcelTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbParcelTransferActionPerformed(evt);
            }
        });
        jToolBar1.add(rbParcelTransfer);

        bgTransfer.add(rbPartyTransfer);
        rbPartyTransfer.setText(bundle.getString("SlrMigrationForm.rbPartyTransfer.text")); // NOI18N
        rbPartyTransfer.setFocusable(false);
        rbPartyTransfer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        rbPartyTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbPartyTransferActionPerformed(evt);
            }
        });
        jToolBar1.add(rbPartyTransfer);

        bgTransfer.add(rbLeaseTransfer);
        rbLeaseTransfer.setText(bundle.getString("SlrMigrationForm.rbLeaseTransfer.text")); // NOI18N
        rbLeaseTransfer.setFocusable(false);
        rbLeaseTransfer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        rbLeaseTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbLeaseTransferActionPerformed(evt);
            }
        });
        jToolBar1.add(rbLeaseTransfer);
        jToolBar1.add(jSeparator4);

        btnTransfer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/slr-transfer.png"))); // NOI18N
        btnTransfer.setText(bundle.getString("SlrMigrationForm.btnTransfer.text")); // NOI18N
        btnTransfer.setFocusable(false);
        btnTransfer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTransfer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransferActionPerformed(evt);
            }
        });
        jToolBar1.add(btnTransfer);

        pnlTransfer.setLayout(new java.awt.GridLayout(2, 3, 15, 0));

        txtAdjudicationArea.setText(bundle.getString("SlrMigrationForm.txtAdjudicationArea.text")); // NOI18N

        jLabel1.setText(bundle.getString("SlrMigrationForm.jLabel1.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtAdjudicationArea)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtAdjudicationArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTransfer.add(jPanel2);

        jLabel3.setText(bundle.getString("SlrMigrationForm.jLabel3.text")); // NOI18N

        txtRegistrationDate.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtRegistrationDate.setText(bundle.getString("SlrMigrationForm.txtRegistrationDate.text")); // NOI18N

        btnRegistrationDate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnRegistrationDate.setText(bundle.getString("SlrMigrationForm.btnRegistrationDate.text")); // NOI18N
        btnRegistrationDate.setBorder(null);
        btnRegistrationDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrationDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnRegistrationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnRegistrationDate))
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(txtRegistrationDate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRegistrationDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTransfer.add(jPanel6);

        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        jLabel2.setText(bundle.getString("SlrMigrationForm.jLabel2.text")); // NOI18N

        cbxRegisteredOnly.setText(bundle.getString("SlrMigrationForm.cbxRegisteredOnly.text")); // NOI18N
        cbxRegisteredOnly.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(cbxRegisteredOnly, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxRegisteredOnly)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel4.add(jPanel3);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 99, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 62, Short.MAX_VALUE)
        );

        jPanel4.add(jPanel5);

        pnlTransfer.add(jPanel4);

        jLabel7.setText(bundle.getString("SlrMigrationForm.jLabel7.text")); // NOI18N

        txtModifiedFrom.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtModifiedFrom.setText(bundle.getString("SlrMigrationForm.txtModifiedFrom.text")); // NOI18N

        btnModifiedFrom.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnModifiedFrom.setText(bundle.getString("SlrMigrationForm.btnModifiedFrom.text")); // NOI18N
        btnModifiedFrom.setBorder(null);
        btnModifiedFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifiedFromActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnModifiedFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnModifiedFrom))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addComponent(txtModifiedFrom)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtModifiedFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTransfer.add(jPanel16);

        jLabel6.setText(bundle.getString("SlrMigrationForm.jLabel6.text")); // NOI18N

        txtModifiedTo.setFormatterFactory(FormattersFactory.getInstance().getDateFormatterFactory());
        txtModifiedTo.setText(bundle.getString("SlrMigrationForm.txtModifiedTo.text")); // NOI18N

        btnModifiedTo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/calendar.png"))); // NOI18N
        btnModifiedTo.setText(bundle.getString("SlrMigrationForm.btnModifiedTo.text")); // NOI18N
        btnModifiedTo.setBorder(null);
        btnModifiedTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModifiedToActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnModifiedTo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel15Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnModifiedTo))
        );

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(txtModifiedTo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtModifiedTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTransfer.add(jPanel14);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 198, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 62, Short.MAX_VALUE)
        );

        pnlTransfer.add(jPanel9);

        groupPanel2.setTitleText(bundle.getString("SlrMigrationForm.groupPanel2.titleText")); // NOI18N

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        bgLoad.add(rbSourceLoad);
        rbSourceLoad.setSelected(true);
        rbSourceLoad.setText(bundle.getString("SlrMigrationForm.rbSourceLoad.text")); // NOI18N
        rbSourceLoad.setFocusable(false);
        rbSourceLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        rbSourceLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbSourceLoadActionPerformed(evt);
            }
        });
        jToolBar2.add(rbSourceLoad);

        bgLoad.add(rbParcelLoad);
        rbParcelLoad.setText(bundle.getString("SlrMigrationForm.rbParcelLoad.text")); // NOI18N
        rbParcelLoad.setFocusable(false);
        rbParcelLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        rbParcelLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbParcelLoadActionPerformed(evt);
            }
        });
        jToolBar2.add(rbParcelLoad);

        bgLoad.add(rbLeaseAndPartyLoad);
        rbLeaseAndPartyLoad.setText(bundle.getString("SlrMigrationForm.rbLeaseAndPartyLoad.text")); // NOI18N
        rbLeaseAndPartyLoad.setFocusable(false);
        rbLeaseAndPartyLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        rbLeaseAndPartyLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbLeaseAndPartyLoadActionPerformed(evt);
            }
        });
        jToolBar2.add(rbLeaseAndPartyLoad);

        bgLoad.add(rbRrrSourceLinkLoad);
        rbRrrSourceLinkLoad.setText(bundle.getString("SlrMigrationForm.rbRrrSourceLinkLoad.text")); // NOI18N
        rbRrrSourceLinkLoad.setFocusable(false);
        rbRrrSourceLinkLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        rbRrrSourceLinkLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRrrSourceLinkLoadActionPerformed(evt);
            }
        });
        jToolBar2.add(rbRrrSourceLinkLoad);
        jToolBar2.add(jSeparator2);

        cbxMakeCurrent.setText(bundle.getString("SlrMigrationForm.cbxMakeCurrent.text")); // NOI18N
        cbxMakeCurrent.setFocusable(false);
        cbxMakeCurrent.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(cbxMakeCurrent);
        jToolBar2.add(jSeparator3);

        btnTransfer1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/common/slr-migration.png"))); // NOI18N
        btnTransfer1.setText(bundle.getString("SlrMigrationForm.btnTransfer1.text")); // NOI18N
        btnTransfer1.setFocusable(false);
        btnTransfer1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnTransfer1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransfer1ActionPerformed(evt);
            }
        });
        jToolBar2.add(btnTransfer1);

        groupPanel3.setTitleText(bundle.getString("SlrMigrationForm.groupPanel3.titleText")); // NOI18N

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        btnRefresh.setText(bundle.getString("SlrMigrationForm.btnRefresh.text")); // NOI18N
        btnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });
        jToolBar3.add(btnRefresh);

        txtProgressMsg.setEditable(false);
        txtProgressMsg.setColumns(20);
        txtProgressMsg.setRows(5);
        jScrollPane1.setViewportView(txtProgressMsg);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(groupPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(groupPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(groupPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTransfer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 625, Short.MAX_VALUE)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(groupPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTransfer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(groupPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rbSourceTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSourceTransferActionPerformed
        customizeForm();
    }//GEN-LAST:event_rbSourceTransferActionPerformed

    private void rbParcelTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbParcelTransferActionPerformed
        customizeForm();
    }//GEN-LAST:event_rbParcelTransferActionPerformed

    private void rbPartyTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbPartyTransferActionPerformed
        customizeForm();
    }//GEN-LAST:event_rbPartyTransferActionPerformed

    private void rbLeaseTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbLeaseTransferActionPerformed
        customizeForm();
    }//GEN-LAST:event_rbLeaseTransferActionPerformed

    private void rbSourceLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSourceLoadActionPerformed
        customizeForm();
    }//GEN-LAST:event_rbSourceLoadActionPerformed

    private void rbParcelLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbParcelLoadActionPerformed
        customizeForm();
    }//GEN-LAST:event_rbParcelLoadActionPerformed

    private void rbLeaseAndPartyLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbLeaseAndPartyLoadActionPerformed
        customizeForm();
    }//GEN-LAST:event_rbLeaseAndPartyLoadActionPerformed

    private void rbRrrSourceLinkLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRrrSourceLinkLoadActionPerformed
        customizeForm();
    }//GEN-LAST:event_rbRrrSourceLinkLoadActionPerformed

    private void btnRegistrationDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrationDateActionPerformed
        showCalendar(txtRegistrationDate);
    }//GEN-LAST:event_btnRegistrationDateActionPerformed

    private void btnModifiedToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifiedToActionPerformed
        showCalendar(txtModifiedTo);
    }//GEN-LAST:event_btnModifiedToActionPerformed

    private void btnModifiedFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModifiedFromActionPerformed
        showCalendar(txtModifiedFrom);
    }//GEN-LAST:event_btnModifiedFromActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        refreshProgressMessage();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnTransfer1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransfer1ActionPerformed
        executeLoad();
    }//GEN-LAST:event_btnTransfer1ActionPerformed

    private void btnTransferActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransferActionPerformed
        executeTransfer();
    }//GEN-LAST:event_btnTransferActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgLoad;
    private javax.swing.ButtonGroup bgTransfer;
    private javax.swing.JButton btnModifiedFrom;
    private javax.swing.JButton btnModifiedTo;
    private org.sola.clients.swing.common.buttons.BtnRefresh btnRefresh;
    private javax.swing.JButton btnRegistrationDate;
    private javax.swing.JButton btnTransfer;
    private javax.swing.JButton btnTransfer1;
    private javax.swing.JCheckBox cbxMakeCurrent;
    private javax.swing.JCheckBox cbxRegisteredOnly;
    private org.sola.clients.swing.ui.GroupPanel groupPanel1;
    private org.sola.clients.swing.ui.GroupPanel groupPanel2;
    private org.sola.clients.swing.ui.GroupPanel groupPanel3;
    private org.sola.clients.swing.ui.HeaderPanel headerPanel1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JPanel pnlTransfer;
    private javax.swing.JRadioButton rbLeaseAndPartyLoad;
    private javax.swing.JRadioButton rbLeaseTransfer;
    private javax.swing.JRadioButton rbParcelLoad;
    private javax.swing.JRadioButton rbParcelTransfer;
    private javax.swing.JRadioButton rbPartyTransfer;
    private javax.swing.JRadioButton rbRrrSourceLinkLoad;
    private javax.swing.JRadioButton rbSourceLoad;
    private javax.swing.JRadioButton rbSourceTransfer;
    private javax.swing.JTextField txtAdjudicationArea;
    private javax.swing.JFormattedTextField txtModifiedFrom;
    private javax.swing.JFormattedTextField txtModifiedTo;
    private javax.swing.JTextArea txtProgressMsg;
    private javax.swing.JFormattedTextField txtRegistrationDate;
    // End of variables declaration//GEN-END:variables
}
