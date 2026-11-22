/*     */ package com.mojang.realmsclient.gui;
/*     */ 
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.gui.screens.configuration.RealmsConfigureWorldScreen;
/*     */ import com.mojang.realmsclient.util.RealmsTextureManager;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ 
/*     */ public class RealmsWorldSlotButton extends Button {
/*  23 */   private static final Identifier SLOT_FRAME_SPRITE = Identifier.withDefaultNamespace("widget/slot_frame");
/*     */   
/*  24 */   public static final Identifier EMPTY_SLOT_LOCATION = Identifier.withDefaultNamespace("textures/gui/realms/empty_frame.png");
/*     */   
/*  25 */   public static final Identifier DEFAULT_WORLD_SLOT_1 = Identifier.withDefaultNamespace("textures/gui/title/background/panorama_0.png");
/*     */   
/*  26 */   public static final Identifier DEFAULT_WORLD_SLOT_2 = Identifier.withDefaultNamespace("textures/gui/title/background/panorama_2.png");
/*     */   
/*  27 */   public static final Identifier DEFAULT_WORLD_SLOT_3 = Identifier.withDefaultNamespace("textures/gui/title/background/panorama_3.png");
/*     */   
/*  28 */   private static final Component SWITCH_TO_MINIGAME_SLOT_TOOLTIP = (Component)Component.translatable("mco.configure.world.slot.tooltip.minigame");
/*     */   
/*  29 */   private static final Component SWITCH_TO_WORLD_SLOT_TOOLTIP = (Component)Component.translatable("mco.configure.world.slot.tooltip");
/*     */   
/*  30 */   private static final Component MINIGAME = (Component)Component.translatable("mco.worldSlot.minigame");
/*     */   
/*     */   private final RealmsConfigureWorldScreen configureWorldScreen;
/*     */   
/*     */   private final int slotIndex;
/*     */   
/*     */   private final StringWidget slotNameWidget;
/*     */   
/*     */   private State state;
/*     */   
/*     */   public RealmsWorldSlotButton(RealmsConfigureWorldScreen configureWorldScreen, int x, int y, int width, int height, int slotIndex, RealmsServer serverData, Button.OnPress onPress) {
/*  40 */     super(x, y, width, height, CommonComponents.EMPTY, onPress, DEFAULT_NARRATION);
/*  41 */     this.configureWorldScreen = configureWorldScreen;
/*  42 */     this.slotIndex = slotIndex;
/*  43 */     this.state = setServerData(serverData);
/*  44 */     this.slotNameWidget = new StringWidget((Component)Component.literal(this.state.slotName), (Minecraft.getInstance()).font);
/*     */   }
/*     */   
/*     */   public State getState() {
/*  48 */     return this.state;
/*     */   }
/*     */   
/*     */   public enum Action {
/*  52 */     NOTHING, SWITCH_SLOT;
/*     */   }
/*     */   
/*     */   public static class State {
/*     */     private final String slotName;
/*     */     
/*     */     private final String slotVersion;
/*     */     
/*     */     private final RealmsServer.Compatibility compatibility;
/*     */     
/*     */     private final long imageId;
/*     */     
/*     */     private final String image;
/*     */     
/*     */     public final boolean empty;
/*     */     
/*     */     public final boolean minigame;
/*     */     
/*     */     public final RealmsWorldSlotButton.Action action;
/*     */     
/*     */     public final boolean hardcore;
/*     */     
/*     */     public final boolean activeSlot;
/*     */     
/*     */     public State(RealmsServer serverData, int slotIndex) {
/*  69 */       this.minigame = (slotIndex == 4);
/*  70 */       if (this.minigame) {
/*  71 */         this.slotName = RealmsWorldSlotButton.MINIGAME.getString();
/*  72 */         this.imageId = serverData.minigameId;
/*  73 */         this.image = serverData.minigameImage;
/*  74 */         this.empty = (serverData.minigameId == -1);
/*  75 */         this.slotVersion = "";
/*  76 */         this.compatibility = RealmsServer.Compatibility.UNVERIFIABLE;
/*  77 */         this.hardcore = false;
/*  78 */         this.activeSlot = serverData.isMinigameActive();
/*     */       } else {
/*  80 */         RealmsSlot slot = (RealmsSlot)serverData.slots.get(Integer.valueOf(slotIndex));
/*  81 */         this.slotName = slot.options.getSlotName(slotIndex);
/*  82 */         this.imageId = slot.options.templateId;
/*  83 */         this.image = slot.options.templateImage;
/*  84 */         this.empty = slot.options.empty;
/*  85 */         this.slotVersion = slot.options.version;
/*  86 */         this.compatibility = slot.options.compatibility;
/*  87 */         this.hardcore = slot.isHardcore();
/*  88 */         this.activeSlot = (serverData.activeSlot == slotIndex && !serverData.isMinigameActive());
/*     */       } 
/*  90 */       this.action = RealmsWorldSlotButton.getAction(this.activeSlot, this.empty, serverData.expired);
/*     */     }
/*     */   }
/*     */   
/*     */   public State setServerData(RealmsServer serverData) {
/*  95 */     this.state = new State(serverData, this.slotIndex);
/*  96 */     setTooltipAndNarration(this.state, serverData.minigameName);
/*  97 */     return this.state;
/*     */   }
/*     */   
/*     */   private void setTooltipAndNarration(State state, String minigameName) {
/* 101 */     switch (state.action.ordinal()) {
/*     */       case 1:
/* 102 */         if (state.minigame);
/*     */       default:
/*     */         break;
/*     */     } 
/* 103 */     Component tooltipComponent = null;
/* 106 */     if (tooltipComponent != null)
/* 107 */       setTooltip(Tooltip.create(tooltipComponent)); 
/* 110 */     MutableComponent slotContents = Component.literal(state.slotName);
/* 111 */     if (state.minigame && minigameName != null)
/* 112 */       slotContents = slotContents.append(CommonComponents.SPACE).append(minigameName); 
/* 115 */     setMessage((Component)slotContents);
/*     */   }
/*     */   
/*     */   private static Action getAction(boolean activeSlot, boolean empty, boolean expired) {
/* 119 */     if (!activeSlot && (!empty || !expired))
/* 120 */       return Action.SWITCH_SLOT; 
/* 122 */     return Action.NOTHING;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 127 */     return (this.state.action != Action.NOTHING && super.isActive());
/*     */   }
/*     */   
/*     */   public void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/*     */     Identifier texture;
/* 132 */     int x = getX();
/* 133 */     int y = getY();
/* 134 */     boolean hoveredOrFocused = isHoveredOrFocused();
/* 138 */     if (this.state.minigame) {
/* 139 */       texture = RealmsTextureManager.worldTemplate(String.valueOf(this.state.imageId), this.state.image);
/* 140 */     } else if (this.state.empty) {
/* 141 */       texture = EMPTY_SLOT_LOCATION;
/* 142 */     } else if (this.state.image != null && this.state.imageId != -1L) {
/* 143 */       texture = RealmsTextureManager.worldTemplate(String.valueOf(this.state.imageId), this.state.image);
/* 144 */     } else if (this.slotIndex == 1) {
/* 145 */       texture = DEFAULT_WORLD_SLOT_1;
/* 146 */     } else if (this.slotIndex == 2) {
/* 147 */       texture = DEFAULT_WORLD_SLOT_2;
/* 148 */     } else if (this.slotIndex == 3) {
/* 149 */       texture = DEFAULT_WORLD_SLOT_3;
/*     */     } else {
/* 151 */       texture = EMPTY_SLOT_LOCATION;
/*     */     } 
/* 154 */     int color = -1;
/* 155 */     if (!this.state.activeSlot)
/* 156 */       color = ARGB.colorFromFloat(1.0F, 0.56F, 0.56F, 0.56F); 
/* 159 */     graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x + 1, y + 1, 0.0F, 0.0F, this.width - 2, this.height - 2, 74, 74, 74, 74, color);
/* 161 */     if (hoveredOrFocused && this.state.action != Action.NOTHING) {
/* 162 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_FRAME_SPRITE, x, y, this.width, this.height);
/* 163 */     } else if (this.state.activeSlot) {
/* 164 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_FRAME_SPRITE, x, y, this.width, this.height, ARGB.colorFromFloat(1.0F, 0.8F, 0.8F, 0.8F));
/*     */     } else {
/* 166 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_FRAME_SPRITE, x, y, this.width, this.height, ARGB.colorFromFloat(1.0F, 0.56F, 0.56F, 0.56F));
/*     */     } 
/* 169 */     if (this.state.hardcore)
/* 170 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, RealmsMainScreen.HARDCORE_MODE_SPRITE, x + 3, y + 4, 9, 8); 
/* 173 */     this.slotNameWidget.setMaxWidth(getWidth() - (this.state.activeSlot ? 2 : 0), StringWidget.TextOverflow.SCROLLING);
/* 174 */     this.slotNameWidget.setPosition(getX() + getWidth() / 2 - this.slotNameWidget.getWidth() / 2, y + this.height - 14);
/* 175 */     this.slotNameWidget.extractRenderState(graphics, mouseX, mouseY, a);
/* 177 */     if (this.state.activeSlot)
/* 178 */       graphics.centeredText((Minecraft.getInstance()).font, RealmsMainScreen.getVersionComponent(this.state.slotVersion, this.state.compatibility.isCompatible()), x + this.width / 2, y + this.height + 2, -1); 
/*     */   }
/*     */   
/*     */   protected void extractTooltipForNextRenderPass(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
/* 184 */     if ((Minecraft.getInstance()).gui.screen() == this.configureWorldScreen)
/* 185 */       super.extractTooltipForNextRenderPass(graphics, mouseX, mouseY); 
/*     */   }
/*     */   
/*     */   public void updateSlotState(RealmsServer serverData) {
/* 190 */     this.state = setServerData(serverData);
/* 191 */     this.slotNameWidget.setMessage((Component)Component.literal(this.state.slotName));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\RealmsWorldSlotButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */