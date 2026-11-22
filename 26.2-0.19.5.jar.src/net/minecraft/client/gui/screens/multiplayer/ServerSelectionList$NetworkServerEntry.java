/*     */ package net.minecraft.client.gui.screens.multiplayer;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.server.LanServer;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class NetworkServerEntry extends ServerSelectionList.Entry {
/*     */   private static final int ICON_WIDTH = 32;
/*     */   
/* 186 */   private static final Component LAN_SERVER_HEADER = (Component)Component.translatable("lanServer.title");
/*     */   
/* 187 */   private static final Component HIDDEN_ADDRESS_TEXT = (Component)Component.translatable("selectServer.hiddenAddress");
/*     */   
/*     */   private final JoinMultiplayerScreen screen;
/*     */   
/*     */   protected final Minecraft minecraft;
/*     */   
/*     */   protected final LanServer serverData;
/*     */   
/*     */   protected NetworkServerEntry(JoinMultiplayerScreen screen, LanServer serverData) {
/* 194 */     this.screen = screen;
/* 195 */     this.serverData = serverData;
/* 196 */     this.minecraft = Minecraft.getInstance();
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 201 */     graphics.text(this.minecraft.font, LAN_SERVER_HEADER, getContentX() + 32 + 3, getContentY() + 1, -1);
/* 202 */     graphics.text(this.minecraft.font, this.serverData.getMotd(), getContentX() + 32 + 3, getContentY() + 12, -8355712);
/* 204 */     if (this.minecraft.options.hideServerAddress) {
/* 205 */       graphics.text(this.minecraft.font, HIDDEN_ADDRESS_TEXT, getContentX() + 32 + 3, getContentY() + 12 + 11, -8355712);
/*     */     } else {
/* 207 */       graphics.text(this.minecraft.font, this.serverData.getAddress(), getContentX() + 32 + 3, getContentY() + 12 + 11, -8355712);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 213 */     if (doubleClick)
/* 214 */       join(); 
/* 216 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 221 */     if (event.isSelection()) {
/* 222 */       join();
/* 223 */       return true;
/*     */     } 
/* 225 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public void join() {
/* 230 */     this.screen.join(new ServerData(this.serverData.getMotd(), this.serverData.getAddress(), ServerData.Type.LAN));
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 235 */     return (Component)Component.translatable("narrator.select", new Object[] { getServerNarration() });
/*     */   }
/*     */   
/*     */   public Component getServerNarration() {
/* 239 */     return (Component)Component.empty().append(LAN_SERVER_HEADER).append(CommonComponents.SPACE).append(this.serverData.getMotd());
/*     */   }
/*     */   
/*     */   protected boolean matches(ServerSelectionList.Entry other) {
/* 244 */     if (other instanceof NetworkServerEntry) {
/* 244 */       NetworkServerEntry networkServerEntry = (NetworkServerEntry)other;
/* 244 */       if (networkServerEntry.serverData == this.serverData);
/*     */     } 
/* 244 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\multiplayer\ServerSelectionList$NetworkServerEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */