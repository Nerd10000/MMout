/*    */ package net.minecraft.client.main;
/*    */ 
/*    */ import net.minecraft.util.StringUtil;
/*    */ 
/*    */ public final class QuickPlayMultiplayerData extends Record implements GameConfig.QuickPlayVariant {
/*    */   private final String serverAddress;
/*    */   
/*    */   public QuickPlayMultiplayerData(String serverAddress) {
/* 96 */     this.serverAddress = serverAddress;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #96	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #96	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #96	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/main/GameConfig$QuickPlayMultiplayerData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String serverAddress() {
/* 96 */     return this.serverAddress;
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 99 */     return !StringUtil.isBlank(this.serverAddress);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\main\GameConfig$QuickPlayMultiplayerData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */