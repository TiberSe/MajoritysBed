package tech.septims.majoritysbed

import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import tech.septims.majoritysbed.event.VoteEvent

class ElectionManager {
    constructor(){
        Bukkit.getWorlds().forEach { ElectionManager.worldList[it] = null }

    }
    companion object {
        private var worldList : HashMap<World, VoteEvent?> = HashMap()
        fun startNewElection(player: Player, world: World) : Boolean{
            if(inVoting(world)) { return false }
            worldList[world] = VoteEvent(player, world)
            return true
        }
        fun endElection(voteEvent: VoteEvent){
            for (world in worldList){
                if(world.value == null) { continue }
                if(world.value!! == voteEvent){
                    world.setValue(null)
                    return
                }
            }
        }
        fun voteAgree(player: Player, world: World) : Boolean{
            if(!inVoting(world)) { return false }
            return worldList[world]?.voteAgree(player) == true
        }
        fun voteDecline(player: Player, world: World) : Boolean{
            if(!inVoting(world)) { return false }
            return worldList[world]?.voteDecline(player) == true
        }

        fun inVoting(world: World) : Boolean{
            return worldList[world] != null
        }

        private fun isNotVoted(player: Player, world: World) : Boolean{
            if(!inVoting(world)) { return false}
            return worldList[world]?.isNotVoted(player) == true
        }
    }


}