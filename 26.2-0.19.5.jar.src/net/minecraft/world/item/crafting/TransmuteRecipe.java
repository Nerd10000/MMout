/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class TransmuteRecipe extends NormalCraftingRecipe {
/*     */   private static final int MIN_MATERIAL_COUNT = 1;
/*     */   
/*     */   private static final int MAX_MATERIAL_COUNT = 8;
/*     */   
/*  28 */   public static final MinMaxBounds.Ints DEFAULT_MATERIAL_COUNT = MinMaxBounds.Ints.exactly(1);
/*     */   
/*  30 */   public static final MinMaxBounds.Ints FULL_RANGE_MATERIAL_COUNT = MinMaxBounds.Ints.between(1, 8);
/*     */   
/*  32 */   public static final Codec<MinMaxBounds.Ints> MATERIAL_COUNT_BOUNDS = MinMaxBounds.Ints.CODEC
/*  33 */     .validate(MinMaxBounds.validateContainedInRange((MinMaxBounds)FULL_RANGE_MATERIAL_COUNT));
/*     */   
/*     */   public static final MapCodec<TransmuteRecipe> MAP_CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, TransmuteRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/*  35 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Recipe.CommonInfo.MAP_CODEC.forGetter(()), (App)CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(()), (App)Ingredient.CODEC.fieldOf("input").forGetter(()), (App)Ingredient.CODEC.fieldOf("material").forGetter(()), (App)MATERIAL_COUNT_BOUNDS.optionalFieldOf("material_count", DEFAULT_MATERIAL_COUNT).forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(()), (App)Codec.BOOL.optionalFieldOf("add_material_count_to_result", Boolean.valueOf(false)).forGetter(())).apply((Applicative)i, TransmuteRecipe::new));
/*  45 */     STREAM_CODEC = StreamCodec.composite(Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo, CraftingRecipe.CraftingBookInfo.STREAM_CODEC, o -> o.bookInfo, Ingredient.CONTENTS_STREAM_CODEC, o -> o.input, Ingredient.CONTENTS_STREAM_CODEC, o -> o.material, MinMaxBounds.Ints.STREAM_CODEC, o -> o.materialCount, ItemStackTemplate.STREAM_CODEC, o -> o.result, ByteBufCodecs.BOOL, o -> Boolean.valueOf(o.addMaterialCountToResult), TransmuteRecipe::new);
/*     */   }
/*     */   
/*  56 */   public static final RecipeSerializer<TransmuteRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   private final Ingredient input;
/*     */   
/*     */   private final Ingredient material;
/*     */   
/*     */   private final MinMaxBounds.Ints materialCount;
/*     */   
/*     */   private final ItemStackTemplate result;
/*     */   
/*     */   private final boolean addMaterialCountToResult;
/*     */   
/*     */   public TransmuteRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, Ingredient input, Ingredient material, MinMaxBounds.Ints materialCount, ItemStackTemplate result, boolean addMaterialCountToResult) {
/*  66 */     super(commonInfo, bookInfo);
/*  67 */     this.input = input;
/*  68 */     this.material = material;
/*  69 */     this.materialCount = materialCount;
/*  70 */     this.result = result;
/*  71 */     this.addMaterialCountToResult = addMaterialCountToResult;
/*     */   }
/*     */   
/*     */   public static ItemStack createWithOriginalComponents(ItemStackTemplate target, ItemStack input) {
/*  75 */     return createWithOriginalComponents(target, input, 0);
/*     */   }
/*     */   
/*     */   public static ItemStack createWithOriginalComponents(ItemStackTemplate target, ItemStack input, int extraCount) {
/*  82 */     return target.apply(target.count() + extraCount, input.getComponentsPatch());
/*     */   }
/*     */   
/*     */   private int computeResultSize(int materialCount) {
/*  86 */     return this.addMaterialCountToResult ? (materialCount + this.result.count()) : this.result.count();
/*     */   }
/*     */   
/*     */   private ItemStack computeResult(ItemStack inputIngredient, int materialCount) {
/*  90 */     return createWithOriginalComponents(this.result, inputIngredient, materialCount);
/*     */   }
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  95 */     int minMaterialCount = minMaterialCount();
/*  96 */     int maxMaterialCount = maxMaterialCount();
/*  99 */     if (input.ingredientCount() < minMaterialCount + 1 || input.ingredientCount() > maxMaterialCount + 1)
/* 100 */       return false; 
/* 103 */     ItemStack foundInput = null;
/* 104 */     int materialCount = 0;
/* 106 */     for (int slot = 0; slot < input.size(); slot++) {
/* 107 */       ItemStack stack = input.getItem(slot);
/* 109 */       if (!stack.isEmpty())
/* 113 */         if (this.input.test(stack)) {
/* 114 */           if (foundInput != null)
/* 115 */             return false; 
/* 117 */           foundInput = stack;
/* 118 */         } else if (this.material.test(stack)) {
/* 119 */           materialCount++;
/* 120 */           if (materialCount > maxMaterialCount)
/* 121 */             return false; 
/*     */         } else {
/* 124 */           return false;
/*     */         }  
/*     */     } 
/* 128 */     if (foundInput == null || foundInput.isEmpty() || !this.materialCount.matches(materialCount))
/* 129 */       return false; 
/* 132 */     int resultCount = computeResultSize(materialCount);
/* 134 */     if (resultCount != 1)
/* 135 */       return true; 
/* 138 */     ItemStack result = computeResult(foundInput, 0);
/* 139 */     if (result.isEmpty())
/* 141 */       return false; 
/* 144 */     return !ItemStack.isSameItemSameComponents(foundInput, result);
/*     */   }
/*     */   
/*     */   public ItemStack assemble(CraftingInput input) {
/* 149 */     if (this.addMaterialCountToResult) {
/* 150 */       int materialCount = 0;
/* 151 */       ItemStack inputIngredient = ItemStack.EMPTY;
/* 152 */       for (int i = 0; i < input.size(); i++) {
/* 153 */         ItemStack itemStack = input.getItem(i);
/* 154 */         if (!itemStack.isEmpty())
/* 158 */           if (this.input.test(itemStack)) {
/* 159 */             inputIngredient = itemStack;
/* 160 */           } else if (this.material.test(itemStack)) {
/* 161 */             materialCount++;
/*     */           }  
/*     */       } 
/* 165 */       return computeResult(inputIngredient, materialCount);
/*     */     } 
/* 167 */     for (int slot = 0; slot < input.size(); slot++) {
/* 168 */       ItemStack itemStack = input.getItem(slot);
/* 170 */       if (!itemStack.isEmpty() && this.input.test(itemStack))
/* 171 */         return computeResult(itemStack, 0); 
/*     */     } 
/* 174 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public List<RecipeDisplay> display() {
/* 180 */     List<RecipeDisplay> displays = new ArrayList<>();
/* 181 */     List<SlotDisplay> ingredientSlots = new ArrayList<>();
/* 182 */     ingredientSlots.add(this.input.display());
/* 184 */     SlotDisplay materialDisplay = this.material.display();
/* 185 */     int maxMaterialCount = maxMaterialCount();
/* 186 */     int minMaterialCount = minMaterialCount();
/* 187 */     for (int materialCount = minMaterialCount; materialCount <= maxMaterialCount; materialCount++) {
/* 188 */       ingredientSlots.add(materialDisplay);
/* 189 */       int resultCount = computeResultSize(materialCount);
/* 190 */       displays.add(new ShapelessCraftingRecipeDisplay(
/* 191 */             List.copyOf(ingredientSlots), (SlotDisplay)new SlotDisplay.ItemStackSlotDisplay(this.result
/* 192 */               .withCount(resultCount)), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
/*     */     } 
/* 197 */     return displays;
/*     */   }
/*     */   
/*     */   private int minMaterialCount() {
/* 201 */     return ((Integer)this.materialCount.min().orElse(Integer.valueOf(1))).intValue();
/*     */   }
/*     */   
/*     */   private int maxMaterialCount() {
/* 205 */     return ((Integer)this.materialCount.max().orElse(Integer.valueOf(8))).intValue();
/*     */   }
/*     */   
/*     */   public RecipeSerializer<TransmuteRecipe> getSerializer() {
/* 210 */     return SERIALIZER;
/*     */   }
/*     */   
/*     */   protected PlacementInfo createPlacementInfo() {
/* 217 */     int maxMaterialCount = maxMaterialCount();
/* 218 */     List<Ingredient> ingredients = new ArrayList<>(1 + maxMaterialCount);
/* 219 */     ingredients.add(this.input);
/* 220 */     ingredients.addAll(Collections.nCopies(maxMaterialCount, this.material));
/* 221 */     return PlacementInfo.create(ingredients);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\TransmuteRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */