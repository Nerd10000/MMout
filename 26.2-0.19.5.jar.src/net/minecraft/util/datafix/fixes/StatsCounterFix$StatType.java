/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ final class StatType extends Record {
/*     */   private final String type;
/*     */   
/*     */   private final String typeKey;
/*     */   
/*     */   private StatType(String type, String typeKey) {
/* 199 */     this.type = type;
/* 199 */     this.typeKey = typeKey;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/datafix/fixes/StatsCounterFix$StatType;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #199	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/StatsCounterFix$StatType;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/datafix/fixes/StatsCounterFix$StatType;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #199	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/util/datafix/fixes/StatsCounterFix$StatType;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/datafix/fixes/StatsCounterFix$StatType;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #199	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/util/datafix/fixes/StatsCounterFix$StatType;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String type() {
/* 199 */     return this.type;
/*     */   }
/*     */   
/*     */   public String typeKey() {
/* 199 */     return this.typeKey;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\StatsCounterFix$StatType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */