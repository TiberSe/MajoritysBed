package tech.septims.majoritysbed.config

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.logging.Level

open class ConfigBase () {
    private lateinit var configFile: File
    private lateinit var plugin: Plugin
    private lateinit var file: String

    constructor(plugin: Plugin, file: String) : this() {
        this.configFile = File(plugin.dataFolder, file)
        this.plugin = plugin
        this.file = file;
    }

    private var config: FileConfiguration? = null
    fun getConfig(): FileConfiguration? {
        if (config == null) {
            reloadConfig()
        }
        return config
    }
    private fun reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile)
        val defConfigStream = plugin.getResource(file) ?: return
        (config as YamlConfiguration).setDefaults(YamlConfiguration.loadConfiguration(InputStreamReader(defConfigStream, StandardCharsets.UTF_8)))
    }
    fun saveDefaultConfig() {
        if (!configFile.exists()) {
            plugin.saveResource(file, false)
        }
    }
    fun saveDefaultConfig(overwrite : Boolean) {
        if (!configFile.exists()) {
            plugin.saveResource(file, overwrite)
        }
    }

    fun saveConfig() {
        if (config == null) {
            return
        }
        try {
            getConfig()!!.save(configFile)
        } catch (ex: IOException) {
            plugin.logger.log(Level.SEVERE, "Could not save config to $configFile", ex)
        }
    }

}