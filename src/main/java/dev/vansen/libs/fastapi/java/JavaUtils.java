package dev.vansen.libs.fastapi.java;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/*
 * Utility class for various common operations such as null checks,
 * equality checks, and task execution with automatic null pointer handling.
 * This class never throws a {@link NullPointerException} on its own.
 */
@SuppressWarnings({"unused"})
public final class JavaUtils {

    // Private constructor
    private JavaUtils() {
    }

    /**
     * Performs a null check and executes the provided task if the object is not null.
     *
     * @param obj  The object to check.
     * @param task The task to execute if the object is not null.
     */
    public static void taskIfNotNull(@Nullable Object obj, @NotNull Runnable task) {
        if (obj != null) {
            task.run();
        }
    }

    /**
     * Performs a null check and executes the provided task if the object is not null.
     *
     * @param obj   The object to check.
     * @param task  The task to execute if the object is not null.
     * @param task2 The task to execute if the object is null.
     */
    public static void taskIfNotNull(@Nullable Object obj, @NotNull Runnable task, @NotNull Runnable task2) {
        if (obj != null) {
            task.run();
        } else {
            task2.run();
        }
    }

    public static <T> void taskIfNotNull(@Nullable T param, @NotNull Consumer<T> task, @NotNull Runnable task2) {
        if (param != null) {
            task.accept(param);
        } else {
            task2.run();
        }
    }

    /**
     * Checks if an object is null and returns a default value if it is.
     *
     * @param obj          The object to check.
     * @param defaultValue The default value to return if the object is null.
     * @param <T>          The type of the object.
     * @return The object if it is not null; otherwise, the default value.
     */
    public static <T> T defaultIfNull(@Nullable T obj, @Nullable T defaultValue) {
        return obj != null ? obj : defaultValue;
    }

    /**
     * Returns the default value if the input integer is negative.
     *
     * @param value        The integer value to check.
     * @param defaultValue The default value to return if the integer is negative.
     * @return The default value if the integer is negative; otherwise, the integer value.
     */
    public static int defaultIfNegative(int value, int defaultValue) {
        return value >= 0 ? value : defaultValue;
    }

    /**
     * Checks if a string is null or empty.
     *
     * @param str The string to check.
     * @return true if the string is null or empty; false otherwise.
     */
    public static boolean isNullOrEmpty(@Nullable String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Checks if a collection is null or empty.
     *
     * @param collection The collection to check.
     * @param <T>        The type of elements in the collection.
     * @return true if the collection is null or empty; false otherwise.
     */
    public static <T> boolean isNullOrEmpty(@Nullable Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Checks if a map is null or empty.
     *
     * @param map The map to check.
     * @param <K> The type of keys in the map.
     * @param <V> The type of values in the map.
     * @return true if the map is null or empty; false otherwise.
     */
    public static <K, V> boolean isNullOrEmpty(@Nullable Map<K, V> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Checks if an array is null or empty.
     *
     * @param array The array to check.
     * @param <T>   The type of elements in the array.
     * @return true if the array is null or empty; false otherwise.
     */
    public static <T> boolean isNullOrEmpty(@Nullable T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Safely casts an object to the specified type.
     *
     * @param obj   The object to cast.
     * @param clazz The class of the type to cast to.
     * @param <T>   The type to cast to.
     * @return The cast object, or null if the cast is not possible.
     */
    @Nullable
    public static <T> T safeCast(@Nullable Object obj, @NotNull Class<T> clazz) {
        return clazz.isInstance(obj) ? clazz.cast(obj) : null;
    }

    /**
     * Executes a task with a nullable parameter if the parameter is not null.
     *
     * @param param The parameter to check.
     * @param task  The task to execute if the parameter is not null.
     * @param <T>   The type of the parameter.
     */
    public static <T> void taskIfNotNull(@Nullable T param, @NotNull Consumer<T> task) {
        if (param != null) {
            task.accept(param);
        }
    }

    /**
     * Executes a task if the parameter is true.
     *
     * @param param The parameter to check.
     * @param task  The task to execute if the parameter is true.
     */
    public static void taskIfTrue(boolean param, @NotNull Runnable task) {
        if (param) {
            task.run();
        }
    }

    /**
     * Executes a task if the parameter is false.
     *
     * @param param The parameter to check.
     * @param task  The task to execute if the parameter is false.
     */
    public static void taskIfFalse(boolean param, @NotNull Runnable task) {
        if (!param) {
            task.run();
        }
    }
}