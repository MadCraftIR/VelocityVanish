package ir.syrent.velocityvanish.spigot.listener

import ir.syrent.velocityvanish.spigot.VelocityVanishSpigot
import ir.syrent.velocityvanish.spigot.ruom.Ruom
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent


class PlayerInteractListener(
    private val plugin: VelocityVanishSpigot
) : Listener {

    init {
        Ruom.registerListener(this)
    }

    // TODO: Fix nested if statements
    @EventHandler
    private fun onActivePlate(event: PlayerInteractEvent) {
        val player = event.player

        if (!plugin.vanishedNames.contains(player.name)) return

        if (event.action == Action.PHYSICAL && event.clickedBlock != null) {
            if (event.clickedBlock!!.type.toString().contains("PLATE")) {
                event.isCancelled = true
            }
        }
    }

    private val silentInventoryMaterials = Material.values().filter { it.name.contains("CHEST") || it.name.contains("SHULKER_BOX") || it.name.contains("BARREL") }

    /**
     * Note: Not working when some ProCosmetics cosmetics are enabled!
     */
    @EventHandler
    fun onChestOpen(event: PlayerInteractEvent) {
        val block = event.clickedBlock ?: return
        val player = event.player

        if (!plugin.vanishedNames.contains(player.name)) return


        if (block.type == Material.ENDER_CHEST) {
            event.isCancelled = true
            player.openInventory(player.enderChest)
            return
        }

        if (!silentInventoryMaterials.contains(block.type)) return

        val gamemode = player.gameMode
        player.gameMode = GameMode.SPECTATOR
        Ruom.runSync({
            player.gameMode = gamemode
        }, 2)
    }}