/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class Builder {
/*    */   private final Component message;
/*    */   
/*    */   private final Button.OnPress onPress;
/*    */   
/*    */   private Tooltip tooltip;
/*    */   
/*    */   private int x;
/*    */   
/*    */   private int y;
/*    */   
/* 46 */   private int width = 150;
/*    */   
/* 47 */   private int height = 20;
/*    */   
/* 48 */   private Button.CreateNarration createNarration = Button.DEFAULT_NARRATION;
/*    */   
/*    */   public Builder(Component message, Button.OnPress onPress) {
/* 51 */     this.message = message;
/* 52 */     this.onPress = onPress;
/*    */   }
/*    */   
/*    */   public Builder pos(int x, int y) {
/* 56 */     this.x = x;
/* 57 */     this.y = y;
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   public Builder width(int width) {
/* 62 */     this.width = width;
/* 63 */     return this;
/*    */   }
/*    */   
/*    */   public Builder size(int width, int height) {
/* 67 */     this.width = width;
/* 68 */     this.height = height;
/* 69 */     return this;
/*    */   }
/*    */   
/*    */   public Builder bounds(int x, int y, int width, int height) {
/* 73 */     return pos(x, y).size(width, height);
/*    */   }
/*    */   
/*    */   public Builder tooltip(Tooltip tooltip) {
/* 77 */     this.tooltip = tooltip;
/* 78 */     return this;
/*    */   }
/*    */   
/*    */   public Builder createNarration(Button.CreateNarration createNarration) {
/* 82 */     this.createNarration = createNarration;
/* 83 */     return this;
/*    */   }
/*    */   
/*    */   public Button build() {
/* 87 */     Button button = new Button.Plain(this.x, this.y, this.width, this.height, this.message, this.onPress, this.createNarration);
/* 88 */     button.setTooltip(this.tooltip);
/* 89 */     return button;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\Button$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */