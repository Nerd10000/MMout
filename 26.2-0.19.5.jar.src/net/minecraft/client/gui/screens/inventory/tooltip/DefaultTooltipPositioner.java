/*    */ package net.minecraft.client.gui.screens.inventory.tooltip;
/*    */ 
/*    */ import org.joml.Vector2i;
/*    */ import org.joml.Vector2ic;
/*    */ 
/*    */ public class DefaultTooltipPositioner implements ClientTooltipPositioner {
/*  7 */   public static final ClientTooltipPositioner INSTANCE = new DefaultTooltipPositioner();
/*    */   
/*    */   public Vector2ic positionTooltip(int screenWidth, int screenHeight, int x, int y, int tooltipWidth, int tooltipHeight) {
/* 15 */     Vector2i result = (new Vector2i(x, y)).add(12, -12);
/* 16 */     positionTooltip(screenWidth, screenHeight, result, tooltipWidth, tooltipHeight);
/* 17 */     return (Vector2ic)result;
/*    */   }
/*    */   
/*    */   private void positionTooltip(int screenWidth, int screenHeight, Vector2i result, int tooltipWidth, int tooltipHeight) {
/* 22 */     if (result.x + tooltipWidth > screenWidth)
/* 23 */       result.x = Math.max(result.x - 24 - tooltipWidth, 4); 
/* 27 */     int paddedHeight = tooltipHeight + 3;
/* 28 */     if (result.y + paddedHeight > screenHeight)
/* 29 */       result.y = screenHeight - paddedHeight; 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\tooltip\DefaultTooltipPositioner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */