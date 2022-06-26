package tech.septims.majoritysbed.config

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.util.StringJoiner

class MessageConfig : ConfigBase {

    constructor(plugin: Plugin, file: String) : super(plugin, file){
        super.saveDefaultConfig()
        messageConfig = super.getConfig()!!
    }

    companion object {
        private lateinit var messageConfig: FileConfiguration

        fun getVotePassedMessage(): String {
            return messageConfig.getString("broadcast.votePassedMessage", "§b[MajoritysBed]§r The night was skipped due to majority ayes.").toString()
        }

        fun getVoteRejectMessage(): String {
            return messageConfig.getString("broadcast.voteRejectMessage", "§b[MajoritysBed]§r The night was not skipped due to a majority of objections.").toString()
        }

        fun getVoteUndecidedMessage(): String {
            return messageConfig.getString("broadcast.voteUndecidedMessage", "§b[MajoritysBed]§r The vote was rejected on a tie vote.").toString()
        }

        fun getVoteBelowStipulationMessage(): String {
            return messageConfig.getString("broadcast.voteBelowStipulationMessage", "§b[MajoritysBed]§r The vote was Dismissed because the percentage of votes cast was below the stipulation.").toString()
        }

        fun getVotedToAgreeMessage(): String{
            return messageConfig.getString("notice.voteToAgreeMessage", "§b[MajoritysBed]§r Voted to Agree.").toString()
        }

        fun getVotedToDeclineMessage(): String{
            return messageConfig.getString("notice.voteToDeclineMessage", "§b[MajoritysBed]§r Voted to Decline.").toString()
        }

        fun getVoteIsNotHeldMessage(): String {
            return messageConfig.getString("notice.voteIsNotHeldMessage", "§b[MajoritysBed]§r Voting is not currently held.").toString()
        }

        fun getVoteSuggestsMessage(): String {
            return messageConfig.getString("notice.voteSuggestsMessage", "§b[MajoritysBed]§r %s suggests sleep.").toString()
        }

        fun getVoteAgreeButtonText(): String {
            return messageConfig.getString("notice.voteAgreeButtonText", "[AGREE] ").toString()
        }

        fun getVoteDeclineButtonText(): String {
            return messageConfig.getString("notice.voteDeclineButtonText", " [DECLINE]").toString()
        }
    }
}