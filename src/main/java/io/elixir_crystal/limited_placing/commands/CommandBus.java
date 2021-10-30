package io.elixir_crystal.limited_placing.commands;

import io.elixir_crystal.limited_placing.PlugGividado;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import redempt.redlib.commandmanager.CommandHook;
import redempt.redlib.inventorygui.InventoryGUI;
import redempt.redlib.inventorygui.ItemButton;
import redempt.redlib.itemutils.ItemBuilder;
import redempt.redlib.misc.FormatUtils;

@RequiredArgsConstructor
@Getter
public class CommandBus {

    private final PlugGividado plug;

    @CommandHook("open")
    public void open(CommandSender sender) {
        Bukkit.getScheduler().runTask(getPlug(), () -> {
            InventoryGUI gui = new InventoryGUI(9, FormatUtils.color("&4&l建造数量达到上限"));
            gui.addButton(0, new ItemButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setName(FormatUtils.color(" "))
                    .addLore(FormatUtils.color("&f"))
                    .addLore(FormatUtils.color("&e资源世界 &f主要用于采集"))
                    .addLore(FormatUtils.color("&f大型建筑工程请移步 &c生存世界"))
                    .addLore(FormatUtils.color("&f"))
                    .addLore(FormatUtils.color("&f点击此按钮来忽略建筑上限 (以后将不会有提示)"))
                    .addLore(FormatUtils.color("&f"))
                    .addDamage(1)) {

                @Override
                public void onClick(InventoryClickEvent evt) {
                    if (!evt.getWhoClicked().hasPermission(getPlug().getCman().getConfig().getString("taggingPermission"))) {
                        getPlug().addPermission(evt.getWhoClicked().getUniqueId(), getPlug().getCman().getConfig().getString("taggingPermission"));
                        evt.getWhoClicked().closeInventory();
                    }
                }
            });
            gui.addButton(8, new ItemButton(new ItemBuilder(Material.STAINED_GLASS_PANE).setName(FormatUtils.color(" "))
                    .addLore(FormatUtils.color("&f"))
                    .addLore(FormatUtils.color("&c为什么不能建造??"))
                    .addLore(FormatUtils.color("&c我想继续建造!!!"))
                    .addLore(FormatUtils.color("&f"))
                    .addDamage(14)) {

                @Override
                public void onClick(InventoryClickEvent evt) {
                    ((Player) evt.getWhoClicked()).performCommand(getPlug().getCman().getConfig().getString("deniedCommand"));
                    evt.getWhoClicked().closeInventory();
                }
            });

            for (int bd : new int[]{1, 2, 3, 4, 5, 6, 7}) {
                gui.getInventory().setItem(bd, new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").addDamage(7));
            }

            gui.open((Player) sender);
        });
    }

    @CommandHook("openfor")
    public void openfor(CommandSender sender, String plrName) {
        open(Bukkit.getPlayer(plrName));
    }

    @CommandHook("reload")
    public void reload(CommandSender sender) {
        Bukkit.getScheduler().runTask(getPlug(), new ReloadRunnable(getPlug(), sender));
    }

}
