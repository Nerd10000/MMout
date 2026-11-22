/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public final class GlobalPos extends Record {
/*    */   private final ResourceKey<Level> dimension;
/*    */   
/*    */   private final BlockPos pos;
/*    */   
/*    */   public static final MapCodec<GlobalPos> MAP_CODEC;
/*    */   
/*    */   public GlobalPos(ResourceKey<Level> dimension, BlockPos pos) {
/* 12 */     this.dimension = dimension;
/* 12 */     this.pos = pos;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/GlobalPos;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/GlobalPos;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/GlobalPos;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/GlobalPos;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceKey<Level> dimension() {
/* 12 */     return this.dimension;
/*    */   }
/*    */   
/*    */   public BlockPos pos() {
/* 12 */     return this.pos;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(GlobalPos::dimension), (App)BlockPos.CODEC.fieldOf("pos").forGetter(GlobalPos::pos)).apply((Applicative)i, GlobalPos::of));
/*    */   }
/*    */   
/* 20 */   public static final Codec<GlobalPos> CODEC = MAP_CODEC.codec();
/*    */   
/* 22 */   public static final StreamCodec<ByteBuf, GlobalPos> STREAM_CODEC = StreamCodec.composite(
/* 23 */       ResourceKey.streamCodec(Registries.DIMENSION), GlobalPos::dimension, BlockPos.STREAM_CODEC, GlobalPos::pos, GlobalPos::of);
/*    */   
/*    */   public static GlobalPos of(ResourceKey<Level> dimension, BlockPos pos) {
/* 29 */     return new GlobalPos(dimension, pos);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 34 */     return String.valueOf(this.dimension) + " " + String.valueOf(this.dimension);
/*    */   }
/*    */   
/*    */   public boolean isCloseEnough(ResourceKey<Level> dimension, BlockPos pos, int maxDistance) {
/* 38 */     return (this.dimension.equals(dimension) && this.pos.distChessboard(pos) <= maxDistance);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\GlobalPos.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */