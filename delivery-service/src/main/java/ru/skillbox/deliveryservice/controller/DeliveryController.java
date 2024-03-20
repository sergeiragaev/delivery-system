package ru.skillbox.deliveryservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.deliveryservice.exception.DeliveryNotFoundException;
import ru.skillbox.deliveryservice.service.DeliveryService;

@Slf4j
@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Operation(summary = "Remove delivery by id.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delivery/{deliveryId}")
    public ResponseEntity<Void> deleteDeliverById(@PathVariable @Parameter(description = "Id of delivery") long deliveryId)
            throws DeliveryNotFoundException {

        deliveryService.deleteDeliveryById(deliveryId);
        return ResponseEntity.ok().build();
    }
}
