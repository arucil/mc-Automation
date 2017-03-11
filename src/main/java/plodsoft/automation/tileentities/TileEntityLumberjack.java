package plodsoft.automation.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import plodsoft.automation.Config;

public class TileEntityLumberjack extends TileEntityTickable {

   public TileEntityLumberjack() {
      super(60);
   }

   @Override
   public void onEntityUpdate() {
      EnumFacing front = EnumFacing.getFront(7 & getBlockMetadata());
      BlockPos frontPos = pos.add(front.getFrontOffsetX(), 0, front.getFrontOffsetZ());
      Block block = worldObj.getBlockState(frontPos).getBlock();
      if (block == Blocks.AIR) {
         if (!Blocks.SAPLING.canPlaceBlockAt(worldObj, frontPos))
            return;
         IInventory inv = getInventoryAtFacing(front.getOpposite());
         if (null == inv)
            return;
         for (int i = 0, j = inv.getSizeInventory(); i < j; ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null && stack.stackSize != 0
                  && stack.getItem() == Item.getItemFromBlock(Blocks.SAPLING)) {
               worldObj.setBlockState(frontPos, Blocks.SAPLING.getStateFromMeta(stack.getItemDamage()));
               if (--stack.stackSize <= 0)
                  inv.setInventorySlotContents(i, null);
               break;
            }
         }
      } else if (block == Blocks.LOG || block == Blocks.LOG2) {
         front = front.getOpposite();
         IInventory inv = getInventoryAtFacing(front);
         if (null == inv)
            return;
         int y0 = pos.getY();
         int z0 = pos.getZ() - Config.Lumberjack.Range;
         for (int x = pos.getX() - Config.Lumberjack.Range, x1 = x + 2 * Config.Lumberjack.Range + 1; x <= x1; ++x) {
            for (int y = y0, y1 = y + Config.Lumberjack.RangeY; y <= y1; ++y) {
               for (int z = z0, z1 = z + Config.Lumberjack.Range * 2 + 1; z <= z1; ++z) {
                  BlockPos pos1 = new BlockPos(x, y, z);
                  IBlockState state = worldObj.getBlockState(pos1);
                  block = state.getBlock();
                  if (block == Blocks.LOG || block == Blocks.LOG2
                        || block == Blocks.LEAVES || block == Blocks.LEAVES2) {
                     for (ItemStack stack : block.getDrops(worldObj, pos1, state, 0)) {
                        stack = TileEntityHopper.putStackInInventoryAllSlots(inv, stack, front);
                        if (null != stack && stack.stackSize != 0) {
                           worldObj.spawnEntityInWorld(new EntityItem(worldObj, x, y, z, stack));
                        }
                     }
                     block.breakBlock(worldObj, pos1, state);
                     worldObj.setBlockToAir(pos1);
                  }
               }
            }
         }
      }
   }
}
