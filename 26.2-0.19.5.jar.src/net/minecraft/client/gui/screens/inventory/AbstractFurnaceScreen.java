/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.navigation.ScreenPosition;
/*    */ import net.minecraft.client.gui.screens.recipebook.FurnaceRecipeBookComponent;
/*    */ import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractFurnaceMenu;
/*    */ 
/*    */ public abstract class AbstractFurnaceScreen<T extends AbstractFurnaceMenu> extends AbstractRecipeBookScreen<T> {
/*    */   private final Identifier texture;
/*    */   
/*    */   private final Identifier litProgressSprite;
/*    */   
/*    */   private final Identifier burnProgressSprite;
/*    */   
/*    */   public AbstractFurnaceScreen(T menu, Inventory inventory, Component title, Component recipeFilterName, Identifier texture, Identifier litProgressSprite, Identifier burnProgressSprite, List<RecipeBookComponent.TabInfo> tabInfos) {
/* 22 */     super(menu, (RecipeBookComponent<?>)new FurnaceRecipeBookComponent((AbstractFurnaceMenu)menu, recipeFilterName, tabInfos), inventory, title);
/* 24 */     this.texture = texture;
/* 25 */     this.litProgressSprite = litProgressSprite;
/* 26 */     this.burnProgressSprite = burnProgressSprite;
/*    */   }
/*    */   
/*    */   public void init() {
/* 31 */     super.init();
/* 32 */     this.titleLabelX = (this.imageWidth - this.font.width((FormattedText)this.title)) / 2;
/*    */   }
/*    */   
/*    */   protected ScreenPosition getRecipeBookButtonPosition() {
/* 37 */     return new ScreenPosition(this.leftPos + 20, this.height / 2 - 49);
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 42 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 43 */     int xo = this.leftPos;
/* 44 */     int yo = this.topPos;
/* 45 */     graphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/* 46 */     if (((AbstractFurnaceMenu)this.menu).isLit()) {
/* 47 */       int litSpriteHeight = 14;
/* 48 */       int litProgressHeight = Mth.ceil(((AbstractFurnaceMenu)this.menu).getLitProgress() * 13.0F) + 1;
/* 49 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.litProgressSprite, 14, 14, 0, 14 - litProgressHeight, xo + 56, yo + 36 + 14 - litProgressHeight, 14, litProgressHeight);
/*    */     } 
/* 52 */     int burnSpriteWidth = 24;
/* 53 */     int burnProgressWidth = Mth.ceil(((AbstractFurnaceMenu)this.menu).getBurnProgress() * 24.0F);
/* 54 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.burnProgressSprite, 24, 16, 0, 0, xo + 79, yo + 34, burnProgressWidth, 16);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\AbstractFurnaceScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */