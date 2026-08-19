package net.ada.mixins;

import net.ada.Client;
import net.ada.api.eventbus.events.impl.input.*;
import net.lax1dude.eaglercraft.v1_8.Keyboard;
import net.lax1dude.eaglercraft.v1_8.Mouse;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Redirect(
            method = "handleInput",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/lax1dude/eaglercraft/v1_8/Keyboard;next()Z"
            )
    )
    private boolean herz$GuiScreen$keyboardNext() {
        boolean next = Keyboard.next();

        if (next) {
            if ((Keyboard.getEventKeyState())) {
                Client.INSTANCE.getEventBus().fireEvent(KeyDownEvent.class, new KeyDownEvent(Keyboard.getEventCharacter(), Keyboard.getEventKey(), KeyInteractLocation.OUTGAME));
            }
            else {
                Client.INSTANCE.getEventBus().fireEvent(KeyReleasedEvent.class, new KeyReleasedEvent(Keyboard.getEventCharacter(), Keyboard.getEventKey(), KeyInteractLocation.OUTGAME));
            }
        }
        return next;
    }
    @Redirect(
            method = "handleInput",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/lax1dude/eaglercraft/v1_8/Mouse;next()Z"
            )
    )
    private boolean herz$GuiScreen$mouseNext() {
        boolean next = Mouse.next();
        if (next) {
            int button = Mouse.getEventButton();
            int dx = Mouse.getDX();
            int dy = Mouse.getDY();
            int dwheel = Mouse.getDWheel();

            if (button >= 0 && Mouse.getEventButtonState()) {
                Client.INSTANCE.getEventBus().fireEvent(MouseDownEvent.class,
                        new MouseDownEvent(button,
                                KeyInteractLocation.OUTGAME,
                                dx,
                                dy,
                                dwheel));
            }
            else if (button >= 0) {
                Client.INSTANCE.getEventBus().fireEvent(MouseReleasedEvent.class,
                        new MouseReleasedEvent(button,
                                KeyInteractLocation.OUTGAME,
                                dx,
                                dy,
                                dwheel));
            }

            if (dwheel != 0) {
                Client.INSTANCE.getEventBus().fireEvent(MouseScrollEvent.class,
                        new MouseScrollEvent(KeyInteractLocation.OUTGAME, dx, dy, dwheel));
            }
        }
        return next;
    }
}
