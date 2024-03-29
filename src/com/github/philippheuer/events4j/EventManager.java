package com.github.philippheuer.events4j;

import com.github.philippheuer.events4j.annotation.EventSubscriber;
import com.github.philippheuer.events4j.domain.Event;
import com.github.philippheuer.events4j.services.AnnotationEventManager;
import com.github.philippheuer.events4j.services.ServiceMediator;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.*;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.Calendar;
import java.util.UUID;

/**
 * The EventManager
 *
 * @author Philipp Heuer [https://github.com/PhilippHeuer]
 * @version %I%, %G%
 * @since 1.0
 */
@Getter
@Slf4j
public class EventManager {

    /**
     * The scheduler provides some guarantees required by Reactive Streams flows like FIFO execution
     */
    private final Scheduler scheduler;

    /**
     * Used to bridge gateway events to the subscribers
     */
    private final FluxProcessor<Event, Event> processor;

    /**
     * Holds the ServiceMediator
     */
    private final ServiceMediator serviceMediator;

    /**
     * Event Sink
     */
    private final FluxSink<Event> eventSink;

    /**
     * Annotation based event manager
     */
    private final AnnotationEventManager annotationEventManager = new AnnotationEventManager();

    /**
     * Creates a new EventManager
     */
    public EventManager() {
        this.scheduler = Schedulers.elastic();
        this.processor = EmitterProcessor.create(256);
        this.eventSink = processor.sink();
        this.serviceMediator = new ServiceMediator(this);

        registerInternalListener();
    }

    /**
     * Constructor to provide a custom processor / scheduler
     *
     * @param scheduler The scheduler provides some guarantees required by Reactive Streams flows like FIFO execution
     * @param processor Used to bridge gateway events to the subscribers
     */
    public EventManager(Scheduler scheduler, FluxProcessor<Event, Event> processor) {
        this.scheduler = scheduler;
        this.processor = processor;
        this.eventSink = processor.sink();
        this.serviceMediator = new ServiceMediator(this);

        registerInternalListener();
    }

    /**
     * Dispatches a event
     *
     * @param event A event extending the base event class.
     */
    public void dispatchEvent(Event event) {
        // enrich event data
        // - fired at
        event.setFiredAt(Calendar.getInstance());
        // - serviceMediator to access 3rd party services
        event.setServiceMediator(getServiceMediator());
        // - unique event id
        event.setEventId(UUID.randomUUID().toString());

        // log event dispatch
        log.debug("Dispatching event of type {} with id {}.", event.getClass().getSimpleName(), event.getEventId());

        // publish event
        eventSink.next(event);
    }

    /**
     * Retrieves a {@link reactor.core.publisher.Flux} of the given event type.
     *
     * @param eventClass the event class to obtain events from
     * @param <E> the eventType
     * @return a new {@link reactor.core.publisher.Flux} of the given eventType
     */
    public <E extends Event> Flux<E> onEvent(Class<E> eventClass) {
        return processor.publishOn(scheduler).ofType(eventClass);
    }

    /**
     * Registers a listener using {@link EventSubscriber} method annotations.
     *
     * @param eventListener The class instance containing methods annotated with {@link EventSubscriber}.
     */
    public void registerListener(Object eventListener) {
        getAnnotationEventManager().registerListener(eventListener);
    }

    /**
     * Register internal listeners
     */
    private void registerInternalListener() {
        // Annotation-based EventListener
        onEvent(Event.class).subscribe(event -> {
            annotationEventManager.dispatch(event);
        });
    }

}
