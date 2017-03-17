package plodsoft.automation.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModBlocks {
   public static Block blockFeeder;
   public static Block blockButcher;
   public static Block blockLumberjack;
   public static Block blockFarmer;
   public static Block blockQuarry;
   public static Block blockCbChest;

   public static void preInit() {
      blockFeeder = new BlockFeeder();
      blockButcher = new BlockButcher();
      blockLumberjack = new BlockLumberjack();
      blockFarmer = new BlockFarmer();
      blockQuarry = new BlockQuarry();
      blockCbChest = new BlockCobblestoneInfinity();

      BlockFeeder.initModel();
      BlockButcher.initModel();
      BlockLumberjack.initModel();
      BlockFarmer.initModel();
      BlockQuarry.initModel();
      BlockCobblestoneInfinity.initModel();
   }

   public static void init() {
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

      GameRegistry.addShapedRecipe(new ItemStack(blockQuarry),
            "EPE",
            "IRI",
            "GCG",
            'E', Items.ENDER_PEARL,
            'P', Items.DIAMOND_PICKAXE,
            'I', Items.IRON_INGOT,
            'R', Blocks.REDSTONE_BLOCK,
            'G', Items.GOLD_INGOT,
            'C', Blocks.CHEST);

      GameRegistry.addShapedRecipe(new ItemStack(blockCbChest),
            "WPL",
            "CRC",
            "CHC",
            'W', Items.WATER_BUCKET,
            'L', Items.LAVA_BUCKET,
            'P', Items.IRON_PICKAXE,
            'C', Blocks.COBBLESTONE,
            'R', Blocks.REDSTONE_BLOCK,
            'H', Blocks.CHEST);
   }
}
