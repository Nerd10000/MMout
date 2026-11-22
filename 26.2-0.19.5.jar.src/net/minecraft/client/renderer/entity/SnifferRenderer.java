/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.animal.sniffer.SnifferModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SnifferRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class SnifferRenderer extends AgeableMobRenderer<Sniffer, SnifferRenderState, SnifferModel> {
/* 12 */   private static final Identifier SNIFFER_LOCATION = Identifier.withDefaultNamespace("textures/entity/sniffer/sniffer.png");
/*    */   
/* 13 */   private static final Identifier SNIFFLET_LOCATION = Identifier.withDefaultNamespace("textures/entity/sniffer/snifflet.png");
/*    */   
/*    */   public SnifferRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new SnifferModel(context.bakeLayer(ModelLayers.SNIFFER)), new SnifferModel(context.bakeLayer(ModelLayers.SNIFFER_BABY)), 1.1F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(SnifferRenderState state) {
/* 21 */     return state.isBaby ? SNIFFLET_LOCATION : SNIFFER_LOCATION;
/*    */   }
/*    */   
/*    */   public SnifferRenderState createRenderState() {
/* 26 */     return new SnifferRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Sniffer entity, SnifferRenderState state, float partialTicks) {
/* 31 */     super.extractRenderState(entity, state, partialTicks);
/* 32 */     state.isSearching = entity.isSearching();
/* 33 */     state.diggingAnimationState.copyFrom(entity.diggingAnimationState);
/* 34 */     state.sniffingAnimationState.copyFrom(entity.sniffingAnimationState);
/* 35 */     state.risingAnimationState.copyFrom(entity.risingAnimationState);
/* 36 */     state.feelingHappyAnimationState.copyFrom(entity.feelingHappyAnimationState);
/* 37 */     state.scentingAnimationState.copyFrom(entity.scentingAnimationState);
/*    */   }
/*    */   
/*    */   protected AABB getBoundingBoxForCulling(Sniffer entity) {
/* 43 */     return super.getBoundingBoxForCulling(entity).inflate(0.6000000238418579D);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\SnifferRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */