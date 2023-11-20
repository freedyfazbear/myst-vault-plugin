package ru.rusekh.mystvault.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rusekh.mystvault.MystVaultPlugin;
import ru.rusekh.mystvault.inventory.MystVaultShopInventory;

public class VaultShopCommand implements CommandExecutor
{
    private final MystVaultPlugin vaultPlugin;

    public VaultShopCommand(MystVaultPlugin vaultPlugin) {
        this.vaultPlugin = vaultPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        new MystVaultShopInventory(vaultPlugin).openGui(player);

        return false;
    }
}
