package ru.rusekh.mystvault.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.rusekh.mystvault.MystVaultPlugin;
import ru.rusekh.mystvault.helper.ChatHelper;

public class RemoveVaultComand implements CommandExecutor
{
    private final MystVaultPlugin vaultPlugin;

    public RemoveVaultComand(MystVaultPlugin vaultPlugin) {
        this.vaultPlugin = vaultPlugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player = (Player) sender;

        ItemStack itemStack = player.getInventory().getItem(EquipmentSlot.HAND);

        if (itemStack.getType() == Material.AIR) {
            ChatHelper.sendMessage(player, "&cYou cant block AIR");
            return false;
        }

        var serverData = vaultPlugin.getServerDataManager().getServerData("data");
        serverData.getDisallowedItems().remove(itemStack);
        serverData.save();

        ChatHelper.sendMessage(player, "&aSucessfully allowed " + itemStack.getType().name());

        return false;
    }
}
