/*     */ package net.minecraft.client.main;
/*     */ 
/*     */ import net.minecraft.util.StringUtil;
/*     */ 
/*     */ public final class QuickPlayRealmsData extends Record implements GameConfig.QuickPlayVariant {
/*     */   private final String realmId;
/*     */   
/*     */   public QuickPlayRealmsData(String realmId) {
/* 103 */     this.realmId = realmId;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #103	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #103	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #103	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayRealmsData;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String realmId() {
/* 103 */     return this.realmId;
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/* 106 */     return !StringUtil.isBlank(this.realmId);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\main\GameConfig$QuickPlayRealmsData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */