/*
 * Copyright 2023 Javier Llorente <javier@opensuse.org>.
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
package com.javierllorente.netbeans.rest.client.ui;

import com.javierllorente.netbeans.rest.client.parsers.PostmanUtilities;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

public final class RestClientOptionsPanel extends javax.swing.JPanel {

    public static final String VERIFY_SSL_CERTIFICATES = "verifySslCertificates";
    private final RestClientOptionsOptionsPanelController controller;

    RestClientOptionsPanel(RestClientOptionsOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
        // TODO listen to changes in form fields and call controller.changed()
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        verifySslCertsCheckBox = new javax.swing.JCheckBox();
        postmanCollectionsLabel = new javax.swing.JLabel();
        importPostmanButton = new javax.swing.JButton();
        exportPostmanButton = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(verifySslCertsCheckBox, "Verify SSL Certificates");

        org.openide.awt.Mnemonics.setLocalizedText(postmanCollectionsLabel, "Postman Collections");

        org.openide.awt.Mnemonics.setLocalizedText(importPostmanButton, "Import...");
        importPostmanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importPostmanButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(exportPostmanButton, "Export...");
        exportPostmanButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportPostmanButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(verifySslCertsCheckBox)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(postmanCollectionsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importPostmanButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(exportPostmanButton)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(verifySslCertsCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(postmanCollectionsLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(importPostmanButton)
                        .addComponent(exportPostmanButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void importPostmanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importPostmanButtonActionPerformed
        JFileChooser fileChooser = createJsonChooser("Import from Postman Collection");
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                Path path = fileChooser.getSelectedFile().toPath();
                int imported = PostmanUtilities.importRequests(path);
                String message = (imported == 0)
                        ? "No items imported"
                        : imported + " item(s) successfully imported";
                NotifyDescriptor nd = new NotifyDescriptor
                        .Message(message, NotifyDescriptor.INFORMATION_MESSAGE);
                DialogDisplayer.getDefault().notify(nd);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
                showError(ex.getMessage());
            }
        }
    }//GEN-LAST:event_importPostmanButtonActionPerformed

    private void exportPostmanButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportPostmanButtonActionPerformed
        JFileChooser fileChooser = createJsonChooser("Export to Postman Collection");
        int result = fileChooser.showSaveDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            int exported = 0;
            Path path = fileChooser.getSelectedFile().toPath();
            try {
                exported = PostmanUtilities.exportRequests(path);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
                showError(ex.getMessage());
            }            
            String message = (exported == 0)
                    ? "No items exported"
                    : exported + " item(s) successfully exported";
            NotifyDescriptor nd = new NotifyDescriptor.Message(message,
                    NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(nd);
        }
    }//GEN-LAST:event_exportPostmanButtonActionPerformed
    
    private JFileChooser createJsonChooser(String dialogTitle) {
        JFileChooser fileChooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                File file = getSelectedFile();
                if (file.exists() && getDialogType() == SAVE_DIALOG) {
                    String message = "Are you sure you want to overwrite "
                            + file.getName() + "?";
                    String title = "File already exists";
                    NotifyDescriptor nd = new NotifyDescriptor.Confirmation(message, title,
                            NotifyDescriptor.YES_NO_OPTION);
                    Object result = DialogDisplayer.getDefault().notify(nd);
                    if (result != NotifyDescriptor.YES_OPTION) {
                        return;
                    }
                }
                super.approveSelection();
            }
        };
        fileChooser.setDialogTitle(dialogTitle);
        fileChooser.setAcceptAllFileFilterUsed(false);
        String description = "Postman Collection (*.json)";
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter(description, "json");
        fileChooser.addChoosableFileFilter(extensionFilter);
        return fileChooser;
    }
    
    private void showError(String exception) {
        String message = "There was an error! " + exception;
        NotifyDescriptor nd = new NotifyDescriptor.Message(message,
                NotifyDescriptor.ERROR_MESSAGE);
        DialogDisplayer.getDefault().notify(nd);
    }
    
    void load() {
         verifySslCertsCheckBox.setSelected(NbPreferences.forModule(
                 RestClientOptionsPanel.class).getBoolean(VERIFY_SSL_CERTIFICATES, true));
    }

    void store() {
        NbPreferences.forModule(RestClientOptionsPanel.class).putBoolean(
                VERIFY_SSL_CERTIFICATES, verifySslCertsCheckBox.isSelected());
    }

    boolean valid() {
        // TODO check whether form is consistent and complete
        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exportPostmanButton;
    private javax.swing.JButton importPostmanButton;
    private javax.swing.JLabel postmanCollectionsLabel;
    private javax.swing.JCheckBox verifySslCertsCheckBox;
    // End of variables declaration//GEN-END:variables
}
