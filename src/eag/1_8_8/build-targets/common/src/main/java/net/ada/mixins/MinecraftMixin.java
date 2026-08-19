package net.ada.mixins;

import net.ada.Client;
import net.lax1dude.eaglercraft.v1_8.EagRuntime;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "run", at = @At("HEAD"))
    private void onInitialize(CallbackInfo ci) {
        System.out.println(">>> MINECRAFT HAS STARTED! <<<");

        Client client = new Client();

        var webGUI =
                client
                        .getClientAPI()
                        .getWebGUIPlatform();

        var clickGUI =
                webGUI.generateGUIObject(
                        "herz-clickgui"
                );

        clickGUI.setCSSComponent(
                EagRuntime.getRequiredResourceString(
                        "/assets/herz/gui/clickgui/clickgui.css"
                )
        );

        clickGUI.setJSComponent(
                EagRuntime.getRequiredResourceString(
                        "/assets/herz/gui/clickgui/clickgui.js"
                )
        );

        clickGUI.setHTMLComponent(
                EagRuntime.getRequiredResourceString(
                        "/assets/herz/gui/clickgui/clickgui.html"
                )
        );

        webGUI.addGUI(clickGUI);


        webGUI.addClickListener(
                "module-killaura",
                () -> {
                    System.out.println("KillAura toggled");
                }
        );

        webGUI.addClickListener(
                "module-velocity",
                () -> {
                    System.out.println("Velocity toggled");
                }
        );

        webGUI.addClickListener(
                "module-flight",
                () -> {
                    System.out.println("Flight toggled");
                }
        );

        webGUI.addClickListener(
                "module-sprint",
                () -> {
                    System.out.println("Sprint toggled");
                }
        );

        webGUI.addClickListener(
                "module-noslow",
                () -> {
                    System.out.println("NoSlow toggled");
                }
        );

        webGUI.addClickListener(
                "module-inventory",
                () -> {
                    System.out.println("Inventory toggled");
                }
        );

        webGUI.addClickListener(
                "module-esp",
                () -> {
                    System.out.println("ESP toggled");
                }
        );

        webGUI.addClickListener(
                "module-fullbright",
                () -> {
                    System.out.println("Fullbright toggled");
                }
        );

        webGUI.addClickListener(
                "module-hud",
                () -> {
                    System.out.println("HUD toggled");
                }
        );
    }
}
