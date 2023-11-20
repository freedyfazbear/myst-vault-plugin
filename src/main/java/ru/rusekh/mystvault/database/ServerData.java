package ru.rusekh.mystvault.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import ru.rusekh.mystvault.MystVaultPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServerData
{
    private List<ItemStack> disallowedItems;
    private Gson gson = new Gson();

    public ServerData() {
        this.disallowedItems = new ArrayList<>();
    }

    public ServerData(Document document) {
        try {
            disallowedItems = gson.fromJson(document.getString("disallowedItems"), new TypeToken<List<ItemStack>>() {
            }.getType());
        } catch (MongoException exception) {
            exception.printStackTrace();
        }
    }

    public void save() {
        Bukkit.getScheduler().runTaskLater(MystVaultPlugin.getInstance(), () -> {
            Document document = new Document();
            document.append("disallowedItems", gson.toJson(disallowedItems));
            MystVaultPlugin.getInstance().getServerDataCollection().replaceOne(Filters.eq("disallowedItems", gson.toJson(disallowedItems)), document, new ReplaceOptions().upsert(true));
        }, 5L);
    }

    public void saveAtShutdown() {
        CompletableFuture.runAsync(() -> {
            Document document = new Document();
            document.append("disallowedItems", gson.toJson(disallowedItems));
            MystVaultPlugin.getInstance().getServerDataCollection().replaceOne(Filters.eq("disallowedItems", gson.toJson(disallowedItems)), document, new ReplaceOptions().upsert(true));
        });
    }

    public List<ItemStack> getDisallowedItems() {
        return disallowedItems;
    }
}
