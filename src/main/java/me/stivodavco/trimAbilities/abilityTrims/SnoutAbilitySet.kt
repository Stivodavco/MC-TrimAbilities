package me.stivodavco.trimAbilities.abilityTrims

import me.stivodavco.trimAbilities.TrimAbilities
import me.stivodavco.trimAbilities.abstractClasses.AbilitySet
import me.stivodavco.trimAbilities.abstractClasses.Passive
import me.stivodavco.trimAbilities.abstractClasses.TimeAbility
import me.stivodavco.trimAbilities.utilities.Storage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Particle
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.PiglinBarterEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryPickupItemEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.TrimPattern
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class SnoutAbilitySet(val owner: Player) : AbilitySet(owner, TrimPattern.SNOUT), TimeAbility, Passive {
    override var activeTimeLeft = 0
    override var active = false
    override var cooldownLeft = cooldown

    private val spawnedPiglins: MutableList<Entity> = mutableListOf()
    private var runnable: BukkitTask? = null

    override fun internalAbility() {
        val brute = owner.world.spawn(owner.location, PiglinBrute::class.java)
        brute.isImmuneToZombification = true
        spawnedPiglins.add(brute)

        for (i in 1..3) {
            val piglin = owner.world.spawn(owner.location, Piglin::class.java)
            piglin.isImmuneToZombification = true
            spawnedPiglins.add(piglin)
        }

        runnable = object : BukkitRunnable() {
            override fun run() {
                despawnPiglins()
            }
        }.runTaskLater(TrimAbilities.getInstance(), activationTime * 20L)
    }

    override fun internalDestroy() {
        runnable?.cancel()
        despawnPiglins()

        for (item in owner.inventory.contents) {
            if (item?.persistentDataContainer?.has(KEY) == true) {
                clearItem(item)
            }
        }
    }

    override fun internalPassive() {}

    /* HELPER FUNCTIONS */
    fun despawnPiglins() {
        for (piglin in spawnedPiglins) {
            Particle.POOF.builder().
            location(piglin.location)
                .receivers(20)
                .count(1)
                .spawn()

            piglin.remove()
        }
        spawnedPiglins.clear()
    }

    fun clearItem(item: ItemStack) {
        item.editPersistentDataContainer { pdc: PersistentDataContainer? ->
            pdc!!.remove(KEY)
        }
        val meta = item.itemMeta
        meta.lore(null)
        item.itemMeta = meta
    }

    /* EVENTS */
    @EventHandler
    fun onEntityTarget(e: EntityTargetEvent) {
        val target = e.target
        if (target is Player && e.entity is PiglinAbstract && Storage.getAbilitySet(target) == this) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onPiglinBarter(e: PiglinBarterEvent) {
        for (item in e.outcome) {
            item.editPersistentDataContainer { pdc: PersistentDataContainer? ->
                pdc!!.set(KEY, PersistentDataType.INTEGER, item.amount)
            }
            val meta = item.itemMeta
            meta.lore(
                listOf(
                    Component.text("Bartered Item ").color(NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false).decorate(TextDecoration.BOLD),
                    Component.text("Refundable with Snout Ability Trim").color(NamedTextColor.GRAY),
                    Component.text("Accept [Left Click]").color(NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                    Component.text("Refund [Shift + Left Click]").color(NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false)
                )
            )
            item.itemMeta = meta
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onInventoryClick(e: InventoryClickEvent) {
        val player = TrimAbilities.getInstance().server.getPlayer(e.whoClicked.uniqueId)
        val item = e.currentItem

        if (item == null || player == null) return

        if (Storage.getAbilitySet(player) == this) {
            val acceptClick = ClickType.LEFT
            val refundClick = ClickType.SHIFT_LEFT

            val pdc = item.persistentDataContainer

            if (pdc.has(KEY)) {
                e.isCancelled = true

                if (e.click == acceptClick) {
                    clearItem(item)
                } else if (e.click == refundClick) {
                    e.currentItem = null
                    player.give(
                        ItemStack.of(Material.GOLD_INGOT).asQuantity(
                            Math.floorDiv(
                                item.amount,
                                pdc.get(KEY, PersistentDataType.INTEGER)!!
                            )
                        )
                    )
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerDropItem(e: PlayerDropItemEvent) {
        val item = e.itemDrop.itemStack

        val pdc = item.persistentDataContainer

        if (pdc.has(KEY)) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onEntityPickupItem(e: EntityPickupItemEvent) {
        val item = e.item.itemStack
        val player = TrimAbilities.getInstance().server.getPlayer(e.entity.uniqueId)

        if (item.persistentDataContainer.has(KEY)) {
            if (player != null && Storage.getAbilitySet(player) !is SnoutAbilitySet) {
                clearItem(item)
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onInventoryPickupItem(e: InventoryPickupItemEvent) {
        val item = e.item.itemStack

        if (item.persistentDataContainer.has(KEY)) {
            clearItem(item)
        }
    }

    companion object {
        val KEY = NamespacedKey.fromString("barter_stack_size", TrimAbilities.getInstance())!!
    }
}