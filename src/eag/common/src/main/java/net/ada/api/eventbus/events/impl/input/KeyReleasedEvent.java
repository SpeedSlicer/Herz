package net.ada.api.eventbus.events.impl.input;

public class KeyReleasedEvent implements IKeyInteractEvent {
    char k;
    int code;
    KeyInteractLocation ki;
    public KeyReleasedEvent(char key, int keycode, KeyInteractLocation ki) {
        this.k = key;
        this.code = keycode;
        this.ki = ki;
    }

    @Override
    public char getKeyChar() {
        return k;
    }

    @Override
    public int getKeyCode() {
        return code;
    }

    @Override
    public KeyInteractLocation getKeyInteractLocation() {
        return ki;
    }
}
