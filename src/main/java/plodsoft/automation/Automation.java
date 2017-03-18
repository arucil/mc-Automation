package plodsoft.automation;

import plodsoft.automation.gui.GuiHandler;
import plodsoft.automation.items.ModItems;
import plodsoft.automation.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import plodsoft.automation.blocks.ModBlocks;

@Mod(modid = Automation.MODID, name = Automation.NAME, version = Automation.VERSION,
      acceptedMinecraftVersions = "[1.10.2]")
public class Automation {
   public static final String MODID = "automation";
   public static final String VERSION = "1.3";
   public static final String NAME = "Automation";

   @Mod.Instance(MODID)
   public static Object instance;

   @SidedProxy(clientSide = "plodsoft.automation.proxy.ClientProxy",
         serverSide = "plodsoft.automation.proxy.CommonProxy")
   public static CommonProxy proxy;

   public static final CreativeTabs Tab = new TabAutomation();


   @Mod.EventHandler
   public void preInit(FMLPreInitializationEvent e) {
      ModBlocks.preInit();
      ModItems.preInit();

      Config.load(e);
   }

   @Mod.EventHandler
   public void init(FMLInitializationEvent e) {
      ModBlocks.init();
      ModItems.init();

      NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
   }
}
