package ru.rusekh.mystvault.inventory;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import ru.rusekh.mystvault.MystVaultPlugin;
import ru.rusekh.mystvault.helper.ChatHelper;
import ru.rusekh.mystvault.helper.VaultHelper;

import java.util.Arrays;

public class MystVaultShopInventory
{
    private final MystVaultPlugin vaultPlugin;

    public MystVaultShopInventory(MystVaultPlugin vaultPlugin) {
        this.vaultPlugin = vaultPlugin;
    }

    public void openGui(Player player) {
        Gui gui = Gui.gui()
                .title(ChatHelper.parseComponent("Vault Shop"))
                .type(GuiType.HOPPER)
                .disableAllInteractions()
                .create();
        var userData = vaultPlugin.getUserManager().getUser(player.getUniqueId());

        GuiItem buyVault = ItemBuilder.from(vaultPlugin.getConfiguration().vaultItem)
                .setLore(ChatHelper.parse(Arrays.asList("", "&8>> &7Purchase for &c" + vaultPlugin.getConfiguration().vaultPrice + "$")))
                .asGuiItem(event -> {
                    if (VaultHelper.purchase(player, vaultPlugin.getConfiguration().vaultPrice)) {
                        userData.setVaultSlots(userData.getVaultSlots() + 1);

                        ChatHelper.sendTitle(player, "", "&aSuccessfully bought a vault");

                        player.closeInventory();
                    }
                });
        gui.setItem(2, buyVault);
        gui.getFiller().fill(ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).asGuiItem());

        gui.open(player);
    }
}
