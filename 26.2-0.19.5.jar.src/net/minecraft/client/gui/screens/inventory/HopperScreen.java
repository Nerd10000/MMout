/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.HopperMenu;
/*    */ 
/*    */ public class HopperScreen extends AbstractContainerScreen<HopperMenu> {
/* 11 */   private static final Identifier HOPPER_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/hopper.png");
/*    */   
/*    */   public HopperScreen(HopperMenu menu, Inventory inventory, Component title) {
/* 14 */     super(menu, inventory, title, 176, 133);
/* 15 */     this.inventoryLabelY = this.imageHeight - 94;
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 20 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 21 */     int xo = (this.width - this.imageWidth) / 2;
/* 22 */     int yo = (this.height - this.imageHeight) / 2;
/* 23 */     graphics.blit(RenderPipelines.GUI_TEXTURED, HOPPER_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\HopperScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */