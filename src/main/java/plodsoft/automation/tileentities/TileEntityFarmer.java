package plodsoft.automation.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
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

      for (int x = pos.getX() - Config.Farmer.Range, x1 = pos.getX() + Config.Farmer.Range;
           x < x1; ++x) {
         for (int z = z0; z < z1; ++z) {
            IBlockState state = worldObj.getBlockState(new BlockPos(x, y, z));
            Block block = state.getBlock();
            if (Blocks.WHEAT == block) {
               if (state.<Integer>getValue(BlockCrops.AGE) >= 7) {
               }
            }
         }
      }
   }
}
