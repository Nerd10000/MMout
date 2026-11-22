/*     */ package net.minecraft.world.item.component;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Shape implements StringRepresentable {
/* 105 */   SMALL_BALL(0, "small_ball"),
/* 106 */   LARGE_BALL(1, "large_ball"),
/* 107 */   STAR(2, "star"),
/* 108 */   CREEPER(3, "creeper"),
/* 109 */   BURST(4, "burst");
/*     */   
/*     */   private static final IntFunction<Shape> BY_ID;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Shape> STREAM_CODEC;
/*     */   
/*     */   public static final Codec<Shape> CODEC;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 112 */     BY_ID = ByIdMap.continuous(Shape::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 113 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Shape::getId);
/* 115 */     CODEC = StringRepresentable.fromValues(Shape::values);
/*     */   }
/*     */   
/*     */   Shape(int id, String name) {
/* 121 */     this.id = id;
/* 122 */     this.name = name;
/*     */   }
/*     */   
/*     */   public MutableComponent getName() {
/* 126 */     return Component.translatable("item.minecraft.firework_star.shape." + this.name);
/*     */   }
/*     */   
/*     */   public int getId() {
/* 130 */     return this.id;
/*     */   }
/*     */   
/*     */   public static Shape byId(int id) {
/* 134 */     return BY_ID.apply(id);
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 139 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\FireworkExplosion$Shape.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */