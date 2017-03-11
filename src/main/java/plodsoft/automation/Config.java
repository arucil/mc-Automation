package plodsoft.automation;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {
   public static class Butcher {
      public static final String CATEGORY = "butcher";

      public static int Range;
      public static int Limit;
   }

   public static class Feeder {
      public static final String CATEGORY = "feeder";

      public static int Range;
      public static int InvSize;
   }

   public static class Lumberjack {
      public static final String CATEGORY = "lumberjack";

      public static int Range;
      public static int RangeY;
   }

   public static class Farmer {
      public static final String CATEGORY = "farmer";

      public static int Range;
   }

   public static void load(FMLPreInitializationEvent e) {
      Configuration cfg = new Configuration(e.getSuggestedConfigurationFile());
      cfg.load();

      // butcher
      Butcher.Range = cfg.getInt("range", Butcher.CATEGORY, 4, 1, 8, "");
      Butcher.Limit = cfg.getInt("limit", Butcher.CATEGORY, 5, 1, 100, "");

      // feeder
      Feeder.Range = cfg.getInt("range", Feeder.CATEGORY, 4, 1, 8, "");
      Feeder.InvSize = cfg.getInt("inventorySize", Feeder.CATEGORY, 4096, 1, 65535, "");

      // lumberjack
      Lumberjack.Range = cfg.getInt("range", Lumberjack.CATEGORY, 5, 1, 30, "");
      Lumberjack.RangeY = cfg.getInt("rangeY", Lumberjack.CATEGORY, 15, 1, 100, "");

      // farmer
      Farmer.Range = cfg.getInt("range", Farmer.CATEGORY, 4, 1, 8, "");

      if (cfg.hasChanged())
         cfg.save();
   }
}
