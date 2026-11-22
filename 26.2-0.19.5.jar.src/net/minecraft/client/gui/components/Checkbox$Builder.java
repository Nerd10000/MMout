/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.OptionInstance;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class Builder {
/*    */   private final Component message;
/*    */   
/*    */   private final Font font;
/*    */   
/*    */   private int maxWidth;
/*    */   
/* 43 */   private int x = 0;
/*    */   
/* 44 */   private int y = 0;
/*    */   
/* 45 */   private Checkbox.OnValueChange onValueChange = Checkbox.OnValueChange.NOP;
/*    */   
/*    */   private boolean selected = false;
/*    */   
/* 47 */   private OptionInstance<Boolean> option = null;
/*    */   
/* 48 */   private Tooltip tooltip = null;
/*    */   
/*    */   private Builder(Component message, Font font) {
/* 51 */     this.message = message;
/* 52 */     this.font = font;
/* 53 */     this.maxWidth = Checkbox.getDefaultWidth(message, font);
/*    */   }
/*    */   
/*    */   public Builder pos(int x, int y) {
/* 57 */     this.x = x;
/* 58 */     this.y = y;
/* 59 */     return this;
/*    */   }
/*    */   
/*    */   public Builder onValueChange(Checkbox.OnValueChange onValueChange) {
/* 63 */     this.onValueChange = onValueChange;
/* 64 */     return this;
/*    */   }
/*    */   
/*    */   public Builder selected(boolean selected) {
/* 68 */     this.selected = selected;
/* 69 */     this.option = null;
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public Builder selected(OptionInstance<Boolean> option) {
/* 74 */     this.option = option;
/* 75 */     this.selected = ((Boolean)option.get()).booleanValue();
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public Builder tooltip(Tooltip tooltip) {
/* 80 */     this.tooltip = tooltip;
/* 81 */     return this;
/*    */   }
/*    */   
/*    */   public Builder maxWidth(int maxWidth) {
/* 85 */     this.maxWidth = maxWidth;
/* 86 */     return this;
/*    */   }
/*    */   
/*    */   public Checkbox build() {
/* 90 */     Checkbox.OnValueChange onChange = (this.option == null) ? this.onValueChange : ((checkbox, value) -> {
/*    */         this.option.set(Boolean.valueOf(value));
/*    */         this.onValueChange.onValueChange(checkbox, value);
/*    */       });
/* 95 */     Checkbox box = new Checkbox(this.x, this.y, this.maxWidth, this.message, this.font, this.selected, onChange);
/* 96 */     if (box.overflowsRowLimit(this.font))
/* 97 */       box.setTooltip(this.tooltip); 
/* 99 */     return box;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\Checkbox$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */