/*    */ package net.minecraft.client.gui.navigation;
/*    */ 
/*    */ public final class ScreenPosition extends Record {
/*    */   private final int x;
/*    */   
/*    */   private final int y;
/*    */   
/*    */   public ScreenPosition(int x, int y) {
/*  3 */     this.x = x;
/*  3 */     this.y = y;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/navigation/ScreenPosition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/navigation/ScreenPosition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/navigation/ScreenPosition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/navigation/ScreenPosition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/navigation/ScreenPosition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/navigation/ScreenPosition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int x() {
/*  3 */     return this.x;
/*    */   }
/*    */   
/*    */   public int y() {
/*  3 */     return this.y;
/*    */   }
/*    */   
/*    */   public static ScreenPosition of(ScreenAxis axis, int primaryValue, int secondaryValue) {
/*  5 */     switch (axis) {
/*    */       default:
/*  5 */         throw new MatchException(null, null);
/*    */       case DOWN:
/*    */       
/*    */       case UP:
/*    */         break;
/*    */     } 
/*  5 */     return 
/*    */       
/*  7 */       new ScreenPosition(secondaryValue, primaryValue);
/*    */   }
/*    */   
/*    */   public ScreenPosition step(ScreenDirection direction) {
/* 12 */     switch (direction) {
/*    */       default:
/* 12 */         throw new MatchException(null, null);
/*    */       case DOWN:
/*    */       
/*    */       case UP:
/*    */       
/*    */       case LEFT:
/*    */       
/*    */       case RIGHT:
/*    */         break;
/*    */     } 
/* 12 */     return 
/*    */       
/* 16 */       new ScreenPosition(this.x + 1, this.y);
/*    */   }
/*    */   
/*    */   public int getCoordinate(ScreenAxis axis) {
/* 21 */     switch (axis) {
/*    */       default:
/* 21 */         throw new MatchException(null, null);
/*    */       case DOWN:
/*    */       
/*    */       case UP:
/*    */         break;
/*    */     } 
/* 21 */     return 
/*    */       
/* 23 */       this.y;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\navigation\ScreenPosition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */