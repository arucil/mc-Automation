package plodsoft.automation.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import plodsoft.automation.items.ItemUpgradeFilter;

import javax.annotation.Nullable;

public class ContainerFilter extends Container {
   private EntityPlayer player;
   private ItemStackHandler handler;
   private ItemStack stack;

   public ContainerFilter(EntityPlayer player) {
      this.player = player;
      stack = player.getHeldItem(EnumHand.MAIN_HAND);
      handler = ItemUpgradeFilter.getInventory(stack);

      addOwnSlots(handler);
      addPlayerSlots(player.inventory, player.inventory.currentItem);
   }

   @Override
   public void onContainerClosed(EntityPlayer playerIn) {
      super.onContainerClosed(playerIn);
      ItemUpgradeFilter.setInventory(stack, handler);
   }

   @Override
   public boolean canInteractWith(EntityPlayer playerIn) {
      return playerIn == player;
   }

   void addPlayerSlots(IInventory playerInventory, int slot) {
      // Slots for the hotbar
      for (int i1 = 0; i1 < 9; ++i1) {
         addSlotToContainer(slot == i1 ? new SlotReadOnly(playerInventory, i1, 8 + i1 * 18, 109)
               : new Slot(playerInventory, i1, 8 + i1 * 18, 109));
      }

      // Slots for the main inventory
      for (int l = 0; l < 3; ++l) {
         for (int j1 = 0; j1 < 9; ++j1) {
            addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 51 + l * 18));
         }
      }
   }

   private void addOwnSlots(ItemStackHandler handler) {
      for (int i = 0; i < ItemUpgradeFilter.SIZE; ++i) {
         addSlotToContainer(new SlotItemHandler(handler, i, 44 + i * 18, 20));
      }
   }

   @Nullable
   @Override
   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
      ItemStack itemstack = null;
      Slot slot = inventorySlots.get(index);

      if (slot != null && slot.getHasStack() && slot.canTakeStack(null)) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();

         if (index < ItemUpgradeFilter.SIZE) {
            if (!mergeItemStack(itemstack1, ItemUpgradeFilter.SIZE, inventorySlots.size(), true)) {
               return null;
            }
         } else if (!mergeItemStack(itemstack1, 0, ItemUpgradeFilter.SIZE, false)) {
            return null;
         }

         if (itemstack1.stackSize == 0) {
            slot.putStack(null);
         } else {
            slot.onSlotChanged();
         }
      }

      return itemstack;
   }

   public static class SlotReadOnly extends Slot {
      public SlotReadOnly(IInventory inventoryIn, int index, int xPosition, int yPosition) {
         super(inventoryIn, index, xPosition, yPosition);
      }

      @Override
      public boolean canTakeStack(EntityPlayer playerIn) {
         return false;
      }
   }
}
