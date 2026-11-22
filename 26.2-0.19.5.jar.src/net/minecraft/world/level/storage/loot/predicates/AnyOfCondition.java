/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class AnyOfCondition extends CompositeLootItemCondition {
/*  9 */   public static final MapCodec<AnyOfCondition> MAP_CODEC = createCodec(AnyOfCondition::new);
/*    */   
/*    */   private AnyOfCondition(List<LootItemCondition> terms) {
/* 12 */     super(terms, Util.anyOf(terms));
/*    */   }
/*    */   
/*    */   public MapCodec<AnyOfCondition> codec() {
/* 17 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public static class Builder extends CompositeLootItemCondition.Builder {
/*    */     public Builder(LootItemCondition.Builder... terms) {
/* 22 */       super(terms);
/*    */     }
/*    */     
/*    */     public Builder or(LootItemCondition.Builder term) {
/* 27 */       addTerm(term);
/* 28 */       return this;
/*    */     }
/*    */     
/*    */     protected LootItemCondition create(List<LootItemCondition> terms) {
/* 33 */       return new AnyOfCondition(terms);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder anyOf(LootItemCondition.Builder... terms) {
/* 38 */     return new Builder(terms);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\AnyOfCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */