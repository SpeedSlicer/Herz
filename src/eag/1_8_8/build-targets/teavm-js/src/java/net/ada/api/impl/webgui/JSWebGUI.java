package net.ada.api.impl.webgui;

import net.ada.api.webgui.IWebGUI;
import net.ada.api.webgui.IWebGUIPlatform;
import net.ada.util.Sordidabilis;

import java.util.UUID;

public class JSWebGUI implements IWebGUI {
    String id;
    Sordidabilis<String> htmlComponent, cssComponent, jsComponent;
    IWebGUIPlatform platform;

    public JSWebGUI(String gui) {
        this.id = gui + "_" + UUID.randomUUID().toString();

        htmlComponent = new Sordidabilis<>("");
        cssComponent = new Sordidabilis<>("");
        jsComponent = new Sordidabilis<>("");
    }

    @Override
    public IWebGUI setHTMLComponent(String htmlComponent) {
        this.htmlComponent.set(htmlComponent);
        return this;
    }

    @Override
    public IWebGUI setCSSComponent(String cssComponent) {
        this.cssComponent.set(cssComponent);
        return this;
    }

    @Override
    public IWebGUI setJSComponent(String jsComponent) {
        this.jsComponent.set(jsComponent);
        return this;
    }

    @Override
    public void flush() {
        htmlComponent.push();
        jsComponent.push();
        cssComponent.push();
        platform.updateGUI(this);
    }

    @Override
    public void attach(IWebGUIPlatform platform) {
        this.platform = platform;
    }


    @Override
    public String getID() {
        return id;
    }

    @Override
    public String getHtmlComponent() {
        return htmlComponent.get();
    }
    @Override
    public String getCSSComponent() {
        return cssComponent.get();
    }

    @Override
    public String getJSComponent() {
        return jsComponent.get();
    }
}
