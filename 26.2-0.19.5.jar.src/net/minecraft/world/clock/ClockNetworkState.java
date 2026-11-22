/*    */ package net.minecraft.world.clock;
/*    */ 
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ClockNetworkState extends Record {
/*    */   private final long totalTicks;
/*    */   
/*    */   private final float partialTick;
/*    */   
/*    */   private final float rate;
/*    */   
/*    */   public ClockNetworkState(long totalTicks, float partialTick, float rate) {
/*  7 */     this.totalTicks = totalTicks;
/*  7 */     this.partialTick = partialTick;
/*  7 */     this.rate = rate;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/clock/ClockNetworkState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/clock/ClockNetworkState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/clock/ClockNetworkState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/clock/ClockNetworkState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/clock/ClockNetworkState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/clock/ClockNetworkState;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public long totalTicks() {
/*  7 */     return this.totalTicks;
/*    */   }
/*    */   
/*    */   public float partialTick() {
/*  7 */     return this.partialTick;
/*    */   }
/*    */   
/*    */   public float rate() {
/*  7 */     return this.rate;
/*    */   }
/*    */   
/* 12 */   public static final StreamCodec<ByteBuf, ClockNetworkState> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_LONG, ClockNetworkState::totalTicks, ByteBufCodecs.FLOAT, ClockNetworkState::partialTick, ByteBufCodecs.FLOAT, ClockNetworkState::rate, ClockNetworkState::new);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\clock\ClockNetworkState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */