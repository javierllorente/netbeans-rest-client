/*
 * Copyright 2022-2024 Javier Llorente <javier@opensuse.org>.
 * Copyright 2025        Luca Bartoli <lbdevweb@gmail.com>
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
package com.lb.netbeans.rest.client.ui;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Javier Llorente 
 * @author Luca Bartoli <lbdevweb@gmail.com>
 */
public class TablePanel extends javax.swing.JPanel {
    
    private static final Logger logger = Logger.getLogger(TablePanel.class.getName());
    private static final int ENABLE_COLUMN = 0;
    private static final int KEY_COLUMN = 1;
    private static final int VALUE_COLUMN = 2;

    /**
     * Creates new form TablePanel
     */
    public TablePanel() {
        initComponents();
    }
    
    public boolean isRowEnabled(int row) {
        return (boolean) table.getModel().getValueAt(row, ENABLE_COLUMN);
    }
    
    public void setDefaultRenderer(Class<?> columnClass, TableCellRenderer renderer) {
        table.setDefaultRenderer(columnClass, renderer);
    }
    
    public void addTableModelListener(TableModelListener tl) {
        table.getModel().addTableModelListener(tl);
    }
    
    public void removeTableModelListener(TableModelListener tl) {
        table.getModel().removeTableModelListener(tl);
    }
    
    public void addTableFocusListener(FocusListener listener) {
        table.addFocusListener(listener);
    }
    
    public void addTableKeyListener(KeyListener listener) {
        table.addKeyListener(listener);
    }
    
    public void addCellKeyListener(KeyListener listener) {
        DefaultCellEditor dce = (DefaultCellEditor) table.getDefaultEditor(Object.class);
        dce.getComponent().addKeyListener(listener);
    }
    
    public void addDocumentListener(DocumentListener dl) {
        DefaultCellEditor dce = (DefaultCellEditor) table.getDefaultEditor(Object.class);
        JTextField textField = (JTextField) dce.getComponent();
        textField.getDocument().addDocumentListener(dl);
    }
    
    public void removeDocumentListener(DocumentListener dl) {
        DefaultCellEditor dce = (DefaultCellEditor) table.getDefaultEditor(Object.class);
        JTextField textField = (JTextField) dce.getComponent();
        textField.getDocument().removeDocumentListener(dl);
    }
    
    @Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        table.addPropertyChangeListener(propertyName, listener);
    }
    
    public String getKey(int row) {
        return (String) table.getModel().getValueAt(row, KEY_COLUMN);
    }
    
    public String getValue(int row) {
        return (String) table.getModel().getValueAt(row, VALUE_COLUMN);
    }
    
    public void setValue(String value, int row) {
        table.getModel().setValueAt(value, row, VALUE_COLUMN);
    }
    
    public MultivaluedMap<String, String> getValues() {
        MultivaluedMap<String, String> values = new MultivaluedHashMap<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            if (isRowEnabled(i)) {
                values.add(getKey(i), getValue(i));
            }
        }
        return values;
    }
    
    public void setValues(MultivaluedMap<String, String> values) {
        for (Map.Entry<String, List<String>> entry : values.entrySet()) {
            String value = "";
            if (!entry.getValue().isEmpty()) {
                value = entry.getValue().get(0);
            }
            addRow(entry.getKey(), value);
        }
    }
    
    public void clearValues() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }
    
    public String getValuesString() {
        String values = "";
        for (int i = 0; i < table.getRowCount(); i++) {
            if (isRowEnabled(i)) {
                values += getKey(i) + "=" + getValue(i);
                if (i != table.getRowCount() - 1) {
                    values += ",";
                }
            }
        }
        return values;
    }
    
    public void setValuesString(String values) {
        removeAllRows();
        if (!values.isEmpty()) {
            List<String> list = new ArrayList<>(Arrays.asList(values.split(",")));
            for (String entry : list) {
                List<String> row = Arrays.asList(entry.split("=", -1));
                addRow(row.get(0), row.get(1));
            }
        }
    }
    
    public void addRow(String key, String value) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.addRow(new Object[]{true, key, value});
    }    
    
    public void changeSelection(int row, int column) {
        table.changeSelection(row, column, false, false);
    }
    
    public void selectLastItem() {
        table.changeSelection(table.getRowCount() - 1, 1, false, false);
    }
    
    public void showLastItem() {
        table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));
    }
    
    public void editRow(int row, String key, String value) {
        table.setValueAt(key, row, KEY_COLUMN);
        table.setValueAt(value, row, VALUE_COLUMN);
    }
    
    public void insertRow(int row, String key, String value) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.insertRow(row, new Object[]{true, key, value});
    }
    
    public void removeRow(int row) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.removeRow(row);
    }
    
    public void removeAllRows() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
    }
    
    public int containsKey(String key) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, KEY_COLUMN).equals(key)) {
                return i;
            }
        }

        return -1;
    }
    
    public void hideEnableColumn() {
        table.getColumnModel().getColumn(ENABLE_COLUMN).setMinWidth(0);
        table.getColumnModel().getColumn(ENABLE_COLUMN).setMaxWidth(0);
        table.getColumnModel().getColumn(ENABLE_COLUMN).setWidth(0);
        table.getColumnModel().getColumn(ENABLE_COLUMN).setResizable(false);
    }
    
    public void setReadOnly() {
        // Hide buttons
        buttonPanel.setVisible(false);
        // Remove first column
        table.removeColumn(table.getColumnModel().getColumn(ENABLE_COLUMN));
        // Set non-editable
        table.setDefaultEditor(Object.class, null);
    }
    
    public boolean isEmpty() {
        return table.getModel().getRowCount() == 0;
    }
    
    public int getRowCount() {
        return table.getRowCount();
    }
    
    public int getSelectedRow() {
        return table.getSelectedRow();
    }
    
    public int getSelectedColumn() {
        return table.getSelectedColumn();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableScrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        buttonPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(788, 144));

        tableScrollPane.setPreferredSize(new java.awt.Dimension(452, 127));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Key", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        table.getTableHeader().setReorderingAllowed(false);
        table.getSelectionModel().addListSelectionListener((ListSelectionEvent lse) -> {
            if (!lse.getValueIsAdjusting()) {
                logger.info("Table selection changed");
                removeButton.setEnabled(!table.getSelectionModel().isSelectionEmpty());
            }
        });

        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tableScrollPane.setViewportView(table);
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        org.openide.awt.Mnemonics.setLocalizedText(addButton, "Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(removeButton, "Remove");
        removeButton.setEnabled(false);
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanelLayout.setHorizontalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addButton)
                    .addComponent(removeButton))
                .addGap(0, 35, Short.MAX_VALUE))
        );
        buttonPanelLayout.setVerticalGroup(
            buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonPanelLayout.createSequentialGroup()
                .addComponent(addButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeButton)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        addRow("", "");
        selectLastItem();
        showLastItem();
        table.editCellAt(table.getRowCount() - 1, 1);
        table.transferFocus();
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int[] rows = table.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            model.removeRow(table.getSelectedRow());
        }

        // Select row after deletion
        if (table.getRowCount() > 0) {
            if (rows[rows.length - 1] <= table.getRowCount() - 1) {
                table.changeSelection(rows[rows.length - 1], 1, false, false);
            } else {
                selectLastItem();
            }
        }

    }//GEN-LAST:event_removeButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton removeButton;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScrollPane;
    // End of variables declaration//GEN-END:variables
}
