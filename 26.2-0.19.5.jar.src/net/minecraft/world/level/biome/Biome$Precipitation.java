/*    */ package net.minecraft.world.level.biome;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Precipitation implements StringRepresentable {
/* 70 */   NONE("none"),
/* 71 */   RAIN("rain"),
/* 72 */   SNOW("snow");
/*    */   
/*    */   public static final Codec<Precipitation> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 75 */     CODEC = (Codec<Precipitation>)StringRepresentable.fromEnum(Precipitation::values);
/*    */   }
/*    */   
/*    */   Precipitation(String name) {
/* 80 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 85 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Biome$Precipitation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */