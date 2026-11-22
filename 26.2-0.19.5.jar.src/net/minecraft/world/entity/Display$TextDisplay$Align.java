/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Align implements StringRepresentable {
/* 659 */   CENTER("center"),
/* 660 */   LEFT("left"),
/* 661 */   RIGHT("right");
/*     */   
/*     */   public static final Codec<Align> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 663 */     CODEC = (Codec<Align>)StringRepresentable.fromEnum(Align::values);
/*     */   }
/*     */   
/*     */   Align(String name) {
/* 668 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 673 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Display$TextDisplay$Align.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */