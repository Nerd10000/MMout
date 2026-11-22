/*      */ package net.minecraft.server.level;
/*      */ 
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.kinds.Applicative;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import java.util.function.BiFunction;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ 
/*      */ public final class RespawnConfig extends Record {
/*      */   private final LevelData.RespawnData respawnData;
/*      */   
/*      */   private final boolean forced;
/*      */   
/*      */   public static final Codec<RespawnConfig> CODEC;
/*      */   
/*      */   public RespawnConfig(LevelData.RespawnData respawnData, boolean forced) {
/* 2402 */     this.respawnData = respawnData;
/* 2402 */     this.forced = forced;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2402	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2402	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2402	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public LevelData.RespawnData respawnData() {
/* 2402 */     return this.respawnData;
/*      */   }
/*      */   
/*      */   public boolean forced() {
/* 2402 */     return this.forced;
/*      */   }
/*      */   
/*      */   static {
/* 2406 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)LevelData.RespawnData.MAP_CODEC.forGetter(RespawnConfig::respawnData), (App)Codec.BOOL.optionalFieldOf("forced", Boolean.valueOf(false)).forGetter(RespawnConfig::forced)).apply((Applicative)i, RespawnConfig::new));
/*      */   }
/*      */   
/*      */   private static ResourceKey<Level> getDimensionOrDefault(RespawnConfig respawnConfig) {
/* 2412 */     return (respawnConfig != null) ? respawnConfig.respawnData().dimension() : Level.OVERWORLD;
/*      */   }
/*      */   
/*      */   public boolean isSamePosition(RespawnConfig other) {
/* 2416 */     return (other != null && this.respawnData.globalPos().equals(other.respawnData.globalPos()));
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerPlayer$RespawnConfig.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */