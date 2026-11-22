/*    */ package net.minecraft.world.entity.animal.chicken;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum ModelType implements StringRepresentable {
/* 52 */   NORMAL("normal"),
/* 53 */   COLD("cold");
/*    */   
/*    */   public static final Codec<ModelType> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 55 */     CODEC = (Codec<ModelType>)StringRepresentable.fromEnum(ModelType::values);
/*    */   }
/*    */   
/*    */   ModelType(String name) {
/* 60 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 65 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\chicken\ChickenVariant$ModelType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */