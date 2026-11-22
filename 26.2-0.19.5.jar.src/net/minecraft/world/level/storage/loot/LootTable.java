/*     */ package net.minecraft.world.level.storage.loot;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextKeySet;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*     */ import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LootTable implements Validatable {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  36 */   public static final Codec<ResourceKey<LootTable>> KEY_CODEC = ResourceKey.codec(Registries.LOOT_TABLE);
/*     */   
/*  38 */   public static final ContextKeySet DEFAULT_PARAM_SET = LootContextParamSets.ALL_PARAMS;
/*     */   
/*     */   public static final long RANDOMIZE_SEED = 0L;
/*     */   
/*  42 */   public static final Codec<LootTable> DIRECT_CODEC = Codec.lazyInitialized(() -> RecordCodecBuilder.create(()));
/*     */   
/*  50 */   public static final Codec<Holder<LootTable>> CODEC = (Codec<Holder<LootTable>>)RegistryFileCodec.create(Registries.LOOT_TABLE, DIRECT_CODEC);
/*     */   
/*  52 */   public static final LootTable EMPTY = new LootTable(LootContextParamSets.EMPTY, Optional.empty(), List.of(), List.of());
/*     */   
/*     */   private final ContextKeySet paramSet;
/*     */   
/*     */   private final Optional<Identifier> randomSequence;
/*     */   
/*     */   private final List<LootPool> pools;
/*     */   
/*     */   private final List<LootItemFunction> functions;
/*     */   
/*     */   private final BiFunction<ItemStack, LootContext, ItemStack> compositeFunction;
/*     */   
/*     */   private LootTable(ContextKeySet paramSet, Optional<Identifier> randomSequence, List<LootPool> pools, List<LootItemFunction> functions) {
/*  63 */     this.paramSet = paramSet;
/*  64 */     this.randomSequence = randomSequence;
/*  65 */     this.pools = pools;
/*  66 */     this.functions = functions;
/*  67 */     this.compositeFunction = LootItemFunctions.compose(functions);
/*     */   }
/*     */   
/*     */   public static Consumer<ItemStack> createStackSplitter(ServerLevel level, Consumer<ItemStack> output) {
/*  71 */     return result -> {
/*     */         if (!result.isItemEnabled(level.enabledFeatures()))
/*     */           return; 
/*     */         if (result.getCount() < result.getMaxStackSize()) {
/*     */           output.accept(result);
/*     */         } else {
/*     */           int count = result.getCount();
/*     */           while (count > 0) {
/*     */             ItemStack copy = result.copyWithCount(Math.min(result.getMaxStackSize(), count));
/*     */             count -= copy.getCount();
/*     */             output.accept(copy);
/*     */           } 
/*     */         } 
/*     */       };
/*     */   }
/*     */   
/*     */   public void getRandomItemsRaw(LootParams params, Consumer<ItemStack> output) {
/*  89 */     getRandomItemsRaw((new LootContext.Builder(params)).create(this.randomSequence), output);
/*     */   }
/*     */   
/*     */   public void getRandomItemsRaw(LootContext context, Consumer<ItemStack> output) {
/*  93 */     LootContext.VisitedEntry<?> breadcrumb = LootContext.createVisitedEntry(this);
/*  94 */     if (context.pushVisitedElement(breadcrumb)) {
/*  95 */       Consumer<ItemStack> decoratedOutput = LootItemFunction.decorate(this.compositeFunction, output, context);
/*  96 */       for (LootPool pool : this.pools)
/*  97 */         pool.addRandomItems(decoratedOutput, context); 
/*  99 */       context.popVisitedElement(breadcrumb);
/*     */     } else {
/* 101 */       LOGGER.warn("Detected infinite loop in loot tables");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void getRandomItems(LootParams params, long optionalLootTableSeed, Consumer<ItemStack> output) {
/* 106 */     getRandomItemsRaw((new LootContext.Builder(params)).withOptionalRandomSeed(optionalLootTableSeed).create(this.randomSequence), createStackSplitter(params.getLevel(), output));
/*     */   }
/*     */   
/*     */   public void getRandomItems(LootParams params, Consumer<ItemStack> output) {
/* 110 */     getRandomItemsRaw(params, createStackSplitter(params.getLevel(), output));
/*     */   }
/*     */   
/*     */   public void getRandomItems(LootContext context, Consumer<ItemStack> output) {
/* 114 */     getRandomItemsRaw(context, createStackSplitter(context.getLevel(), output));
/*     */   }
/*     */   
/*     */   public ObjectArrayList<ItemStack> getRandomItems(LootParams params, RandomSource randomSource) {
/* 118 */     return getRandomItems((new LootContext.Builder(params)).withOptionalRandomSource(randomSource).create(this.randomSequence));
/*     */   }
/*     */   
/*     */   public ObjectArrayList<ItemStack> getRandomItems(LootParams params, long optionalLootTableSeed) {
/* 122 */     return getRandomItems((new LootContext.Builder(params)).withOptionalRandomSeed(optionalLootTableSeed).create(this.randomSequence));
/*     */   }
/*     */   
/*     */   public ObjectArrayList<ItemStack> getRandomItems(LootParams params) {
/* 126 */     return getRandomItems((new LootContext.Builder(params)).create(this.randomSequence));
/*     */   }
/*     */   
/*     */   private ObjectArrayList<ItemStack> getRandomItems(LootContext context) {
/* 130 */     ObjectArrayList<ItemStack> result = new ObjectArrayList();
/* 131 */     Objects.requireNonNull(result);
/* 131 */     getRandomItems(context, result::add);
/* 132 */     return result;
/*     */   }
/*     */   
/*     */   public ContextKeySet getParamSet() {
/* 136 */     return this.paramSet;
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext context) {
/* 141 */     Validatable.validate(context, "pools", (List)this.pools);
/* 142 */     Validatable.validate(context, "functions", (List)this.functions);
/*     */   }
/*     */   
/*     */   public void fill(Container container, LootParams params, long optionalRandomSeed) {
/* 146 */     LootContext context = (new LootContext.Builder(params)).withOptionalRandomSeed(optionalRandomSeed).create(this.randomSequence);
/* 147 */     ObjectArrayList<ItemStack> itemStacks = getRandomItems(context);
/* 148 */     RandomSource random = context.getRandom();
/* 149 */     List<Integer> availableSlots = getAvailableSlots(container, random);
/* 150 */     shuffleAndSplitItems(itemStacks, availableSlots.size(), random);
/* 151 */     for (ObjectListIterator<ItemStack> objectListIterator = itemStacks.iterator(); objectListIterator.hasNext(); ) {
/* 151 */       ItemStack itemStack = objectListIterator.next();
/* 152 */       if (availableSlots.isEmpty()) {
/* 153 */         LOGGER.warn("Tried to over-fill a container");
/*     */         return;
/*     */       } 
/* 157 */       if (itemStack.isEmpty()) {
/* 158 */         container.setItem(((Integer)availableSlots.remove(availableSlots.size() - 1)).intValue(), ItemStack.EMPTY);
/*     */         continue;
/*     */       } 
/* 160 */       container.setItem(((Integer)availableSlots.remove(availableSlots.size() - 1)).intValue(), itemStack);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void shuffleAndSplitItems(ObjectArrayList<ItemStack> result, int availableSlots, RandomSource random) {
/* 166 */     List<ItemStack> splittableItems = Lists.newArrayList();
/* 167 */     for (ObjectListIterator<ItemStack> objectListIterator = result.iterator(); objectListIterator.hasNext(); ) {
/* 168 */       ItemStack itemStack = objectListIterator.next();
/* 169 */       if (itemStack.isEmpty()) {
/* 170 */         objectListIterator.remove();
/*     */         continue;
/*     */       } 
/* 171 */       if (itemStack.getCount() > 1) {
/* 172 */         splittableItems.add(itemStack);
/* 173 */         objectListIterator.remove();
/*     */       } 
/*     */     } 
/* 177 */     while (availableSlots - result.size() - splittableItems.size() > 0 && !splittableItems.isEmpty()) {
/* 178 */       ItemStack itemStack = splittableItems.remove(Mth.nextInt(random, 0, splittableItems.size() - 1));
/* 179 */       int remove = Mth.nextInt(random, 1, itemStack.getCount() / 2);
/* 180 */       ItemStack copy = itemStack.split(remove);
/* 182 */       if (itemStack.getCount() > 1 && random.nextBoolean()) {
/* 183 */         splittableItems.add(itemStack);
/*     */       } else {
/* 185 */         result.add(itemStack);
/*     */       } 
/* 188 */       if (copy.getCount() > 1 && random.nextBoolean()) {
/* 189 */         splittableItems.add(copy);
/*     */         continue;
/*     */       } 
/* 191 */       result.add(copy);
/*     */     } 
/* 195 */     result.addAll(splittableItems);
/* 197 */     Util.shuffle((List)result, random);
/*     */   }
/*     */   
/*     */   private List<Integer> getAvailableSlots(Container container, RandomSource random) {
/* 201 */     ObjectArrayList<Integer> slots = new ObjectArrayList();
/* 203 */     for (int i = 0; i < container.getContainerSize(); i++) {
/* 204 */       if (container.getItem(i).isEmpty())
/* 205 */         slots.add(Integer.valueOf(i)); 
/*     */     } 
/* 209 */     Util.shuffle((List)slots, random);
/* 210 */     return (List<Integer>)slots;
/*     */   }
/*     */   
/*     */   public static class Builder implements FunctionUserBuilder<Builder> {
/* 214 */     private final ImmutableList.Builder<LootPool> pools = ImmutableList.builder();
/*     */     
/* 216 */     private final ImmutableList.Builder<LootItemFunction> functions = ImmutableList.builder();
/*     */     
/* 218 */     private ContextKeySet paramSet = LootTable.DEFAULT_PARAM_SET;
/*     */     
/* 219 */     private Optional<Identifier> randomSequence = Optional.empty();
/*     */     
/*     */     public Builder withPool(LootPool.Builder pool) {
/* 222 */       this.pools.add(pool.build());
/* 223 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setParamSet(ContextKeySet paramSet) {
/* 227 */       this.paramSet = paramSet;
/* 228 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setRandomSequence(Identifier key) {
/* 232 */       this.randomSequence = Optional.of(key);
/* 233 */       return this;
/*     */     }
/*     */     
/*     */     public Builder apply(LootItemFunction.Builder function) {
/* 238 */       this.functions.add(function.build());
/* 239 */       return this;
/*     */     }
/*     */     
/*     */     public Builder unwrap() {
/* 244 */       return this;
/*     */     }
/*     */     
/*     */     public LootTable build() {
/* 248 */       return new LootTable(this.paramSet, this.randomSequence, (List<LootPool>)this.pools.build(), (List<LootItemFunction>)this.functions.build());
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder lootTable() {
/* 253 */     return new Builder();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootTable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */