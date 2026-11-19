package me.stivodavco.trimAbilities.utilities;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.List;

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

    public static void beam(Location from, Location to, ParticleBuilder particle) {
        double distance = from.distance(to);
        Vector direction = to.toVector().subtract(from.toVector()).normalize();

        for (double i = 0; i <= distance; i++) {
            Location loc = from.clone().add(direction.clone().multiply(i));

            particle.location(loc).spawn();
        }
    }

    public static void cube(Location fromCorner, Location toCorner, ParticleBuilder particle) {
        World world = fromCorner.getWorld();

        double x1 = fromCorner.getX(), y1 = fromCorner.getY(), z1 = fromCorner.getZ();
        double x2 = toCorner.getX(),   y2 = toCorner.getY(),   z2 = toCorner.getZ();

        Location b1 = new Location(world, x1, y1, z1);
        Location b2 = new Location(world, x2, y1, z1);
        Location b3 = new Location(world, x2, y1, z2);
        Location b4 = new Location(world, x1, y1, z2);

        Location t1 = new Location(world, x1, y2, z1);
        Location t2 = new Location(world, x2, y2, z1);
        Location t3 = new Location(world, x2, y2, z2);
        Location t4 = new Location(world, x1, y2, z2);

        beam(b1, b2, particle);
        beam(b2, b3, particle);
        beam(b3, b4, particle);
        beam(b4, b1, particle);

        beam(t1, t2, particle);
        beam(t2, t3, particle);
        beam(t3, t4, particle);
        beam(t4, t1, particle);

        beam(b1, t1, particle);
        beam(b2, t2, particle);
        beam(b3, t3, particle);
        beam(b4, t4, particle);
    }
}
