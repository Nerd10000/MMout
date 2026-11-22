/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ final class DisplayEntry extends Record {
/*     */   private final Component name;
/*     */   
/*     */   private final Component score;
/*     */   
/*     */   private final int scoreWidth;
/*     */   
/*     */   DisplayEntry(Component name, Component score, int scoreWidth) {
/* 714 */     this.name = name;
/* 714 */     this.score = score;
/* 714 */     this.scoreWidth = scoreWidth;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/Hud$1DisplayEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #714	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/Hud$1DisplayEntry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/Hud$1DisplayEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #714	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/Hud$1DisplayEntry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/Hud$1DisplayEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #714	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/Hud$1DisplayEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Component name() {
/* 714 */     return this.name;
/*     */   }
/*     */   
/*     */   public Component score() {
/* 714 */     return this.score;
/*     */   }
/*     */   
/*     */   public int scoreWidth() {
/* 714 */     return this.scoreWidth;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\Hud$1DisplayEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */