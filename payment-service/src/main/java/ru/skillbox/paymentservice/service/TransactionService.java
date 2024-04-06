package ru.skillbox.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.function.Tuple2;
import ru.skillbox.paymentservice.domain.model.Payments;
import ru.skillbox.paymentservice.repository.PaymentRepository;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TransactionService {

    private final PaymentRepository transactionRepository;
    private final Scheduler jdbcScheduler;

    @Autowired
    public TransactionService(PaymentRepository transactionRepository,
                              Scheduler jdbcScheduler) {
        this.transactionRepository = transactionRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    public Flux<Payments> getAll() {
        return Flux.defer(
                        () -> Flux.fromIterable(transactionRepository.findAll()))
                .subscribeOn(jdbcScheduler);
    }

    public Flux<List<Payments>> reactiveGetAll() {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(2000));
        Flux<List<Payments>> transactioinFlux = Flux.fromStream(
                Stream.generate(transactionRepository::findAll));
        return Flux.zip(interval, transactioinFlux)
                .map(Tuple2::getT2);
    }
    public Mono<Payments> getById(Long id) {
        return Mono.fromCallable(
                        () -> transactionRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "Transaction ID=" + id + " does not exist")))
                .subscribeOn(jdbcScheduler);
    }
}
