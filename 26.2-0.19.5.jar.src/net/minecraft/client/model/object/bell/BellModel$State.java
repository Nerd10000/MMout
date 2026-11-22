/*    */ package net.minecraft.client.model.object.bell;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ 
/*    */ public final class State extends Record {
/*    */   private final float ticks;
/*    */   
/*    */   private final Direction shakeDirection;
/*    */   
/*    */   public State(float ticks, Direction shakeDirection) {
/* 64 */     this.ticks = ticks;
/* 64 */     this.shakeDirection = shakeDirection;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/object/bell/BellModel$State;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/model/object/bell/BellModel$State;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/object/bell/BellModel$State;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/model/object/bell/BellModel$State;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/object/bell/BellModel$State;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #64	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/model/object/bell/BellModel$State;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public float ticks() {
/* 64 */     return this.ticks;
/*    */   }
/*    */   
/*    */   public Direction shakeDirection() {
/* 64 */     return this.shakeDirection;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\object\bell\BellModel$State.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */