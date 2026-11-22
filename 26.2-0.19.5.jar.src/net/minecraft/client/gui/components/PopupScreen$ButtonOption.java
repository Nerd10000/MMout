/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ final class ButtonOption extends Record {
/*     */   private final Component message;
/*     */   
/*     */   private final Consumer<PopupScreen> action;
/*     */   
/*     */   private ButtonOption(Component message, Consumer<PopupScreen> action) {
/* 166 */     this.message = message;
/* 166 */     this.action = action;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #166	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #166	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #166	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/gui/components/PopupScreen$ButtonOption;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Component message() {
/* 166 */     return this.message;
/*     */   }
/*     */   
/*     */   public Consumer<PopupScreen> action() {
/* 166 */     return this.action;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\PopupScreen$ButtonOption.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */