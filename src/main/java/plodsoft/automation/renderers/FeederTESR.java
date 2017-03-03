package plodsoft.automation.renderers;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import plodsoft.automation.tileentities.TileEntityFeeder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class FeederTESR extends TileEntitySpecialRenderer<TileEntityFeeder> {
   private static final int[] ANGLES = { 0, 0, 0, 180, 90, -90 };
   @Override
   public void renderTileEntityAt(TileEntityFeeder te, double x, double y, double z, float partialTicks, int destroyStage) {
      ItemStack stack = te.stack;
      if (stack != null) {
         RenderHelper.enableStandardItemLighting();
         GlStateManager.enableLighting();
         GlStateManager.pushMatrix();

         GlStateManager.translate(x + .5, y + .1875, z + .5);
         GlStateManager.scale(.4f, .4f, .4f);
         GlStateManager.rotate(90, -1, 0, 0);
         GlStateManager.rotate(ANGLES[te.getBlockMetadata()], 0, 0, 1);
         Minecraft.getMinecraft().getRenderItem().renderItem(stack,
               ItemCameraTransforms.TransformType.NONE);

         GlStateManager.popMatrix();
      }
   }
}
