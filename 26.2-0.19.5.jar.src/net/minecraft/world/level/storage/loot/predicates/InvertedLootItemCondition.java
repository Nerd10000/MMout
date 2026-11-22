/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public final class InvertedLootItemCondition extends Record implements LootItemCondition {
/*    */   private final LootItemCondition term;
/*    */   
/*    */   public static final MapCodec<InvertedLootItemCondition> MAP_CODEC;
/*    */   
/*    */   public InvertedLootItemCondition(LootItemCondition term) {
/*  9 */     this.term = term;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/InvertedLootItemCondition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public LootItemCondition term() {
/*  9 */     return this.term;
/*    */   }
/*    */   
/*    */   static {
/* 10 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LootItemCondition.DIRECT_CODEC.fieldOf("term").forGetter(InvertedLootItemCondition::term)).apply((Applicative)i, InvertedLootItemCondition::new));
/*    */   }
/*    */   
/*    */   public MapCodec<InvertedLootItemCondition> codec() {
/* 16 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 21 */     return !this.term.test(context);
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext output) {
/* 26 */     super.validate(output);
/* 27 */     Validatable.validate(output, "term", (Validatable)this.term);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder invert(LootItemCondition.Builder term) {
/* 31 */     InvertedLootItemCondition result = new InvertedLootItemCondition(term.build());
/* 32 */     return () -> result;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\InvertedLootItemCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */