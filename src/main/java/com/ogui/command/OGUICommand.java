package com.ogui.command;

import com.ogui.OGUIPlugin;
import com.ogui.gui.GuiDefinition;
import com.ogui.util.ColorUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class OGUICommand implements CommandExecutor, TabCompleter {

    private final OGUIPlugin plugin;

    public OGUICommand(OGUIPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ColorUtil.color("&e&lOGUI Enhanced"));
            sender.sendMessage(ColorUtil.color("&7Usage: &f/ogui <open|reload> [id] [player]"));
            sender.sendMessage(ColorUtil.color("&7Examples:"));
            sender.sendMessage(ColorUtil.color("  &f/ogui open shop"));
            sender.sendMessage(ColorUtil.color("  &f/ogui open shop PlayerName"));
            sender.sendMessage(ColorUtil.color("  &f/ogui reload"));
            return true;
        }

        String subCommand = args[0].toLowerCase(Locale.ENGLISH);

        if (subCommand.equals("reload")) {
            if (!sender.hasPermission("ogui.reload")) {
                sender.sendMessage(ColorUtil.color("&cYou do not have permission to reload GUIs."));
                return true;
            }

            plugin.reloadGuis();
            sender.sendMessage(ColorUtil.color("&a✔ OGUI menus reloaded successfully!"));
            sender.sendMessage(ColorUtil.color("&7Loaded &f" + plugin.getGuiRegistry().getGuiIds().size() + " &7GUI(s)"));
            return true;
        }

        if (subCommand.equals("open")) {
            if (!sender.hasPermission("ogui.open")) {
                sender.sendMessage(ColorUtil.color("&cYou do not have permission to open GUIs."));
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(ColorUtil.color("&cUsage: /ogui open <id> [player]"));
                sender.sendMessage(ColorUtil.color("&7Available GUIs: &f" + String.join(", ", plugin.getGuiRegistry().getGuiIds())));
                return true;
            }

            String id = args[1];
            GuiDefinition definition = plugin.getGuiRegistry().getGui(id);

            if (definition == null) {
                sender.sendMessage(ColorUtil.color("&cUnknown GUI id: &f" + id));
                sender.sendMessage(ColorUtil.color("&7Available GUIs: &f" + String.join(", ", plugin.getGuiRegistry().getGuiIds())));
                return true;
            }

            Player target;
            if (args.length >= 3) {
                target = Bukkit.getPlayerExact(args[2]);
                if (target == null) {
                    sender.sendMessage(ColorUtil.color("&cPlayer not found: &f" + args[2]));
                    return true;
                }
            } else if (sender instanceof Player) {
                target = (Player) sender;
            } else {
                sender.sendMessage(ColorUtil.color("&cYou must specify a player from console."));
                return true;
            }

            definition.createInventory(plugin.getInventoryManager(), plugin).open(target);

            if (!target.equals(sender)) {
                sender.sendMessage(ColorUtil.color("&aOpened GUI &f" + id + " &afor &f" + target.getName()));
            }

            return true;
        }

        sender.sendMessage(ColorUtil.color("&cUnknown subcommand: &f" + subCommand));
        sender.sendMessage(ColorUtil.color("&7Use &f/ogui &7for help"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            if (sender.hasPermission("ogui.open")) {
                options.add("open");
            }
            if (sender.hasPermission("ogui.reload")) {
                options.add("reload");
            }
            return filterOptions(options, args[0]);
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("open")) {
            if (!sender.hasPermission("ogui.open")) {
                return Collections.emptyList();
            }
            return filterOptions(new ArrayList<>(plugin.getGuiRegistry().getGuiIds()), args[1]);
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("open")) {
            if (!sender.hasPermission("ogui.open")) {
                return Collections.emptyList();
            }
            List<String> players = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                players.add(player.getName());
            }
            return filterOptions(players, args[2]);
        }

        return Collections.emptyList();
    }

    private List<String> filterOptions(List<String> options, String input) {
        if (input.isEmpty()) {
            return options;
        }

        List<String> filtered = new ArrayList<>();
        String lowerInput = input.toLowerCase(Locale.ENGLISH);

        for (String option : options) {
            if (option.toLowerCase(Locale.ENGLISH).startsWith(lowerInput)) {
                filtered.add(option);
            }
        }

        return filtered;
    }
}