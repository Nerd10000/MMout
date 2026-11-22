/*     */ package net.minecraft.world.entity.ai.attributes;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public final class Packed extends Record {
/*     */   private final Holder<Attribute> attribute;
/*     */   
/*     */   private final double baseValue;
/*     */   
/*     */   private final List<AttributeModifier> modifiers;
/*     */   
/*     */   public static final Codec<Packed> CODEC;
/*     */   
/*     */   public Packed(Holder<Attribute> attribute, double baseValue, List<AttributeModifier> modifiers) {
/* 210 */     this.attribute = attribute;
/* 210 */     this.baseValue = baseValue;
/* 210 */     this.modifiers = modifiers;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #210	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #210	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #210	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/entity/ai/attributes/AttributeInstance$Packed;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Holder<Attribute> attribute() {
/* 210 */     return this.attribute;
/*     */   }
/*     */   
/*     */   public double baseValue() {
/* 210 */     return this.baseValue;
/*     */   }
/*     */   
/*     */   public List<AttributeModifier> modifiers() {
/* 210 */     return this.modifiers;
/*     */   }
/*     */   
/*     */   static {
/* 215 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("id").forGetter(Packed::attribute), (App)ExtraCodecs.optionalAlwaysPresentFieldOf((Codec)Codec.DOUBLE, "base", Double.valueOf(0.0D)).forGetter(Packed::baseValue), (App)AttributeModifier.CODEC.listOf().optionalFieldOf("modifiers", List.of()).forGetter(Packed::modifiers)).apply((Applicative)i, Packed::new));
/*     */   }
/*     */   
/* 221 */   public static final Codec<List<Packed>> LIST_CODEC = CODEC.listOf();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\attributes\AttributeInstance$Packed.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */