package net.ada.api.eventbus.events.impl.input;

import net.ada.api.eventbus.events.IEvent;

public interface IMouseEvent extends IEvent {
    int getKeyCode();

    KeyInteractLocation getKeyInteractLocation();

    int getDX();

    int getDY();

    int getDwheel();
}
