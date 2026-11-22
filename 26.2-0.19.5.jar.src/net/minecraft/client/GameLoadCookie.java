/*   */ package net.minecraft.client;
/*   */ 
/*   */ import com.mojang.realmsclient.client.RealmsClient;
/*   */ import net.minecraft.client.main.GameConfig;
/*   */ 
/*   */ public final class GameLoadCookie extends Record {
/*   */   private final RealmsClient realmsClient;
/*   */   
/*   */   private final GameConfig.QuickPlayData quickPlayData;
/*   */   
/*   */   public GameLoadCookie(RealmsClient realmsClient, GameConfig.QuickPlayData quickPlayData) {
/* 6 */     this.realmsClient = realmsClient;
/* 6 */     this.quickPlayData = quickPlayData;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/GameLoadCookie;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/GameLoadCookie;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/GameLoadCookie;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/GameLoadCookie;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/GameLoadCookie;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/GameLoadCookie;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public RealmsClient realmsClient() {
/* 6 */     return this.realmsClient;
/*   */   }
/*   */   
/*   */   public GameConfig.QuickPlayData quickPlayData() {
/* 6 */     return this.quickPlayData;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\GameLoadCookie.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */