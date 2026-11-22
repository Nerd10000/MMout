/*    */ package net.minecraft.world.clock;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class ClockTimeMarker extends Record {
/*    */   private final Holder<WorldClock> clock;
/*    */   
/*    */   private final int ticks;
/*    */   
/*    */   private final Optional<Integer> periodTicks;
/*    */   
/*    */   private final boolean showInCommands;
/*    */   
/*    */   public ClockTimeMarker(Holder<WorldClock> clock, int ticks, Optional<Integer> periodTicks, boolean showInCommands) {
/*  9 */     this.clock = clock;
/*  9 */     this.ticks = ticks;
/*  9 */     this.periodTicks = periodTicks;
/*  9 */     this.showInCommands = showInCommands;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/clock/ClockTimeMarker;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/clock/ClockTimeMarker;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/clock/ClockTimeMarker;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/clock/ClockTimeMarker;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/clock/ClockTimeMarker;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/clock/ClockTimeMarker;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<WorldClock> clock() {
/*  9 */     return this.clock;
/*    */   }
/*    */   
/*    */   public int ticks() {
/*  9 */     return this.ticks;
/*    */   }
/*    */   
/*    */   public Optional<Integer> periodTicks() {
/*  9 */     return this.periodTicks;
/*    */   }
/*    */   
/*    */   public boolean showInCommands() {
/*  9 */     return this.showInCommands;
/*    */   }
/*    */   
/* 15 */   public static final Codec<ResourceKey<ClockTimeMarker>> KEY_CODEC = ResourceKey.codec(ClockTimeMarkers.ROOT_ID);
/*    */   
/*    */   public long getRepetitionCount(long totalTicks) {
/* 18 */     if (this.periodTicks.isEmpty())
/* 19 */       return (totalTicks >= this.ticks) ? 1L : 0L; 
/* 21 */     int periodTicks = ((Integer)this.periodTicks.get()).intValue();
/* 22 */     return totalTicks / periodTicks + ((totalTicks % periodTicks >= this.ticks) ? 1L : 0L);
/*    */   }
/*    */   
/*    */   public long resolveTimeToMoveTo(long totalTicks) {
/* 26 */     if (this.periodTicks.isEmpty())
/* 27 */       return this.ticks; 
/* 29 */     int periodTicks = ((Integer)this.periodTicks.get()).intValue();
/* 30 */     return totalTicks + durationToNext(periodTicks, totalTicks % periodTicks, this.ticks);
/*    */   }
/*    */   
/*    */   public boolean occursAt(long totalTicks) {
/* 34 */     if (this.periodTicks.isEmpty())
/* 35 */       return (this.ticks == totalTicks); 
/* 37 */     return (this.ticks == totalTicks % ((Integer)this.periodTicks.get()).intValue());
/*    */   }
/*    */   
/*    */   private static long durationToNext(int periodTicks, long from, long to) {
/* 41 */     long duration = to - from;
/* 42 */     return (duration > 0L) ? duration : (periodTicks + duration);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\clock\ClockTimeMarker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */