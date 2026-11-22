/*     */ package net.minecraft.client.gui.screens.debug;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.floats.FloatComparators;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.debug.DebugEntryCategory;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntry;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class OptionList extends ContainerObjectSelectionList<DebugOptionsScreen.AbstractOptionEntry> {
/*     */   private static final Comparator<Map.Entry<Identifier, DebugScreenEntry>> COMPARATOR;
/*     */   
/*     */   private static final int ITEM_HEIGHT = 20;
/*     */   
/*     */   static {
/* 130 */     COMPARATOR = ((o1, o2) -> {
/*     */         int byCategory = FloatComparators.NATURAL_COMPARATOR.compare(((DebugScreenEntry)o1.getValue()).category().sortKey(), ((DebugScreenEntry)o2.getValue()).category().sortKey());
/*     */         return (byCategory != 0) ? byCategory : ((Identifier)o1.getKey()).compareTo((Identifier)o2.getKey());
/*     */       });
/*     */   }
/*     */   
/*     */   public OptionList() {
/* 140 */     super(Minecraft.getInstance(), DebugOptionsScreen.this.width, DebugOptionsScreen.this.layout.getContentHeight(), DebugOptionsScreen.this.layout.getHeaderHeight(), 20);
/* 141 */     updateSearch("");
/*     */   }
/*     */   
/*     */   public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 146 */     super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 151 */     return 350;
/*     */   }
/*     */   
/*     */   public void refreshEntries() {
/* 155 */     children().forEach(DebugOptionsScreen.AbstractOptionEntry::refreshEntry);
/*     */   }
/*     */   
/*     */   public void updateSearch(String value) {
/* 159 */     clearEntries();
/* 160 */     List<Map.Entry<Identifier, DebugScreenEntry>> all = new ArrayList<>(DebugScreenEntries.allEntries().entrySet());
/* 161 */     all.sort(COMPARATOR);
/* 162 */     DebugEntryCategory currentCategory = null;
/* 163 */     for (Map.Entry<Identifier, DebugScreenEntry> entry : all) {
/* 164 */       if (((Identifier)entry.getKey()).getPath().contains(value)) {
/* 165 */         DebugEntryCategory newCategory = ((DebugScreenEntry)entry.getValue()).category();
/* 166 */         if (!newCategory.equals(currentCategory)) {
/* 167 */           addEntry((AbstractSelectionList.Entry)new DebugOptionsScreen.CategoryEntry(DebugOptionsScreen.this, newCategory.label()));
/* 168 */           currentCategory = newCategory;
/*     */         } 
/* 170 */         addEntry((AbstractSelectionList.Entry)new DebugOptionsScreen.OptionEntry(DebugOptionsScreen.this, entry.getKey()));
/*     */       } 
/*     */     } 
/* 173 */     notifyListUpdated();
/*     */   }
/*     */   
/*     */   private void notifyListUpdated() {
/* 177 */     refreshScrollAmount();
/* 178 */     DebugOptionsScreen.this.triggerImmediateNarration(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\debug\DebugOptionsScreen$OptionList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */