/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.axolotl.AdultAxolotlModel;
/*    */ import net.minecraft.client.model.animal.axolotl.BabyAxolotlModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.AxolotlRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*    */ 
/*    */ public class AxolotlRenderer extends AgeableMobRenderer<Axolotl, AxolotlRenderState, EntityModel<AxolotlRenderState>> {
/*    */   private static final Map<Axolotl.Variant, AxolotlTextures> TEXTURE_BY_TYPE;
/*    */   
/*    */   static {
/* 17 */     TEXTURE_BY_TYPE = (Map<Axolotl.Variant, AxolotlTextures>)Util.make(Maps.newHashMap(), map -> {
/*    */           for (Axolotl.Variant variant : Axolotl.Variant.values()) {
/*    */             AxolotlTextures textures = new AxolotlTextures(Identifier.withDefaultNamespace(String.format(Locale.ROOT, "textures/entity/axolotl/axolotl_%s.png", new Object[] { variant.getName() })), Identifier.withDefaultNamespace(String.format(Locale.ROOT, "textures/entity/axolotl/axolotl_%s_baby.png", new Object[] { variant.getName() })));
/*    */             map.put(variant, textures);
/*    */           } 
/*    */         });
/*    */   }
/*    */   
/*    */   public AxolotlRenderer(EntityRendererProvider.Context context) {
/* 26 */     super(context, new AdultAxolotlModel(context.bakeLayer(ModelLayers.AXOLOTL)), new BabyAxolotlModel(context.bakeLayer(ModelLayers.AXOLOTL_BABY)), 0.5F);
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(AxolotlRenderState state) {
/* 31 */     AxolotlTextures textures = TEXTURE_BY_TYPE.get(state.variant);
/* 32 */     return state.isBaby ? textures.baby : textures.adult;
/*    */   }
/*    */   
/*    */   public AxolotlRenderState createRenderState() {
/* 37 */     return new AxolotlRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Axolotl entity, AxolotlRenderState state, float partialTicks) {
/* 42 */     super.extractRenderState(entity, state, partialTicks);
/* 43 */     state.variant = entity.getVariant();
/* 44 */     state.playingDeadFactor = entity.playingDeadAnimator.getFactor(partialTicks);
/* 45 */     state.inWaterFactor = entity.inWaterAnimator.getFactor(partialTicks);
/* 46 */     state.onGroundFactor = entity.onGroundAnimator.getFactor(partialTicks);
/* 47 */     state.movingFactor = entity.movingAnimator.getFactor(partialTicks);
/* 48 */     state.swimAnimation.copyFrom(entity.swimAnimationState);
/* 49 */     state.walkAnimationState.copyFrom(entity.walkAnimationState);
/* 50 */     state.walkUnderWaterAnimationState.copyFrom(entity.walkUnderWaterAnimationState);
/* 51 */     state.idleOnGroundAnimationState.copyFrom(entity.idleOnGroundAnimationState);
/* 52 */     state.idleUnderWaterOnGroundAnimationState.copyFrom(entity.idleUnderWaterOnGroundAnimationState);
/* 53 */     state.idleUnderWaterAnimationState.copyFrom(entity.idleUnderWaterAnimationState);
/* 54 */     state.playDeadAnimationState.copyFrom(entity.playDeadAnimationState);
/*    */   }
/*    */   
/*    */   private static final class AxolotlTextures extends Record {
/*    */     private final Identifier adult;
/*    */     
/*    */     private final Identifier baby;
/*    */     
/*    */     private AxolotlTextures(Identifier adult, Identifier baby) {
/* 57 */       this.adult = adult;
/* 57 */       this.baby = baby;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/AxolotlRenderer$AxolotlTextures;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/AxolotlRenderer$AxolotlTextures;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/AxolotlRenderer$AxolotlTextures;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/AxolotlRenderer$AxolotlTextures;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/AxolotlRenderer$AxolotlTextures;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #57	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/entity/AxolotlRenderer$AxolotlTextures;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Identifier adult() {
/* 57 */       return this.adult;
/*    */     }
/*    */     
/*    */     public Identifier baby() {
/* 57 */       return this.baby;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\AxolotlRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */