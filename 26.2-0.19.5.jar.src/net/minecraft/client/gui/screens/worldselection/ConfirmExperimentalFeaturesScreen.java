/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.MultiLineLabel;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ 
/*     */ public class ConfirmExperimentalFeaturesScreen extends Screen {
/*  30 */   private static final Component TITLE = (Component)Component.translatable("selectWorld.experimental.title");
/*     */   
/*  31 */   private static final Component MESSAGE = (Component)Component.translatable("selectWorld.experimental.message");
/*     */   
/*  32 */   private static final Component DETAILS_BUTTON = (Component)Component.translatable("selectWorld.experimental.details");
/*     */   
/*     */   private static final int COLUMN_SPACING = 10;
/*     */   
/*     */   private static final int DETAILS_BUTTON_WIDTH = 100;
/*     */   
/*     */   private final BooleanConsumer callback;
/*     */   
/*     */   private final Collection<Pack> enabledPacks;
/*     */   
/*  39 */   private final GridLayout layout = (new GridLayout()).columnSpacing(10).rowSpacing(20);
/*     */   
/*     */   public ConfirmExperimentalFeaturesScreen(Collection<Pack> enabledPacks, BooleanConsumer callback) {
/*  42 */     super(TITLE);
/*  43 */     this.enabledPacks = enabledPacks;
/*  44 */     this.callback = callback;
/*     */   }
/*     */   
/*     */   public Component getNarrationMessage() {
/*  49 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), MESSAGE });
/*     */   }
/*     */   
/*     */   protected void init() {
/*  54 */     super.init();
/*  57 */     GridLayout.RowHelper helper = this.layout.createRowHelper(2);
/*  59 */     LayoutSettings centered = helper.newCellSettings().alignHorizontallyCenter();
/*  60 */     helper.addChild((LayoutElement)new StringWidget(this.title, this.font), 2, centered);
/*  61 */     MultiLineTextWidget messageLabel = (MultiLineTextWidget)helper.addChild((LayoutElement)(new MultiLineTextWidget(MESSAGE, this.font)).setCentered(true), 2, centered);
/*  62 */     messageLabel.setMaxWidth(310);
/*  64 */     helper.addChild((LayoutElement)Button.builder(DETAILS_BUTTON, paramButton -> this.minecraft.gui.setScreen(new DetailsScreen())).width(100).build(), 2, centered);
/*  66 */     helper.addChild((LayoutElement)Button.builder(CommonComponents.GUI_PROCEED, button -> this.callback.accept(true)).build());
/*  67 */     helper.addChild((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> this.callback.accept(false)).build());
/*  69 */     ConfirmExperimentalFeaturesScreen confirmExperimentalFeaturesScreen = this;
/*  69 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/*  70 */     this.layout.arrangeElements();
/*  71 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/*  76 */     FrameLayout.alignInRectangle((LayoutElement)this.layout, 0, 0, this.width, this.height, 0.5F, 0.5F);
/*     */   }
/*     */   
/*     */   public void onClose() {
/*  81 */     this.callback.accept(false);
/*     */   }
/*     */   
/*     */   private class DetailsScreen extends Screen {
/*  85 */     private static final Component TITLE = (Component)Component.translatable("selectWorld.experimental.details.title");
/*     */     
/*  87 */     private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */     
/*     */     private PackList list;
/*     */     
/*     */     private DetailsScreen() {
/*  92 */       super(TITLE);
/*     */     }
/*     */     
/*     */     protected void init() {
/*  97 */       this.layout.addTitleHeader(TITLE, this.font);
/*  98 */       this.list = (PackList)this.layout.addToContents((LayoutElement)new PackList(this, this.minecraft, ConfirmExperimentalFeaturesScreen.this.enabledPacks));
/*  99 */       this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).build());
/* 100 */       DetailsScreen detailsScreen = this;
/* 100 */       this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/* 101 */       repositionElements();
/*     */     }
/*     */     
/*     */     protected void repositionElements() {
/* 106 */       if (this.list != null)
/* 107 */         this.list.updateSize(this.width, this.layout); 
/* 109 */       this.layout.arrangeElements();
/*     */     }
/*     */     
/*     */     public void onClose() {
/* 114 */       this.minecraft.gui.setScreen(ConfirmExperimentalFeaturesScreen.this);
/*     */     }
/*     */     
/*     */     private class PackList extends ObjectSelectionList<PackListEntry> {
/*     */       public PackList(ConfirmExperimentalFeaturesScreen.DetailsScreen this$0, Minecraft minecraft, Collection<Pack> selectedPacks) {
/* 119 */         super(minecraft, this$0.width, this$0.layout.getContentHeight(), this$0.layout.getHeaderHeight(), (9 + 2) * 3);
/* 121 */         for (Pack pack : selectedPacks) {
/* 122 */           String nonVanillaFeatures = FeatureFlags.printMissingFlags(FeatureFlags.VANILLA_SET, pack.getRequestedFeatures());
/* 123 */           if (!nonVanillaFeatures.isEmpty()) {
/* 124 */             Component title = ComponentUtils.mergeStyles(pack.getTitle(), Style.EMPTY.withBold(Boolean.valueOf(true)));
/* 125 */             MutableComponent mutableComponent = Component.translatable("selectWorld.experimental.details.entry", new Object[] { nonVanillaFeatures });
/* 126 */             addEntry((AbstractSelectionList.Entry)new ConfirmExperimentalFeaturesScreen.DetailsScreen.PackListEntry(title, (Component)mutableComponent, MultiLineLabel.create(this$0.font, (Component)mutableComponent, getRowWidth())));
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/*     */       public int getRowWidth() {
/* 133 */         return this.width * 3 / 4;
/*     */       }
/*     */     }
/*     */     
/*     */     private class PackListEntry extends ObjectSelectionList.Entry<PackListEntry> {
/*     */       private final Component packId;
/*     */       
/*     */       private final Component message;
/*     */       
/*     */       private final MultiLineLabel splitMessage;
/*     */       
/*     */       private PackListEntry(Component packId, Component message, MultiLineLabel splitMessage) {
/* 143 */         this.packId = packId;
/* 144 */         this.message = message;
/* 145 */         this.splitMessage = splitMessage;
/*     */       }
/*     */       
/*     */       public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 150 */         ActiveTextCollector textRenderer = graphics.textRenderer();
/* 151 */         graphics.text(ConfirmExperimentalFeaturesScreen.DetailsScreen.this.minecraft.font, this.packId, getContentX(), getContentY(), -1);
/* 152 */         Objects.requireNonNull(ConfirmExperimentalFeaturesScreen.DetailsScreen.this.font);
/* 152 */         this.splitMessage.visitLines(TextAlignment.LEFT, getContentX(), getContentY() + 12, 9, textRenderer);
/*     */       }
/*     */       
/*     */       public Component getNarration() {
/* 157 */         return (Component)Component.translatable("narrator.select", new Object[] { CommonComponents.joinForNarration(new Component[] { this.packId, this.message }) });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private class PackList extends ObjectSelectionList<DetailsScreen.PackListEntry> {
/*     */     public PackList(ConfirmExperimentalFeaturesScreen this$0, Minecraft minecraft, Collection<Pack> selectedPacks) {
/*     */       super(minecraft, ((ConfirmExperimentalFeaturesScreen.DetailsScreen)this$0).width, ((ConfirmExperimentalFeaturesScreen.DetailsScreen)this$0).layout.getContentHeight(), ((ConfirmExperimentalFeaturesScreen.DetailsScreen)this$0).layout.getHeaderHeight(), (9 + 2) * 3);
/*     */       for (Pack pack : selectedPacks) {
/*     */         String nonVanillaFeatures = FeatureFlags.printMissingFlags(FeatureFlags.VANILLA_SET, pack.getRequestedFeatures());
/*     */         if (!nonVanillaFeatures.isEmpty()) {
/*     */           Component title = ComponentUtils.mergeStyles(pack.getTitle(), Style.EMPTY.withBold(Boolean.valueOf(true)));
/*     */           MutableComponent mutableComponent = Component.translatable("selectWorld.experimental.details.entry", new Object[] { nonVanillaFeatures });
/*     */           addEntry((AbstractSelectionList.Entry)new ConfirmExperimentalFeaturesScreen.DetailsScreen.PackListEntry(title, (Component)mutableComponent, MultiLineLabel.create(((ConfirmExperimentalFeaturesScreen.DetailsScreen)this$0).font, (Component)mutableComponent, getRowWidth())));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public int getRowWidth() {
/*     */       return this.width * 3 / 4;
/*     */     }
/*     */   }
/*     */   
/*     */   private class PackListEntry extends ObjectSelectionList.Entry<DetailsScreen.PackListEntry> {
/*     */     private final Component packId;
/*     */     
/*     */     private final Component message;
/*     */     
/*     */     private final MultiLineLabel splitMessage;
/*     */     
/*     */     private PackListEntry(Component packId, Component message, MultiLineLabel splitMessage) {
/*     */       this.packId = packId;
/*     */       this.message = message;
/*     */       this.splitMessage = splitMessage;
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       ActiveTextCollector textRenderer = graphics.textRenderer();
/*     */       graphics.text(this.this$1.minecraft.font, this.packId, getContentX(), getContentY(), -1);
/*     */       Objects.requireNonNull(this.this$1.font);
/*     */       this.splitMessage.visitLines(TextAlignment.LEFT, getContentX(), getContentY() + 12, 9, textRenderer);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 157 */       return (Component)Component.translatable("narrator.select", new Object[] { CommonComponents.joinForNarration(new Component[] { this.packId, this.message }) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\ConfirmExperimentalFeaturesScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */