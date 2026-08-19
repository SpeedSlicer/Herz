package net.ada;

import net.ada.api.eventbus.events.impl.core.ClientInitEvent;
import net.ada.api.eventbus.events.impl.core.MinecraftInitEvent;
import net.ada.api.eventbus.events.impl.core.TickEvent;
import net.ada.api.eventbus.events.impl.input.KeyDownEvent;
import net.ada.api.eventbus.events.impl.input.KeyReleasedEvent;
import net.ada.api.eventbus.events.impl.input.MouseDownEvent;
import net.ada.api.eventbus.events.impl.input.MouseReleasedEvent;
import net.ada.api.eventbus.events.impl.input.MouseScrollEvent;
import net.ada.api.eventbus.handler.EventBus;
import net.ada.api.eventbus.handler.EventHandler;
import net.ada.api.impl.ClientAPI;

import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public class Client {
    public static Client INSTANCE;

    ClientAPI clientAPI;
    EventBus eventBus;
    public static Logger LOGGER = Logger.getLogger("[Herz]");

    public Client() {
        if (INSTANCE == null) {
            INSTANCE = this;
        }
        else {
            LOGGER.warning("Client initialized twice?");
            // TODO logger error!!!
        }
        clientAPI = new ClientAPI();
        eventBus = new EventBus();

        // Register Common Events Here!!!
        eventBus.addEvent(new EventHandler<>(MinecraftInitEvent.class));
        eventBus.addEvent(new EventHandler<>(ClientInitEvent.class));
        eventBus.addEvent(new EventHandler<>(KeyDownEvent.class));
        eventBus.addEvent(new EventHandler<>(KeyReleasedEvent.class));
        eventBus.addEvent(new EventHandler<>(TickEvent.class));
        eventBus.addEvent(new EventHandler<>(MouseDownEvent.class));
        eventBus.addEvent(new EventHandler<>(MouseReleasedEvent.class));
        eventBus.addEvent(new EventHandler<>(MouseScrollEvent.class));

        eventBus.fireEvent(ClientInitEvent.class, new ClientInitEvent());
    }

    public ClientAPI getClientAPI() {
        return clientAPI;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
