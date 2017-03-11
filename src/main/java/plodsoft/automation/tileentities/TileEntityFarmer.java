package plodsoft.automation.tileentities;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import plodsoft.automation.Config;

public class TileEntityFarmer extends TileEntityTickable {

   public TileEntityFarmer() {
      super(60);
   }

   @Override
   public void onEntityUpdate() {
      int y = pos.getY();
      int z0 = pos.getZ() - Config.Farmer.Range;
      int z1 = pos.getZ() + Config.Farmer.Range;
      EnumFacing front = EnumFacing.getFront(7 & getBlockMetadata());
      IInventory inv = getInventoryAtFacing(front);
      if (null == inv)
         return;
      front = front.getOpposite();

      for (int x = pos.getX() - Config.Farmer.Range, x1 = pos.getX() + Config.Farmer.Range;
           x <= x1; ++x) {
         for (int z = z0; z <= z1; ++z) {
            BlockPos pos1 = new BlockPos(x, y, z);
            IBlockState state = worldObj.getBlockState(pos1);
            Block block = state.getBlock();

            if (Blocks.WHEAT == block || Blocks.POTATOES == block || Blocks.CARROTS == block) {
               if (state.<Integer>getValue(BlockCrops.AGE) >= 7) {
                  harvest(worldObj, pos1, state, block, front, inv);
               }
            } else if (Blocks.NETHER_WART == block || Blocks.BEETROOTS == block) {
               if (state.<Integer>getValue(BlockNetherWart.AGE) >= 3) {
                  harvest(worldObj, pos1, state, block, front, inv);
               }
            } else if (Blocks.AIR == block) {
               BlockPos pos2 = pos1.down();
               state = worldObj.getBlockState(pos2);
               block = state.getBlock();

               if (Blocks.DIRT == block || Blocks.GRASS == block || Blocks.GRASS_PATH == block) {
                  for (int i = inv.getSizeInventory(); --i >= 0; ) {
                     ItemStack stack = inv.getStackInSlot(i);
                     if (stack != null && stack.stackSize != 0
                           && stack.getItem() instanceof ItemHoe) {
                        worldObj.setBlockState(pos2, Blocks.FARMLAND.getDefaultState());
                        if (stack.attemptDamageItem(1, worldObj.rand))
                           inv.setInventorySlotContents(i, null);
                        break;
                     }
                  }
               } else if (Blocks.FARMLAND == block) {
                  for (int i = inv.getSizeInventory(); --i >= 0; ) {
                     ItemStack stack = inv.getStackInSlot(i);
                     if (stack != null && stack.stackSize != 0) {
                        Item item = stack.getItem();
                        if (Items.WHEAT_SEEDS == item) {
                           worldObj.setBlockState(pos1, Blocks.WHEAT.getDefaultState());
                           if (--stack.stackSize <= 0)
                              inv.setInventorySlotContents(i, null);
                           break;
                        } else if (Items.CARROT == item) {
                           worldObj.setBlockState(pos1, Blocks.CARROTS.getDefaultState());
                           if (--stack.stackSize <= 0)
                              inv.setInventorySlotContents(i, null);
                           break;
                        } else if (Items.POTATO == item) {
                           worldObj.setBlockState(pos1, Blocks.POTATOES.getDefaultState());
                           if (--stack.stackSize <= 0)
                              inv.setInventorySlotContents(i, null);
                           break;
                        } else if (Items.BEETROOT_SEEDS == item) {
                           worldObj.setBlockState(pos1, Blocks.BEETROOTS.getDefaultState());
                           if (--stack.stackSize <= 0)
                              inv.setInventorySlotContents(i, null);
                           break;
                        }
                     }
                  }
               } else if (Blocks.SOUL_SAND == block) {
                  for (int i = inv.getSizeInventory(); --i >= 0; ) {
                     ItemStack stack = inv.getStackInSlot(i);
                     if (stack != null && stack.stackSize != 0
                           && Items.NETHER_WART == stack.getItem()) {
                        worldObj.setBlockState(pos1, Blocks.NETHER_WART.getDefaultState());
                        if (--stack.stackSize <= 0)
                           inv.setInventorySlotContents(i, null);
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private static void harvest(World world, BlockPos pos, IBlockState state, Block block, EnumFacing front, IInventory inv) {
      for (ItemStack stack : block.getDrops(world, pos, state, 0)) {
         stack = TileEntityHopper.putStackInInventoryAllSlots(inv, stack, front);
         if (null != stack && stack.stackSize != 0) {
            world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
         }
      }
      block.breakBlock(world, pos, state);
      world.setBlockToAir(pos);
   }
}
