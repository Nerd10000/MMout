/*     */ package net.minecraft.world.level.gamerules;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.arguments.ArgumentType;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Objects;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.flag.FeatureElement;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ 
/*     */ public final class GameRule<T> implements FeatureElement {
/*     */   private final GameRuleCategory category;
/*     */   
/*     */   private final GameRuleType gameRuleType;
/*     */   
/*     */   private final ArgumentType<T> argument;
/*     */   
/*     */   private final GameRules.VisitorCaller<T> visitorCaller;
/*     */   
/*     */   private final Codec<T> valueCodec;
/*     */   
/*     */   private final ToIntFunction<T> commandResultFunction;
/*     */   
/*     */   private final T defaultValue;
/*     */   
/*     */   private final FeatureFlagSet requiredFeatures;
/*     */   
/*     */   public GameRule(GameRuleCategory category, GameRuleType gameRuleType, ArgumentType<T> argument, GameRules.VisitorCaller<T> visitorCaller, Codec<T> valueCodec, ToIntFunction<T> commandResultFunction, T defaultValue, FeatureFlagSet requiredFeatures) {
/*  37 */     this.category = category;
/*  38 */     this.gameRuleType = gameRuleType;
/*  39 */     this.argument = argument;
/*  40 */     this.visitorCaller = visitorCaller;
/*  41 */     this.valueCodec = valueCodec;
/*  42 */     this.commandResultFunction = commandResultFunction;
/*  43 */     this.defaultValue = defaultValue;
/*  44 */     this.requiredFeatures = requiredFeatures;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  49 */     return id();
/*     */   }
/*     */   
/*     */   public String id() {
/*  53 */     return getIdentifier().toShortString();
/*     */   }
/*     */   
/*     */   public Identifier getIdentifier() {
/*  57 */     return Objects.<Identifier>requireNonNull(BuiltInRegistries.GAME_RULE.getKey(this));
/*     */   }
/*     */   
/*     */   public Identifier getIdentifierWithFallback() {
/*  61 */     return Objects.<Identifier>requireNonNullElse(BuiltInRegistries.GAME_RULE.getKey(this), Identifier.withDefaultNamespace("unregistered_sadface"));
/*     */   }
/*     */   
/*     */   public String getDescriptionId() {
/*  65 */     return Util.makeDescriptionId("gamerule", getIdentifier());
/*     */   }
/*     */   
/*     */   public String serialize(T value) {
/*  69 */     return value.toString();
/*     */   }
/*     */   
/*     */   public DataResult<T> deserialize(String value) {
/*     */     try {
/*  74 */       StringReader reader = new StringReader(value);
/*  75 */       T result = (T)this.argument.parse(reader);
/*  76 */       if (reader.canRead())
/*  77 */         return DataResult.error(() -> "Failed to deserialize; trailing characters", result); 
/*  79 */       return DataResult.success(result);
/*  80 */     } catch (CommandSyntaxException ignored) {
/*  81 */       return DataResult.error(() -> "Failed to deserialize");
/*     */     } 
/*     */   }
/*     */   
/*     */   public Class<T> valueClass() {
/*  87 */     return (Class)this.defaultValue.getClass();
/*     */   }
/*     */   
/*     */   public void callVisitor(GameRuleTypeVisitor visitor) {
/*  91 */     this.visitorCaller.call(visitor, this);
/*     */   }
/*     */   
/*     */   public int getCommandResult(T value) {
/*  95 */     return this.commandResultFunction.applyAsInt(value);
/*     */   }
/*     */   
/*     */   public GameRuleCategory category() {
/*  99 */     return this.category;
/*     */   }
/*     */   
/*     */   public GameRuleType gameRuleType() {
/* 103 */     return this.gameRuleType;
/*     */   }
/*     */   
/*     */   public ArgumentType<T> argument() {
/* 107 */     return this.argument;
/*     */   }
/*     */   
/*     */   public Codec<T> valueCodec() {
/* 111 */     return this.valueCodec;
/*     */   }
/*     */   
/*     */   public T defaultValue() {
/* 115 */     return this.defaultValue;
/*     */   }
/*     */   
/*     */   public FeatureFlagSet requiredFeatures() {
/* 120 */     return this.requiredFeatures;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gamerules\GameRule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */