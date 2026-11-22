/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class FireworkStarFadeRecipe extends CustomRecipe {
/*     */   public static final MapCodec<FireworkStarFadeRecipe> MAP_CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, FireworkStarFadeRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/*  17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Ingredient.CODEC.fieldOf("target").forGetter(()), (App)Ingredient.CODEC.fieldOf("dye").forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(())).apply((Applicative)i, FireworkStarFadeRecipe::new));
/*  23 */     STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, o -> o.target, Ingredient.CONTENTS_STREAM_CODEC, o -> o.dye, ItemStackTemplate.STREAM_CODEC, o -> o.result, FireworkStarFadeRecipe::new);
/*     */   }
/*     */   
/*  30 */   public static final RecipeSerializer<FireworkStarFadeRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   private final Ingredient target;
/*     */   
/*     */   private final Ingredient dye;
/*     */   
/*     */   private final ItemStackTemplate result;
/*     */   
/*     */   public FireworkStarFadeRecipe(Ingredient target, Ingredient dye, ItemStackTemplate result) {
/*  38 */     this.target = target;
/*  39 */     this.dye = dye;
/*  40 */     this.result = result;
/*     */   }
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  46 */     if (input.ingredientCount() < 2)
/*  47 */       return false; 
/*  50 */     boolean hasDye = false;
/*  51 */     boolean hasTarget = false;
/*  53 */     for (int slot = 0; slot < input.size(); slot++) {
/*  54 */       ItemStack itemStack = input.getItem(slot);
/*  55 */       if (!itemStack.isEmpty())
/*  59 */         if (this.dye.test(itemStack) && itemStack.has(DataComponents.DYE)) {
/*  60 */           hasDye = true;
/*  61 */         } else if (this.target.test(itemStack)) {
/*  62 */           if (hasTarget)
/*  63 */             return false; 
/*  65 */           hasTarget = true;
/*     */         } else {
/*  67 */           return false;
/*     */         }  
/*     */     } 
/*  71 */     return (hasTarget && hasDye);
/*     */   }
/*     */   
/*     */   public ItemStack assemble(CraftingInput input) {
/*  76 */     IntArrayList intArrayList = new IntArrayList();
/*  77 */     ItemStack targetStack = null;
/*  79 */     for (int slot = 0; slot < input.size(); slot++) {
/*  80 */       ItemStack itemStack = input.getItem(slot);
/*  82 */       if (this.dye.test(itemStack)) {
/*  83 */         DyeColor dye = (DyeColor)itemStack.getOrDefault(DataComponents.DYE, DyeColor.WHITE);
/*  84 */         intArrayList.add(dye.getFireworkColor());
/*  85 */       } else if (this.target.test(itemStack)) {
/*  86 */         targetStack = itemStack;
/*     */       } 
/*     */     } 
/*  91 */     if (targetStack == null || intArrayList.isEmpty())
/*  92 */       return ItemStack.EMPTY; 
/*  95 */     ItemStack result = TransmuteRecipe.createWithOriginalComponents(this.result, targetStack);
/*  96 */     result.update(DataComponents.FIREWORK_EXPLOSION, FireworkExplosion.DEFAULT, intArrayList, FireworkExplosion::withFadeColors);
/*  97 */     return result;
/*     */   }
/*     */   
/*     */   public RecipeSerializer<FireworkStarFadeRecipe> getSerializer() {
/* 102 */     return SERIALIZER;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\FireworkStarFadeRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */