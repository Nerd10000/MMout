/*    */ package net.minecraft.world.entity.animal.cow;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum ModelType implements StringRepresentable {
/* 52 */   NORMAL("normal"),
/* 53 */   COLD("cold"),
/* 54 */   WARM("warm");
/*    */   
/*    */   public static final Codec<ModelType> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 56 */     CODEC = (Codec<ModelType>)StringRepresentable.fromEnum(ModelType::values);
/*    */   }
/*    */   
/*    */   ModelType(String name) {
/* 61 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 66 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\cow\CowVariant$ModelType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */