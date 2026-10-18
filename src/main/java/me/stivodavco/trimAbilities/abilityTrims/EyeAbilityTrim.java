package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class EyeAbilityTrim extends AbilityTrim {
    public static final int MAX_TELEPORT_DIST = 80;
    public static final PotionEffect GLOWING = new PotionEffect(PotionEffectType.GLOWING, 10 * 20, 255, true, false);

    public EyeAbilityTrim(Player newOwner) {
        super(newOwner, TrimPattern.EYE, AbilityType.INSTANT);
    }

    @Override
    public void enable() {}

    @Override
    public void customAbility() {
        Location startLoc = getOwner().getLocation();

        Block targetBlock = getOwner().getTargetBlockExact(MAX_TELEPORT_DIST);
        if (targetBlock != null) {
            Location teleportLoc = targetBlock.getLocation().add(0, 1, 0);
            teleportLoc.setRotation(getOwner().getLocation().getRotation());
            getOwner().teleport(teleportLoc);

            ParticleBuilder particle = Particle.DUST.builder()
                    .data(new Particle.DustOptions(Color.fromRGB(255,0,255), 4))
                    .extra(0);

            ParticleUtils.beam(startLoc, getOwner().getLocation(), particle);

            for (Location loc : List.of(startLoc, getOwner().getLocation())) {
                getOwner().getWorld().playSound(loc, Sound.ENTITY_PLAYER_TELEPORT, 1, 0);
            }

            getOwner().addPotionEffect(GLOWING);
        }
    }

    @Override
    public void internalDisable() {}

    @Override
    public void internalUsed() {}
}
