/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SmithingRecipeDisplay;
/*     */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*     */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*     */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*     */ 
/*     */ public class SmithingTrimRecipe extends SimpleSmithingRecipe {
/*     */   public static final MapCodec<SmithingTrimRecipe> MAP_CODEC;
/*     */   
/*     */   public static final StreamCodec<RegistryFriendlyByteBuf, SmithingTrimRecipe> STREAM_CODEC;
/*     */   
/*     */   static {
/*  23 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Recipe.CommonInfo.MAP_CODEC.forGetter(()), (App)Ingredient.CODEC.fieldOf("template").forGetter(()), (App)Ingredient.CODEC.fieldOf("base").forGetter(()), (App)Ingredient.CODEC.fieldOf("addition").forGetter(()), (App)TrimPattern.CODEC.fieldOf("pattern").forGetter(())).apply((Applicative)i, SmithingTrimRecipe::new));
/*  31 */     STREAM_CODEC = StreamCodec.composite(Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo, Ingredient.CONTENTS_STREAM_CODEC, o -> o.template, Ingredient.CONTENTS_STREAM_CODEC, o -> o.base, Ingredient.CONTENTS_STREAM_CODEC, o -> o.addition, TrimPattern.STREAM_CODEC, o -> o.pattern, SmithingTrimRecipe::new);
/*     */   }
/*     */   
/*  40 */   public static final RecipeSerializer<SmithingTrimRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*     */   
/*     */   private final Ingredient template;
/*     */   
/*     */   private final Ingredient base;
/*     */   
/*     */   private final Ingredient addition;
/*     */   
/*     */   private final Holder<TrimPattern> pattern;
/*     */   
/*     */   public SmithingTrimRecipe(Recipe.CommonInfo commonInfo, Ingredient template, Ingredient base, Ingredient addition, Holder<TrimPattern> pattern) {
/*  48 */     super(commonInfo);
/*  49 */     this.template = template;
/*  50 */     this.base = base;
/*  51 */     this.addition = addition;
/*  52 */     this.pattern = pattern;
/*     */   }
/*     */   
/*     */   public ItemStack assemble(SmithingRecipeInput input) {
/*  57 */     return applyTrim(input.base(), input.addition(), this.pattern);
/*     */   }
/*     */   
/*     */   public static ItemStack applyTrim(ItemStack baseItem, ItemStack materialItem, Holder<TrimPattern> pattern) {
/*  61 */     Holder<TrimMaterial> material = (Holder<TrimMaterial>)materialItem.get(DataComponents.PROVIDES_TRIM_MATERIAL);
/*  62 */     if (material != null) {
/*  63 */       ArmorTrim existingTrim = (ArmorTrim)baseItem.get(DataComponents.TRIM);
/*  64 */       ArmorTrim newTrim = new ArmorTrim(material, pattern);
/*  65 */       if (Objects.equals(existingTrim, newTrim))
/*  66 */         return ItemStack.EMPTY; 
/*  68 */       ItemStack trimmedItem = baseItem.copyWithCount(1);
/*  69 */       trimmedItem.set(DataComponents.TRIM, newTrim);
/*  70 */       return trimmedItem;
/*     */     } 
/*  72 */     return ItemStack.EMPTY;
/*     */   }
/*     */   
/*     */   public Optional<Ingredient> templateIngredient() {
/*  77 */     return Optional.of(this.template);
/*     */   }
/*     */   
/*     */   public Ingredient baseIngredient() {
/*  82 */     return this.base;
/*     */   }
/*     */   
/*     */   public Optional<Ingredient> additionIngredient() {
/*  87 */     return Optional.of(this.addition);
/*     */   }
/*     */   
/*     */   public RecipeSerializer<SmithingTrimRecipe> getSerializer() {
/*  92 */     return SERIALIZER;
/*     */   }
/*     */   
/*     */   protected PlacementInfo createPlacementInfo() {
/*  97 */     return PlacementInfo.create(List.of(this.template, this.base, this.addition));
/*     */   }
/*     */   
/*     */   public List<RecipeDisplay> display() {
/* 102 */     SlotDisplay base = this.base.display();
/* 103 */     SlotDisplay material = this.addition.display();
/* 104 */     SlotDisplay template = this.template.display();
/* 106 */     return (List)List.of(new SmithingRecipeDisplay(template, base, material, (SlotDisplay)new SlotDisplay.SmithingTrimDemoSlotDisplay(base, material, this.pattern), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(Items.SMITHING_TABLE)));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\SmithingTrimRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */