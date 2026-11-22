/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.math.OctahedralGroup;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public enum Rotation implements StringRepresentable {
/*  20 */   NONE(0, "none", OctahedralGroup.IDENTITY),
/*  21 */   CLOCKWISE_90(1, "clockwise_90", OctahedralGroup.ROT_90_Y_NEG),
/*  22 */   CLOCKWISE_180(2, "180", OctahedralGroup.ROT_180_FACE_XZ),
/*  23 */   COUNTERCLOCKWISE_90(3, "counterclockwise_90", OctahedralGroup.ROT_90_Y_POS);
/*     */   
/*     */   public static final IntFunction<Rotation> BY_ID;
/*     */   
/*     */   public static final Codec<Rotation> CODEC;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Rotation> STREAM_CODEC;
/*     */   
/*     */   @Deprecated
/*     */   public static final Codec<Rotation> LEGACY_CODEC;
/*     */   
/*     */   private final int index;
/*     */   
/*     */   private final String id;
/*     */   
/*     */   private final OctahedralGroup rotation;
/*     */   
/*     */   static {
/*  26 */     BY_ID = ByIdMap.continuous(Rotation::getIndex, (Object[])values(), ByIdMap.OutOfBoundsStrategy.WRAP);
/*  28 */     CODEC = (Codec<Rotation>)StringRepresentable.fromEnum(Rotation::values);
/*  29 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Rotation::getIndex);
/*  32 */     LEGACY_CODEC = ExtraCodecs.legacyEnum(Rotation::valueOf);
/*     */   }
/*     */   
/*     */   Rotation(int index, String id, OctahedralGroup rotation) {
/*  39 */     this.index = index;
/*  40 */     this.id = id;
/*  41 */     this.rotation = rotation;
/*     */   }
/*     */   
/*     */   public Rotation getRotated(Rotation rot) {
/*  45 */     switch (rot.ordinal()) {
/*     */       case 2:
/*  46 */         switch (ordinal()) {
/*     */           default:
/*  46 */             throw new MatchException(null, null);
/*     */           case 0:
/*     */           
/*     */           case 1:
/*     */           
/*     */           case 2:
/*     */           
/*     */           case 3:
/*     */             break;
/*     */         } 
/*     */       case 3:
/*  52 */         switch (ordinal()) {
/*     */           default:
/*  52 */             throw new MatchException(null, null);
/*     */           case 0:
/*     */           
/*     */           case 1:
/*     */           
/*     */           case 2:
/*     */           
/*     */           case 3:
/*     */             break;
/*     */         } 
/*     */       case 1:
/*  58 */         switch (ordinal()) {
/*     */           default:
/*  58 */             throw new MatchException(null, null);
/*     */           case 0:
/*     */           
/*     */           case 1:
/*     */           
/*     */           case 2:
/*     */           
/*     */           case 3:
/*     */             break;
/*     */         } 
/*     */     } 
/*  64 */     return this;
/*     */   }
/*     */   
/*     */   public OctahedralGroup rotation() {
/*  69 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public Direction rotate(Direction direction) {
/*  73 */     if (direction.getAxis() == Direction.Axis.Y)
/*  74 */       return direction; 
/*  76 */     switch (ordinal()) {
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 1:
/*     */       
/*     */     } 
/*  76 */     return 
/*     */       
/*  80 */       direction;
/*     */   }
/*     */   
/*     */   public int rotate(int rotation, int steps) {
/*  85 */     switch (ordinal()) {
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 1:
/*     */       
/*     */     } 
/*  85 */     return 
/*     */       
/*  89 */       rotation;
/*     */   }
/*     */   
/*     */   public static Rotation getRandom(RandomSource random) {
/*  94 */     return (Rotation)Util.getRandom((Object[])values(), random);
/*     */   }
/*     */   
/*     */   public static List<Rotation> getShuffled(RandomSource random) {
/*  98 */     return Util.shuffledCopy((Object[])values(), random);
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 103 */     return this.id;
/*     */   }
/*     */   
/*     */   private int getIndex() {
/* 107 */     return this.index;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\Rotation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */