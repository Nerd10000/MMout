/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SelectWorldScreen extends Screen {
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  32 */   public static final WorldOptions TEST_OPTIONS = new WorldOptions("test1".hashCode(), true, false);
/*     */   
/*     */   protected final Screen lastScreen;
/*     */   
/*     */   private final HeaderAndFooterLayout layout;
/*     */   
/*     */   private Button deleteButton;
/*     */   
/*     */   private Button playWorldButton;
/*     */   
/*     */   private Button editButton;
/*     */   
/*     */   private Button recreateButton;
/*     */   
/*     */   protected EditBox searchBox;
/*     */   
/*     */   private WorldSelectionList list;
/*     */   
/*     */   public SelectWorldScreen(Screen lastScreen) {
/*  49 */     super((Component)Component.translatable("selectWorld.title"));
/*     */     Objects.requireNonNull((Minecraft.getInstance()).font);
/*     */     this.layout = new HeaderAndFooterLayout(this, 8 + 9 + 8 + 20 + 4, 60);
/*  50 */     this.lastScreen = lastScreen;
/*     */   }
/*     */   
/*     */   protected void init() {
/*  55 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(4));
/*  56 */     header.defaultCellSetting().alignHorizontallyCenter();
/*  57 */     header.addChild((LayoutElement)new StringWidget(this.title, this.font));
/*  59 */     LinearLayout subHeader = (LinearLayout)header.addChild((LayoutElement)LinearLayout.horizontal().spacing(4));
/*  60 */     if (SharedConstants.DEBUG_WORLD_RECREATE)
/*  61 */       subHeader.addChild((LayoutElement)createDebugWorldRecreateButton()); 
/*  63 */     this.searchBox = (EditBox)subHeader.addChild((LayoutElement)new EditBox(this.font, this.width / 2 - 100, 22, 200, 20, this.searchBox, (Component)Component.translatable("selectWorld.search")));
/*  64 */     this.searchBox.setResponder(value -> {
/*     */           if (this.list != null)
/*     */             this.list.updateFilter(value); 
/*     */         });
/*  69 */     this.searchBox.setHint((Component)Component.translatable("gui.selectWorld.search").setStyle(EditBox.SEARCH_HINT_STYLE));
/*  71 */     Consumer<WorldSelectionList.WorldListEntry> joinWorld = WorldSelectionList.WorldListEntry::joinWorld;
/*  72 */     this.list = (WorldSelectionList)this.layout.addToContents((LayoutElement)(new WorldSelectionList.Builder(this.minecraft, this)).width(this.width).height(this.layout.getContentHeight()).filter(this.searchBox.getValue()).oldList(this.list)
/*  73 */         .onEntrySelect(this::updateButtonStatus).onEntryInteract(joinWorld)
/*  74 */         .build());
/*  76 */     createFooterButtons(joinWorld, this.list);
/*  78 */     SelectWorldScreen selectWorldScreen = this;
/*  78 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/*  79 */     repositionElements();
/*  80 */     updateButtonStatus((LevelSummary)null);
/*     */   }
/*     */   
/*     */   private void createFooterButtons(Consumer<WorldSelectionList.WorldListEntry> joinWorld, WorldSelectionList list) {
/*  84 */     GridLayout footer = (GridLayout)this.layout.addToFooter((LayoutElement)(new GridLayout()).columnSpacing(8).rowSpacing(4));
/*  85 */     footer.defaultCellSetting().alignHorizontallyCenter();
/*  86 */     GridLayout.RowHelper rowHelper = footer.createRowHelper(4);
/*  87 */     this.playWorldButton = (Button)rowHelper.addChild((LayoutElement)Button.builder(LevelSummary.PLAY_WORLD, button -> list.getSelectedOpt().ifPresent(joinWorld)).build(), 2);
/*  88 */     rowHelper.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.create"), button -> {
/*     */             Objects.requireNonNull(list);
/*     */             CreateWorldScreen.openFresh(this.minecraft, list::returnToScreen);
/*  88 */           }).build(), 2);
/*  89 */     this.editButton = (Button)rowHelper.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.edit"), button -> list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::editWorld)).width(71).build());
/*  90 */     this.deleteButton = (Button)rowHelper.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.delete"), button -> list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::deleteWorld)).width(71).build());
/*  91 */     this.recreateButton = (Button)rowHelper.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.recreate"), button -> list.getSelectedOpt().ifPresent(WorldSelectionList.WorldListEntry::recreateWorld)).width(71).build());
/*  92 */     rowHelper.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> this.minecraft.gui.setScreen(this.lastScreen)).width(71).build());
/*     */   }
/*     */   
/*     */   private Button createDebugWorldRecreateButton() {
/*  96 */     return Button.builder((Component)Component.literal("DEBUG recreate"), button -> {
/*     */           try {
/*     */             String levelName = "DEBUG world";
/*     */             if (this.list != null && !this.list.children().isEmpty()) {
/*     */               WorldSelectionList.Entry entry = this.list.children().getFirst();
/*     */               if (entry instanceof WorldSelectionList.WorldListEntry) {
/*     */                 WorldSelectionList.WorldListEntry worldEntry = (WorldSelectionList.WorldListEntry)entry;
/*     */                 if (worldEntry.getLevelName().equals("DEBUG world"))
/*     */                   worldEntry.doDeleteWorld(); 
/*     */               } 
/*     */             } 
/*     */             LevelSettings levelSettings = new LevelSettings("DEBUG world", GameType.SPECTATOR, LevelSettings.DifficultySettings.DEFAULT, true, WorldDataConfiguration.DEFAULT);
/*     */             String resultFolder = FileUtil.findAvailableName(this.minecraft.getLevelSource().getBaseDir(), "DEBUG world", "");
/*     */             this.minecraft.createWorldOpenFlows().createFreshLevel(resultFolder, levelSettings, TEST_OPTIONS, WorldPresets::createNormalWorldDimensions, this);
/* 110 */           } catch (IOException e) {
/*     */             LOGGER.error("Failed to recreate the debug world", e);
/*     */           } 
/* 113 */         }).width(72).build();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 118 */     if (this.list != null)
/* 119 */       this.list.updateSize(this.width, this.layout); 
/* 121 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   protected void setInitialFocus() {
/* 126 */     if (this.searchBox != null)
/* 127 */       setInitialFocus((GuiEventListener)this.searchBox); 
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 133 */     this.minecraft.gui.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   public void updateButtonStatus(LevelSummary summary) {
/* 137 */     if (this.playWorldButton == null || this.editButton == null || this.recreateButton == null || this.deleteButton == null)
/*     */       return; 
/* 140 */     if (summary == null) {
/* 141 */       this.playWorldButton.setMessage(LevelSummary.PLAY_WORLD);
/* 142 */       this.playWorldButton.active = false;
/* 143 */       this.editButton.active = false;
/* 144 */       this.recreateButton.active = false;
/* 145 */       this.deleteButton.active = false;
/*     */     } else {
/* 147 */       this.playWorldButton.setMessage(summary.primaryActionMessage());
/* 148 */       this.playWorldButton.active = summary.primaryActionActive();
/* 149 */       this.editButton.active = summary.canEdit();
/* 150 */       this.recreateButton.active = summary.canRecreate();
/* 151 */       this.deleteButton.active = summary.canDelete();
/* 152 */       if (summary.requiresFileFixing()) {
/* 153 */         this.editButton.setTooltip(Tooltip.create((Component)Component.translatable("selectWorld.requiresFileFixingTooltip.edit")));
/* 154 */         this.playWorldButton.setTooltip(Tooltip.create((Component)Component.translatable("selectWorld.requiresFileFixingTooltip.play")));
/* 155 */         this.recreateButton.setTooltip(Tooltip.create((Component)Component.translatable("selectWorld.requiresFileFixingTooltip.recreate")));
/*     */       } else {
/* 157 */         this.editButton.setTooltip(null);
/* 158 */         this.playWorldButton.setTooltip(null);
/* 159 */         this.recreateButton.setTooltip(null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removed() {
/* 167 */     if (this.list != null)
/* 168 */       this.list.children().forEach(WorldSelectionList.Entry::close); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\SelectWorldScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */