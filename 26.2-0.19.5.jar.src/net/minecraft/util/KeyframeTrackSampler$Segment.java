/*     */ package net.minecraft.util;
/*     */ 
/*     */ final class Segment<T> extends Record {
/*     */   private final EasingType easing;
/*     */   
/*     */   private final T fromValue;
/*     */   
/*     */   private final int fromTicks;
/*     */   
/*     */   private final T toValue;
/*     */   
/*     */   private final int toTicks;
/*     */   
/*     */   public int toTicks() {
/*  92 */     return this.toTicks;
/*     */   }
/*     */   
/*     */   public T toValue() {
/*  92 */     return this.toValue;
/*     */   }
/*     */   
/*     */   public int fromTicks() {
/*  92 */     return this.fromTicks;
/*     */   }
/*     */   
/*     */   public T fromValue() {
/*  92 */     return this.fromValue;
/*     */   }
/*     */   
/*     */   public EasingType easing() {
/*  92 */     return this.easing;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/KeyframeTrackSampler$Segment;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #92	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/KeyframeTrackSampler$Segment;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #92	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment<TT;>;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/KeyframeTrackSampler$Segment;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #92	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment<TT;>;
/*     */   }
/*     */   
/*     */   private Segment(EasingType easing, T fromValue, int fromTicks, T toValue, int toTicks) {
/*  92 */     this.easing = easing;
/*  92 */     this.fromValue = fromValue;
/*  92 */     this.fromTicks = fromTicks;
/*  92 */     this.toValue = toValue;
/*  92 */     this.toTicks = toTicks;
/*     */   }
/*     */   
/*     */   public Segment(KeyframeTrack<T> track, Keyframe<T> from, int fromTicks, Keyframe<T> to, int toTicks) {
/* 100 */     this(track
/* 101 */         .easingType(), from
/* 102 */         .value(), fromTicks, to
/* 103 */         .value(), toTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\KeyframeTrackSampler$Segment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */