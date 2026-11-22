/*     */ package net.minecraft.advancements.triggers;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Arrays;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*     */ import net.minecraft.advancements.predicates.ItemPredicate;
/*     */ import net.minecraft.advancements.predicates.LocationPredicate;
/*     */ import net.minecraft.advancements.predicates.StatePropertiesPredicate;
/*     */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.Validatable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContextSource;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.predicates.MatchTool;
/*     */ 
/*     */ public final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*     */   private final Optional<ContextAwarePredicate> player;
/*     */   
/*     */   private final Optional<ContextAwarePredicate> location;
/*     */   
/*     */   public static final Codec<TriggerInstance> CODEC;
/*     */   
/*     */   public TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> location) {
/*  53 */     this.player = player;
/*  53 */     this.location = location;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/ItemUsedOnLocationTrigger$TriggerInstance;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #53	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/ItemUsedOnLocationTrigger$TriggerInstance;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/ItemUsedOnLocationTrigger$TriggerInstance;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #53	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/triggers/ItemUsedOnLocationTrigger$TriggerInstance;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/ItemUsedOnLocationTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #53	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/triggers/ItemUsedOnLocationTrigger$TriggerInstance;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Optional<ContextAwarePredicate> player() {
/*  53 */     return this.player;
/*     */   }
/*     */   
/*     */   public Optional<ContextAwarePredicate> location() {
/*  53 */     return this.location;
/*     */   }
/*     */   
/*     */   static {
/*  57 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)ContextAwarePredicate.CODEC.optionalFieldOf("location").forGetter(TriggerInstance::location)).apply((Applicative)i, TriggerInstance::new));
/*     */   }
/*     */   
/*     */   public static Criterion<TriggerInstance> placedBlock(Block block) {
/*  63 */     ContextAwarePredicate location = ContextAwarePredicate.create(new LootItemCondition[] { LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).build() });
/*  64 */     return CriteriaTriggers.PLACED_BLOCK.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(location)));
/*     */   }
/*     */   
/*     */   public static Criterion<TriggerInstance> placedBlock(LootItemCondition.Builder... conditions) {
/*  68 */     ContextAwarePredicate location = ContextAwarePredicate.create((LootItemCondition[])Arrays.<LootItemCondition.Builder>stream(conditions).map(LootItemCondition.Builder::build).toArray(x$0 -> new LootItemCondition[x$0]));
/*  69 */     return CriteriaTriggers.PLACED_BLOCK.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(location)));
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T>> Criterion<TriggerInstance> placedBlockWithProperties(Block block, Property<T> property, String propertyValue) {
/*  73 */     StatePropertiesPredicate.Builder predicateBuilder = StatePropertiesPredicate.Builder.properties().hasProperty(property, propertyValue);
/*  74 */     ContextAwarePredicate location = ContextAwarePredicate.create(new LootItemCondition[] { LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(predicateBuilder).build() });
/*  75 */     return CriteriaTriggers.PLACED_BLOCK.createCriterion(new TriggerInstance(Optional.empty(), Optional.of(location)));
/*     */   }
/*     */   
/*     */   public static Criterion<TriggerInstance> placedBlockWithProperties(Block block, Property<Boolean> property, boolean propertyValue) {
/*  79 */     return placedBlockWithProperties(block, property, String.valueOf(propertyValue));
/*     */   }
/*     */   
/*     */   public static Criterion<TriggerInstance> placedBlockWithProperties(Block block, Property<Integer> property, int propertyValue) {
/*  83 */     return placedBlockWithProperties(block, property, String.valueOf(propertyValue));
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T> & StringRepresentable> Criterion<TriggerInstance> placedBlockWithProperties(Block block, Property<T> properties, T propertyValue) {
/*  87 */     return placedBlockWithProperties(block, properties, ((StringRepresentable)propertyValue).getSerializedName());
/*     */   }
/*     */   
/*     */   private static TriggerInstance itemUsedOnLocation(LocationPredicate.Builder location, ItemPredicate.Builder item) {
/*  91 */     ContextAwarePredicate predicate = ContextAwarePredicate.create(new LootItemCondition[] { LocationCheck.checkLocation(location).build(), 
/*  93 */           MatchTool.toolMatches(item).build() });
/*  96 */     return new TriggerInstance(Optional.empty(), Optional.of(predicate));
/*     */   }
/*     */   
/*     */   public static Criterion<TriggerInstance> itemUsedOnBlock(LocationPredicate.Builder location, ItemPredicate.Builder item) {
/* 100 */     return CriteriaTriggers.ITEM_USED_ON_BLOCK.createCriterion(itemUsedOnLocation(location, item));
/*     */   }
/*     */   
/*     */   public static Criterion<TriggerInstance> allayDropItemOnBlock(LocationPredicate.Builder location, ItemPredicate.Builder item) {
/* 104 */     return CriteriaTriggers.ALLAY_DROP_ITEM_ON_BLOCK.createCriterion(itemUsedOnLocation(location, item));
/*     */   }
/*     */   
/*     */   public boolean matches(LootContext locationContext) {
/* 108 */     return (this.location.isEmpty() || ((ContextAwarePredicate)this.location.get()).matches(locationContext));
/*     */   }
/*     */   
/*     */   public void validate(ValidationContextSource validator) {
/* 113 */     super.validate(validator);
/* 115 */     Validatable.validate(validator.context(LootContextParamSets.ADVANCEMENT_LOCATION), "location", this.location);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\ItemUsedOnLocationTrigger$TriggerInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */