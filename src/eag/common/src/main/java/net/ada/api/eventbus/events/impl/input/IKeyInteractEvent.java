package net.ada.api.eventbus.events.impl.input;

import net.ada.api.eventbus.events.IEvent;

public interface IKeyInteractEvent extends IEvent {
    char getKeyChar();

    int getKeyCode();

    KeyInteractLocation getKeyInteractLocation();
}