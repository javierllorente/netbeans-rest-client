package com.javierllorente.netbeans.rest.client.http.editor.sidebar;

import com.javierllorente.netbeans.rest.client.http.editor.sidebar.request.IRequestProcessor;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

/**
 * Sidebar Panel to display task-related UI elements.
 */
public class RunHttpRequestsSideBarPanel extends JPanel {

    public RunHttpRequestsSideBarPanel(JTextComponent target, IRequestProcessor taskProcessor) {
        super(new BorderLayout());
        add(new DrawingPanel(target, taskProcessor));
    }
}
