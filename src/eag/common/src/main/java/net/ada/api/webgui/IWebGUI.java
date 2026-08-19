package net.ada.api.webgui;

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