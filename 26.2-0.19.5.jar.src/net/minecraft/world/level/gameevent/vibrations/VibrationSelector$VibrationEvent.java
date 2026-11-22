/*    */ package net.minecraft.world.level.gameevent.vibrations;
/*    */ 
/*    */ final class VibrationEvent extends Record {
/*    */   private final VibrationInfo event;
/*    */   
/*    */   private final long tick;
/*    */   
/*    */   private VibrationEvent(VibrationInfo event, long tick) {
/* 63 */     this.event = event;
/* 63 */     this.tick = tick;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationSelector$VibrationEvent;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationSelector$VibrationEvent;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationSelector$VibrationEvent;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationSelector$VibrationEvent;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/gameevent/vibrations/VibrationSelector$VibrationEvent;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #63	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/gameevent/vibrations/VibrationSelector$VibrationEvent;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public VibrationInfo event() {
/* 63 */     return this.event;
/*    */   }
/*    */   
/*    */   public long tick() {
/* 63 */     return this.tick;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\vibrations\VibrationSelector$VibrationEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */