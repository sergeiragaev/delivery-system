package ru.skillbox.deliveryservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.deliveryservice.domain.enums.OrderStatus;
import ru.skillbox.deliveryservice.domain.event.DeliveryEvent;
import ru.skillbox.deliveryservice.domain.model.Delivery;
import ru.skillbox.deliveryservice.exception.DeliveryNotFoundException;
import ru.skillbox.deliveryservice.exception.FailedDeliveryException;
import ru.skillbox.deliveryservice.repository.DeliveryRepository;

import java.util.Optional;

@Slf4j
@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public String deliverOrder(DeliveryEvent event) {

        try {
            double randomValue = Math.round(Math.random() * 100.0) / 100.0;
            if (randomValue > 0.85) {
                String comment = "The order delivery was unsuccessful";
                event.setStatus(OrderStatus.DELIVERY_FAILED);
                throw new FailedDeliveryException(comment);
            }

            Delivery delivery = new Delivery();
            delivery.setOrderId(event.getOrderId());
            delivery.setDestinationAddress(event.getDestinationAddress());
            deliveryRepository.save(delivery);

            return "The order delivers successfully";
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
    }

    public void deleteDeliveryById(Long deliveryId) throws DeliveryNotFoundException {
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        if (optionalDelivery.isEmpty()) {
            throw new DeliveryNotFoundException("Delivery with ID " + deliveryId + " not found.");
        }

        deliveryRepository.delete(optionalDelivery.get());
    }

}
