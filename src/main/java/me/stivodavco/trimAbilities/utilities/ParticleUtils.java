package me.stivodavco.trimAbilities.utilities;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;

public class ParticleUtils {
    public static void circle(Location center, double radius, ParticleBuilder particle) {
        for (double i = 0; i < Math.PI * 2; i += 0.2) {
            double x = Math.cos(i) * radius;
            double z = Math.sin(i) * radius;

            Location loc = center.clone().add(x, 0, z);

            particle.location(loc).receivers(50).spawn();
        }
    }

    public static void sphere(Location center, double radius, ParticleBuilder particle) {
        for (double y = -radius; y <= radius; y += 0.5) {
            Location circleCenter = center.clone().add(0, y, 0);
            circle(circleCenter, Math.sqrt(radius * radius - y * y), particle);
        }
    }

    public static void cylinder(Location center, double radius, double height, ParticleBuilder particle) {
        for (double y = 0; y <= height; y += 0.5) {
            circle(center.clone().add(0, y, 0), radius, particle);
        }
    }
}
