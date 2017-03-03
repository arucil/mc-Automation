package plodsoft.automation.tileentities;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityFeeder extends TileEntityTickable {
   public static final int RANGE = 4;
   public static final int SIZE = 4096;

   public ItemStack stack;

   public TileEntityFeeder() {
      super(60);
      stack = new ItemStack(Items.WHEAT);
   }

   @Override
   public void onEntityUpdate() {
      List<EntityAnimal> animals = worldObj.getEntitiesWithinAABB(EntityAnimal.class,
            new AxisAlignedBB(pos.add(-RANGE, -RANGE, -1),
                  pos.add(RANGE, RANGE, 1)),
            x -> !(x.isInLove() || x.isChild()));
   }

   @Override
   public void readFromNBT(NBTTagCompound compound) {
      super.readFromNBT(compound);
      if (compound.hasKey("item"))
         stack = ItemStack.loadItemStackFromNBT((NBTTagCompound) compound.getTag("item"));
      else
         stack = null;
   }

   @Override
   public NBTTagCompound writeToNBT(NBTTagCompound compound) {
      super.writeToNBT(compound);
      if (stack != null)
         compound.setTag("item", stack.serializeNBT());
      return compound;
   }

   @Override
   public NBTTagCompound getUpdateTag() {
      return writeToNBT(new NBTTagCompound());
   }

   @Override
   public SPacketUpdateTileEntity getUpdatePacket() {
      return new SPacketUpdateTileEntity(getPos(), 0, writeToNBT(new NBTTagCompound()));
   }

   @Override
   public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
      readFromNBT(packet.getNbtCompound());
   }

}
