package ru.rusekh.mystvault.inventory;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.rusekh.mystvault.MystVaultPlugin;
import ru.rusekh.mystvault.database.UserData;
import ru.rusekh.mystvault.helper.ChatHelper;

import java.util.Collections;

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

        for (ItemStack itemStack : gui.getInventory().getContents()) {
            if (itemStack == null) continue;
            if (itemStack.getType() == Material.AIR) continue;

            ItemMeta meta = itemStack.getItemMeta();
            meta.setLore(Collections.singletonList(ChatHelper.parse(vaultPlugin.getConfiguration().itemsLoreName)));
            itemStack.setItemMeta(meta);

            for (int i = 0; i < userData.getVaultSlots(); i++) {
                gui.setItem(i, ItemBuilder.from(itemStack).asGuiItem());
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
