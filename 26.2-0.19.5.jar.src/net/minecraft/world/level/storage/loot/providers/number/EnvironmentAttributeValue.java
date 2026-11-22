/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public final class EnvironmentAttributeValue extends Record implements NumberProvider {
/*    */   private final EnvironmentAttribute<?> attribute;
/*    */   
/*    */   private static final Codec<EnvironmentAttribute<?>> ATTRIBUTE_CODEC;
/*    */   
/*    */   public static final MapCodec<EnvironmentAttributeValue> MAP_CODEC;
/*    */   
/*    */   public EnvironmentAttributeValue(EnvironmentAttribute<?> attribute) {
/* 15 */     this.attribute = attribute;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/EnvironmentAttributeValue;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/EnvironmentAttributeValue;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/EnvironmentAttributeValue;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/EnvironmentAttributeValue;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/EnvironmentAttributeValue;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/EnvironmentAttributeValue;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public EnvironmentAttribute<?> attribute() {
/* 15 */     return this.attribute;
/*    */   }
/*    */   
/*    */   static {
/* 18 */     ATTRIBUTE_CODEC = EnvironmentAttributes.CODEC.validate(attribute -> (attribute.type().toFloat() == null) ? DataResult.error(()) : DataResult.success(attribute));
/* 24 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ATTRIBUTE_CODEC.fieldOf("attribute").forGetter(EnvironmentAttributeValue::attribute)).apply((Applicative)i, EnvironmentAttributeValue::new));
/*    */   }
/*    */   
/*    */   public MapCodec<EnvironmentAttributeValue> codec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public float getFloat(LootContext context) {
/* 35 */     return getAsFloat(context, this.attribute);
/*    */   }
/*    */   
/*    */   private static <Value> float getAsFloat(LootContext context, EnvironmentAttribute<Value> attribute) {
/* 39 */     Value value = (Value)context.getLevel().environmentAttributes().getValue(context, attribute);
/* 40 */     return attribute.type().toFloat(value);
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 45 */     return this.attribute.isPositional() ? Set.<ContextKey<?>>of(LootContextParams.ORIGIN) : Set.<ContextKey<?>>of();
/*    */   }
/*    */   
/*    */   public static EnvironmentAttributeValue forEnvironmentAttribute(EnvironmentAttribute<?> attribute) {
/* 49 */     return new EnvironmentAttributeValue(attribute);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\number\EnvironmentAttributeValue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */