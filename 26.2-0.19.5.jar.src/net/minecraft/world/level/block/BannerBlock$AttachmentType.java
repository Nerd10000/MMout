/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum AttachmentType implements StringRepresentable {
/* 83 */   WALL("wall"),
/* 84 */   GROUND("ground");
/*    */   
/*    */   public static final Codec<AttachmentType> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 87 */     CODEC = (Codec<AttachmentType>)StringRepresentable.fromEnum(AttachmentType::values);
/*    */   }
/*    */   
/*    */   AttachmentType(String name) {
/* 92 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 97 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BannerBlock$AttachmentType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */