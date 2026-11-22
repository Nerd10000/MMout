/*    */ package net.minecraft.client.resources.metadata.animation;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class AnimationMetadataSection extends Record {
/*    */   private final Optional<List<AnimationFrame>> frames;
/*    */   
/*    */   private final Optional<Integer> frameWidth;
/*    */   
/*    */   private final Optional<Integer> frameHeight;
/*    */   
/*    */   private final int defaultFrameTime;
/*    */   
/*    */   private final boolean interpolatedFrames;
/*    */   
/*    */   public static final Codec<AnimationMetadataSection> CODEC;
/*    */   
/*    */   public AnimationMetadataSection(Optional<List<AnimationFrame>> frames, Optional<Integer> frameWidth, Optional<Integer> frameHeight, int defaultFrameTime, boolean interpolatedFrames) {
/* 11 */     this.frames = frames;
/* 11 */     this.frameWidth = frameWidth;
/* 11 */     this.frameHeight = frameHeight;
/* 11 */     this.defaultFrameTime = defaultFrameTime;
/* 11 */     this.interpolatedFrames = interpolatedFrames;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/animation/AnimationMetadataSection;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<List<AnimationFrame>> frames() {
/* 11 */     return this.frames;
/*    */   }
/*    */   
/*    */   public Optional<Integer> frameWidth() {
/* 11 */     return this.frameWidth;
/*    */   }
/*    */   
/*    */   public Optional<Integer> frameHeight() {
/* 11 */     return this.frameHeight;
/*    */   }
/*    */   
/*    */   public int defaultFrameTime() {
/* 11 */     return this.defaultFrameTime;
/*    */   }
/*    */   
/*    */   public boolean interpolatedFrames() {
/* 11 */     return this.interpolatedFrames;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)AnimationFrame.CODEC.listOf().optionalFieldOf("frames").forGetter(AnimationMetadataSection::frames), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("width").forGetter(AnimationMetadataSection::frameWidth), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("height").forGetter(AnimationMetadataSection::frameHeight), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("frametime", Integer.valueOf(1)).forGetter(AnimationMetadataSection::defaultFrameTime), (App)Codec.BOOL.optionalFieldOf("interpolate", Boolean.valueOf(false)).forGetter(AnimationMetadataSection::interpolatedFrames)).apply((Applicative)i, AnimationMetadataSection::new));
/*    */   }
/*    */   
/* 26 */   public static final MetadataSectionType<AnimationMetadataSection> TYPE = new MetadataSectionType("animation", CODEC);
/*    */   
/*    */   public FrameSize calculateFrameSize(int spriteWidth, int spriteHeight) {
/* 29 */     if (this.frameWidth.isPresent()) {
/* 30 */       if (this.frameHeight.isPresent())
/* 31 */         return new FrameSize(((Integer)this.frameWidth.get()).intValue(), ((Integer)this.frameHeight.get()).intValue()); 
/* 35 */       return new FrameSize(((Integer)this.frameWidth.get()).intValue(), spriteHeight);
/*    */     } 
/* 38 */     if (this.frameHeight.isPresent())
/* 40 */       return new FrameSize(spriteWidth, ((Integer)this.frameHeight.get()).intValue()); 
/* 44 */     int minDimension = Math.min(spriteWidth, spriteHeight);
/* 45 */     return new FrameSize(minDimension, minDimension);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\metadata\animation\AnimationMetadataSection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */