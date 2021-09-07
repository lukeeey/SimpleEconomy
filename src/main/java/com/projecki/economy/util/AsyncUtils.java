package com.projecki.economy.util;

import com.projecki.economy.SimpleEconomyPlugin;
import org.bukkit.Bukkit;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class AsyncUtils {
    private static final Executor bukkitExecutor = Bukkit.getScheduler().getMainThreadExecutor(SimpleEconomyPlugin.getInstance());

    /**
     * Provides an easy way to use {@link CompletableFuture} while using the
     * Bukkit executor, to ensure nothing funky happens.
     *
     * @param supplier a function returning the value to be used to
     *                 complete the returned CompletableFuture
     * @param <T> the function's return type
     * @return the new CompletableFuture
     */
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, bukkitExecutor);
    }
}