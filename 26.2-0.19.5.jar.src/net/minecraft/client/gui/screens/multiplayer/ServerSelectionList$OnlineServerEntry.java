/*     */ package net.minecraft.client.gui.screens.multiplayer;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.SelectableEntry;
/*     */ import net.minecraft.client.gui.screens.FaviconTexture;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.network.EventLoopGroupHolder;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class OnlineServerEntry extends ServerSelectionList.Entry implements SelectableEntry {
/*     */   private static final int ICON_SIZE = 32;
/*     */   
/*     */   private static final int SPACING = 5;
/*     */   
/*     */   private static final int STATUS_ICON_WIDTH = 10;
/*     */   
/*     */   private static final int STATUS_ICON_HEIGHT = 8;
/*     */   
/*     */   private final JoinMultiplayerScreen screen;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final ServerData serverData;
/*     */   
/*     */   private final FaviconTexture icon;
/*     */   
/*     */   private byte[] lastIconBytes;
/*     */   
/*     */   private List<Component> onlinePlayersTooltip;
/*     */   
/*     */   private Identifier statusIcon;
/*     */   
/*     */   private Component statusIconTooltip;
/*     */   
/*     */   protected OnlineServerEntry(JoinMultiplayerScreen screen, ServerData serverData) {
/* 264 */     this.screen = screen;
/* 265 */     this.serverData = serverData;
/* 266 */     this.minecraft = Minecraft.getInstance();
/* 267 */     this.icon = FaviconTexture.forServer(this.minecraft.getTextureManager(), serverData.ip);
/* 268 */     refreshStatus();
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 273 */     if (this.serverData.state() == ServerData.State.INITIAL) {
/* 274 */       this.serverData.setState(ServerData.State.PINGING);
/* 275 */       this.serverData.motd = CommonComponents.EMPTY;
/* 276 */       this.serverData.status = CommonComponents.EMPTY;
/* 278 */       ServerSelectionList.THREAD_POOL.submit(() -> {
/*     */             try {
/*     */               this.screen.getPinger().pingServer(this.serverData, (), (), EventLoopGroupHolder.remote(this.minecraft.options.useNativeTransport()));
/* 288 */             } catch (UnknownHostException ignored) {
/*     */               this.serverData.setState(ServerData.State.UNREACHABLE);
/*     */               this.serverData.motd = ServerSelectionList.CANT_RESOLVE_TEXT;
/*     */               this.minecraft.execute(this::refreshStatus);
/* 292 */             } catch (Exception ignored) {
/*     */               this.serverData.setState(ServerData.State.UNREACHABLE);
/*     */               this.serverData.motd = ServerSelectionList.CANT_CONNECT_TEXT;
/*     */               this.minecraft.execute(this::refreshStatus);
/*     */             } 
/*     */           });
/*     */     } 
/* 299 */     graphics.text(this.minecraft.font, this.serverData.name, getContentX() + 32 + 3, getContentY() + 1, -1);
/* 301 */     List<FormattedCharSequence> lines = this.minecraft.font.split((FormattedText)this.serverData.motd, getContentWidth() - 32 - 2);
/* 302 */     for (int i = 0; i < Math.min(lines.size(), 2); i++) {
/* 303 */       Objects.requireNonNull(this.minecraft.font);
/* 303 */       graphics.text(this.minecraft.font, lines.get(i), getContentX() + 32 + 3, getContentY() + 12 + 9 * i, -8355712);
/*     */     } 
/* 305 */     extractIcon(graphics, getContentX(), getContentY(), this.icon.textureLocation());
/* 307 */     int index = ServerSelectionList.this.children().indexOf(this);
/* 308 */     if (this.serverData.state() == ServerData.State.PINGING) {
/* 309 */       int iconIndex = (int)(Util.getMillis() / 100L + (index * 2) & 0x7L);
/* 310 */       if (iconIndex > 4)
/* 311 */         iconIndex = 8 - iconIndex; 
/* 313 */       switch (iconIndex) {
/*     */         default:
/*     */         
/*     */         case 1:
/*     */         
/*     */         case 2:
/*     */         
/*     */         case 3:
/*     */         
/*     */         case 4:
/*     */           break;
/*     */       } 
/* 313 */       this
/*     */         
/* 319 */         .statusIcon = ServerSelectionList.PINGING_5_SPRITE;
/*     */     } 
/* 322 */     int statusIconX = getContentRight() - 10 - 5;
/* 323 */     if (this.statusIcon != null)
/* 324 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.statusIcon, statusIconX, getContentY(), 10, 8); 
/* 327 */     byte[] currentIconBytes = this.serverData.getIconBytes();
/* 328 */     if (!Arrays.equals(currentIconBytes, this.lastIconBytes))
/* 329 */       if (uploadServerIcon(currentIconBytes)) {
/* 330 */         this.lastIconBytes = currentIconBytes;
/*     */       } else {
/* 332 */         this.serverData.setIconBytes(null);
/* 333 */         updateServerList();
/*     */       }  
/* 337 */     Component status = (this.serverData.state() == ServerData.State.INCOMPATIBLE) ? (Component)this.serverData.version.copy().withStyle(ChatFormatting.RED) : this.serverData.status;
/* 338 */     int statusWidth = this.minecraft.font.width((FormattedText)status);
/* 339 */     int statusX = statusIconX - statusWidth - 5;
/* 340 */     graphics.text(this.minecraft.font, status, statusX, getContentY() + 1, -8355712);
/* 342 */     if (this.statusIconTooltip != null && mouseX >= statusIconX && mouseX <= statusIconX + 10 && mouseY >= getContentY() && mouseY <= getContentY() + 8) {
/* 343 */       graphics.setTooltipForNextFrame(this.statusIconTooltip, mouseX, mouseY);
/*     */     } else {
/* 344 */       Objects.requireNonNull(this.minecraft.font);
/* 344 */       if (this.onlinePlayersTooltip != null && mouseX >= statusX && mouseX <= statusX + statusWidth && mouseY >= getContentY() && mouseY <= getContentY() - 1 + 9)
/* 345 */         graphics.setTooltipForNextFrame(Lists.transform(this.onlinePlayersTooltip, Component::getVisualOrderText), mouseX, mouseY); 
/*     */     } 
/* 348 */     if (hovered) {
/* 349 */       graphics.fill(getContentX(), getContentY(), getContentX() + 32, getContentY() + 32, -1601138544);
/* 350 */       int relX = mouseX - getContentX();
/* 351 */       int relY = mouseY - getContentY();
/* 353 */       if (mouseOverRightHalf(relX, relY, 32)) {
/* 354 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.JOIN_HIGHLIGHTED_SPRITE, getContentX(), getContentY(), 32, 32);
/* 355 */         ServerSelectionList.access$000(ServerSelectionList.this, graphics);
/*     */       } else {
/* 357 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.JOIN_SPRITE, getContentX(), getContentY(), 32, 32);
/*     */       } 
/* 359 */       if (index > 0)
/* 360 */         if (mouseOverTopLeftQuarter(relX, relY, 32)) {
/* 361 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.MOVE_UP_HIGHLIGHTED_SPRITE, getContentX(), getContentY(), 32, 32);
/* 362 */           ServerSelectionList.access$100(ServerSelectionList.this, graphics);
/*     */         } else {
/* 364 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.MOVE_UP_SPRITE, getContentX(), getContentY(), 32, 32);
/*     */         }  
/* 367 */       if (index < this.screen.getServers().size() - 1)
/* 368 */         if (mouseOverBottomLeftQuarter(relX, relY, 32)) {
/* 369 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.MOVE_DOWN_HIGHLIGHTED_SPRITE, getContentX(), getContentY(), 32, 32);
/* 370 */           ServerSelectionList.access$200(ServerSelectionList.this, graphics);
/*     */         } else {
/* 372 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ServerSelectionList.MOVE_DOWN_SPRITE, getContentX(), getContentY(), 32, 32);
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   private void refreshStatus() {
/* 379 */     this.onlinePlayersTooltip = null;
/* 381 */     switch (ServerSelectionList.null.$SwitchMap$net$minecraft$client$multiplayer$ServerData$State[this.serverData.state().ordinal()]) {
/*     */       case 1:
/*     */       case 2:
/* 383 */         this.statusIcon = ServerSelectionList.PING_1_SPRITE;
/* 384 */         this.statusIconTooltip = ServerSelectionList.PINGING_STATUS;
/*     */         break;
/*     */       case 3:
/* 387 */         this.statusIcon = ServerSelectionList.INCOMPATIBLE_SPRITE;
/* 388 */         this.statusIconTooltip = ServerSelectionList.INCOMPATIBLE_STATUS;
/* 389 */         this.onlinePlayersTooltip = this.serverData.playerList;
/*     */         break;
/*     */       case 4:
/* 392 */         this.statusIcon = ServerSelectionList.UNREACHABLE_SPRITE;
/* 393 */         this.statusIconTooltip = ServerSelectionList.NO_CONNECTION_STATUS;
/*     */         break;
/*     */       case 5:
/* 396 */         if (this.serverData.ping < 150L) {
/* 397 */           this.statusIcon = ServerSelectionList.PING_5_SPRITE;
/* 398 */         } else if (this.serverData.ping < 300L) {
/* 399 */           this.statusIcon = ServerSelectionList.PING_4_SPRITE;
/* 400 */         } else if (this.serverData.ping < 600L) {
/* 401 */           this.statusIcon = ServerSelectionList.PING_3_SPRITE;
/* 402 */         } else if (this.serverData.ping < 1000L) {
/* 403 */           this.statusIcon = ServerSelectionList.PING_2_SPRITE;
/*     */         } else {
/* 405 */           this.statusIcon = ServerSelectionList.PING_1_SPRITE;
/*     */         } 
/* 407 */         this.statusIconTooltip = (Component)Component.translatable("multiplayer.status.ping", new Object[] { Long.valueOf(this.serverData.ping) });
/* 408 */         this.onlinePlayersTooltip = this.serverData.playerList;
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateServerList() {
/* 414 */     this.screen.getServers().save();
/*     */   }
/*     */   
/*     */   protected void extractIcon(GuiGraphicsExtractor graphics, int rowLeft, int rowTop, Identifier location) {
/* 418 */     graphics.blit(RenderPipelines.GUI_TEXTURED, location, rowLeft, rowTop, 0.0F, 0.0F, 32, 32, 32, 32);
/*     */   }
/*     */   
/*     */   private boolean uploadServerIcon(byte[] serverIconBytes) {
/* 422 */     if (serverIconBytes == null) {
/* 423 */       this.icon.clear();
/*     */     } else {
/*     */       try {
/* 426 */         this.icon.upload(NativeImage.read(serverIconBytes));
/* 427 */       } catch (Throwable t) {
/* 428 */         ServerSelectionList.LOGGER.error("Invalid icon for server {} ({})", new Object[] { this.serverData.name, this.serverData.ip, t });
/* 429 */         return false;
/*     */       } 
/*     */     } 
/* 432 */     return true;
/*     */   }
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 437 */     if (event.isSelection()) {
/* 438 */       join();
/* 439 */       return true;
/*     */     } 
/* 441 */     if (event.hasShiftDown()) {
/* 443 */       ServerSelectionList list = this.screen.serverSelectionList;
/* 444 */       int currentIndex = list.children().indexOf(this);
/* 446 */       if (currentIndex == -1)
/* 447 */         return true; 
/* 450 */       if ((event.isDown() && currentIndex < this.screen.getServers().size() - 1) || (event
/* 451 */         .isUp() && currentIndex > 0)) {
/* 453 */         swap(currentIndex, event.isDown() ? (currentIndex + 1) : (currentIndex - 1));
/* 454 */         return true;
/*     */       } 
/*     */     } 
/* 458 */     return super.keyPressed(event);
/*     */   }
/*     */   
/*     */   public void join() {
/* 463 */     this.screen.join(this.serverData);
/*     */   }
/*     */   
/*     */   private void swap(int currentIndex, int newIndex) {
/* 467 */     this.screen.getServers().swap(currentIndex, newIndex);
/* 468 */     ServerSelectionList.access$300(this.screen.serverSelectionList, currentIndex, newIndex);
/*     */   }
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 473 */     int relX = (int)event.x() - getContentX();
/* 474 */     int relY = (int)event.y() - getContentY();
/* 476 */     if (mouseOverRightHalf(relX, relY, 32)) {
/* 477 */       join();
/* 478 */       return true;
/*     */     } 
/* 481 */     int currentIndex = this.screen.serverSelectionList.children().indexOf(this);
/* 482 */     if (currentIndex > 0 && mouseOverTopLeftQuarter(relX, relY, 32)) {
/* 483 */       swap(currentIndex, currentIndex - 1);
/* 484 */       return true;
/*     */     } 
/* 487 */     if (currentIndex < this.screen.getServers().size() - 1 && mouseOverBottomLeftQuarter(relX, relY, 32)) {
/* 488 */       swap(currentIndex, currentIndex + 1);
/* 489 */       return true;
/*     */     } 
/* 492 */     if (doubleClick)
/* 493 */       join(); 
/* 496 */     return super.mouseClicked(event, doubleClick);
/*     */   }
/*     */   
/*     */   public ServerData getServerData() {
/* 500 */     return this.serverData;
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 505 */     MutableComponent narrationComponent = Component.empty();
/* 506 */     narrationComponent.append((Component)Component.translatable("narrator.select", new Object[] { this.serverData.name }));
/* 507 */     narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
/* 508 */     switch (ServerSelectionList.null.$SwitchMap$net$minecraft$client$multiplayer$ServerData$State[this.serverData.state().ordinal()]) {
/*     */       case 3:
/* 510 */         narrationComponent.append(ServerSelectionList.INCOMPATIBLE_STATUS);
/* 511 */         narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
/* 512 */         narrationComponent.append((Component)Component.translatable("multiplayer.status.version.narration", new Object[] { this.serverData.version }));
/* 513 */         narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
/* 514 */         narrationComponent.append((Component)Component.translatable("multiplayer.status.motd.narration", new Object[] { this.serverData.motd }));
/* 532 */         return (Component)narrationComponent;
/*     */       case 4:
/*     */         narrationComponent.append(ServerSelectionList.NO_CONNECTION_STATUS);
/* 532 */         return (Component)narrationComponent;
/*     */       case 2:
/*     */         narrationComponent.append(ServerSelectionList.PINGING_STATUS);
/* 532 */         return (Component)narrationComponent;
/*     */     } 
/*     */     narrationComponent.append(ServerSelectionList.ONLINE_STATUS);
/*     */     narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
/*     */     narrationComponent.append((Component)Component.translatable("multiplayer.status.ping.narration", new Object[] { Long.valueOf(this.serverData.ping) }));
/*     */     narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
/*     */     narrationComponent.append((Component)Component.translatable("multiplayer.status.motd.narration", new Object[] { this.serverData.motd }));
/*     */     if (this.serverData.players != null) {
/*     */       narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
/*     */       narrationComponent.append((Component)Component.translatable("multiplayer.status.player_count.narration", new Object[] { Integer.valueOf(this.serverData.players.online()), Integer.valueOf(this.serverData.players.max()) }));
/*     */       narrationComponent.append(CommonComponents.NARRATION_SEPARATOR);
/*     */       narrationComponent.append(ComponentUtils.formatList(this.serverData.playerList, (Component)Component.literal(", ")));
/*     */     } 
/* 532 */     return (Component)narrationComponent;
/*     */   }
/*     */   
/*     */   public void close() {
/* 537 */     this.icon.close();
/*     */   }
/*     */   
/*     */   protected boolean matches(ServerSelectionList.Entry other) {
/* 542 */     if (other instanceof OnlineServerEntry) {
/* 542 */       OnlineServerEntry onlineServerEntry = (OnlineServerEntry)other;
/* 542 */       if (onlineServerEntry.serverData == this.serverData);
/*     */     } 
/* 542 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\multiplayer\ServerSelectionList$OnlineServerEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */