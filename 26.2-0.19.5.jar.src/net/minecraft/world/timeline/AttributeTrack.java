/*    */ package net.minecraft.world.timeline;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.KeyframeTrack;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*    */ import net.minecraft.world.clock.ClockManager;
/*    */ import net.minecraft.world.clock.WorldClock;
/*    */ 
/*    */ public final class AttributeTrack<Value, Argument> extends Record {
/*    */   private final AttributeModifier<Value, Argument> modifier;
/*    */   
/*    */   private final KeyframeTrack<Argument> argumentTrack;
/*    */   
/*    */   public AttributeTrack(AttributeModifier<Value, Argument> modifier, KeyframeTrack<Argument> argumentTrack) {
/* 16 */     this.modifier = modifier;
/* 16 */     this.argumentTrack = argumentTrack;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/timeline/AttributeTrack;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/timeline/AttributeTrack;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/timeline/AttributeTrack<TValue;TArgument;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/timeline/AttributeTrack;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/timeline/AttributeTrack;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/timeline/AttributeTrack<TValue;TArgument;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/timeline/AttributeTrack;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/timeline/AttributeTrack;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/timeline/AttributeTrack<TValue;TArgument;>;
/*    */   }
/*    */   
/*    */   public AttributeModifier<Value, Argument> modifier() {
/* 16 */     return this.modifier;
/*    */   }
/*    */   
/*    */   public KeyframeTrack<Argument> argumentTrack() {
/* 16 */     return this.argumentTrack;
/*    */   }
/*    */   
/*    */   public static <Value> Codec<AttributeTrack<Value, ?>> createCodec(EnvironmentAttribute<Value> attribute) {
/* 21 */     MapCodec<AttributeModifier<Value, ?>> modifierCodec = attribute.type().modifierCodec().optionalFieldOf("modifier", AttributeModifier.override());
/* 22 */     return modifierCodec.dispatch(AttributeTrack::modifier, Util.memoize(modifier -> createCodecWithModifier(attribute, modifier)));
/*    */   }
/*    */   
/*    */   private static <Value, Argument> MapCodec<AttributeTrack<Value, Argument>> createCodecWithModifier(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Argument> modifier) {
/* 28 */     return KeyframeTrack.mapCodec(modifier.argumentCodec(attribute)).xmap(track -> new AttributeTrack<>(modifier, track), AttributeTrack::argumentTrack);
/*    */   }
/*    */   
/*    */   public AttributeTrackSampler<Value, Argument> bakeSampler(EnvironmentAttribute<Value> attribute, Holder<WorldClock> clock, Optional<Integer> periodTicks, ClockManager clockManager) {
/* 35 */     return new AttributeTrackSampler<>(clock, periodTicks, this.modifier, this.argumentTrack, this.modifier.argumentKeyframeLerp(attribute), clockManager);
/*    */   }
/*    */   
/*    */   public static DataResult<AttributeTrack<?, ?>> validatePeriod(AttributeTrack<?, ?> track, int periodTicks) {
/* 39 */     return KeyframeTrack.validatePeriod(track.argumentTrack(), periodTicks).map(ignored -> track);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\timeline\AttributeTrack.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */