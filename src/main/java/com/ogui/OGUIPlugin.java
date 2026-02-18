package com.ogui;

import com.ogui.command.OGUICommand;
import com.ogui.gui.GuiDefinition;
import com.ogui.gui.GuiRegistry;
import com.ogui.items.DefaultItemProvider;
import com.ogui.items.ItemProvider;
import com.ogui.listener.NPCInteractListener;
import com.ogui.util.MessageManager;
import fr.minuskube.inv.InventoryManager;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.*;

public class OGUIPlugin extends JavaPlugin implements Listener {

    private InventoryManager inventoryManager;
    private GuiRegistry guiRegistry;
    private ItemProvider itemProvider;
    private MessageManager messageManager;

    private final List<String> registeredCommands = new ArrayList<>();
    private boolean oreoHooked = false;

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
                (getItemProvider() instanceof DefaultItemProvider dip && dip.hasOreoEconomy()) ? "yes" : "no"));

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
        getLogger().info("Registering GUI commands...");
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

        Bukkit.getScheduler().runTask(this, this::hookOreoEssentialsIfPresent);
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

    @EventHandler
    public void onPluginEnable(PluginEnableEvent e) {
        if (e.getPlugin().getName().equalsIgnoreCase("OreoEssentials")) {
            hookOreoEssentialsIfPresent();
        }
    }

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

    private void registerGuiCommands() {
        try {
            CommandMap commandMap = getCommandMap();
            Map<String, Command> knownCommands = getKnownCommands(commandMap);

            for (String guiId : guiRegistry.getGuiIds()) {
                GuiDefinition definition = guiRegistry.getGui(guiId);
                if (definition == null || definition.getCommands().isEmpty()) continue;

                List<String> normalized = normalizeCommands(definition.getCommands());
                if (normalized.isEmpty()) continue;

                String primary = normalized.get(0);
                List<String> aliases = normalized.size() > 1
                        ? new ArrayList<>(normalized.subList(1, normalized.size()))
                        : Collections.emptyList();

                Command cmd = new Command(primary) {
                    @Override
                    public boolean execute(CommandSender sender, String label, String[] args) {
                        if (!(sender instanceof Player)) {
                            messageManager.send(sender, "general.player_only");
                            return true;
                        }

                        Player player = (Player) sender;

                        if (!player.hasPermission("ogui.command." + guiId)) {
                            messageManager.send(player, "general.no_permission");
                            return true;
                        }

                        definition.createInventory(inventoryManager, OGUIPlugin.this).open(player);
                        return true;
                    }
                };

                cmd.setAliases(new ArrayList<>(aliases));
                cmd.setDescription("Opens the " + definition.getTitle() + " menu");
                cmd.setPermission("ogui.command." + guiId);

                // Remove any existing entries for these names before registering
                forceUnregisterNames(knownCommands, primary, aliases);

                // Mark the command as registered without going through register()'s
                // conflict-detection logic, which can silently drop plain-name entries
                cmd.register(commandMap);

                // Manually insert all names into knownCommands so Bukkit can route them
                knownCommands.put(primary.toLowerCase(Locale.ENGLISH), cmd);
                knownCommands.put(("ogui:" + primary).toLowerCase(Locale.ENGLISH), cmd);
                registeredCommands.add(primary);

                for (String a : aliases) {
                    knownCommands.put(a.toLowerCase(Locale.ENGLISH), cmd);
                    knownCommands.put(("ogui:" + a).toLowerCase(Locale.ENGLISH), cmd);
                    registeredCommands.add(a);
                }

                Map<String, String> replacements = new HashMap<>();
                replacements.put("command", primary + (aliases.isEmpty() ? "" : " (aliases: " + String.join(", ", aliases) + ")"));
                replacements.put("gui", guiId);
                getLogger().info(messageManager.getMessage("loading.command_registered", replacements));
            }

        } catch (Exception e) {
            Map<String, String> replacements = new HashMap<>();
            replacements.put("error", e.getMessage());
            getLogger().severe(messageManager.getMessage("errors.command_register_failed", replacements));
            e.printStackTrace();
        }
    }

    private void unregisterGuiCommands() {
        try {
            CommandMap commandMap = getCommandMap();
            Map<String, Command> knownCommands = getKnownCommands(commandMap);

            for (String cmdName : registeredCommands) {
                String key = cmdName.toLowerCase(Locale.ENGLISH);
                knownCommands.remove(key);
                knownCommands.remove(("ogui:" + key).toLowerCase(Locale.ENGLISH));

                knownCommands.entrySet().removeIf(e ->
                        e.getKey() != null &&
                                (e.getKey().equalsIgnoreCase(cmdName) ||
                                        e.getKey().equalsIgnoreCase("ogui:" + cmdName)));
            }

        } catch (Exception e) {
            getLogger().warning("Failed to unregister GUI commands: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private CommandMap getCommandMap() throws Exception {
        Class<?> clazz = getServer().getClass();
        while (clazz != null) {
            try {
                Field commandMapField = clazz.getDeclaredField("commandMap");
                commandMapField.setAccessible(true);
                return (CommandMap) commandMapField.get(getServer());
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new Exception("Could not find commandMap field in server class hierarchy");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Command> getKnownCommands(CommandMap commandMap) throws Exception {
        Class<?> clazz = commandMap.getClass();
        while (clazz != null) {
            try {
                Field knownCommandsField = clazz.getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                return (Map<String, Command>) knownCommandsField.get(commandMap);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new Exception("Could not find knownCommands field in CommandMap hierarchy");
    }

    private List<String> normalizeCommands(List<String> raw) {
        List<String> out = new ArrayList<>();
        for (String s : raw) {
            if (s == null) continue;
            String v = s.trim();
            if (v.isEmpty()) continue;
            if (v.startsWith("/")) v = v.substring(1);
            v = v.toLowerCase(Locale.ENGLISH);
            v = v.replaceAll("[^a-z0-9_:\\-]", "");
            if (!v.isEmpty()) out.add(v);
        }
        LinkedHashSet<String> set = new LinkedHashSet<>(out);
        return new ArrayList<>(set);
    }

    private void forceUnregisterNames(Map<String, Command> knownCommands, String primary, List<String> aliases) {
        List<String> all = new ArrayList<>();
        all.add(primary);
        all.addAll(aliases);

        for (String name : all) {
            String key = name.toLowerCase(Locale.ENGLISH);

            Command existing = knownCommands.get(key);
            if (existing != null) {
                getLogger().warning("Command '/" + name + "' was already registered by another plugin. Overriding it for OGUI GUI command.");
            }

            knownCommands.remove(key);
            knownCommands.remove(("ogui:" + key).toLowerCase(Locale.ENGLISH));

            knownCommands.entrySet().removeIf(e ->
                    e.getKey() != null &&
                            (e.getKey().equalsIgnoreCase(name) ||
                                    e.getKey().equalsIgnoreCase("ogui:" + name)));
        }
    }
}