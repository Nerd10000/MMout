/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Variant implements StringRepresentable {
/* 166 */   SMALL("small", 0, 0.5F),
/* 167 */   MEDIUM("medium", 1, 1.0F),
/* 168 */   LARGE("large", 2, 1.5F);
/*     */   
/*     */   public static final Variant DEFAULT;
/*     */   
/*     */   public static final Codec<Variant> CODEC;
/*     */   
/*     */   private static final IntFunction<Variant> BY_ID;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final float boundingBoxScale;
/*     */   
/*     */   static {
/* 170 */     DEFAULT = MEDIUM;
/* 172 */     CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/* 174 */     BY_ID = ByIdMap.continuous(Variant::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
/* 176 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::id);
/*     */   }
/*     */   
/*     */   Variant(String name, int id, float boundingBoxScale) {
/* 183 */     this.name = name;
/* 184 */     this.id = id;
/* 185 */     this.boundingBoxScale = boundingBoxScale;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 190 */     return this.name;
/*     */   }
/*     */   
/*     */   private int id() {
/* 194 */     return this.id;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fish\Salmon$Variant.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */