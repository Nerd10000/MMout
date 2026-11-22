/*    */ package net.minecraft.client.gui.screens.inventory.tooltip;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.components.PlayerFaceExtractor;
/*    */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*    */ import net.minecraft.world.inventory.tooltip.TooltipComponent;
/*    */ 
/*    */ public class ClientActivePlayersTooltip implements ClientTooltipComponent {
/*    */   private static final int SKIN_SIZE = 10;
/*    */   
/*    */   private static final int PADDING = 2;
/*    */   
/*    */   private final List<PlayerSkinRenderCache.RenderInfo> activePlayers;
/*    */   
/*    */   public ClientActivePlayersTooltip(ActivePlayersTooltip activePlayersTooltip) {
/* 19 */     this.activePlayers = activePlayersTooltip.profiles();
/*    */   }
/*    */   
/*    */   public int getHeight(Font font) {
/* 24 */     return this.activePlayers.size() * 12 + 2;
/*    */   }
/*    */   
/*    */   private static String getName(PlayerSkinRenderCache.RenderInfo activePlayer) {
/* 28 */     return activePlayer.gameProfile().name();
/*    */   }
/*    */   
/*    */   public int getWidth(Font font) {
/* 33 */     int widest = 0;
/* 34 */     for (PlayerSkinRenderCache.RenderInfo activePlayer : this.activePlayers) {
/* 35 */       int width = font.width(getName(activePlayer));
/* 36 */       if (width > widest)
/* 37 */         widest = width; 
/*    */     } 
/* 40 */     return widest + 10 + 6;
/*    */   }
/*    */   
/*    */   public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
/* 45 */     for (int i = 0; i < this.activePlayers.size(); i++) {
/* 46 */       PlayerSkinRenderCache.RenderInfo activePlayer = this.activePlayers.get(i);
/* 47 */       int y1 = y + 2 + i * 12;
/* 48 */       PlayerFaceExtractor.extractRenderState(graphics, activePlayer.playerSkin(), x + 2, y1, 10);
/* 49 */       graphics.text(font, getName(activePlayer), x + 10 + 4, y1 + 2, -1);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static final class ActivePlayersTooltip extends Record implements TooltipComponent {
/*    */     private final List<PlayerSkinRenderCache.RenderInfo> profiles;
/*    */     
/*    */     public ActivePlayersTooltip(List<PlayerSkinRenderCache.RenderInfo> profiles) {
/* 53 */       this.profiles = profiles;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientActivePlayersTooltip$ActivePlayersTooltip;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientActivePlayersTooltip$ActivePlayersTooltip;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientActivePlayersTooltip$ActivePlayersTooltip;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientActivePlayersTooltip$ActivePlayersTooltip;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientActivePlayersTooltip$ActivePlayersTooltip;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #53	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientActivePlayersTooltip$ActivePlayersTooltip;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public List<PlayerSkinRenderCache.RenderInfo> profiles() {
/* 53 */       return this.profiles;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\tooltip\ClientActivePlayersTooltip.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */