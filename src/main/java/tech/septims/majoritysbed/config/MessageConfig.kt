package tech.septims.majoritysbed.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin

class MessageConfig : ConfigBase {
    private var messageConfig: FileConfiguration
    constructor(plugin: Plugin, file: String) : super(plugin, file){
        super.saveDefaultConfig()
        messageConfig = super.getConfig()!!
    }
}