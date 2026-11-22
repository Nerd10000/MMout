/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.clock.WorldClock;
/*     */ 
/*     */ public final class ClockTime extends Record implements TestEnvironmentDefinition<Long> {
/*     */   private final Holder<WorldClock> clock;
/*     */   
/*     */   private final int time;
/*     */   
/*     */   public static final MapCodec<ClockTime> CODEC;
/*     */   
/*     */   public ClockTime(Holder<WorldClock> clock, int time) {
/* 140 */     this.clock = clock;
/* 140 */     this.time = time;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$ClockTime;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #140	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$ClockTime;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$ClockTime;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #140	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$ClockTime;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$ClockTime;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #140	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$ClockTime;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Holder<WorldClock> clock() {
/* 140 */     return this.clock;
/*     */   }
/*     */   
/*     */   public int time() {
/* 140 */     return this.time;
/*     */   }
/*     */   
/*     */   static {
/* 144 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WorldClock.CODEC.fieldOf("clock").forGetter(ClockTime::clock), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("time").forGetter(ClockTime::time)).apply((Applicative)i, ClockTime::new));
/*     */   }
/*     */   
/*     */   public Long setup(ServerLevel level) {
/* 151 */     MinecraftServer server = level.getServer();
/* 152 */     long previous = server.clockManager().getTotalTicks(this.clock);
/* 153 */     server.clockManager().setTotalTicks(this.clock, this.time);
/* 154 */     return Long.valueOf(previous);
/*     */   }
/*     */   
/*     */   public void teardown(ServerLevel level, Long saveData) {
/* 159 */     MinecraftServer server = level.getServer();
/* 160 */     server.clockManager().setTotalTicks(this.clock, saveData.longValue());
/*     */   }
/*     */   
/*     */   public MapCodec<ClockTime> codec() {
/* 165 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$ClockTime.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */