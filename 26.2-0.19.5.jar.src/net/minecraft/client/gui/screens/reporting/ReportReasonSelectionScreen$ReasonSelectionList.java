/*     */ package net.minecraft.client.gui.screens.reporting;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.multiplayer.chat.report.ReportReason;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class ReasonSelectionList extends ObjectSelectionList<ReportReasonSelectionScreen.ReasonSelectionList.Entry> {
/*     */   public ReasonSelectionList(Minecraft minecraft) {
/* 142 */     super(minecraft, ReportReasonSelectionScreen.this.width, ReportReasonSelectionScreen.this.listHeight(), ReportReasonSelectionScreen.this.layout.getHeaderHeight(), 18);
/* 143 */     for (ReportReason reason : ReportReason.values()) {
/* 144 */       if (!ReportReason.getIncompatibleCategories(ReportReasonSelectionScreen.this.reportType).contains(reason))
/* 145 */         addEntry((AbstractSelectionList.Entry)new Entry(reason)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Entry findEntry(ReportReason reason) {
/* 151 */     return children().stream()
/* 152 */       .filter(entry -> (entry.reason == reason))
/* 153 */       .findFirst()
/* 154 */       .orElse(null);
/*     */   }
/*     */   
/*     */   public int getRowWidth() {
/* 159 */     return 320;
/*     */   }
/*     */   
/*     */   public void setSelected(Entry selected) {
/* 164 */     super.setSelected((AbstractSelectionList.Entry)selected);
/* 165 */     ReportReasonSelectionScreen.this.currentlySelectedReason = (selected != null) ? selected.getReason() : null;
/*     */   }
/*     */   
/*     */   public class Entry extends ObjectSelectionList.Entry<Entry> {
/*     */     private final ReportReason reason;
/*     */     
/*     */     public Entry(ReportReason reason) {
/* 172 */       this.reason = reason;
/*     */     }
/*     */     
/*     */     public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 177 */       int textX = getContentX() + 1;
/* 178 */       Objects.requireNonNull(ReportReasonSelectionScreen.access$000(this.this$1.this$0));
/* 178 */       int textY = getContentY() + (getContentHeight() - 9) / 2 + 1;
/* 179 */       graphics.text(ReportReasonSelectionScreen.access$100(this.this$1.this$0), this.reason.title(), textX, textY, -1);
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/* 184 */       return (Component)Component.translatable("gui.abuseReport.reason.narration", new Object[] { this.reason.title(), this.reason.description() });
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 189 */       ReportReasonSelectionScreen.ReasonSelectionList.this.setSelected(this);
/* 190 */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */     
/*     */     public ReportReason getReason() {
/* 194 */       return this.reason;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\reporting\ReportReasonSelectionScreen$ReasonSelectionList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */