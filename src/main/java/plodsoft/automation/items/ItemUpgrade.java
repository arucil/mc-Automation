package plodsoft.automation.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import plodsoft.automation.Automation;

public class ItemUpgrade extends Item {
   public ItemUpgrade(String name) {
      setRegistryName(name);
      setUnlocalizedName(name);
      setCreativeTab(Automation.Tab);
      setMaxStackSize(1);

      GameRegistry.register(this);
   }

   public void initModel() {
      Automation.proxy.registerItemRenderer(this, 0);
   }

   public static boolean isUpgrade(ItemStack stack) {
      return null != stack && stack.getItem() instanceof ItemUpgrade;
   }
}
