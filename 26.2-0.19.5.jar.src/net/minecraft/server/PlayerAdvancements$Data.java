/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.AdvancementProgress;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ final class Data extends Record {
/*     */   private final Map<Identifier, AdvancementProgress> map;
/*     */   
/*     */   private Data(Map<Identifier, AdvancementProgress> map) {
/* 344 */     this.map = map;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/PlayerAdvancements$Data;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #344	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/PlayerAdvancements$Data;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/PlayerAdvancements$Data;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #344	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/PlayerAdvancements$Data;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/PlayerAdvancements$Data;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #344	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/PlayerAdvancements$Data;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Map<Identifier, AdvancementProgress> map() {
/* 344 */     return this.map;
/*     */   }
/*     */   
/* 345 */   public static final Codec<Data> CODEC = Codec.unboundedMap(Identifier.CODEC, AdvancementProgress.CODEC).xmap(Data::new, Data::map);
/*     */   
/*     */   public void forEach(BiConsumer<Identifier, AdvancementProgress> consumer) {
/* 348 */     this.map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(entry -> consumer.accept((Identifier)entry.getKey(), (AdvancementProgress)entry.getValue()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\PlayerAdvancements$Data.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */