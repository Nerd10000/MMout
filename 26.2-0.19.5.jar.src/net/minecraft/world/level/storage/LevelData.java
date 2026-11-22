/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ 
/*    */ public interface LevelData {
/*    */   RespawnData getRespawnData();
/*    */   
/*    */   long getGameTime();
/*    */   
/*    */   boolean isHardcore();
/*    */   
/*    */   Difficulty getDifficulty();
/*    */   
/*    */   boolean isDifficultyLocked();
/*    */   
/*    */   public static final class RespawnData extends Record {
/*    */     private final GlobalPos globalPos;
/*    */     
/*    */     private final float yaw;
/*    */     
/*    */     private final float pitch;
/*    */     
/*    */     public RespawnData(GlobalPos globalPos, float yaw, float pitch) {
/* 19 */       this.globalPos = globalPos;
/* 19 */       this.yaw = yaw;
/* 19 */       this.pitch = pitch;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/LevelData$RespawnData;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelData$RespawnData;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelData$RespawnData;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelData$RespawnData;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelData$RespawnData;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #19	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelData$RespawnData;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public GlobalPos globalPos() {
/* 19 */       return this.globalPos;
/*    */     }
/*    */     
/*    */     public float yaw() {
/* 19 */       return this.yaw;
/*    */     }
/*    */     
/*    */     public float pitch() {
/* 19 */       return this.pitch;
/*    */     }
/*    */     
/* 20 */     public static final RespawnData DEFAULT = new RespawnData(GlobalPos.of(Level.OVERWORLD, BlockPos.ZERO), 0.0F, 0.0F);
/*    */     
/*    */     public static final MapCodec<RespawnData> MAP_CODEC;
/*    */     
/*    */     static {
/* 21 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)GlobalPos.MAP_CODEC.forGetter(RespawnData::globalPos), (App)Codec.floatRange(-180.0F, 180.0F).fieldOf("yaw").forGetter(RespawnData::yaw), (App)Codec.floatRange(-90.0F, 90.0F).fieldOf("pitch").forGetter(RespawnData::pitch)).apply((Applicative)i, RespawnData::new));
/*    */     }
/*    */     
/* 26 */     public static final Codec<RespawnData> CODEC = MAP_CODEC.codec();
/*    */     
/* 27 */     public static final StreamCodec<ByteBuf, RespawnData> STREAM_CODEC = StreamCodec.composite(GlobalPos.STREAM_CODEC, RespawnData::globalPos, ByteBufCodecs.FLOAT, RespawnData::yaw, ByteBufCodecs.FLOAT, RespawnData::pitch, RespawnData::new);
/*    */     
/*    */     public static RespawnData of(ResourceKey<Level> dimension, BlockPos pos, float yaw, float pitch) {
/* 35 */       return new RespawnData(GlobalPos.of(dimension, pos.immutable()), Mth.wrapDegrees(yaw), Mth.clamp(pitch, -90.0F, 90.0F));
/*    */     }
/*    */     
/*    */     public ResourceKey<Level> dimension() {
/* 39 */       return this.globalPos.dimension();
/*    */     }
/*    */     
/*    */     public BlockPos pos() {
/* 43 */       return this.globalPos.pos();
/*    */     }
/*    */   }
/*    */   
/*    */   default void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
/* 58 */     category.setDetail("Level spawn location", () -> CrashReportCategory.formatLocation(levelHeightAccessor, getRespawnData().pos()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */