package dev.vansen.wellstaff.api.events.eventbus.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * This annotation marks a subscriber method to be invoked asynchronously.
 * <p>
 * Note that this annotation is not recommended in most cases, as the event
 * will not wait for the method to finish, and thus the event will not be able
 * to be cancelled.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Async {
}