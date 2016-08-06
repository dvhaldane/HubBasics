package net.notfab.hubbasics;

/*
 * Copyright (c) 2016.
 *
 * The contents of this project are licensed under a Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International.
 * Please read the information linked below before you attempt to use this project or it's contents to make sure you are abiding
 * by it's terms.
 *
 * https://creativecommons.org/licenses/by-nc-sa/4.0/
 */

import lombok.Getter;
import net.notfab.hubbasics.managers.CommandManager;
import net.notfab.hubbasics.managers.CustomItemManager;
import net.notfab.hubbasics.managers.ModuleManager;
import net.notfab.hubbasics.managers.SimpleConfigManager;
import net.notfab.hubbasics.managers.UpdateManager;
import net.notfab.hubbasics.nms.NMSVersion;
import net.notfab.hubbasics.objects.MetricsLite;
import net.notfab.hubbasics.plugin.messages.HMessenger;
import net.notfab.hubbasics.plugin.messages.MessageManager;
import net.notfab.hubbasics.plugin.settings.FileConverter;
import net.notfab.hubbasics.plugin.settings.HConfiguration;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class HubBasics extends JavaPlugin {

    @Getter private static HubBasics instance;

    @Getter private CommandManager commandManager;
    @Getter private HConfiguration pluginConfiguration;
    @Getter private SimpleConfigManager configManager;
    @Getter private MessageManager messageManager;
    @Getter private ModuleManager moduleManager;
    @Getter private MetricsLite metrics;
    @Getter private UpdateManager updateManager;
    @Getter private NMSVersion nmsVersion;
    @Getter private CustomItemManager customItemManager;

    @Override
    public void onEnable() {
        instance = this;
        this.nmsVersion = new NMSVersion();
        this.configManager = new SimpleConfigManager(this);
        this.pluginConfiguration = new HConfiguration();
        this.messageManager = new MessageManager();

        FileConverter.convert();
        this.getMessageManager().loadMessages();
        this.getPluginConfiguration().loadConfig();

        this.moduleManager = new ModuleManager();
        this.getModuleManager().onEnable();

        try {
            metrics = new MetricsLite(this);
            metrics.start();
        } catch (IOException ex) {
            HMessenger.printStackTrace(ex);
        }

        this.updateManager = new UpdateManager();
        this.commandManager = new CommandManager();
        this.customItemManager = new CustomItemManager();

        Bukkit.getPluginManager().registerEvents(this.getCustomItemManager(), this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        this.moduleManager.onDisable();
        instance = null;
    }

}
