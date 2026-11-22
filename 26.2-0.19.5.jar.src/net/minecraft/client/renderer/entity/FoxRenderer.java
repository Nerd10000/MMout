/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import java.util.EnumMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.animal.fox.AdultFoxModel;
/*    */ import net.minecraft.client.model.animal.fox.BabyFoxModel;
/*    */ import net.minecraft.client.model.animal.fox.FoxModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.FoxHeldItemLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.FoxRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.fox.Fox;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class FoxRenderer extends AgeableMobRenderer<Fox, FoxRenderState, FoxModel> {
/* 20 */   private static final Identifier RED_FOX_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fox/fox.png");
/*    */   
/* 21 */   private static final Identifier RED_FOX_SLEEP_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fox/fox_sleep.png");
/*    */   
/* 22 */   private static final Identifier SNOW_FOX_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fox/fox_snow.png");
/*    */   
/* 23 */   private static final Identifier SNOW_FOX_SLEEP_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fox/fox_snow_sleep.png");
/*    */   
/* 25 */   private static final Identifier BABY_RED_FOX_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fox/fox_baby.png");
/*    */   
/* 26 */   private static final Identifier BABY_RED_FOX_SLEEP_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fox/fox_sleep_baby.png");
/*    */   
/* 27 */   private static final Identifier BABY_SNOW_FOX_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fox/fox_snow_baby.png");
/*    */   
/* 28 */   private static final Identifier BABY_SNOW_FOX_SLEEP_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fox/fox_snow_sleep_baby.png");
/*    */   
/* 30 */   private static final EnumMap<Fox.Variant, FoxTexturesByState> TEXTURES_BY_VARIANT = Maps.newEnumMap(Map.of(Fox.Variant.RED, new FoxTexturesByState(new FoxTexturesByAge(RED_FOX_TEXTURE, BABY_RED_FOX_TEXTURE), new FoxTexturesByAge(RED_FOX_SLEEP_TEXTURE, BABY_RED_FOX_SLEEP_TEXTURE)), Fox.Variant.SNOW, new FoxTexturesByState(new FoxTexturesByAge(SNOW_FOX_TEXTURE, BABY_SNOW_FOX_TEXTURE), new FoxTexturesByAge(SNOW_FOX_SLEEP_TEXTURE, BABY_SNOW_FOX_SLEEP_TEXTURE))));
/*    */   
/*    */   public FoxRenderer(EntityRendererProvider.Context context) {
/* 42 */     super(context, new AdultFoxModel(context.bakeLayer(ModelLayers.FOX)), new BabyFoxModel(context.bakeLayer(ModelLayers.FOX_BABY)), 0.4F);
/* 44 */     addLayer((RenderLayer<FoxRenderState, FoxModel>)new FoxHeldItemLayer(this));
/*    */   }
/*    */   
/*    */   protected void setupRotations(FoxRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 49 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/* 51 */     if (state.isPouncing || state.isFaceplanted)
/* 52 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-state.xRot)); 
/*    */   }
/*    */   
/*    */   public Identifier getTextureLocation(FoxRenderState state) {
/* 58 */     FoxTexturesByState byState = TEXTURES_BY_VARIANT.get(state.variant);
/* 59 */     if (byState == null)
/* 60 */       return RED_FOX_TEXTURE; 
/* 63 */     FoxTexturesByAge ageTextures = state.isSleeping ? byState.sleeping() : byState.idle();
/* 64 */     return state.isBaby ? ageTextures.baby() : ageTextures.adult();
/*    */   }
/*    */   
/*    */   public FoxRenderState createRenderState() {
/* 69 */     return new FoxRenderState();
/*    */   }
/*    */   
/*    */   public void extractRenderState(Fox entity, FoxRenderState state, float partialTicks) {
/* 74 */     super.extractRenderState(entity, state, partialTicks);
/* 75 */     HoldingEntityRenderState.extractHoldingEntityRenderState((LivingEntity)entity, (HoldingEntityRenderState)state, this.itemModelResolver);
/* 76 */     state.headRollAngle = entity.getHeadRollAngle(partialTicks);
/* 77 */     state.isCrouching = entity.isCrouching();
/* 78 */     state.crouchAmount = entity.getCrouchAmount(partialTicks);
/* 79 */     state.isSleeping = entity.isSleeping();
/* 80 */     state.isSitting = entity.isSitting();
/* 81 */     state.isFaceplanted = entity.isFaceplanted();
/* 82 */     state.isPouncing = entity.isPouncing();
/* 83 */     state.variant = entity.getVariant();
/*    */   }
/*    */   
/*    */   private static final class FoxTexturesByAge extends Record {
/*    */     private final Identifier adult;
/*    */     
/*    */     private final Identifier baby;
/*    */     
/*    */     private FoxTexturesByAge(Identifier adult, Identifier baby) {
/* 86 */       this.adult = adult;
/* 86 */       this.baby = baby;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByAge;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #86	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByAge;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByAge;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #86	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByAge;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByAge;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #86	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByAge;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Identifier adult() {
/* 86 */       return this.adult;
/*    */     }
/*    */     
/*    */     public Identifier baby() {
/* 86 */       return this.baby;
/*    */     }
/*    */   }
/*    */   
/*    */   private static final class FoxTexturesByState extends Record {
/*    */     private final FoxRenderer.FoxTexturesByAge idle;
/*    */     
/*    */     private final FoxRenderer.FoxTexturesByAge sleeping;
/*    */     
/*    */     private FoxTexturesByState(FoxRenderer.FoxTexturesByAge idle, FoxRenderer.FoxTexturesByAge sleeping) {
/* 88 */       this.idle = idle;
/* 88 */       this.sleeping = sleeping;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByState;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #88	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByState;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByState;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #88	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByState;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByState;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #88	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/entity/FoxRenderer$FoxTexturesByState;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public FoxRenderer.FoxTexturesByAge idle() {
/* 88 */       return this.idle;
/*    */     }
/*    */     
/*    */     public FoxRenderer.FoxTexturesByAge sleeping() {
/* 88 */       return this.sleeping;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\FoxRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */