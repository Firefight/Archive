package studio.archetype.firefight.cardinal.server.data;

import com.mongodb.client.MongoCollection;
import lombok.NoArgsConstructor;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import studio.archetype.firefight.cardinal.server.rtx.ProjectileConfiguration;
import studio.archetype.firefight.cardinal.server.rtx.RayResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class Weapon {
    private UUID _id;
    private String key;
    private ModelDataDescriptors modelDescriptors;
    private int scopeZoom;
    private int baseDamage;
    private ProjectileConfiguration projectileConfiguration;

    public static final String COLLECTION = "weapons";

    public static MongoCollection<Weapon> getCollection() {
        return Database.getDatabase().getCollection(COLLECTION, Weapon.class);
    }

    public void save() {
        MongoCollection<Weapon> collection = getCollection();
        Bson idFilter = eq("_id", _id);
        Weapon document = collection.find(idFilter).first();
        if (document == null) collection.insertOne(this);
        else collection.replaceOne(idFilter, this);
    }

    @Nullable
    public static Weapon findById(UUID id) {
        return getCollection().find(eq("_id", id)).first();
    }

    public void giveWeapon(Player recipient) {
        Bukkit.getLogger().info("fuck " + baseDamage);
        ItemStack weapon = new ItemStack(Material.STICK);
        ItemMeta weaponMeta = weapon.getItemMeta();
        weaponMeta.setCustomModelData(modelDescriptors.getNormal());
        weapon.setItemMeta(weaponMeta);

        net.minecraft.world.item.ItemStack nmsStick = CraftItemStack.asNMSCopy(weapon);
        CompoundTag tag = nmsStick.getOrCreateTag();
        tag.putString("weaponId", _id.toString());
        tag.putFloat("Zoom", scopeZoom);
        nmsStick.setTag(tag);

        ItemStack finalItem = CraftItemStack.asBukkitCopy(nmsStick);

        // the following code is just to be sure, we can remove it later.
        if (recipient.getInventory().firstEmpty() > -1) recipient.getInventory().addItem(finalItem);
        else recipient.getWorld().dropItem(recipient.getLocation(), finalItem);
    }

    public void shootForPlayer(Player player, RayResponse consumer) {
        projectileConfiguration.shoot(player.getEyeLocation(), consumer);
    }

    public static Weapon held(Player player) {
        ItemStack handItem = player.getInventory().getItemInMainHand();
        net.minecraft.world.item.ItemStack nmsHandItem = CraftItemStack.asNMSCopy(handItem);

        if (!nmsHandItem.hasTag()) return null;
        CompoundTag tag = nmsHandItem.getOrCreateTag();

        if (tag.contains("weaponId")) return Weapon.findById(UUID.fromString(tag.getString("weaponId")));
        else return null;
    }
}
