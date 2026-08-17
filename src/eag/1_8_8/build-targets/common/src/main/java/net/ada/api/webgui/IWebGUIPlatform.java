package net.ada.api.webgui;

import net.ada.api.webgui.listener.IWebGUIClickListener; /**
 * This is the JANKIEST thing I have ever written. Good luck, developers.
 */
public interface IWebGUIPlatform {
    void addGUI(IWebGUI webGUI);
    void removeWindow(IWebGUI webGUI);
    void updateGUI(IWebGUI webGUI);
    IWebGUI generateGUIObject(String id);
    void addClickListener(
            String elementId,
            IWebGUIClickListener listener
    );}
