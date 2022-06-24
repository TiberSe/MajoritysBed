package tech.septims.majoritysbed.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerBedEnterEvent
import tech.septims.majoritysbed.ElectionManager

class EventListener() : Listener {

    @EventHandler
    fun onPlayerBedEnt(e: PlayerBedEnterEvent){
        if(PlayerBedEnterEvent.BedEnterResult.OK != e.bedEnterResult) { return }
        ElectionManager.startNewElection(e.player, e.bed.world)
        e.isCancelled = true
    }
}