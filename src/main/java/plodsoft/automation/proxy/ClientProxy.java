package plodsoft.automation.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ClientProxy extends CommonProxy {
   @Override
   public void registerItemRenderer(Item item, int meta) {
      ModelLoader.setCustomModelResourceLocation(item, meta,
            new ModelResourceLocation(item.getRegistryName(), "inventory"));
   }
}
