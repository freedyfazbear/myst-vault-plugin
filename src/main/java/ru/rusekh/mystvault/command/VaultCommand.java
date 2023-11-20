package ru.rusekh.mystvault.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.rusekh.mystvault.MystVaultPlugin;
import ru.rusekh.mystvault.inventory.MystVaultInventory;

public class VaultCommand implements CommandExecutor
{
    private final MystVaultPlugin vaultPlugin;

    public VaultCommand(MystVaultPlugin vaultPlugin) {
        this.vaultPlugin = vaultPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;
        var userData = vaultPlugin.getUserManager().getUser(player.getUniqueId());

        new MystVaultInventory(vaultPlugin).openGui(player);

        return false;
    }
}
