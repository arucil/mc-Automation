package plodsoft.automation.tileentities;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public abstract class TileEntityTickable extends TileEntity implements ITickable {
   private int delay;
   private int delay0;

   public TileEntityTickable(int seconds) {
      this.delay0 = seconds * 20;
   }

   @Override
   public void update() {
      if (--delay <= 0) {
         delay = delay0;
         onEntityUpdate();
      }
   }

   public abstract void onEntityUpdate();
}
