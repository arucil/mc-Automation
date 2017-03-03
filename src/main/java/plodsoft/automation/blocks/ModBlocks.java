package plodsoft.automation.blocks;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ModBlocks {
   public static Block blockFeeder;

   public static void preInit(FMLPreInitializationEvent e) {
      blockFeeder = new BlockFeeder();
      if (e.getSide() == Side.CLIENT)
         BlockFeeder.initModel();
   }

   public static void init(FMLInitializationEvent e) {
   }
}
