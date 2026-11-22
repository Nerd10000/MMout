/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.equine.BabyHorseModel;
/*    */ import net.minecraft.client.model.animal.equine.EquineSaddleModel;
/*    */ import net.minecraft.client.model.animal.equine.HorseModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.HorseMarkingLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EquineRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HorseRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*    */ import net.minecraft.world.entity.animal.equine.Horse;
/*    */ import net.minecraft.world.entity.animal.equine.Variant;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class HorseRenderer extends AbstractHorseRenderer<Horse, HorseRenderState, HorseModel> {
/* 19 */   private static final Map<Variant, HorseTextures> LOCATION_BY_VARIANT = Maps.newEnumMap(Map.of(Variant.WHITE, new HorseTextures(
/* 20 */           Identifier.withDefaultNamespace("textures/entity/horse/horse_white.png"), Identifier.withDefaultNamespace("textures/entity/horse/horse_white_baby.png")), Variant.CREAMY, new HorseTextures(
/* 21 */           Identifier.withDefaultNamespace("textures/entity/horse/horse_creamy.png"), Identifier.withDefaultNamespace("textures/entity/horse/horse_creamy_baby.png")), Variant.CHESTNUT, new HorseTextures(
/* 22 */           Identifier.withDefaultNamespace("textures/entity/horse/horse_chestnut.png"), Identifier.withDefaultNamespace("textures/entity/horse/horse_chestnut_baby.png")), Variant.BROWN, new HorseTextures(
/* 23 */           Identifier.withDefaultNamespace("textures/entity/horse/horse_brown.png"), Identifier.withDefaultNamespace("textures/entity/horse/horse_brown_baby.png")), Variant.BLACK, new HorseTextures(
/* 24 */           Identifier.withDefaultNamespace("textures/entity/horse/horse_black.png"), Identifier.withDefaultNamespace("textures/entity/horse/horse_black_baby.png")), Variant.GRAY, new HorseTextures(
/* 25 */           Identifier.withDefaultNamespace("textures/entity/horse/horse_gray.png"), Identifier.withDefaultNamespace("textures/entity/horse/horse_gray_baby.png")), Variant.DARK_BROWN, new HorseTextures(
/* 26 */           Identifier.withDefaultNamespace("textures/entity/horse/horse_darkbrown.png"), Identifier.withDefaultNamespace("textures/entity/horse/horse_darkbrown_baby.png"))));
/*    */   
/*    */   public HorseRenderer(EntityRendererProvider.Context context) {
/* 30 */     super(context, new HorseModel(context.bakeLayer(ModelLayers.HORSE)), new BabyHorseModel(context.bakeLayer(ModelLayers.HORSE_BABY)));
/* 32 */     addLayer((RenderLayer<HorseRenderState, HorseModel>)new HorseMarkingLayer(this));
/* 33 */     addLayer((RenderLayer<HorseRenderState, HorseModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.HORSE_BODY, state -> state.bodyArmorItem, (EntityModel)new HorseModel(context
/*    */             
/* 36 */             .bakeLayer(ModelLayers.HORSE_ARMOR)), null, 2));
/* 40 */     addLayer((RenderLayer<HorseRenderState, HorseModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.HORSE_SADDLE, state -> state.saddle, (EntityModel)new EquineSaddleModel(context
/*    */             
/* 43 */             .bakeLayer(ModelLayers.HORSE_SADDLE)), null, 2));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(HorseRenderState state) {
/* 51 */     HorseTextures variant = LOCATION_BY_VARIANT.get(state.variant);
/* 52 */     return state.isBaby ? variant.baby : variant.adult;
/*    */   }
/*    */   
/*    */   public HorseRenderState createRenderState() {
/* 57 */     return new HorseRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Horse entity, HorseRenderState state, float partialTicks) {
/* 62 */     super.extractRenderState(entity, state, partialTicks);
/* 63 */     state.variant = entity.getVariant();
/* 64 */     state.markings = entity.getMarkings();
/* 65 */     state.bodyArmorItem = entity.getBodyArmorItem().copy();
/*    */   }
/*    */   
/*    */   private static final class HorseTextures extends Record {
/*    */     private final Identifier adult;
/*    */     
/*    */     private final Identifier baby;
/*    */     
/*    */     private HorseTextures(Identifier adult, Identifier baby) {
/* 68 */       this.adult = adult;
/* 68 */       this.baby = baby;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/HorseRenderer$HorseTextures;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/HorseRenderer$HorseTextures;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/HorseRenderer$HorseTextures;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/HorseRenderer$HorseTextures;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/HorseRenderer$HorseTextures;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #68	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/entity/HorseRenderer$HorseTextures;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Identifier adult() {
/* 68 */       return this.adult;
/*    */     }
/*    */     
/*    */     public Identifier baby() {
/* 68 */       return this.baby;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\HorseRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */