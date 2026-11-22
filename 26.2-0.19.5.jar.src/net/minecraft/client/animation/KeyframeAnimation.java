/*     */ package net.minecraft.client.animation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ public class KeyframeAnimation {
/*     */   private final AnimationDefinition definition;
/*     */   
/*     */   private final List<Entry> entries;
/*     */   
/*     */   private KeyframeAnimation(AnimationDefinition definition, List<Entry> entries) {
/*  21 */     this.definition = definition;
/*  22 */     this.entries = entries;
/*     */   }
/*     */   
/*     */   static KeyframeAnimation bake(ModelPart root, AnimationDefinition definition) {
/*  27 */     List<Entry> entries = new ArrayList<>();
/*  28 */     Function<String, ModelPart> partLookup = root.createPartLookup();
/*  30 */     for (Map.Entry<String, List<AnimationChannel>> entry : definition.boneAnimations().entrySet()) {
/*  31 */       String partName = entry.getKey();
/*  32 */       List<AnimationChannel> channels = entry.getValue();
/*  34 */       ModelPart part = partLookup.apply(partName);
/*  35 */       if (part == null)
/*  36 */         throw new IllegalArgumentException("Cannot animate " + partName + ", which does not exist in model"); 
/*  39 */       for (AnimationChannel channel : channels)
/*  40 */         entries.add(new Entry(part, channel.target(), channel.keyframes())); 
/*     */     } 
/*  44 */     return new KeyframeAnimation(definition, List.copyOf(entries));
/*     */   }
/*     */   
/*     */   public void applyStatic() {
/*  48 */     apply(0L, 1.0F);
/*     */   }
/*     */   
/*     */   public void applyWalk(float animationPos, float animationSpeed, float speedFactor, float scaleFactor) {
/*  52 */     long time = (long)(animationPos * 50.0F * speedFactor);
/*  53 */     float scale = Math.min(animationSpeed * scaleFactor, 1.0F);
/*  54 */     apply(time, scale);
/*     */   }
/*     */   
/*     */   public void apply(AnimationState animationState, float currentTime) {
/*  58 */     apply(animationState, currentTime, 1.0F);
/*     */   }
/*     */   
/*     */   public void apply(AnimationState animationState, float currentTime, float speedFactor) {
/*  62 */     animationState.ifStarted(state -> apply((long)((float)state.getTimeInMillis(currentTime) * speedFactor), 1.0F));
/*     */   }
/*     */   
/*     */   public void apply(long millisSinceStart, float targetScale) {
/*  68 */     float secondsSinceStart = getElapsedSeconds(millisSinceStart);
/*  69 */     Vector3f scratchVector = new Vector3f();
/*  70 */     for (Entry entry : this.entries)
/*  71 */       entry.apply(secondsSinceStart, targetScale, scratchVector); 
/*     */   }
/*     */   
/*     */   private float getElapsedSeconds(long millisSinceStart) {
/*  76 */     float secondsSinceStart = (float)millisSinceStart / 1000.0F;
/*  77 */     return this.definition.looping() ? (secondsSinceStart % this.definition.lengthInSeconds()) : secondsSinceStart;
/*     */   }
/*     */   
/*     */   private static final class Entry extends Record {
/*     */     private final ModelPart part;
/*     */     
/*     */     private final AnimationChannel.Target target;
/*     */     
/*     */     private final Keyframe[] keyframes;
/*     */     
/*     */     private Entry(ModelPart part, AnimationChannel.Target target, Keyframe[] keyframes) {
/*  81 */       this.part = part;
/*  81 */       this.target = target;
/*  81 */       this.keyframes = keyframes;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/animation/KeyframeAnimation$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #81	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/animation/KeyframeAnimation$Entry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/animation/KeyframeAnimation$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #81	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/animation/KeyframeAnimation$Entry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/animation/KeyframeAnimation$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #81	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/animation/KeyframeAnimation$Entry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ModelPart part() {
/*  81 */       return this.part;
/*     */     }
/*     */     
/*     */     public AnimationChannel.Target target() {
/*  81 */       return this.target;
/*     */     }
/*     */     
/*     */     public Keyframe[] keyframes() {
/*  81 */       return this.keyframes;
/*     */     }
/*     */     
/*     */     public void apply(float secondsSinceStart, float targetScale, Vector3f scratchVector) {
/*     */       float lerpAlpha;
/*  88 */       int prev = Math.max(0, Mth.binarySearch(0, this.keyframes.length, i -> (secondsSinceStart <= this.keyframes[i].timestamp())) - 1);
/*  89 */       int next = Math.min(this.keyframes.length - 1, prev + 1);
/*  91 */       Keyframe previousFrame = this.keyframes[prev];
/*  92 */       Keyframe nextFrame = this.keyframes[next];
/*  94 */       float keyframeTimeDelta = secondsSinceStart - previousFrame.timestamp();
/*  96 */       if (next != prev) {
/*  97 */         lerpAlpha = Mth.clamp(keyframeTimeDelta / (nextFrame.timestamp() - previousFrame.timestamp()), 0.0F, 1.0F);
/*     */       } else {
/*  99 */         lerpAlpha = 0.0F;
/*     */       } 
/* 102 */       nextFrame.interpolation().apply(scratchVector, lerpAlpha, this.keyframes, prev, next, targetScale);
/* 103 */       this.target.apply(this.part, scratchVector);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\animation\KeyframeAnimation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */