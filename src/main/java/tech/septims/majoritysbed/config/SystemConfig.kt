package tech.septims.majoritysbed.config

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.Plugin
import java.io.File

class SystemConfig : ConfigBase {

    constructor(plugin: Plugin, file: String) : super(plugin, file){
        super.saveDefaultConfig()
        systemConfig = super.getConfig()!!
    }
    companion object{
        private lateinit var systemConfig: FileConfiguration
        fun getLanguageFile() : String {
            return when(val langRaw = systemConfig.getString("lang", "en")){
                "ja_JP" -> "ja_JP.yml"
                "en_UK" -> "en_GB.yml"
                else -> {
                    Bukkit.getLogger().warning("The language: %s is not supported. Uses English instead.".format(langRaw))
                    "en_GB.yml"
                }
            }
        }
    }
}