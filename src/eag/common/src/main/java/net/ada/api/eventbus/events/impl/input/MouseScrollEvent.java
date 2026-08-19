package net.ada.api.eventbus.events.impl.input;

public class MouseScrollEvent implements IMouseEvent {
    private final KeyInteractLocation keyInteractLocation;
    private final int dx;
    private final int dy;
    private final int dwheel;

    public MouseScrollEvent(KeyInteractLocation keyInteractLocation, int dx, int dy, int dwheel) {
        this.keyInteractLocation = keyInteractLocation;
        this.dx = dx;
        this.dy = dy;
        this.dwheel = dwheel;
    }

    @Override
    public int getKeyCode() {
        return -1;
    }

    @Override
    public KeyInteractLocation getKeyInteractLocation() {
        return keyInteractLocation;
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
