/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.dto.RealmsWorldOptions;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.components.AbstractSliderButton;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.CommonLayouts;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class RealmsSlotOptionsScreen extends RealmsScreen {
/*     */   private static final int DEFAULT_DIFFICULTY = 2;
/*     */   
/*  30 */   public static final List<Difficulty> DIFFICULTIES = (List<Difficulty>)ImmutableList.of(Difficulty.PEACEFUL, Difficulty.EASY, Difficulty.NORMAL, Difficulty.HARD);
/*     */   
/*     */   private static final int DEFAULT_GAME_MODE = 0;
/*     */   
/*  38 */   public static final List<GameType> GAME_MODES = (List<GameType>)ImmutableList.of(GameType.SURVIVAL, GameType.CREATIVE, GameType.ADVENTURE);
/*     */   
/*  44 */   private static final Component TITLE = (Component)Component.translatable("mco.configure.world.buttons.options");
/*     */   
/*  45 */   private static final Component WORLD_NAME_EDIT_LABEL = (Component)Component.translatable("mco.configure.world.edit.slot.name");
/*     */   
/*  46 */   private static final Component SPAWN_PROTECTION_TEXT = (Component)Component.translatable("mco.configure.world.spawnProtection");
/*     */   
/*  47 */   private static final Component GAME_MODE_BUTTON = (Component)Component.translatable("selectWorld.gameMode");
/*     */   
/*  48 */   private static final Component DIFFICULTY_BUTTON = (Component)Component.translatable("options.difficulty");
/*     */   
/*  49 */   private static final Component FORCE_GAME_MODE_BUTTON = (Component)Component.translatable("mco.configure.world.forceGameMode");
/*     */   
/*     */   private static final int SPACING = 8;
/*     */   
/*  52 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout((Screen)this);
/*     */   
/*     */   private final RealmsConfigureWorldScreen parentScreen;
/*     */   
/*     */   private final RealmsSlot slot;
/*     */   
/*     */   private final RealmsServer.WorldType worldType;
/*     */   
/*     */   private final String defaultSlotName;
/*     */   
/*     */   private int spawnProtection;
/*     */   
/*     */   private boolean forceGameMode;
/*     */   
/*     */   private Difficulty difficulty;
/*     */   
/*     */   private GameType gameMode;
/*     */   
/*     */   private String worldName;
/*     */   
/*     */   private StringWidget warningHeader;
/*     */   
/*     */   private SettingsSlider spawnProtectionButton;
/*     */   
/*     */   public RealmsSlotOptionsScreen(RealmsConfigureWorldScreen configureWorldScreen, RealmsSlot slot, RealmsServer.WorldType worldType, int activeSlot) {
/*  69 */     super(TITLE);
/*  70 */     this.parentScreen = configureWorldScreen;
/*  71 */     this.slot = slot;
/*  72 */     this.worldType = worldType;
/*  74 */     this.difficulty = findByIndex(DIFFICULTIES, slot.options.difficulty, 2);
/*  75 */     this.gameMode = findByIndex(GAME_MODES, slot.options.gameMode, 0);
/*  77 */     this.defaultSlotName = slot.options.getDefaultSlotName(activeSlot);
/*  78 */     setWorldName(slot.options.getSlotName(activeSlot));
/*  80 */     if (worldType == RealmsServer.WorldType.NORMAL) {
/*  81 */       this.spawnProtection = slot.options.spawnProtection;
/*  82 */       this.forceGameMode = slot.options.forceGameMode;
/*     */     } else {
/*  84 */       this.spawnProtection = 0;
/*  85 */       this.forceGameMode = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void init() {
/*  91 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(8));
/*  92 */     header.defaultCellSetting().alignHorizontallyCenter();
/*  93 */     header.addChild((LayoutElement)new StringWidget(TITLE, this.minecraft.font));
/*  94 */     switch (this.worldType) {
/*     */       case ADVENTUREMAP:
/*     */       
/*     */       case INSPIRATION:
/*     */       
/*     */       case EXPERIENCE:
/*     */       
/*     */       default:
/*     */         break;
/*     */     } 
/*  94 */     Component warning = 
/*     */       
/*  98 */       null;
/* 100 */     if (warning != null) {
/* 101 */       Objects.requireNonNull(this.font);
/* 101 */       this.layout.setHeaderHeight(41 + 9 + 8);
/* 102 */       this.warningHeader = (StringWidget)header.addChild((LayoutElement)new StringWidget(warning, this.font));
/*     */     } 
/* 105 */     GridLayout contentGrid = (GridLayout)this.layout.addToContents((LayoutElement)(new GridLayout()).spacing(8));
/* 106 */     contentGrid.defaultCellSetting().alignHorizontallyCenter();
/* 107 */     GridLayout.RowHelper rowHelper = contentGrid.createRowHelper(2);
/* 108 */     EditBox worldNameEdit = new EditBox(this.minecraft.font, 0, 0, 150, 20, null, WORLD_NAME_EDIT_LABEL);
/* 109 */     worldNameEdit.setValue(this.worldName);
/* 110 */     worldNameEdit.setResponder(this::setWorldName);
/* 111 */     rowHelper.addChild((LayoutElement)CommonLayouts.labeledElement(this.font, (LayoutElement)worldNameEdit, WORLD_NAME_EDIT_LABEL), 2);
/* 112 */     CycleButton<Difficulty> difficultyCycleButton = (CycleButton<Difficulty>)rowHelper.addChild((LayoutElement)CycleButton.builder(Difficulty::getDisplayName, this.difficulty).withValues(DIFFICULTIES)
/* 113 */         .create(0, 0, 150, 20, DIFFICULTY_BUTTON, (paramCycleButton, value) -> this.difficulty = value));
/* 115 */     CycleButton<GameType> gameTypeCycleButton = (CycleButton<GameType>)rowHelper.addChild((LayoutElement)CycleButton.builder(GameType::getShortDisplayName, this.gameMode).withValues(GAME_MODES)
/* 116 */         .create(0, 0, 150, 20, GAME_MODE_BUTTON, (paramCycleButton, value) -> this.gameMode = value));
/* 118 */     CycleButton<Boolean> forceGameModeButton = (CycleButton<Boolean>)rowHelper.addChild((LayoutElement)CycleButton.onOffBuilder(this.forceGameMode)
/* 119 */         .create(0, 0, 150, 20, FORCE_GAME_MODE_BUTTON, (paramCycleButton, value) -> this.forceGameMode = value.booleanValue()));
/* 121 */     this.spawnProtectionButton = (SettingsSlider)rowHelper.addChild((LayoutElement)new SettingsSlider(0, 0, 150, this.spawnProtection, 0.0F, 16.0F));
/* 123 */     if (this.worldType != RealmsServer.WorldType.NORMAL) {
/* 124 */       this.spawnProtectionButton.active = false;
/* 125 */       forceGameModeButton.active = false;
/*     */     } 
/* 128 */     if (this.slot.isHardcore()) {
/* 129 */       difficultyCycleButton.active = false;
/* 130 */       gameTypeCycleButton.active = false;
/* 131 */       forceGameModeButton.active = false;
/*     */     } 
/* 134 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 135 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CONTINUE, paramButton -> saveSettings()).build());
/* 136 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, paramButton -> onClose()).build());
/* 138 */     RealmsSlotOptionsScreen realmsSlotOptionsScreen = this;
/* 138 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/* 139 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 144 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 149 */     this.minecraft.gui.setScreen((Screen)this.parentScreen);
/*     */   }
/*     */   
/*     */   private static <T> T findByIndex(List<T> values, int index, int defaultIndex) {
/* 153 */     if (index < 0 || index >= values.size())
/* 154 */       return values.get(defaultIndex); 
/* 156 */     return values.get(index);
/*     */   }
/*     */   
/*     */   private static <T> int findIndex(List<T> values, T value, int defaultIndex) {
/* 160 */     int result = values.indexOf(value);
/* 161 */     return (result == -1) ? defaultIndex : result;
/*     */   }
/*     */   
/*     */   public Component getNarrationMessage() {
/* 166 */     if (this.warningHeader == null)
/* 167 */       return super.getNarrationMessage(); 
/* 169 */     return (Component)CommonComponents.joinForNarration(new Component[] { getTitle(), this.warningHeader.getMessage() });
/*     */   }
/*     */   
/*     */   private void setWorldName(String value) {
/* 173 */     if (value.equals(this.defaultSlotName)) {
/* 174 */       this.worldName = "";
/*     */     } else {
/* 176 */       this.worldName = value;
/*     */     } 
/*     */   }
/*     */   
/*     */   private class SettingsSlider extends AbstractSliderButton {
/*     */     private final double minValue;
/*     */     
/*     */     private final double maxValue;
/*     */     
/*     */     public SettingsSlider(int x, int y, int width, int currentValue, float minValue, float maxValue) {
/* 185 */       super(x, y, width, 20, CommonComponents.EMPTY, 0.0D);
/* 187 */       this.minValue = minValue;
/* 188 */       this.maxValue = maxValue;
/* 190 */       this.value = ((Mth.clamp(currentValue, minValue, maxValue) - minValue) / (maxValue - minValue));
/* 192 */       updateMessage();
/*     */     }
/*     */     
/*     */     public void applyValue() {
/* 197 */       if (!RealmsSlotOptionsScreen.this.spawnProtectionButton.active)
/*     */         return; 
/* 201 */       RealmsSlotOptionsScreen.this.spawnProtection = (int)Mth.lerp(Mth.clamp(this.value, 0.0D, 1.0D), this.minValue, this.maxValue);
/*     */     }
/*     */     
/*     */     protected void updateMessage() {
/* 206 */       setMessage((Component)CommonComponents.optionNameValue(RealmsSlotOptionsScreen.SPAWN_PROTECTION_TEXT, (RealmsSlotOptionsScreen.this.spawnProtection == 0) ? CommonComponents.OPTION_OFF : (Component)Component.literal(String.valueOf(RealmsSlotOptionsScreen.this.spawnProtection))));
/*     */     }
/*     */   }
/*     */   
/*     */   private void saveSettings() {
/* 211 */     int difficultyId = findIndex(DIFFICULTIES, this.difficulty, 2);
/* 212 */     int gameModeId = findIndex(GAME_MODES, this.gameMode, 0);
/* 214 */     if (this.worldType == RealmsServer.WorldType.ADVENTUREMAP || this.worldType == RealmsServer.WorldType.EXPERIENCE || this.worldType == RealmsServer.WorldType.INSPIRATION) {
/* 215 */       this.parentScreen.saveSlotSettings(new RealmsSlot(this.slot.slotId, new RealmsWorldOptions(this.slot.options.spawnProtection, difficultyId, gameModeId, this.slot.options.forceGameMode, this.worldName, this.slot.options.version, this.slot.options.compatibility), this.slot.settings));
/*     */     } else {
/* 217 */       this.parentScreen.saveSlotSettings(new RealmsSlot(this.slot.slotId, new RealmsWorldOptions(this.spawnProtection, difficultyId, gameModeId, this.forceGameMode, this.worldName, this.slot.options.version, this.slot.options.compatibility), this.slot.settings));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsSlotOptionsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */