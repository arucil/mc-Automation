package plodsoft.automation.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import plodsoft.automation.Automation;
import plodsoft.automation.tileentities.TileEntityTickable;

import javax.annotation.Nullable;

public abstract class BlockTickable extends Block {
   public BlockTickable() {
      super(Material.ROCK);
      setHardness(3f);
      setCreativeTab(Automation.Tab);
   }

   @Override
   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (worldIn.isRemote)
         return true;
      TileEntity te = worldIn.getTileEntity(pos);
      if (!(te instanceof TileEntityTickable))
         return false;
      onBlockActivated(worldIn, pos, playerIn, (TileEntityTickable) te);
      return true;
   }

   protected abstract void onBlockActivated(World world, BlockPos pos, EntityPlayer player, TileEntityTickable te);
}
