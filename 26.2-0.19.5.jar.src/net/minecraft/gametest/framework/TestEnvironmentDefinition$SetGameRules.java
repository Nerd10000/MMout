/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.level.gamerules.GameRule;
/*     */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public final class SetGameRules extends Record implements TestEnvironmentDefinition<GameRuleMap> {
/*     */   private final GameRuleMap gameRulesMap;
/*     */   
/*     */   public static final MapCodec<SetGameRules> CODEC;
/*     */   
/*     */   public SetGameRules(GameRuleMap gameRulesMap) {
/* 169 */     this.gameRulesMap = gameRulesMap;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #169	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #169	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #169	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/gametest/framework/TestEnvironmentDefinition$SetGameRules;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public GameRuleMap gameRulesMap() {
/* 169 */     return this.gameRulesMap;
/*     */   }
/*     */   
/*     */   static {
/* 170 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)GameRuleMap.CODEC.fieldOf("rules").forGetter(SetGameRules::gameRulesMap)).apply((Applicative)i, SetGameRules::new));
/*     */   }
/*     */   
/*     */   public GameRuleMap setup(ServerLevel level) {
/* 176 */     GameRuleMap originalState = GameRuleMap.of();
/* 177 */     GameRules gameRules = level.getGameRules();
/* 178 */     this.gameRulesMap.keySet().forEach(rule -> setFromActive(originalState, rule, gameRules));
/* 179 */     gameRules.setAll(this.gameRulesMap, level.getServer());
/* 181 */     return originalState;
/*     */   }
/*     */   
/*     */   private static <T> void setFromActive(GameRuleMap map, GameRule<T> rule, GameRules rules) {
/* 185 */     map.set(rule, rules.get(rule));
/*     */   }
/*     */   
/*     */   public void teardown(ServerLevel level, GameRuleMap saveData) {
/* 190 */     level.getGameRules().setAll(saveData, level.getServer());
/*     */   }
/*     */   
/*     */   public MapCodec<SetGameRules> codec() {
/* 195 */     return CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\TestEnvironmentDefinition$SetGameRules.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */