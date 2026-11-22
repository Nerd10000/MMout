/*     */ package net.minecraft.client.gui.screens.advancements;
/*     */ 
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ final class Sprites extends Record {
/*     */   private final Identifier first;
/*     */   
/*     */   private final Identifier middle;
/*     */   
/*     */   private final Identifier last;
/*     */   
/*     */   private Sprites(Identifier first, Identifier middle, Identifier last) {
/* 144 */     this.first = first;
/* 144 */     this.middle = middle;
/* 144 */     this.last = last;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/advancements/AdvancementTabType$Sprites;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #144	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/advancements/AdvancementTabType$Sprites;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/advancements/AdvancementTabType$Sprites;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #144	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/screens/advancements/AdvancementTabType$Sprites;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/advancements/AdvancementTabType$Sprites;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #144	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/screens/advancements/AdvancementTabType$Sprites;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier first() {
/* 144 */     return this.first;
/*     */   }
/*     */   
/*     */   public Identifier middle() {
/* 144 */     return this.middle;
/*     */   }
/*     */   
/*     */   public Identifier last() {
/* 144 */     return this.last;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\advancements\AdvancementTabType$Sprites.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */