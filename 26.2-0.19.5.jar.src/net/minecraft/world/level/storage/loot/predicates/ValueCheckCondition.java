/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public final class ValueCheckCondition extends Record implements LootItemCondition {
/*    */   private final NumberProvider value;
/*    */   
/*    */   private final IntRange range;
/*    */   
/*    */   public static final MapCodec<ValueCheckCondition> MAP_CODEC;
/*    */   
/*    */   public ValueCheckCondition(NumberProvider value, IntRange range) {
/* 12 */     this.value = value;
/* 12 */     this.range = range;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/ValueCheckCondition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public NumberProvider value() {
/* 12 */     return this.value;
/*    */   }
/*    */   
/*    */   public IntRange range() {
/* 12 */     return this.range;
/*    */   }
/*    */   
/*    */   static {
/* 16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)NumberProviders.CODEC.fieldOf("value").forGetter(ValueCheckCondition::value), (App)IntRange.CODEC.fieldOf("range").forGetter(ValueCheckCondition::range)).apply((Applicative)i, ValueCheckCondition::new));
/*    */   }
/*    */   
/*    */   public MapCodec<ValueCheckCondition> codec() {
/* 23 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 28 */     super.validate(context);
/* 30 */     Validatable.validate(context, "value", (Validatable)this.value);
/* 31 */     Validatable.validate(context, "range", (Validatable)this.range);
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 36 */     return this.range.test(context, this.value.getInt(context));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder hasValue(NumberProvider value, IntRange range) {
/* 40 */     return () -> new ValueCheckCondition(value, range);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\ValueCheckCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */