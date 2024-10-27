package dev.vansen.libs.fastapi.concurrent;

import dev.vansen.welldevelopment.Holder;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/*
 * A utility class to handle async and main-thread tasks, while calculating execution time
 * and ensuring the main thread is not blocked.
 */
@SuppressWarnings("unused")
public final class Tasker<T> {

    private Callable<T> asyncTask;
    private Consumer<T> mainTask;

    /*
     * Creates a new Tasker instance.
     *
     * @param <T> The type of result returned by the async task.
     * @return A new Tasker instance.
     */
    public static <T> Tasker<T> getNew() {
        return new Tasker<>();
    }

    /*
     * Defines the async task to be executed.
     *
     * @param asyncTask The async task to be executed.
     * @return The same Tasker instance for chaining.
     */
    public Tasker<T> async(@NotNull Callable<T> asyncTask) {
        this.asyncTask = asyncTask;
        return this;
    }

    /*
     * Defines the main-thread task to be executed after the async task completes.
     *
     * @param mainTask The task to be executed on the main thread.
     * @return The same Tasker instance for chaining.
     */
    public Tasker<T> mainThread(@NotNull Consumer<T> mainTask) {
        this.mainTask = mainTask;
        return this;
    }

    /*
     * Calculates how long the async task took to execute and then ensures
     * the main-thread task runs without blocking the main thread.
     */
    public void execute() {
        CompletableFuture.supplyAsync(() -> {
            try {
                return asyncTask.call();
            } catch (Exception e) {
                Holder.get().getComponentLogger()
                        .error("An error occurred while executing the async task", e);
            }
            return null;
        }).whenCompleteAsync(((u, t) -> Bukkit.getScheduler().runTask(Holder.get(), () -> mainTask.accept(u))));
    }
}