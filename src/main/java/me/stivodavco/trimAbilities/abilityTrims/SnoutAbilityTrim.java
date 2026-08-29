package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.enums.AbilityActivationType;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class SnoutAbilityTrim extends AbilityTrim {
    private List<Entity> spawnedPiglins = new ArrayList<>();
    private BukkitTask task;

    public SnoutAbilityTrim(Player newOwner) {
        super(newOwner, 120, AbilityActivationType.LIMITED_TIME, 20, "Snout", "Call for help");
    }

    public void despawnPiglins() {
        for (Entity piglin : spawnedPiglins) {
            Particle.CLOUD.builder().
                    location(piglin.getLocation()).
                    receivers(20).
                    count(1).
                    spawn();
            piglin.remove();
        }
        spawnedPiglins.clear();
    }

    @Override
    public void customAbility() {
        active = true;
        Location ownerLoc = getOwner().getLocation();

        PiglinBrute brute = getOwner().getWorld().spawn(ownerLoc, PiglinBrute.class);
        brute.setImmuneToZombification(true);
        spawnedPiglins.add(brute);

        for (int i = 1; i <= 3; i++) {
            Piglin piglin = getOwner().getWorld().spawn(ownerLoc, Piglin.class);
            piglin.setImmuneToZombification(true);
            spawnedPiglins.add(piglin);
        }

        task = new BukkitRunnable() {
            @Override
            public void run() {
                despawnPiglins();
            }
        }.runTaskLater(TrimAbilities.getInstance(), getActivationTime() * 20L);
    }

    @Override
    public void disable() {
        if (task != null) {
            task.cancel();
            despawnPiglins();
        }
    }
}
