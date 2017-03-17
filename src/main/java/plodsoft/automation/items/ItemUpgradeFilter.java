package plodsoft.automation.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import plodsoft.automation.Automation;
import plodsoft.automation.gui.GuiHandler;

public class ItemUpgradeFilter extends ItemUpgrade {
   public static final String NAME = "upgradeFilter";
   public static final int SIZE = 5;
   public static final String NBT = "QUpgradeInv";

   public ItemUpgradeFilter() {
      super(NAME);
   }

   public static ItemStackHandler getInventory(ItemStack stack) {
      NBTTagCompound compound = stack.getTagCompound();
      ItemStackHandler handler = new ItemStackHandler(SIZE);
      if (compound == null || !compound.hasKey(NBT)) {
         stack.setTagCompound(handler.serializeNBT());
         return handler;
      }
      handler.deserializeNBT((NBTTagCompound) compound.getTag(NBT));
      return handler;
   }

   public static void setInventory(ItemStack stack, ItemStackHandler handler) {
      NBTTagCompound compound = stack.getTagCompound();
      if (null == compound)
         stack.setTagCompound(compound = new NBTTagCompound());
      compound.setTag(NBT, handler.serializeNBT());
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
      if (!worldIn.isRemote && hand == EnumHand.MAIN_HAND) {
         playerIn.openGui(Automation.instance, GuiHandler.GUI_FILTER_ID, worldIn, 0, 0, 0);
      }
      return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
   }

   public static class ItemComparer {
      public static final ItemComparer OBSIDIAN = new ItemComparer(Item.getItemFromBlock(Blocks.OBSIDIAN));

      private Item item;
      private int meta;

      public ItemComparer(ItemStack stack) {
         item = stack.getItem();
         meta = stack.getItemDamage();
      }

      public ItemComparer(Item item) {
         this.item = item;
      }

      @Override
      public boolean equals(Object o) {
         if (!(o instanceof ItemComparer))
            return false;
         ItemComparer c = (ItemComparer) o;
         return item == c.item && meta == c.meta;
      }

      @Override
      public int hashCode() {
         return item.hashCode() ^ meta;
      }
   }
}
