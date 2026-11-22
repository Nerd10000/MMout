/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.ChestMenu;
/*    */ 
/*    */ public class ContainerScreen extends AbstractContainerScreen<ChestMenu> {
/* 11 */   private static final Identifier CONTAINER_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/container/generic_54.png");
/*    */   
/*    */   private final int containerRows;
/*    */   
/*    */   public ContainerScreen(ChestMenu menu, Inventory inventory, Component title) {
/* 16 */     super(menu, inventory, title, 176, 114 + menu.getRowCount() * 18);
/* 17 */     this.containerRows = menu.getRowCount();
/* 18 */     this.inventoryLabelY = this.imageHeight - 94;
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 23 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 24 */     int xo = (this.width - this.imageWidth) / 2;
/* 25 */     int yo = (this.height - this.imageHeight) / 2;
/* 26 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, xo, yo, 0.0F, 0.0F, this.imageWidth, this.containerRows * 18 + 17, 256, 256);
/* 27 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_BACKGROUND, xo, yo + this.containerRows * 18 + 17, 0.0F, 126.0F, this.imageWidth, 96, 256, 256);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\ContainerScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */