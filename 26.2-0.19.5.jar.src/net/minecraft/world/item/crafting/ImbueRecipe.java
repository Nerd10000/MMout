/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class ImbueRecipe extends NormalCraftingRecipe {
/*     */   public static final MapCodec<ImbueRecipe> MAP_CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, ImbueRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/*  19 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Recipe.CommonInfo.MAP_CODEC.forGetter(()), (App)CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(()), (App)Ingredient.CODEC.fieldOf("source").forGetter(()), (App)Ingredient.CODEC.fieldOf("material").forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(())).apply((Applicative)i, ImbueRecipe::new));
/*  27 */     STREAM_CODEC = StreamCodec.composite(Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo, CraftingRecipe.CraftingBookInfo.STREAM_CODEC, o -> o.bookInfo, Ingredient.CONTENTS_STREAM_CODEC, o -> o.source, Ingredient.CONTENTS_STREAM_CODEC, o -> o.material, ItemStackTemplate.STREAM_CODEC, o -> o.result, ImbueRecipe::new);
/*     */   }
/*     */   
/*  36 */   public static final RecipeSerializer<ImbueRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   private final Ingredient source;
/*     */   
/*     */   private final Ingredient material;
/*     */   
/*     */   private final ItemStackTemplate result;
/*     */   
/*     */   public ImbueRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo, Ingredient source, Ingredient material, ItemStackTemplate result) {
/*  43 */     super(commonInfo, bookInfo);
/*  44 */     this.source = source;
/*  45 */     this.material = material;
/*  46 */     this.result = result;
/*     */   }
/*     */   
/*     */   public boolean matches(CraftingInput input, Level level) {
/*  52 */     if (input.width() != 3 || input.height() != 3 || input.ingredientCount() != 9)
/*  53 */       return false; 
/*  56 */     for (int y = 0; y < input.height(); y++) {
/*  57 */       for (int x = 0; x < input.width(); x++) {
/*  58 */         ItemStack itemStack = input.getItem(x, y);
/*  60 */         if (itemStack.isEmpty())
/*  61 */           return false; 
/*  64 */         Ingredient ingredient = (x == 1 && y == 1) ? this.source : this.material;
/*  66 */         if (!ingredient.test(itemStack))
/*  67 */           return false; 
/*     */       } 
/*     */     } 
/*  72 */     return true;
/*     */   }
/*     */   
/*     */   public ItemStack assemble(CraftingInput input) {
/*  77 */     ItemStack source = input.getItem(1, 1);
/*  79 */     ItemStack result = this.result.create();
/*  80 */     result.set(DataComponents.POTION_CONTENTS, source.get(DataComponents.POTION_CONTENTS));
/*  81 */     return result;
/*     */   }
/*     */   
/*     */   public RecipeSerializer<ImbueRecipe> getSerializer() {
/*  86 */     return SERIALIZER;
/*     */   }
/*     */   
/*     */   protected PlacementInfo createPlacementInfo() {
/*  91 */     return PlacementInfo.create(List.of(this.material, this.material, this.material, this.material, this.source, this.material, this.material, this.material, this.material));
/*     */   }
/*     */   
/*     */   public List<RecipeDisplay> display() {
/* 100 */     SlotDisplay material = this.material.display();
/* 101 */     SlotDisplay.WithAnyPotion source = new SlotDisplay.WithAnyPotion(this.source.display());
/* 102 */     return (List)List.of(new ShapedCraftingRecipeDisplay(3, 3, 
/*     */           
/* 105 */           List.of(material, material, material, material, source, material, material, material, material), (SlotDisplay)new SlotDisplay.WithAnyPotion((SlotDisplay)new SlotDisplay.ItemStackSlotDisplay(this.result)), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\ImbueRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */