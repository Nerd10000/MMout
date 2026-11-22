/*    */ package net.minecraft.world.clock;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ 
/*    */ public final class PackedClockStates extends Record {
/*    */   private final Map<Holder<WorldClock>, ClockState> clocks;
/*    */   
/*    */   public PackedClockStates(Map<Holder<WorldClock>, ClockState> clocks) {
/*  8 */     this.clocks = clocks;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/clock/PackedClockStates;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/clock/PackedClockStates;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/clock/PackedClockStates;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/clock/PackedClockStates;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/clock/PackedClockStates;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/clock/PackedClockStates;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<Holder<WorldClock>, ClockState> clocks() {
/*  8 */     return this.clocks;
/*    */   }
/*    */   
/* 11 */   public static final PackedClockStates EMPTY = new PackedClockStates(Map.of());
/*    */   
/* 13 */   public static final Codec<PackedClockStates> CODEC = Codec.unboundedMap(WorldClock.CODEC, ClockState.CODEC).xmap(PackedClockStates::new, PackedClockStates::clocks);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\clock\PackedClockStates.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */