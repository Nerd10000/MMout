/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.ActiveTextCollector;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.TextAlignment;
/*     */ import net.minecraft.client.gui.components.MultiLineLabel;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class PackListEntry extends ObjectSelectionList.Entry<ConfirmExperimentalFeaturesScreen.DetailsScreen.PackListEntry> {
/*     */   private final Component packId;
/*     */   
/*     */   private final Component message;
/*     */   
/*     */   private final MultiLineLabel splitMessage;
/*     */   
/*     */   private PackListEntry(Component packId, Component message, MultiLineLabel splitMessage) {
/* 143 */     this.packId = packId;
/* 144 */     this.message = message;
/* 145 */     this.splitMessage = splitMessage;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 150 */     ActiveTextCollector textRenderer = graphics.textRenderer();
/* 151 */     graphics.text((ConfirmExperimentalFeaturesScreen.DetailsScreen.access$100(ConfirmExperimentalFeaturesScreen.DetailsScreen.this)).font, this.packId, getContentX(), getContentY(), -1);
/* 152 */     Objects.requireNonNull(ConfirmExperimentalFeaturesScreen.DetailsScreen.access$200(ConfirmExperimentalFeaturesScreen.DetailsScreen.this));
/* 152 */     this.splitMessage.visitLines(TextAlignment.LEFT, getContentX(), getContentY() + 12, 9, textRenderer);
/*     */   }
/*     */   
/*     */   public Component getNarration() {
/* 157 */     return (Component)Component.translatable("narrator.select", new Object[] { CommonComponents.joinForNarration(new Component[] { this.packId, this.message }) });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\ConfirmExperimentalFeaturesScreen$DetailsScreen$PackListEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */