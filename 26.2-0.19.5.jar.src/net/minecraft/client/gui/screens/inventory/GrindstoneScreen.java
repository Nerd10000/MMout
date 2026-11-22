/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.GrindstoneMenu;
/*    */ 
/*    */ public class GrindstoneScreen extends AbstractContainerScreen<GrindstoneMenu> {
/* 11 */   private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/grindstone/error");
/*    */   
/* 12 */   private static final Identifier GRINDSTONE_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/grindstone.png");
/*    */   
/*    */   public GrindstoneScreen(GrindstoneMenu menu, Inventory inventory, Component title) {
/* 15 */     super(menu, inventory, title);
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 20 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 21 */     int xo = (this.width - this.imageWidth) / 2;
/* 22 */     int yo = (this.height - this.imageHeight) / 2;
/* 23 */     graphics.blit(RenderPipelines.GUI_TEXTURED, GRINDSTONE_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/* 25 */     if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(2).hasItem())
/* 26 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + 92, yo + 31, 28, 21); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\GrindstoneScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */