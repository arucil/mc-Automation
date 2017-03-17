package plodsoft.automation.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
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
import plodsoft.automation.items.ItemUpgradeEnchant;
import plodsoft.automation.items.ItemUpgradeFilter;
import plodsoft.automation.items.ModItems;

import java.util.HashSet;
import java.util.Set;

public class TileEntityQuarry extends TileEntityTickable {
   public static final int SIZE = 4;
   private static final int Y_INIT = -2;
   private static final int Y_OVER = -1;

   public ItemStackHandler handler = new ItemStackHandler(SIZE) {
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

      EnumFacing facing = EnumFacing.getFront(7 & getBlockMetadata());
      IInventory inv = getInventoryAtFacing(facing);

      ItemStack stack;
      if (Y_OVER != y && burnTime < 20 && (stack = handler.getStackInSlot(0)) != null && inv != null) {
         burnTime += TileEntityFurnace.getItemBurnTime(stack);
         totalBurnTime = burnTime;
         if (--stack.stackSize <= 0)
            handler.setStackInSlot(0, null);
      }

      if (0 != burnTime) {
         if (burnTime >= 20) {
            burnTime -= 20;

            if (Y_OVER != y && inv != null) {
               boolean obsidian = false;
               int fortune = 0;
               boolean silk = false;
               Set<ItemUpgradeFilter.ItemComparer> filter = new HashSet<>();

               // check upgrades
               for (int i = 1; i < 4; ++i) {
                  stack = handler.getStackInSlot(i);
                  if (stack != null) {
                     Item item = stack.getItem();
                     if (ModItems.itemUpgradeObsidian == item) {
                        obsidian = true;
                     } else if (item instanceof ItemUpgradeEnchant) {
                        NBTTagCompound compound = stack.getTagCompound();
                        if (null != compound && compound.hasKey(ItemUpgradeEnchant.NBT_ENCH)) {
                           Enchantment ench = Enchantment.getEnchantmentByID(
                                 compound.getShort(ItemUpgradeEnchant.NBT_ENCH));
                           int lvl = compound.getShort(ItemUpgradeEnchant.NBT_LVL);
                           if (Enchantments.FORTUNE == ench) {
                              if (lvl > fortune)
                                 fortune = lvl;
                           } else if (Enchantments.SILK_TOUCH == ench) {
                              silk = true;
                           }
                        }
                     } else if (ModItems.itemUpgradeFilter == item) {
                        ItemStackHandler handler = ItemUpgradeFilter.getInventory(stack);
                        for (int j = ItemUpgradeFilter.SIZE; --j >= 0; ) {
                           ItemStack stack1 = handler.getStackInSlot(j);
                           if (stack1 != null) {
                              System.out.println("add " + stack1);
                              filter.add(new ItemUpgradeFilter.ItemComparer(stack1));
                           }
                        }
                     }
                  }
               }

               BlockPos pos1 = new BlockPos(x, y, z);
               IBlockState state = worldObj.getBlockState(pos1);
               Block block = state.getBlock();
               while (Blocks.AIR == block || block.getBlockHardness(state, worldObj, pos1) < 0) {
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

               facing = facing.getOpposite();
               if (Y_OVER != y) {
                  if (obsidian && block.getMaterial(state) == Material.LAVA) {
                     if (state.getValue(BlockLiquid.LEVEL) == 0) {
                        worldObj.destroyBlock(pos1, false);
                        if (!filter.contains(ItemUpgradeFilter.ItemComparer.OBSIDIAN)) {
                           ItemStack stack1 = TileEntityHopper.putStackInInventoryAllSlots(inv,
                                 new ItemStack(Blocks.OBSIDIAN), facing);
                           if (null != stack1 && stack1.stackSize != 0) {
                              worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos1.getX(),
                                    pos1.getY(), pos1.getZ(), stack1));
                           }
                        }
                     }
                  } else if (FluidRegistry.lookupFluidForBlock(block) == null
                        && !(block instanceof BlockDynamicLiquid)) {
                     worldObj.destroyBlock(pos1, false);
                     if (silk) {
                        if (null != (stack = createStackedBlock(block, state))
                              && !filter.contains(stack)) {
                           // put fuel in own inventory first
                           if (TileEntityFurnace.getItemBurnTime(stack) > 0)
                              stack = putStackInInventory(handler, stack);
                           stack = TileEntityHopper.putStackInInventoryAllSlots(inv, stack, facing);
                           if (null != stack && stack.stackSize != 0) {
                              worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos1.getX(),
                                    pos1.getY(), pos1.getZ(), stack));
                           }
                        }
                     } else {
                        for (ItemStack stack1 : block.getDrops(worldObj, pos1, state, fortune)) {
                           // put fuel in own inventory first
                           if (TileEntityFurnace.getItemBurnTime(stack1) > 0)
                              stack1 = putStackInInventory(handler, stack1);
                           if (stack1 != null && filter.contains(new ItemUpgradeFilter.ItemComparer(stack1)))
                              continue;
                           stack1 = TileEntityHopper.putStackInInventoryAllSlots(inv, stack1, facing);
                           if (null != stack1 && stack1.stackSize != 0) {
                              worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos1.getX(),
                                    pos1.getY(), pos1.getZ(), stack1));
                           }
                        }
                     }
                  }
               }

               if (--x < x0) {
                  x = x0 + 15;
                  if (--z < z0) {
                     z = z0 + 15;
                     --y;
                  }
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

   private static ItemStack putStackInInventory(ItemStackHandler handler, ItemStack stack) {
      ItemStack stack1 = handler.getStackInSlot(0);
      if (null == stack1) {
         if (stack.stackSize > stack.getMaxStackSize()) {
            stack1 = stack.copy();
            stack1.stackSize = stack.getMaxStackSize();
            handler.setStackInSlot(0, stack1);
            stack.stackSize -= stack1.stackSize;
            return stack;
         } else {
            handler.setStackInSlot(0, stack);
         }
      } else if (stack.getItem() == stack1.getItem() && stack.getMetadata() == stack1.getMetadata()
            && ItemStack.areItemsEqual(stack, stack1)) {
         stack1.stackSize += stack.stackSize;
         if (stack1.stackSize > stack1.getMaxStackSize()) {
            stack.stackSize = stack1.stackSize - stack1.getMaxStackSize();
            stack1.stackSize = stack1.getMaxStackSize();
            return stack;
         }
      }
      return null;
   }

   // copy from Block
   public static ItemStack createStackedBlock(Block block, IBlockState state) {
      Item item = Item.getItemFromBlock(block);

      if (item == null) {
         return null;
      } else if (item.getHasSubtypes()) {
         return new ItemStack(item, 1, block.getMetaFromState(state));
      } else
         return new ItemStack(item);
   }
}
