package plodsoft.automation.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import plodsoft.automation.items.ItemUpgrade;
import plodsoft.automation.tileentities.TileEntityQuarry;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ContainerQuarry extends Container {
   private TileEntityQuarry te;
   private int burnTime, totalBurnTime;

   public ContainerQuarry(IInventory playerInventory, TileEntityQuarry te) {
      this.te = te;

      addOwnSlots();
      addPlayerSlots(playerInventory);
   }

   @Override
   public void addListener(IContainerListener listener) {
      super.addListener(listener);
      listener.sendProgressBarUpdate(this, 0, burnTime = te.burnTime);
      listener.sendProgressBarUpdate(this, 1, totalBurnTime = te.totalBurnTime);
   }

   @Override
   public void detectAndSendChanges() {
      super.detectAndSendChanges();

      for (int i = 0; i < listeners.size(); ++i) {
         IContainerListener listener = listeners.get(i);
         if (burnTime != te.burnTime) {
            listener.sendProgressBarUpdate(this, 0, te.burnTime);
         }
         if (totalBurnTime != te.totalBurnTime) {
            listener.sendProgressBarUpdate(this, 1, te.totalBurnTime);
         }
      }

      burnTime = te.burnTime;
      totalBurnTime = te.totalBurnTime;
   }

   @SideOnly(Side.CLIENT)
   public void updateProgressBar(int id, int data)
   {
      switch (id) {
      case 0: te.burnTime = data; break;
      case 1: te.totalBurnTime = data;
      }
   }

    void addPlayerSlots(IInventory playerInventory) {
      // Slots for the main inventory
      for (int l = 0; l < 3; ++l) {
         for (int j1 = 0; j1 < 9; ++j1) {
            addSlotToContainer(new Slot(playerInventory, j1 + l * 9 + 9, 8 + j1 * 18, 102 - 18 + l * 18));
         }
      }

      // Slots for the hotbar
      for (int i1 = 0; i1 < 9; ++i1) {
         addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 160 - 18));
      }
   }

   private void addOwnSlots() {
      IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

      addSlotToContainer(new SlotPredicate(x -> TileEntityFurnace.isItemFuel(x)
            || SlotFurnaceFuel.isBucket(x), itemHandler, 0, 80, 35));
      addSlotToContainer(new SlotPredicate(ItemUpgrade::isUpgrade, itemHandler, 1, 11, 17));
      addSlotToContainer(new SlotPredicate(ItemUpgrade::isUpgrade, itemHandler, 2, 11, 35));
      addSlotToContainer(new SlotPredicate(ItemUpgrade::isUpgrade, itemHandler, 3, 11, 53));
   }

   @Nullable
   @Override
   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
      ItemStack itemstack = null;
      Slot slot = inventorySlots.get(index);

      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();

         if (index < TileEntityQuarry.SIZE) {
            if (!mergeItemStack(itemstack1, TileEntityQuarry.SIZE, inventorySlots.size(), true)) {
               return null;
            }
         } else if (!mergeItemStack(itemstack1, 0, TileEntityQuarry.SIZE, false)) {
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

   @Override
   public boolean canInteractWith(EntityPlayer playerIn) {
      return te.canInteractWith(playerIn);
   }

   public static class SlotPredicate extends SlotItemHandler {
      Predicate<ItemStack> pred;

      public SlotPredicate(Predicate<ItemStack> pred, IItemHandler itemHandler,
                           int index, int xPosition, int yPosition) {
         super(itemHandler, index, xPosition, yPosition);
         this.pred = pred;
      }

      @Override
      public boolean isItemValid(@Nullable ItemStack stack) {
         return super.isItemValid(stack) && pred.test(stack);
      }
   }
}
