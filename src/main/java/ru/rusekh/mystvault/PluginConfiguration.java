package ru.rusekh.mystvault;

import eu.okaeri.configs.OkaeriConfig;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import ru.rusekh.mystvault.helper.ChatHelper;

public class PluginConfiguration extends OkaeriConfig
{
    public ItemStack vaultItem = ItemBuilder.from(Material.CHEST)
            .setName(ChatHelper.parse("&dVault"))
            .build();
    public double vaultPrice = 5.5;
    public String itemsLoreName = "&c&lX Season Transfer Pass";
    public String disallowedItems = "[]";
}
