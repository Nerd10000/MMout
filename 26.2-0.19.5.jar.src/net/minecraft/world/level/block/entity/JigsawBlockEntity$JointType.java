/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum JointType implements StringRepresentable {
/* 34 */   ROLLABLE("rollable"),
/* 35 */   ALIGNED("aligned");
/*    */   
/*    */   public static final StringRepresentable.EnumCodec<JointType> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 37 */     CODEC = StringRepresentable.fromEnum(JointType::values);
/*    */   }
/*    */   
/*    */   JointType(String name) {
/* 42 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 47 */     return this.name;
/*    */   }
/*    */   
/*    */   public Component getTranslatedName() {
/* 51 */     return (Component)Component.translatable("jigsaw_block.joint." + this.name);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\JigsawBlockEntity$JointType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */