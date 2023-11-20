package ru.rusekh.mystvault.database;

import com.mongodb.MongoException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.rusekh.mystvault.MystVaultPlugin;
import ru.rusekh.mystvault.helper.ItemHelper;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class UserData
{
    private UUID playerUUID;
    private String playerName;
    private ItemStack[] vaultContent;
    private int vaultSlots;

    public UserData(UUID playerUUID, String playerName) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.vaultContent = new ItemStack[7];
        this.vaultSlots = 7;
    }

    public UserData(Document document) {
        try {
            playerUUID = UUID.fromString(document.getString("playerUUID"));
            playerName = document.getString("playerName");
            vaultContent = ItemHelper.itemStackArrayFromBase64(document.getString("vaultContent"));
            vaultSlots = document.getInteger("vaultSlots");
        } catch (MongoException exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Document document = new Document();
                document.append("playerUUID", playerUUID.toString());
                document.append("playerName", playerName);
                document.append("vaultContent", ItemHelper.itemStackArrayToBase64(vaultContent));
                document.append("vaultSlots", vaultSlots);
                MystVaultPlugin.getInstance().getVaultCollection().replaceOne(Filters.eq("playerUUID", playerUUID.toString()), document, new ReplaceOptions().upsert(true));
            }
        }.runTaskLaterAsynchronously(MystVaultPlugin.getInstance(), 5L);
    }

    public void saveAtShutdown() {
        CompletableFuture.runAsync(() -> {
            Document document = new Document();
            document.append("playerUUID", playerUUID.toString());
            document.append("playerName", playerName);
            document.append("vaultContent", ItemHelper.itemStackArrayToBase64(vaultContent));
            document.append("vaultSlots", vaultSlots);
            MystVaultPlugin.getInstance().getVaultCollection().replaceOne(Filters.eq("playerUUID", playerUUID.toString()), document, new ReplaceOptions().upsert(true));
        });
    }

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public int getVaultSlots() {
        return vaultSlots;
    }

    public void setVaultSlots(int vaultSlots) {
        this.vaultSlots = vaultSlots;
    }

    public void setVaultContent(ItemStack[] vaultContent) {
        this.vaultContent = vaultContent;
    }

    public ItemStack[] getVaultContent() {
        return vaultContent;
    }
}
