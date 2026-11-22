/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ 
/*    */ public final class PlayerSprite extends Record implements FontDescription {
/*    */   private final ResolvableProfile profile;
/*    */   
/*    */   private final boolean hat;
/*    */   
/*    */   public PlayerSprite(ResolvableProfile profile, boolean hat) {
/* 28 */     this.profile = profile;
/* 28 */     this.hat = hat;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/FontDescription$PlayerSprite;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$PlayerSprite;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/FontDescription$PlayerSprite;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/FontDescription$PlayerSprite;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/FontDescription$PlayerSprite;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/FontDescription$PlayerSprite;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResolvableProfile profile() {
/* 28 */     return this.profile;
/*    */   }
/*    */   
/*    */   public boolean hat() {
/* 28 */     return this.hat;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\FontDescription$PlayerSprite.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */