/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.PlainSignBlock;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class SignEditScreen extends AbstractSignEditScreen {
/*    */   public static final float MAGIC_BACKGROUND_SCALE = 3.9F;
/*    */   
/*    */   public static final float MAGIC_TEXT_SCALE = 0.9765628F;
/*    */   
/*    */   private static final int TEXTURE_WIDTH = 24;
/*    */   
/*    */   private static final int TEXTURE_HEIGHT = 26;
/*    */   
/*    */   private static final int POST_HEIGHT = 14;
/*    */   
/* 17 */   private static final Vector3fc TEXT_SCALE = (Vector3fc)new Vector3f(0.9765628F, 0.9765628F, 0.9765628F);
/*    */   
/*    */   private final int displayedHeight;
/*    */   
/*    */   private final Identifier texture;
/*    */   
/*    */   public SignEditScreen(SignBlockEntity sign, boolean isFrontText, boolean shouldFilter) {
/* 22 */     super(sign, isFrontText, shouldFilter);
/* 23 */     this.texture = Identifier.withDefaultNamespace("textures/gui/signs/" + this.woodType.name() + ".png");
/* 24 */     boolean isWallSign = (PlainSignBlock.getAttachmentPoint(sign.getBlockState()) == PlainSignBlock.Attachment.WALL);
/* 25 */     this.displayedHeight = isWallSign ? 12 : 26;
/*    */   }
/*    */   
/*    */   protected float getSignYOffset() {
/* 30 */     return 90.0F;
/*    */   }
/*    */   
/*    */   protected void extractSignBackground(GuiGraphicsExtractor graphics) {
/* 35 */     graphics.pose().translate(0.0F, 27.0F);
/* 36 */     graphics.pose().scale(3.9F, 3.9F);
/* 38 */     graphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, -12, -13, 0.0F, 0.0F, 24, this.displayedHeight, 24, 26);
/*    */   }
/*    */   
/*    */   protected Vector3fc getSignTextScale() {
/* 43 */     return TEXT_SCALE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\SignEditScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */