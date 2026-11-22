/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.DispenserMenu;
/*    */ 
/*    */ public class DispenserScreen extends AbstractContainerScreen<DispenserMenu> {
/* 11 */   private static final Identifier CONTAINER_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/dispenser.png");
/*    */   
/*    */   public DispenserScreen(DispenserMenu menu, Inventory inventory, Component title) {
/* 14 */     super(menu, inventory, title);
/*    */   }
/*    */   
/*    */   protected void init() {
/* 19 */     super.init();
/* 20 */     this.titleLabelX = (this.imageWidth - this.font.width((FormattedText)this.title)) / 2;
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 25 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 26 */     int xo = (this.width - this.imageWidth) / 2;
/* 27 */     int yo = (this.height - this.imageHeight) / 2;
/* 28 */     graphics.blit(RenderPipelines.GUI_TEXTURED, CONTAINER_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\DispenserScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */