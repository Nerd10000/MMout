/*    */ package net.minecraft.client.gui.navigation;
/*    */ 
/*    */ public final class TabNavigation extends Record implements FocusNavigationEvent {
/*    */   private final boolean forward;
/*    */   
/*    */   public TabNavigation(boolean forward) {
/*  8 */     this.forward = forward;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$TabNavigation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$TabNavigation;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$TabNavigation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$TabNavigation;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$TabNavigation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/navigation/FocusNavigationEvent$TabNavigation;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public boolean forward() {
/*  8 */     return this.forward;
/*    */   }
/*    */   
/*    */   public ScreenDirection getVerticalDirectionForInitialFocus() {
/* 11 */     return this.forward ? ScreenDirection.DOWN : ScreenDirection.UP;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\navigation\FocusNavigationEvent$TabNavigation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */