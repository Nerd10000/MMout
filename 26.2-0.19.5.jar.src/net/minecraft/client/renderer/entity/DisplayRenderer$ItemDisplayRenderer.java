/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.entity.state.DisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.ItemDisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.Display;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class ItemDisplayRenderer extends DisplayRenderer<Display.ItemDisplay, Display.ItemDisplay.ItemRenderState, ItemDisplayEntityRenderState> {
/*     */   private final ItemModelResolver itemModelResolver;
/*     */   
/*     */   protected ItemDisplayRenderer(EntityRendererProvider.Context context) {
/* 189 */     super(context);
/* 190 */     this.itemModelResolver = context.getItemModelResolver();
/*     */   }
/*     */   
/*     */   public ItemDisplayEntityRenderState createRenderState() {
/* 195 */     return new ItemDisplayEntityRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(Display.ItemDisplay entity, ItemDisplayEntityRenderState state, float partialTicks) {
/* 200 */     super.extractRenderState(entity, state, partialTicks);
/* 201 */     Display.ItemDisplay.ItemRenderState itemRenderState = entity.itemRenderState();
/* 202 */     if (itemRenderState != null) {
/* 203 */       this.itemModelResolver.updateForNonLiving(state.item, itemRenderState.itemStack(), itemRenderState.itemTransform(), (Entity)entity);
/*     */     } else {
/* 205 */       state.item.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void submitInner(ItemDisplayEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float interpolationProgress) {
/* 211 */     if (state.item.isEmpty())
/*     */       return; 
/* 215 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotation(3.1415927F));
/* 216 */     state.item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\DisplayRenderer$ItemDisplayRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */