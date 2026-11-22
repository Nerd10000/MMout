/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.clock.WorldClock;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ 
/*    */ public final class TimeCheck extends Record implements LootItemCondition {
/*    */   private final Holder<WorldClock> clock;
/*    */   
/*    */   private final Optional<Long> period;
/*    */   
/*    */   private final IntRange value;
/*    */   
/*    */   public static final MapCodec<TimeCheck> MAP_CODEC;
/*    */   
/*    */   public TimeCheck(Holder<WorldClock> clock, Optional<Long> period, IntRange value) {
/* 16 */     this.clock = clock;
/* 16 */     this.period = period;
/* 16 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/TimeCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Holder<WorldClock> clock() {
/* 16 */     return this.clock;
/*    */   }
/*    */   
/*    */   public Optional<Long> period() {
/* 16 */     return this.period;
/*    */   }
/*    */   
/*    */   public IntRange value() {
/* 16 */     return this.value;
/*    */   }
/*    */   
/*    */   static {
/* 22 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)WorldClock.CODEC.fieldOf("clock").forGetter(TimeCheck::clock), (App)Codec.LONG.optionalFieldOf("period").forGetter(TimeCheck::period), (App)IntRange.CODEC.fieldOf("value").forGetter(TimeCheck::value)).apply((Applicative)i, TimeCheck::new));
/*    */   }
/*    */   
/*    */   public MapCodec<TimeCheck> codec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 35 */     super.validate(context);
/* 37 */     Validatable.validate(context, "value", (Validatable)this.value);
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 42 */     ServerLevel level = context.getLevel();
/* 44 */     long time = level.clockManager().getTotalTicks(this.clock);
/* 46 */     if (this.period.isPresent())
/* 47 */       time %= ((Long)this.period.get()).longValue(); 
/* 50 */     return this.value.test(context, (int)time);
/*    */   }
/*    */   
/*    */   public static class Builder implements LootItemCondition.Builder {
/*    */     private final Holder<WorldClock> clock;
/*    */     
/* 55 */     private Optional<Long> period = Optional.empty();
/*    */     
/*    */     private final IntRange value;
/*    */     
/*    */     public Builder(Holder<WorldClock> clock, IntRange value) {
/* 59 */       this.clock = clock;
/* 60 */       this.value = value;
/*    */     }
/*    */     
/*    */     public Builder setPeriod(long period) {
/* 64 */       this.period = Optional.of(Long.valueOf(period));
/* 65 */       return this;
/*    */     }
/*    */     
/*    */     public TimeCheck build() {
/* 70 */       return new TimeCheck(this.clock, this.period, this.value);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder time(Holder<WorldClock> clock, IntRange value) {
/* 75 */     return new Builder(clock, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\TimeCheck.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */