/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.ContainerHelper;
/*     */ import net.minecraft.world.WorldlyContainer;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.player.StackedItemContents;
/*     */ import net.minecraft.world.inventory.ContainerData;
/*     */ import net.minecraft.world.inventory.RecipeCraftingHolder;
/*     */ import net.minecraft.world.inventory.StackedContentsCompatible;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeInput;
/*     */ import net.minecraft.world.item.crafting.RecipeManager;
/*     */ import net.minecraft.world.item.crafting.RecipeType;
/*     */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.AbstractFurnaceBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractFurnaceBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, StackedContentsCompatible, RecipeCraftingHolder {
/*     */   protected static final int SLOT_INPUT = 0;
/*     */   
/*     */   protected static final int SLOT_FUEL = 1;
/*     */   
/*     */   protected static final int SLOT_RESULT = 2;
/*     */   
/*     */   public static final int DATA_LIT_TIME = 0;
/*     */   
/*  51 */   private static final int[] SLOTS_FOR_UP = new int[] { 0 };
/*     */   
/*  54 */   private static final int[] SLOTS_FOR_DOWN = new int[] { 2, 1 };
/*     */   
/*  57 */   private static final int[] SLOTS_FOR_SIDES = new int[] { 1 };
/*     */   
/*     */   public static final int DATA_LIT_DURATION = 1;
/*     */   
/*     */   public static final int DATA_COOKING_PROGRESS = 2;
/*     */   
/*     */   public static final int DATA_COOKING_TOTAL_TIME = 3;
/*     */   
/*     */   public static final int NUM_DATA_VALUES = 4;
/*     */   
/*     */   public static final int BURN_TIME_STANDARD = 200;
/*     */   
/*     */   public static final int BURN_COOL_SPEED = 2;
/*     */   
/*  69 */   private static final Codec<Map<ResourceKey<Recipe<?>>, Integer>> RECIPES_USED_CODEC = (Codec<Map<ResourceKey<Recipe<?>>, Integer>>)Codec.unboundedMap(Recipe.KEY_CODEC, (Codec)Codec.INT);
/*     */   
/*     */   private static final short DEFAULT_COOKING_TIMER = 0;
/*     */   
/*     */   private static final short DEFAULT_COOKING_TOTAL_TIME = 0;
/*     */   
/*     */   private static final short DEFAULT_LIT_TIME_REMAINING = 0;
/*     */   
/*     */   private static final short DEFAULT_LIT_TOTAL_TIME = 0;
/*     */   
/*  76 */   protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
/*     */   
/*     */   private int litTimeRemaining;
/*     */   
/*     */   private int litTotalTime;
/*     */   
/*     */   private int cookingTimer;
/*     */   
/*     */   private int cookingTotalTime;
/*     */   
/*  83 */   protected final ContainerData dataAccess = new ContainerData() {
/*     */       public int get(int dataId) {
/*  86 */         switch (dataId) {
/*     */           case 0:
/*  88 */             return AbstractFurnaceBlockEntity.this.litTimeRemaining;
/*     */           case 1:
/*  91 */             return AbstractFurnaceBlockEntity.this.litTotalTime;
/*     */           case 2:
/*  94 */             return AbstractFurnaceBlockEntity.this.cookingTimer;
/*     */           case 3:
/*  97 */             return AbstractFurnaceBlockEntity.this.cookingTotalTime;
/*     */         } 
/* 102 */         return 0;
/*     */       }
/*     */       
/*     */       public void set(int dataId, int value) {
/* 107 */         switch (dataId) {
/*     */           case 0:
/* 108 */             AbstractFurnaceBlockEntity.this.litTimeRemaining = value;
/*     */             break;
/*     */           case 1:
/* 109 */             AbstractFurnaceBlockEntity.this.litTotalTime = value;
/*     */             break;
/*     */           case 2:
/* 110 */             AbstractFurnaceBlockEntity.this.cookingTimer = value;
/*     */             break;
/*     */           case 3:
/* 111 */             AbstractFurnaceBlockEntity.this.cookingTotalTime = value;
/*     */             break;
/*     */         } 
/*     */       }
/*     */       
/*     */       public int getCount() {
/* 119 */         return 4;
/*     */       }
/*     */     };
/*     */   
/* 123 */   private final Reference2IntOpenHashMap<ResourceKey<Recipe<?>>> recipesUsed = new Reference2IntOpenHashMap();
/*     */   
/*     */   private final RecipeManager.CachedCheck<SingleRecipeInput, ? extends AbstractCookingRecipe> quickCheck;
/*     */   
/*     */   protected AbstractFurnaceBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, RecipeType<? extends AbstractCookingRecipe> recipeType) {
/* 128 */     super(type, worldPosition, blockState);
/* 129 */     this.quickCheck = RecipeManager.createCheck(recipeType);
/*     */   }
/*     */   
/*     */   protected void loadAdditional(ValueInput input) {
/* 134 */     super.loadAdditional(input);
/* 136 */     this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
/* 137 */     ContainerHelper.loadAllItems(input, this.items);
/* 139 */     this.cookingTimer = input.getShortOr("cooking_time_spent", (short)0);
/* 140 */     this.cookingTotalTime = input.getShortOr("cooking_total_time", (short)0);
/* 141 */     this.litTimeRemaining = input.getShortOr("lit_time_remaining", (short)0);
/* 142 */     this.litTotalTime = input.getShortOr("lit_total_time", (short)0);
/* 144 */     this.recipesUsed.clear();
/* 145 */     this.recipesUsed.putAll(input.read("RecipesUsed", RECIPES_USED_CODEC).orElse(Map.of()));
/*     */   }
/*     */   
/*     */   protected void saveAdditional(ValueOutput output) {
/* 150 */     super.saveAdditional(output);
/* 151 */     output.putShort("cooking_time_spent", (short)this.cookingTimer);
/* 152 */     output.putShort("cooking_total_time", (short)this.cookingTotalTime);
/* 153 */     output.putShort("lit_time_remaining", (short)this.litTimeRemaining);
/* 154 */     output.putShort("lit_total_time", (short)this.litTotalTime);
/* 156 */     ContainerHelper.saveAllItems(output, this.items);
/* 158 */     output.store("RecipesUsed", RECIPES_USED_CODEC, this.recipesUsed);
/*     */   }
/*     */   
/*     */   public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity entity) {
/* 163 */     boolean isLit, wasLit, changed = false;
/* 167 */     if (entity.litTimeRemaining > 0) {
/* 168 */       wasLit = true;
/* 170 */       entity.litTimeRemaining--;
/* 171 */       isLit = (entity.litTimeRemaining > 0);
/*     */     } else {
/* 173 */       wasLit = false;
/* 174 */       isLit = false;
/*     */     } 
/* 177 */     ItemStack fuel = (ItemStack)entity.items.get(1);
/* 178 */     ItemStack ingredient = (ItemStack)entity.items.get(0);
/* 179 */     boolean hasIngredient = !ingredient.isEmpty();
/* 180 */     boolean hasFuel = !fuel.isEmpty();
/* 182 */     if (isLit || (hasFuel && hasIngredient)) {
/* 184 */       if (hasIngredient) {
/* 185 */         SingleRecipeInput input = new SingleRecipeInput(ingredient);
/* 186 */         RecipeHolder<? extends AbstractCookingRecipe> recipe = entity.quickCheck.getRecipeFor((RecipeInput)input, level).orElse(null);
/* 187 */         if (recipe != null) {
/* 188 */           int maxStackSize = entity.getMaxStackSize();
/* 189 */           ItemStack burnResult = ((AbstractCookingRecipe)recipe.value()).assemble(input);
/* 190 */           if (!burnResult.isEmpty() && canBurn(entity.items, maxStackSize, burnResult)) {
/* 191 */             if (!isLit) {
/* 193 */               int newLitTime = entity.getBurnDuration(level.fuelValues(), fuel);
/* 194 */               entity.litTimeRemaining = newLitTime;
/* 195 */               entity.litTotalTime = newLitTime;
/* 197 */               if (newLitTime > 0) {
/* 198 */                 consumeFuel(entity.items, fuel);
/* 199 */                 isLit = true;
/* 200 */                 changed = true;
/*     */               } 
/*     */             } 
/* 204 */             if (isLit) {
/* 205 */               entity.cookingTimer++;
/* 207 */               if (entity.cookingTimer == entity.cookingTotalTime) {
/* 208 */                 entity.cookingTimer = 0;
/* 210 */                 entity.cookingTotalTime = ((AbstractCookingRecipe)recipe.value()).cookingTime();
/* 211 */                 burn(entity.items, ingredient, burnResult);
/* 212 */                 entity.setRecipeUsed(recipe);
/* 213 */                 changed = true;
/*     */               } 
/*     */             } else {
/* 216 */               entity.cookingTimer = 0;
/*     */             } 
/*     */           } else {
/* 219 */             entity.cookingTimer = 0;
/*     */           } 
/*     */         } 
/*     */       } else {
/* 223 */         entity.cookingTimer = 0;
/*     */       } 
/* 225 */     } else if (entity.cookingTimer > 0) {
/* 226 */       entity.cookingTimer = Mth.clamp(entity.cookingTimer - 2, 0, entity.cookingTotalTime);
/*     */     } 
/* 229 */     if (wasLit != isLit) {
/* 230 */       changed = true;
/* 231 */       state = (BlockState)state.setValue((Property)AbstractFurnaceBlock.LIT, Boolean.valueOf(isLit));
/* 232 */       level.setBlock(pos, state, 3);
/*     */     } 
/* 235 */     if (changed)
/* 236 */       setChanged((Level)level, pos, state); 
/*     */   }
/*     */   
/*     */   private static void consumeFuel(NonNullList<ItemStack> items, ItemStack fuel) {
/* 241 */     Item fuelItem = fuel.getItem();
/* 242 */     fuel.shrink(1);
/* 243 */     if (fuel.isEmpty()) {
/* 244 */       ItemStackTemplate remainder = fuelItem.getCraftingRemainder();
/* 245 */       items.set(1, (remainder != null) ? remainder.create() : ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean canBurn(NonNullList<ItemStack> items, int maxStackSize, ItemStack burnResult) {
/* 250 */     ItemStack resultItemStack = (ItemStack)items.get(2);
/* 251 */     if (resultItemStack.isEmpty())
/* 252 */       return true; 
/* 254 */     if (!ItemStack.isSameItemSameComponents(resultItemStack, burnResult))
/* 255 */       return false; 
/* 257 */     int resultCount = resultItemStack.getCount() + burnResult.count();
/* 258 */     int maxResultCount = Math.min(maxStackSize, burnResult.getMaxStackSize());
/* 259 */     return (resultCount <= maxResultCount);
/*     */   }
/*     */   
/*     */   private static void burn(NonNullList<ItemStack> items, ItemStack inputItemStack, ItemStack result) {
/* 264 */     ItemStack resultItemStack = (ItemStack)items.get(2);
/* 265 */     if (resultItemStack.isEmpty()) {
/* 266 */       items.set(2, result.copy());
/*     */     } else {
/* 268 */       resultItemStack.grow(result.getCount());
/*     */     } 
/* 271 */     if (inputItemStack.is(Items.WET_SPONGE) && !((ItemStack)items.get(1)).isEmpty() && ((ItemStack)items.get(1)).is(Items.BUCKET))
/* 272 */       items.set(1, new ItemStack((ItemLike)Items.WATER_BUCKET)); 
/* 275 */     inputItemStack.shrink(1);
/*     */   }
/*     */   
/*     */   protected int getBurnDuration(FuelValues fuelValues, ItemStack itemStack) {
/* 279 */     return fuelValues.burnDuration(itemStack);
/*     */   }
/*     */   
/*     */   private static int getTotalCookTime(ServerLevel level, AbstractFurnaceBlockEntity entity) {
/* 283 */     SingleRecipeInput input = new SingleRecipeInput(entity.getItem(0));
/* 284 */     return ((Integer)entity.quickCheck.getRecipeFor((RecipeInput)input, level).map(recipeHolder -> Integer.valueOf(((AbstractCookingRecipe)recipeHolder.value()).cookingTime())).orElse(Integer.valueOf(200))).intValue();
/*     */   }
/*     */   
/*     */   public int[] getSlotsForFace(Direction direction) {
/* 289 */     if (direction == Direction.DOWN)
/* 290 */       return SLOTS_FOR_DOWN; 
/* 291 */     if (direction == Direction.UP)
/* 292 */       return SLOTS_FOR_UP; 
/* 294 */     return SLOTS_FOR_SIDES;
/*     */   }
/*     */   
/*     */   public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
/* 300 */     return canPlaceItem(slot, itemStack);
/*     */   }
/*     */   
/*     */   public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
/* 305 */     if (direction == Direction.DOWN && slot == 1)
/* 306 */       return (itemStack.is(Items.WATER_BUCKET) || itemStack.is(Items.BUCKET)); 
/* 309 */     return true;
/*     */   }
/*     */   
/*     */   public int getContainerSize() {
/* 314 */     return this.items.size();
/*     */   }
/*     */   
/*     */   protected NonNullList<ItemStack> getItems() {
/* 319 */     return this.items;
/*     */   }
/*     */   
/*     */   protected void setItems(NonNullList<ItemStack> items) {
/* 324 */     this.items = items;
/*     */   }
/*     */   
/*     */   public void setItem(int slot, ItemStack itemStack) {
/* 329 */     ItemStack oldStack = (ItemStack)this.items.get(slot);
/* 330 */     boolean same = (!itemStack.isEmpty() && ItemStack.isSameItemSameComponents(oldStack, itemStack));
/* 331 */     this.items.set(slot, itemStack);
/* 332 */     itemStack.limitSize(getMaxStackSize(itemStack));
/* 334 */     if (slot == 0 && !same) {
/* 334 */       Level level = this.level;
/* 334 */       if (level instanceof ServerLevel) {
/* 334 */         ServerLevel serverLevel = (ServerLevel)level;
/* 335 */         this.cookingTotalTime = getTotalCookTime(serverLevel, this);
/* 336 */         this.cookingTimer = 0;
/* 337 */         setChanged();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canPlaceItem(int slot, ItemStack itemStack) {
/* 343 */     if (slot == 2)
/* 344 */       return false; 
/* 346 */     if (slot == 1) {
/* 347 */       ItemStack fuelSlot = (ItemStack)this.items.get(1);
/* 348 */       return (this.level.fuelValues().isFuel(itemStack) || (itemStack.is(Items.BUCKET) && !fuelSlot.is(Items.BUCKET)));
/*     */     } 
/* 350 */     return true;
/*     */   }
/*     */   
/*     */   public void setRecipeUsed(RecipeHolder<?> recipeUsed) {
/* 355 */     if (recipeUsed != null) {
/* 356 */       ResourceKey<Recipe<?>> id = recipeUsed.id();
/* 357 */       this.recipesUsed.addTo(id, 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public RecipeHolder<?> getRecipeUsed() {
/* 363 */     return null;
/*     */   }
/*     */   
/*     */   public void awardUsedRecipes(Player player, List<ItemStack> itemStacks) {}
/*     */   
/*     */   public void awardUsedRecipesAndPopExperience(ServerPlayer player) {
/* 372 */     List<RecipeHolder<?>> recipesToAward = getRecipesToAwardAndPopExperience(player.level(), player.position());
/* 373 */     player.awardRecipes(recipesToAward);
/* 374 */     for (RecipeHolder<?> recipe : recipesToAward)
/* 375 */       player.triggerRecipeCrafted(recipe, (List)this.items); 
/* 377 */     this.recipesUsed.clear();
/*     */   }
/*     */   
/*     */   public List<RecipeHolder<?>> getRecipesToAwardAndPopExperience(ServerLevel level, Vec3 position) {
/* 381 */     List<RecipeHolder<?>> recipesToAward = Lists.newArrayList();
/* 382 */     for (ObjectIterator<Reference2IntMap.Entry<ResourceKey<Recipe<?>>>> objectIterator = this.recipesUsed.reference2IntEntrySet().iterator(); objectIterator.hasNext(); ) {
/* 382 */       Reference2IntMap.Entry<ResourceKey<Recipe<?>>> entry = objectIterator.next();
/* 383 */       level.recipeAccess().byKey((ResourceKey)entry.getKey()).ifPresent(recipe -> {
/*     */             recipesToAward.add(recipe);
/*     */             createExperience(level, position, entry.getIntValue(), ((AbstractCookingRecipe)recipe.value()).experience());
/*     */           });
/*     */     } 
/* 388 */     return recipesToAward;
/*     */   }
/*     */   
/*     */   private static void createExperience(ServerLevel level, Vec3 position, int amount, float value) {
/* 392 */     int xpReward = Mth.floor(amount * value);
/* 393 */     float xpFraction = Mth.frac(amount * value);
/* 394 */     if (xpFraction != 0.0F && level.getRandom().nextFloat() < xpFraction)
/* 395 */       xpReward++; 
/* 398 */     ExperienceOrb.award(level, position, xpReward);
/*     */   }
/*     */   
/*     */   public void fillStackedContents(StackedItemContents contents) {
/* 403 */     for (ItemStack itemStack : this.items)
/* 404 */       contents.accountStack(itemStack); 
/*     */   }
/*     */   
/*     */   public void preRemoveSideEffects(BlockPos pos, BlockState state) {
/* 410 */     super.preRemoveSideEffects(pos, state);
/* 411 */     Level level = this.level;
/* 411 */     if (level instanceof ServerLevel) {
/* 411 */       ServerLevel serverLevel = (ServerLevel)level;
/* 412 */       getRecipesToAwardAndPopExperience(serverLevel, Vec3.atCenterOf((Vec3i)pos));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\AbstractFurnaceBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */