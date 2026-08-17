package net.ada.api.impl.webgui;

import net.ada.api.webgui.IWebGUI;
import net.ada.api.webgui.IWebGUIPlatform;
import org.teavm.jso.JSBody;

import java.util.HashMap;

public class JSWebGUIPlatform implements IWebGUIPlatform {
    public HashMap<String, IWebGUI> webGUIs;
    public JSWebGUIPlatform() {
        webGUIs = new HashMap<>();
    }

    @Override
    public IWebGUI addGUI(IWebGUI gui) {
        webGUIs.put(gui.getID(), gui);
        createGUIDiv(gui.getID());
        gui.attach(this);
        gui.flush();
        return gui;
    }

    @Override
    public void removeWindow(IWebGUI id) {
        destroyGUIDiv(id.getID());
    }

    @Override
    public void updateGUI(IWebGUI webGUI) {
        updateGUI(webGUI.getHtmlComponent(), webGUI.getJSComponent(), webGUI.getCSSComponent(), webGUI.getID());
    }

    @Override
    public IWebGUI generateGUIObject(String id) {
        return new JSWebGUI(id);
    }

    @JSBody(
            params = { "gui_id" },
            script =
                    "var gui = document.createElement('div');" +
                            "gui.id = gui_id;" +
                            "gui.className = 'herz-gui';" +
                            "document.querySelector('.herz-overlay').appendChild(gui);"
    )
    private static native void createGUIDiv(String gui_id);

    @JSBody(
            params = { "gui_id" },
            script =
                    "var gui = document.getElementById('gui_id');" +
                            "gui.remove();"

    )
    private static native void destroyGUIDiv(String gui_id);

    @JSBody(
            params = { "html", "js", "css", "id" },
            script =
                    "var gui = document.getElementById(id);" +
                            "if (gui === null) {" +
                            "    console.error('GUI container not found:', id);" +
                            "    return;" +
                            "}" +

                            "gui.innerHTML = html == null ? '' : html;" +

                            "var styleId = id + '-component-style';" +
                            "var styleElement = document.getElementById(styleId);" +
                            "if (styleElement === null) {" +
                            "    styleElement = document.createElement('style');" +
                            "    styleElement.id = styleId;" +
                            "    document.head.appendChild(styleElement);" +
                            "}" +
                            "styleElement.textContent = css == null ? '' : css;" +

                            "if (js != null && js.length > 0) {" +
                            "    try {" +
                            "        (new Function('gui', js))(gui);" +
                            "    } catch (error) {" +
                            "        console.error('GUI script failed for ' + id, error);" +
                            "    }" +
                            "}"
    )
    private static native void updateGUI(
            String html,
            String js,
            String css,
            String id
    );
}
