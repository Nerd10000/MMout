/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Attachment implements StringRepresentable {
/* 15 */   WALL("wall"),
/* 16 */   CEILING("ceiling"),
/* 17 */   CEILING_MIDDLE("ceiling_middle");
/*    */   
/*    */   public static final Codec<Attachment> CODEC;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   static {
/* 20 */     CODEC = (Codec<Attachment>)StringRepresentable.fromEnum(Attachment::values);
/*    */   }
/*    */   
/*    */   Attachment(String name) {
/* 25 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 30 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\HangingSignBlock$Attachment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */