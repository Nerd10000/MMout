/*    */ package net.minecraft.client.renderer.state.gui;
/*    */ 
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import org.joml.Matrix3x2f;
/*    */ import org.joml.Matrix3x2fc;
/*    */ 
/*    */ public final class GuiItemRenderState implements ScreenArea {
/*    */   private final Matrix3x2f pose;
/*    */   
/*    */   private final TrackingItemStackRenderState itemStackRenderState;
/*    */   
/*    */   private final int x;
/*    */   
/*    */   private final int y;
/*    */   
/*    */   private final ScreenRectangle scissorArea;
/*    */   
/*    */   private final ScreenRectangle oversizedItemBounds;
/*    */   
/*    */   private final ScreenRectangle bounds;
/*    */   
/*    */   public GuiItemRenderState(Matrix3x2f pose, TrackingItemStackRenderState itemStackRenderState, int x, int y, ScreenRectangle scissorArea) {
/* 21 */     this.pose = pose;
/* 22 */     this.itemStackRenderState = itemStackRenderState;
/* 23 */     this.x = x;
/* 24 */     this.y = y;
/* 25 */     this.scissorArea = scissorArea;
/* 26 */     this.oversizedItemBounds = itemStackRenderState().isOversizedInGui() ? calculateOversizedItemBounds() : null;
/* 27 */     this.bounds = calculateBounds((this.oversizedItemBounds != null) ? this.oversizedItemBounds : new ScreenRectangle(this.x, this.y, 16, 16));
/*    */   }
/*    */   
/*    */   private ScreenRectangle calculateOversizedItemBounds() {
/* 31 */     AABB aabb = this.itemStackRenderState.getModelBoundingBox();
/* 32 */     int actualXSize = Mth.ceil(aabb.getXsize() * 16.0D);
/* 33 */     int actualYSize = Mth.ceil(aabb.getYsize() * 16.0D);
/* 34 */     if (actualXSize > 16 || actualYSize > 16) {
/* 35 */       float xOffset = (float)(aabb.minX * 16.0D);
/* 36 */       float yOffset = (float)(aabb.maxY * 16.0D);
/* 37 */       int flooredXOffset = Mth.floor(xOffset);
/* 38 */       int flooredYOffset = Mth.floor(yOffset);
/* 39 */       int actualX = this.x + flooredXOffset + 8;
/* 40 */       int actualY = this.y - flooredYOffset + 8;
/* 41 */       return new ScreenRectangle(actualX, actualY, actualXSize, actualYSize);
/*    */     } 
/* 43 */     return null;
/*    */   }
/*    */   
/*    */   private ScreenRectangle calculateBounds(ScreenRectangle itemBounds) {
/* 48 */     ScreenRectangle bounds = itemBounds.transformMaxBounds((Matrix3x2fc)this.pose);
/* 49 */     return (this.scissorArea != null) ? this.scissorArea.intersection(bounds) : bounds;
/*    */   }
/*    */   
/*    */   public Matrix3x2f pose() {
/* 53 */     return this.pose;
/*    */   }
/*    */   
/*    */   public TrackingItemStackRenderState itemStackRenderState() {
/* 57 */     return this.itemStackRenderState;
/*    */   }
/*    */   
/*    */   public int x() {
/* 61 */     return this.x;
/*    */   }
/*    */   
/*    */   public int y() {
/* 65 */     return this.y;
/*    */   }
/*    */   
/*    */   public ScreenRectangle scissorArea() {
/* 69 */     return this.scissorArea;
/*    */   }
/*    */   
/*    */   public ScreenRectangle oversizedItemBounds() {
/* 73 */     return this.oversizedItemBounds;
/*    */   }
/*    */   
/*    */   public ScreenRectangle bounds() {
/* 78 */     return this.bounds;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\GuiItemRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */