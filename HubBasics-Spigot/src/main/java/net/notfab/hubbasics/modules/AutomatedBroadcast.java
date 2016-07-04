package net.notfab.hubbasics.modules;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;
import net.notfab.hubbasics.HubBasics;
import net.notfab.hubbasics.abstracts.module.Module;
import net.notfab.hubbasics.abstracts.module.ModuleEnum;
import net.notfab.hubbasics.plugin.settings.ConfigurationKey;

public class AutomatedBroadcast extends Module {
    
    private HubBasics pl;
    private boolean enable, random;
    private long timing;
    private List<String> messages;

    public AutomatedBroadcast() {
        super(ModuleEnum.AUTOMATED_BROADCASTS);
        this.pl = HubBasics.getInstance();
    }

    @Override
    public void onEnable() {
        this.enable = getBoolean(ConfigurationKey.AUTOMATED_BROADCASTS_ENABLED);
        this.random = getBoolean(ConfigurationKey.AUTOMATED_BROADCASTS_RANDOM);
        this.timing = getConfig().getInt(ConfigurationKey.AUTOMATED_BROADCASTS_TIMING)*20L;
        this.messages = getConfig().getStringList(ConfigurationKey.AUTOMATED_BROADCASTS_MESSAGES).stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        if (!enable) return;
        if (timing <= 0) {
            Bukkit.getConsoleSender().sendMessage("Automated broadcast timings below or equal to 0 are not allowed!");
            return;
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(pl, new Runnable() {
            Random rand = new Random();
            int index = 0;

            @Override
            public void run() {
                if (random) {
                    Bukkit.broadcastMessage(messages.get(rand.nextInt(messages.size())));
                } else {
                    Bukkit.broadcastMessage(messages.get(index));
                    index++;

                    if (index >= messages.size()) {
                        index = 0;
                    }
                }
            }
        }, timing, timing);
    }

    @Override
    public void onDisable() {

    }
}