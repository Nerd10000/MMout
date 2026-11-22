/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum BoundingBoxType implements StringRepresentable {
/* 19 */   PIECE("piece"),
/* 20 */   STRUCTURE("full");
/*    */   
/*    */   public static final Codec<BoundingBoxType> CODEC;
/*    */   
/*    */   private final String id;
/*    */   
/*    */   static {
/* 22 */     CODEC = (Codec<BoundingBoxType>)StringRepresentable.fromEnum(BoundingBoxType::values);
/*    */   }
/*    */   
/*    */   BoundingBoxType(String id) {
/* 27 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 32 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\StructureSpawnOverride$BoundingBoxType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */