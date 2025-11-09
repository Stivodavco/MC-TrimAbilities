package me.stivodavco.trimAbilities.utilities;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class ParticleUtils {
    public static void circlePlr(Player plr, double radius, ParticleBuilder particle) {
        for (double i = 0; i < Math.PI * 2; i += 0.01) {
            double x = Math.cos(i) * radius;
            double z = Math.sin(i) * radius;

            Location loc = plr.getLocation().clone().add(x, 0, z);

            particle.location(loc).source(plr).receivers(50).spawn();
        }
    }
}
