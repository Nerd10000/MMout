/*    */ package net.minecraft.world.entity.ai.memory;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ 
/*    */ public final class ExpirableValue<T> extends Record {
/*    */   private final T value;
/*    */   
/*    */   private final Optional<Long> timeToLive;
/*    */   
/*    */   public ExpirableValue(T value, Optional<Long> timeToLive) {
/* 10 */     this.value = value;
/* 10 */     this.timeToLive = timeToLive;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/memory/ExpirableValue;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/memory/ExpirableValue;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/memory/ExpirableValue<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/memory/ExpirableValue;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/memory/ExpirableValue;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/memory/ExpirableValue<TT;>;
/*    */   }
/*    */   
/*    */   public T value() {
/* 10 */     return this.value;
/*    */   }
/*    */   
/*    */   public Optional<Long> timeToLive() {
/* 10 */     return this.timeToLive;
/*    */   }
/*    */   
/*    */   public static <T> ExpirableValue<T> of(T value) {
/* 12 */     return new ExpirableValue<>(value, Optional.empty());
/*    */   }
/*    */   
/*    */   public static <T> ExpirableValue<T> of(T value, long ticksUntilExpiry) {
/* 16 */     return new ExpirableValue<>(value, Optional.of(Long.valueOf(ticksUntilExpiry)));
/*    */   }
/*    */   
/*    */   public String toString() {
/* 21 */     return String.valueOf(this.value) + String.valueOf(this.value);
/*    */   }
/*    */   
/*    */   public static <T> Codec<ExpirableValue<T>> codec(Codec<T> valueCodec) {
/* 25 */     return RecordCodecBuilder.create(i -> i.group((App)valueCodec.fieldOf("value").forGetter(ExpirableValue::value), (App)Codec.LONG.lenientOptionalFieldOf("ttl").forGetter(ExpirableValue::timeToLive)).apply((Applicative)i, ExpirableValue::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\memory\ExpirableValue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */