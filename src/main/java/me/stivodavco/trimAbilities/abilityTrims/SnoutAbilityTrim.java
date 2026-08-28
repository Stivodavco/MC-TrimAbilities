package me.stivodavco.trimAbilities.abilityTrims;

import com.destroystokyo.paper.ParticleBuilder;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.enums.AbilityActivationType;
import me.stivodavco.trimAbilities.utilities.ParticleUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class SnoutAbilityTrim extends AbilityTrim {
    public final double RADIUS = 10;

    // potion effects (in ticks)
    public final int BLINDNESS_DURATION = 100;

    public final int SLOWNESS_DURATION = 100;
    public final int SLOWNESS_AMPLIF = 2;

    public final int SPEED_DURATION = 100;
    public final int SPEED_AMPLIF = 2;

    public SnoutAbilityTrim(Player newOwner) {
        super(newOwner, 120, AbilityActivationType.LIMITED_TIME, 20, "Snout", "Call for help");
    }

    @Override
    public void customAbility() {
        active = true;
        //ocation ownerLoc = getOwner().getLocation();
        //List<Location> spawnLocations = List.of(getOwner().);
        //getOwner().getWorld().spawn();
    }

    @Override
    public void disable() {

    }
}
