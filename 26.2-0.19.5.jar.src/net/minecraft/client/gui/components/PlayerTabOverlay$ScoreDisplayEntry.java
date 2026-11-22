/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ final class ScoreDisplayEntry extends Record {
/*     */   private final Component name;
/*     */   
/*     */   private final int score;
/*     */   
/*     */   private final Component formattedScore;
/*     */   
/*     */   private final int scoreWidth;
/*     */   
/*     */   private ScoreDisplayEntry(Component name, int score, Component formattedScore, int scoreWidth) {
/* 111 */     this.name = name;
/* 111 */     this.score = score;
/* 111 */     this.formattedScore = formattedScore;
/* 111 */     this.scoreWidth = scoreWidth;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #111	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #111	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #111	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Component name() {
/* 111 */     return this.name;
/*     */   }
/*     */   
/*     */   public int score() {
/* 111 */     return this.score;
/*     */   }
/*     */   
/*     */   public Component formattedScore() {
/* 111 */     return this.formattedScore;
/*     */   }
/*     */   
/*     */   public int scoreWidth() {
/* 111 */     return this.scoreWidth;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\PlayerTabOverlay$ScoreDisplayEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */