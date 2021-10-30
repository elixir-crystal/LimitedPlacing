package io.elixir_crystal.limited_placing;

import io.elixir_crystal.limited_placing.commands.CommandBus;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import redempt.redlib.commandmanager.CommandParser;
import redempt.redlib.configmanager.ConfigManager;
import redempt.redlib.misc.EventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlugGividado extends JavaPlugin {

    private Plugin plug;
    private ConfigManager cman;
    private LuckPerms lman;

    @Override
    public void onEnable() {

        plug = this;
        cman = new ConfigManager(this).register(new ConfigBus()).saveDefaults().load();
        CommandBus dman = new CommandBus(this);
        new CommandParser(getPlug().getResource("command.rdcml")).parse().register(getPlug().getDescription().getName(),
                dman);

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            lman = provider.getProvider();
        }

        new EventListener<>(this, BlockPlaceEvent.class, evt -> {
            if (evt.isCancelled()) return;
            if (getCman().getConfig().getBoolean("debug"))
                System.out.println(Boolean.valueOf(getStatistics().containsKey(evt.getPlayer().getUniqueId())).toString() + getStatistics().getOrDefault(evt.getPlayer().getUniqueId(), 0));
            if (!evt.getPlayer().hasPermission(getCman().getConfig().getString("taggingPermission"))) {
                if (getCman().getConfig().getBoolean("debug")) System.out.println("tag 1");
                for (String w : getCman().getConfig().getStringList("enabledWorlds")) {
                    if (evt.getBlock().getWorld().getName().equals(w)) {
                        if (getCman().getConfig().getBoolean("debug")) System.out.println("tag 2");
                        getStatistics().put(evt.getPlayer().getUniqueId(), getStatistics().getOrDefault(evt.getPlayer().getUniqueId(), 0) + 1);
                        if (getStatistics().getOrDefault(evt.getPlayer().getUniqueId(), 0) > getCman().getConfig().getInt("limitBlockNumber")) {
                            if (getCman().getConfig().getBoolean("debug")) System.out.println("tag 3");
                            dman.open(evt.getPlayer());
                        }
                    }
                }
            }
        });
    }

    public String getPrefix() {
        return cman.getConfig().getString("prefix");
    }

    private final Map<UUID, Integer> statistics = new HashMap<>();

    public void addPermission(UUID userUuid, String permission) {
        getLman().getUserManager().modifyUser(userUuid, user -> user.data().add(Node.builder(permission).build()));
    }

}
