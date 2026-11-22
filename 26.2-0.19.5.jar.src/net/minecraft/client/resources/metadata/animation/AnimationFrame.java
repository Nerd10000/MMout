/*    */ package net.minecraft.client.resources.metadata.animation;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class AnimationFrame extends Record {
/*    */   private final int index;
/*    */   
/*    */   private final Optional<Integer> time;
/*    */   
/*    */   public static final Codec<AnimationFrame> FULL_CODEC;
/*    */   
/*    */   public static final Codec<AnimationFrame> CODEC;
/*    */   
/*    */   public AnimationFrame(int index, Optional<Integer> time) {
/* 10 */     this.index = index;
/* 10 */     this.time = time;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/animation/AnimationFrame;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/AnimationFrame;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/animation/AnimationFrame;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/AnimationFrame;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/animation/AnimationFrame;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/animation/AnimationFrame;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int index() {
/* 10 */     return this.index;
/*    */   }
/*    */   
/*    */   public Optional<Integer> time() {
/* 10 */     return this.time;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("index").forGetter(AnimationFrame::index), (App)ExtraCodecs.POSITIVE_INT.optionalFieldOf("time").forGetter(AnimationFrame::time)).apply((Applicative)i, AnimationFrame::new));
/* 22 */     CODEC = Codec.either(ExtraCodecs.NON_NEGATIVE_INT, FULL_CODEC).xmap(either -> (AnimationFrame)either.map(AnimationFrame::new, ()), frame -> frame.time.isPresent() ? Either.right(frame) : Either.left(Integer.valueOf(frame.index)));
/*    */   }
/*    */   
/*    */   public AnimationFrame(int index) {
/* 28 */     this(index, Optional.empty());
/*    */   }
/*    */   
/*    */   public int timeOr(int defaultFrameTime) {
/* 32 */     return ((Integer)this.time.orElse(Integer.valueOf(defaultFrameTime))).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\metadata\animation\AnimationFrame.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */