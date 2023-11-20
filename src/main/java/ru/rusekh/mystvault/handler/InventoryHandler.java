package ru.rusekh.mystvault.handler;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import ru.rusekh.mystvault.MystVaultPlugin;
import ru.rusekh.mystvault.helper.ChatHelper;
import ru.rusekh.mystvault.helper.ItemHelper;

public class InventoryHandler implements Listener
{
    private final MystVaultPlugin vaultPlugin;

    public InventoryHandler(MystVaultPlugin vaultPlugin) {
        this.vaultPlugin = vaultPlugin;
    }

    @EventHandler
    private void onPlayerDrag(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null) return;

        if (event.getView().getTitle().equals("Your vault")) {
            var serverData = vaultPlugin.getServerDataManager().getServerData("data");

            for (ItemStack disallowedItem : serverData.getDisallowedItems()) {
                if (itemStack.isSimilar(disallowedItem)) {
                    ChatHelper.sendMessage(event.getWhoClicked(), "&cThis item is not allowed");
                    event.setResult(Event.Result.DENY);
                }
            }
        }

    }

    @EventHandler
    private void onPlayerInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (event.getView().getTitle().equals("Your vault")) {
            var userData = vaultPlugin.getUserManager().getUser(player.getUniqueId());

            userData.setVaultContent(event.getInventory().getContents());

            ChatHelper.sendMessage(player, "&aYour vault has been saved");
        }
    }
}
