/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.attribute.EnvironmentAttribute;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public final class EnvironmentAttributeCheck<Value> extends Record implements LootItemCondition {
/*    */   private final EnvironmentAttribute<Value> attribute;
/*    */   
/*    */   private final Value value;
/*    */   
/*    */   public EnvironmentAttributeCheck(EnvironmentAttribute<Value> attribute, Value value) {
/* 12 */     this.attribute = attribute;
/* 12 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/EnvironmentAttributeCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnvironmentAttributeCheck;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnvironmentAttributeCheck<TValue;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/EnvironmentAttributeCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnvironmentAttributeCheck;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnvironmentAttributeCheck<TValue;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/EnvironmentAttributeCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnvironmentAttributeCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EnvironmentAttributeCheck<TValue;>;
/*    */   }
/*    */   
/*    */   public EnvironmentAttribute<Value> attribute() {
/* 12 */     return this.attribute;
/*    */   }
/*    */   
/*    */   public Value value() {
/* 12 */     return this.value;
/*    */   }
/*    */   
/* 16 */   public static final MapCodec<EnvironmentAttributeCheck<?>> MAP_CODEC = EnvironmentAttributes.CODEC.dispatchMap("attribute", EnvironmentAttributeCheck::attribute, EnvironmentAttributeCheck::createCodec);
/*    */   
/*    */   private static <Value> MapCodec<EnvironmentAttributeCheck<Value>> createCodec(EnvironmentAttribute<Value> attribute) {
/* 23 */     return attribute.valueCodec().fieldOf("value").xmap(value -> new EnvironmentAttributeCheck(attribute, value), EnvironmentAttributeCheck::value);
/*    */   }
/*    */   
/*    */   public MapCodec<EnvironmentAttributeCheck<Value>> codec() {
/* 33 */     return (MapCodec)MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 38 */     return this.attribute.isPositional() ? Set.<ContextKey<?>>of(LootContextParams.ORIGIN) : Set.<ContextKey<?>>of();
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 43 */     Value actualValue = (Value)context.getLevel().environmentAttributes().getValue(context, this.attribute);
/* 44 */     return this.value.equals(actualValue);
/*    */   }
/*    */   
/*    */   public static <Value> LootItemCondition.Builder environmentAttribute(EnvironmentAttribute<Value> attribute, Value value) {
/* 48 */     return () -> new EnvironmentAttributeCheck(attribute, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\EnvironmentAttributeCheck.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */