/*    */ package net.minecraft.client.gui.screens.options.controls;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.KeyMapping;
/*    */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*    */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.narration.NarratableEntry;
/*    */ 
/*    */ public class CategoryEntry extends KeyBindsList.Entry {
/*    */   private final FocusableTextWidget categoryName;
/*    */   
/*    */   public CategoryEntry(KeyMapping.Category category) {
/* 73 */     this.categoryName = FocusableTextWidget.builder(category.label(), (KeyBindsList.access$000(KeyBindsList.this)).font).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS).build();
/*    */   }
/*    */   
/*    */   public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 78 */     this.categoryName.setPosition(KeyBindsList.access$100(KeyBindsList.this) / 2 - this.categoryName.getWidth() / 2, getContentBottom() - this.categoryName.getHeight());
/* 79 */     this.categoryName.extractRenderState(graphics, mouseX, mouseY, a);
/*    */   }
/*    */   
/*    */   public List<? extends GuiEventListener> children() {
/* 84 */     return (List)List.of(this.categoryName);
/*    */   }
/*    */   
/*    */   public List<? extends NarratableEntry> narratables() {
/* 89 */     return (List)List.of(this.categoryName);
/*    */   }
/*    */   
/*    */   public void refreshEntry() {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\options\controls\KeyBindsList$CategoryEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */