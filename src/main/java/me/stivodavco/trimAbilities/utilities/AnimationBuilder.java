package me.stivodavco.trimAbilities.utilities;

import me.stivodavco.trimAbilities.TrimAbilities;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class AnimationBuilder {
    private final HashMap<Integer, List<Consumer<AnimationBuilder>>> keyframes = new HashMap<>();
    private BukkitTask animationTask;
    private int runTime = 0;

    private AnimationBuilder getInstance() { return this; }

    public AnimationBuilder at(int time, Consumer<AnimationBuilder> action) {
        List<Consumer<AnimationBuilder>> actions = keyframes.get(time);

        if (actions != null) {
            actions.add(action);
        } else {
            List<Consumer<AnimationBuilder>> newList = new ArrayList<>();
            newList.add(action);
            keyframes.put(time, newList);
        }
        return this;
    }

    public AnimationBuilder atRepeated(int time, int period, int repeatTimes, Consumer<AnimationBuilder> action) {
        for (int i = 1; i <= repeatTimes; i++) {
            at(time + i * period, action);
        }
        return this;
    }

    public AnimationBuilder play() {
        animationTask = new BukkitRunnable() {
            @Override
            public void run() {
                List<Consumer<AnimationBuilder>> currentKeyframes = keyframes.get(runTime);

                if (currentKeyframes != null) {
                    for (Consumer<AnimationBuilder> keyframe : currentKeyframes) {
                        keyframe.accept(getInstance());
                    }
                }

                runTime++;
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, 1);
        return this;
    }

    public AnimationBuilder stop() {
        if (animationTask != null) {
            animationTask.cancel();
            animationTask = null;
        }
        return this;
    }
}
