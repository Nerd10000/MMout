/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ public final class StandAlone<T> extends Record {
/*     */   private final List<T> value;
/*     */   
/*     */   private final ListOperation operation;
/*     */   
/*     */   public StandAlone(List<T> value, ListOperation operation) {
/* 182 */     this.value = value;
/* 182 */     this.operation = operation;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #182	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone<TT;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #182	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #182	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone<TT;>;
/*     */   }
/*     */   
/*     */   public List<T> value() {
/* 182 */     return this.value;
/*     */   }
/*     */   
/*     */   public ListOperation operation() {
/* 182 */     return this.operation;
/*     */   }
/*     */   
/*     */   public static <T> Codec<StandAlone<T>> codec(Codec<T> valueCodec, int maxSize) {
/* 184 */     return RecordCodecBuilder.create(i -> i.group((App)valueCodec.sizeLimitedListOf(maxSize).fieldOf("values").forGetter(()), (App)ListOperation.codec(maxSize).forGetter(())).apply((Applicative)i, StandAlone::new));
/*     */   }
/*     */   
/*     */   public List<T> apply(List<T> input) {
/* 191 */     return this.operation.apply(input, this.value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ListOperation$StandAlone.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */