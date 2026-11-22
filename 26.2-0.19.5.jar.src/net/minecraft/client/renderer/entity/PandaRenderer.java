/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.model.animal.panda.BabyPandaModel;
/*     */ import net.minecraft.client.model.animal.panda.PandaModel;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.renderer.entity.layers.PandaHoldsItemLayer;
/*     */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.PandaRenderState;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.panda.Panda;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class PandaRenderer extends AgeableMobRenderer<Panda, PandaRenderState, PandaModel> {
/*  19 */   private static final Map<Panda.Gene, Identifier> TEXTURES = Maps.newEnumMap(Map.of(Panda.Gene.NORMAL, 
/*  20 */         Identifier.withDefaultNamespace("textures/entity/panda/panda.png"), Panda.Gene.LAZY, 
/*  21 */         Identifier.withDefaultNamespace("textures/entity/panda/panda_lazy.png"), Panda.Gene.WORRIED, 
/*  22 */         Identifier.withDefaultNamespace("textures/entity/panda/panda_worried.png"), Panda.Gene.PLAYFUL, 
/*  23 */         Identifier.withDefaultNamespace("textures/entity/panda/panda_playful.png"), Panda.Gene.BROWN, 
/*  24 */         Identifier.withDefaultNamespace("textures/entity/panda/panda_brown.png"), Panda.Gene.WEAK, 
/*  25 */         Identifier.withDefaultNamespace("textures/entity/panda/panda_weak.png"), Panda.Gene.AGGRESSIVE, 
/*  26 */         Identifier.withDefaultNamespace("textures/entity/panda/panda_aggressive.png")));
/*     */   
/*  29 */   private static final Map<Panda.Gene, Identifier> BABY_TEXTURES = Maps.newEnumMap(Map.of(Panda.Gene.NORMAL, 
/*  30 */         Identifier.withDefaultNamespace("textures/entity/panda/panda_baby.png"), Panda.Gene.LAZY, 
/*  31 */         Identifier.withDefaultNamespace("textures/entity/panda/lazy_panda_baby.png"), Panda.Gene.WORRIED, 
/*  32 */         Identifier.withDefaultNamespace("textures/entity/panda/worried_panda_baby.png"), Panda.Gene.PLAYFUL, 
/*  33 */         Identifier.withDefaultNamespace("textures/entity/panda/playful_panda_baby.png"), Panda.Gene.BROWN, 
/*  34 */         Identifier.withDefaultNamespace("textures/entity/panda/brown_panda_baby.png"), Panda.Gene.WEAK, 
/*  35 */         Identifier.withDefaultNamespace("textures/entity/panda/weak_panda_baby.png"), Panda.Gene.AGGRESSIVE, 
/*  36 */         Identifier.withDefaultNamespace("textures/entity/panda/aggressive_panda_baby.png")));
/*     */   
/*     */   public PandaRenderer(EntityRendererProvider.Context context) {
/*  40 */     super(context, new PandaModel(context.bakeLayer(ModelLayers.PANDA)), new BabyPandaModel(context.bakeLayer(ModelLayers.PANDA_BABY)), 0.9F);
/*  42 */     addLayer((RenderLayer<PandaRenderState, PandaModel>)new PandaHoldsItemLayer(this));
/*     */   }
/*     */   
/*     */   public Identifier getTextureLocation(PandaRenderState state) {
/*  47 */     Map<Panda.Gene, Identifier> textures = state.isBaby ? BABY_TEXTURES : TEXTURES;
/*  48 */     return textures.getOrDefault(state.variant, textures.get(Panda.Gene.NORMAL));
/*     */   }
/*     */   
/*     */   public PandaRenderState createRenderState() {
/*  53 */     return new PandaRenderState();
/*     */   }
/*     */   
/*     */   public void extractRenderState(Panda entity, PandaRenderState state, float partialTicks) {
/*  58 */     super.extractRenderState(entity, state, partialTicks);
/*  59 */     HoldingEntityRenderState.extractHoldingEntityRenderState((LivingEntity)entity, (HoldingEntityRenderState)state, this.itemModelResolver);
/*  60 */     state.variant = entity.getVariant();
/*  61 */     state.isUnhappy = (entity.getUnhappyCounter() > 0);
/*  62 */     state.isSneezing = entity.isSneezing();
/*  63 */     state.sneezeTime = entity.getSneezeCounter();
/*  64 */     state.isEating = entity.isEating();
/*  65 */     state.isScared = entity.isScared();
/*  66 */     state.isSitting = entity.isSitting();
/*  67 */     state.sitAmount = entity.getSitAmount(partialTicks);
/*  68 */     state.lieOnBackAmount = entity.getLieOnBackAmount(partialTicks);
/*  69 */     state.rollAmount = entity.isBaby() ? 0.0F : entity.getRollAmount(partialTicks);
/*  70 */     state.rollTime = (entity.rollCounter > 0) ? (entity.rollCounter + partialTicks) : 0.0F;
/*     */   }
/*     */   
/*     */   protected void setupRotations(PandaRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/*  75 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/*  77 */     if (state.rollTime > 0.0F) {
/*  78 */       float rollTransitionTime = Mth.frac(state.rollTime);
/*  79 */       int rollPos = Mth.floor(state.rollTime);
/*  80 */       int nextRollPos = rollPos + 1;
/*  82 */       float divider = 7.0F;
/*  83 */       float y = state.isBaby ? 0.3F : 0.8F;
/*  85 */       if (rollPos < 8.0F) {
/*  86 */         float thisAngle = 90.0F * rollPos / 7.0F;
/*  87 */         float nextAngle = 90.0F * nextRollPos / 7.0F;
/*  88 */         float angle = getAngle(thisAngle, nextAngle, nextRollPos, rollTransitionTime, 8.0F);
/*  90 */         poseStack.translate(0.0F, (y + 0.2F) * angle / 90.0F, 0.0F);
/*  91 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-angle));
/*  92 */       } else if (rollPos < 16.0F) {
/*  93 */         float internalRollCounter = (rollPos - 8.0F) / 7.0F;
/*  94 */         float thisAngle = 90.0F + 90.0F * internalRollCounter;
/*  95 */         float nextAngle = 90.0F + 90.0F * (nextRollPos - 8.0F) / 7.0F;
/*  96 */         float angle = getAngle(thisAngle, nextAngle, nextRollPos, rollTransitionTime, 16.0F);
/*  98 */         poseStack.translate(0.0F, y + 0.2F + (y - 0.2F) * (angle - 90.0F) / 90.0F, 0.0F);
/*  99 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-angle));
/* 100 */       } else if (rollPos < 24.0F) {
/* 101 */         float internalRollCounter = (rollPos - 16.0F) / 7.0F;
/* 102 */         float thisAngle = 180.0F + 90.0F * internalRollCounter;
/* 103 */         float nextAngle = 180.0F + 90.0F * (nextRollPos - 16.0F) / 7.0F;
/* 104 */         float angle = getAngle(thisAngle, nextAngle, nextRollPos, rollTransitionTime, 24.0F);
/* 106 */         poseStack.translate(0.0F, y + y * (270.0F - angle) / 90.0F, 0.0F);
/* 107 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-angle));
/* 108 */       } else if (rollPos < 32) {
/* 109 */         float internalRollCounter = (rollPos - 24.0F) / 7.0F;
/* 110 */         float thisAngle = 270.0F + 90.0F * internalRollCounter;
/* 111 */         float nextAngle = 270.0F + 90.0F * (nextRollPos - 24.0F) / 7.0F;
/* 112 */         float angle = getAngle(thisAngle, nextAngle, nextRollPos, rollTransitionTime, 32.0F);
/* 114 */         poseStack.translate(0.0F, y * (360.0F - angle) / 90.0F, 0.0F);
/* 115 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-angle));
/*     */       } 
/*     */     } 
/* 119 */     float sitAmount = state.sitAmount;
/* 120 */     if (sitAmount > 0.0F) {
/* 121 */       poseStack.translate(0.0F, 0.8F * sitAmount, 0.0F);
/* 122 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(Mth.lerp(sitAmount, state.xRot, state.xRot + 90.0F)));
/* 125 */       poseStack.translate(0.0F, -1.0F * sitAmount, 0.0F);
/* 127 */       if (state.isScared) {
/* 128 */         float shakeRot = (float)(Math.cos((state.ageInTicks * 1.25F)) * Math.PI * 0.05000000074505806D);
/* 130 */         poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(shakeRot));
/* 131 */         if (state.isBaby)
/* 132 */           poseStack.translate(0.0F, 0.8F, 0.55F); 
/*     */       } 
/*     */     } 
/* 137 */     float lieOnBackAmount = state.lieOnBackAmount;
/* 138 */     if (lieOnBackAmount > 0.0F) {
/* 139 */       float y = state.isBaby ? 0.5F : 1.3F;
/* 140 */       poseStack.translate(0.0F, y * lieOnBackAmount, 0.0F);
/* 141 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(Mth.lerp(lieOnBackAmount, state.xRot, state.xRot + 180.0F)));
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getAngle(float thisAngle, float nextAngle, int nextRollPos, float rollTransitionTime, float threshold) {
/* 146 */     if (nextRollPos < threshold)
/* 147 */       return Mth.lerp(rollTransitionTime, thisAngle, nextAngle); 
/* 149 */     return thisAngle;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\PandaRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */