package plodsoft.automation.tileentities;

import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.items.ItemHandlerHelper;
import plodsoft.automation.Config;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class TileEntityFeeder extends TileEntityTickable implements IInventory {

   private static final Set<Item> ANIMAL_FOODS = Sets.newHashSet(new Item[] {
         Items.GOLDEN_APPLE, Items.GOLDEN_CARROT, Items.BEETROOT,
         Items.WHEAT_SEEDS, Items.WHEAT, Items.CARROT, Items.POTATO,
         Items.PUMPKIN_SEEDS, Items.MELON_SEEDS, Items.BEETROOT_SEEDS,
         Items.PORKCHOP, Items.COOKED_PORKCHOP, Items.BEEF, Items.COOKED_BEEF,
         Items.CHICKEN, Items.COOKED_CHICKEN, Items.RABBIT, Items.COOKED_RABBIT,
         Items.MUTTON, Items.ROTTEN_FLESH, Items.COOKED_MUTTON, Items.FISH,
   });

   public ItemStack stack;

   public TileEntityFeeder() {
      super(60);
   }

   @Override
   public void onEntityUpdate() {
      ItemStack stack1 = stack;
      if (null == stack1)
         return;
      List<EntityAnimal> list = worldObj.getEntitiesWithinAABB(EntityAnimal.class,
            new AxisAlignedBB(pos.add(-Config.Feeder.Range, -1, -Config.Feeder.Range),
                  pos.add(Config.Feeder.Range + 1, 2, Config.Feeder.Range + 1)),
            x -> !x.isInLove()
                  // !x.isChild() won't work, when growingAge is positive, it means
                  // the animals are in breeding cooldown.
                  && x.getGrowingAge() == 0
                  && x.isBreedingItem(stack1));
      int i = list.size() & ~1;
      if (i != 0) {
         int flag = 1;
         while (--i >= 0) {
            list.get(i).setInLove(null);
            if (--stack1.stackSize <= 0) {
               stack = null;
               flag = 3;
               break;
            }
         }
         markDirty();
         IBlockState state = worldObj.getBlockState(pos);
         // for data update only, flag is 1
         // for rendering update, flag is 3
         worldObj.notifyBlockUpdate(pos, state, state, flag);
      }
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

   @Override
   public int getSizeInventory() {
      return 1;
   }

   @Nullable
   @Override
   public ItemStack getStackInSlot(int index) {
      return null;
   }

   @Nullable
   @Override
   public ItemStack decrStackSize(int index, int count) {
      return null;
   }

   @Nullable
   @Override
   public ItemStack removeStackFromSlot(int index) {
      return null;
   }

   // only get called on server
   @Override
   public void setInventorySlotContents(int index, @Nullable ItemStack stack1) {
      markDirty();
      int flag = 1;
      if (null == stack) {
         stack = stack1.copy();
         stack1.stackSize = 0;
         flag = 3;
      } else {
         int limit = Config.Feeder.InvSize - stack.stackSize;
         if (stack1.stackSize > limit) {
            stack.stackSize += limit;
            stack1.stackSize -= limit;
         } else {
            stack.stackSize += stack1.stackSize;
            stack1.stackSize = 0;
         }
      }
      IBlockState state = worldObj.getBlockState(getPos());
      worldObj.notifyBlockUpdate(getPos(), state, state, flag);
   }

   @Override
   public int getInventoryStackLimit() {
      return Config.Feeder.InvSize;
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer player) {
      return false;
   }

   @Override
   public void openInventory(EntityPlayer player) {

   }

   @Override
   public void closeInventory(EntityPlayer player) {

   }

   @Override
   public boolean isItemValidForSlot(int index, ItemStack stack1) {
      return null == stack ? ANIMAL_FOODS.contains(stack1.getItem())
            : ItemHandlerHelper.canItemStacksStack(stack1, stack);
   }

   @Override
   public int getField(int id) {
      return 0;
   }

   @Override
   public void setField(int id, int value) {

   }

   @Override
   public int getFieldCount() {
      return 0;
   }

   @Override
   public void clear() {
      stack = null;
   }

   @Override
   public String getName() {
      return "tile.automation.feeder.name";
   }

   @Override
   public boolean hasCustomName() {
      return false;
   }
}
