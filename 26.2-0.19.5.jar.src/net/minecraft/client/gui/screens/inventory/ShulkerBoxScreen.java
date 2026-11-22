/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.ShulkerBoxMenu;
/*    */ 
/*    */ public class ShulkerBoxScreen extends AbstractContainerScreen<ShulkerBoxMenu> {
/* 11 */   private static final Identifier CONTAINER_TEXTURE = Identifier.withDefaultNamespace("textures/gui/container/shulker_box.png");
/*    */   
/*    */   public ShulkerBoxScreen(ShulkerBoxMenu menu, Inventory inventory, Component title) {
/* 14 */     super(menu, inventory, title, 176, 167);
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 19 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 20 */     int xo = (this.width - this.imageWidth) / 2;
/* 21 */     int yo = (this.height - this.imageHeight) / 2;
/* 22 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_TEXTURE, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\ShulkerBoxScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */