/*     */ package net.minecraft.world.entity.animal.rabbit;
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
/* 112 */   BROWN(0, "brown"),
/* 113 */   WHITE(1, "white"),
/* 114 */   BLACK(2, "black"),
/* 115 */   WHITE_SPLOTCHED(3, "white_splotched"),
/* 116 */   GOLD(4, "gold"),
/* 117 */   SALT(5, "salt"),
/* 118 */   EVIL(99, "evil");
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
/* 121 */     DEFAULT = BROWN;
/* 123 */     BY_ID = ByIdMap.sparse(Variant::id, (Object[])values(), DEFAULT);
/* 125 */     CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/* 127 */     Objects.requireNonNull(BY_ID);
/* 127 */     LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::id);
/* 129 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::id);
/*     */   }
/*     */   
/*     */   Variant(int id, String name) {
/* 135 */     this.id = id;
/* 136 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 141 */     return this.name;
/*     */   }
/*     */   
/*     */   public int id() {
/* 145 */     return this.id;
/*     */   }
/*     */   
/*     */   public static Variant byId(int id) {
/* 149 */     return BY_ID.apply(id);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\rabbit\Rabbit$Variant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */