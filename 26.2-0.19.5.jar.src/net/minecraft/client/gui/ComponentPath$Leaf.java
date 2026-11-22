/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ 
/*    */ public final class Leaf extends Record implements ComponentPath {
/*    */   private final GuiEventListener component;
/*    */   
/*    */   public Leaf(GuiEventListener component) {
/* 50 */     this.component = component;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/ComponentPath$Leaf;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/ComponentPath$Leaf;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/ComponentPath$Leaf;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/ComponentPath$Leaf;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/ComponentPath$Leaf;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #50	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/ComponentPath$Leaf;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public GuiEventListener component() {
/* 50 */     return this.component;
/*    */   }
/*    */   
/*    */   public void applyFocus(boolean focused) {
/* 53 */     this.component.setFocused(focused);
/*    */   }
/*    */   
/*    */   public GuiEventListener leafComponent() {
/* 58 */     return this.component;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\ComponentPath$Leaf.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */