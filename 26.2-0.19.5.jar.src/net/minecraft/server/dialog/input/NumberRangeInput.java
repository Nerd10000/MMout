/*     */ package net.minecraft.server.dialog.input;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public final class NumberRangeInput extends Record implements InputControl {
/*     */   private final int width;
/*     */   
/*     */   private final Component label;
/*     */   
/*     */   private final String labelFormat;
/*     */   
/*     */   private final RangeInfo rangeInfo;
/*     */   
/*     */   public static final MapCodec<NumberRangeInput> MAP_CODEC;
/*     */   
/*     */   public NumberRangeInput(int width, Component label, String labelFormat, RangeInfo rangeInfo) {
/*  15 */     this.width = width;
/*  15 */     this.label = label;
/*  15 */     this.labelFormat = labelFormat;
/*  15 */     this.rangeInfo = rangeInfo;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/NumberRangeInput;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/NumberRangeInput;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/NumberRangeInput;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #15	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int width() {
/*  15 */     return this.width;
/*     */   }
/*     */   
/*     */   public Component label() {
/*  15 */     return this.label;
/*     */   }
/*     */   
/*     */   public String labelFormat() {
/*  15 */     return this.labelFormat;
/*     */   }
/*     */   
/*     */   public RangeInfo rangeInfo() {
/*  15 */     return this.rangeInfo;
/*     */   }
/*     */   
/*     */   static {
/*  22 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Dialog.WIDTH_CODEC.optionalFieldOf("width", Integer.valueOf(200)).forGetter(NumberRangeInput::width), (App)ComponentSerialization.CODEC.fieldOf("label").forGetter(NumberRangeInput::label), (App)Codec.STRING.optionalFieldOf("label_format", "options.generic_value").forGetter(NumberRangeInput::labelFormat), (App)RangeInfo.MAP_CODEC.forGetter(NumberRangeInput::rangeInfo)).apply((Applicative)i, NumberRangeInput::new));
/*     */   }
/*     */   
/*     */   public MapCodec<NumberRangeInput> mapCodec() {
/*  31 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   public Component computeLabel(String value) {
/*  35 */     return (Component)Component.translatable(this.labelFormat, new Object[] { this.label, value });
/*     */   }
/*     */   
/*     */   public static final class RangeInfo extends Record {
/*     */     private final float start;
/*     */     
/*     */     private final float end;
/*     */     
/*     */     private final Optional<Float> initial;
/*     */     
/*     */     private final Optional<Float> step;
/*     */     
/*     */     public static final MapCodec<RangeInfo> MAP_CODEC;
/*     */     
/*     */     public RangeInfo(float start, float end, Optional<Float> initial, Optional<Float> step) {
/*  39 */       this.start = start;
/*  39 */       this.end = end;
/*  39 */       this.initial = initial;
/*  39 */       this.step = step;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #39	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public float start() {
/*  39 */       return this.start;
/*     */     }
/*     */     
/*     */     public float end() {
/*  39 */       return this.end;
/*     */     }
/*     */     
/*     */     public Optional<Float> initial() {
/*  39 */       return this.initial;
/*     */     }
/*     */     
/*     */     public Optional<Float> step() {
/*  39 */       return this.step;
/*     */     }
/*     */     
/*     */     static {
/*  52 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("start").forGetter(RangeInfo::start), (App)Codec.FLOAT.fieldOf("end").forGetter(RangeInfo::end), (App)Codec.FLOAT.optionalFieldOf("initial").forGetter(RangeInfo::initial), (App)ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("step").forGetter(RangeInfo::step)).apply((Applicative)i, RangeInfo::new)).validate(range -> {
/*     */             if (range.initial.isPresent()) {
/*     */               double initial = ((Float)range.initial.get()).floatValue();
/*     */               double min = Math.min(range.start, range.end);
/*     */               double max = Math.max(range.start, range.end);
/*     */               if (initial < min || initial > max)
/*     */                 return DataResult.error(()); 
/*     */             } 
/*     */             return DataResult.success(range);
/*     */           });
/*     */     }
/*     */     
/*     */     public float computeScaledValue(float sliderValue) {
/*  66 */       float valueInRange = Mth.lerp(sliderValue, this.start, this.end);
/*  68 */       if (this.step.isEmpty())
/*  70 */         return valueInRange; 
/*  73 */       float step = ((Float)this.step.get()).floatValue();
/*  74 */       float initialValue = initialScaledValue();
/*  75 */       float deltaToInitial = valueInRange - initialValue;
/*  77 */       int stepsOutsideInitial = Math.round(deltaToInitial / step);
/*  78 */       float result = initialValue + stepsOutsideInitial * step;
/*  79 */       if (!isOutOfRange(result))
/*  80 */         return result; 
/*  83 */       int oneStepLess = stepsOutsideInitial - Mth.sign(stepsOutsideInitial);
/*  84 */       return initialValue + oneStepLess * step;
/*     */     }
/*     */     
/*     */     private boolean isOutOfRange(float scaledValue) {
/*  88 */       float sliderPos = scaledValueToSlider(scaledValue);
/*  89 */       return (sliderPos < 0.0D || sliderPos > 1.0D);
/*     */     }
/*     */     
/*     */     private float initialScaledValue() {
/*  93 */       if (this.initial.isPresent())
/*  94 */         return ((Float)this.initial.get()).floatValue(); 
/*  97 */       return (this.start + this.end) / 2.0F;
/*     */     }
/*     */     
/*     */     public float initialSliderValue() {
/* 101 */       float value = initialScaledValue();
/* 102 */       return scaledValueToSlider(value);
/*     */     }
/*     */     
/*     */     private float scaledValueToSlider(float value) {
/* 106 */       if (this.start == this.end)
/* 107 */         return 0.5F; 
/* 109 */       return Mth.inverseLerp(value, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dialog\input\NumberRangeInput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */