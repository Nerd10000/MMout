/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.OptionInstance;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class Builder<T> {
/*     */   private final Supplier<T> defaultValueSupplier;
/*     */   
/*     */   private final Function<T, Component> valueStringifier;
/*     */   
/*     */   private OptionInstance.TooltipSupplier<T> tooltipSupplier = value -> null;
/*     */   
/*     */   private CycleButton.SpriteSupplier<T> spriteSupplier = (button, value) -> null;
/*     */   
/* 194 */   private Function<CycleButton<T>, MutableComponent> narrationProvider = CycleButton::createDefaultNarrationMessage;
/*     */   
/* 196 */   private CycleButton.ValueListSupplier<T> values = CycleButton.ValueListSupplier.create((Collection<T>)ImmutableList.of());
/*     */   
/* 197 */   private CycleButton.DisplayState displayState = CycleButton.DisplayState.NAME_AND_VALUE;
/*     */   
/*     */   public Builder(Function<T, Component> valueStringifier, Supplier<T> defaultValueSupplier) {
/* 200 */     this.valueStringifier = valueStringifier;
/* 201 */     this.defaultValueSupplier = defaultValueSupplier;
/*     */   }
/*     */   
/*     */   public Builder<T> withValues(Collection<T> values) {
/* 205 */     return withValues(CycleButton.ValueListSupplier.create(values));
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public final Builder<T> withValues(T... values) {
/* 210 */     return withValues((Collection<T>)ImmutableList.copyOf((Object[])values));
/*     */   }
/*     */   
/*     */   public Builder<T> withValues(List<T> values, List<T> altValues) {
/* 214 */     return withValues(CycleButton.ValueListSupplier.create(CycleButton.DEFAULT_ALT_LIST_SELECTOR, values, altValues));
/*     */   }
/*     */   
/*     */   public Builder<T> withValues(BooleanSupplier altCondition, List<T> values, List<T> altValues) {
/* 218 */     return withValues(CycleButton.ValueListSupplier.create(altCondition, values, altValues));
/*     */   }
/*     */   
/*     */   public Builder<T> withValues(CycleButton.ValueListSupplier<T> valueListSupplier) {
/* 222 */     this.values = valueListSupplier;
/* 223 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> withTooltip(OptionInstance.TooltipSupplier<T> tooltipSupplier) {
/* 227 */     this.tooltipSupplier = tooltipSupplier;
/* 228 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> withCustomNarration(Function<CycleButton<T>, MutableComponent> narrationProvider) {
/* 232 */     this.narrationProvider = narrationProvider;
/* 233 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> withSprite(CycleButton.SpriteSupplier<T> spriteSupplier) {
/* 237 */     this.spriteSupplier = spriteSupplier;
/* 238 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> displayState(CycleButton.DisplayState state) {
/* 242 */     this.displayState = state;
/* 243 */     return this;
/*     */   }
/*     */   
/*     */   public Builder<T> displayOnlyValue() {
/* 247 */     return displayState(CycleButton.DisplayState.VALUE);
/*     */   }
/*     */   
/*     */   public CycleButton<T> create(Component name, CycleButton.OnValueChange<T> valueChangeListener) {
/* 251 */     return create(0, 0, 150, 20, name, valueChangeListener);
/*     */   }
/*     */   
/*     */   public CycleButton<T> create(int x, int y, int width, int height, Component name) {
/* 255 */     return create(x, y, width, height, name, (button, value) -> {
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   public CycleButton<T> create(int x, int y, int width, int height, Component name, CycleButton.OnValueChange<T> valueChangeListener) {
/* 259 */     List<T> values = this.values.getDefaultList();
/* 260 */     if (values.isEmpty())
/* 261 */       throw new IllegalStateException("No values for cycle button"); 
/* 264 */     T initialValue = this.defaultValueSupplier.get();
/* 265 */     int initialIndex = values.indexOf(initialValue);
/* 266 */     Component valueText = this.valueStringifier.apply(initialValue);
/* 267 */     Component initialTitle = (this.displayState == CycleButton.DisplayState.VALUE) ? valueText : (Component)CommonComponents.optionNameValue(name, valueText);
/* 269 */     return new CycleButton<>(x, y, width, height, initialTitle, name, initialIndex, initialValue, this.defaultValueSupplier, this.values, this.valueStringifier, this.narrationProvider, valueChangeListener, this.tooltipSupplier, this.displayState, this.spriteSupplier);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\CycleButton$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */