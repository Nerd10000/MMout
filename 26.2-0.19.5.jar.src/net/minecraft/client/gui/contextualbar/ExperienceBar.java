/*    */ package net.minecraft.client.gui.contextualbar;
/*    */ 
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ExperienceBar implements ContextualBar {
/* 11 */   private static final Identifier EXPERIENCE_BAR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/experience_bar_background");
/*    */   
/* 12 */   private static final Identifier EXPERIENCE_BAR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/experience_bar_progress");
/*    */   
/*    */   private final Minecraft minecraft;
/*    */   
/*    */   public ExperienceBar(Minecraft minecraft) {
/* 17 */     this.minecraft = minecraft;
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
/* 22 */     LocalPlayer player = this.minecraft.player;
/* 24 */     int left = left(this.minecraft.getWindow());
/* 25 */     int top = top(this.minecraft.getWindow());
/* 27 */     int xpNeededForNextLevel = player.getXpNeededForNextLevel();
/* 28 */     if (xpNeededForNextLevel > 0) {
/* 29 */       int progress = (int)(player.experienceProgress * 183.0F);
/* 31 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EXPERIENCE_BAR_BACKGROUND_SPRITE, left, top, 182, 5);
/* 32 */       if (progress > 0)
/* 33 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EXPERIENCE_BAR_PROGRESS_SPRITE, 182, 5, 0, 0, left, top, progress, 5); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public void extractRenderState(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\contextualbar\ExperienceBar.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */