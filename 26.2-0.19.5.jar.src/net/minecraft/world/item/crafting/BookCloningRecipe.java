/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.component.WrittenBookContent;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class BookCloningRecipe extends CustomRecipe {
/*  18 */   public static final MinMaxBounds.Ints ALLOWED_BOOK_GENERATION_RANGES = MinMaxBounds.Ints.between(0, 2);
/*     */   
/*  19 */   public static final MinMaxBounds.Ints DEFAULT_BOOK_GENERATION_RANGES = MinMaxBounds.Ints.between(0, 1);
/*     */   
/*  21 */   private static final Codec<MinMaxBounds.Ints> ALLOWED_GENERATION_CODEC = MinMaxBounds.Ints.CODEC.validate(MinMaxBounds.validateContainedInRange((MinMaxBounds)ALLOWED_BOOK_GENERATION_RANGES));
/*     */   
/*     */   public static final MapCodec<BookCloningRecipe> MAP_CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, BookCloningRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/*  23 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Ingredient.CODEC.fieldOf("source").forGetter(()), (App)Ingredient.CODEC.fieldOf("material").forGetter(()), (App)ALLOWED_GENERATION_CODEC.optionalFieldOf("allowed_generations", DEFAULT_BOOK_GENERATION_RANGES).forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(())).apply((Applicative)i, BookCloningRecipe::new));
/*  30 */     STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, o -> o.source, Ingredient.CONTENTS_STREAM_CODEC, o -> o.material, MinMaxBounds.Ints.STREAM_CODEC, o -> o.allowedGenerations, ItemStackTemplate.STREAM_CODEC, o -> o.result, BookCloningRecipe::new);
/*     */   }
/*     */   
/*  38 */   public static final RecipeSerializer<BookCloningRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   private final Ingredient source;
/*     */   
/*     */   private final Ingredient material;
/*     */   
/*     */   private final MinMaxBounds.Ints allowedGenerations;
/*     */   
/*     */   private final ItemStackTemplate result;
/*     */   
/*     */   public BookCloningRecipe(Ingredient source, Ingredient material, MinMaxBounds.Ints allowedGenerations, ItemStackTemplate result) {
/*  47 */     this.source = source;
/*  48 */     this.material = material;
/*  49 */     this.allowedGenerations = allowedGenerations;
/*  50 */     this.result = result;
/*     */   }
/*     */   
/*     */   private boolean canCraftCopy(WrittenBookContent writtenBookContent) {
/*  54 */     return this.allowedGenerations.matches(writtenBookContent.generation());
/*     */   }
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  60 */     if (input.ingredientCount() < 2)
/*  61 */       return false; 
/*  64 */     boolean hasMaterial = false;
/*  65 */     boolean hasSource = false;
/*  67 */     for (int slot = 0; slot < input.size(); slot++) {
/*  68 */       ItemStack itemStack = input.getItem(slot);
/*  69 */       if (!itemStack.isEmpty())
/*  73 */         if (this.source.test(itemStack)) {
/*  74 */           WrittenBookContent writtenBookContent = (WrittenBookContent)itemStack.get(DataComponents.WRITTEN_BOOK_CONTENT);
/*  75 */           if (writtenBookContent == null || !canCraftCopy(writtenBookContent))
/*  76 */             return false; 
/*  79 */           if (hasSource)
/*  80 */             return false; 
/*  82 */           hasSource = true;
/*  83 */         } else if (this.material.test(itemStack)) {
/*  84 */           hasMaterial = true;
/*     */         } else {
/*  86 */           return false;
/*     */         }  
/*     */     } 
/*  90 */     return (hasSource && hasMaterial);
/*     */   }
/*     */   
/*     */   public ItemStack assemble(CraftingInput input) {
/*  95 */     int count = 0;
/*  96 */     ItemStack source = ItemStack.EMPTY;
/*  98 */     for (int slot = 0; slot < input.size(); slot++) {
/*  99 */       ItemStack itemStack = input.getItem(slot);
/* 100 */       if (!itemStack.isEmpty())
/* 104 */         if (this.source.test(itemStack) && itemStack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
/* 105 */           if (!source.isEmpty())
/* 106 */             return ItemStack.EMPTY; 
/* 108 */           source = itemStack;
/* 109 */         } else if (this.material.test(itemStack)) {
/* 110 */           count++;
/*     */         } else {
/* 112 */           return ItemStack.EMPTY;
/*     */         }  
/*     */     } 
/* 116 */     WrittenBookContent sourceContent = (WrittenBookContent)source.get(DataComponents.WRITTEN_BOOK_CONTENT);
/* 117 */     if (sourceContent == null)
/* 118 */       return ItemStack.EMPTY; 
/* 121 */     WrittenBookContent copiedContent = sourceContent.craftCopy();
/* 122 */     ItemStack result = TransmuteRecipe.createWithOriginalComponents(this.result, source, count - 1);
/* 123 */     result.set(DataComponents.WRITTEN_BOOK_CONTENT, copiedContent);
/* 124 */     return result;
/*     */   }
/*     */   
/*     */   public NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
/* 129 */     NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);
/* 131 */     for (int slot = 0; slot < result.size(); slot++) {
/* 132 */       ItemStack itemStack = input.getItem(slot);
/* 133 */       ItemStackTemplate remainder = itemStack.getItem().getCraftingRemainder();
/* 134 */       if (remainder != null) {
/* 135 */         result.set(slot, remainder.create());
/* 136 */       } else if (itemStack.has(DataComponents.WRITTEN_BOOK_CONTENT)) {
/* 137 */         result.set(slot, itemStack.copyWithCount(1));
/*     */         break;
/*     */       } 
/*     */     } 
/* 142 */     return result;
/*     */   }
/*     */   
/*     */   public RecipeSerializer<BookCloningRecipe> getSerializer() {
/* 147 */     return SERIALIZER;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\BookCloningRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */