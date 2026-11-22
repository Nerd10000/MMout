/*     */ package net.minecraft.client.gui.screens.achievement;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.resources.language.I18n;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.stats.Stat;
/*     */ import net.minecraft.stats.Stats;
/*     */ 
/*     */ class GeneralStatisticsList extends ObjectSelectionList<StatsScreen.GeneralStatisticsList.Entry> {
/*     */   public GeneralStatisticsList(Minecraft minecraft) {
/* 204 */     super(minecraft, paramStatsScreen.width, paramStatsScreen.layout.getContentHeight(), 33, 14);
/* 206 */     ObjectArrayList<Stat<Identifier>> stats = new ObjectArrayList(Stats.CUSTOM.iterator());
/* 207 */     stats.sort(Comparator.comparing(k -> I18n.get(StatsScreen.getTranslationKey(k), new Object[0])));
/* 208 */     for (ObjectListIterator<Stat<Identifier>> objectListIterator = stats.iterator(); objectListIterator.hasNext(); ) {
/* 208 */       Stat<Identifier> stat = objectListIterator.next();
/* 209 */       addEntry((AbstractSelectionList.Entry)new Entry(stat));
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 215 */     return 280;
/*     */   }
/*     */   
/*     */   protected void extractListBackground(GuiGraphicsExtractor graphics) {}
/*     */   
/*     */   protected void extractListSeparators(GuiGraphicsExtractor graphics) {}
/*     */   
/*     */   private class Entry extends ObjectSelectionList.Entry<Entry> {
/*     */     private final Stat<Identifier> stat;
/*     */     
/*     */     private final Component statDisplay;
/*     */     
/*     */     private Entry(Stat<Identifier> stat) {
/* 231 */       this.stat = stat;
/* 232 */       this.statDisplay = (Component)Component.translatable(StatsScreen.getTranslationKey(stat));
/*     */     }
/*     */     
/*     */     private String getValueText() {
/* 236 */       return this.stat.format(this.this$1.this$0.stats.getValue(this.stat));
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 241 */       Objects.requireNonNull(StatsScreen.access$000(this.this$1.this$0));
/* 241 */       int y = getContentYMiddle() - 9 / 2;
/* 242 */       int index = StatsScreen.GeneralStatisticsList.this.children().indexOf(this);
/* 243 */       int color = (index % 2 == 0) ? -1 : -4539718;
/* 244 */       graphics.text(StatsScreen.access$100(this.this$1.this$0), this.statDisplay, getContentX() + 2, y, color);
/* 245 */       String msg = getValueText();
/* 246 */       graphics.text(StatsScreen.access$200(this.this$1.this$0), msg, getContentRight() - StatsScreen.access$300(this.this$1.this$0).width(msg) - 4, y, color);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 251 */       return (Component)Component.translatable("narrator.select", new Object[] { Component.empty().append(this.statDisplay).append(CommonComponents.SPACE).append(getValueText()) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\achievement\StatsScreen$GeneralStatisticsList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */