/*    */ package net.minecraft.client.renderer.blockentity.state;
/*    */ 
/*    */ import com.mojang.math.Transformation;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.block.BannerBlock;
/*    */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*    */ 
/*    */ public class BannerRenderState extends BlockEntityRenderState {
/*    */   public DyeColor baseColor;
/*    */   
/* 10 */   public BannerPatternLayers patterns = BannerPatternLayers.EMPTY;
/*    */   
/*    */   public float phase;
/*    */   
/* 12 */   public Transformation transformation = Transformation.IDENTITY;
/*    */   
/* 13 */   public BannerBlock.AttachmentType attachmentType = BannerBlock.AttachmentType.GROUND;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\blockentity\state\BannerRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */