/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public class Builder implements LootItemCondition.Builder {
/* 77 */   private final ImmutableMap.Builder<String, IntRange> scores = ImmutableMap.builder();
/*    */   
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   
/*    */   public Builder(LootContext.EntityTarget entityTarget) {
/* 81 */     this.entityTarget = entityTarget;
/*    */   }
/*    */   
/*    */   public Builder withScore(String score, IntRange bounds) {
/* 85 */     this.scores.put(score, bounds);
/* 86 */     return this;
/*    */   }
/*    */   
/*    */   public LootItemCondition build() {
/* 91 */     return new EntityHasScoreCondition((Map<String, IntRange>)this.scores.build(), this.entityTarget);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\EntityHasScoreCondition$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */