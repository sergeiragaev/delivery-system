package ru.skillbox.paymentservice.consumer;


import ru.skillbox.paymentservice.domain.event.Event;

public interface EventConsumer<T extends Event> {
    void consumeEvent(T event);
}