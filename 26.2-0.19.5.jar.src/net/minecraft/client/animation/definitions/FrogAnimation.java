/*     */ package net.minecraft.client.animation.definitions;
/*     */ 
/*     */ import net.minecraft.client.animation.AnimationChannel;
/*     */ import net.minecraft.client.animation.AnimationDefinition;
/*     */ import net.minecraft.client.animation.Keyframe;
/*     */ import net.minecraft.client.animation.KeyframeAnimations;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class FrogAnimation {
/*  12 */   public static final AnimationDefinition FROG_CROAK = AnimationDefinition.Builder.withLength(3.0F)
/*  13 */     .addAnimation("croaking_body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  15 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  16 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  17 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  18 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  19 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("croaking_body", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] { 
/*  22 */           new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.scaleVec(0.0D, 0.0D, 0.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  23 */             (Vector3fc)KeyframeAnimations.scaleVec(0.0D, 0.0D, 0.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/*  24 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  25 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5417F, 
/*  26 */             (Vector3fc)KeyframeAnimations.scaleVec(1.2999999523162842D, 2.0999999046325684D, 1.600000023841858D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/*  27 */             (Vector3fc)KeyframeAnimations.scaleVec(1.2999999523162842D, 2.0999999046325684D, 1.600000023841858D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7083F, 
/*  28 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.25F, 
/*  29 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.3333F, 
/*  30 */             (Vector3fc)KeyframeAnimations.scaleVec(1.2999999523162842D, 2.0999999046325684D, 1.600000023841858D), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.4167F, 
/*  31 */             (Vector3fc)KeyframeAnimations.scaleVec(1.2999999523162842D, 2.0999999046325684D, 1.600000023841858D), AnimationChannel.Interpolations.LINEAR), 
/*  31 */           new Keyframe(2.5F, 
/*  32 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.5833F, 
/*  33 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.6667F, 
/*  34 */             (Vector3fc)KeyframeAnimations.scaleVec(1.2999999523162842D, 2.0999999046325684D, 1.600000023841858D), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.875F, 
/*  35 */             (Vector3fc)KeyframeAnimations.scaleVec(1.2999999523162842D, 2.0999999046325684D, 1.600000023841858D), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.9583F, 
/*  36 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(3.0F, 
/*  37 */             (Vector3fc)KeyframeAnimations.scaleVec(0.0D, 0.0D, 0.0D), AnimationChannel.Interpolations.LINEAR) })).build();
/*     */   
/*  41 */   public static final AnimationDefinition FROG_WALK = AnimationDefinition.Builder.withLength(1.25F)
/*  42 */     .looping()
/*  43 */     .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  45 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, -2.67F, -7.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/*  46 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7917F, 
/*  47 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/*  48 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  49 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  53 */             (Vector3fc)KeyframeAnimations.posVec(-0.5F, -0.25F, -0.13F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/*  54 */             (Vector3fc)KeyframeAnimations.posVec(-0.5F, 0.1F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/*  55 */             (Vector3fc)KeyframeAnimations.posVec(0.5F, 1.0F, -0.11F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  56 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, -2.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  60 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  61 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/*  62 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/*  63 */             (Vector3fc)KeyframeAnimations.degreeVec(7.5F, 2.33F, 7.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  64 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.5F, 0.1F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  68 */             (Vector3fc)KeyframeAnimations.posVec(-0.5F, 1.0F, 0.12F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/*  69 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, -2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/*  70 */             (Vector3fc)KeyframeAnimations.posVec(0.5F, -0.25F, -0.13F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  71 */             (Vector3fc)KeyframeAnimations.posVec(0.5F, 0.1F, 2.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  75 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/*  76 */             (Vector3fc)KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/*  77 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7917F, 
/*  78 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  79 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, 1.2F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  83 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  84 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.0F, 1.06F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7917F, 
/*  85 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  86 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, 1.2F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-33.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0417F, 
/*  90 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  91 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7917F, 
/*  92 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/*  93 */             (Vector3fc)KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/*  94 */             (Vector3fc)KeyframeAnimations.degreeVec(-33.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.14F, 0.11F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/*  98 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, -1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.7917F, 
/*  99 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.1F, 2.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.125F, 
/* 100 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.0F, 0.95F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 101 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.14F, 0.11F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2917F, 
/* 105 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.5F, 0.33F, 7.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.625F, 
/* 106 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.9583F, 
/* 107 */             (Vector3fc)KeyframeAnimations.degreeVec(-7.5F, 0.33F, -7.5F), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.25F, 
/* 108 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).build();
/*     */   
/* 112 */   public static final AnimationDefinition FROG_JUMP = AnimationDefinition.Builder.withLength(0.5F)
/* 113 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 115 */             (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 119 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-56.14F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 123 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.14F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 127 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(-56.14F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 131 */             (Vector3fc)KeyframeAnimations.degreeVec(-56.14F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 135 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 139 */             (Vector3fc)KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 143 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 147 */             (Vector3fc)KeyframeAnimations.degreeVec(45.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 151 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).build();
/*     */   
/* 155 */   public static final AnimationDefinition FROG_TONGUE = AnimationDefinition.Builder.withLength(0.5F)
/* 156 */     .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/* 158 */             (Vector3fc)KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/* 159 */             (Vector3fc)KeyframeAnimations.degreeVec(-60.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 160 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/* 164 */             (Vector3fc)KeyframeAnimations.degreeVec(0.998F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/* 165 */             (Vector3fc)KeyframeAnimations.degreeVec(0.998F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 166 */             (Vector3fc)KeyframeAnimations.degreeVec(1.0F, 1.0F, 1.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("tongue", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0833F, 
/* 170 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/* 171 */             (Vector3fc)KeyframeAnimations.degreeVec(-18.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 172 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("tongue", new AnimationChannel(AnimationChannel.Targets.SCALE, new Keyframe[] { new Keyframe(0.0833F, (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.1667F, 
/* 176 */             (Vector3fc)KeyframeAnimations.scaleVec(0.5D, 1.0D, 5.0D), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4167F, 
/* 177 */             (Vector3fc)KeyframeAnimations.scaleVec(1.0D, 1.0D, 1.0D), AnimationChannel.Interpolations.LINEAR) })).build();
/*     */   
/* 181 */   public static final AnimationDefinition FROG_SWIM = AnimationDefinition.Builder.withLength(1.04167F)
/* 182 */     .looping()
/* 183 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.3333F, 
/* 185 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6667F, 
/* 186 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0417F, 
/* 187 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 191 */             (Vector3fc)KeyframeAnimations.degreeVec(45.0F, 22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6667F, 
/* 192 */             (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, -22.5F, -22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.875F, 
/* 193 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, -22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9583F, 
/* 194 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0417F, 
/* 195 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.64F, 2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 199 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.64F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6667F, 
/* 200 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.875F, 
/* 201 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.27F, -1.14F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9583F, 
/* 202 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.45F, 0.43F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0417F, 
/* 203 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.64F, 2.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(90.0F, -22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 207 */             (Vector3fc)KeyframeAnimations.degreeVec(45.0F, -22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6667F, 
/* 208 */             (Vector3fc)KeyframeAnimations.degreeVec(-22.5F, 22.5F, 22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.875F, 
/* 209 */             (Vector3fc)KeyframeAnimations.degreeVec(-45.0F, 22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9583F, 
/* 210 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 0.0F, -22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0417F, 
/* 211 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, -22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.64F, 2.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 215 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.64F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.6667F, 
/* 216 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.875F, 
/* 217 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.27F, -1.14F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9583F, 
/* 218 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.45F, 0.43F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0417F, 
/* 219 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -0.64F, 2.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 223 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 224 */             (Vector3fc)KeyframeAnimations.degreeVec(67.5F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7917F, 
/* 225 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9583F, 
/* 226 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0417F, 
/* 227 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(-2.5F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 231 */             (Vector3fc)KeyframeAnimations.posVec(-2.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 232 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7917F, 
/* 233 */             (Vector3fc)KeyframeAnimations.posVec(0.58F, 0.0F, -2.83F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9583F, 
/* 234 */             (Vector3fc)KeyframeAnimations.posVec(-2.5F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0417F, 
/* 235 */             (Vector3fc)KeyframeAnimations.posVec(-2.5F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 239 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 240 */             (Vector3fc)KeyframeAnimations.degreeVec(67.5F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7917F, 
/* 241 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9583F, 
/* 242 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0417F, 
/* 243 */             (Vector3fc)KeyframeAnimations.degreeVec(90.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(2.5F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.25F, 
/* 247 */             (Vector3fc)KeyframeAnimations.posVec(2.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.4583F, 
/* 248 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, -2.0F, -1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.7917F, 
/* 249 */             (Vector3fc)KeyframeAnimations.posVec(-0.58F, 0.0F, -2.83F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.9583F, 
/* 250 */             (Vector3fc)KeyframeAnimations.posVec(2.5F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0417F, 
/* 251 */             (Vector3fc)KeyframeAnimations.posVec(2.5F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM) })).build();
/*     */   
/* 255 */   public static final AnimationDefinition FROG_IDLE_WATER = AnimationDefinition.Builder.withLength(3.0F)
/* 256 */     .looping()
/* 257 */     .addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.625F, 
/* 259 */             (Vector3fc)KeyframeAnimations.degreeVec(-10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 260 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2083F, 
/* 264 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -45.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 265 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, -22.5F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2083F, 
/* 269 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 270 */             (Vector3fc)KeyframeAnimations.posVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2083F, 
/* 274 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 45.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 275 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 22.5F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(2.2083F, 
/* 279 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, -0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 280 */             (Vector3fc)KeyframeAnimations.posVec(1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(22.5F, -22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 284 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, -22.5F, -45.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 285 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, -22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 289 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 290 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 294 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 22.5F, 45.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 295 */             (Vector3fc)KeyframeAnimations.degreeVec(22.5F, 22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM) })).addAnimation("right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.0F, 
/* 299 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(3.0F, 
/* 300 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.CATMULLROM) })).build();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\animation\definitions\FrogAnimation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */