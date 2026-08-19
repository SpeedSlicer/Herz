package net.ada.api.eventbus.events.impl.input;

public class MouseReleasedEvent implements IMouseEvent {
    int code;
    KeyInteractLocation ki;
    int dx, dy, dwheel;
    public MouseReleasedEvent(int keycode, KeyInteractLocation ki, int dx, int dy, int dwheel) {
        this.code = keycode;
        this.ki = ki;
        this.dx = dx;
        this.dy = dy;
        this.dwheel = dwheel;
    }
    @Override
    public int getKeyCode() {
        return code;
    }
    @Override
    public KeyInteractLocation getKeyInteractLocation() {
        return ki;
    }
    @Override
    public int getDX() {
        return dx;
    }
    @Override
    public int getDY() {
        return dy;
    }
    @Override
    public int getDwheel() {
        return dwheel;
    }
}
