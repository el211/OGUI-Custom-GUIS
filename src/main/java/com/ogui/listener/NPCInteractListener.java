package com.ogui.listener;

import com.ogui.OGUIPlugin;
import com.ogui.gui.GuiDefinition;
import fr.elias.npcs.events.NPCInteractEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

public class NPCInteractListener implements Listener {

    private final OGUIPlugin plugin;

    public NPCInteractListener(OGUIPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onNPCInteract(NPCInteractEvent event) {
        Player player = event.getPlayer();
        int npcId = event.getNPCData().getId();
        NPCInteractEvent.InteractionType interactionType = event.getInteractionType();

        // Example: Only open GUI on right-click
        if (interactionType != NPCInteractEvent.InteractionType.RIGHT_CLICK) {
            return;
        }
        // O(1) lookup instead of O(n) iteration
        String guiId = plugin.getGuiRegistry().getGuiByNpc(npcId);
        if (guiId == null) {
            return; // No GUI bound to this NPC
        }

        GuiDefinition definition = plugin.getGuiRegistry().getGui(guiId);
        if (definition == null) {
            plugin.getLogger().warning("GUI '" + guiId + "' is bound to NPC " + npcId + " but doesn't exist!");
            return;
        }

        // Cancel the event to prevent default NPC behavior
        event.setCancelled(true);

        try {
            definition.createInventory(plugin.getInventoryManager(), plugin).open(player);

            // Success message to player
            Map<String, String> replacements = new HashMap<>();
            replacements.put("gui", guiId);
            replacements.put("id", String.valueOf(npcId));
            plugin.getMessageManager().send(player, "npc.gui_opened", replacements);

            // Console logging
            Map<String, String> consoleReplacements = new HashMap<>();
            consoleReplacements.put("player", player.getName());
            consoleReplacements.put("gui", guiId);
            consoleReplacements.put("id", String.valueOf(npcId));
            plugin.getLogger().info(
                    plugin.getMessageManager().getMessage("npc.interaction_logged", consoleReplacements)
            );

        } catch (Exception e) {
            // Notify player about the error
            Map<String, String> errorReplacements = new HashMap<>();
            errorReplacements.put("gui", guiId);
            plugin.getMessageManager().send(player, "npc.error_opening_gui", errorReplacements);

            // Console error logging
            plugin.getLogger().severe("Failed to open GUI '" + guiId + "' for NPC " + npcId + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}