package tech.septims.majoritysbed

import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import tech.septims.majoritysbed.command.Executor
import tech.septims.majoritysbed.config.MessageConfig
import tech.septims.majoritysbed.config.SystemConfig
import tech.septims.majoritysbed.event.EventListener
import java.io.File

class MajoritysBed : JavaPlugin(), Listener{
    private lateinit var config: SystemConfig
    private lateinit var electionManager: ElectionManager
    private lateinit var messageConfig: MessageConfig
    override fun onEnable(){
        instance = this
        config = SystemConfig(this, "config.yml")
        messageConfig = MessageConfig(this, SystemConfig.getLanguageFile())
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