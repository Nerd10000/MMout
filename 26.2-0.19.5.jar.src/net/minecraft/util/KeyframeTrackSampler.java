/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.world.attribute.LerpFunction;
/*     */ 
/*     */ public class KeyframeTrackSampler<T> {
/*     */   private final Optional<Integer> periodTicks;
/*     */   
/*     */   private final LerpFunction<T> lerp;
/*     */   
/*     */   private final List<Segment<T>> segments;
/*     */   
/*     */   KeyframeTrackSampler(KeyframeTrack<T> track, Optional<Integer> periodTicks, LerpFunction<T> lerp) {
/*  17 */     this.periodTicks = periodTicks;
/*  18 */     this.lerp = lerp;
/*  19 */     this.segments = bakeSegments(track, periodTicks);
/*     */   }
/*     */   
/*     */   private static <T> List<Segment<T>> bakeSegments(KeyframeTrack<T> track, Optional<Integer> periodTicks) {
/*  23 */     List<Keyframe<T>> keyframes = track.keyframes();
/*  24 */     if (keyframes.size() == 1) {
/*  25 */       T value = ((Keyframe<T>)keyframes.getFirst()).value();
/*  26 */       return List.of(new Segment<>(EasingType.CONSTANT, value, 0, value, 0));
/*     */     } 
/*  28 */     List<Segment<T>> segments = new ArrayList<>();
/*  31 */     if (periodTicks.isPresent()) {
/*  32 */       Keyframe<T> firstKeyframe = keyframes.getFirst();
/*  33 */       Keyframe<T> lastKeyframe = keyframes.getLast();
/*  34 */       segments.add(new Segment<>(track, lastKeyframe, lastKeyframe
/*     */             
/*  37 */             .ticks() - ((Integer)periodTicks.get()).intValue(), firstKeyframe, firstKeyframe
/*     */             
/*  39 */             .ticks()));
/*  41 */       addSegmentsFromKeyframes(track, keyframes, segments);
/*  42 */       segments.add(new Segment<>(track, lastKeyframe, lastKeyframe
/*     */             
/*  45 */             .ticks(), firstKeyframe, firstKeyframe
/*     */             
/*  47 */             .ticks() + ((Integer)periodTicks.get()).intValue()));
/*     */     } else {
/*  50 */       addSegmentsFromKeyframes(track, keyframes, segments);
/*     */     } 
/*  52 */     return List.copyOf(segments);
/*     */   }
/*     */   
/*     */   private static <T> void addSegmentsFromKeyframes(KeyframeTrack<T> track, List<Keyframe<T>> keyframes, List<Segment<T>> output) {
/*  56 */     for (int i = 0; i < keyframes.size() - 1; i++) {
/*  57 */       Keyframe<T> keyframe = keyframes.get(i);
/*  58 */       Keyframe<T> nextKeyframe = keyframes.get(i + 1);
/*  59 */       output.add(new Segment<>(track, keyframe, keyframe.ticks(), nextKeyframe, nextKeyframe.ticks()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public T sample(long ticks) {
/*  64 */     long sampleTicks = loopTicks(ticks);
/*  65 */     Segment<T> segment = getSegmentAt(sampleTicks);
/*  66 */     if (sampleTicks <= segment.fromTicks)
/*  67 */       return segment.fromValue; 
/*  68 */     if (sampleTicks >= segment.toTicks)
/*  69 */       return segment.toValue; 
/*  71 */     float alpha = (float)(sampleTicks - segment.fromTicks) / (segment.toTicks - segment.fromTicks);
/*  72 */     float easedAlpha = segment.easing.apply(alpha);
/*  73 */     return (T)this.lerp.apply(easedAlpha, segment.fromValue, segment.toValue);
/*     */   }
/*     */   
/*     */   private Segment<T> getSegmentAt(long currentTicks) {
/*  77 */     for (Segment<T> segment : this.segments) {
/*  78 */       if (currentTicks < segment.toTicks)
/*  79 */         return segment; 
/*     */     } 
/*  82 */     return this.segments.getLast();
/*     */   }
/*     */   
/*     */   private long loopTicks(long ticks) {
/*  86 */     if (this.periodTicks.isPresent())
/*  87 */       return Math.floorMod(ticks, ((Integer)this.periodTicks.get()).intValue()); 
/*  89 */     return ticks;
/*     */   }
/*     */   
/*     */   private static final class Segment<T> extends Record {
/*     */     private final EasingType easing;
/*     */     
/*     */     private final T fromValue;
/*     */     
/*     */     private final int fromTicks;
/*     */     
/*     */     private final T toValue;
/*     */     
/*     */     private final int toTicks;
/*     */     
/*     */     public int toTicks() {
/*  92 */       return this.toTicks;
/*     */     }
/*     */     
/*     */     public T toValue() {
/*  92 */       return this.toValue;
/*     */     }
/*     */     
/*     */     public int fromTicks() {
/*  92 */       return this.fromTicks;
/*     */     }
/*     */     
/*     */     public T fromValue() {
/*  92 */       return this.fromValue;
/*     */     }
/*     */     
/*     */     public EasingType easing() {
/*  92 */       return this.easing;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/KeyframeTrackSampler$Segment;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/KeyframeTrackSampler$Segment;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment<TT;>;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/KeyframeTrackSampler$Segment;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment<TT;>;
/*     */     }
/*     */     
/*     */     private Segment(EasingType easing, T fromValue, int fromTicks, T toValue, int toTicks) {
/*  92 */       this.easing = easing;
/*  92 */       this.fromValue = fromValue;
/*  92 */       this.fromTicks = fromTicks;
/*  92 */       this.toValue = toValue;
/*  92 */       this.toTicks = toTicks;
/*     */     }
/*     */     
/*     */     public Segment(KeyframeTrack<T> track, Keyframe<T> from, int fromTicks, Keyframe<T> to, int toTicks) {
/* 100 */       this(track
/* 101 */           .easingType(), from
/* 102 */           .value(), fromTicks, to
/* 103 */           .value(), toTicks);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\KeyframeTrackSampler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */