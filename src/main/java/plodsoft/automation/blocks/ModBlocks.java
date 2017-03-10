package plodsoft.automation.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModBlocks {
   public static Block blockFeeder;
   public static Block blockButcher;
   public static Block blockLumberjack;
   public static Block blockFarmer;

   public static void preInit(FMLPreInitializationEvent e) {
      blockFeeder = new BlockFeeder();
      blockButcher = new BlockButcher();
      blockLumberjack = new BlockLumberjack();
      blockFarmer = new BlockFarmer();

      if (e.getSide() == Side.CLIENT) {
         BlockFeeder.initModel();
         BlockButcher.initModel();
         BlockLumberjack.initModel();
         BlockFarmer.initModel();
      }
   }

   public static void init(FMLInitializationEvent e) {
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockFeeder),
            "IDI",
            "PRP",
            "PCP",
            'D', Blocks.DISPENSER,
            'I', Items.IRON_INGOT,
            'P', "plankWood",
            'R', Blocks.REDSTONE_BLOCK,
            'C', Blocks.CHEST));

      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockButcher),
            "ISI",
            "PRP",
            "PHP",
            'I', Items.IRON_INGOT,
            'S', Items.IRON_SWORD,
            'P', "plankWood",
            'R', Blocks.REDSTONE_BLOCK,
            'H', Blocks.HOPPER));

      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockLumberjack),
            "IAI",
            "PRP",
            "PSP",
            'I', Items.IRON_INGOT,
            'P', "plankWood",
            'A', Items.IRON_AXE,
            'R', Blocks.REDSTONE_BLOCK,
            'S', Blocks.SAPLING));

      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockFarmer),
            "KHK",
            "PRP",
            "PWP",
            'H', Items.IRON_HOE,
            'P', "plankWood",
            'R', Blocks.REDSTONE_BLOCK,
            'W', Items.WATER_BUCKET,
            'K', Blocks.LIT_PUMPKIN));
   }
}
