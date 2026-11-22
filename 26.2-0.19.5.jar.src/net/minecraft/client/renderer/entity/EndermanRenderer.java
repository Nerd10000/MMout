/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.enderman.EndermanModel;
/*    */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*    */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*    */ import net.minecraft.client.renderer.entity.layers.CarriedBlockLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.EnderEyesLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EndermanRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.EnderMan;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class EndermanRenderer extends MobRenderer<EnderMan, EndermanRenderState, EndermanModel<EndermanRenderState>> {
/* 17 */   public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
/*    */   
/* 19 */   private static final Identifier ENDERMAN_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderman/enderman.png");
/*    */   
/* 21 */   private final RandomSource random = RandomSource.create();
/*    */   
/*    */   private final BlockModelResolver blockModelResolver;
/*    */   
/*    */   public EndermanRenderer(EntityRendererProvider.Context context) {
/* 25 */     super(context, new EndermanModel(context.bakeLayer(ModelLayers.ENDERMAN)), 0.5F);
/* 26 */     this.blockModelResolver = context.getBlockModelResolver();
/* 28 */     addLayer((RenderLayer<EndermanRenderState, EndermanModel<EndermanRenderState>>)new EnderEyesLayer(this));
/* 29 */     addLayer((RenderLayer<EndermanRenderState, EndermanModel<EndermanRenderState>>)new CarriedBlockLayer(this));
/*    */   }
/*    */   
/*    */   public Vec3 getRenderOffset(EndermanRenderState state) {
/* 34 */     Vec3 offset = super.getRenderOffset(state);
/* 35 */     if (state.isCreepy) {
/* 36 */       double d = 0.02D * state.scale;
/* 37 */       return offset.add(this.random.nextGaussian() * d, 0.0D, this.random.nextGaussian() * d);
/*    */     } 
/* 39 */     return offset;
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(EndermanRenderState state) {
/* 44 */     return ENDERMAN_LOCATION;
/*    */   }
/*    */   
/*    */   public EndermanRenderState createRenderState() {
/* 49 */     return new EndermanRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(EnderMan entity, EndermanRenderState state, float partialTicks) {
/* 54 */     super.extractRenderState(entity, state, partialTicks);
/* 55 */     HumanoidMobRenderer.extractHumanoidRenderState((LivingEntity)entity, (HumanoidRenderState)state, partialTicks, this.itemModelResolver);
/* 56 */     state.isCreepy = entity.isCreepy();
/* 57 */     BlockState carriedBlock = entity.getCarriedBlock();
/* 58 */     if (carriedBlock != null) {
/* 59 */       this.blockModelResolver.update(state.carriedBlock, carriedBlock, BLOCK_DISPLAY_CONTEXT);
/*    */     } else {
/* 61 */       state.carriedBlock.clear();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\EndermanRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */