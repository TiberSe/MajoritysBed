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
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitTask
import tech.septims.majoritysbed.ElectionManager
import tech.septims.majoritysbed.MajoritysBed
import tech.septims.majoritysbed.config.MessageConfig
import tech.septims.majoritysbed.config.SystemConfig

class VoteEvent : Listener {
    private var world: World
    private val proposer: Player

    private var allPlayers : ArrayList<Player>
    private var notVotedPlayer : ArrayList<Player>
    private var agreePlayer: ArrayList<Player> = ArrayList()
    private var declinePlayer: ArrayList<Player> = ArrayList()

    private val requireAgreeRatio: Int
    private val requireDeclineRatio: Int

    private var progressBar: BossBar
    private val progressBarTitle: String

    private val endTimer: BukkitTask

    private val notifyMsg: TextComponent
    private val commandMsg = TextComponent(MessageConfig.getVoteAgreeButtonText())

    constructor(player: Player, world: World) {
        this.world = world
        proposer = player
        allPlayers = ArrayList(world.players)
        requireAgreeRatio = SystemConfig.getRequiredAgreeRatio()
        requireDeclineRatio = SystemConfig.getRequiredDeclineRatio()
        notVotedPlayer = ArrayList(world.players)
        Bukkit.getServer().pluginManager.registerEvents(this, MajoritysBed.getInstance())

        progressBar = Bukkit.getServer().createBossBar("", SystemConfig.getBossbarColor(), SystemConfig.getBossbarStyle())
        progressBarTitle = MessageConfig.getVoteBossBarTitle()

        if(SystemConfig.getBossbarShow()) { world.players.forEach { progressBar.addPlayer(it) } }

        endTimer = Bukkit.getScheduler().runTaskLater(MajoritysBed.getInstance(), Runnable { end() }, SystemConfig.getVoteTimeLimit() * 20)

        notifyMsg = TextComponent(MessageConfig.getVoteSuggestsMessage().format(proposer.name))
        notifyMsg.color = ChatColor.UNDERLINE
        commandMsg.color = ChatColor.GREEN
        commandMsg.isBold = true
        commandMsg.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/majoritysbed vote agree")
        val declineMsg = TextComponent(MessageConfig.getVoteDeclineButtonText())
        declineMsg.color = ChatColor.RED
        declineMsg.isBold = true
        declineMsg.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/majoritysbed vote decline")
        commandMsg.addExtra(declineMsg)

        notifyVote(world.players)
        updateProgressBar()

        if(SystemConfig.getSuggesterAutoVoteToAgree()) { voteAgree(proposer) }

    }

    private fun updateProgressBar(){
        progressBar.progress = (agreePlayer.size / allPlayers.size).toDouble()
        progressBar.setTitle(progressBarTitle.format(
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

    private fun isNotVoted(player: Player) : Boolean {
        return (notVotedPlayer.contains(player))
    }

    private fun check() : Boolean{
        if ((allPlayers.size / requireAgreeRatio) < agreePlayer.size) {
            passVote()
            return true
        } else if ((allPlayers.size / requireDeclineRatio) < declinePlayer.size) {
            rejectVote()
            return true
        } else if (notVotedPlayer.size == 0 ) {
            Bukkit.broadcastMessage(MessageConfig.getVoteUndecidedMessage())
            finalize()
            return false
        }
        return false
    }

    private fun passVote(){
        when(SystemConfig.getPhantomSpawnTimerResetTarget()) {
            "agreed" -> agreePlayer.forEach{ it.setStatistic(Statistic.TIME_SINCE_REST, 0) }
            "all" -> allPlayers.forEach{ it.setStatistic(Statistic.TIME_SINCE_REST, 0) }
        }
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
        HandlerList.unregisterAll(this)
        progressBar.removeAll()
        endTimer.cancel()
        ElectionManager.endElection(this)
    }

    private fun end(){
        Bukkit.broadcastMessage(MessageConfig.getVoteBelowStipulationMessage())
        finalize()
    }

    private fun notifyVote(player: Player){
        player.spigot().sendMessage(notifyMsg)
        player.spigot().sendMessage(commandMsg)
    }
    private fun notifyVote(players: List<Player>){
        players.forEach {
            notifyVote(it)
        }
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent){
        if(e.player.world != this.world){ return }
        this.notVotedPlayer.add(e.player)
        updateProgressBar()
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent){
        if(e.player.world != this.world){ return }
        this.notVotedPlayer.remove(e.player)
        this.agreePlayer.remove(e.player)
        this.declinePlayer.remove(e.player)
        updateProgressBar()
    }
    @EventHandler
    fun onPlayerChangedWorld(e: PlayerChangedWorldEvent){
        if(e.from != world) { return }
        this.notVotedPlayer.remove(e.player)
        this.agreePlayer.remove(e.player)
        this.declinePlayer.remove(e.player)
        updateProgressBar()
    }
}