package dev.vansen.wellstaff.api.events.eventbus;

import dev.vansen.libs.fastapi.java.JavaUtils;
import dev.vansen.welldevelopment.Holder;
import dev.vansen.wellstaff.api.events.eventbus.annotations.Async;
import dev.vansen.wellstaff.api.events.eventbus.annotations.Subscribe;
import dev.vansen.wellstaff.api.events.eventbus.utils.Event;
import dev.vansen.wellstaff.api.events.eventbus.utils.EventListener;
import dev.vansen.wellstaff.api.events.eventbus.utils.EventPriority;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unused")
public final class Eventer {

    private static final Map<Class<?>, List<Subscriber>> subscribers = new HashMap<>();

    public void register(@NotNull EventListener listener, @NotNull EventPriority priority) {
        Arrays.stream(listener.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Subscribe.class) && method.getParameterCount() == 1)
                .forEach(method -> {
                    Class<?> eventType = method.getParameterTypes()[0];
                    subscribers.computeIfAbsent(eventType, k -> new ArrayList<>())
                            .add(new Subscriber(listener, method, priority));
                    subscribers.get(eventType).sort(Comparator.comparingInt(Subscriber::getPriority));
                });
    }

    public void register(@NotNull EventListener listener) {
        register(listener, EventPriority.NORMAL);
    }

    public void unregister(@NotNull EventListener listener) {
        subscribers.values().forEach(subscriberList ->
                subscriberList.removeIf(subscriber -> subscriber.listener.equals(listener))
        );
    }

    public void post(@NotNull Event event) {
        List<Subscriber> subscriberList = getSubscriberList(event.getClass());
        JavaUtils.taskIfNotNull(subscriberList, () -> subscriberList.stream()
                .takeWhile(subscriber -> !(event instanceof Cancellable cancellable && cancellable.isCancelled()))
                .forEach(subscriber -> {
                    if (subscriber.method.isAnnotationPresent(Async.class)) {
                        CompletableFuture.runAsync(() -> invokeSubscriber(subscriber, event));
                    } else {
                        invokeSubscriber(subscriber, event);
                    }
                }));
    }

    private List<Subscriber> getSubscriberList(Class<?> eventClass) {
        while (eventClass != null) {
            List<Subscriber> subscriberList = subscribers.get(eventClass);
            if (subscriberList != null) {
                return subscriberList;
            }
            eventClass = eventClass.getSuperclass();
        }
        return null;
    }

    private void invokeSubscriber(@NotNull Subscriber subscriber, @NotNull Object event) {
        try {
            if (subscriber.method.isAnnotationPresent(Async.class)) {
                CompletableFuture.runAsync(() -> invokeMethod(subscriber, event));
            } else {
                invokeMethod(subscriber, event);
            }
        } catch (Exception e) {
            Holder.get().getComponentLogger().trace("Failed to invoke subscriber", e);
        }
    }

    private void invokeMethod(@NotNull Subscriber subscriber, @NotNull Object event) {
        try {
            subscriber.method.invoke(subscriber.listener, event);
        } catch (Exception e) {
            Holder.get()
                    .getComponentLogger()
                    .trace("Failed to invoke subscriber", e);
        }
    }

    private record Subscriber(@NotNull EventListener listener, @NotNull Method method,
                              @NotNull EventPriority priority) {
        public int getPriority() {
            return priority.value();
        }
    }
}