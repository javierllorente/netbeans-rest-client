package com.javierllorente.netbeans.rest.client.http.editor.sidebar;

import com.javierllorente.netbeans.rest.client.http.editor.sidebar.request.RequestProcessor;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.spi.editor.SideBarFactory;

public class RunHttpRequestsSideBarFactory implements SideBarFactory {

    @Override
    public JComponent createSideBar(JTextComponent target) {
        return new RunHttpRequestsSideBarPanel(target, new RequestProcessor((StyledDocument) target.getDocument()));
    }
}
