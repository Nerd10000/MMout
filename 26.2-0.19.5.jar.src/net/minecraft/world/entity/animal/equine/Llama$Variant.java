/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Objects;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Variant implements StringRepresentable {
/*  88 */   CREAMY(0, "creamy"),
/*  89 */   WHITE(1, "white"),
/*  90 */   BROWN(2, "brown"),
/*  91 */   GRAY(3, "gray");
/*     */   
/*     */   public static final Variant DEFAULT;
/*     */   
/*     */   private static final IntFunction<Variant> BY_ID;
/*     */   
/*     */   public static final Codec<Variant> CODEC;
/*     */   
/*     */   @Deprecated
/*     */   public static final Codec<Variant> LEGACY_CODEC;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/*  94 */     DEFAULT = CREAMY;
/*  96 */     BY_ID = ByIdMap.continuous(Variant::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/*  97 */     CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/*  99 */     Objects.requireNonNull(BY_ID);
/*  99 */     LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::getId);
/* 101 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*     */   }
/*     */   
/*     */   Variant(int id, String name) {
/* 107 */     this.id = id;
/* 108 */     this.name = name;
/*     */   }
/*     */   
/*     */   public int getId() {
/* 112 */     return this.id;
/*     */   }
/*     */   
/*     */   public static Variant byId(int id) {
/* 116 */     return BY_ID.apply(id);
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 121 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\Llama$Variant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */