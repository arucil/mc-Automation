package plodsoft.automation.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiFilter extends GuiContainer {
   private static final ResourceLocation TEXTURE =
         new ResourceLocation("textures/gui/container/hopper.png");

   private IInventory inv;

   public GuiFilter(EntityPlayer player) {
      super(new ContainerFilter(player));
      this.inv = player.inventory;
      ySize = 133;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      fontRendererObj.drawString(inv.getDisplayName().getUnformattedText(),
            8, 133 - 96 + 2, 4210752);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      mc.getTextureManager().bindTexture(TEXTURE);
      drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
   }
}
