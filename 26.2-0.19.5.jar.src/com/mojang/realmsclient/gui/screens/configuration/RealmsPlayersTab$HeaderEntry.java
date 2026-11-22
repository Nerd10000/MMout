/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ class HeaderEntry extends RealmsPlayersTab.Entry {
/* 269 */   private String cachedNumberOfInvites = "";
/*     */   
/*     */   private final FocusableTextWidget invitedWidget;
/*     */   
/*     */   public HeaderEntry() {
/* 273 */     MutableComponent mutableComponent = Component.translatable("mco.configure.world.invited.number", new Object[] { "" }).withStyle(ChatFormatting.UNDERLINE);
/* 274 */     this.invitedWidget = FocusableTextWidget.builder((Component)mutableComponent, paramRealmsPlayersTab.font).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS).build();
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 279 */     String numberOfInvites = (RealmsPlayersTab.this.serverData.players != null) ? Integer.toString(RealmsPlayersTab.this.serverData.players.size()) : "0";
/* 280 */     if (!numberOfInvites.equals(this.cachedNumberOfInvites)) {
/* 281 */       this.cachedNumberOfInvites = numberOfInvites;
/* 282 */       MutableComponent mutableComponent = Component.translatable("mco.configure.world.invited.number", new Object[] { numberOfInvites }).withStyle(ChatFormatting.UNDERLINE);
/* 283 */       this.invitedWidget.setMessage((Component)mutableComponent);
/*     */     } 
/* 285 */     this.invitedWidget.setPosition(RealmsPlayersTab.this.invitedList.getRowLeft() + RealmsPlayersTab.this.invitedList.getRowWidth() / 2 - this.invitedWidget.getWidth() / 2, getY() + getHeight() / 2 - this.invitedWidget.getHeight() / 2);
/* 286 */     this.invitedWidget.extractRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   private int height(int lineHeight) {
/* 290 */     return lineHeight + this.invitedWidget.getPadding() * 2;
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 295 */     return (List)List.of(this.invitedWidget);
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 300 */     return (List)List.of(this.invitedWidget);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsPlayersTab$HeaderEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */