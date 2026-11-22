/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.server.ServerScoreboard;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*    */ import net.minecraft.world.scores.ScoreHolder;
/*    */ import net.minecraft.world.scores.Scoreboard;
/*    */ 
/*    */ public final class EntityHasScoreCondition extends Record implements LootItemCondition {
/*    */   private final Map<String, IntRange> scores;
/*    */   
/*    */   private final LootContext.EntityTarget entityTarget;
/*    */   
/*    */   public static final MapCodec<EntityHasScoreCondition> MAP_CODEC;
/*    */   
/*    */   public EntityHasScoreCondition(Map<String, IntRange> scores, LootContext.EntityTarget entityTarget) {
/* 19 */     this.scores = scores;
/* 19 */     this.entityTarget = entityTarget;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #19	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/EntityHasScoreCondition;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Map<String, IntRange> scores() {
/* 19 */     return this.scores;
/*    */   }
/*    */   
/*    */   public LootContext.EntityTarget entityTarget() {
/* 19 */     return this.entityTarget;
/*    */   }
/*    */   
/*    */   static {
/* 23 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.unboundedMap((Codec)Codec.STRING, IntRange.CODEC).fieldOf("scores").forGetter(EntityHasScoreCondition::scores), (App)LootContext.EntityTarget.CODEC.fieldOf("entity").forGetter(EntityHasScoreCondition::entityTarget)).apply((Applicative)i, EntityHasScoreCondition::new));
/*    */   }
/*    */   
/*    */   public MapCodec<EntityHasScoreCondition> codec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 35 */     return Set.of(this.entityTarget.contextParam());
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 40 */     super.validate(context);
/* 42 */     this.scores.forEach((score, value) -> value.validate(context.forMapField("scores", score)));
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 49 */     Entity entity = (Entity)context.getOptionalParameter(this.entityTarget.contextParam());
/* 51 */     if (entity == null)
/* 52 */       return false; 
/* 55 */     ServerScoreboard serverScoreboard = context.getLevel().getScoreboard();
/* 56 */     for (Map.Entry<String, IntRange> entry : this.scores.entrySet()) {
/* 57 */       if (!hasScore(context, entity, (Scoreboard)serverScoreboard, entry.getKey(), entry.getValue()))
/* 58 */         return false; 
/*    */     } 
/* 61 */     return true;
/*    */   }
/*    */   
/*    */   private boolean hasScore(LootContext context, Entity entity, Scoreboard scoreboard, String objectiveName, IntRange range) {
/* 65 */     Objective objective = scoreboard.getObjective(objectiveName);
/* 66 */     if (objective == null)
/* 67 */       return false; 
/* 69 */     ReadOnlyScoreInfo scoreInfo = scoreboard.getPlayerScoreInfo((ScoreHolder)entity, objective);
/* 70 */     if (scoreInfo == null)
/* 71 */       return false; 
/* 73 */     return range.test(context, scoreInfo.value());
/*    */   }
/*    */   
/*    */   public static class Builder implements LootItemCondition.Builder {
/* 77 */     private final ImmutableMap.Builder<String, IntRange> scores = ImmutableMap.builder();
/*    */     
/*    */     private final LootContext.EntityTarget entityTarget;
/*    */     
/*    */     public Builder(LootContext.EntityTarget entityTarget) {
/* 81 */       this.entityTarget = entityTarget;
/*    */     }
/*    */     
/*    */     public Builder withScore(String score, IntRange bounds) {
/* 85 */       this.scores.put(score, bounds);
/* 86 */       return this;
/*    */     }
/*    */     
/*    */     public LootItemCondition build() {
/* 91 */       return new EntityHasScoreCondition((Map<String, IntRange>)this.scores.build(), this.entityTarget);
/*    */     }
/*    */   }
/*    */   
/*    */   public static Builder hasScores(LootContext.EntityTarget target) {
/* 96 */     return new Builder(target);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\EntityHasScoreCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */