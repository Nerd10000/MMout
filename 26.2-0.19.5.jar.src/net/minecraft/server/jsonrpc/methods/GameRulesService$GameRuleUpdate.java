/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.gamerules.GameRule;
/*    */ import net.minecraft.world.level.gamerules.GameRuleType;
/*    */ 
/*    */ public final class GameRuleUpdate<T> extends Record {
/*    */   private final GameRule<T> gameRule;
/*    */   
/*    */   private final T value;
/*    */   
/*    */   public GameRuleUpdate(GameRule<T> gameRule, T value) {
/* 16 */     this.gameRule = gameRule;
/* 16 */     this.value = value;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate<TT;>;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/GameRulesService$GameRuleUpdate<TT;>;
/*    */   }
/*    */   
/*    */   public GameRule<T> gameRule() {
/* 16 */     return this.gameRule;
/*    */   }
/*    */   
/*    */   public T value() {
/* 16 */     return this.value;
/*    */   }
/*    */   
/* 17 */   public static final Codec<GameRuleUpdate<?>> TYPED_CODEC = BuiltInRegistries.GAME_RULE.byNameCodec().dispatch("key", GameRuleUpdate::gameRule, GameRuleUpdate::getValueAndTypeCodec);
/*    */   
/* 18 */   public static final Codec<GameRuleUpdate<?>> CODEC = BuiltInRegistries.GAME_RULE.byNameCodec().dispatch("key", GameRuleUpdate::gameRule, GameRuleUpdate::getValueCodec);
/*    */   
/*    */   private static <T> MapCodec<? extends GameRuleUpdate<T>> getValueCodec(GameRule<T> gameRule) {
/* 21 */     return gameRule.valueCodec().fieldOf("value")
/* 22 */       .xmap(value -> new GameRuleUpdate(gameRule, value), GameRuleUpdate::value);
/*    */   }
/*    */   
/*    */   private static <T> MapCodec<? extends GameRuleUpdate<T>> getValueAndTypeCodec(GameRule<T> gameRule) {
/* 26 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)StringRepresentable.fromEnum(GameRuleType::values).fieldOf("type").forGetter(()), (App)gameRule.valueCodec().fieldOf("value").forGetter(GameRuleUpdate::value)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   private static <T> GameRuleUpdate<T> getUntypedRule(GameRule<T> gameRule, GameRuleType readType, T value) {
/* 33 */     if (gameRule.gameRuleType() != readType)
/* 34 */       throw new InvalidParameterJsonRpcException("Stated type \"" + String.valueOf(readType) + "\" mismatches with actual type \"" + String.valueOf(gameRule.gameRuleType()) + "\" of gamerule \"" + gameRule.id() + "\""); 
/* 36 */     return new GameRuleUpdate<>(gameRule, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\methods\GameRulesService$GameRuleUpdate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */