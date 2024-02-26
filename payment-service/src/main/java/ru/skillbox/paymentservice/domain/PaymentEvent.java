package ru.skillbox.paymentservice.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.skillbox.paymentservice.domain.enums.PaymentStatus;

@ToString
@Setter
@Getter
@Builder
public class PaymentEvent implements Event {

    private static final String EVENT = "Payment";

    private long orderId;
    private PaymentStatus status;
    private long cost;

    @Override
    public String getEvent() {
        return EVENT;
    }

}
