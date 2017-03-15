package plodsoft.automation.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityQuarry extends TileEntityTickable {
   public static final int SIZE = 4;
   private static final int Y_INIT = -2;
   private static final int Y_OVER = -1;

   private ItemStackHandler handler = new ItemStackHandler(SIZE) {
      @Override
      protected void onContentsChanged(int slot) {
         TileEntityQuarry.this.markDirty();
      }
   };

   public int burnTime, totalBurnTime;
   private int x0, x, y = Y_INIT, z0, z;

   public TileEntityQuarry() {
      super((short) 1);
   }

   @Override
   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      if (compound.hasKey("items")) {
         handler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
         burnTime = compound.getInteger("burnTime");
         totalBurnTime = compound.getInteger("totalBurnTime");
         x0 = compound.getInteger("Qx0");
         x = compound.getInteger("Qx");
         y = compound.getInteger("Qy");
         z0 = compound.getInteger("Qz0");
         z = compound.getInteger("Qz");
      }
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      compound.setTag("items", handler.serializeNBT());
      compound.setInteger("burnTime", burnTime);
      compound.setInteger("totalBurnTime", totalBurnTime);
      compound.setInteger("Qx0", x0);
      compound.setInteger("Qx", x);
      compound.setInteger("Qy", y);
      compound.setInteger("Qz0", z0);
      compound.setInteger("Qz", z);
      return compound;
   }

   public boolean canInteractWith(EntityPlayer playerIn) {
      // If we are too far away from this tile entity you cannot use it
      return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
   }

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

   @Override
   public void onEntityUpdate() {
      boolean dirty = false;

      if (Y_INIT == y) {
         Chunk c = worldObj.getChunkFromBlockCoords(pos);
         x0 = c.xPosition << 4;
         x = x0 + 15;
         y = pos.getY() - 1;
         z0 = c.zPosition << 4;
         z = z0 + 15;
         dirty = true;
      }

      ItemStack stack;
      if (Y_OVER != y && burnTime < 20 && (stack = handler.getStackInSlot(0)) != null) {
         burnTime += TileEntityFurnace.getItemBurnTime(stack);
         totalBurnTime = burnTime;
         if (--stack.stackSize <= 0)
            handler.setStackInSlot(0, null);
      }

      if (0 != burnTime) {
         if (burnTime >= 20) {
            burnTime -= 20;

            BlockPos pos1 = new BlockPos(x, y, z);
            IBlockState state = worldObj.getBlockState(pos1);
            Block block = state.getBlock();
            while (Blocks.AIR == block || FluidRegistry.lookupFluidForBlock(block) != null
                  || Blocks.FLOWING_LAVA == block || Blocks.FLOWING_WATER == block
                  || block.getBlockHardness(state, worldObj, pos1) < 0) {
               if (--x < x0) {
                  x = x0 + 15;
                  if (--z < z0) {
                     z = z0 + 15;
                     if (--y < 0)
                        break;
                  }
               }
               pos1 = new BlockPos(x, y, z);
               state = worldObj.getBlockState(pos1);
               block = state.getBlock();
            }

            EnumFacing facing = EnumFacing.getFront(7 & getBlockMetadata());
            IInventory inv = getInventoryAtFacing(facing);
            facing = facing.getOpposite();
            if (Y_OVER != y && inv != null) {
               for (ItemStack stack1 : block.getDrops(worldObj, pos1, state, 0)) {
                  // put fuel in own inventory first
                  if (TileEntityFurnace.getItemBurnTime(stack1) > 0)
                     stack1 = putStackInInventory(handler, 0, stack1);
                  stack1 = TileEntityHopper.putStackInInventoryAllSlots(inv, stack1, facing);
                  if (null != stack1 && stack1.stackSize != 0) {
                     worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos1.getX(), pos1.getY(), pos1.getZ(), stack1));
                  }
               }
               worldObj.destroyBlock(pos1, false);
            }

            if (--x < x0) {
               x = x0 + 15;
               if (--z < z0) {
                  z = z0 + 15;
                  --y;
               }
            }
         } else
            burnTime = 0;
         dirty = true;
      }

      if (dirty) {
         markDirty();
      }
   }

   public static ItemStack putStackInInventory(ItemStackHandler handler, int i, ItemStack stack) {
      ItemStack stack1 = handler.getStackInSlot(i);
      if (null == stack1) {
         if (stack.stackSize > stack.getMaxStackSize()) {
            stack1 = stack.copy();
            stack1.stackSize = stack.getMaxStackSize();
            handler.setStackInSlot(i, stack1);
            stack.stackSize -= stack1.stackSize;
            return stack;
         } else {
            handler.setStackInSlot(i, stack);
         }
      } else if (stack.getItem() == stack1.getItem() && stack.getMetadata() == stack1.getMetadata()
            && ItemStack.areItemsEqual(stack, stack1)) {
         stack1.stackSize += stack.stackSize;
         if (stack1.stackSize > stack.getMaxStackSize()) {
            stack.stackSize = stack1.stackSize - stack.getMaxStackSize();
            return stack;
         }
      }
      return null;
   }
}
