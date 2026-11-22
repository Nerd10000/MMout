/*   */ package net.minecraft.client.gui.components.debug;
/*   */ 
/*   */ import net.minecraft.network.chat.Component;
/*   */ 
/*   */ public final class DebugEntryCategory extends Record {
/*   */   private final Component label;
/*   */   
/*   */   private final float sortKey;
/*   */   
/*   */   public DebugEntryCategory(Component label, float sortKey) {
/* 6 */     this.label = label;
/* 6 */     this.sortKey = sortKey;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/debug/DebugEntryCategory;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/gui/components/debug/DebugEntryCategory;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/debug/DebugEntryCategory;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/gui/components/debug/DebugEntryCategory;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/debug/DebugEntryCategory;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/gui/components/debug/DebugEntryCategory;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public Component label() {
/* 6 */     return this.label;
/*   */   }
/*   */   
/*   */   public float sortKey() {
/* 6 */     return this.sortKey;
/*   */   }
/*   */   
/* 7 */   public static final DebugEntryCategory SCREEN_TEXT = new DebugEntryCategory((Component)Component.translatable("debug.options.category.text"), 1.0F);
/*   */   
/* 8 */   public static final DebugEntryCategory RENDERER = new DebugEntryCategory((Component)Component.translatable("debug.options.category.renderer"), 2.0F);
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugEntryCategory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */