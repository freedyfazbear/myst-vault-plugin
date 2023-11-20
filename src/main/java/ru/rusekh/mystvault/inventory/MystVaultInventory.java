package ru.rusekh.mystvault.inventory;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import ru.rusekh.mystvault.MystVaultPlugin;
import ru.rusekh.mystvault.database.UserData;
import ru.rusekh.mystvault.helper.ChatHelper;

public class MystVaultInventory
{
    private final MystVaultPlugin vaultPlugin;

    public MystVaultInventory(MystVaultPlugin vaultPlugin) {
        this.vaultPlugin = vaultPlugin;
    }

    public void openGui(Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(ChatHelper.parseComponent("Your vault"))
                .rows(getRowsBySlots(vaultPlugin.getUserManager().getUser(player.getUniqueId())))
                .create();
        var userData = vaultPlugin.getUserManager().getUser(player.getUniqueId());

        for (ItemStack itemStack : userData.getVaultContent()) {
            if (itemStack == null) continue;
            if (itemStack.getType() == Material.AIR) continue;

            gui.addItem(ItemBuilder.from(itemStack)
                            .setLore(ChatHelper.parse(vaultPlugin.getConfiguration().itemsLoreName))
                    .asGuiItem());
        }

        for (int i = 0; i < 54; i++) {
            for (int j = 0; j < userData.getVaultSlots(); j++) {
                if (j != i) {
                    gui.setItem(i, ItemBuilder.from(Material.BARRIER)
                            .setName(ChatHelper.parse("&cYou need to buy more slots!"))
                            .asGuiItem(event -> event.setResult(Event.Result.DENY)));
                }
            }
        }

        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName("&cPrevious").asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName("&cNext").asGuiItem(event -> gui.next()));

        gui.open(player);
    }

    public int getRowsBySlots(UserData userData) {
        if (userData.getVaultSlots() <= 9) return 1;
        if (userData.getVaultSlots() <= 18) return 2;
        if (userData.getVaultSlots() <= 27) return 3;
        if (userData.getVaultSlots() <= 36) return 4;
        if (userData.getVaultSlots() <= 45) return 5;
        if (userData.getVaultSlots() <= 54) return 6;
        return 1;
    }
}
