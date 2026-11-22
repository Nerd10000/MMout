/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.BannerItem;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ 
/*     */ public class BannerDuplicateRecipe extends CustomRecipe {
/*     */   public static final MapCodec<BannerDuplicateRecipe> MAP_CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, BannerDuplicateRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/*  18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Ingredient.CODEC.fieldOf("banner").forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(())).apply((Applicative)i, BannerDuplicateRecipe::new));
/*  23 */     STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, o -> o.banner, ItemStackTemplate.STREAM_CODEC, o -> o.result, BannerDuplicateRecipe::new);
/*     */   }
/*     */   
/*  29 */   public static final RecipeSerializer<BannerDuplicateRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   private final Ingredient banner;
/*     */   
/*     */   private final ItemStackTemplate result;
/*     */   
/*     */   public BannerDuplicateRecipe(Ingredient banner, ItemStackTemplate result) {
/*  35 */     this.banner = banner;
/*  36 */     this.result = result;
/*     */   }
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  42 */     if (input.ingredientCount() != 2)
/*  43 */       return false; 
/*  46 */     DyeColor color = null;
/*  47 */     boolean hasTarget = false;
/*  48 */     boolean hasSource = false;
/*  50 */     for (int slot = 0; slot < input.size(); slot++) {
/*  51 */       ItemStack itemStack = input.getItem(slot);
/*  52 */       if (!itemStack.isEmpty()) {
/*  55 */         if (this.banner.test(itemStack)) {
/*  55 */           Item item = itemStack.getItem();
/*  55 */           if (item instanceof BannerItem) {
/*  55 */             BannerItem banner = (BannerItem)item;
/*  56 */             if (color == null) {
/*  57 */               color = banner.getColor();
/*  58 */             } else if (color != banner.getColor()) {
/*  59 */               return false;
/*     */             } 
/*     */           } else {
/*  62 */             return false;
/*     */           } 
/*     */         } else {
/*  62 */           return false;
/*     */         } 
/*  65 */         int patternCount = ((BannerPatternLayers)itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)).layers().size();
/*  66 */         if (patternCount > 6)
/*  67 */           return false; 
/*  70 */         if (patternCount > 0) {
/*  71 */           if (hasSource)
/*  72 */             return false; 
/*  74 */           hasSource = true;
/*     */         } else {
/*  77 */           if (hasTarget)
/*  78 */             return false; 
/*  80 */           hasTarget = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*  85 */     return (hasSource && hasTarget);
/*     */   }
/*     */   
/*     */   public ItemStack assemble(CraftingInput input) {
/*  91 */     for (int slot = 0; slot < input.size(); slot++) {
/*  92 */       ItemStack itemStack = input.getItem(slot);
/*  93 */       if (!itemStack.isEmpty()) {
/*  96 */         int patternCount = ((BannerPatternLayers)itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)).layers().size();
/*  97 */         if (patternCount > 0 && patternCount <= 6)
/*  98 */           return TransmuteRecipe.createWithOriginalComponents(this.result, itemStack); 
/*     */       } 
/*     */     } 
/* 102 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
/* 107 */     NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);
/* 109 */     for (int slot = 0; slot < result.size(); slot++) {
/* 110 */       ItemStack itemStack = input.getItem(slot);
/* 111 */       if (!itemStack.isEmpty()) {
/* 112 */         ItemStackTemplate remainder = itemStack.getItem().getCraftingRemainder();
/* 113 */         if (remainder != null) {
/* 114 */           result.set(slot, remainder.create());
/* 115 */         } else if (!((BannerPatternLayers)itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)).layers().isEmpty()) {
/* 116 */           result.set(slot, itemStack.copyWithCount(1));
/*     */         } 
/*     */       } 
/*     */     } 
/* 121 */     return result;
/*     */   }
/*     */   
/*     */   public RecipeSerializer<BannerDuplicateRecipe> getSerializer() {
/* 126 */     return SERIALIZER;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\BannerDuplicateRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */