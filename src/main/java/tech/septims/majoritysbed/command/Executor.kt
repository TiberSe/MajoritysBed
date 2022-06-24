package tech.septims.majoritysbed.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import tech.septims.majoritysbed.ElectionManager
import tech.septims.majoritysbed.config.MessageConfig

class Executor() : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (command.name) {
            "majoritysbed" -> {
                if(args.size != 2) { return false }
                val player = sender as Player
                when (args[0]) {
                    "vote" -> {
                        if(!ElectionManager.inVoting(player.world)){
                            player.sendMessage(MessageConfig.getVoteIsNotHeldMessage())
                            return false
                        }
                        when(args[1]){
                            "agree" -> {
                                if(ElectionManager.voteAgree(player, player.world)){
                                    player.sendMessage(MessageConfig.getVotedToAgreeMessage())
                                }
                                return true
                            }
                            "decline" -> {
                                if(ElectionManager.voteDecline(player, player.world)){
                                    player.sendMessage(MessageConfig.getVotedToDeclineMessage())
                                }
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }
}