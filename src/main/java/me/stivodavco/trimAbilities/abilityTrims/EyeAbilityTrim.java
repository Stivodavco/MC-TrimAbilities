package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

public class EyeAbilityTrim extends AbilityTrim {
    public static final int MAX_TELEPORT_DIST = 80;
    public static final PotionEffect GLOWING = new PotionEffect(PotionEffectType.GLOWING, 10 * 20, 255, true, false);

    public EyeAbilityTrim(Player newOwner) {
        super(newOwner, 240, AbilityType.INSTANT, 0, 0, "Eye", "Ender Sight");
    }

    @Override
    public void enable() {}

    private void trail(Location from, Location to) {
        double distance = from.distance(getOwner().getLocation());
        Vector direction = to.toVector().subtract(from.toVector()).normalize();

        ParticleBuilder particle = Particle.DUST.builder()
                .data(new Particle.DustOptions(Color.fromRGB(255,0,255), 4))
                .extra(0);

        for (Location loc : List.of(from, to)) {
            getOwner().getWorld().playSound(loc, Sound.ENTITY_PLAYER_TELEPORT, 1, 0);
        }

        for (double i = 0; i <= distance; i++) {
            Location loc = from.clone().add(direction.clone().multiply(i));

            particle.location(loc).spawn();
        }
    }

    @Override
    public void customAbility() {
        Location startLoc = getOwner().getLocation();

        Block targetBlock = getOwner().getTargetBlockExact(MAX_TELEPORT_DIST);
        if (targetBlock != null) {
            Location teleportLoc = targetBlock.getLocation().add(0, 1, 0);
            teleportLoc.setRotation(getOwner().getLocation().getRotation());
            getOwner().teleport(teleportLoc);

            trail(startLoc, getOwner().getLocation());

            getOwner().addPotionEffect(GLOWING);
        }
    }

    @Override
    public void internalDisable() {}

    @Override
    public void internalUsed() {}
}
