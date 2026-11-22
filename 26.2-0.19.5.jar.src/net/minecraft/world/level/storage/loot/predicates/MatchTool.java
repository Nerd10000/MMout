/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.predicates.ItemPredicate;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public final class MatchTool extends Record implements LootItemCondition {
/*    */   private final Optional<ItemPredicate> predicate;
/*    */   
/*    */   public static final MapCodec<MatchTool> MAP_CODEC;
/*    */   
/*    */   public MatchTool(Optional<ItemPredicate> predicate) {
/* 14 */     this.predicate = predicate;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/MatchTool;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Optional<ItemPredicate> predicate() {
/* 14 */     return this.predicate;
/*    */   }
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ItemPredicate.CODEC.optionalFieldOf("predicate").forGetter(MatchTool::predicate)).apply((Applicative)i, MatchTool::new));
/*    */   }
/*    */   
/*    */   public MapCodec<MatchTool> codec() {
/* 23 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 28 */     return Set.of(LootContextParams.TOOL);
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 33 */     ItemInstance tool = (ItemInstance)context.getOptionalParameter(LootContextParams.TOOL);
/* 34 */     return (tool != null && (this.predicate.isEmpty() || ((ItemPredicate)this.predicate.get()).test(tool)));
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder toolMatches(ItemPredicate.Builder predicate) {
/* 38 */     return () -> new MatchTool(Optional.of(predicate.build()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\MatchTool.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */