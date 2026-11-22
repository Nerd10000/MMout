/*    */ package net.minecraft.world.clock;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public final class ClockState extends Record {
/*    */   private final long totalTicks;
/*    */   
/*    */   private final float partialTick;
/*    */   
/*    */   private final float rate;
/*    */   
/*    */   private final boolean paused;
/*    */   
/*    */   public static final Codec<ClockState> CODEC;
/*    */   
/*    */   public ClockState(long totalTicks, float partialTick, float rate, boolean paused) {
/*  7 */     this.totalTicks = totalTicks;
/*  7 */     this.partialTick = partialTick;
/*  7 */     this.rate = rate;
/*  7 */     this.paused = paused;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/clock/ClockState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/clock/ClockState;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/clock/ClockState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/clock/ClockState;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/clock/ClockState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/clock/ClockState;
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
/*    */   public boolean paused() {
/*  7 */     return this.paused;
/*    */   }
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.LONG.fieldOf("total_ticks").forGetter(ClockState::totalTicks), (App)Codec.FLOAT.optionalFieldOf("partial_tick", Float.valueOf(0.0F)).forGetter(ClockState::partialTick), (App)ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("rate", Float.valueOf(1.0F)).forGetter(ClockState::rate), (App)Codec.BOOL.optionalFieldOf("paused", Boolean.valueOf(false)).forGetter(ClockState::paused)).apply((Applicative)i, ClockState::new));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\clock\ClockState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */