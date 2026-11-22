/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.golem.SnowGolemModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*    */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SnowGolemHeadLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SnowGolemRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.golem.SnowGolem;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ 
/*    */ public class SnowGolemRenderer extends MobRenderer<SnowGolem, SnowGolemRenderState, SnowGolemModel> {
/* 14 */   public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
/*    */   
/* 16 */   private static final Identifier SNOW_GOLEM_LOCATION = Identifier.withDefaultNamespace("textures/entity/snow_golem/snow_golem.png");
/*    */   
/*    */   private final BlockModelResolver blockModelResolver;
/*    */   
/*    */   public SnowGolemRenderer(EntityRendererProvider.Context context) {
/* 21 */     super(context, new SnowGolemModel(context.bakeLayer(ModelLayers.SNOW_GOLEM)), 0.5F);
/* 22 */     this.blockModelResolver = context.getBlockModelResolver();
/* 24 */     addLayer((RenderLayer<SnowGolemRenderState, SnowGolemModel>)new SnowGolemHeadLayer(this));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(SnowGolemRenderState state) {
/* 29 */     return SNOW_GOLEM_LOCATION;
/*    */   }
/*    */   
/*    */   public SnowGolemRenderState createRenderState() {
/* 34 */     return new SnowGolemRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(SnowGolem entity, SnowGolemRenderState state, float partialTicks) {
/* 39 */     super.extractRenderState(entity, state, partialTicks);
/* 40 */     if (entity.hasPumpkin()) {
/* 41 */       this.blockModelResolver.update(state.headBlock, Blocks.CARVED_PUMPKIN.defaultBlockState(), BLOCK_DISPLAY_CONTEXT);
/*    */     } else {
/* 43 */       state.headBlock.clear();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\SnowGolemRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */