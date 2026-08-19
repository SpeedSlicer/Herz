package net.ada.mixins;

import net.ada.Client;
import net.ada.api.eventbus.events.impl.core.MinecraftInitEvent;
import net.ada.api.eventbus.events.impl.core.TickEvent;
import net.ada.api.eventbus.events.impl.input.*;
import net.ada.bootstrap.ClientInit;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "<init>", at = @At("HEAD"))
    private void onInit(CallbackInfo ci) {
        ClientInit.init();
    }
    @Inject(method = "run", at = @At("HEAD"))
    private void onInitialize(CallbackInfo ci) {
        Client.INSTANCE.getEventBus().fireEvent(MinecraftInitEvent.class, new MinecraftInitEvent());
    }
    @Inject(method = "runTick", at = @At("HEAD"))
    private void onBeginTick(CallbackInfo ci) {
        Client.INSTANCE.getEventBus().fireEvent(TickEvent.class, new TickEvent());
    }

    @Redirect(
            method = "runTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/lax1dude/eaglercraft/v1_8/Keyboard;next()Z"
            )
    )
    private boolean herz$keyboardNext() {
        boolean next = Keyboard.next();

        if (next) {
            if ((Keyboard.getEventKeyState())) {
                Client.INSTANCE.getEventBus().fireEvent(KeyDownEvent.class, new KeyDownEvent(Keyboard.getEventCharacter(), Keyboard.getEventKey(), KeyInteractLocation.INGAME));
            }
            else {
                Client.INSTANCE.getEventBus().fireEvent(KeyReleasedEvent.class, new KeyReleasedEvent(Keyboard.getEventCharacter(), Keyboard.getEventKey(), KeyInteractLocation.INGAME));
            }
        }
        return next;
    }
    @Redirect(
            method = "runTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/lax1dude/eaglercraft/v1_8/Mouse;next()Z"
            )
    )
    private boolean herz$mouseNext() {
        boolean next = Mouse.next();
        if (next) {
            int button = Mouse.getEventButton();
            int dx = Mouse.getDX();
            int dy = Mouse.getDY();
            int dwheel = Mouse.getDWheel();

            if (button >= 0 && Mouse.getEventButtonState()) {
                Client.INSTANCE.getEventBus().fireEvent(MouseDownEvent.class,
                        new MouseDownEvent(button,
                                KeyInteractLocation.INGAME,
                                dx,
                                dy,
                                dwheel));
            }
            else if (button >= 0) {
                Client.INSTANCE.getEventBus().fireEvent(MouseReleasedEvent.class,
                        new MouseReleasedEvent(button,
                                KeyInteractLocation.INGAME,
                                dx,
                                dy,
                                dwheel));
            }

            if (dwheel != 0) {
                Client.INSTANCE.getEventBus().fireEvent(MouseScrollEvent.class,
                        new MouseScrollEvent(KeyInteractLocation.INGAME, dx, dy, dwheel));
            }
        }
        return next;
    }
}
