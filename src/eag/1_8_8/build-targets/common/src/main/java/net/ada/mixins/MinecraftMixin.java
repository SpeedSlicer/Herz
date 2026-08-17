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

        var test = client.getClientAPI().getWebGUIPlatform().generateGUIObject("test");

        test.setCSSComponent(EagRuntime.getRequiredResourceString("/assets/herz/gui/test/test.css"));
        test.setJSComponent(EagRuntime.getRequiredResourceString("/assets/herz/gui/test/test.js"));
        test.setHTMLComponent(EagRuntime.getRequiredResourceString("/assets/herz/gui/test/test.html"));


        client.getClientAPI().getWebGUIPlatform().addGUI(test);
        test.flush();
        client.getClientAPI().getWebGUIPlatform().addClickListener("myButton", () -> {
            System.out.println("Button Listener Works!");
        });
    }
}
