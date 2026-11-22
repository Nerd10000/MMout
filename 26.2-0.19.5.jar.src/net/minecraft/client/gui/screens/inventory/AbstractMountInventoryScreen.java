/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Inventory;
/*    */ import net.minecraft.world.inventory.AbstractMountInventoryMenu;
/*    */ 
/*    */ public abstract class AbstractMountInventoryScreen<T extends AbstractMountInventoryMenu> extends AbstractContainerScreen<T> {
/*    */   protected final int inventoryColumns;
/*    */   
/*    */   protected float xMouse;
/*    */   
/*    */   protected float yMouse;
/*    */   
/*    */   protected final LivingEntity mount;
/*    */   
/*    */   public AbstractMountInventoryScreen(T menu, Inventory inventory, Component title, int inventoryColumns, LivingEntity mount) {
/* 19 */     super(menu, inventory, title);
/* 20 */     this.inventoryColumns = inventoryColumns;
/* 21 */     this.mount = mount;
/*    */   }
/*    */   
/*    */   public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 26 */     super.extractBackground(graphics, mouseX, mouseY, a);
/* 27 */     int xo = (this.width - this.imageWidth) / 2;
/* 28 */     int yo = (this.height - this.imageHeight) / 2;
/* 30 */     graphics.blit(RenderPipelines.GUI_TEXTURED, getBackgroundTextureLocation(), xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
/* 32 */     if (this.inventoryColumns > 0 && getChestSlotsSpriteLocation() != null)
/* 33 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getChestSlotsSpriteLocation(), 90, 54, 0, 0, xo + 79, yo + 17, this.inventoryColumns * 18, 54); 
/* 36 */     if (shouldRenderSaddleSlot())
/* 37 */       extractSlot(graphics, xo + 7, yo + 35 - 18); 
/* 39 */     if (shouldRenderArmorSlot())
/* 40 */       extractSlot(graphics, xo + 7, yo + 35); 
/* 43 */     InventoryScreen.extractEntityInInventoryFollowsMouse(graphics, xo + 26, yo + 18, xo + 78, yo + 70, 17, 0.25F, this.xMouse, this.yMouse, this.mount);
/*    */   }
/*    */   
/*    */   protected void extractSlot(GuiGraphicsExtractor graphics, int x, int y) {
/* 47 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getSlotSpriteLocation(), x, y, 18, 18);
/*    */   }
/*    */   
/*    */   public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 52 */     this.xMouse = mouseX;
/* 53 */     this.yMouse = mouseY;
/* 54 */     super.extractRenderState(graphics, mouseX, mouseY, a);
/*    */   }
/*    */   
/*    */   protected abstract Identifier getBackgroundTextureLocation();
/*    */   
/*    */   protected abstract Identifier getSlotSpriteLocation();
/*    */   
/*    */   protected abstract Identifier getChestSlotsSpriteLocation();
/*    */   
/*    */   protected abstract boolean shouldRenderSaddleSlot();
/*    */   
/*    */   protected abstract boolean shouldRenderArmorSlot();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\AbstractMountInventoryScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */