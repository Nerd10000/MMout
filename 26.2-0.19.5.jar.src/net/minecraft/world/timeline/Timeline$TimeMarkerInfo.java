/*     */ package net.minecraft.world.timeline;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ final class TimeMarkerInfo extends Record {
/*     */   private final int ticks;
/*     */   
/*     */   private final boolean showInCommands;
/*     */   
/*     */   private static final Codec<TimeMarkerInfo> FULL_CODEC;
/*     */   
/*     */   public static final Codec<TimeMarkerInfo> CODEC;
/*     */   
/*     */   private TimeMarkerInfo(int ticks, boolean showInCommands) {
/* 153 */     this.ticks = ticks;
/* 153 */     this.showInCommands = showInCommands;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/timeline/Timeline$TimeMarkerInfo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #153	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/timeline/Timeline$TimeMarkerInfo;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/timeline/Timeline$TimeMarkerInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #153	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/timeline/Timeline$TimeMarkerInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/timeline/Timeline$TimeMarkerInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #153	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/timeline/Timeline$TimeMarkerInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int ticks() {
/* 153 */     return this.ticks;
/*     */   }
/*     */   
/*     */   public boolean showInCommands() {
/* 153 */     return this.showInCommands;
/*     */   }
/*     */   
/*     */   static {
/* 157 */     FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("ticks").forGetter(TimeMarkerInfo::ticks), (App)Codec.BOOL.optionalFieldOf("show_in_commands", Boolean.valueOf(false)).forGetter(TimeMarkerInfo::showInCommands)).apply((Applicative)i, TimeMarkerInfo::new));
/* 162 */     CODEC = Codec.either(ExtraCodecs.NON_NEGATIVE_INT, FULL_CODEC).xmap(either -> (TimeMarkerInfo)either.map((), ()), timeMarker -> timeMarker.showInCommands ? Either.right(timeMarker) : Either.left(Integer.valueOf(timeMarker.ticks)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\timeline\Timeline$TimeMarkerInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */