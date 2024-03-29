package ru.skillbox.inventoryservice.consumer;


import ru.skillbox.inventoryservice.domain.event.Event;

public interface EventConsumer<T extends Event> {
    void consumeEvent(T event);
}