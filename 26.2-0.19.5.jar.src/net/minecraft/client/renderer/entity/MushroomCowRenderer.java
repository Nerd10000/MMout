/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.animal.cow.BabyCowModel;
/*    */ import net.minecraft.client.model.animal.cow.CowModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*    */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*    */ import net.minecraft.client.renderer.entity.layers.MushroomCowMushroomLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.cow.MushroomCow;
/*    */ 
/*    */ public class MushroomCowRenderer extends AgeableMobRenderer<MushroomCow, MushroomCowRenderState, CowModel> {
/* 18 */   public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
/*    */   
/*    */   private static final Map<MushroomCow.Variant, MushroomCowTexture> TEXTURES;
/*    */   
/*    */   private final BlockModelResolver blockModelResolver;
/*    */   
/*    */   static {
/* 20 */     TEXTURES = (Map<MushroomCow.Variant, MushroomCowTexture>)Util.make(Maps.newHashMap(), map -> {
/*    */           map.put(MushroomCow.Variant.BROWN, new MushroomCowTexture(Identifier.withDefaultNamespace("textures/entity/cow/mooshroom_brown.png"), Identifier.withDefaultNamespace("textures/entity/cow/mooshroom_brown_baby.png")));
/*    */           map.put(MushroomCow.Variant.RED, new MushroomCowTexture(Identifier.withDefaultNamespace("textures/entity/cow/mooshroom_red.png"), Identifier.withDefaultNamespace("textures/entity/cow/mooshroom_red_baby.png")));
/*    */         });
/*    */   }
/*    */   
/*    */   public MushroomCowRenderer(EntityRendererProvider.Context context) {
/* 28 */     super(context, new CowModel(context.bakeLayer(ModelLayers.MOOSHROOM)), new BabyCowModel(context.bakeLayer(ModelLayers.MOOSHROOM_BABY)), 0.7F);
/* 29 */     this.blockModelResolver = context.getBlockModelResolver();
/* 30 */     addLayer((RenderLayer<MushroomCowRenderState, CowModel>)new MushroomCowMushroomLayer(this));
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(MushroomCowRenderState state) {
/* 35 */     return state.isBaby ? ((MushroomCowTexture)TEXTURES.get(state.variant)).baby : ((MushroomCowTexture)TEXTURES.get(state.variant)).adult;
/*    */   }
/*    */   
/*    */   public MushroomCowRenderState createRenderState() {
/* 40 */     return new MushroomCowRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(MushroomCow entity, MushroomCowRenderState state, float partialTicks) {
/* 45 */     super.extractRenderState(entity, state, partialTicks);
/* 46 */     state.variant = entity.getVariant();
/* 47 */     this.blockModelResolver.update(state.mushroomModel, state.variant.getBlockState(), BLOCK_DISPLAY_CONTEXT);
/*    */   }
/*    */   
/*    */   private static final class MushroomCowTexture extends Record {
/*    */     private final Identifier adult;
/*    */     
/*    */     private final Identifier baby;
/*    */     
/*    */     private MushroomCowTexture(Identifier adult, Identifier baby) {
/* 50 */       this.adult = adult;
/* 50 */       this.baby = baby;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/MushroomCowRenderer$MushroomCowTexture;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #50	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/MushroomCowRenderer$MushroomCowTexture;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/MushroomCowRenderer$MushroomCowTexture;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #50	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/MushroomCowRenderer$MushroomCowTexture;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/MushroomCowRenderer$MushroomCowTexture;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #50	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/entity/MushroomCowRenderer$MushroomCowTexture;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Identifier adult() {
/* 50 */       return this.adult;
/*    */     }
/*    */     
/*    */     public Identifier baby() {
/* 50 */       return this.baby;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\MushroomCowRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */