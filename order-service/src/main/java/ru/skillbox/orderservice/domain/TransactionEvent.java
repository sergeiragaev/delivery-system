package ru.skillbox.orderservice.domain;

import lombok.Getter;
import lombok.ToString;
import ru.skillbox.orderservice.domain.enums.TransactionStatus;

@ToString
@Getter
public class TransactionEvent implements Event {

    private static final String EVENT = "Transaction";

    private Long orderId;
    private TransactionStatus status;

    @Override
    public String getEvent() {
        return EVENT;
    }

}
