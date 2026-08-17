package net.ada.api.webgui;

/**
 * This is the JANKIEST thing I have ever written. Good luck, developers.
 */
public interface IWebGUIPlatform {
    void addGUI(IWebGUI webGUI);
    void removeWindow(IWebGUI webGUI);
    void updateGUI(IWebGUI webGUI);
    IWebGUI generateGUIObject(String id);}
