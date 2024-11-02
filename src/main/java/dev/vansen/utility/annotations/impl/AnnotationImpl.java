package dev.vansen.utility.annotations.impl;

import dev.vansen.utility.annotations.Init;
import dev.vansen.utility.annotations.Register;
import dev.vansen.welldevelopment.Holder;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.jar.JarFile;

public class AnnotationImpl {

    /**
     * Processes all annotations in the classpath and initializes them if they are annotated with @Init or @Register
     */
    public static void process() {
        try (JarFile jar = new JarFile(new File(((URLClassLoader) Holder.get().getClass().getClassLoader()).getURLs()[0].toURI()))) {
            jar.stream()
                    .filter(entry -> entry.getName().endsWith(".class"))
                    .map(entry -> entry.getName().replace('/', '.').replace(".class", ""))
                    .map(AnnotationImpl::load)
                    .filter(Objects::nonNull)
                    .forEach(clazz -> {
                        if (clazz.isAnnotationPresent(Init.class)) {
                            constructor(clazz);
                        }
                        for (Method method : clazz.getDeclaredMethods()) {
                            if (method.isAnnotationPresent(Init.class) && method.getParameterCount() == 0) {
                                method(method, clazz);
                            }
                        }
                        if (clazz.isAnnotationPresent(Register.class) && Listener.class.isAssignableFrom(clazz)) {
                            register(clazz);
                        }
                    });
        } catch (Exception e) {
            Holder.get()
                    .getComponentLogger()
                    .trace("Failed to process annotations", e);
        }
    }

    private static Class<?> load(@NotNull String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            return null;
        }
    }

    private static void constructor(@NotNull Class<?> clazz) {
        try {
            clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            Holder.get()
                    .getComponentLogger()
                    .trace("Failed to initialize class: {}", clazz.getName(), e);
        }
    }

    private static void method(@NotNull Method method, @NotNull Class<?> clazz) {
        try {
            if (Modifier.isStatic(method.getModifiers())) {
                method.setAccessible(true);
                method.invoke(null);
            } else {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                method.setAccessible(true);
                method.invoke(instance);
            }
        } catch (Exception e) {
            Holder.get()
                    .getComponentLogger()
                    .trace("Failed to invoke method: {} in class: {}", method.getName(), clazz.getName(), e);
        }
    }

    private static void register(@NotNull Class<?> clazz) {
        try {
            Bukkit.getPluginManager().registerEvents((Listener) clazz.getDeclaredConstructor().newInstance(), Holder.get());
        } catch (Exception e) {
            Holder.get()
                    .getComponentLogger()
                    .trace("Failed to register listener: {}", clazz.getName(), e);
        }
    }
}