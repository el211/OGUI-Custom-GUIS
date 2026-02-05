package com.ogui.condition.impl;

import com.ogui.condition.Condition;
import com.ogui.condition.ConditionType;
import com.ogui.util.ColorUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultMoneyCondition implements Condition {
    private final double amount;
    private Economy economy;

    public VaultMoneyCondition(double amount) {
        this.amount = amount;
        setupEconomy();
    }

    private void setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) return;
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) economy = rsp.getProvider();
    }

    @Override
    public boolean check(Player player) {
        return economy != null && economy.has(player, amount);
    }

    @Override
    public boolean take(Player player) {
        if (economy == null || !check(player)) return false;
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    @Override
    public String getErrorMessage(Player player) {
        if (economy == null) return ColorUtil.color("&cEconomy system not available!");
        double balance = economy.getBalance(player);
        return ColorUtil.color("&cInsufficient money! Need: &f$" + String.format("%.2f", amount) +
                " &c(You have: &f$" + String.format("%.2f", balance) + "&c)");
    }

    @Override
    public ConditionType getType() {
        return ConditionType.VAULT_MONEY;
    }
}