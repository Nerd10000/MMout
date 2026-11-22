/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public enum Relative {
/*  12 */   X(0),
/*  13 */   Y(1),
/*  14 */   Z(2),
/*  15 */   Y_ROT(3),
/*  16 */   X_ROT(4),
/*  17 */   DELTA_X(5),
/*  18 */   DELTA_Y(6),
/*  19 */   DELTA_Z(7),
/*  20 */   ROTATE_DELTA(8);
/*     */   
/*     */   public static final Set<Relative> ALL;
/*     */   
/*     */   public static final Set<Relative> ROTATION;
/*     */   
/*     */   public static final Set<Relative> DELTA;
/*     */   
/*     */   public static final StreamCodec<ByteBuf, Set<Relative>> SET_STREAM_CODEC;
/*     */   
/*     */   private final int bit;
/*     */   
/*     */   static {
/*  22 */     ALL = Set.of(values());
/*  23 */     ROTATION = Set.of(X_ROT, Y_ROT);
/*  24 */     DELTA = Set.of(DELTA_X, DELTA_Y, DELTA_Z, ROTATE_DELTA);
/*  74 */     SET_STREAM_CODEC = ByteBufCodecs.INT.map(Relative::unpack, Relative::pack);
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static Set<Relative> union(Set<Relative>... sets) {
/*     */     HashSet<Relative> set = new HashSet<>();
/*     */     for (Set<Relative> s : sets)
/*     */       set.addAll(s); 
/*     */     return set;
/*     */   }
/*     */   
/*     */   public static Set<Relative> rotation(boolean relativeYRot, boolean relativeXRot) {
/*     */     Set<Relative> relatives = EnumSet.noneOf(Relative.class);
/*     */     if (relativeYRot)
/*     */       relatives.add(Y_ROT); 
/*     */     if (relativeXRot)
/*     */       relatives.add(X_ROT); 
/*     */     return relatives;
/*     */   }
/*     */   
/*     */   public static Set<Relative> position(boolean relativeX, boolean relativeY, boolean relativeZ) {
/*     */     Set<Relative> relatives = EnumSet.noneOf(Relative.class);
/*     */     if (relativeX)
/*     */       relatives.add(X); 
/*     */     if (relativeY)
/*     */       relatives.add(Y); 
/*     */     if (relativeZ)
/*     */       relatives.add(Z); 
/*     */     return relatives;
/*     */   }
/*     */   
/*     */   public static Set<Relative> direction(boolean relativeX, boolean relativeY, boolean relativeZ) {
/*     */     Set<Relative> relatives = EnumSet.noneOf(Relative.class);
/*     */     if (relativeX)
/*     */       relatives.add(DELTA_X); 
/*     */     if (relativeY)
/*     */       relatives.add(DELTA_Y); 
/*     */     if (relativeZ)
/*     */       relatives.add(DELTA_Z); 
/*     */     return relatives;
/*     */   }
/*     */   
/*     */   Relative(int bit) {
/*  79 */     this.bit = bit;
/*     */   }
/*     */   
/*     */   private int getMask() {
/*  83 */     return 1 << this.bit;
/*     */   }
/*     */   
/*     */   private boolean isSet(int value) {
/*  87 */     return ((value & getMask()) == getMask());
/*     */   }
/*     */   
/*     */   public static Set<Relative> unpack(int value) {
/*  91 */     Set<Relative> result = EnumSet.noneOf(Relative.class);
/*  93 */     for (Relative argument : values()) {
/*  94 */       if (argument.isSet(value))
/*  95 */         result.add(argument); 
/*     */     } 
/*  99 */     return result;
/*     */   }
/*     */   
/*     */   public static int pack(Set<Relative> set) {
/* 103 */     int result = 0;
/* 105 */     for (Relative argument : set)
/* 106 */       result |= argument.getMask(); 
/* 109 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Relative.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */