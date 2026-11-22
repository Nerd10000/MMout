/*    */ package net.minecraft.client.input;
/*    */ 
/*    */ public final class MouseButtonEvent extends Record implements InputWithModifiers {
/*    */   private final double x;
/*    */   
/*    */   private final double y;
/*    */   
/*    */   private final MouseButtonInfo buttonInfo;
/*    */   
/*    */   public MouseButtonEvent(double x, double y, MouseButtonInfo buttonInfo) {
/*  3 */     this.x = x;
/*  3 */     this.y = y;
/*  3 */     this.buttonInfo = buttonInfo;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/input/MouseButtonEvent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/MouseButtonEvent;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/input/MouseButtonEvent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/input/MouseButtonEvent;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/input/MouseButtonEvent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/input/MouseButtonEvent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public double x() {
/*  3 */     return this.x;
/*    */   }
/*    */   
/*    */   public double y() {
/*  3 */     return this.y;
/*    */   }
/*    */   
/*    */   public MouseButtonInfo buttonInfo() {
/*  3 */     return this.buttonInfo;
/*    */   }
/*    */   
/*    */   public int input() {
/* 10 */     return button();
/*    */   }
/*    */   
/*    */   public int button() {
/* 14 */     return buttonInfo().button();
/*    */   }
/*    */   
/*    */   public int modifiers() {
/* 19 */     return buttonInfo().modifiers();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\input\MouseButtonEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */