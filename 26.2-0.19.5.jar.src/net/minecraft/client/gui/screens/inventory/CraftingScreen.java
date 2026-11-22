/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.navigation.ScreenPosition;
/*    */ import net.minecraft.client.gui.screens.recipebook.CraftingRecipeBookComponent;
/*    */ import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractCraftingMenu;
/*    */ import net.minecraft.world.inventory.CraftingMenu;
/*    */ 
/*    */ public class CraftingScreen extends AbstractRecipeBookScreen<CraftingMenu> {
/* 13 */   private static final Identifier CRAFTING_TABLE_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/crafting_table.png");
/*    */   
/*    */   public CraftingScreen(CraftingMenu menu, Inventory inventory, Component title) {
/* 16 */     super(menu, (RecipeBookComponent<?>)new CraftingRecipeBookComponent((AbstractCraftingMenu)menu), inventory, title);
/*    */   }
/*    */   
/*    */   protected void init() {
/* 21 */     super.init();
/* 22 */     this.titleLabelX = 29;
/*    */   }
/*    */   
/*    */   protected ScreenPosition getRecipeBookButtonPosition() {
/* 27 */     return new ScreenPosition(this.leftPos + 5, this.height / 2 - 49);
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 32 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 33 */     int xo = this.leftPos;
/* 34 */     int yo = (this.height - this.imageHeight) / 2;
/* 35 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CRAFTING_TABLE_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\CraftingScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */