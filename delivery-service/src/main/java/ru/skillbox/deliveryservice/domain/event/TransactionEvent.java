package ru.skillbox.deliveryservice.domain.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.skillbox.deliveryservice.domain.enums.TransactionStatus;

@ToString
@Getter
@Builder
public class TransactionEvent implements Event {

    private static final String EVENT = "Transaction";

    private Long orderId;
    private Long userId;
    private TransactionStatus status;

    @Override
    public String getEvent() {
        return EVENT;
    }

}
