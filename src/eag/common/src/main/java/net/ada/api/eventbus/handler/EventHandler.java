package net.ada.api.eventbus.handler;

import net.ada.api.eventbus.events.IEvent;
import net.ada.api.eventbus.events.IEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventHandler<E extends IEvent> {

    private final List<IEventListener<E>> listeners = new ArrayList<>();
    private final Class<E> eventClass;

    public EventHandler(Class<E> eventClass) {
        this.eventClass = eventClass;
    }

    public Class<E> getEventClass() {
        return eventClass;
    }

    public void add(IEventListener<E> listener) {
        listeners.add(listener);
    }

    public void remove(IEventListener<E> listener) {
        listeners.remove(listener);
    }

    public void fire(E event) {
        for (IEventListener<E> listener : listeners) {
            listener.execute(event);
        }
    }
}