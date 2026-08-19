package net.ada.api.eventbus.events;

@FunctionalInterface
public interface IEventListener<E extends IEvent> {
    void execute(E event);
}