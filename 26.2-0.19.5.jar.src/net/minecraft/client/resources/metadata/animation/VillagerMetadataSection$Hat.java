/*    */ package net.minecraft.client.resources.metadata.animation;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Hat implements StringRepresentable {
/* 18 */   NONE("none"),
/* 19 */   PARTIAL("partial"),
/* 20 */   FULL("full");
/*    */   
/*    */   public static final Codec<Hat> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 22 */     CODEC = (Codec<Hat>)StringRepresentable.fromEnum(Hat::values);
/*    */   }
/*    */   
/*    */   Hat(String name) {
/* 27 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 32 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\metadata\animation\VillagerMetadataSection$Hat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */