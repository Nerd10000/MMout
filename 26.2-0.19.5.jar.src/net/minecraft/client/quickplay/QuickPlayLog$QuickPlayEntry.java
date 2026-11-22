/*     */ package net.minecraft.client.quickplay;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.time.Instant;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ final class QuickPlayEntry extends Record {
/*     */   private final QuickPlayLog.QuickPlayWorld quickPlayWorld;
/*     */   
/*     */   private final Instant lastPlayedTime;
/*     */   
/*     */   private final GameType gamemode;
/*     */   
/*     */   public static final Codec<QuickPlayEntry> CODEC;
/*     */   
/*     */   private QuickPlayEntry(QuickPlayLog.QuickPlayWorld quickPlayWorld, Instant lastPlayedTime, GameType gamemode) {
/* 112 */     this.quickPlayWorld = quickPlayWorld;
/* 112 */     this.lastPlayedTime = lastPlayedTime;
/* 112 */     this.gamemode = gamemode;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayEntry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayEntry;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayEntry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayEntry;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayEntry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/quickplay/QuickPlayLog$QuickPlayEntry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public QuickPlayLog.QuickPlayWorld quickPlayWorld() {
/* 112 */     return this.quickPlayWorld;
/*     */   }
/*     */   
/*     */   public Instant lastPlayedTime() {
/* 112 */     return this.lastPlayedTime;
/*     */   }
/*     */   
/*     */   public GameType gamemode() {
/* 112 */     return this.gamemode;
/*     */   }
/*     */   
/*     */   static {
/* 113 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)QuickPlayLog.QuickPlayWorld.MAP_CODEC.forGetter(QuickPlayEntry::quickPlayWorld), (App)ExtraCodecs.INSTANT_ISO8601.fieldOf("lastPlayedTime").forGetter(QuickPlayEntry::lastPlayedTime), (App)GameType.CODEC.fieldOf("gamemode").forGetter(QuickPlayEntry::gamemode)).apply((Applicative)i, QuickPlayEntry::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\quickplay\QuickPlayLog$QuickPlayEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */