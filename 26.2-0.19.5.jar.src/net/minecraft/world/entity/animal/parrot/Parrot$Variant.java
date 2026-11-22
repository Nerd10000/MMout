/*     */ package net.minecraft.world.entity.animal.parrot;
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
/* 142 */   RED_BLUE(0, "red_blue"),
/* 143 */   BLUE(1, "blue"),
/* 144 */   GREEN(2, "green"),
/* 145 */   YELLOW_BLUE(3, "yellow_blue"),
/* 146 */   GRAY(4, "gray");
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
/* 149 */     DEFAULT = RED_BLUE;
/* 151 */     BY_ID = ByIdMap.continuous(Variant::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/* 152 */     CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/* 154 */     Objects.requireNonNull(BY_ID);
/* 154 */     LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::getId);
/* 156 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*     */   }
/*     */   
/*     */   Variant(int id, String name) {
/* 162 */     this.id = id;
/* 163 */     this.name = name;
/*     */   }
/*     */   
/*     */   public int getId() {
/* 167 */     return this.id;
/*     */   }
/*     */   
/*     */   public static Variant byId(int id) {
/* 171 */     return BY_ID.apply(id);
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 176 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\parrot\Parrot$Variant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */