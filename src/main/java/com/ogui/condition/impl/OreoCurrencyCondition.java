package com.ogui.condition.impl;

import com.ogui.OGUIPlugin;
import com.ogui.condition.Condition;
import com.ogui.condition.ConditionType;
import com.ogui.util.ColorUtil;
import fr.elias.oreoEssentials.OreoEssentials;
import fr.elias.oreoEssentials.modules.currency.Currency;
import fr.elias.oreoEssentials.modules.currency.CurrencyService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class OreoCurrencyCondition implements Condition {
    private final OGUIPlugin plugin;
    private final String currencyId;
    private final double amount;

    public OreoCurrencyCondition(OGUIPlugin plugin, String currencyId, double amount) {
        this.plugin = plugin;
        this.currencyId = currencyId;
        this.amount = amount;
    }

    @Override
    public boolean check(Player player) {
        CurrencyService service = getCurrencyService();
        if (service == null) return false;
        try {
            double balance = service.getBalance(player.getUniqueId(), currencyId).get();
            return balance >= amount;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean take(Player player) {
        CurrencyService service = getCurrencyService();
        if (service == null) return false;
        try {
            return service.withdraw(player.getUniqueId(), currencyId, amount).get();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getErrorMessage(Player player) {
        CurrencyService service = getCurrencyService();
        if (service == null) return ColorUtil.color("&cOreoEssentials not available!");
        Currency currency = service.getCurrency(currencyId);
        if (currency == null) return ColorUtil.color("&cCurrency not found: " + currencyId);
        try {
            double balance = service.getBalance(player.getUniqueId(), currencyId).get();
            return ColorUtil.color("&cInsufficient " + currency.getName() + "! Need: &f" +
                    currency.format(amount) + " &c(You have: &f" + currency.format(balance) + "&c)");
        } catch (Exception e) {
            return ColorUtil.color("&cFailed to check balance!");
        }
    }

    @Override
    public ConditionType getType() {
        return ConditionType.OREO_CURRENCY;
    }

    private CurrencyService getCurrencyService() {
        try {
            OreoEssentials oreo = (OreoEssentials) Bukkit.getPluginManager().getPlugin("OreoEssentials");
            return oreo != null && oreo.isEnabled() ? oreo.getCurrencyService() : null;
        } catch (Exception e) {
            return null;
        }
    }
}