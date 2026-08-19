package net.ada.api.webgui;

import net.ada.api.webgui.listener.IWebGUIClickListener;
import net.ada.api.webgui.listener.IWebGUIGenericListener;

/**
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
    );

    void addEventListener(
            String elementId,
            String type,
            IWebGUIGenericListener listener
    );

    String getValueFromElement(String elementID);

    String getInnerHTMLFromElement(String elementID);
}
