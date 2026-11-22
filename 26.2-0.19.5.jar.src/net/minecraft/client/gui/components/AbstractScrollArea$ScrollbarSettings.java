/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public final class ScrollbarSettings extends Record {
/*     */   private final Identifier scrollerSprite;
/*     */   
/*     */   private final Identifier disabledScrollerSprite;
/*     */   
/*     */   private final Identifier backgroundSprite;
/*     */   
/*     */   private final int scrollbarWidth;
/*     */   
/*     */   private final int scrollbarMinHeight;
/*     */   
/*     */   private final int scrollRate;
/*     */   
/*     */   private final boolean resizingScrollbar;
/*     */   
/*     */   public ScrollbarSettings(Identifier scrollerSprite, Identifier disabledScrollerSprite, Identifier backgroundSprite, int scrollbarWidth, int scrollbarMinHeight, int scrollRate, boolean resizingScrollbar) {
/* 145 */     this.scrollerSprite = scrollerSprite;
/* 145 */     this.disabledScrollerSprite = disabledScrollerSprite;
/* 145 */     this.backgroundSprite = backgroundSprite;
/* 145 */     this.scrollbarWidth = scrollbarWidth;
/* 145 */     this.scrollbarMinHeight = scrollbarMinHeight;
/* 145 */     this.scrollRate = scrollRate;
/* 145 */     this.resizingScrollbar = resizingScrollbar;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/AbstractScrollArea$ScrollbarSettings;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #145	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/AbstractScrollArea$ScrollbarSettings;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/AbstractScrollArea$ScrollbarSettings;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #145	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/AbstractScrollArea$ScrollbarSettings;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/AbstractScrollArea$ScrollbarSettings;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #145	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/components/AbstractScrollArea$ScrollbarSettings;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier scrollerSprite() {
/* 145 */     return this.scrollerSprite;
/*     */   }
/*     */   
/*     */   public Identifier disabledScrollerSprite() {
/* 145 */     return this.disabledScrollerSprite;
/*     */   }
/*     */   
/*     */   public Identifier backgroundSprite() {
/* 145 */     return this.backgroundSprite;
/*     */   }
/*     */   
/*     */   public int scrollbarWidth() {
/* 145 */     return this.scrollbarWidth;
/*     */   }
/*     */   
/*     */   public int scrollbarMinHeight() {
/* 145 */     return this.scrollbarMinHeight;
/*     */   }
/*     */   
/*     */   public int scrollRate() {
/* 145 */     return this.scrollRate;
/*     */   }
/*     */   
/*     */   public boolean resizingScrollbar() {
/* 145 */     return this.resizingScrollbar;
/*     */   }
/*     */   
/* 154 */   public static final ScrollbarSettings NO_SCROLL = AbstractScrollArea.defaultSettings(0);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\AbstractScrollArea$ScrollbarSettings.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */