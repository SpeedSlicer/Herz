package net.ada.mixins;

import net.ada.Client;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "run", at = @At("HEAD"))
    private void onInitialize(CallbackInfo ci) {
        System.out.println(">>> HERZ HAS LOADED <<<");
        Client client = new Client();
    }
}
