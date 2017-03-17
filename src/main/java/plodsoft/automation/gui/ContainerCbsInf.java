package plodsoft.automation.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import plodsoft.automation.tileentities.TileEntityCbsInf;

import javax.annotation.Nullable;

public class ContainerCbsInf extends Container {
   private TileEntityCbsInf te;

   public ContainerCbsInf(IInventory inv, TileEntityCbsInf te) {
      this.te = te;
      addOwnSlots(te.handler);
      addPlayerSlots(inv);
   }

   @Override
   public boolean canInteractWith(EntityPlayer playerIn) {
      return te.canInteractWith(playerIn);
   }

   void addPlayerSlots(IInventory playerInventory) {
      // Slots for the hotbar
      for (int i1 = 0; i1 < 9; ++i1) {
         addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 160 - 18));
      }

      // Slots for the main inventory
      for (int l = 0; l < 3; ++l) {
         for (int j1 = 0; j1 < 9; ++j1) {
            addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 102 - 18 + l * 18));
         }
      }
   }

   private void addOwnSlots(IItemHandler handler) {
      addSlotToContainer(new SlotInfinite(handler, 0, 80, 35));
   }

   @Nullable
   @Override
   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
      if (0 != index)
         return null;
      mergeItemStack(new ItemStack(Blocks.COBBLESTONE, 64), 1, inventorySlots.size(), true);
      return null;
   }

   public static class SlotInfinite extends SlotItemHandler {
      public SlotInfinite(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
         super(itemHandler, index, xPosition, yPosition);
      }

      @Override
      public void putStack(ItemStack stack) {
      }
   }
}
