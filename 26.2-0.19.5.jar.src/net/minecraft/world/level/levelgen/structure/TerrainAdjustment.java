/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum TerrainAdjustment implements StringRepresentable {
/*  8 */   NONE("none"),
/*  9 */   BURY("bury"),
/* 11 */   BEARD_THIN("beard_thin"),
/* 12 */   BEARD_BOX("beard_box"),
/* 13 */   ENCAPSULATE("encapsulate");
/*    */   
/*    */   public static final Codec<TerrainAdjustment> CODEC;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   static {
/* 16 */     CODEC = (Codec<TerrainAdjustment>)StringRepresentable.fromEnum(TerrainAdjustment::values);
/*    */   }
/*    */   
/*    */   TerrainAdjustment(String id) {
/* 21 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 26 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\TerrainAdjustment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */