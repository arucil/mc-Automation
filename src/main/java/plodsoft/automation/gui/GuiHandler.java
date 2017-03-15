package plodsoft.automation.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import plodsoft.automation.tileentities.TileEntityQuarry;

public class GuiHandler implements IGuiHandler {
   public static final int GUI_QUARRY_ID = 1;

   @Override
   public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
      switch (ID) {
      case GUI_QUARRY_ID: {
         TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
         if (te instanceof TileEntityQuarry)
            return new ContainerQuarry(player.inventory, (TileEntityQuarry) te);
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
      }
      return null;
   }
}
