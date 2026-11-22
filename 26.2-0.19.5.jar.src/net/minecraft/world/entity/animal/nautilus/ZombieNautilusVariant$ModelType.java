/*    */ package net.minecraft.world.entity.animal.nautilus;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum ModelType implements StringRepresentable {
/* 46 */   NORMAL("normal"),
/* 47 */   WARM("warm");
/*    */   
/*    */   public static final Codec<ModelType> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 49 */     CODEC = (Codec<ModelType>)StringRepresentable.fromEnum(ModelType::values);
/*    */   }
/*    */   
/*    */   ModelType(String name) {
/* 54 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 59 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\nautilus\ZombieNautilusVariant$ModelType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */