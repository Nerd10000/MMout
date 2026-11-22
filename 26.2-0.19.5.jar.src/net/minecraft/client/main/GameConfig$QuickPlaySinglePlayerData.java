/*    */ package net.minecraft.client.main;
/*    */ 
/*    */ public final class QuickPlaySinglePlayerData extends Record implements GameConfig.QuickPlayVariant {
/*    */   private final String worldId;
/*    */   
/*    */   public QuickPlaySinglePlayerData(String worldId) {
/* 89 */     this.worldId = worldId;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #89	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #89	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #89	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlaySinglePlayerData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String worldId() {
/* 89 */     return this.worldId;
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 92 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\main\GameConfig$QuickPlaySinglePlayerData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */