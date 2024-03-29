package ru.skillbox.inventoryservice.domain.event;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import ru.skillbox.inventoryservice.domain.enums.TransactionStatus;

@ToString
@Data
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
