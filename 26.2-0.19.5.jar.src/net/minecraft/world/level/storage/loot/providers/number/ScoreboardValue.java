/*    */ package net.minecraft.world.level.storage.loot.providers.number;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.server.ServerScoreboard;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.providers.score.ContextScoreboardNameProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.score.ScoreboardNameProviders;
/*    */ import net.minecraft.world.scores.Objective;
/*    */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*    */ import net.minecraft.world.scores.ScoreHolder;
/*    */ 
/*    */ public final class ScoreboardValue extends Record implements NumberProvider {
/*    */   private final ScoreboardNameProvider target;
/*    */   
/*    */   private final String score;
/*    */   
/*    */   private final float scale;
/*    */   
/*    */   public static final MapCodec<ScoreboardValue> MAP_CODEC;
/*    */   
/*    */   public ScoreboardValue(ScoreboardNameProvider target, String score, float scale) {
/* 17 */     this.target = target;
/* 17 */     this.score = score;
/* 17 */     this.scale = scale;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/number/ScoreboardValue;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/ScoreboardValue;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/number/ScoreboardValue;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/ScoreboardValue;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/number/ScoreboardValue;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/number/ScoreboardValue;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ScoreboardNameProvider target() {
/* 17 */     return this.target;
/*    */   }
/*    */   
/*    */   public String score() {
/* 17 */     return this.score;
/*    */   }
/*    */   
/*    */   public float scale() {
/* 17 */     return this.scale;
/*    */   }
/*    */   
/*    */   static {
/* 22 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ScoreboardNameProviders.CODEC.fieldOf("target").forGetter(ScoreboardValue::target), (App)Codec.STRING.fieldOf("score").forGetter(ScoreboardValue::score), (App)Codec.FLOAT.optionalFieldOf("scale", Float.valueOf(1.0F)).forGetter(ScoreboardValue::scale)).apply((Applicative)i, ScoreboardValue::new));
/*    */   }
/*    */   
/*    */   public MapCodec<ScoreboardValue> codec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 35 */     super.validate(context);
/* 37 */     Validatable.validate(context, "target", (Validatable)this.target);
/*    */   }
/*    */   
/*    */   public static ScoreboardValue fromScoreboard(LootContext.EntityTarget entityTarget, String score) {
/* 41 */     return fromScoreboard(entityTarget, score, 1.0F);
/*    */   }
/*    */   
/*    */   public static ScoreboardValue fromScoreboard(LootContext.EntityTarget entityTarget, String score, float scale) {
/* 45 */     return new ScoreboardValue(ContextScoreboardNameProvider.forTarget(entityTarget), score, scale);
/*    */   }
/*    */   
/*    */   public float getFloat(LootContext context) {
/* 50 */     ScoreHolder scoreHolder = this.target.getScoreHolder(context);
/* 51 */     if (scoreHolder == null)
/* 52 */       return 0.0F; 
/* 55 */     ServerScoreboard serverScoreboard = context.getLevel().getScoreboard();
/* 56 */     Objective objective = serverScoreboard.getObjective(this.score);
/* 57 */     if (objective == null)
/* 58 */       return 0.0F; 
/* 61 */     ReadOnlyScoreInfo scoreInfo = serverScoreboard.getPlayerScoreInfo(scoreHolder, objective);
/* 62 */     if (scoreInfo == null)
/* 63 */       return 0.0F; 
/* 65 */     return scoreInfo.value() * this.scale;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\number\ScoreboardValue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */