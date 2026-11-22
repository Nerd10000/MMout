/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ final class InfoUnderneathSettings extends Record {
/*     */   private final int maxInfoRows;
/*     */   
/*     */   private final boolean alwaysMaxHeight;
/*     */   
/*     */   private InfoUnderneathSettings(int maxInfoRows, boolean alwaysMaxHeight) {
/* 185 */     this.maxInfoRows = maxInfoRows;
/* 185 */     this.alwaysMaxHeight = alwaysMaxHeight;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #185	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #185	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #185	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int maxInfoRows() {
/* 185 */     return this.maxInfoRows;
/*     */   }
/*     */   
/*     */   public boolean alwaysMaxHeight() {
/* 185 */     return this.alwaysMaxHeight;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\SwitchGrid$InfoUnderneathSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */