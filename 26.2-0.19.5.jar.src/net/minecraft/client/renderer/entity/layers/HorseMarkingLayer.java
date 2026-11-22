/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.equine.HorseModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HorseRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.equine.Markings;
/*    */ 
/*    */ public class HorseMarkingLayer extends RenderLayer<HorseRenderState, HorseModel> {
/* 17 */   private static final Identifier INVISIBLE_TEXTURE = Identifier.withDefaultNamespace("invisible");
/*    */   
/* 19 */   private static final Map<Markings, HorseMarkingTextures> LOCATION_BY_MARKINGS = Maps.newEnumMap(Map.of(Markings.NONE, new HorseMarkingTextures(INVISIBLE_TEXTURE, INVISIBLE_TEXTURE), Markings.WHITE, new HorseMarkingTextures(
/*    */           
/* 21 */           Identifier.withDefaultNamespace("textures/entity/horse/horse_markings_white.png"), Identifier.withDefaultNamespace("textures/entity/horse/horse_markings_white_baby.png")), Markings.WHITE_FIELD, new HorseMarkingTextures(
/* 22 */           Identifier.withDefaultNamespace("textures/entity/horse/horse_markings_whitefield.png"), Identifier.withDefaultNamespace("textures/entity/horse/horse_markings_whitefield_baby.png")), Markings.WHITE_DOTS, new HorseMarkingTextures(
/* 23 */           Identifier.withDefaultNamespace("textures/entity/horse/horse_markings_whitedots.png"), Identifier.withDefaultNamespace("textures/entity/horse/horse_markings_whitedots_baby.png")), Markings.BLACK_DOTS, new HorseMarkingTextures(
/* 24 */           Identifier.withDefaultNamespace("textures/entity/horse/horse_markings_blackdots.png"), Identifier.withDefaultNamespace("textures/entity/horse/horse_markings_blackdots_baby.png"))));
/*    */   
/*    */   public HorseMarkingLayer(RenderLayerParent<HorseRenderState, HorseModel> renderer) {
/* 28 */     super(renderer);
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, HorseRenderState state, float yRot, float xRot) {
/* 33 */     HorseMarkingTextures variant = LOCATION_BY_MARKINGS.get(state.markings);
/* 34 */     Identifier texture = state.isBaby ? variant.baby : variant.adult;
/* 36 */     if (texture == INVISIBLE_TEXTURE || state.isInvisible)
/*    */       return; 
/* 40 */     submitNodeCollector.order(1).submitModel((Model)getParentModel(), state, poseStack, RenderTypes.entityTranslucent(texture), lightCoords, LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F), state.outlineColor, null);
/*    */   }
/*    */   
/*    */   private static final class HorseMarkingTextures extends Record {
/*    */     private final Identifier adult;
/*    */     
/*    */     private final Identifier baby;
/*    */     
/*    */     private HorseMarkingTextures(Identifier adult, Identifier baby) {
/* 43 */       this.adult = adult;
/* 43 */       this.baby = baby;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/layers/HorseMarkingLayer$HorseMarkingTextures;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/HorseMarkingLayer$HorseMarkingTextures;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/layers/HorseMarkingLayer$HorseMarkingTextures;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/layers/HorseMarkingLayer$HorseMarkingTextures;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/layers/HorseMarkingLayer$HorseMarkingTextures;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #43	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/entity/layers/HorseMarkingLayer$HorseMarkingTextures;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Identifier adult() {
/* 43 */       return this.adult;
/*    */     }
/*    */     
/*    */     public Identifier baby() {
/* 43 */       return this.baby;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\HorseMarkingLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */