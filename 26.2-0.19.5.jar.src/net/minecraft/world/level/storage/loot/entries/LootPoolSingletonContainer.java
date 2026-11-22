/*     */ package net.minecraft.world.level.storage.loot.entries;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.Products;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.Validatable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public abstract class LootPoolSingletonContainer extends LootPoolEntryContainer {
/*     */   public static final int DEFAULT_WEIGHT = 1;
/*     */   
/*     */   public static final int DEFAULT_QUALITY = 0;
/*     */   
/*     */   protected final int weight;
/*     */   
/*     */   protected final int quality;
/*     */   
/*     */   protected final List<LootItemFunction> functions;
/*     */   
/*     */   private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*     */   
/*     */   private final LootPoolEntry entry;
/*     */   
/*     */   protected LootPoolSingletonContainer(int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/*  33 */     super(conditions);
/*  66 */     this.entry = new EntryBase() {
/*     */         public void createItemStack(Consumer<ItemStack> output, LootContext context) {
/*  69 */           LootPoolSingletonContainer.this.createItemStack(LootItemFunction.decorate(LootPoolSingletonContainer.this.compositeFunction, output, context), context);
/*     */         }
/*     */       };
/*     */     this.weight = weight;
/*     */     this.quality = quality;
/*     */     this.functions = functions;
/*     */     this.compositeFunction = LootItemFunctions.compose(functions);
/*     */   }
/*     */   
/*     */   protected static <T extends LootPoolSingletonContainer> Products.P4<RecordCodecBuilder.Mu<T>, Integer, Integer, List<LootItemCondition>, List<LootItemFunction>> singletonFields(RecordCodecBuilder.Instance<T> i) {
/*     */     return i.group((App)Codec.INT.optionalFieldOf("weight", Integer.valueOf(1)).forGetter(e -> Integer.valueOf(e.weight)), (App)Codec.INT.optionalFieldOf("quality", Integer.valueOf(0)).forGetter(e -> Integer.valueOf(e.quality))).and(commonFields((RecordCodecBuilder.Instance)i).t1()).and((App)LootItemFunctions.ROOT_CODEC.listOf().optionalFieldOf("functions", List.of()).forGetter(e -> e.functions));
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext context) {
/*     */     super.validate(context);
/*     */     Validatable.validate(context, "functions", this.functions);
/*     */   }
/*     */   
/*     */   protected abstract class EntryBase implements LootPoolEntry {
/*     */     public int getWeight(float luck) {
/*     */       return Math.max(Mth.floor(LootPoolSingletonContainer.this.weight + LootPoolSingletonContainer.this.quality * luck), 0);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean expand(LootContext context, Consumer<LootPoolEntry> output) {
/*  77 */     if (canRun(context)) {
/*  78 */       output.accept(this.entry);
/*  79 */       return true;
/*     */     } 
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   public static abstract class Builder<T extends Builder<T>> extends LootPoolEntryContainer.Builder<T> implements FunctionUserBuilder<T> {
/*  86 */     protected int weight = 1;
/*     */     
/*  87 */     protected int quality = 0;
/*     */     
/*  89 */     private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/*     */     
/*     */     public T apply(LootItemFunction.Builder function) {
/*  93 */       this.functions.add(function.build());
/*  94 */       return getThis();
/*     */     }
/*     */     
/*     */     protected List<LootItemFunction> getFunctions() {
/*  98 */       return (List<LootItemFunction>)this.functions.build();
/*     */     }
/*     */     
/*     */     public T setWeight(int weight) {
/* 102 */       this.weight = weight;
/* 103 */       return getThis();
/*     */     }
/*     */     
/*     */     public T setQuality(int quality) {
/* 107 */       this.quality = quality;
/* 108 */       return getThis();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DummyBuilder extends Builder<DummyBuilder> {
/*     */     private final LootPoolSingletonContainer.EntryConstructor constructor;
/*     */     
/*     */     public DummyBuilder(LootPoolSingletonContainer.EntryConstructor constructor) {
/* 121 */       this.constructor = constructor;
/*     */     }
/*     */     
/*     */     protected DummyBuilder getThis() {
/* 126 */       return this;
/*     */     }
/*     */     
/*     */     public LootPoolEntryContainer build() {
/* 131 */       return this.constructor.build(this.weight, this.quality, getConditions(), getFunctions());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder<?> simpleBuilder(EntryConstructor constructor) {
/* 136 */     return new DummyBuilder(constructor);
/*     */   }
/*     */   
/*     */   public abstract MapCodec<? extends LootPoolSingletonContainer> codec();
/*     */   
/*     */   protected abstract void createItemStack(Consumer<ItemStack> paramConsumer, LootContext paramLootContext);
/*     */   
/*     */   @FunctionalInterface
/*     */   protected static interface EntryConstructor {
/*     */     LootPoolSingletonContainer build(int param1Int1, int param1Int2, List<LootItemCondition> param1List, List<LootItemFunction> param1List1);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolSingletonContainer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */