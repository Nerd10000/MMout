/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum MipmapStrategy implements StringRepresentable {
/*  7 */   AUTO("auto"),
/*  8 */   MEAN("mean"),
/*  9 */   CUTOUT("cutout"),
/* 10 */   STRICT_CUTOUT("strict_cutout"),
/* 11 */   DARK_CUTOUT("dark_cutout");
/*    */   
/*    */   public static final Codec<MipmapStrategy> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 13 */     CODEC = StringRepresentable.fromValues(MipmapStrategy::values);
/*    */   }
/*    */   
/*    */   MipmapStrategy(String name) {
/* 18 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 23 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\MipmapStrategy.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */