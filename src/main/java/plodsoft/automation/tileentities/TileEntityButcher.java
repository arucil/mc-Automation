package plodsoft.automation.tileentities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class TileEntityButcher extends TileEntityTickable {
   public static final int LIMIT = 5;
   public static final int RANGE = 4;

   private static FakePlayer fakePlayer;

   public TileEntityButcher() {
      super(120);
   }

   @Override
   public void onEntityUpdate() {
      FakePlayer fake = fakePlayer;
      if (null == fake)
         fakePlayer = fake = FakePlayerFactory.getMinecraft((WorldServer) worldObj);
      int i = LIMIT;
      for (EntityAnimal animal : worldObj.getEntitiesWithinAABB(EntityAnimal.class,
            new AxisAlignedBB(pos.add(-RANGE, -1, -RANGE),
                  pos.add(RANGE + 1, 2, RANGE + 1)),
                  x -> x.getGrowingAge() >= 0)) {
         animal.setHealth(.5f);
         animal.attackEntityFrom(DamageSource.causePlayerDamage(fake), 400f);
         if (--i <= 0)
            break;
      }

      EnumFacing facing = EnumFacing.getFront(7 & getBlockMetadata());
      IInventory inv = getInventoryAtFacing(facing);
      if (null == inv) {
         return;
      }
      facing = facing.getOpposite();
      // collect dropped items
      for (EntityItem item : worldObj.getEntitiesWithinAABB(EntityItem.class,
            new AxisAlignedBB(pos.add(-RANGE - 2, -3, -RANGE - 2),
                  pos.add(RANGE + 3, 5, RANGE + 3)))) {
         ItemStack stack = TileEntityHopper.putStackInInventoryAllSlots(inv, item.getEntityItem(),
               facing);
         if (stack == null || stack.stackSize == 0)
            item.setDead();
         else
            item.setEntityItemStack(stack);
      }
   }

   // copy from TileEntityHopper
   private IInventory getInventoryAtFacing(EnumFacing facing) {
      BlockPos blockpos = new BlockPos(pos.getX() + facing.getFrontOffsetX(),
            pos.getY() + facing.getFrontOffsetY(),
            pos.getZ() + facing.getFrontOffsetZ());
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
