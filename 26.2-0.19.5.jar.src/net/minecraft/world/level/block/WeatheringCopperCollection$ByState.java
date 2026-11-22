/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public final class ByState<T> extends Record {
/*     */   private final T unaffected;
/*     */   
/*     */   private final T exposed;
/*     */   
/*     */   private final T weathered;
/*     */   
/*     */   private final T oxidized;
/*     */   
/*     */   public ByState(T unaffected, T exposed, T weathered, T oxidized) {
/* 142 */     this.unaffected = unaffected;
/* 142 */     this.exposed = exposed;
/* 142 */     this.weathered = weathered;
/* 142 */     this.oxidized = oxidized;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #142	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #142	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #142	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/WeatheringCopperCollection$ByState<TT;>;
/*     */   }
/*     */   
/*     */   public T unaffected() {
/* 142 */     return this.unaffected;
/*     */   }
/*     */   
/*     */   public T exposed() {
/* 142 */     return this.exposed;
/*     */   }
/*     */   
/*     */   public T weathered() {
/* 142 */     return this.weathered;
/*     */   }
/*     */   
/*     */   public T oxidized() {
/* 142 */     return this.oxidized;
/*     */   }
/*     */   
/*     */   public static <T> ByState<T> create(T value) {
/* 149 */     return new ByState<>(value, value, value, value);
/*     */   }
/*     */   
/*     */   public <U> ByState<U> map(Function<T, U> mapper) {
/* 153 */     return new ByState((T)mapper
/* 154 */         .apply(this.unaffected), (T)mapper
/* 155 */         .apply(this.exposed), (T)mapper
/* 156 */         .apply(this.weathered), (T)mapper
/* 157 */         .apply(this.oxidized));
/*     */   }
/*     */   
/*     */   public T pick(WeatheringCopper.WeatherState state) {
/* 162 */     switch (WeatheringCopperCollection.null.$SwitchMap$net$minecraft$world$level$block$WeatheringCopper$WeatherState[state.ordinal()]) {
/*     */       default:
/* 162 */         throw new MatchException(null, null);
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 4:
/*     */         break;
/*     */     } 
/* 162 */     return 
/*     */       
/* 166 */       this.oxidized;
/*     */   }
/*     */   
/*     */   public void forEach(Consumer<T> consumer) {
/* 171 */     consumer.accept(this.unaffected);
/* 172 */     consumer.accept(this.exposed);
/* 173 */     consumer.accept(this.weathered);
/* 174 */     consumer.accept(this.oxidized);
/*     */   }
/*     */   
/*     */   public void progressMapping(BiConsumer<T, T> consumer) {
/* 178 */     consumer.accept(this.unaffected, this.exposed);
/* 179 */     consumer.accept(this.exposed, this.weathered);
/* 180 */     consumer.accept(this.weathered, this.oxidized);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\WeatheringCopperCollection$ByState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */