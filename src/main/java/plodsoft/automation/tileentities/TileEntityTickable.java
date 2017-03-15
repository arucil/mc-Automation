package plodsoft.automation.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public abstract class TileEntityTickable extends TileEntity implements ITickable {
   private int delay;
   private int delay0;

   public TileEntityTickable(int seconds) {
      this.delay = this.delay0 = seconds * 20;
   }

   public TileEntityTickable(short halfSec) {
      this.delay = this.delay0 = halfSec * 10;
   }

   @Override
   public void update() {
      if (isInvalid() || worldObj.isRemote)
         return;
      if (--delay <= 0) {
         delay = delay0;
         onEntityUpdate();
      }
   }

   public void resetTimer() {
      delay = 0;
   }

   @Override
   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      delay = compound.getInteger("delay");
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      compound.setInteger("delay", delay);
      return compound;
   }

   public abstract void onEntityUpdate();

   // copy from TileEntityHopper
   protected IInventory getInventoryAtFacing(EnumFacing facing) {
      BlockPos blockpos = pos.add(facing.getFrontOffsetX(), 0,
            facing.getFrontOffsetZ());
      Block block = worldObj.getBlockState(blockpos).getBlock();

      if (block.hasTileEntity()) {
         TileEntity tileentity = worldObj.getTileEntity(blockpos);

         if (tileentity instanceof TileEntityChest && block instanceof BlockChest)
            return ((BlockChest) block).getContainer(worldObj, blockpos, true);
         if (tileentity instanceof IInventory)
            return (IInventory) tileentity;
      }
      return null;
   }
}
