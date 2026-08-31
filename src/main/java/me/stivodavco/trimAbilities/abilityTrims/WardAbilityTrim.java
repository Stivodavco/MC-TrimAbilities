package me.stivodavco.trimAbilities.abilityTrims;

import me.stivodavco.trimAbilities.TrimAbilities;
import me.stivodavco.trimAbilities.abstractClasses.AbilityTrim;
import me.stivodavco.trimAbilities.enums.AbilityActivationType;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;

public class WardAbilityTrim extends AbilityTrim {
    public static final NamespacedKey MODIFIER_KEY = new NamespacedKey(TrimAbilities.getInstance(), "ward_trim_health_modifier");

    public WardAbilityTrim(Player newOwner) {
        super(newOwner, 180, AbilityActivationType.INSTANT, 0, "Wild", "Sensitive Shriek");
    }

    @Override
    public void enable() {
        AttributeInstance maxHealth =  getOwner().getAttribute(Attribute.MAX_HEALTH);

        if (maxHealth != null && maxHealth.getModifier(MODIFIER_KEY) == null) {
            AttributeModifier modifier = new AttributeModifier(MODIFIER_KEY, 10, AttributeModifier.Operation.ADD_NUMBER);

            maxHealth.addModifier(modifier);
        }
    }

    @Override
    public void customAbility() {

    }

    @Override
    public void internalDisable() {
        AttributeInstance maxHealth =  getOwner().getAttribute(Attribute.MAX_HEALTH);

        if (maxHealth != null && maxHealth.getModifier(MODIFIER_KEY) != null) {
            maxHealth.removeModifier(MODIFIER_KEY);
        }
    }
}
