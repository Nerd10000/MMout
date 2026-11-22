/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.DyedItemColor;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class DyeRecipe extends NormalCraftingRecipe {
/*     */   public static final MapCodec<DyeRecipe> MAP_CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, DyeRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/*  22 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Recipe.CommonInfo.MAP_CODEC.forGetter(()), (App)CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(()), (App)Ingredient.CODEC.fieldOf("target").forGetter(()), (App)Ingredient.CODEC.fieldOf("dye").forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(())).apply((Applicative)i, DyeRecipe::new));
/*  30 */     STREAM_CODEC = StreamCodec.composite(Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo, CraftingRecipe.CraftingBookInfo.STREAM_CODEC, o -> o.bookInfo, Ingredient.CONTENTS_STREAM_CODEC, o -> o.target, Ingredient.CONTENTS_STREAM_CODEC, o -> o.dye, ItemStackTemplate.STREAM_CODEC, o -> o.result, DyeRecipe::new);
/*     */   }
/*     */   
/*  39 */   public static final RecipeSerializer<DyeRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   private final Ingredient target;
/*     */   
/*     */   private final Ingredient dye;
/*     */   
/*     */   private final ItemStackTemplate result;
/*     */   
/*     */   public DyeRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, Ingredient target, Ingredient dye, ItemStackTemplate result) {
/*  46 */     super(commonInfo, bookInfo);
/*  47 */     this.target = target;
/*  48 */     this.dye = dye;
/*  49 */     this.result = result;
/*     */   }
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  55 */     if (input.ingredientCount() < 2)
/*  56 */       return false; 
/*  59 */     boolean hasTarget = false;
/*  60 */     boolean hasDyes = false;
/*  62 */     for (int slot = 0; slot < input.size(); slot++) {
/*  63 */       ItemStack itemStack = input.getItem(slot);
/*  64 */       if (!itemStack.isEmpty())
/*  68 */         if (this.target.test(itemStack)) {
/*  69 */           if (hasTarget)
/*  70 */             return false; 
/*  72 */           hasTarget = true;
/*  73 */         } else if (this.dye.test(itemStack) && itemStack.has(DataComponents.DYE)) {
/*  74 */           hasDyes = true;
/*     */         } else {
/*  76 */           return false;
/*     */         }  
/*     */     } 
/*  80 */     return (hasDyes && hasTarget);
/*     */   }
/*     */   
/*     */   public ItemStack assemble(CraftingInput input) {
/*  85 */     List<DyeColor> dyes = new ArrayList<>();
/*  86 */     ItemStack targetStack = ItemStack.EMPTY;
/*  88 */     for (int slot = 0; slot < input.size(); slot++) {
/*  89 */       ItemStack itemStack = input.getItem(slot);
/*  90 */       if (!itemStack.isEmpty())
/*  94 */         if (this.target.test(itemStack)) {
/*  95 */           if (!targetStack.isEmpty())
/*  96 */             return ItemStack.EMPTY; 
/*  99 */           targetStack = itemStack;
/* 100 */         } else if (this.dye.test(itemStack)) {
/* 101 */           DyeColor dye = (DyeColor)itemStack.getOrDefault(DataComponents.DYE, DyeColor.WHITE);
/* 102 */           dyes.add(dye);
/*     */         } else {
/* 104 */           return ItemStack.EMPTY;
/*     */         }  
/*     */     } 
/* 108 */     if (targetStack.isEmpty() || dyes.isEmpty())
/* 109 */       return ItemStack.EMPTY; 
/* 112 */     DyedItemColor currentDye = (DyedItemColor)targetStack.get(DataComponents.DYED_COLOR);
/* 113 */     DyedItemColor newDyedColor = DyedItemColor.applyDyes(currentDye, dyes);
/* 115 */     ItemStack result = TransmuteRecipe.createWithOriginalComponents(this.result, targetStack);
/* 116 */     result.set(DataComponents.DYED_COLOR, newDyedColor);
/* 117 */     return result;
/*     */   }
/*     */   
/*     */   public RecipeSerializer<DyeRecipe> getSerializer() {
/* 122 */     return SERIALIZER;
/*     */   }
/*     */   
/*     */   protected PlacementInfo createPlacementInfo() {
/* 127 */     return PlacementInfo.create(List.of(this.target, this.dye));
/*     */   }
/*     */   
/*     */   public List<RecipeDisplay> display() {
/* 134 */     SlotDisplay.OnlyWithComponent dyesWithDyeComponent = new SlotDisplay.OnlyWithComponent(this.dye.display(), DataComponents.DYE);
/* 135 */     SlotDisplay targetDisplay = this.target.display();
/* 136 */     return (List)List.of(new ShapelessCraftingRecipeDisplay(
/*     */           
/* 138 */           List.of(targetDisplay, dyesWithDyeComponent), (SlotDisplay)new SlotDisplay.DyedSlotDemo((SlotDisplay)dyesWithDyeComponent, targetDisplay), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\DyeRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */