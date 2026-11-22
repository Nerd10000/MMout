/*     */ package net.minecraft.client.gui.screens.debug;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ class CategoryEntry extends DebugOptionsScreen.AbstractOptionEntry {
/*     */   private final Component category;
/*     */   
/*     */   public CategoryEntry(Component category) {
/* 190 */     this.category = category;
/*     */   }
/*     */   
/*     */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 195 */     graphics.centeredText((DebugOptionsScreen.access$000(DebugOptionsScreen.this)).font, this.category, getContentX() + getContentWidth() / 2, getContentY() + 5, -1);
/*     */   }
/*     */   
/*     */   public List<? extends GuiEventListener> children() {
/* 200 */     return (List<? extends GuiEventListener>)ImmutableList.of();
/*     */   }
/*     */   
/*     */   public List<? extends NarratableEntry> narratables() {
/* 205 */     return (List<? extends NarratableEntry>)ImmutableList.of(new NarratableEntry() {
/*     */           public NarratableEntry.NarrationPriority narrationPriority() {
/* 208 */             return NarratableEntry.NarrationPriority.HOVERED;
/*     */           }
/*     */           
/*     */           public void updateNarration(NarrationElementOutput output) {
/* 213 */             output.add(NarratedElementType.TITLE, DebugOptionsScreen.CategoryEntry.this.category);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void refreshEntry() {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\debug\DebugOptionsScreen$CategoryEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */