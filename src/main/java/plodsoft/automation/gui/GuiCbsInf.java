package plodsoft.automation.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import plodsoft.automation.Automation;
import plodsoft.automation.tileentities.TileEntityCbsInf;

public class GuiCbsInf extends GuiContainer {
   private static final ResourceLocation TEXTURE = new ResourceLocation(Automation.MODID,
         "textures/gui/cbsInf.png");

   private IInventory playerInv;
   private TileEntityCbsInf te;

   public GuiCbsInf(IInventory playerInv, TileEntityCbsInf te) {
      super(new ContainerCbsInf(playerInv, te));
      this.playerInv = playerInv;
      this.te = te;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      fontRendererObj.drawString(playerInv.getDisplayName().getUnformattedText(), 8, 72, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      mc.getTextureManager().bindTexture(TEXTURE);
      drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
   }
}
