/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum BedPart implements StringRepresentable {
/*  7 */   HEAD("head"),
/*  8 */   FOOT("foot");
/*    */   
/*    */   public static final Codec<BedPart> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 11 */     CODEC = (Codec<BedPart>)StringRepresentable.fromEnum(BedPart::values);
/*    */   }
/*    */   
/*    */   BedPart(String name) {
/* 16 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 21 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 26 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\BedPart.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */