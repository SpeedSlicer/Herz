package net.ada.api.webgui;

public interface IWebGUIPlatform {
    IWebGUI addGUI(IWebGUI webGUI);
    void removeWindow(IWebGUI webGUI);
    void updateGUI(IWebGUI webGUI);
    IWebGUI generateGUIObject(String id);}
