package net.ada.api.impl;

import net.ada.api.impl.webgui.JSWebGUIPlatform;
import net.ada.api.webgui.IWebGUIPlatform;

public class ClientAPI {
    IWebGUIPlatform webGUI;
    public ClientAPI() {
        webGUI = new JSWebGUIPlatform();
    }

    public IWebGUIPlatform getWebGUIPlatform() {
        return webGUI;
    }
}
