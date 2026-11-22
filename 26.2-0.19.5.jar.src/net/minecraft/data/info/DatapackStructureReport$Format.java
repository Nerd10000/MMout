/*     */ package net.minecraft.data.info;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ enum Format implements StringRepresentable {
/* 118 */   STRUCTURE("structure"),
/* 119 */   MCFUNCTION("mcfunction");
/*     */   
/*     */   public static final Codec<Format> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 122 */     CODEC = (Codec<Format>)StringRepresentable.fromEnum(Format::values);
/*     */   }
/*     */   
/*     */   Format(String name) {
/* 127 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 132 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\info\DatapackStructureReport$Format.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */