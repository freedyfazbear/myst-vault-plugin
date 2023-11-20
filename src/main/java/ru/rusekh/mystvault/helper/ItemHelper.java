package ru.rusekh.mystvault.helper;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ItemHelper
{

    public static void addItemToPlayer(Player player, ItemStack itemStack) {
        addItemToPlayer(player, itemStack, player.getLocation());
    }

    public static void addItemToPlayer(Player player, ItemStack itemStack, Location location) {
        HashMap<Integer, ItemStack> map = player.getInventory().addItem(itemStack);
        if (!map.isEmpty()) {
            location.getWorld().dropItemNaturally(location, itemStack);
        }
    }

    public static void addItemToPlayer(Player player, Collection<ItemStack> items) {
        addItemToPlayer(player, items, player.getLocation());
    }

    public static void addItemToPlayer(Player player, Collection<ItemStack> items, Location location) {
        HashMap map = player.getInventory().addItem(items.toArray(new ItemStack[0]));
        if (!map.isEmpty()) {
            for (ItemStack itemStack : items) {
                location.getWorld().dropItemNaturally(location, itemStack);
            }
        }
    }


    public static int getAmount(Player player, ItemStack itemStack) {
        if (itemStack == null) {
            return 0;
        }
        PlayerInventory inventory = player.getInventory();
        int amount = 0;
        for (int i = 0; i < 41; ++i) {
            ItemStack slot = inventory.getItem(i);
            if (slot == null || !isSimilar(slot, itemStack)) continue;
            amount += slot.getAmount();
        }
        return amount;
    }

    public static void reduceItemAmount(Player player, ItemStack itemStack, int remove) {
        PlayerInventory inventory = player.getInventory();
        for (int i = 41; i >= 0; --i) {
            ItemStack slot = inventory.getItem(i);
            if (slot == null || !isSimilar(slot, itemStack)) continue;
            int amount = slot.getAmount() - remove;
            if (amount > 0) {
                slot.setAmount(amount);
                if (player.getInventory().getItemInOffHand().isSimilar(itemStack)) {
                    player.getInventory().getItemInOffHand().setAmount(amount);
                }
                break;
            }
            inventory.setItem(i, null);
            if ((remove -= slot.getAmount()) == 0) break;
        }
    }

    private static boolean isSimilar(ItemStack is, ItemStack is1) {
        return is.getType() == is1.getType() && is.getDurability() == is1.getDurability();
    }

    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);
            for (int i = 0; i < items.length; ++i) {
                dataOutput.writeObject(items[i]);
            }
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }
        catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack[] itemStackArrayFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; ++i) {
                items[i] = (ItemStack)dataInput.readObject();
            }
            dataInput.close();
            return items;
        }
        catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return new ItemStack[0];
    }





    public static String itemStackToString(ItemStack... items) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(items.length);

            for (ItemStack item : items)
                dataOutput.writeObject(item);

            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (Exception ignored) {
            return "";
        }
    }

    public static ItemStack[] itemStackFromString(String source) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(source));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < items.length; i++)
                items[i] = (ItemStack) dataInput.readObject();

            return items;
        } catch (Exception ignored) {
            return new ItemStack[0];
        }
    }



    public static List<ItemStack> refundFromString(String path) {
        ItemStack[] itemStacks = itemStackArrayFromBase64(path);
        return Arrays.stream(itemStacks).collect(Collectors.toList());
    }
}
