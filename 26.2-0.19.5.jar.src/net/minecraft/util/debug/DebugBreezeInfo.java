/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class DebugBreezeInfo extends Record {
/*    */   private final Optional<Integer> attackTarget;
/*    */   
/*    */   private final Optional<BlockPos> jumpTarget;
/*    */   
/*    */   public DebugBreezeInfo(Optional<Integer> attackTarget, Optional<BlockPos> jumpTarget) {
/* 10 */     this.attackTarget = attackTarget;
/* 10 */     this.jumpTarget = jumpTarget;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugBreezeInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugBreezeInfo;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugBreezeInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugBreezeInfo;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugBreezeInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugBreezeInfo;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<Integer> attackTarget() {
/* 10 */     return this.attackTarget;
/*    */   }
/*    */   
/*    */   public Optional<BlockPos> jumpTarget() {
/* 10 */     return this.jumpTarget;
/*    */   }
/*    */   
/* 14 */   public static final StreamCodec<ByteBuf, DebugBreezeInfo> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT
/* 15 */       .apply(ByteBufCodecs::optional), DebugBreezeInfo::attackTarget, BlockPos.STREAM_CODEC
/* 16 */       .apply(ByteBufCodecs::optional), DebugBreezeInfo::jumpTarget, DebugBreezeInfo::new);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\DebugBreezeInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */