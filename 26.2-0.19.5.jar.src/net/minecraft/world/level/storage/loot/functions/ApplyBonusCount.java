/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.item.ItemInstance;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.enchantment.Enchantment;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class ApplyBonusCount extends LootItemConditionalFunction {
/*     */   private static final class FormulaType extends Record {
/*     */     private final Identifier id;
/*     */     
/*     */     private final Codec<? extends ApplyBonusCount.Formula> codec;
/*     */     
/*     */     private FormulaType(Identifier id, Codec<? extends ApplyBonusCount.Formula> codec) {
/*  28 */       this.id = id;
/*  28 */       this.codec = codec;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #28	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$FormulaType;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Identifier id() {
/*  28 */       return this.id;
/*     */     }
/*     */     
/*     */     public Codec<? extends ApplyBonusCount.Formula> codec() {
/*  28 */       return this.codec;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class BinomialWithBonusCount extends Record implements Formula {
/*     */     private final int extraRounds;
/*     */     
/*     */     private final float probability;
/*     */     
/*     */     private static final Codec<BinomialWithBonusCount> CODEC;
/*     */     
/*     */     private BinomialWithBonusCount(int extraRounds, float probability) {
/*  37 */       this.extraRounds = extraRounds;
/*  37 */       this.probability = probability;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #37	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$BinomialWithBonusCount;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int extraRounds() {
/*  37 */       return this.extraRounds;
/*     */     }
/*     */     
/*     */     public float probability() {
/*  37 */       return this.probability;
/*     */     }
/*     */     
/*     */     static {
/*  38 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("extra").forGetter(BinomialWithBonusCount::extraRounds), (App)Codec.FLOAT.fieldOf("probability").forGetter(BinomialWithBonusCount::probability)).apply((Applicative)i, BinomialWithBonusCount::new));
/*     */     }
/*     */     
/*  43 */     public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("binomial_with_bonus_count"), (Codec)CODEC);
/*     */     
/*     */     public int calculateNewCount(RandomSource random, int count, int level) {
/*  47 */       for (int i = 0; i < level + this.extraRounds; i++) {
/*  48 */         if (random.nextFloat() < this.probability)
/*  49 */           count++; 
/*     */       } 
/*  52 */       return count;
/*     */     }
/*     */     
/*     */     public ApplyBonusCount.FormulaType getType() {
/*  57 */       return TYPE;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class UniformBonusCount extends Record implements Formula {
/*     */     private final int bonusMultiplier;
/*     */     
/*     */     public static final Codec<UniformBonusCount> CODEC;
/*     */     
/*     */     private UniformBonusCount(int bonusMultiplier) {
/*  61 */       this.bonusMultiplier = bonusMultiplier;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #61	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #61	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #61	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$UniformBonusCount;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int bonusMultiplier() {
/*  61 */       return this.bonusMultiplier;
/*     */     }
/*     */     
/*     */     static {
/*  62 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("bonusMultiplier").forGetter(UniformBonusCount::bonusMultiplier)).apply((Applicative)i, UniformBonusCount::new));
/*     */     }
/*     */     
/*  66 */     public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("uniform_bonus_count"), (Codec)CODEC);
/*     */     
/*     */     public int calculateNewCount(RandomSource random, int count, int level) {
/*  70 */       return count + random.nextInt(this.bonusMultiplier * level + 1);
/*     */     }
/*     */     
/*     */     public ApplyBonusCount.FormulaType getType() {
/*  75 */       return TYPE;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class OreDrops extends Record implements Formula {
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ApplyBonusCount$OreDrops;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*  80 */     public static final OreDrops INSTANCE = new OreDrops();
/*     */     
/*  82 */     public static final Codec<OreDrops> CODEC = MapCodec.unitCodec(INSTANCE);
/*     */     
/*  83 */     public static final ApplyBonusCount.FormulaType TYPE = new ApplyBonusCount.FormulaType(Identifier.withDefaultNamespace("ore_drops"), (Codec)CODEC);
/*     */     
/*     */     public int calculateNewCount(RandomSource random, int count, int level) {
/*  87 */       if (level > 0) {
/*  88 */         int bonus = random.nextInt(level + 2) - 1;
/*  89 */         if (bonus < 0)
/*  90 */           bonus = 0; 
/*  92 */         return count * (bonus + 1);
/*     */       } 
/*  95 */       return count;
/*     */     }
/*     */     
/*     */     public ApplyBonusCount.FormulaType getType() {
/* 100 */       return TYPE;
/*     */     }
/*     */   }
/*     */   
/* 104 */   private static final Map<Identifier, FormulaType> FORMULAS = (Map<Identifier, FormulaType>)Stream.<FormulaType>of(new FormulaType[] { BinomialWithBonusCount.TYPE, OreDrops.TYPE, UniformBonusCount.TYPE }).collect(Collectors.toMap(FormulaType::id, Function.identity()));
/*     */   
/*     */   private static final Codec<FormulaType> FORMULA_TYPE_CODEC;
/*     */   
/*     */   static {
/* 110 */     FORMULA_TYPE_CODEC = Identifier.CODEC.comapFlatMap(location -> {
/*     */           FormulaType type = FORMULAS.get(location);
/*     */           return (type != null) ? DataResult.success(type) : DataResult.error(());
/* 110 */         }FormulaType::id);
/*     */   }
/*     */   
/* 118 */   private static final MapCodec<Formula> FORMULA_CODEC = ExtraCodecs.dispatchOptionalValue("formula", "parameters", FORMULA_TYPE_CODEC, Formula::getType, FormulaType::codec);
/*     */   
/*     */   public static final MapCodec<ApplyBonusCount> MAP_CODEC;
/*     */   
/*     */   private final Holder<Enchantment> enchantment;
/*     */   
/*     */   private final Formula formula;
/*     */   
/*     */   static {
/* 120 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)Enchantment.CODEC.fieldOf("enchantment").forGetter(()), (App)FORMULA_CODEC.forGetter(()))).apply((Applicative)i, ApplyBonusCount::new));
/*     */   }
/*     */   
/*     */   private ApplyBonusCount(List<LootItemCondition> predicates, Holder<Enchantment> enchantment, Formula formula) {
/* 129 */     super(predicates);
/* 130 */     this.enchantment = enchantment;
/* 131 */     this.formula = formula;
/*     */   }
/*     */   
/*     */   public MapCodec<ApplyBonusCount> codec() {
/* 136 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 141 */     return Set.of(LootContextParams.TOOL);
/*     */   }
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 146 */     ItemInstance tool = (ItemInstance)context.getOptionalParameter(LootContextParams.TOOL);
/* 148 */     if (tool != null) {
/* 149 */       int level = EnchantmentHelper.getItemEnchantmentLevel(this.enchantment, tool);
/* 150 */       int newCount = this.formula.calculateNewCount(context.getRandom(), itemStack.getCount(), level);
/* 151 */       itemStack.setCount(newCount);
/*     */     } 
/* 153 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> addBonusBinomialDistributionCount(Holder<Enchantment> enchantment, float probability, int extraRounds) {
/* 157 */     return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, new BinomialWithBonusCount(extraRounds, probability)));
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> addOreBonusCount(Holder<Enchantment> enchantment) {
/* 161 */     return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, OreDrops.INSTANCE));
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> addUniformBonusCount(Holder<Enchantment> enchantment) {
/* 165 */     return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, new UniformBonusCount(1)));
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> addUniformBonusCount(Holder<Enchantment> enchantment, int bonusMultiplier) {
/* 169 */     return simpleBuilder(conditions -> new ApplyBonusCount(conditions, enchantment, new UniformBonusCount(bonusMultiplier)));
/*     */   }
/*     */   
/*     */   private static interface Formula {
/*     */     int calculateNewCount(RandomSource param1RandomSource, int param1Int1, int param1Int2);
/*     */     
/*     */     ApplyBonusCount.FormulaType getType();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ApplyBonusCount.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */