/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ class null extends EditBox {
/*     */   null(CreateWorldScreen.WorldTab this$1, Font font, int width, int height, Component narration) {
/* 218 */     super(font, width, height, narration);
/*     */   }
/*     */   
/*     */   protected MutableComponent createNarrationMessage() {
/* 221 */     return super.createNarrationMessage().append(CommonComponents.NARRATION_SEPARATOR).append(CreateWorldScreen.WorldTab.SEED_EMPTY_HINT);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\CreateWorldScreen$WorldTab$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */