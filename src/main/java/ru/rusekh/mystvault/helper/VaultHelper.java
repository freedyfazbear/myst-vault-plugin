package ru.rusekh.mystvault.helper;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class VaultHelper {
    public static final Economy ECONOMY = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();

    private VaultHelper() {
    }

    public static double getMoney(Player player) {
        return ECONOMY.getBalance(player);
    }

    public static boolean canAfford(Player player, double cost) {
        return getMoney(player) >= cost;
    }

    /**
     * Zwraca true jesli gracza stac na zakup, oraz zabiera z jego konta te pieniadze
     * Zwraca false i nie robi nic jesli gracza nie stac
     */
    public static boolean purchase(Player player, double cost) {
        if (!canAfford(player, cost)) {
            return false;
        }

        ECONOMY.withdrawPlayer(player, cost);
        return true;
    }

    public static void sell(Player player, double cost) {
        ECONOMY.depositPlayer(player, cost);
    }
}