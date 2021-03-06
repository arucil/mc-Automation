package plodsoft.automation.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import plodsoft.automation.items.ModItems;
import plodsoft.automation.tileentities.TileEntityCbsInf;
import plodsoft.automation.tileentities.TileEntityQuarry;

public class GuiHandler implements IGuiHandler {
   public static final int GUI_QUARRY_ID = 1;
   public static final int GUI_FILTER_ID = 2;
   public static final int GUI_COBBLESTONE_ID = 3;

   @Override
   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      switch (ID) {
      case GUI_QUARRY_ID: {
         TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
         if (te instanceof TileEntityQuarry)
            return new ContainerQuarry(player.inventory, (TileEntityQuarry) te);
         break;
      }
      case GUI_FILTER_ID: {
         ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
         if (stack != null && stack.getItem() == ModItems.itemUpgradeFilter)
            return new ContainerFilter(player);
         break;
      }
      case GUI_COBBLESTONE_ID: {
         TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
         if (te instanceof TileEntityCbsInf)
            return new ContainerCbsInf(player.inventory, (TileEntityCbsInf) te);
         break;
      }
      }
      return null;
   }

   @Override
   public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      switch (ID) {
      case GUI_QUARRY_ID: {
         TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
         if (te instanceof TileEntityQuarry)
            return new GuiQuarry(player.inventory, (TileEntityQuarry) te);
         break;
      }
      case GUI_FILTER_ID: {
         ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
         if (stack !=null && stack.getItem() == ModItems.itemUpgradeFilter)
            return new GuiFilter(player);
         break;
      }
      case GUI_COBBLESTONE_ID: {
         TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
         if (te instanceof TileEntityCbsInf)
            return new GuiCbsInf(player.inventory, (TileEntityCbsInf) te);
         break;
      }
      }
      return null;
   }
}
