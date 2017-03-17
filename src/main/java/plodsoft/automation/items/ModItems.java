package plodsoft.automation.items;

import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {

   public static ItemUpgrade itemUpgradeObsidian;
   public static ItemUpgradeEnchant itemUpgradeEnch;
   public static ItemUpgradeFilter itemUpgradeFilter;

   public static void preInit() {
      itemUpgradeObsidian = new ItemUpgradeObsidian();
      itemUpgradeEnch = new ItemUpgradeEnchant();
      itemUpgradeFilter = new ItemUpgradeFilter();

      itemUpgradeObsidian.initModel();
      itemUpgradeEnch.initModel();
      itemUpgradeFilter.initModel();
   }

   public static void init() {
      GameRegistry.addShapedRecipe(new ItemStack(itemUpgradeObsidian),
            "ORO",
            "OWO",
            'O', Blocks.OBSIDIAN,
            'R', Blocks.REDSTONE_BLOCK,
            'W', Items.WATER_BUCKET);

      itemUpgradeEnch.registerRecipes();

      GameRegistry.addShapedRecipe(new ItemStack(itemUpgradeFilter),
            "GHG",
            "IRI",
            "DCD",
            'G', Items.GOLD_INGOT,
            'H', Blocks.HOPPER,
            'I', Items.IRON_INGOT,
            'R', Blocks.REDSTONE_BLOCK,
            'D', Items.DIAMOND,
            'C', Blocks.CHEST);
   }
}
