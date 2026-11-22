/*     */ package net.minecraft.client.gui.screens.dialog.input;
/*     */ 
/*     */ import net.minecraft.client.gui.components.AbstractSliderButton;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.nbt.FloatTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.dialog.action.Action;
/*     */ import net.minecraft.server.dialog.input.InputControl;
/*     */ import net.minecraft.server.dialog.input.NumberRangeInput;
/*     */ 
/*     */ class NumberRangeHandler implements InputControlHandler<NumberRangeInput> {
/*     */   private static class SliderImpl extends AbstractSliderButton {
/*     */     private final NumberRangeInput input;
/*     */     
/*     */     private SliderImpl(NumberRangeInput input, double initialSliderValue) {
/* 144 */       super(0, 0, input.width(), 20, computeMessage(input, initialSliderValue), initialSliderValue);
/* 145 */       this.input = input;
/*     */     }
/*     */     
/*     */     protected void updateMessage() {
/* 150 */       setMessage(computeMessage(this.input, this.value));
/*     */     }
/*     */     
/*     */     protected void applyValue() {}
/*     */     
/*     */     public String stringValueToSend() {
/* 158 */       return sliderValueToString(this.input, this.value);
/*     */     }
/*     */     
/*     */     public float floatValueToSend() {
/* 162 */       return scaledValue(this.input, this.value);
/*     */     }
/*     */     
/*     */     private static float scaledValue(NumberRangeInput input, double sliderValue) {
/* 166 */       return input.rangeInfo().computeScaledValue((float)sliderValue);
/*     */     }
/*     */     
/*     */     private static String sliderValueToString(NumberRangeInput input, double sliderValue) {
/* 170 */       return valueToString(scaledValue(input, sliderValue));
/*     */     }
/*     */     
/*     */     private static Component computeMessage(NumberRangeInput input, double sliderValue) {
/* 174 */       return input.computeLabel(sliderValueToString(input, sliderValue));
/*     */     }
/*     */     
/*     */     private static String valueToString(float v) {
/* 178 */       int intV = (int)v;
/* 179 */       if (intV == v)
/* 181 */         return Integer.toString(intV); 
/* 183 */       return Float.toString(v);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addControl(NumberRangeInput input, Screen screen, InputControlHandler.Output output) {
/* 189 */     float initialValue = input.rangeInfo().initialSliderValue();
/* 190 */     final SliderImpl control = new SliderImpl(input, initialValue);
/* 191 */     output.accept((LayoutElement)control, new Action.ValueGetter(this) {
/*     */           public String asTemplateSubstitution() {
/* 194 */             return control.stringValueToSend();
/*     */           }
/*     */           
/*     */           public Tag asTag() {
/* 199 */             return (Tag)FloatTag.valueOf(control.floatValueToSend());
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\input\InputControlHandlers$NumberRangeHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */