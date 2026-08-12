package net.ada.mixins;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MinecraftMixin {
    @Inject(method = "appMain", at = @At("HEAD"))
    private void onInitialize(CallbackInfo ci) {
        System.out.println(">>> MINECRAFT HAS STARTED! <<<");
    }
}
