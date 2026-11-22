/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ import net.minecraft.world.item.component.ResolvableProfile;
/*    */ 
/*    */ public class PlayerFaceExtractor {
/*    */   public static final int SKIN_HEAD_U = 8;
/*    */   
/*    */   public static final int SKIN_HEAD_V = 8;
/*    */   
/*    */   public static final int SKIN_HEAD_WIDTH = 8;
/*    */   
/*    */   public static final int SKIN_HEAD_HEIGHT = 8;
/*    */   
/*    */   public static final int SKIN_HAT_U = 40;
/*    */   
/*    */   public static final int SKIN_HAT_V = 8;
/*    */   
/*    */   public static final int SKIN_HAT_WIDTH = 8;
/*    */   
/*    */   public static final int SKIN_HAT_HEIGHT = 8;
/*    */   
/*    */   public static final int SKIN_TEX_WIDTH = 64;
/*    */   
/*    */   public static final int SKIN_TEX_HEIGHT = 64;
/*    */   
/*    */   public static void extractRenderState(GuiGraphicsExtractor graphics, ResolvableProfile skinProfile, int x, int y, int size) {
/* 25 */     PlayerSkinRenderCache skinCache = Minecraft.getInstance().playerSkinRenderCache();
/* 26 */     PlayerSkinRenderCache.RenderInfo renderInfo = skinCache.getOrDefault(skinProfile);
/* 27 */     extractRenderState(graphics, renderInfo.playerSkin(), x, y, size);
/*    */   }
/*    */   
/*    */   public static void extractRenderState(GuiGraphicsExtractor graphics, PlayerSkin skin, int x, int y, int size) {
/* 31 */     extractRenderState(graphics, skin, x, y, size, -1);
/*    */   }
/*    */   
/*    */   public static void extractRenderState(GuiGraphicsExtractor graphics, PlayerSkin skin, int x, int y, int size, int color) {
/* 35 */     extractRenderState(graphics, skin.body().texturePath(), x, y, size, true, false, color);
/*    */   }
/*    */   
/*    */   public static void extractRenderState(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int size, boolean hat, boolean flip, int color) {
/* 39 */     int skinHeadV = 8 + (flip ? 8 : 0);
/* 40 */     int skinHeadHeight = 8 * (flip ? -1 : 1);
/* 41 */     graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 8.0F, skinHeadV, size, size, 8, skinHeadHeight, 64, 64, color);
/* 43 */     if (hat)
/* 44 */       extractHat(graphics, texture, x, y, size, flip, color); 
/*    */   }
/*    */   
/*    */   private static void extractHat(GuiGraphicsExtractor graphics, Identifier texture, int x, int y, int size, boolean flip, int color) {
/* 49 */     int skinHatV = 8 + (flip ? 8 : 0);
/* 50 */     int skinHatHeight = 8 * (flip ? -1 : 1);
/* 52 */     graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 40.0F, skinHatV, size, size, 8, skinHatHeight, 64, 64, color);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\PlayerFaceExtractor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */