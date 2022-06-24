package tech.septims.majoritysbed.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin

class SystemConfig : ConfigBase {
    private var systemConfig: FileConfiguration

    constructor(plugin: Plugin, file: String) : super(plugin, file){
        super.saveDefaultConfig()
        systemConfig = super.getConfig()!!
    }
}