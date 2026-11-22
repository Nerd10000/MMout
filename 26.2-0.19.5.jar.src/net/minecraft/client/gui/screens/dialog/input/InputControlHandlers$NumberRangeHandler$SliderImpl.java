/*     */ package net.minecraft.client.gui.screens.dialog.input;
/*     */ 
/*     */ import net.minecraft.client.gui.components.AbstractSliderButton;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.dialog.input.NumberRangeInput;
/*     */ 
/*     */ class SliderImpl extends AbstractSliderButton {
/*     */   private final NumberRangeInput input;
/*     */   
/*     */   private SliderImpl(NumberRangeInput input, double initialSliderValue) {
/* 144 */     super(0, 0, input.width(), 20, computeMessage(input, initialSliderValue), initialSliderValue);
/* 145 */     this.input = input;
/*     */   }
/*     */   
/*     */   protected void updateMessage() {
/* 150 */     setMessage(computeMessage(this.input, this.value));
/*     */   }
/*     */   
/*     */   protected void applyValue() {}
/*     */   
/*     */   public String stringValueToSend() {
/* 158 */     return sliderValueToString(this.input, this.value);
/*     */   }
/*     */   
/*     */   public float floatValueToSend() {
/* 162 */     return scaledValue(this.input, this.value);
/*     */   }
/*     */   
/*     */   private static float scaledValue(NumberRangeInput input, double sliderValue) {
/* 166 */     return input.rangeInfo().computeScaledValue((float)sliderValue);
/*     */   }
/*     */   
/*     */   private static String sliderValueToString(NumberRangeInput input, double sliderValue) {
/* 170 */     return valueToString(scaledValue(input, sliderValue));
/*     */   }
/*     */   
/*     */   private static Component computeMessage(NumberRangeInput input, double sliderValue) {
/* 174 */     return input.computeLabel(sliderValueToString(input, sliderValue));
/*     */   }
/*     */   
/*     */   private static String valueToString(float v) {
/* 178 */     int intV = (int)v;
/* 179 */     if (intV == v)
/* 181 */       return Integer.toString(intV); 
/* 183 */     return Float.toString(v);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\input\InputControlHandlers$NumberRangeHandler$SliderImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */