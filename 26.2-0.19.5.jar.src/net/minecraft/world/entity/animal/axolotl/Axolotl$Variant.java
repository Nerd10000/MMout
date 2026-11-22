/*     */ package net.minecraft.world.entity.animal.axolotl;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public enum Variant implements StringRepresentable {
/* 138 */   LUCY(0, "lucy", true),
/* 139 */   WILD(1, "wild", true),
/* 140 */   GOLD(2, "gold", true),
/* 141 */   CYAN(3, "cyan", true),
/* 142 */   BLUE(4, "blue", false);
/*     */   
/*     */   public static final Variant DEFAULT;
/*     */   
/*     */   private static final IntFunction<Variant> BY_ID;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*     */   
/*     */   public static final Codec<Variant> CODEC;
/*     */   
/*     */   @Deprecated
/*     */   public static final Codec<Variant> LEGACY_CODEC;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final boolean common;
/*     */   
/*     */   static {
/* 144 */     DEFAULT = LUCY;
/* 146 */     BY_ID = ByIdMap.continuous(Variant::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 148 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/* 150 */     CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/* 152 */     Objects.requireNonNull(BY_ID);
/* 152 */     LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::getId);
/*     */   }
/*     */   
/*     */   Variant(int id, String name, boolean common) {
/* 159 */     this.id = id;
/* 160 */     this.name = name;
/* 161 */     this.common = common;
/*     */   }
/*     */   
/*     */   public int getId() {
/* 165 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 169 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 174 */     return this.name;
/*     */   }
/*     */   
/*     */   public static Variant byId(int id) {
/* 178 */     return BY_ID.apply(id);
/*     */   }
/*     */   
/*     */   public static Variant getCommonSpawnVariant(RandomSource random) {
/* 182 */     return getSpawnVariant(random, true);
/*     */   }
/*     */   
/*     */   public static Variant getRareSpawnVariant(RandomSource random) {
/* 186 */     return getSpawnVariant(random, false);
/*     */   }
/*     */   
/*     */   private static Variant getSpawnVariant(RandomSource random, boolean common) {
/* 190 */     Variant[] validVariants = (Variant[])Arrays.<Variant>stream(values()).filter(v -> (v.common == common)).toArray(x$0 -> new Variant[x$0]);
/* 191 */     return (Variant)Util.getRandom((Object[])validVariants, random);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\axolotl\Axolotl$Variant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */