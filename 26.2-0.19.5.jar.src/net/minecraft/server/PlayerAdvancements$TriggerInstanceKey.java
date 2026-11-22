/*     */ package net.minecraft.server;
/*     */ 
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ 
/*     */ public final class TriggerInstanceKey extends Record {
/*     */   private final AdvancementHolder advancement;
/*     */   
/*     */   private final String criterion;
/*     */   
/*     */   public TriggerInstanceKey(AdvancementHolder advancement, String criterion) {
/* 341 */     this.advancement = advancement;
/* 341 */     this.criterion = criterion;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/PlayerAdvancements$TriggerInstanceKey;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #341	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/PlayerAdvancements$TriggerInstanceKey;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/PlayerAdvancements$TriggerInstanceKey;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #341	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/PlayerAdvancements$TriggerInstanceKey;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/PlayerAdvancements$TriggerInstanceKey;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #341	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/PlayerAdvancements$TriggerInstanceKey;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public AdvancementHolder advancement() {
/* 341 */     return this.advancement;
/*     */   }
/*     */   
/*     */   public String criterion() {
/* 341 */     return this.criterion;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\PlayerAdvancements$TriggerInstanceKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */