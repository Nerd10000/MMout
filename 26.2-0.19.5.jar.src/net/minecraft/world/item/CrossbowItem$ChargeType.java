/*     */ package net.minecraft.world.item;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum ChargeType implements StringRepresentable {
/* 278 */   NONE("none"),
/* 279 */   ARROW("arrow"),
/* 280 */   ROCKET("rocket");
/*     */   
/*     */   public static final Codec<ChargeType> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 283 */     CODEC = (Codec<ChargeType>)StringRepresentable.fromEnum(ChargeType::values);
/*     */   }
/*     */   
/*     */   ChargeType(String name) {
/* 288 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 293 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\CrossbowItem$ChargeType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */