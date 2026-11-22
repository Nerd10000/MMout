/*    */ package net.minecraft.client.renderer.state.level;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public final class DistancePerDirection extends Record {
/*    */   private final Direction direction;
/*    */   
/*    */   private final double distance;
/*    */   
/*    */   public DistancePerDirection(Direction direction, double distance) {
/* 31 */     this.direction = direction;
/* 31 */     this.distance = distance;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/state/level/WorldBorderRenderState$DistancePerDirection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/level/WorldBorderRenderState$DistancePerDirection;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/state/level/WorldBorderRenderState$DistancePerDirection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/state/level/WorldBorderRenderState$DistancePerDirection;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/state/level/WorldBorderRenderState$DistancePerDirection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/state/level/WorldBorderRenderState$DistancePerDirection;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Direction direction() {
/* 31 */     return this.direction;
/*    */   }
/*    */   
/*    */   public double distance() {
/* 31 */     return this.distance;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\level\WorldBorderRenderState$DistancePerDirection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */