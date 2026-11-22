/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public enum Types implements StringRepresentable {
/* 42 */   WORLD_SURFACE_WG(0, "WORLD_SURFACE_WG", Heightmap.Usage.WORLDGEN, Heightmap.NOT_AIR),
/* 43 */   WORLD_SURFACE(1, "WORLD_SURFACE", Heightmap.Usage.CLIENT, Heightmap.NOT_AIR),
/* 44 */   OCEAN_FLOOR_WG(2, "OCEAN_FLOOR_WG", Heightmap.Usage.WORLDGEN, Heightmap.MATERIAL_MOTION_BLOCKING),
/* 45 */   OCEAN_FLOOR(3, "OCEAN_FLOOR", Heightmap.Usage.LIVE_WORLD, Heightmap.MATERIAL_MOTION_BLOCKING),
/* 45 */   MOTION_BLOCKING(3, "OCEAN_FLOOR", Heightmap.Usage.LIVE_WORLD, Heightmap.MATERIAL_MOTION_BLOCKING),
/* 45 */   MOTION_BLOCKING_NO_LEAVES(3, "OCEAN_FLOOR", Heightmap.Usage.LIVE_WORLD, Heightmap.MATERIAL_MOTION_BLOCKING);
/*    */   
/*    */   public static final Codec<Types> CODEC;
/*    */   
/*    */   private static final IntFunction<Types> BY_ID;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, Types> STREAM_CODEC;
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String serializationKey;
/*    */   
/*    */   private final Heightmap.Usage usage;
/*    */   
/*    */   private final Predicate<BlockState> isOpaque;
/*    */   
/*    */   static {
/* 46 */     MOTION_BLOCKING = new Types("MOTION_BLOCKING", 4, 4, "MOTION_BLOCKING", Heightmap.Usage.CLIENT, input -> (input.blocksMotion() || !input.getFluidState().isEmpty()));
/* 47 */     MOTION_BLOCKING_NO_LEAVES = new Types("MOTION_BLOCKING_NO_LEAVES", 5, 5, "MOTION_BLOCKING_NO_LEAVES", Heightmap.Usage.CLIENT, input -> ((input.blocksMotion() || !input.getFluidState().isEmpty()) && !(input.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock)));
/*    */   }
/*    */   
/*    */   static {
/* 51 */     CODEC = (Codec<Types>)StringRepresentable.fromEnum(Types::values);
/* 53 */     BY_ID = ByIdMap.continuous(t -> t.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 54 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, t -> t.id);
/*    */   }
/*    */   
/*    */   Types(int id, String serializationKey, Heightmap.Usage usage, Predicate<BlockState> isOpaque) {
/* 62 */     this.id = id;
/* 63 */     this.serializationKey = serializationKey;
/* 64 */     this.usage = usage;
/* 65 */     this.isOpaque = isOpaque;
/*    */   }
/*    */   
/*    */   public String getSerializationKey() {
/* 69 */     return this.serializationKey;
/*    */   }
/*    */   
/*    */   public boolean sendToClient() {
/* 73 */     return (this.usage == Heightmap.Usage.CLIENT);
/*    */   }
/*    */   
/*    */   public boolean keepAfterWorldgen() {
/* 77 */     return (this.usage != Heightmap.Usage.WORLDGEN);
/*    */   }
/*    */   
/*    */   public Predicate<BlockState> isOpaque() {
/* 81 */     return this.isOpaque;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 86 */     return this.serializationKey;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\Heightmap$Types.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */