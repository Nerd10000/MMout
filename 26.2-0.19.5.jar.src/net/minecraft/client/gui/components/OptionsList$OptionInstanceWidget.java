/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import net.minecraft.client.OptionInstance;
/*     */ 
/*     */ public final class OptionInstanceWidget extends Record {
/*     */   private final AbstractWidget widget;
/*     */   
/*     */   private final OptionInstance<?> optionInstance;
/*     */   
/*     */   public OptionInstance<?> optionInstance() {
/* 210 */     return this.optionInstance;
/*     */   }
/*     */   
/*     */   public AbstractWidget widget() {
/* 210 */     return this.widget;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #210	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #210	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #210	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;
/*     */   }
/*     */   
/*     */   public OptionInstanceWidget(AbstractWidget widget, OptionInstance<?> optionInstance) {
/* 210 */     this.widget = widget;
/* 210 */     this.optionInstance = optionInstance;
/*     */   }
/*     */   
/*     */   public OptionInstanceWidget(AbstractWidget widget) {
/* 212 */     this(widget, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\OptionsList$OptionInstanceWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */