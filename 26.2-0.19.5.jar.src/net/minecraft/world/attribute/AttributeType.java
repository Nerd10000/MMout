/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import com.google.common.collect.ImmutableBiMap;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.ToFloatFunction;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ 
/*     */ public final class AttributeType<Value> extends Record {
/*     */   private final Codec<Value> valueCodec;
/*     */   
/*     */   private final Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLibrary;
/*     */   
/*     */   private final Codec<AttributeModifier<Value, ?>> modifierCodec;
/*     */   
/*     */   private final LerpFunction<Value> keyframeLerp;
/*     */   
/*     */   private final LerpFunction<Value> stateChangeLerp;
/*     */   
/*     */   private final LerpFunction<Value> spatialLerp;
/*     */   
/*     */   private final LerpFunction<Value> partialTickLerp;
/*     */   
/*     */   private final ToFloatFunction<Value> toFloat;
/*     */   
/*     */   public AttributeType(Codec<Value> valueCodec, Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLibrary, Codec<AttributeModifier<Value, ?>> modifierCodec, LerpFunction<Value> keyframeLerp, LerpFunction<Value> stateChangeLerp, LerpFunction<Value> spatialLerp, LerpFunction<Value> partialTickLerp, ToFloatFunction<Value> toFloat) {
/*  14 */     this.valueCodec = valueCodec;
/*  14 */     this.modifierLibrary = modifierLibrary;
/*  14 */     this.modifierCodec = modifierCodec;
/*  14 */     this.keyframeLerp = keyframeLerp;
/*  14 */     this.stateChangeLerp = stateChangeLerp;
/*  14 */     this.spatialLerp = spatialLerp;
/*  14 */     this.partialTickLerp = partialTickLerp;
/*  14 */     this.toFloat = toFloat;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/AttributeType;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #14	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/attribute/AttributeType;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/attribute/AttributeType<TValue;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/AttributeType;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #14	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/attribute/AttributeType;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/world/attribute/AttributeType<TValue;>;
/*     */   }
/*     */   
/*     */   public Codec<Value> valueCodec() {
/*  14 */     return this.valueCodec;
/*     */   }
/*     */   
/*     */   public Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLibrary() {
/*  14 */     return this.modifierLibrary;
/*     */   }
/*     */   
/*     */   public Codec<AttributeModifier<Value, ?>> modifierCodec() {
/*  14 */     return this.modifierCodec;
/*     */   }
/*     */   
/*     */   public LerpFunction<Value> keyframeLerp() {
/*  14 */     return this.keyframeLerp;
/*     */   }
/*     */   
/*     */   public LerpFunction<Value> stateChangeLerp() {
/*  14 */     return this.stateChangeLerp;
/*     */   }
/*     */   
/*     */   public LerpFunction<Value> spatialLerp() {
/*  14 */     return this.spatialLerp;
/*     */   }
/*     */   
/*     */   public LerpFunction<Value> partialTickLerp() {
/*  14 */     return this.partialTickLerp;
/*     */   }
/*     */   
/*     */   public ToFloatFunction<Value> toFloat() {
/*  14 */     return this.toFloat;
/*     */   }
/*     */   
/*     */   public static <Value> AttributeType<Value> ofInterpolated(Codec<Value> valueCodec, Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLibrary, LerpFunction<Value> lerp) {
/*  31 */     return ofInterpolated(valueCodec, modifierLibrary, lerp, lerp, null);
/*     */   }
/*     */   
/*     */   public static <Value> AttributeType<Value> ofInterpolated(Codec<Value> valueCodec, Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLibrary, LerpFunction<Value> lerp, LerpFunction<Value> partialTickLerp, ToFloatFunction<Value> toFloat) {
/*  41 */     return new AttributeType<>(valueCodec, modifierLibrary, 
/*     */         
/*  44 */         createModifierCodec(modifierLibrary), lerp, lerp, lerp, partialTickLerp, toFloat);
/*     */   }
/*     */   
/*     */   public static <Value> AttributeType<Value> ofNotInterpolated(Codec<Value> valueCodec, Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLibrary) {
/*  57 */     return new AttributeType<>(valueCodec, modifierLibrary, 
/*     */         
/*  60 */         createModifierCodec(modifierLibrary), 
/*     */         
/*  62 */         LerpFunction.ofStep(1.0F), 
/*     */         
/*  64 */         LerpFunction.ofStep(0.0F), 
/*     */         
/*  66 */         LerpFunction.ofStep(0.5F), 
/*     */         
/*  68 */         LerpFunction.ofStep(0.0F), null);
/*     */   }
/*     */   
/*     */   public static <Value> AttributeType<Value> ofNotInterpolated(Codec<Value> valueCodec) {
/*  74 */     return ofNotInterpolated(valueCodec, Map.of());
/*     */   }
/*     */   
/*     */   private static <Value> Codec<AttributeModifier<Value, ?>> createModifierCodec(Map<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifiers) {
/*  81 */     ImmutableBiMap<AttributeModifier.OperationId, AttributeModifier<Value, ?>> modifierLookup = ImmutableBiMap.builder().put(AttributeModifier.OperationId.OVERRIDE, AttributeModifier.override()).putAll(modifiers).buildOrThrow();
/*  85 */     Objects.requireNonNull(modifierLookup);
/*  86 */     Objects.requireNonNull(modifierLookup.inverse());
/*  86 */     return ExtraCodecs.idResolverCodec(AttributeModifier.OperationId.CODEC, modifierLookup::get, modifierLookup.inverse()::get);
/*     */   }
/*     */   
/*     */   public void checkAllowedModifier(AttributeModifier<Value, ?> modifier) {
/*  91 */     if (modifier != AttributeModifier.override() && !this.modifierLibrary.containsValue(modifier))
/*  92 */       throw new IllegalArgumentException("Modifier " + String.valueOf(modifier) + " is not valid for " + String.valueOf(this)); 
/*     */   }
/*     */   
/*     */   public float toFloat(Value value) {
/*  97 */     if (this.toFloat == null)
/*  98 */       throw new IllegalStateException(String.valueOf(value) + " cannot be represented as a float"); 
/* 100 */     return this.toFloat.applyAsFloat(value);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 105 */     return Util.getRegisteredName(BuiltInRegistries.ATTRIBUTE_TYPE, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\AttributeType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */