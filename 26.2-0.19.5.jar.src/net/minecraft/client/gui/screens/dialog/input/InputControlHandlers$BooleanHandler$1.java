/*     */ package net.minecraft.client.gui.screens.dialog.input;
/*     */ 
/*     */ import net.minecraft.client.gui.components.Checkbox;
/*     */ import net.minecraft.nbt.ByteTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.server.dialog.action.Action;
/*     */ import net.minecraft.server.dialog.input.BooleanInput;
/*     */ 
/*     */ class null implements Action.ValueGetter {
/*     */   null(InputControlHandlers.BooleanHandler this$0) {}
/*     */   
/*     */   public String asTemplateSubstitution() {
/* 128 */     return control.selected() ? input.onTrue() : input.onFalse();
/*     */   }
/*     */   
/*     */   public Tag asTag() {
/* 133 */     return (Tag)ByteTag.valueOf(control.selected());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\input\InputControlHandlers$BooleanHandler$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */