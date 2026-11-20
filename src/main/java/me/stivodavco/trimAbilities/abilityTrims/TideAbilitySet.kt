package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.abstractClasses.TimeAbility
import me.stivodavco.trimAbilities.utilities.ParticleUtils
import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.Particle.DustOptions
import org.bukkit.Sound
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.bukkit.util.Vector


class TideAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.TIDE), TimeAbility, Passive {
    override var activeTimeLeft = 0
    override var active = false
    override var cooldownLeft = cooldown

    private var passiveRunnable: BukkitTask? = null
    private var abilityRunnable: BukkitTask? = null

    init {
        passive()
    }

    override fun internalPassive() {
        passiveRunnable = object : BukkitRunnable() {
            override fun run() {
                if ((owner.isInWater || owner.isInRain) && !owner.hasPotionEffect(PotionEffectType.REGENERATION)) {
                    owner.addPotionEffect(REGEN)
                }
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, 10)
    }

    override fun internalAbility() : Boolean {
        owner.setGravity(false)
        owner.allowFlight = true
        abilityRunnable = object : BukkitRunnable() {
            var ticksPassed: Int = 0

            override fun run() {
                if (!active) {
                    owner.setGravity(true)
                    owner.allowFlight = false
                    owner.isFlying = false
                    abilityRunnable = null
                    cancel()
                }

                for (entity in owner.getNearbyEntities(KNOCKBACK_RADIUS, KNOCKBACK_RADIUS, KNOCKBACK_RADIUS)) {
                    if (entity is LivingEntity) {
                        val knockbackVector: Vector = entity.velocity.clone().normalize().multiply(-KNOCKBACK_POWER)
                        entity.velocity = knockbackVector

                        owner.world.playSound(owner.location, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 1f, 0f)
                    }
                }

                owner.world.playSound(owner.location, Sound.ENTITY_BREEZE_IDLE_GROUND, 1f, 0f)

                if (ticksPassed >= 10) {
                    val particle = Particle.DUST.builder()
                        .data(DustOptions(Color.AQUA, 4f))
                        .extra(0.2)

                    ParticleUtils.sphere(owner.location, KNOCKBACK_RADIUS, particle)
                }

                ticksPassed += 5
            }
        }.runTaskTimer(TrimAbilities.getInstance(), 0, 5)
        return true
    }

    override fun internalDestroy() {
        owner.setGravity(true)
        owner.allowFlight = false

        passiveRunnable?.cancel()
        abilityRunnable?.cancel()
    }

    /* EVENTS */
    @EventHandler(ignoreCancelled = true)
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (active && e.player == owner) {
            e.isCancelled = true
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityDamage(e: EntityDamageEvent) {
        if (active && e.entity == owner) {
            e.isCancelled = true
        }
    }

    companion object {
        val KNOCKBACK_RADIUS = TrimAbilities.getTrimsConfig().getDouble("tide.ability.knockback-radius")
        val KNOCKBACK_POWER = TrimAbilities.getTrimsConfig().getDouble("tide.ability.knockback-power")

        val REGEN = PotionEffect(PotionEffectType.REGENERATION, -1,
            TrimAbilities.getTrimsConfig().getInt("tide.passive.regen-amplifier"), false, false)
    }

}