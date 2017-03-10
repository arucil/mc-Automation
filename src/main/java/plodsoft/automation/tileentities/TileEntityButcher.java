package plodsoft.automation.tileentities;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import plodsoft.automation.Config;

public class TileEntityButcher extends TileEntityTickable {

   private static FakePlayer fakePlayer;

   public TileEntityButcher() {
      super(120);
   }

   @Override
   public void onEntityUpdate() {
      FakePlayer fake = fakePlayer;
      if (null == fake)
         fakePlayer = fake = FakePlayerFactory.getMinecraft((WorldServer) worldObj);
      int i = Config.Butcher.Limit;
      for (EntityAnimal animal : worldObj.getEntitiesWithinAABB(EntityAnimal.class,
            new AxisAlignedBB(pos.add(-Config.Butcher.Range, -1, -Config.Butcher.Range),
                  pos.add(Config.Butcher.Range + 1, 2, Config.Butcher.Range + 1)),
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
            new AxisAlignedBB(pos.add(-Config.Butcher.Range - 2, -3, -Config.Butcher.Range - 2),
                  pos.add(Config.Butcher.Range + 3, 5, Config.Butcher.Range + 3)))) {
         ItemStack stack = TileEntityHopper.putStackInInventoryAllSlots(inv, item.getEntityItem(),
               facing);
         if (stack == null || stack.stackSize == 0)
            item.setDead();
         else
            item.setEntityItemStack(stack);
      }
   }
}
