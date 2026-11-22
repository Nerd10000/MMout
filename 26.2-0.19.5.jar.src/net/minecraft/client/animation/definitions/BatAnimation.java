/*     */ package net.minecraft.client.animation.definitions;
/*     */ 
/*     */ import net.minecraft.client.animation.AnimationChannel;
/*     */ import net.minecraft.client.animation.AnimationDefinition;
/*     */ import net.minecraft.client.animation.Keyframe;
/*     */ import net.minecraft.client.animation.KeyframeAnimations;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class BatAnimation {
/*  11 */   public static final AnimationDefinition BAT_RESTING = AnimationDefinition.Builder.withLength(0.5F)
/*  12 */     .looping()
/*  13 */     .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(180.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("feet", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_wing", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_wing_tip", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -120.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("left_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("left_wing", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("left_wing_tip", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 120.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).build();
/*     */   
/*  48 */   public static final AnimationDefinition BAT_FLYING = AnimationDefinition.Builder.withLength(0.5F)
/*  49 */     .looping()
/*  50 */     .addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  52 */             (Vector3fc)KeyframeAnimations.degreeVec(20.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  53 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("head", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  57 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  58 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  59 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  60 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  61 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  65 */             (Vector3fc)KeyframeAnimations.degreeVec(52.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  66 */             (Vector3fc)KeyframeAnimations.degreeVec(40.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("body", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  70 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 2.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  71 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  72 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.4583F, 
/*  73 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  74 */             (Vector3fc)KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("feet", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  78 */             (Vector3fc)KeyframeAnimations.degreeVec(-21.25F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  79 */             (Vector3fc)KeyframeAnimations.degreeVec(-12.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  80 */             (Vector3fc)KeyframeAnimations.degreeVec(10.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 85.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  84 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -55.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  85 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 50.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  86 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 70.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  87 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 85.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("right_wing_tip", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0417F, 
/*  91 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 65.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/*  92 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -135.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/*  93 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 10.5F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("left_wing", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -85.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.125F, 
/*  97 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 55.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.25F, 
/*  98 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -50.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.375F, 
/*  99 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -70.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 100 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -85.0F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).addAnimation("left_wing_tip", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe[] { new Keyframe(0.0F, (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -10.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.0417F, 
/* 104 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -65.5F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.2083F, 
/* 105 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, 135.0F, 0.0F), AnimationChannel.Interpolations.LINEAR), new Keyframe(0.5F, 
/* 106 */             (Vector3fc)KeyframeAnimations.degreeVec(0.0F, -10.5F, 0.0F), AnimationChannel.Interpolations.LINEAR) })).build();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\animation\definitions\BatAnimation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */