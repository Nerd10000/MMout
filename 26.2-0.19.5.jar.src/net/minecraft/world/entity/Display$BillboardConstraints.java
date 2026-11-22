/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum BillboardConstraints implements StringRepresentable {
/* 104 */   FIXED((byte)0, "fixed"),
/* 105 */   VERTICAL((byte)1, "vertical"),
/* 106 */   HORIZONTAL((byte)2, "horizontal"),
/* 107 */   CENTER((byte)3, "center");
/*     */   
/*     */   public static final Codec<BillboardConstraints> CODEC;
/*     */   
/*     */   public static final IntFunction<BillboardConstraints> BY_ID;
/*     */   
/*     */   private final byte id;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 110 */     CODEC = (Codec<BillboardConstraints>)StringRepresentable.fromEnum(BillboardConstraints::values);
/* 111 */     BY_ID = ByIdMap.continuous(BillboardConstraints::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */   }
/*     */   
/*     */   BillboardConstraints(byte id, String name) {
/* 117 */     this.name = name;
/* 118 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 123 */     return this.name;
/*     */   }
/*     */   
/*     */   private byte getId() {
/* 127 */     return this.id;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$BillboardConstraints.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */