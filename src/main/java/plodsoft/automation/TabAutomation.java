package plodsoft.automation;

import plodsoft.automation.blocks.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabAutomation extends CreativeTabs {
   public TabAutomation() {
      super(Automation.MODID);
   }

   @Override
   public Item getTabIconItem() {
      return Item.getItemFromBlock(ModBlocks.blockFeeder);
   }
}
