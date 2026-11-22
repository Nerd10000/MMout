/*    */ package net.minecraft.world.level.gameevent;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class BlockPositionSource extends Record implements PositionSource {
/*    */   private final BlockPos pos;
/*    */   
/*    */   public static final MapCodec<BlockPositionSource> CODEC;
/*    */   
/*    */   public BlockPositionSource(BlockPos pos) {
/* 13 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/gameevent/BlockPositionSource;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/BlockPositionSource;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/gameevent/BlockPositionSource;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/gameevent/BlockPositionSource;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/gameevent/BlockPositionSource;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/gameevent/BlockPositionSource;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public BlockPos pos() {
/* 13 */     return this.pos;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockPos.CODEC.fieldOf("pos").forGetter(BlockPositionSource::pos)).apply((Applicative)i, BlockPositionSource::new));
/*    */   }
/*    */   
/* 18 */   public static final StreamCodec<ByteBuf, BlockPositionSource> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, BlockPositionSource::pos, BlockPositionSource::new);
/*    */   
/*    */   public Optional<Vec3> getPosition(Level level) {
/* 25 */     return Optional.of(Vec3.atCenterOf((Vec3i)this.pos));
/*    */   }
/*    */   
/*    */   public PositionSourceType<BlockPositionSource> getType() {
/* 30 */     return PositionSourceType.BLOCK;
/*    */   }
/*    */   
/*    */   public static class Type implements PositionSourceType<BlockPositionSource> {
/*    */     public MapCodec<BlockPositionSource> codec() {
/* 36 */       return BlockPositionSource.CODEC;
/*    */     }
/*    */     
/*    */     public StreamCodec<ByteBuf, BlockPositionSource> streamCodec() {
/* 41 */       return BlockPositionSource.STREAM_CODEC;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gameevent\BlockPositionSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */