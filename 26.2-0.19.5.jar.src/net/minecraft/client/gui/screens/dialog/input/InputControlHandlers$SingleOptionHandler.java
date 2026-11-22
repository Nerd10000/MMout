/*     */ package net.minecraft.client.gui.screens.dialog.input;
/*     */ 
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.server.dialog.action.Action;
/*     */ import net.minecraft.server.dialog.input.InputControl;
/*     */ import net.minecraft.server.dialog.input.SingleOptionInput;
/*     */ 
/*     */ class SingleOptionHandler implements InputControlHandler<SingleOptionInput> {
/*     */   public void addControl(SingleOptionInput input, Screen screen, InputControlHandler.Output output) {
/* 109 */     SingleOptionInput.Entry initial = input.initial().orElse(input.entries().getFirst());
/* 112 */     CycleButton.Builder<SingleOptionInput.Entry> controlBuilder = CycleButton.builder(SingleOptionInput.Entry::displayOrDefault, initial).withValues(input.entries()).displayState(!input.labelVisible() ? CycleButton.DisplayState.VALUE : CycleButton.DisplayState.NAME_AND_VALUE);
/* 114 */     CycleButton<SingleOptionInput.Entry> control = controlBuilder.create(0, 0, input.width(), 20, input.label());
/* 116 */     output.accept((LayoutElement)control, Action.ValueGetter.of(() -> ((SingleOptionInput.Entry)control.getValue()).id()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\input\InputControlHandlers$SingleOptionHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */