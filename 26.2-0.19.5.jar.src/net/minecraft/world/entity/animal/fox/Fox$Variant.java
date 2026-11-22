/*     */ package net.minecraft.world.entity.animal.fox;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ 
/*     */ public enum Variant implements StringRepresentable {
/* 171 */   RED(0, "red"),
/* 172 */   SNOW(1, "snow");
/*     */   
/*     */   public static final Variant DEFAULT;
/*     */   
/*     */   public static final Codec<Variant> CODEC;
/*     */   
/*     */   private static final IntFunction<Variant> BY_ID;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 174 */     DEFAULT = RED;
/* 176 */     CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/* 178 */     BY_ID = ByIdMap.continuous(Variant::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 180 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*     */   }
/*     */   
/*     */   Variant(int id, String name) {
/* 186 */     this.id = id;
/* 187 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 192 */     return this.name;
/*     */   }
/*     */   
/*     */   public int getId() {
/* 196 */     return this.id;
/*     */   }
/*     */   
/*     */   public static Variant byId(int id) {
/* 200 */     return BY_ID.apply(id);
/*     */   }
/*     */   
/*     */   public static Variant byBiome(Holder<Biome> biome) {
/* 204 */     return biome.is(BiomeTags.SPAWNS_SNOW_FOXES) ? SNOW : RED;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$Variant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */