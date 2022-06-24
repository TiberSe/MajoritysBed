package tech.septims.majoritysbed.config

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import java.util.StringJoiner

class MessageConfig : ConfigBase {

    constructor(plugin: Plugin, file: String) : super(plugin, file){
        super.saveDefaultConfig()
        messageConfig = super.getConfig()!!
        lang = getLanguage()
    }
    private fun getLanguage() : String {
        return when(val langRaw = MessageConfig.messageConfig.getString("lang", "en")){
            "ja" -> "ja"
            "en" -> "en"
            else -> {
                Bukkit.getLogger().warning("The language: %s is not supported. Uses English instead.".format(langRaw))
                "en"
            }
        }
    }
    companion object {
        private lateinit var lang : String
        private lateinit var messageConfig: FileConfiguration

        fun getVotePassedMessage(): String {
            return messageConfig.getString("%s.broadcast.votePassedMessage".format(lang), "§b[MajoritysBed]§r The night was skipped due to majority ayes.").toString()
        }

        fun getVoteRejectMessage(): String {
            return messageConfig.getString("%s.broadcast.voteRejectMessage".format(lang), "§b[MajoritysBed]§r The night was not skipped due to a majority of objections.").toString()
        }

        fun getVoteUndecidedMessage(): String {
            return messageConfig.getString("%s.broadcast.voteUndecidedMessage".format(lang), "§b[MajoritysBed]§r The vote was rejected on a tie vote.").toString()
        }

        fun getVoteBelowStipulationMessage(): String {
            return messageConfig.getString("%s.broadcast.voteBelowStipulationMessage".format(lang), "§b[MajoritysBed]§r The vote was Dismissed because the percentage of votes cast was below the stipulation.").toString()
        }

        fun getVotedToAgreeMessage(): String{
            return messageConfig.getString("%s.notice.voteToAgreeMessage".format(lang), "§b[MajoritysBed]§r Voted to Agree.").toString()
        }

        fun getVotedToDeclineMessage(): String{
            return messageConfig.getString("%s.notice.voteToDeclineMessage".format(lang), "§b[MajoritysBed]§r Voted to Decline.").toString()
        }

        fun getVoteIsNotHeldMessage(): String {
            return messageConfig.getString("%s.notice.voteIsNotHeldMessage".format(lang), "§b[MajoritysBed]§r Voting is not currently held.").toString()
        }

        fun getVoteSuggestsMessage(): String {
            return messageConfig.getString("%s.notice.voteSuggestsMessage".format(lang), "§b[MajoritysBed]§r %s suggests sleep.").toString()
        }

        fun getVoteAgreeButtonText(): String {
            return messageConfig.getString("%s.notice.voteAgreeButtonText".format(lang), "[AGREE] ").toString()
        }

        fun getVoteDeclineButtonText(): String {
            return messageConfig.getString("%s.notice.voteDeclineButtonText".format(lang), " [DECLINE]").toString()
        }


    }
}