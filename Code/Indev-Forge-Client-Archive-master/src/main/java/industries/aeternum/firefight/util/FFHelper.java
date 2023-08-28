package industries.aeternum.firefight.util;

import industries.aeternum.firefight.FFConstants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.Constants;

public class FFHelper implements FFConstants
{
    public static boolean isHoldingGun(EntityPlayer player)
    {
        if (player.isSpectator())
        {
            return false;
        }

        ItemStack stack = player.getHeldItemMainhand();
        return !stack.isEmpty() && stack.getItem() == Items.SHIELD && stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG_WEAPON);
    }

    public static int getFireRate(ItemStack stack)
    {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG_FIRERATE, Constants.NBT.TAG_ANY_NUMERIC) ? stack.getTagCompound().getInteger(TAG_FIRERATE) : 4;
    }

    public static boolean isZoomed(EntityPlayer player)
    {
        return isHoldingGun(player) && player.isHandActive();
    }
}
