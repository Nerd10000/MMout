/*     */ package net.minecraft.client.gui.screens.social;
/*     */ 
/*     */ import java.util.UUID;
/*     */ 
/*     */ public final class PlayerData extends Record {
/*     */   private final UUID id;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   public PlayerData(UUID id, String name) {
/* 228 */     this.id = id;
/* 228 */     this.name = name;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/social/PlayerSocialManager$PlayerData;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #228	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/social/PlayerSocialManager$PlayerData;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/social/PlayerSocialManager$PlayerData;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #228	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/social/PlayerSocialManager$PlayerData;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/social/PlayerSocialManager$PlayerData;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #228	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/social/PlayerSocialManager$PlayerData;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public UUID id() {
/* 228 */     return this.id;
/*     */   }
/*     */   
/*     */   public String name() {
/* 228 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\social\PlayerSocialManager$PlayerData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */