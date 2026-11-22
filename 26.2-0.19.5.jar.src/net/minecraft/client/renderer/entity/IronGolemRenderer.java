/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.golem.IronGolemModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*    */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*    */ import net.minecraft.client.renderer.entity.layers.IronGolemCrackinessLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.IronGolemFlowerLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class IronGolemRenderer extends MobRenderer<IronGolem, IronGolemRenderState, IronGolemModel> {
/* 17 */   public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
/*    */   
/* 19 */   private static final Identifier GOLEM_LOCATION = Identifier.withDefaultNamespace("textures/entity/iron_golem/iron_golem.png");
/*    */   
/*    */   private final BlockModelResolver blockModelResolver;
/*    */   
/*    */   public IronGolemRenderer(EntityRendererProvider.Context context) {
/* 24 */     super(context, new IronGolemModel(context.bakeLayer(ModelLayers.IRON_GOLEM)), 0.7F);
/* 25 */     this.blockModelResolver = context.getBlockModelResolver();
/* 26 */     addLayer((RenderLayer<IronGolemRenderState, IronGolemModel>)new IronGolemCrackinessLayer(this));
/* 27 */     addLayer((RenderLayer<IronGolemRenderState, IronGolemModel>)new IronGolemFlowerLayer(this));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(IronGolemRenderState state) {
/* 32 */     return GOLEM_LOCATION;
/*    */   }
/*    */   
/*    */   public IronGolemRenderState createRenderState() {
/* 37 */     return new IronGolemRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(IronGolem entity, IronGolemRenderState state, float partialTicks) {
/* 42 */     super.extractRenderState(entity, state, partialTicks);
/* 43 */     state.attackTicksRemaining = (entity.getAttackAnimationTick() > 0.0F) ? (entity.getAttackAnimationTick() - partialTicks) : 0.0F;
/* 44 */     state.offerFlowerTick = entity.getOfferFlowerTick();
/* 45 */     if (state.offerFlowerTick > 0) {
/* 46 */       this.blockModelResolver.update(state.flowerBlock, Blocks.POPPY.defaultBlockState(), BLOCK_DISPLAY_CONTEXT);
/*    */     } else {
/* 48 */       state.flowerBlock.clear();
/*    */     } 
/* 50 */     state.crackiness = entity.getCrackiness();
/*    */   }
/*    */   
/*    */   protected void setupRotations(IronGolemRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 55 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/* 56 */     if (state.walkAnimationSpeed < 0.01D)
/*    */       return; 
/* 60 */     float p = 13.0F;
/* 61 */     float wp = state.walkAnimationPos + 6.0F;
/* 62 */     float triangleWave = (Math.abs(wp % 13.0F - 6.5F) - 3.25F) / 3.25F;
/* 63 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(6.5F * triangleWave));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\IronGolemRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */