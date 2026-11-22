/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
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
/*     */ public class ShieldDecorationRecipe extends CustomRecipe {
/*     */   public static final MapCodec<ShieldDecorationRecipe> MAP_CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, ShieldDecorationRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/*  16 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Ingredient.CODEC.fieldOf("banner").forGetter(()), (App)Ingredient.CODEC.fieldOf("target").forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(())).apply((Applicative)i, ShieldDecorationRecipe::new));
/*  22 */     STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, o -> o.banner, Ingredient.CONTENTS_STREAM_CODEC, o -> o.target, ItemStackTemplate.STREAM_CODEC, o -> o.result, ShieldDecorationRecipe::new);
/*     */   }
/*     */   
/*  29 */   public static final RecipeSerializer<ShieldDecorationRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   private final Ingredient banner;
/*     */   
/*     */   private final Ingredient target;
/*     */   
/*     */   private final ItemStackTemplate result;
/*     */   
/*     */   public ShieldDecorationRecipe(Ingredient banner, Ingredient target, ItemStackTemplate result) {
/*  37 */     this.banner = banner;
/*  38 */     this.target = target;
/*  39 */     this.result = result;
/*     */   }
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  45 */     if (input.ingredientCount() != 2)
/*  46 */       return false; 
/*  49 */     boolean hasClearTarget = false;
/*  50 */     boolean hasPatternBanner = false;
/*  52 */     for (int slot = 0; slot < input.size(); slot++) {
/*  53 */       ItemStack itemStack = input.getItem(slot);
/*  54 */       if (!itemStack.isEmpty())
/*  58 */         if (this.banner.test(itemStack) && itemStack.getItem() instanceof BannerItem) {
/*  59 */           if (hasPatternBanner)
/*  60 */             return false; 
/*  62 */           hasPatternBanner = true;
/*  63 */         } else if (this.target.test(itemStack)) {
/*  64 */           if (hasClearTarget)
/*  65 */             return false; 
/*  67 */           BannerPatternLayers patterns = (BannerPatternLayers)itemStack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
/*  68 */           if (!patterns.layers().isEmpty())
/*  69 */             return false; 
/*  71 */           hasClearTarget = true;
/*     */         } else {
/*  73 */           return false;
/*     */         }  
/*     */     } 
/*  77 */     return (hasClearTarget && hasPatternBanner);
/*     */   }
/*     */   
/*     */   public ItemStack assemble(CraftingInput input) {
/*  82 */     BannerPatternLayers patterns = null;
/*  83 */     DyeColor baseColor = DyeColor.WHITE;
/*  84 */     ItemStack target = ItemStack.EMPTY;
/*  86 */     for (int slot = 0; slot < input.size(); slot++) {
/*  87 */       ItemStack itemStack = input.getItem(slot);
/*  88 */       if (itemStack.isEmpty())
/*     */         continue; 
/*  92 */       if (this.banner.test(itemStack)) {
/*  92 */         Item item = itemStack.getItem();
/*  92 */         if (item instanceof BannerItem) {
/*  92 */           BannerItem bannerItem = (BannerItem)item;
/*  93 */           patterns = (BannerPatternLayers)itemStack.get(DataComponents.BANNER_PATTERNS);
/*  94 */           baseColor = bannerItem.getColor();
/*     */           continue;
/*     */         } 
/*     */       } 
/*  95 */       if (this.target.test(itemStack))
/*  96 */         target = itemStack; 
/*     */       continue;
/*     */     } 
/* 100 */     ItemStack result = TransmuteRecipe.createWithOriginalComponents(this.result, target);
/* 101 */     result.set(DataComponents.BANNER_PATTERNS, patterns);
/* 102 */     result.set(DataComponents.BASE_COLOR, baseColor);
/* 104 */     return result;
/*     */   }
/*     */   
/*     */   public RecipeSerializer<ShieldDecorationRecipe> getSerializer() {
/* 109 */     return SERIALIZER;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\ShieldDecorationRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */