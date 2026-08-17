package net.ada.api.webgui;

import net.ada.api.webgui.listener.IWebGUIClickListener;
import net.ada.api.webgui.listener.IWebGUIMoveListener;

public interface IWebGUI {
    IWebGUI setHTMLComponent(String htmlComponent);
    IWebGUI setCSSComponent(String cssComponent);
    IWebGUI setJSComponent(String jsComponent);

    void flush();
    void attach(IWebGUIPlatform platform);
    String getID();

    String getHtmlComponent();
    String getCSSComponent();
    String getJSComponent();

}