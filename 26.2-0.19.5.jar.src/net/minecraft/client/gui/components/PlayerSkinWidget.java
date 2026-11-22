/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.gui.ComponentPath;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.player.PlayerModelType;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ 
/*    */ public class PlayerSkinWidget extends AbstractWidget {
/*    */   private static final float MODEL_HEIGHT = 2.125F;
/*    */   
/*    */   private static final float FIT_SCALE = 0.97F;
/*    */   
/*    */   private static final float ROTATION_SENSITIVITY = 2.5F;
/*    */   
/*    */   private static final float DEFAULT_ROTATION_X = -5.0F;
/*    */   
/*    */   private static final float DEFAULT_ROTATION_Y = 30.0F;
/*    */   
/*    */   private static final float ROTATION_X_LIMIT = 50.0F;
/*    */   
/*    */   private final Model.Simple wideModel;
/*    */   
/*    */   private final Model.Simple slimModel;
/*    */   
/*    */   private final Supplier<PlayerSkin> skin;
/*    */   
/* 37 */   private float rotationX = -5.0F;
/*    */   
/* 38 */   private float rotationY = 30.0F;
/*    */   
/*    */   public PlayerSkinWidget(int width, int height, EntityModelSet models, Supplier<PlayerSkin> skin) {
/* 41 */     super(0, 0, width, height, CommonComponents.EMPTY);
/* 42 */     this.wideModel = new Model.Simple(models.bakeLayer(ModelLayers.PLAYER), RenderTypes::entityTranslucent);
/* 43 */     this.slimModel = new Model.Simple(models.bakeLayer(ModelLayers.PLAYER_SLIM), RenderTypes::entityTranslucent);
/* 44 */     this.skin = skin;
/*    */   }
/*    */   
/*    */   protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 49 */     float scale = 0.97F * getHeight() / 2.125F;
/* 50 */     float pivotY = -1.0625F;
/* 51 */     PlayerSkin skin = this.skin.get();
/* 52 */     Model.Simple model = (skin.model() == PlayerModelType.SLIM) ? this.slimModel : this.wideModel;
/* 53 */     graphics.skin(model, skin.body().texturePath(), scale, this.rotationX, this.rotationY, -1.0625F, getX(), getY(), getRight(), getBottom());
/*    */   }
/*    */   
/*    */   protected void onDrag(MouseButtonEvent event, double dx, double dy) {
/* 58 */     this.rotationX = Mth.clamp(this.rotationX - (float)dy * 2.5F, -50.0F, 50.0F);
/* 59 */     this.rotationY += (float)dx * 2.5F;
/*    */   }
/*    */   
/*    */   public void playDownSound(SoundManager soundManager) {}
/*    */   
/*    */   protected void updateWidgetNarration(NarrationElementOutput output) {}
/*    */   
/*    */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 72 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\PlayerSkinWidget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */