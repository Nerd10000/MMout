/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.DoubleFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.IntSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.client.gui.GuiGraphicsExtractor;
/*     */ import net.minecraft.client.gui.components.AbstractOptionSliderButton;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.ResettableOptionWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class OptionInstance<T> {
/*  38 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  39 */   public static final Enum<Boolean> BOOLEAN_VALUES = new Enum<>((List<Boolean>)ImmutableList.of(Boolean.TRUE, Boolean.FALSE), (Codec<Boolean>)Codec.BOOL);
/*     */   
/*     */   public static final CaptionBasedToString<Boolean> BOOLEAN_TO_STRING;
/*     */   
/*     */   static {
/*  40 */     BOOLEAN_TO_STRING = ((paramComponent, b) -> b.booleanValue() ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
/*     */   }
/*     */   
/*     */   public static final ValueUpdateListener<Object> NO_ACTION = paramObject -> {
/*     */     
/*     */     };
/*     */   
/*     */   private final TooltipSupplier<T> tooltip;
/*     */   
/*     */   private final Function<T, Component> toString;
/*     */   
/*     */   private final ValueSet<T> values;
/*     */   
/*     */   private final Codec<T> codec;
/*     */   
/*     */   private final T initialValue;
/*     */   
/*     */   private final ValueUpdateListener<? super T> onValueUpdate;
/*     */   
/*     */   private final Component caption;
/*     */   
/*     */   private T value;
/*     */   
/*     */   public static OptionInstance<Boolean> createBoolean(String captionId, boolean initialValue, ValueUpdateListener<? super Boolean> onValueUpdate) {
/*  55 */     return createBoolean(captionId, noTooltip(), initialValue, onValueUpdate);
/*     */   }
/*     */   
/*     */   public static OptionInstance<Boolean> createBoolean(String captionId, boolean initialValue) {
/*  59 */     return createBoolean(captionId, noTooltip(), initialValue, (ValueUpdateListener)NO_ACTION);
/*     */   }
/*     */   
/*     */   public static OptionInstance<Boolean> createBoolean(String captionId, TooltipSupplier<Boolean> tooltip, boolean initialValue) {
/*  63 */     return createBoolean(captionId, tooltip, initialValue, (ValueUpdateListener)NO_ACTION);
/*     */   }
/*     */   
/*     */   public static OptionInstance<Boolean> createBoolean(String captionId, TooltipSupplier<Boolean> tooltip, boolean initialValue, ValueUpdateListener<? super Boolean> onValueUpdate) {
/*  67 */     return createBoolean(captionId, tooltip, BOOLEAN_TO_STRING, initialValue, onValueUpdate);
/*     */   }
/*     */   
/*     */   public static OptionInstance<Boolean> createBoolean(String captionId, TooltipSupplier<Boolean> tooltip, CaptionBasedToString<Boolean> toString, boolean initialValue, ValueUpdateListener<? super Boolean> onValueUpdate) {
/*  77 */     return new OptionInstance<>(captionId, tooltip, toString, BOOLEAN_VALUES, 
/*     */         
/*  82 */         Boolean.valueOf(initialValue), onValueUpdate);
/*     */   }
/*     */   
/*     */   public OptionInstance(String captionId, TooltipSupplier<T> tooltip, CaptionBasedToString<T> toString, ValueSet<T> values, T initialValue, ValueUpdateListener<? super T> onValueUpdate) {
/*  88 */     this(captionId, tooltip, toString, values, values.codec(), initialValue, onValueUpdate);
/*     */   }
/*     */   
/*     */   public OptionInstance(String captionId, TooltipSupplier<T> tooltip, CaptionBasedToString<T> toString, ValueSet<T> values, Codec<T> codec, T initialValue, ValueUpdateListener<? super T> onValueUpdate) {
/*  99 */     this.caption = (Component)Component.translatable(captionId);
/* 100 */     this.tooltip = tooltip;
/* 101 */     this.toString = (value -> toString.toString(this.caption, value));
/* 102 */     this.values = values;
/* 103 */     this.codec = codec;
/* 104 */     this.initialValue = initialValue;
/* 105 */     this.onValueUpdate = onValueUpdate;
/* 106 */     this.value = this.initialValue;
/*     */   }
/*     */   
/*     */   public static <T> TooltipSupplier<T> noTooltip() {
/* 110 */     return paramObject -> null;
/*     */   }
/*     */   
/*     */   public static <T> TooltipSupplier<T> cachedConstantTooltip(Component tooltipComponent) {
/* 114 */     return paramObject -> Tooltip.create(tooltipComponent);
/*     */   }
/*     */   
/*     */   public AbstractWidget createButton(Options options) {
/* 118 */     return createButton(options, 0, 0, 150);
/*     */   }
/*     */   
/*     */   public AbstractWidget createButton(Options options, int x, int y, int width) {
/* 122 */     return createButton(options, x, y, width, (ValueUpdateListener)NO_ACTION);
/*     */   }
/*     */   
/*     */   public AbstractWidget createButton(Options options, int x, int y, int width, ValueUpdateListener<? super T> onValueChanged) {
/* 126 */     return this.values.createButton(this.tooltip, options, x, y, width, onValueChanged).apply(this);
/*     */   }
/*     */   
/*     */   public T get() {
/* 130 */     return this.value;
/*     */   }
/*     */   
/*     */   public Codec<T> codec() {
/* 134 */     return this.codec;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 139 */     return this.caption.getString();
/*     */   }
/*     */   
/*     */   public void set(T value) {
/* 143 */     T newValue = this.values.validateValue(value).orElseGet(() -> {
/*     */           LOGGER.error("Illegal option value {} for {}", value, this.caption.getString());
/*     */           return (Supplier)this.initialValue;
/*     */         });
/* 147 */     if (!Minecraft.getInstance().isRunning()) {
/* 149 */       this.value = newValue;
/*     */       return;
/*     */     } 
/* 152 */     if (!Objects.equals(this.value, newValue)) {
/* 153 */       this.value = newValue;
/* 154 */       this.onValueUpdate.valueChanged(newValue);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ValueSet<T> values() {
/* 159 */     return this.values;
/*     */   }
/*     */   
/*     */   public static interface SliderableValueSet<T> extends ValueSet<T> {
/*     */     double toSliderValue(T param1T);
/*     */     
/*     */     default Optional<T> next(T current) {
/* 179 */       return Optional.empty();
/*     */     }
/*     */     
/*     */     default Optional<T> previous(T current) {
/* 183 */       return Optional.empty();
/*     */     }
/*     */     
/*     */     T fromSliderValue(double param1Double);
/*     */     
/*     */     default boolean applyValueImmediately() {
/* 189 */       return true;
/*     */     }
/*     */     
/*     */     default Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> tooltip, Options options, int x, int y, int width, OptionInstance.ValueUpdateListener<? super T> onValueChanged) {
/* 194 */       return instance -> new OptionInstance.OptionInstanceSliderButton<>(options, x, y, width, 20, instance, this, tooltip, onValueChanged, applyValueImmediately());
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface CycleableValueSet<T> extends ValueSet<T> {
/*     */     CycleButton.ValueListSupplier<T> valueListSupplier();
/*     */     
/*     */     default ValueSetter<T> valueSetter() {
/* 206 */       return OptionInstance::set;
/*     */     }
/*     */     
/*     */     default Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> tooltip, Options options, int x, int y, int width, OptionInstance.ValueUpdateListener<? super T> onValueChanged) {
/* 211 */       return instance -> {
/*     */           Objects.requireNonNull(instance);
/*     */           return (AbstractWidget)CycleButton.builder(instance.toString, instance::get).withValues(valueListSupplier()).withTooltip(tooltip).create(x, y, width, 20, instance.caption, ());
/*     */         };
/*     */     }
/*     */     
/*     */     public static interface ValueSetter<T> {
/*     */       void set(OptionInstance<T> param2OptionInstance, T param2T);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface SliderableOrCyclableValueSet<T> extends SliderableValueSet<T>, CycleableValueSet<T> {
/*     */     boolean createCycleButton();
/*     */     
/*     */     default Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> tooltip, Options options, int x, int y, int width, OptionInstance.ValueUpdateListener<? super T> onValueChanged) {
/* 227 */       if (createCycleButton())
/* 228 */         return super.createButton(tooltip, options, x, y, width, onValueChanged); 
/* 230 */       return super.createButton(tooltip, options, x, y, width, onValueChanged);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class AltEnum<T> extends Record implements CycleableValueSet<T> {
/*     */     private final List<T> values;
/*     */     
/*     */     private final List<T> altValues;
/*     */     
/*     */     private final BooleanSupplier altCondition;
/*     */     
/*     */     private final OptionInstance.CycleableValueSet.ValueSetter<T> valueSetter;
/*     */     
/*     */     private final Codec<T> codec;
/*     */     
/*     */     public AltEnum(List<T> values, List<T> altValues, BooleanSupplier altCondition, OptionInstance.CycleableValueSet.ValueSetter<T> valueSetter, Codec<T> codec) {
/* 239 */       this.values = values;
/* 239 */       this.altValues = altValues;
/* 239 */       this.altCondition = altCondition;
/* 239 */       this.valueSetter = valueSetter;
/* 239 */       this.codec = codec;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$AltEnum;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$AltEnum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$AltEnum<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$AltEnum;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$AltEnum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$AltEnum<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$AltEnum;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #239	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$AltEnum;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$AltEnum<TT;>;
/*     */     }
/*     */     
/*     */     public List<T> values() {
/* 239 */       return this.values;
/*     */     }
/*     */     
/*     */     public List<T> altValues() {
/* 239 */       return this.altValues;
/*     */     }
/*     */     
/*     */     public BooleanSupplier altCondition() {
/* 239 */       return this.altCondition;
/*     */     }
/*     */     
/*     */     public OptionInstance.CycleableValueSet.ValueSetter<T> valueSetter() {
/* 239 */       return this.valueSetter;
/*     */     }
/*     */     
/*     */     public Codec<T> codec() {
/* 239 */       return this.codec;
/*     */     }
/*     */     
/*     */     public CycleButton.ValueListSupplier<T> valueListSupplier() {
/* 242 */       return CycleButton.ValueListSupplier.create(this.altCondition, this.values, this.altValues);
/*     */     }
/*     */     
/*     */     public Optional<T> validateValue(T value) {
/* 247 */       return (this.altCondition.getAsBoolean() ? this.altValues : this.values).contains(value) ? Optional.<T>of(value) : Optional.<T>empty();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Enum<T> extends Record implements CycleableValueSet<T> {
/*     */     private final List<T> values;
/*     */     
/*     */     private final Codec<T> codec;
/*     */     
/*     */     public Enum(List<T> values, Codec<T> codec) {
/* 251 */       this.values = values;
/* 251 */       this.codec = codec;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$Enum;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #251	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$Enum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$Enum<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$Enum;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #251	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$Enum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$Enum<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$Enum;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #251	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$Enum;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$Enum<TT;>;
/*     */     }
/*     */     
/*     */     public List<T> values() {
/* 251 */       return this.values;
/*     */     }
/*     */     
/*     */     public Codec<T> codec() {
/* 251 */       return this.codec;
/*     */     }
/*     */     
/*     */     public Optional<T> validateValue(T value) {
/* 254 */       return this.values.contains(value) ? Optional.<T>of(value) : Optional.<T>empty();
/*     */     }
/*     */     
/*     */     public CycleButton.ValueListSupplier<T> valueListSupplier() {
/* 259 */       return CycleButton.ValueListSupplier.create(this.values);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class LazyEnum<T> extends Record implements CycleableValueSet<T> {
/*     */     private final Supplier<List<T>> values;
/*     */     
/*     */     private final Function<T, Optional<T>> validateValue;
/*     */     
/*     */     private final Codec<T> codec;
/*     */     
/*     */     public LazyEnum(Supplier<List<T>> values, Function<T, Optional<T>> validateValue, Codec<T> codec) {
/* 263 */       this.values = values;
/* 263 */       this.validateValue = validateValue;
/* 263 */       this.codec = codec;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$LazyEnum;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #263	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$LazyEnum;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #263	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$LazyEnum;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #263	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum<TT;>;
/*     */     }
/*     */     
/*     */     public Supplier<List<T>> values() {
/* 263 */       return this.values;
/*     */     }
/*     */     
/*     */     public Function<T, Optional<T>> validateValue() {
/* 263 */       return this.validateValue;
/*     */     }
/*     */     
/*     */     public Codec<T> codec() {
/* 263 */       return this.codec;
/*     */     }
/*     */     
/*     */     public Optional<T> validateValue(T value) {
/* 266 */       return this.validateValue.apply(value);
/*     */     }
/*     */     
/*     */     public CycleButton.ValueListSupplier<T> valueListSupplier() {
/* 271 */       return CycleButton.ValueListSupplier.create(this.values.get());
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class OptionInstanceSliderButton<N> extends AbstractOptionSliderButton implements ResettableOptionWidget {
/*     */     private final OptionInstance<N> instance;
/*     */     
/*     */     private final OptionInstance.SliderableValueSet<N> values;
/*     */     
/*     */     private final OptionInstance.TooltipSupplier<N> tooltipSupplier;
/*     */     
/*     */     private final OptionInstance.ValueUpdateListener<? super N> onValueChanged;
/*     */     
/*     */     private Long delayedApplyAt;
/*     */     
/*     */     private final boolean applyValueImmediately;
/*     */     
/*     */     private OptionInstanceSliderButton(Options options, int x, int y, int width, int height, OptionInstance<N> instance, OptionInstance.SliderableValueSet<N> values, OptionInstance.TooltipSupplier<N> tooltipSupplier, OptionInstance.ValueUpdateListener<? super N> onValueChanged, boolean applyValueImmediately) {
/* 284 */       super(options, x, y, width, height, values.toSliderValue(instance.get()));
/* 285 */       this.instance = instance;
/* 286 */       this.values = values;
/* 287 */       this.tooltipSupplier = tooltipSupplier;
/* 288 */       this.onValueChanged = onValueChanged;
/* 289 */       this.applyValueImmediately = applyValueImmediately;
/* 290 */       updateMessage();
/*     */     }
/*     */     
/*     */     protected void updateMessage() {
/* 295 */       setMessage(this.instance.toString.apply(this.values.fromSliderValue(this.value)));
/* 296 */       setTooltip(this.tooltipSupplier.apply(this.values.fromSliderValue(this.value)));
/*     */     }
/*     */     
/*     */     protected void applyValue() {
/* 301 */       if (this.applyValueImmediately) {
/* 302 */         applyUnsavedValue();
/*     */       } else {
/* 304 */         this.delayedApplyAt = Long.valueOf(Util.getMillis() + 600L);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void applyUnsavedValue() {
/* 309 */       N sliderValue = this.values.fromSliderValue(this.value);
/* 310 */       if (!Objects.equals(sliderValue, this.instance.get())) {
/* 311 */         this.instance.set(sliderValue);
/* 312 */         this.onValueChanged.valueChanged(this.instance.get());
/*     */       } 
/*     */     }
/*     */     
/*     */     public void resetValue() {
/* 318 */       if (this.value != this.values.toSliderValue(this.instance.get())) {
/* 319 */         this.value = this.values.toSliderValue(this.instance.get());
/* 320 */         this.delayedApplyAt = null;
/* 321 */         updateMessage();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
/* 327 */       super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
/* 329 */       if (this.delayedApplyAt != null && Util.getMillis() >= this.delayedApplyAt.longValue()) {
/* 330 */         this.delayedApplyAt = null;
/* 331 */         applyUnsavedValue();
/* 332 */         resetValue();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onRelease(MouseButtonEvent event) {
/* 338 */       super.onRelease(event);
/* 339 */       if (this.applyValueImmediately)
/* 340 */         resetValue(); 
/*     */     }
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/* 346 */       if (event.isSelection()) {
/* 347 */         this.canChangeValue = !this.canChangeValue;
/* 348 */         return true;
/*     */       } 
/* 350 */       if (this.canChangeValue) {
/* 351 */         boolean left = event.isLeft();
/* 352 */         boolean right = event.isRight();
/* 353 */         if (left) {
/* 354 */           Optional<N> previous = this.values.previous(this.values.fromSliderValue(this.value));
/* 355 */           if (previous.isPresent()) {
/* 356 */             setValue(this.values.toSliderValue(previous.get()));
/* 357 */             return true;
/*     */           } 
/*     */         } 
/* 360 */         if (right) {
/* 361 */           Optional<N> next = this.values.next(this.values.fromSliderValue(this.value));
/* 362 */           if (next.isPresent()) {
/* 363 */             setValue(this.values.toSliderValue(next.get()));
/* 364 */             return true;
/*     */           } 
/*     */         } 
/* 367 */         if (left || right) {
/* 368 */           float direction = left ? -1.0F : 1.0F;
/* 369 */           setValue(this.value + (direction / (this.width - 8)));
/* 370 */           return true;
/*     */         } 
/*     */       } 
/* 373 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface IntRangeBase extends SliderableValueSet<Integer> {
/*     */     default Optional<Integer> next(Integer current) {
/* 384 */       return Optional.of(Integer.valueOf(current.intValue() + 1));
/*     */     }
/*     */     
/*     */     default Optional<Integer> previous(Integer current) {
/* 389 */       return Optional.of(Integer.valueOf(current.intValue() - 1));
/*     */     }
/*     */     
/*     */     default double toSliderValue(Integer value) {
/* 396 */       if (value.intValue() == minInclusive())
/* 397 */         return 0.0D; 
/* 398 */       if (value.intValue() == maxInclusive())
/* 399 */         return 1.0D; 
/* 401 */       return Mth.map(value.intValue() + 0.5D, minInclusive(), maxInclusive() + 1.0D, 0.0D, 1.0D);
/*     */     }
/*     */     
/*     */     default Integer fromSliderValue(double slider) {
/* 406 */       if (slider >= 1.0D)
/* 407 */         slider = 0.9999899864196777D; 
/* 409 */       return Integer.valueOf(Mth.floor(Mth.map(slider, 0.0D, 1.0D, minInclusive(), maxInclusive() + 1.0D)));
/*     */     }
/*     */     
/*     */     default <R> OptionInstance.SliderableValueSet<R> xmap(final IntFunction<? extends R> to, final ToIntFunction<? super R> from, final boolean discrete) {
/* 413 */       return new OptionInstance.SliderableValueSet<R>() {
/*     */           public Optional<R> validateValue(R value) {
/* 416 */             Objects.requireNonNull(to);
/* 416 */             return OptionInstance.IntRangeBase.this.validateValue(Integer.valueOf(from.applyAsInt(value))).map(to::apply);
/*     */           }
/*     */           
/*     */           public double toSliderValue(R value) {
/* 421 */             return OptionInstance.IntRangeBase.this.toSliderValue(Integer.valueOf(from.applyAsInt(value)));
/*     */           }
/*     */           
/*     */           public Optional<R> next(R current) {
/* 426 */             if (!discrete)
/* 427 */               return Optional.empty(); 
/* 429 */             int currentIndex = from.applyAsInt(current);
/* 430 */             return Optional.of(to.apply(((Integer)OptionInstance.IntRangeBase.this.validateValue(Integer.valueOf(currentIndex + 1)).orElse(Integer.valueOf(currentIndex))).intValue()));
/*     */           }
/*     */           
/*     */           public Optional<R> previous(R current) {
/* 435 */             if (!discrete)
/* 436 */               return Optional.empty(); 
/* 438 */             int currentIndex = from.applyAsInt(current);
/* 439 */             return Optional.of(to.apply(((Integer)OptionInstance.IntRangeBase.this.validateValue(Integer.valueOf(currentIndex - 1)).orElse(Integer.valueOf(currentIndex))).intValue()));
/*     */           }
/*     */           
/*     */           public R fromSliderValue(double slider) {
/* 444 */             return to.apply(OptionInstance.IntRangeBase.this.fromSliderValue(slider).intValue());
/*     */           }
/*     */           
/*     */           public Codec<R> codec() {
/* 449 */             Objects.requireNonNull(to);
/* 449 */             Objects.requireNonNull(from);
/* 449 */             return OptionInstance.IntRangeBase.this.codec().xmap(to::apply, from::applyAsInt);
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     int minInclusive();
/*     */     
/*     */     int maxInclusive();
/*     */   }
/*     */   
/*     */   class null implements SliderableValueSet<R> {
/*     */     public Optional<R> validateValue(R value) {
/*     */       Objects.requireNonNull(to);
/*     */       return OptionInstance.IntRangeBase.this.validateValue(Integer.valueOf(from.applyAsInt(value))).map(to::apply);
/*     */     }
/*     */     
/*     */     public double toSliderValue(R value) {
/*     */       return OptionInstance.IntRangeBase.this.toSliderValue(Integer.valueOf(from.applyAsInt(value)));
/*     */     }
/*     */     
/*     */     public Optional<R> next(R current) {
/*     */       if (!discrete)
/*     */         return Optional.empty(); 
/*     */       int currentIndex = from.applyAsInt(current);
/*     */       return Optional.of(to.apply(((Integer)OptionInstance.IntRangeBase.this.validateValue(Integer.valueOf(currentIndex + 1)).orElse(Integer.valueOf(currentIndex))).intValue()));
/*     */     }
/*     */     
/*     */     public Optional<R> previous(R current) {
/*     */       if (!discrete)
/*     */         return Optional.empty(); 
/*     */       int currentIndex = from.applyAsInt(current);
/*     */       return Optional.of(to.apply(((Integer)OptionInstance.IntRangeBase.this.validateValue(Integer.valueOf(currentIndex - 1)).orElse(Integer.valueOf(currentIndex))).intValue()));
/*     */     }
/*     */     
/*     */     public R fromSliderValue(double slider) {
/*     */       return to.apply(OptionInstance.IntRangeBase.this.fromSliderValue(slider).intValue());
/*     */     }
/*     */     
/*     */     public Codec<R> codec() {
/* 449 */       Objects.requireNonNull(to);
/* 449 */       Objects.requireNonNull(from);
/* 449 */       return OptionInstance.IntRangeBase.this.codec().xmap(to::apply, from::applyAsInt);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class IntRange extends Record implements IntRangeBase {
/*     */     private final int minInclusive;
/*     */     
/*     */     private final int maxInclusive;
/*     */     
/*     */     private final boolean applyValueImmediately;
/*     */     
/*     */     public IntRange(int minInclusive, int maxInclusive, boolean applyValueImmediately) {
/* 455 */       this.minInclusive = minInclusive;
/* 455 */       this.maxInclusive = maxInclusive;
/* 455 */       this.applyValueImmediately = applyValueImmediately;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$IntRange;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #455	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$IntRange;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$IntRange;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #455	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$IntRange;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$IntRange;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #455	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$IntRange;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int minInclusive() {
/* 455 */       return this.minInclusive;
/*     */     }
/*     */     
/*     */     public int maxInclusive() {
/* 455 */       return this.maxInclusive;
/*     */     }
/*     */     
/*     */     public boolean applyValueImmediately() {
/* 455 */       return this.applyValueImmediately;
/*     */     }
/*     */     
/*     */     public IntRange(int minInclusive, int maxInclusive) {
/* 457 */       this(minInclusive, maxInclusive, true);
/*     */     }
/*     */     
/*     */     public Optional<Integer> validateValue(Integer value) {
/* 462 */       return (value.compareTo(Integer.valueOf(minInclusive())) >= 0 && value.compareTo(Integer.valueOf(maxInclusive())) <= 0) ? Optional.<Integer>of(value) : Optional.<Integer>empty();
/*     */     }
/*     */     
/*     */     public Codec<Integer> codec() {
/* 467 */       return Codec.intRange(this.minInclusive, this.maxInclusive + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ClampingLazyMaxIntRange extends Record implements IntRangeBase, SliderableOrCyclableValueSet<Integer> {
/*     */     private final int minInclusive;
/*     */     
/*     */     private final IntSupplier maxSupplier;
/*     */     
/*     */     private final int encodableMaxInclusive;
/*     */     
/*     */     public ClampingLazyMaxIntRange(int minInclusive, IntSupplier maxSupplier, int encodableMaxInclusive) {
/* 471 */       this.minInclusive = minInclusive;
/* 471 */       this.maxSupplier = maxSupplier;
/* 471 */       this.encodableMaxInclusive = encodableMaxInclusive;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #471	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #471	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #471	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int minInclusive() {
/* 471 */       return this.minInclusive;
/*     */     }
/*     */     
/*     */     public IntSupplier maxSupplier() {
/* 471 */       return this.maxSupplier;
/*     */     }
/*     */     
/*     */     public int encodableMaxInclusive() {
/* 471 */       return this.encodableMaxInclusive;
/*     */     }
/*     */     
/*     */     public Optional<Integer> validateValue(Integer value) {
/* 474 */       return Optional.of(Integer.valueOf(Mth.clamp(value.intValue(), minInclusive(), maxInclusive())));
/*     */     }
/*     */     
/*     */     public int maxInclusive() {
/* 479 */       return this.maxSupplier.getAsInt();
/*     */     }
/*     */     
/*     */     public Codec<Integer> codec() {
/* 485 */       return Codec.INT.validate(value -> {
/*     */             int maxExclusive = this.encodableMaxInclusive + 1;
/* 487 */             return (value.compareTo(Integer.valueOf(this.minInclusive)) >= 0 && value.compareTo(Integer.valueOf(maxExclusive)) <= 0) ? DataResult.success(value) : DataResult.error((), value);
/*     */           });
/*     */     }
/*     */     
/*     */     public boolean createCycleButton() {
/* 496 */       return true;
/*     */     }
/*     */     
/*     */     public CycleButton.ValueListSupplier<Integer> valueListSupplier() {
/* 501 */       return CycleButton.ValueListSupplier.create(IntStream.range(this.minInclusive, maxInclusive() + 1).boxed().toList());
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class SliderableEnum<T> extends Record implements SliderableValueSet<T> {
/*     */     private final List<T> values;
/*     */     
/*     */     private final Codec<T> codec;
/*     */     
/*     */     public SliderableEnum(List<T> values, Codec<T> codec) {
/* 505 */       this.values = values;
/* 505 */       this.codec = codec;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$SliderableEnum;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #505	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$SliderableEnum;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #505	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$SliderableEnum;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #505	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum<TT;>;
/*     */     }
/*     */     
/*     */     public List<T> values() {
/* 505 */       return this.values;
/*     */     }
/*     */     
/*     */     public Codec<T> codec() {
/* 505 */       return this.codec;
/*     */     }
/*     */     
/*     */     public double toSliderValue(T value) {
/* 510 */       if (value == this.values.getFirst())
/* 511 */         return 0.0D; 
/* 512 */       if (value == this.values.getLast())
/* 513 */         return 1.0D; 
/* 515 */       return Mth.map(this.values.indexOf(value), 0.0D, (this.values.size() - 1), 0.0D, 1.0D);
/*     */     }
/*     */     
/*     */     public Optional<T> next(T current) {
/* 520 */       int currentIntex = this.values.indexOf(current);
/* 521 */       int nextIndex = Mth.clamp(currentIntex + 1, 0, this.values.size() - 1);
/* 522 */       return Optional.of(this.values.get(nextIndex));
/*     */     }
/*     */     
/*     */     public Optional<T> previous(T current) {
/* 527 */       int currentIntex = this.values.indexOf(current);
/* 528 */       int previousIndex = Mth.clamp(currentIntex - 1, 0, this.values.size() - 1);
/* 529 */       return Optional.of(this.values.get(previousIndex));
/*     */     }
/*     */     
/*     */     public T fromSliderValue(double slider) {
/* 534 */       if (slider >= 1.0D)
/* 535 */         slider = 0.9999899864196777D; 
/* 537 */       int index = Mth.floor(Mth.map(slider, 0.0D, 1.0D, 0.0D, this.values.size()));
/* 538 */       return this.values.get(Mth.clamp(index, 0, this.values.size() - 1));
/*     */     }
/*     */     
/*     */     public Optional<T> validateValue(T value) {
/* 543 */       int index = this.values.indexOf(value);
/* 544 */       return (index > -1) ? Optional.<T>of(value) : Optional.<T>empty();
/*     */     }
/*     */   }
/*     */   
/*     */   public enum UnitDouble implements SliderableValueSet<Double> {
/* 549 */     INSTANCE;
/*     */     
/*     */     public Optional<Double> validateValue(Double value) {
/* 553 */       return (value.doubleValue() >= 0.0D && value.doubleValue() <= 1.0D) ? Optional.<Double>of(value) : Optional.<Double>empty();
/*     */     }
/*     */     
/*     */     public double toSliderValue(Double value) {
/* 558 */       return value.doubleValue();
/*     */     }
/*     */     
/*     */     public Double fromSliderValue(double slider) {
/* 563 */       return Double.valueOf(slider);
/*     */     }
/*     */     
/*     */     public <R> OptionInstance.SliderableValueSet<R> xmap(final DoubleFunction<? extends R> to, final ToDoubleFunction<? super R> from) {
/* 567 */       return new OptionInstance.SliderableValueSet<R>() {
/*     */           public Optional<R> validateValue(R value) {
/* 570 */             Objects.requireNonNull(to);
/* 570 */             return OptionInstance.UnitDouble.this.validateValue(Double.valueOf(from.applyAsDouble(value))).map(to::apply);
/*     */           }
/*     */           
/*     */           public double toSliderValue(R value) {
/* 575 */             return OptionInstance.UnitDouble.this.toSliderValue(Double.valueOf(from.applyAsDouble(value)));
/*     */           }
/*     */           
/*     */           public R fromSliderValue(double slider) {
/* 580 */             return to.apply(OptionInstance.UnitDouble.this.fromSliderValue(slider).doubleValue());
/*     */           }
/*     */           
/*     */           public Codec<R> codec() {
/* 585 */             Objects.requireNonNull(to);
/* 585 */             Objects.requireNonNull(from);
/* 585 */             return OptionInstance.UnitDouble.this.codec().xmap(to::apply, from::applyAsDouble);
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     public Codec<Double> codec() {
/* 593 */       return Codec.withAlternative(
/* 594 */           Codec.doubleRange(0.0D, 1.0D), (Codec)Codec.BOOL, b -> Double.valueOf(b.booleanValue() ? 1.0D : 0.0D));
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements SliderableValueSet<R> {
/*     */     public Optional<R> validateValue(R value) {
/*     */       Objects.requireNonNull(to);
/*     */       return OptionInstance.UnitDouble.this.validateValue(Double.valueOf(from.applyAsDouble(value))).map(to::apply);
/*     */     }
/*     */     
/*     */     public double toSliderValue(R value) {
/*     */       return OptionInstance.UnitDouble.this.toSliderValue(Double.valueOf(from.applyAsDouble(value)));
/*     */     }
/*     */     
/*     */     public R fromSliderValue(double slider) {
/*     */       return to.apply(OptionInstance.UnitDouble.this.fromSliderValue(slider).doubleValue());
/*     */     }
/*     */     
/*     */     public Codec<R> codec() {
/*     */       Objects.requireNonNull(to);
/*     */       Objects.requireNonNull(from);
/*     */       return OptionInstance.UnitDouble.this.codec().xmap(to::apply, from::applyAsDouble);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface TooltipSupplier<T> {
/*     */     Tooltip apply(T param1T);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ValueUpdateListener<T> {
/*     */     void valueChanged(T param1T);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface CaptionBasedToString<T> {
/*     */     Component toString(Component param1Component, T param1T);
/*     */   }
/*     */   
/*     */   public static interface ValueSet<T> {
/*     */     Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> param1TooltipSupplier, Options param1Options, int param1Int1, int param1Int2, int param1Int3, OptionInstance.ValueUpdateListener<? super T> param1ValueUpdateListener);
/*     */     
/*     */     Optional<T> validateValue(T param1T);
/*     */     
/*     */     Codec<T> codec();
/*     */   }
/*     */   
/*     */   public static interface ValueSetter<T> {
/*     */     void set(OptionInstance<T> param1OptionInstance, T param1T);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */