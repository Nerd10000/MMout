/*     */ package net.minecraft.client.gui.screens.dialog.input;
/*     */ 
/*     */ import net.minecraft.nbt.FloatTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.server.dialog.action.Action;
/*     */ 
/*     */ class null implements Action.ValueGetter {
/*     */   null(InputControlHandlers.NumberRangeHandler this$0) {}
/*     */   
/*     */   public String asTemplateSubstitution() {
/* 194 */     return control.stringValueToSend();
/*     */   }
/*     */   
/*     */   public Tag asTag() {
/* 199 */     return (Tag)FloatTag.valueOf(control.floatValueToSend());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\input\InputControlHandlers$NumberRangeHandler$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */