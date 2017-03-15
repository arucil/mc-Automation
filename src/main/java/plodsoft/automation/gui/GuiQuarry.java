package plodsoft.automation.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import plodsoft.automation.Automation;
import plodsoft.automation.tileentities.TileEntityQuarry;

public class GuiQuarry extends GuiContainer {
   private static final ResourceLocation TEXTURE = new ResourceLocation(Automation.MODID,
         "textures/gui/quarry.png");

   private IInventory playerInv;
   private TileEntityQuarry te;

   public GuiQuarry(IInventory playerInv, TileEntityQuarry te) {
      super(new ContainerQuarry(playerInv, te));
      this.playerInv = playerInv;
      this.te = te;
   }

   /**
    * Draw the foreground layer for the GuiContainer (everything in front of the items)
    */
   @Override
   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      fontRendererObj.drawString(playerInv.getDisplayName().getUnformattedText(), 8, 72, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      mc.getTextureManager().bindTexture(TEXTURE);
      drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
      if (te.burnTime > 0) {
         int k = te.burnTime * 13 / te.totalBurnTime;
         drawTexturedModalRect(guiLeft + 80, guiTop + 30 - k, 176, 12 - k, 14, k + 1);
      }
   }
}
