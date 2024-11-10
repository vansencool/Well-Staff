package dev.vansen.utility.annotations.impl;

import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.annotations.Register;
import dev.vansen.utility.debugging.Debug;
import dev.vansen.welldevelopment.Holder;
import dev.vansen.wellstaff.api.events.eventbus.annotations.Eventer;
import dev.vansen.wellstaff.api.events.eventbus.utils.EventListener;
import dev.vansen.wellstaff.api.events.eventbus.utils.EventPriority;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.jar.JarFile;

public class AnnotationImpl {

    // random way of flexing :3
    public static void process() {
        try (JarFile jar = new JarFile(new File(((URLClassLoader) Holder.get().getClass().getClassLoader()).getURLs()[0].toURI()))) {
            jar.stream()
                    .filter(entry -> entry.getName().endsWith(".class"))
                    .map(entry -> entry.getName().replace('/', '.').replace(".class", ""))
                    .map(AnnotationImpl::load)
                    .filter(Objects::nonNull)
                    .forEach(clazz -> {
                        Optional.ofNullable(clazz.getAnnotation(Init.class))
                                .ifPresent(annotation -> constructor(clazz));

                        Arrays.stream(clazz.getDeclaredMethods())
                                .filter(method -> method.isAnnotationPresent(Init.class) && method.getParameterCount() == 0)
                                .forEach(method -> method(method, clazz));

                        Optional.ofNullable(clazz.getAnnotation(Register.class))
                                .filter(annotation -> Listener.class.isAssignableFrom(clazz))
                                .ifPresent(annotation -> register(clazz));

                        Optional.ofNullable(clazz.getAnnotation(Eventer.class))
                                .filter(annotation -> EventListener.class.isAssignableFrom(clazz))
                                .ifPresent(annotation -> register(clazz, annotation.priority()));
                    });
        } catch (Exception e) {
            Holder.get()
                    .getComponentLogger()
                    .trace("Failed to process annotations", e);
        }
    }

    private static Class<?> load(@NotNull String className) {
        try {
            Debug.debug(Component.text(
                    "Loading class: " + className
            ));
            return Class.forName(className);
        } catch (Exception e) {
            return null;
        }
    }

    private static void constructor(@NotNull Class<?> clazz) {
        try {
            Debug.debug(Component.text(
                    "Initializing class: " + clazz.getName()
            ));
            clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Holder.get()
                    .getComponentLogger()
                    .trace("Failed to initialize class: {}", clazz.getName(), e);
        }
    }

    private static void method(@NotNull Method method, @NotNull Class<?> clazz) {
        try {
            Debug.debug(Component.text(
                    "Invoking method: " + method.getName() + " in class: " + clazz.getName() + " with parameters: " + Arrays.toString(method.getParameterTypes()) + " and return type: " + method.getReturnType()
            ));
            method.setAccessible(true);
            method.invoke(Modifier.isStatic(method.getModifiers()) ? null : clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            Holder.get()
                    .getComponentLogger()
                    .trace("Failed to invoke method: {} in class: {}", method.getName(), clazz.getName(), e);
        }
    }

    private static void register(@NotNull Class<?> clazz) {
        try {
            Debug.debug(Component.text(
                    "Registering listener: " + clazz.getName()
            ));
            Bukkit.getPluginManager().registerEvents((Listener) clazz.getDeclaredConstructor().newInstance(), Holder.get());
        } catch (Exception e) {
            Holder.get()
                    .getComponentLogger()
                    .trace("Failed to register listener: {}", clazz.getName(), e);
        }
    }

    private static void register(@NotNull Class<?> clazz, @NotNull EventPriority priority) {
        try {
            Debug.debug(Component.text(
                    "Registering eventer: " + clazz.getName()
            ));
            Holder.getEventer().register((EventListener) clazz.getDeclaredConstructor().newInstance(), priority);
        } catch (Exception e) {
            Holder.get()
                    .getComponentLogger()
                    .trace("Failed to register eventer: {}", clazz.getName(), e);
        }
    }
}