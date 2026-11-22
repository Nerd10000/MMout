/*    */ package net.minecraft.client.renderer.debug;
/*    */ 
/*    */ final class Marker extends Record {
/*    */   private final int color;
/*    */   
/*    */   private final String text;
/*    */   
/*    */   private final long removeAtTime;
/*    */   
/*    */   private Marker(int color, String text, long removeAtTime) {
/* 19 */     this.color = color;
/* 19 */     this.text = text;
/* 19 */     this.removeAtTime = removeAtTime;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer$Marker;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer$Marker;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer$Marker;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer$Marker;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer$Marker;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/debug/GameTestBlockHighlightRenderer$Marker;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int color() {
/* 19 */     return this.color;
/*    */   }
/*    */   
/*    */   public String text() {
/* 19 */     return this.text;
/*    */   }
/*    */   
/*    */   public long removeAtTime() {
/* 19 */     return this.removeAtTime;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\debug\GameTestBlockHighlightRenderer$Marker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */