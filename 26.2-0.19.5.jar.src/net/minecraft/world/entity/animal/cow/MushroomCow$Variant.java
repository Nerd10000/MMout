/*     */ package net.minecraft.world.entity.animal.cow;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public enum Variant implements StringRepresentable {
/* 277 */   RED("red", 0, Blocks.RED_MUSHROOM.defaultBlockState()),
/* 278 */   BROWN("brown", 1, Blocks.BROWN_MUSHROOM.defaultBlockState());
/*     */   
/*     */   public static final Variant DEFAULT;
/*     */   
/*     */   public static final Codec<Variant> CODEC;
/*     */   
/*     */   private static final IntFunction<Variant> BY_ID;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*     */   
/*     */   private final String type;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final BlockState blockState;
/*     */   
/*     */   static {
/* 280 */     DEFAULT = RED;
/* 282 */     CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/* 284 */     BY_ID = ByIdMap.continuous(Variant::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/* 286 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::id);
/*     */   }
/*     */   
/*     */   Variant(String type, int id, BlockState blockState) {
/* 293 */     this.type = type;
/* 294 */     this.id = id;
/* 295 */     this.blockState = blockState;
/*     */   }
/*     */   
/*     */   public BlockState getBlockState() {
/* 299 */     return this.blockState;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 304 */     return this.type;
/*     */   }
/*     */   
/*     */   private int id() {
/* 308 */     return this.id;
/*     */   }
/*     */   
/*     */   private static Variant byId(int id) {
/* 312 */     return BY_ID.apply(id);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\cow\MushroomCow$Variant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */