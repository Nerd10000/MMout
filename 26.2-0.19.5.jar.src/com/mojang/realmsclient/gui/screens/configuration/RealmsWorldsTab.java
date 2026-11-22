/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.dto.WorldTemplate;
/*     */ import com.mojang.realmsclient.gui.RealmsWorldSlotButton;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsPopups;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsResetWorldScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsSelectWorldTemplateScreen;
/*     */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*     */ import com.mojang.realmsclient.util.task.SwitchMinigameTask;
/*     */ import com.mojang.realmsclient.util.task.SwitchSlotTask;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class RealmsWorldsTab extends GridLayoutTab implements RealmsConfigurationTab {
/*  27 */   static final Component TITLE = (Component)Component.translatable("mco.configure.worlds.title");
/*     */   
/*     */   private final RealmsConfigureWorldScreen configurationScreen;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private RealmsServer serverData;
/*     */   
/*     */   private final Button optionsButton;
/*     */   
/*     */   private final Button backupButton;
/*     */   
/*     */   private final Button resetWorldButton;
/*     */   
/*  35 */   private final List<RealmsWorldSlotButton> slotButtonList = Lists.newArrayList();
/*     */   
/*     */   public RealmsWorldsTab(RealmsConfigureWorldScreen configurationScreen, Minecraft minecraft, RealmsServer serverData) {
/*  38 */     super(TITLE);
/*  39 */     this.configurationScreen = configurationScreen;
/*  40 */     this.minecraft = minecraft;
/*  41 */     this.serverData = serverData;
/*  42 */     GridLayout.RowHelper helper = this.layout.spacing(20).createRowHelper(1);
/*  44 */     GridLayout.RowHelper slots = (new GridLayout()).spacing(16).createRowHelper(4);
/*  45 */     this.slotButtonList.clear();
/*  46 */     for (int i = 1; i < 5; i++)
/*  47 */       this.slotButtonList.add((RealmsWorldSlotButton)slots.addChild((LayoutElement)createSlotButton(i), LayoutSettings.defaults().alignVerticallyBottom())); 
/*  49 */     helper.addChild((LayoutElement)slots.getGrid());
/*  51 */     GridLayout.RowHelper buttons = (new GridLayout()).spacing(8).createRowHelper(1);
/*  53 */     this.optionsButton = (Button)buttons.addChild((LayoutElement)Button.builder((Component)Component.translatable("mco.configure.world.buttons.options"), paramButton -> minecraft.gui.setScreen((Screen)new RealmsSlotOptionsScreen(configurationScreen, ((RealmsSlot)serverData.slots.get(Integer.valueOf(serverData.activeSlot))).copy(), serverData.worldType, serverData.activeSlot)))
/*     */         
/*  55 */         .bounds(0, 0, 150, 20).build());
/*  57 */     this.backupButton = (Button)buttons.addChild((LayoutElement)Button.builder((Component)Component.translatable("mco.configure.world.backup"), paramButton -> minecraft.gui.setScreen((Screen)new RealmsBackupScreen(configurationScreen, serverData.copy(), serverData.activeSlot)))
/*     */         
/*  59 */         .bounds(0, 0, 150, 20).build());
/*  61 */     this.resetWorldButton = (Button)buttons.addChild((LayoutElement)Button.builder((Component)Component.empty(), button -> resetButtonPressed())
/*     */         
/*  63 */         .bounds(0, 0, 150, 20).build());
/*  65 */     helper.addChild((LayoutElement)buttons.getGrid(), LayoutSettings.defaults().alignHorizontallyCenter());
/*  66 */     this.backupButton.active = true;
/*  67 */     updateData(serverData);
/*     */   }
/*     */   
/*     */   private void resetButtonPressed() {
/*  71 */     if (isMinigame()) {
/*  72 */       this.minecraft.gui.setScreen((Screen)new RealmsSelectWorldTemplateScreen((Component)Component.translatable("mco.template.title.minigame"), this::templateSelectionCallback, RealmsServer.WorldType.MINIGAME, null));
/*     */     } else {
/*  74 */       this.minecraft.gui.setScreen((Screen)RealmsResetWorldScreen.forResetSlot((Screen)this.configurationScreen, this.serverData.copy(), () -> this.minecraft.execute(())));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void templateSelectionCallback(WorldTemplate worldTemplate) {
/*  79 */     if (worldTemplate != null && WorldTemplate.WorldTemplateType.MINIGAME == worldTemplate.type()) {
/*  80 */       this.configurationScreen.stateChanged();
/*  81 */       RealmsConfigureWorldScreen newScreen = this.configurationScreen.getNewScreen();
/*  82 */       this.minecraft.gui.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)newScreen, new LongRunningTask[] { (LongRunningTask)new SwitchMinigameTask(this.serverData.id, worldTemplate, newScreen) }));
/*     */     } else {
/*  84 */       this.minecraft.gui.setScreen((Screen)this.configurationScreen);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isMinigame() {
/*  89 */     return this.serverData.isMinigameActive();
/*     */   }
/*     */   
/*     */   public void onSelected(RealmsServer serverData) {
/*  94 */     updateData(serverData);
/*     */   }
/*     */   
/*     */   public void updateData(RealmsServer serverData) {
/*  99 */     this.serverData = serverData;
/* 100 */     this.optionsButton.active = (!serverData.expired && !isMinigame());
/* 101 */     this.resetWorldButton.active = !serverData.expired;
/* 102 */     if (isMinigame()) {
/* 103 */       this.resetWorldButton.setMessage((Component)Component.translatable("mco.configure.world.buttons.switchminigame"));
/*     */     } else {
/* 105 */       boolean emptySlot = (serverData.slots.containsKey(Integer.valueOf(serverData.activeSlot)) && ((RealmsSlot)serverData.slots.get(Integer.valueOf(serverData.activeSlot))).options.empty);
/* 106 */       if (emptySlot) {
/* 107 */         this.resetWorldButton.setMessage((Component)Component.translatable("mco.configure.world.buttons.newworld"));
/*     */       } else {
/* 109 */         this.resetWorldButton.setMessage((Component)Component.translatable("mco.configure.world.buttons.resetworld"));
/*     */       } 
/*     */     } 
/* 112 */     this.backupButton.active = !isMinigame();
/* 113 */     for (RealmsWorldSlotButton realmsWorldSlotButton : this.slotButtonList) {
/* 114 */       RealmsWorldSlotButton.State state = realmsWorldSlotButton.setServerData(serverData);
/* 115 */       if (state.activeSlot) {
/* 116 */         realmsWorldSlotButton.setSize(80, 80);
/*     */         continue;
/*     */       } 
/* 118 */       realmsWorldSlotButton.setSize(50, 50);
/*     */     } 
/* 121 */     ((RealmsWorldSlotButton)this.slotButtonList.get(serverData.activeSlot - 1)).updateSlotState(serverData);
/*     */   }
/*     */   
/*     */   private RealmsWorldSlotButton createSlotButton(int i) {
/* 125 */     return new RealmsWorldSlotButton(this.configurationScreen, 0, 0, 80, 80, i, this.serverData, button -> {
/*     */           RealmsWorldSlotButton.State state = ((RealmsWorldSlotButton)button).getState();
/*     */           switch (state.action) {
/*     */             case NOTHING:
/*     */               return;
/*     */             case SWITCH_SLOT:
/*     */               if (state.minigame) {
/*     */                 switchToMinigame();
/*     */               } else if (state.empty) {
/*     */                 switchToEmptySlot(i, this.serverData);
/*     */               } else {
/*     */                 switchToFullSlot(i, this.serverData);
/*     */               } 
/*     */           } 
/*     */           throw new IllegalStateException("Unknown action " + String.valueOf(state.action));
/*     */         });
/*     */   }
/*     */   
/*     */   private void switchToMinigame() {
/* 148 */     RealmsSelectWorldTemplateScreen screen = new RealmsSelectWorldTemplateScreen((Component)Component.translatable("mco.template.title.minigame"), this::templateSelectionCallback, RealmsServer.WorldType.MINIGAME, null, List.of(
/* 149 */           Component.translatable("mco.minigame.world.info.line1").withColor(-4539718), 
/* 150 */           Component.translatable("mco.minigame.world.info.line2").withColor(-4539718)));
/* 153 */     this.minecraft.gui.setScreen((Screen)screen);
/*     */   }
/*     */   
/*     */   private void switchToFullSlot(int selectedSlot, RealmsServer serverData) {
/* 157 */     this.minecraft.gui.setScreen((Screen)RealmsPopups.infoPopupScreen((Screen)this.configurationScreen, 
/* 158 */           (Component)Component.translatable("mco.configure.world.slot.switch.question.line1"), popup -> {
/*     */             RealmsConfigureWorldScreen newScreen = this.configurationScreen.getNewScreen();
/*     */             this.configurationScreen.stateChanged();
/*     */             this.minecraft.gui.setScreen((Screen)new RealmsLongRunningMcoTaskScreen((Screen)newScreen, new LongRunningTask[] { (LongRunningTask)new SwitchSlotTask(serverData.id, selectedSlot, ()) }));
/*     */           }));
/*     */   }
/*     */   
/*     */   private void switchToEmptySlot(int selectedSlot, RealmsServer serverData) {
/* 169 */     this.minecraft.gui.setScreen(
/* 170 */         (Screen)RealmsPopups.infoPopupScreen((Screen)this.configurationScreen, 
/*     */           
/* 172 */           (Component)Component.translatable("mco.configure.world.slot.switch.question.line1"), paramPopupScreen -> {
/*     */             this.configurationScreen.stateChanged();
/*     */             RealmsResetWorldScreen resetWorldScreen = RealmsResetWorldScreen.forEmptySlot((Screen)this.configurationScreen, selectedSlot, serverData, ());
/*     */             this.minecraft.gui.setScreen((Screen)resetWorldScreen);
/*     */           }));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsWorldsTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */