package tech.septims.majoritysbed.event

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.Statistic
import org.bukkit.World
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import tech.septims.majoritysbed.ElectionManager
import tech.septims.majoritysbed.MajoritysBed
import tech.septims.majoritysbed.config.MessageConfig

class VoteEvent {
    private var playerSize : Int
    private var notVotedPlayer : ArrayList<Player>
    private var agreePlayer: ArrayList<Player> = ArrayList()
    private var declinePlayer: ArrayList<Player> = ArrayList()
    private var world: World
    private val proposer: Player
    private var progressBar: BossBar
    private val endTimer: BukkitTask

    constructor(player: Player, world: World) {
        this.world = world
        playerSize = world.players.size
        notVotedPlayer = ArrayList(world.players)
        proposer = player
        progressBar = Bukkit.getServer().createBossBar("", BarColor.BLUE, BarStyle.SEGMENTED_10)
        updateProgressBar()
        notifyVote()
        world.players.forEach {
            progressBar.addPlayer(it)
        }
        progressBar.progress = 1.0 / playerSize.toDouble()
        endTimer = Bukkit.getScheduler().runTaskLater(MajoritysBed.getInstance(), Runnable { end() }, 60 * 20)
    }
    private fun updateProgressBar(){
        progressBar.progress = agreePlayer.size.toDouble() / playerSize.toDouble()
        progressBar.setTitle(String.format("!VOTE SKIP[%1\$d]!  « AGREE:%2\$d VS DECLINE:%3\$d »",
                notVotedPlayer.size,
                agreePlayer.size,
                declinePlayer.size))
        return
    }

    fun voteAgree(player: Player) : Boolean {
        if(!isNotVoted(player)) { return false }
        agreePlayer.add(player)
        notVotedPlayer.remove(player)
        updateProgressBar()
        check()
        return true
    }

    fun voteDecline(player: Player) : Boolean {
        if(!isNotVoted(player)) { return false }
        declinePlayer.add(player)
        notVotedPlayer.remove(player)
        updateProgressBar()
        check()
        return true
    }

    fun isNotVoted(player: Player) : Boolean {
        return (notVotedPlayer.contains(player))
    }

    private fun check() : Boolean{
        if ((playerSize / 2) < agreePlayer.size) {
            passVote()
            return true
        } else if ((playerSize / 2) < declinePlayer.size) {
            rejectVote()
            return true
        } else if (notVotedPlayer.size == 0) {
            Bukkit.broadcastMessage(MessageConfig.getVoteUndecidedMessage())
            finalize()
            return false
        }
        return false
    }

    private fun passVote(){
        agreePlayer.forEach{ it.setStatistic(Statistic.TIME_SINCE_REST, 0) }
        Bukkit.broadcastMessage(MessageConfig.getVotePassedMessage())
        world.weatherDuration = 0
        world.isThundering = false
        world.setStorm(false)
        world.time = 0
        finalize()
    }

    private fun rejectVote(){
        Bukkit.broadcastMessage(MessageConfig.getVoteRejectMessage())
        finalize()
    }

    private fun finalize(){
        progressBar.removeAll()
        endTimer.cancel()
        ElectionManager.endElection(this)
    }

    private fun end(){
        Bukkit.broadcastMessage(MessageConfig.getVoteBelowStipulationMessage())
        finalize()
    }

    private fun notifyVote(){
        val message = TextComponent(String.format(MessageConfig.getVoteSuggestsMessage(), proposer.name))
        message.isBold = false
        message.color = ChatColor.UNDERLINE
        Bukkit.getServer().spigot().broadcast(message)
        val commandMsg = TextComponent(MessageConfig.getVoteAgreeButtonText())
        commandMsg.color = ChatColor.GREEN
        commandMsg.isBold = true
        commandMsg.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/majoritysbed vote agree")
        val declineMsg = TextComponent(MessageConfig.getVoteDeclineButtonText())
        declineMsg.color = ChatColor.RED
        declineMsg.isBold = true
        declineMsg.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/majoritysbed vote decline")
        commandMsg.addExtra(declineMsg)
        world.players.forEach{
            it.spigot().sendMessage(commandMsg)
        }
    }
}