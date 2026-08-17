package net.ada.api.impl.webgui;

import net.ada.api.webgui.IWebGUI;
import net.ada.api.webgui.IWebGUIPlatform;
import net.ada.api.webgui.listener.IWebGUIClickListener;
import org.teavm.jso.JSBody;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;

import java.util.HashMap;

public class JSWebGUIPlatform implements IWebGUIPlatform {
    public HashMap<String, IWebGUI> webGUIs;
    public JSWebGUIPlatform() {
        webGUIs = new HashMap<>();
    }

    @Override
    public void addGUI(IWebGUI gui) {
        webGUIs.put(gui.getID(), gui);
        createGUIDiv(gui.getID());
        gui.attach(this);
        gui.flush();
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
                            "if (!gui) {" +
                            "    console.error('GUI container not found: ' + id);" +
                            "    return;" +
                            "}" +

                            "var styleId = id + '-component-style';" +
                            "var scriptId = id + '-component-script';" +

                            "var oldStyle = document.getElementById(styleId);" +
                            "if (oldStyle) {" +
                            "    oldStyle.parentNode.removeChild(oldStyle);" +
                            "}" +

                            "var oldScript = document.getElementById(scriptId);" +
                            "if (oldScript) {" +
                            "    oldScript.parentNode.removeChild(oldScript);" +



                            "}" +

                            "gui.innerHTML = html || '';" +

                            "var styleElement = document.createElement('style');" +
                            "styleElement.id = styleId;" +
                            "styleElement.type = 'text/css';" +
                            "styleElement.textContent = css || '';" +
                            "document.head.appendChild(styleElement);" +

                            "if (js) {" +
                            "    var scriptElement = document.createElement('script');" +
                            "    scriptElement.id = scriptId;" +
                            "    scriptElement.type = 'text/javascript';" +
                            "    scriptElement.textContent = js;" +
                            "    document.body.appendChild(scriptElement);" +
                            "}"
    )
    private static native void updateGUI(
            String html,
            String js,
            String css,
            String id
    );

    @Override
    public void addClickListener(
            String elementId,
            IWebGUIClickListener listener
    ) {
        HTMLElement element = Window.current()
                .getDocument()
                .getElementById(elementId);

        if (element == null) {
            throw new IllegalArgumentException(
                    "Element not found: " + elementId
            );
        }

        element.addEventListener("click", event -> listener.onClick());
    }

    @Override
    public String getValueFromTextbox(String elementID) {
        HTMLDocument document = HTMLDocument.current();

        HTMLInputElement myTextBox = (HTMLInputElement) document.getElementById(elementID);

        return myTextBox.getValue();
    }
}
