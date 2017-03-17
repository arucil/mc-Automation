package plodsoft.automation.items;


import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemUpgradeEnchant extends ItemUpgrade {
   public static final String NAME = "upgradeEnchant";

   public static final String NBT_ENCH = "QUpgradeEnch";
   public static final String NBT_LVL = "QUpgradeLvl";

   public static final int FORTUNE_ID = Enchantment.getEnchantmentID(Enchantments.FORTUNE);
   public static final int SILK_ID = Enchantment.getEnchantmentID(Enchantments.SILK_TOUCH);

   public ItemStack fortune1, fortune2, fortune3, silk1;

   public ItemUpgradeEnchant() {
      super(NAME);
      setHasSubtypes(true);

      setEnchantment(fortune1 = new ItemStack(this), FORTUNE_ID, 1);
      setEnchantment(fortune2 = new ItemStack(this), FORTUNE_ID, 2);
      setEnchantment(fortune3 = new ItemStack(this), FORTUNE_ID, 3);
      setEnchantment(silk1 = new ItemStack(this), SILK_ID, 1);
   }

   public boolean isItemTool(ItemStack stack) {
       return false;
   }

   private static void setEnchantment(ItemStack stack, int ench, int level) {
      NBTTagCompound compound = stack.getTagCompound();
      if (null == compound) {
         stack.setTagCompound(compound = new NBTTagCompound());
      }
      compound.setShort(NBT_ENCH, (short) ench);
      compound.setShort(NBT_LVL, (short) level);
   }

   @Override
   @SideOnly(Side.CLIENT)
   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
      subItems.add(fortune1);
      subItems.add(fortune2);
      subItems.add(fortune3);
      subItems.add(silk1);
   }

   @Override
   @SideOnly(Side.CLIENT)
   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
      super.addInformation(stack, playerIn, tooltip, advanced);

      NBTTagCompound compound = stack.getTagCompound();
      if (null != compound && compound.hasKey(NBT_ENCH)) {
         tooltip.add(Enchantment.getEnchantmentByID(compound.getShort(NBT_ENCH))
               .getTranslatedName(compound.getShort(NBT_LVL)));
      }
   }

   @Override
   public boolean hasEffect(ItemStack stack) {
      return true;
   }

   public void registerRecipes() {
      GameRegistry.addRecipe(new Recipe(this));
   }

   private static class Recipe implements IRecipe {
      private static final ItemStack[] EMPTY_STACK = new ItemStack[9];

      private ItemUpgradeEnchant item;

      public Recipe(ItemUpgradeEnchant item) {
         this.item = item;
      }

      @Override
      public boolean matches(InventoryCrafting inv, World worldIn) {
         return match(inv, 0) != null || match(inv, 3) != null;
      }

      private ItemStack match(InventoryCrafting inv, int i) {
         ItemStack stack = inv.getStackInSlot(i);
         ItemStack stack1 = inv.getStackInSlot(i + 4);
         if (null == stack || stack.getItem() != Items.IRON_INGOT
               || (stack = inv.getStackInSlot(i + 1)) == null
               || stack.getItem() != Item.getItemFromBlock(Blocks.REDSTONE_BLOCK)
               || (stack = inv.getStackInSlot(i + 2)) == null || stack.getItem() != Items.IRON_INGOT
               || (stack = inv.getStackInSlot(i + 3)) == null || stack.getItem() != Items.GOLD_INGOT
               || (stack = inv.getStackInSlot(i + 5)) == null || stack.getItem() != Items.GOLD_INGOT
               || null == stack1 || stack1.getItem() != Items.ENCHANTED_BOOK)
            return null;
         NBTTagList list = Items.ENCHANTED_BOOK.getEnchantments(stack1);
         for (int j = 0; j < list.tagCount(); ++j) {
            NBTTagCompound compound = list.getCompoundTagAt(j);
            int id = compound.getShort("id");
            if (id == SILK_ID) {
               return item.silk1;
            } else if (id == FORTUNE_ID) {
               switch (compound.getShort("lvl")) {
               case 1:
                  return item.fortune1;
               case 2:
                  return item.fortune2;
               case 3:
                  return item.fortune3;
               }
            }
         }
         return null;
      }

      @Nullable
      @Override
      public ItemStack getCraftingResult(InventoryCrafting inv) {
         ItemStack stack = match(inv, 0);
         if (stack == null)
            stack = match(inv, 3);
         return null == stack ? null : stack.copy();
      }

      @Override
      public int getRecipeSize() {
         return 9;
      }

      @Nullable
      @Override
      public ItemStack getRecipeOutput() {
         return null;
      }

      @Override
      public ItemStack[] getRemainingItems(InventoryCrafting inv) {
         return EMPTY_STACK;
      }
   }
}
