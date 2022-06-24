package tech.septims.majoritysbed

import org.bukkit.plugin.java.JavaPlugin
import tech.septims.majoritysbed.config.MessageConfig
import tech.septims.majoritysbed.config.SystemConfig

class MajoritysBed : JavaPlugin() {
    private lateinit var config: SystemConfig
    private lateinit var messageConfig: MessageConfig
    override fun onEnable(){
        config = SystemConfig(this, "config.yml")
        messageConfig = MessageConfig(this, "messages.yml")
    }

    override fun onDisable() {

    }
}