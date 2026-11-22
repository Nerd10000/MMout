/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
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
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ 
/*     */ class DetailsScreen extends Screen {
/*  85 */   private static final Component TITLE = (Component)Component.translatable("selectWorld.experimental.details.title");
/*     */   
/*  87 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private PackList list;
/*     */   
/*     */   private DetailsScreen() {
/*  92 */     super(TITLE);
/*     */   }
/*     */   
/*     */   protected void init() {
/*  97 */     this.layout.addTitleHeader(TITLE, this.font);
/*  98 */     this.list = (PackList)this.layout.addToContents((LayoutElement)new PackList(this, this.minecraft, ConfirmExperimentalFeaturesScreen.this.enabledPacks));
/*  99 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose()).build());
/* 100 */     DetailsScreen detailsScreen = this;
/* 100 */     this.layout.visitWidgets(x$0 -> rec$.addRenderableWidget((GuiEventListener)x$0));
/* 101 */     repositionElements();
/*     */   }
/*     */   
/*     */   protected void repositionElements() {
/* 106 */     if (this.list != null)
/* 107 */       this.list.updateSize(this.width, this.layout); 
/* 109 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   public void onClose() {
/* 114 */     this.minecraft.gui.setScreen(ConfirmExperimentalFeaturesScreen.this);
/*     */   }
/*     */   
/*     */   private class PackList extends ObjectSelectionList<PackListEntry> {
/*     */     public PackList(ConfirmExperimentalFeaturesScreen.DetailsScreen this$0, Minecraft minecraft, Collection<Pack> selectedPacks) {
/* 119 */       super(minecraft, this$0.width, this$0.layout.getContentHeight(), this$0.layout.getHeaderHeight(), (9 + 2) * 3);
/* 121 */       for (Pack pack : selectedPacks) {
/* 122 */         String nonVanillaFeatures = FeatureFlags.printMissingFlags(FeatureFlags.VANILLA_SET, pack.getRequestedFeatures());
/* 123 */         if (!nonVanillaFeatures.isEmpty()) {
/* 124 */           Component title = ComponentUtils.mergeStyles(pack.getTitle(), Style.EMPTY.withBold(Boolean.valueOf(true)));
/* 125 */           MutableComponent mutableComponent = Component.translatable("selectWorld.experimental.details.entry", new Object[] { nonVanillaFeatures });
/* 126 */           addEntry((AbstractSelectionList.Entry)new ConfirmExperimentalFeaturesScreen.DetailsScreen.PackListEntry(title, (Component)mutableComponent, MultiLineLabel.create(this$0.font, (Component)mutableComponent, getRowWidth())));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public int getRowWidth() {
/* 133 */       return this.width * 3 / 4;
/*     */     }
/*     */   }
/*     */   
/*     */   private class PackListEntry extends ObjectSelectionList.Entry<PackListEntry> {
/*     */     private final Component packId;
/*     */     
/*     */     private final Component message;
/*     */     
/*     */     private final MultiLineLabel splitMessage;
/*     */     
/*     */     private PackListEntry(Component packId, Component message, MultiLineLabel splitMessage) {
/* 143 */       this.packId = packId;
/* 144 */       this.message = message;
/* 145 */       this.splitMessage = splitMessage;
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 150 */       ActiveTextCollector textRenderer = graphics.textRenderer();
/* 151 */       graphics.text(ConfirmExperimentalFeaturesScreen.DetailsScreen.this.minecraft.font, this.packId, getContentX(), getContentY(), -1);
/* 152 */       Objects.requireNonNull(ConfirmExperimentalFeaturesScreen.DetailsScreen.this.font);
/* 152 */       this.splitMessage.visitLines(TextAlignment.LEFT, getContentX(), getContentY() + 12, 9, textRenderer);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 157 */       return (Component)Component.translatable("narrator.select", new Object[] { CommonComponents.joinForNarration(new Component[] { this.packId, this.message }) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\ConfirmExperimentalFeaturesScreen$DetailsScreen.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */