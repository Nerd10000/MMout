/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*    */ import org.joml.Vector3f;
/*    */ import org.joml.Vector3fc;
/*    */ 
/*    */ public class HangingSignEditScreen extends AbstractSignEditScreen {
/*    */   public static final float MAGIC_BACKGROUND_SCALE = 4.5F;
/*    */   
/* 13 */   private static final Vector3fc TEXT_SCALE = (Vector3fc)new Vector3f(1.0F, 1.0F, 1.0F);
/*    */   
/*    */   private static final int TEXTURE_WIDTH = 16;
/*    */   
/*    */   private static final int TEXTURE_HEIGHT = 16;
/*    */   
/*    */   private final Identifier texture;
/*    */   
/*    */   public HangingSignEditScreen(SignBlockEntity sign, boolean isFrontText, boolean shouldFilter) {
/* 19 */     super(sign, isFrontText, shouldFilter, (Component)Component.translatable("hanging_sign.edit"));
/* 20 */     this.texture = Identifier.withDefaultNamespace("textures/gui/hanging_signs/" + this.woodType.name() + ".png");
/*    */   }
/*    */   
/*    */   protected float getSignYOffset() {
/* 25 */     return 125.0F;
/*    */   }
/*    */   
/*    */   protected void extractSignBackground(GuiGraphicsExtractor graphics) {
/* 30 */     graphics.pose().translate(0.0F, -13.0F);
/* 31 */     graphics.pose().scale(4.5F, 4.5F);
/* 33 */     graphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, -8, -8, 0.0F, 0.0F, 16, 16, 16, 16);
/*    */   }
/*    */   
/*    */   protected Vector3fc getSignTextScale() {
/* 38 */     return TEXT_SCALE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\HangingSignEditScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */