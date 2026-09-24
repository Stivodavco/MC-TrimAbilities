package me.stivodavco.trimAbilities.utilities;

import me.stivodavco.trimAbilities.TrimAbilities;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnimationBuilder {
    private final HashMap<BukkitRunnable, Integer> runnables = new HashMap<>();
    private final List<BukkitTask> tasks = new ArrayList<>();
    private int timePointer = 0;

    public AnimationBuilder addKeyframe(BukkitRunnable runnable, int delay) {
        timePointer += delay;
        runnables.put(runnable, timePointer);
        return this;
    }

    public AnimationBuilder play() {
        cancel();
        for (BukkitRunnable runnable : runnables.keySet()) {
            int delay = runnables.get(runnable);

            BukkitTask task = runnable.runTaskLater(TrimAbilities.getInstance(), delay);
            tasks.add(task);
        }
        return this;
    }

    public AnimationBuilder cancel() {
        for (BukkitTask task : tasks) {
            task.cancel();
        }
        tasks.clear();
        return this;
    }
}
