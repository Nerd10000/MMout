/*     */ package net.minecraft.world.level.dimension;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum Skybox implements StringRepresentable {
/* 154 */   NONE("none"),
/* 155 */   OVERWORLD("overworld"),
/* 156 */   END("end");
/*     */   
/*     */   public static final Codec<Skybox> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   static {
/* 159 */     CODEC = (Codec<Skybox>)StringRepresentable.fromEnum(Skybox::values);
/*     */   }
/*     */   
/*     */   Skybox(String name) {
/* 164 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 169 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\dimension\DimensionType$Skybox.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */