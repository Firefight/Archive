package studio.archetype.firefight.cardinal.server.match.state;

import studio.archetype.firefight.cardinal.server.data.Weapon;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
@Setter
public class PlayerState {
    private boolean scoping = false;
    private boolean inSafety = false;
    private Weapon heldWeapon = null;
    private Player player;
    private int health = 100;

    public PlayerState(Player player) {
        this.player = player;
    }

    public boolean damagePlayer(int amount) {
        if (health - amount <= 0) { // if this amount kills the player
            health = 0;
            player.setHealth(0); // kill the player, todo: allow players to spectate
            return true;
        } else {
            health -= amount;
            return false;
        }
    }

    public void updateModel(boolean firing) {
        int modelData;

        if (firing) {
            if (scoping) modelData = heldWeapon.getModelDescriptors().getCenteredFiring();
            else modelData = heldWeapon.getModelDescriptors().getFiring();
        } else {
            if (scoping) modelData = heldWeapon.getModelDescriptors().getCentered();
            else modelData = heldWeapon.getModelDescriptors().getNormal();
        }

        // Overwrite other states if in safety
        if (inSafety) modelData = heldWeapon.getModelDescriptors().getSafety();

        ItemMeta weaponMeta = player.getInventory().getItemInMainHand().getItemMeta();
        weaponMeta.setCustomModelData(modelData);
        player.getInventory().getItemInMainHand().setItemMeta(weaponMeta);
    }

    public void updateModel() {
        updateModel(false);
    }
}
