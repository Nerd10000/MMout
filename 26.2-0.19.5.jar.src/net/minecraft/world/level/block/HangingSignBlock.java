/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public interface HangingSignBlock {
/*    */   Attachment attachmentPoint(BlockState paramBlockState);
/*    */   
/*    */   static Attachment getAttachmentPoint(BlockState blockState) {
/* 11 */     Block block = blockState.getBlock();
/* 11 */     HangingSignBlock hangingSignBlock = (HangingSignBlock)block;
/* 11 */     return (block instanceof HangingSignBlock) ? hangingSignBlock.attachmentPoint(blockState) : Attachment.CEILING;
/*    */   }
/*    */   
/*    */   public enum Attachment implements StringRepresentable {
/* 15 */     WALL("wall"),
/* 16 */     CEILING("ceiling"),
/* 17 */     CEILING_MIDDLE("ceiling_middle");
/*    */     
/* 20 */     public static final Codec<Attachment> CODEC = (Codec<Attachment>)StringRepresentable.fromEnum(Attachment::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     static {
/*    */     
/*    */     }
/*    */     
/*    */     Attachment(String name) {
/* 25 */       this.name = name;
/*    */     }
/*    */     
/*    */     public String getSerializedName() {
/* 30 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\HangingSignBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */