package com.ogui;

import com.ogui.command.OGUICommand;
import com.ogui.gui.GuiDefinition;
import com.ogui.gui.GuiRegistry;
import com.ogui.items.DefaultItemProvider;
import com.ogui.items.ItemProvider;
import com.ogui.listener.NPCInteractListener;
import com.ogui.util.MessageManager;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import fr.minuskube.inv.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;


public class OGUIPlugin extends JavaPlugin implements Listener {

    private InventoryManager inventoryManager;
    private GuiRegistry guiRegistry;
    private ItemProvider itemProvider;
    private MessageManager messageManager;
    private final List<String> registeredCommands = new ArrayList<>();

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }


        int pluginId = 29355;
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(new SingleLineChart("guis_loaded", () ->
                getGuiRegistry() != null ? getGuiRegistry().getGuiIds().size() : 0));

        metrics.addCustomChart(new SimplePie("hook_oreoessentials", () ->
                (getItemProvider() instanceof com.ogui.items.DefaultItemProvider dip && dip.hasOreoEconomy())
                        ? "yes" : "no"));

        saveResource("guis.yml", false);
        saveResource("lang.yml", false);

        messageManager = new MessageManager(this);

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        itemProvider = new DefaultItemProvider(this);
        getLogger().info(messageManager.getMessage("loading.item_provider"));

        guiRegistry = new GuiRegistry(this);
        guiRegistry.reload();

        OGUICommand command = new OGUICommand(this);
        if (getCommand("ogui") != null) {
            getCommand("ogui").setExecutor(command);
            getCommand("ogui").setTabCompleter(command);
        }

        registerGuiCommands();
        registerNPCListener();

        getLogger().info(messageManager.getMessage("loading.enabled"));
        getLogger().info(messageManager.getMessage("loading.oreo_currencies"));
        getLogger().info(messageManager.getMessage("loading.oreo_warps"));
        getLogger().info(messageManager.getMessage("loading.itemsadder"));
        getLogger().info(messageManager.getMessage("loading.nexo"));
        getLogger().info(messageManager.getMessage("loading.worldguard"));
        getLogger().info(messageManager.getMessage("loading.placeholderapi"));
        getLogger().info(messageManager.getMessage("loading.weather_world"));

        if (Bukkit.getPluginManager().getPlugin("ModeledNPCs") != null) {
            getLogger().info(messageManager.getMessage("loading.modelednpcs"));
        }
        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().runTask(this, () -> {
            hookOreoEssentialsIfPresent();
        });

    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent e) {
        if (e.getPlugin().getName().equalsIgnoreCase("OreoEssentials")) {
            hookOreoEssentialsIfPresent();
        }
    }

    private boolean oreoHooked = false;

    private void hookOreoEssentialsIfPresent() {
        if (oreoHooked) return;

        if (Bukkit.getPluginManager().getPlugin("OreoEssentials") == null) {
            return;
        }

        oreoHooked = true;
        getLogger().info("Detected OreoEssentials. Enabling Oreo hooks...");

        if (itemProvider instanceof DefaultItemProvider) {
            ((DefaultItemProvider) itemProvider).reloadHooks();
        }


        guiRegistry.reload();
        unregisterGuiCommands();
        registeredCommands.clear();
        registerGuiCommands();
    }


    @Override
    public void onDisable() {
        inventoryManager = null;
        guiRegistry = null;
        itemProvider = null;
        messageManager = null;
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

    public MessageManager getMessageManager() {
        return messageManager;
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
                                messageManager.send(sender, "general.player_only");
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

                    Map<String, String> replacements = new HashMap<>();
                    replacements.put("command", cmdName);
                    replacements.put("gui", guiId);
                    getLogger().info(messageManager.getMessage("loading.command_registered", replacements));
                }
            }

        } catch (Exception e) {
            Map<String, String> replacements = new HashMap<>();
            replacements.put("error", e.getMessage());
            getLogger().severe(messageManager.getMessage("errors.command_register_failed", replacements));
            e.printStackTrace();
        }
    }

    private void registerNPCListener() {
        if (Bukkit.getPluginManager().getPlugin("ModeledNPCs") == null) {
            return;
        }

        try {
            getServer().getPluginManager().registerEvents(new NPCInteractListener(this), this);

            int npcBoundGuis = 0;
            for (String guiId : guiRegistry.getGuiIds()) {
                GuiDefinition definition = guiRegistry.getGui(guiId);
                if (definition != null && definition.hasNpcBinding()) {
                    npcBoundGuis++;
                }
            }

            if (npcBoundGuis > 0) {
                Map<String, String> replacements = new HashMap<>();
                replacements.put("count", String.valueOf(npcBoundGuis));
                getLogger().info(messageManager.getMessage("loading.npc_listener", replacements));
            }
        } catch (Exception e) {
            Map<String, String> replacements = new HashMap<>();
            replacements.put("error", e.getMessage());
            getLogger().warning(messageManager.getMessage("errors.npc_listener_register_failed", replacements));
        }
    }

    private void unregisterGuiCommands() {
        try {
            Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(getServer());

            Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);

            for (String cmdName : registeredCommands) {
                knownCommands.remove(cmdName.toLowerCase());
                knownCommands.remove(("ogui:" + cmdName).toLowerCase());

                knownCommands.entrySet().removeIf(e ->
                        e.getKey() != null &&
                                (e.getKey().equalsIgnoreCase(cmdName) || e.getKey().equalsIgnoreCase("ogui:" + cmdName)));
            }

        } catch (Exception e) {
            getLogger().warning("Failed to unregister GUI commands: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void reloadGuis() {
        messageManager.reload();

        if (itemProvider instanceof DefaultItemProvider) {
            ((DefaultItemProvider) itemProvider).reloadHooks();
        }

        unregisterGuiCommands();
        registeredCommands.clear();

        guiRegistry.reload();

        registerGuiCommands();

        Map<String, String> replacements = new HashMap<>();
        replacements.put("count", String.valueOf(guiRegistry.getGuiIds().size()));
        getLogger().info(messageManager.getMessage("general.reload.success"));
        getLogger().info(messageManager.getMessage("general.reload.gui_count", replacements));
    }


}