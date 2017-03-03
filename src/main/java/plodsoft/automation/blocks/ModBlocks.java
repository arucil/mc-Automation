package plodsoft.automation.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModBlocks {
   public static Block blockFeeder;

   public static void preInit(FMLPreInitializationEvent e) {
      blockFeeder = new BlockFeeder();
      if (e.getSide() == Side.CLIENT)
         BlockFeeder.initModel();
   }

   public static void init(FMLInitializationEvent e) {
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockFeeder),
            "IWI",
            "PRP",
            "PCP",
            'W', Items.WHEAT,
            'I', Items.IRON_INGOT,
            'P', "plankWood",
            'R', Blocks.REDSTONE_BLOCK,
            'C', Blocks.CHEST));
   }
}
