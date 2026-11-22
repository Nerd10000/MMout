/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.MultiLineLabel;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ 
/*     */ class PackList extends ObjectSelectionList<ConfirmExperimentalFeaturesScreen.DetailsScreen.PackListEntry> {
/*     */   public PackList(ConfirmExperimentalFeaturesScreen.DetailsScreen paramDetailsScreen, Minecraft minecraft, Collection<Pack> selectedPacks) {
/* 119 */     super(minecraft, paramDetailsScreen.width, paramDetailsScreen.layout.getContentHeight(), paramDetailsScreen.layout.getHeaderHeight(), (9 + 2) * 3);
/* 121 */     for (Pack pack : selectedPacks) {
/* 122 */       String nonVanillaFeatures = FeatureFlags.printMissingFlags(FeatureFlags.VANILLA_SET, pack.getRequestedFeatures());
/* 123 */       if (!nonVanillaFeatures.isEmpty()) {
/* 124 */         Component title = ComponentUtils.mergeStyles(pack.getTitle(), Style.EMPTY.withBold(Boolean.valueOf(true)));
/* 125 */         MutableComponent mutableComponent = Component.translatable("selectWorld.experimental.details.entry", new Object[] { nonVanillaFeatures });
/* 126 */         addEntry((AbstractSelectionList.Entry)new ConfirmExperimentalFeaturesScreen.DetailsScreen.PackListEntry(paramDetailsScreen, title, (Component)mutableComponent, MultiLineLabel.create(ConfirmExperimentalFeaturesScreen.DetailsScreen.access$000(paramDetailsScreen), (Component)mutableComponent, getRowWidth())));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 133 */     return this.width * 3 / 4;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\ConfirmExperimentalFeaturesScreen$DetailsScreen$PackList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */