/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.clock.WorldClock;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ 
/*    */ public class Builder implements LootItemCondition.Builder {
/*    */   private final Holder<WorldClock> clock;
/*    */   
/* 55 */   private Optional<Long> period = Optional.empty();
/*    */   
/*    */   private final IntRange value;
/*    */   
/*    */   public Builder(Holder<WorldClock> clock, IntRange value) {
/* 59 */     this.clock = clock;
/* 60 */     this.value = value;
/*    */   }
/*    */   
/*    */   public Builder setPeriod(long period) {
/* 64 */     this.period = Optional.of(Long.valueOf(period));
/* 65 */     return this;
/*    */   }
/*    */   
/*    */   public TimeCheck build() {
/* 70 */     return new TimeCheck(this.clock, this.period, this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\TimeCheck$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */