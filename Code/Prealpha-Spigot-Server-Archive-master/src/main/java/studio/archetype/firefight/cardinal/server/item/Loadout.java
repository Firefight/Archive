package studio.archetype.firefight.cardinal.server.item;

import studio.archetype.firefight.cardinal.server.data.Weapon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Loadout {
    public static Loadout testLoadout = new Loadout(1);
    static {
        testLoadout.setSlot(
                0,
                Weapon.findById(UUID.fromString("fbc402a4-a487-4be1-8d56-35a228c415ae"))
        );
    }

    private final int maxItems;
    private final ArrayList<Slot> slots;

    public Loadout(int maxItems) {
        this.maxItems = maxItems;
        slots = new ArrayList<>(maxItems);
        for (int i = 0; i < maxItems; i++) slots.add(new Slot());
    }

    public Weapon clearSlot(int slot) {
        if (slot + 1 > maxItems) throw new IllegalArgumentException("Slot greater than max slot capacity (" + maxItems + ")");
        else {
            Weapon weapon = slots.get(slot).getWeapon();
            slots.get(slot).setWeapon(null);
            return weapon;
        }
    }

    public void setSlot(int slot, Weapon weapon) {
        if (slot + 1 > maxItems) throw new IllegalArgumentException("Slot greater than max slot capacity (" + maxItems + ")");
        else slots.get(slot).setWeapon(weapon);
    }

    public void apply(Player player) {
        player.getInventory().clear();
        slots.forEach((slot) -> slot.getWeapon().giveWeapon(player));
    }

    @NoArgsConstructor
    @AllArgsConstructor
    public static class Slot {
        @Getter @Setter
        private Weapon weapon = null;
    }
}
