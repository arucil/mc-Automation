package plodsoft.automation.tileentities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityCbsInf extends TileEntity {
   public IItemHandler handler = new ItemHandler();

   @Override
   public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
      if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
         return true;
      }
      return super.hasCapability(capability, facing);
   }

   @Override
   public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
      if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
         return (T) handler;
      }
      return super.getCapability(capability, facing);
   }

   private static class ItemHandler implements IItemHandler {
      @Override
      public int getSlots() {
         return 1;
      }

      @Override
      public ItemStack getStackInSlot(int slot) {
         return new ItemStack(Blocks.COBBLESTONE, 64);
      }

      @Override
      public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
         return stack;
      }

      @Override
      public ItemStack extractItem(int slot, int amount, boolean simulate) {
         return new ItemStack(Blocks.COBBLESTONE, Math.min(amount, 64));
      }
   }

   public boolean canInteractWith(EntityPlayer playerIn) {
      return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
   }
}
