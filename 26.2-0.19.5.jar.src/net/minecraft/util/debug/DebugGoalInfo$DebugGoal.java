/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class DebugGoal extends Record {
/*    */   private final int priority;
/*    */   
/*    */   private final boolean isRunning;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   public DebugGoal(int priority, boolean isRunning, String name) {
/* 15 */     this.priority = priority;
/* 15 */     this.isRunning = isRunning;
/* 15 */     this.name = name;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugGoalInfo$DebugGoal;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugGoalInfo$DebugGoal;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugGoalInfo$DebugGoal;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugGoalInfo$DebugGoal;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugGoalInfo$DebugGoal;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugGoalInfo$DebugGoal;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int priority() {
/* 15 */     return this.priority;
/*    */   }
/*    */   
/*    */   public boolean isRunning() {
/* 15 */     return this.isRunning;
/*    */   }
/*    */   
/*    */   public String name() {
/* 15 */     return this.name;
/*    */   }
/*    */   
/* 16 */   public static final StreamCodec<ByteBuf, DebugGoal> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, DebugGoal::priority, ByteBufCodecs.BOOL, DebugGoal::isRunning, 
/*    */       
/* 19 */       ByteBufCodecs.stringUtf8(255), DebugGoal::name, DebugGoal::new);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\DebugGoalInfo$DebugGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */