/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import net.minecraft.client.gui.components.AbstractSliderButton;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ class null extends AbstractSliderButton {
/*     */   null(int x, int y, int width, int height, Component message, double initialValue) {
/* 146 */     super(x, y, width, height, message, initialValue);
/* 148 */     updateMessage();
/*     */   }
/*     */   
/*     */   protected void updateMessage() {
/* 153 */     setMessage((Component)Component.translatable("jigsaw_block.levels", new Object[] { Integer.valueOf(this.this$0.levels) }));
/*     */   }
/*     */   
/*     */   protected void applyValue() {
/* 158 */     JigsawBlockEditScreen.this.levels = Mth.floor(Mth.clampedLerp(this.value, 0.0D, 20.0D));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\inventory\JigsawBlockEditScreen$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */