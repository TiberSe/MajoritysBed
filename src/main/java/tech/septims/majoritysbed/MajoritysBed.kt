package tech.septims.majoritysbed

import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import tech.septims.majoritysbed.command.Executor
import tech.septims.majoritysbed.config.MessageConfig
import tech.septims.majoritysbed.config.SystemConfig
import tech.septims.majoritysbed.event.EventListener

class MajoritysBed : JavaPlugin(), Listener{
    private lateinit var config: SystemConfig
    private lateinit var messageConfig: MessageConfig
    private lateinit var electionManager: ElectionManager
    override fun onEnable(){
        instance = this
        config = SystemConfig(this, "config.yml")
        messageConfig = MessageConfig(this, "messages.yml")
        electionManager = ElectionManager()
        server.pluginManager.registerEvents(EventListener(), this)
        getCommand("majoritysbed")!!.setExecutor(Executor())
    }

    override fun onDisable() {

    }

    companion object {
        private lateinit var instance: MajoritysBed

        fun getInstance() : Plugin{
            return instance
        }
    }
}