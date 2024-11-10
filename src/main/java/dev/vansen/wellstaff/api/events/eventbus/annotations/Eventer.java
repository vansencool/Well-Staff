package dev.vansen.wellstaff.api.events.eventbus.annotations;

import dev.vansen.wellstaff.api.events.eventbus.utils.EventListener;
import dev.vansen.wellstaff.api.events.eventbus.utils.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used in classes to register a listener to {@link dev.vansen.wellstaff.api.events.eventbus.Eventer} automatically.
 * <p>
 * Without having to call {@link dev.vansen.wellstaff.api.events.eventbus.Eventer#register(EventListener)} or {@link dev.vansen.wellstaff.api.events.eventbus.Eventer#register(EventListener, EventPriority)}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Eventer {

    /**
     * The priority of the event listener, defaults to {@link dev.vansen.wellstaff.api.events.eventbus.utils.EventPriority#NORMAL}
     */
    EventPriority priority() default EventPriority.NORMAL;
}