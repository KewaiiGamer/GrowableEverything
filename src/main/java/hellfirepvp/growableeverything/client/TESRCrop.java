package hellfirepvp.growableeverything.client;

import hellfirepvp.growableeverything.block.BlockScientificCrop;
import hellfirepvp.growableeverything.block.tile.TileScientificCrop;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * This class is part of the GrowableEverything Mod
 * GrowableEverything is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p>
 * Created by HellFirePvP @ 28.12.2016 22:21
 */
@SideOnly(Side.CLIENT)
public class TESRCrop extends TileEntitySpecialRenderer<TileScientificCrop> {

    @Override
    public void renderTileEntityAt(TileScientificCrop te, double x, double y, double z, float partialTicks, int destroyStage) {
        ItemStack stack = te.getSeedItemStack();
        if(stack.isEmpty()) return;
        IBlockState cropPos = te.getWorld().getBlockState(te.getPos());
        if(cropPos.getBlock() instanceof BlockScientificCrop) {
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glPushMatrix();

            GL11.glTranslated(x + 0.5, y, z + 0.5);
            GL11.glRotated(45, 0, 1, 0);

            //RenderEntityItem

            int age = cropPos.getValue(BlockScientificCrop.AGE);
            if(age == 5) {
                /*EntityItem ei = new EntityItem(Minecraft.getMinecraft().world, 0, 0, 0, stack);
                ei.hoverStart = 0;
                Minecraft.getMinecraft().getRenderManager().doRenderEntity(ei, 0, 0, 0, 0, 0, true);
                GlStateManager.enableAlpha();*/
            } else if(age == 6) {
                /*EntityItem ei = new EntityItem(Minecraft.getMinecraft().world, 0, 0, 0, stack);
                ei.hoverStart = 0;
                Minecraft.getMinecraft().getRenderManager().doRenderEntity(ei, 0, 0, 0, 0, 0, true);
                GlStateManager.enableAlpha();*/
            } else if(age == 7) {
                RenderItem ri = Minecraft.getMinecraft().getRenderItem();
                //ri.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GlStateManager.alphaFunc(516, 0.1F);
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                Minecraft.getMinecraft().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

                IBakedModel ibakedmodel = ri.getItemModelWithOverrides(stack, null, null);
                ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.GROUND, false);

                ri.renderItem(stack, ibakedmodel);
                //ri.renderItemModel(stack, ibakedmodel, cameraTransformType, false);

                //GlStateManager.disableRescaleNormal();
            }

            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

}
