/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import net.minecraft.server.level.ClientInformation;
/*    */ 
/*    */ public final class CommonListenerCookie extends Record {
/*    */   private final GameProfile gameProfile;
/*    */   
/*    */   private final int latency;
/*    */   
/*    */   private final ClientInformation clientInformation;
/*    */   
/*    */   private final boolean transferred;
/*    */   
/*    */   public CommonListenerCookie(GameProfile gameProfile, int latency, ClientInformation clientInformation, boolean transferred) {
/*  6 */     this.gameProfile = gameProfile;
/*  6 */     this.latency = latency;
/*  6 */     this.clientInformation = clientInformation;
/*  6 */     this.transferred = transferred;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/network/CommonListenerCookie;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/CommonListenerCookie;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/network/CommonListenerCookie;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/CommonListenerCookie;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/network/CommonListenerCookie;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/network/CommonListenerCookie;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GameProfile gameProfile() {
/*  6 */     return this.gameProfile;
/*    */   }
/*    */   
/*    */   public int latency() {
/*  6 */     return this.latency;
/*    */   }
/*    */   
/*    */   public ClientInformation clientInformation() {
/*  6 */     return this.clientInformation;
/*    */   }
/*    */   
/*    */   public boolean transferred() {
/*  6 */     return this.transferred;
/*    */   }
/*    */   
/*    */   public static CommonListenerCookie createInitial(GameProfile gameProfile, boolean transferred) {
/* 13 */     return new CommonListenerCookie(gameProfile, 0, 
/*    */         
/* 16 */         ClientInformation.createDefault(), transferred);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\CommonListenerCookie.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */