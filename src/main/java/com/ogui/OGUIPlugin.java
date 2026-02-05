package com.ogui;

import com.ogui.command.OGUICommand;
import com.ogui.gui.GuiDefinition;
import com.ogui.gui.GuiRegistry;
import com.ogui.items.DefaultItemProvider;
import com.ogui.items.ItemProvider;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class OGUIPlugin extends JavaPlugin {

    private InventoryManager inventoryManager;
    private GuiRegistry guiRegistry;
    private ItemProvider itemProvider;
    private final List<String> registeredCommands = new ArrayList<>();

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        saveResource("guis.yml", false);

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        itemProvider = new DefaultItemProvider(this);
        getLogger().info("ItemProvider initialized with support for: Vanilla, ItemsAdder, Nexo");

        guiRegistry = new GuiRegistry(this);
        guiRegistry.reload();

        OGUICommand command = new OGUICommand(this);
        if (getCommand("ogui") != null) {
            getCommand("ogui").setExecutor(command);
            getCommand("ogui").setTabCompleter(command);
        }

        registerGuiCommands();

        getLogger().info("OGUI Enhanced enabled with support for:");
        getLogger().info("- OreoEssentials Custom Currencies");
        getLogger().info("- ItemsAdder Custom Items");
        getLogger().info("- Nexo Custom Items");
        getLogger().info("- WorldGuard Regions");
        getLogger().info("- PlaceholderAPI Conditions");
        getLogger().info("- Multiple Condition Types (XP, Items, Permissions, etc.)");
    }

    @Override
    public void onDisable() {
        inventoryManager = null;
        guiRegistry = null;
        itemProvider = null;
        registeredCommands.clear();
        getLogger().info("OGUI Enhanced disabled");
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public GuiRegistry getGuiRegistry() {
        return guiRegistry;
    }

    public ItemProvider getItemProvider() {
        return itemProvider;
    }

    private void registerGuiCommands() {
        try {
            Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(getServer());

            for (String guiId : guiRegistry.getGuiIds()) {
                GuiDefinition definition = guiRegistry.getGui(guiId);
                if (definition == null || definition.getCommands().isEmpty()) {
                    continue;
                }

                for (String cmdName : definition.getCommands()) {
                    Command cmd = new Command(cmdName) {
                        @Override
                        public boolean execute(CommandSender sender, String label, String[] args) {
                            if (!(sender instanceof Player)) {
                                sender.sendMessage("§cOnly players can open GUIs!");
                                return true;
                            }

                            Player player = (Player) sender;
                            definition.createInventory(inventoryManager, OGUIPlugin.this).open(player);
                            return true;
                        }
                    };

                    cmd.setDescription("Opens the " + definition.getTitle() + " menu");
                    cmd.setPermission("ogui.command." + guiId);

                    commandMap.register("ogui", cmd);
                    registeredCommands.add(cmdName);

                    getLogger().info("Registered command: /" + cmdName + " -> GUI: " + guiId);
                }
            }

        } catch (Exception e) {
            getLogger().severe("Failed to register GUI commands: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void reloadGuis() {
        guiRegistry.reload();

        registeredCommands.clear();
        registerGuiCommands();

        getLogger().info("GUIs reloaded successfully!");
    }
}