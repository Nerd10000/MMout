/*     */ package net.minecraft.world.attribute;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*     */ 
/*     */ public final class Entry<Value, Argument> extends Record {
/*     */   private final Argument argument;
/*     */   
/*     */   private final AttributeModifier<Value, Argument> modifier;
/*     */   
/*     */   public Entry(Argument argument, AttributeModifier<Value, Argument> modifier) {
/*  91 */     this.argument = argument;
/*  91 */     this.modifier = modifier;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #91	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry<TValue;TArgument;>;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #91	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry<TValue;TArgument;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #91	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/world/attribute/EnvironmentAttributeMap$Entry<TValue;TArgument;>;
/*     */   }
/*     */   
/*     */   public Argument argument() {
/*  91 */     return this.argument;
/*     */   }
/*     */   
/*     */   public AttributeModifier<Value, Argument> modifier() {
/*  91 */     return this.modifier;
/*     */   }
/*     */   
/*     */   private static <Value> Codec<Entry<Value, ?>> createCodec(EnvironmentAttribute<Value> attribute) {
/*  98 */     Codec<Entry<Value, ?>> fullCodec = attribute.type().modifierCodec().dispatch("modifier", Entry::modifier, Util.memoize(modifier -> createFullCodec(attribute, modifier)));
/* 100 */     return Codec.either(attribute.valueCodec(), fullCodec).xmap(either -> (Entry)either.map((), ()), entry -> (entry.modifier == AttributeModifier.override()) ? Either.left(entry.argument()) : Either.right(entry));
/*     */   }
/*     */   
/*     */   private static <Value, Argument> MapCodec<Entry<Value, Argument>> createFullCodec(EnvironmentAttribute<Value> attribute, AttributeModifier<Value, Argument> modifier) {
/* 113 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)modifier.argumentCodec(attribute).fieldOf("argument").forGetter(Entry::argument)).apply((Applicative)i, ()));
/*     */   }
/*     */   
/*     */   public Value applyModifier(Value subject) {
/* 119 */     return (Value)this.modifier.apply(subject, this.argument);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\attribute\EnvironmentAttributeMap$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */