/*     */ package net.minecraft.client.gui.screens.dialog.input;
/*     */ 
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.Checkbox;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.nbt.ByteTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.server.dialog.action.Action;
/*     */ import net.minecraft.server.dialog.input.BooleanInput;
/*     */ import net.minecraft.server.dialog.input.InputControl;
/*     */ 
/*     */ class BooleanHandler implements InputControlHandler<BooleanInput> {
/*     */   public void addControl(final BooleanInput input, Screen screen, InputControlHandler.Output output) {
/* 123 */     Font font = screen.getFont();
/* 124 */     final Checkbox control = Checkbox.builder(input.label(), font).selected(input.initial()).build();
/* 125 */     output.accept((LayoutElement)control, new Action.ValueGetter(this) {
/*     */           public String asTemplateSubstitution() {
/* 128 */             return control.selected() ? input.onTrue() : input.onFalse();
/*     */           }
/*     */           
/*     */           public Tag asTag() {
/* 133 */             return (Tag)ByteTag.valueOf(control.selected());
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\input\InputControlHandlers$BooleanHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */