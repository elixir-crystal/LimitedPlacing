package io.elixir_crystal.limited_placing;

import lombok.Getter;
import redempt.redlib.configmanager.annotations.ConfigValue;
import redempt.redlib.misc.FormatUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ConfigBus {

    @ConfigValue("debug")
    public boolean debug = false;

    @ConfigValue("prefix")
    public String prefix = FormatUtils.color("&f[&b限制&f] ");

    @ConfigValue("limitBlockNumber")
    public int limitBlockNumber = 100;

    @ConfigValue("deniedCommand")
    public String deniedCommand = "spawn";

    @ConfigValue("enabledWorlds")
    public List<String> worlds = new ArrayList<>();

    @ConfigValue("taggingPermission")
    public String taggingPermission = "limitedplacing.granted";

    public ConfigBus() {
        worlds.add("resource_world");
        worlds.add("resource_nether");
        worlds.add("resource_end");
    }

}