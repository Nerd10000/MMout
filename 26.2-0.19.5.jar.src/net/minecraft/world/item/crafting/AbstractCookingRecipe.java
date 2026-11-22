/*     */ package net.minecraft.world.item.crafting;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.crafting.display.FurnaceRecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.RecipeDisplay;
/*     */ import net.minecraft.world.item.crafting.display.SlotDisplay;
/*     */ 
/*     */ public abstract class AbstractCookingRecipe extends SingleItemRecipe {
/*     */   protected final CookingBookInfo bookInfo;
/*     */   
/*     */   private final float experience;
/*     */   
/*     */   private final int cookingTime;
/*     */   
/*     */   public AbstractCookingRecipe(Recipe.CommonInfo commonInfo, CookingBookInfo bookInfo, Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime) {
/*  23 */     super(commonInfo, ingredient, result);
/*  24 */     this.bookInfo = bookInfo;
/*  25 */     this.experience = experience;
/*  26 */     this.cookingTime = cookingTime;
/*     */   }
/*     */   
/*     */   public float experience() {
/*  36 */     return this.experience;
/*     */   }
/*     */   
/*     */   public int cookingTime() {
/*  40 */     return this.cookingTime;
/*     */   }
/*     */   
/*     */   public CookingBookCategory category() {
/*  44 */     return this.bookInfo.category;
/*     */   }
/*     */   
/*     */   public String group() {
/*  49 */     return this.bookInfo.group;
/*     */   }
/*     */   
/*     */   public List<RecipeDisplay> display() {
/*  56 */     return (List)List.of(new FurnaceRecipeDisplay(
/*  57 */           input().display(), (SlotDisplay)SlotDisplay.AnyFuel.INSTANCE, (SlotDisplay)new SlotDisplay.ItemStackSlotDisplay(
/*     */             
/*  59 */             result()), (SlotDisplay)new SlotDisplay.ItemSlotDisplay(
/*  60 */             furnaceIcon()), this.cookingTime, this.experience));
/*     */   }
/*     */   
/*     */   public static <T extends AbstractCookingRecipe> MapCodec<T> cookingMapCodec(Factory<T> factory, int defaultCookingTime) {
/*  67 */     return RecordCodecBuilder.mapCodec(i -> {
/*     */           Objects.requireNonNull(factory);
/*     */           return i.group((App)Recipe.CommonInfo.MAP_CODEC.forGetter(()), (App)CookingBookInfo.MAP_CODEC.forGetter(()), (App)Ingredient.CODEC.fieldOf("ingredient").forGetter(SingleItemRecipe::input), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(SingleItemRecipe::result), (App)Codec.FLOAT.optionalFieldOf("experience", Float.valueOf(0.0F)).forGetter(AbstractCookingRecipe::experience), (App)Codec.INT.optionalFieldOf("cookingtime", Integer.valueOf(defaultCookingTime)).forGetter(AbstractCookingRecipe::cookingTime)).apply((Applicative)i, factory::create);
/*     */         });
/*     */   }
/*     */   
/*     */   public static <T extends AbstractCookingRecipe> StreamCodec<RegistryFriendlyByteBuf, T> cookingStreamCodec(Factory<T> factory) {
/*  85 */     Objects.requireNonNull(factory);
/*  85 */     return StreamCodec.composite(Recipe.CommonInfo.STREAM_CODEC, o -> o.commonInfo, CookingBookInfo.STREAM_CODEC, o -> o.bookInfo, Ingredient.CONTENTS_STREAM_CODEC, SingleItemRecipe::input, ItemStackTemplate.STREAM_CODEC, SingleItemRecipe::result, ByteBufCodecs.FLOAT, AbstractCookingRecipe::experience, ByteBufCodecs.INT, AbstractCookingRecipe::cookingTime, factory::create);
/*     */   }
/*     */   
/*     */   public abstract RecipeSerializer<? extends AbstractCookingRecipe> getSerializer();
/*     */   
/*     */   public abstract RecipeType<? extends AbstractCookingRecipe> getType();
/*     */   
/*     */   protected abstract Item furnaceIcon();
/*     */   
/*     */   public static final class CookingBookInfo extends Record implements Recipe.BookInfo<CookingBookCategory> {
/*     */     private final CookingBookCategory category;
/*     */     
/*     */     private final String group;
/*     */     
/*     */     public CookingBookInfo(CookingBookCategory category, String group) {
/*  94 */       this.category = category;
/*  94 */       this.group = group;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/AbstractCookingRecipe$CookingBookInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/AbstractCookingRecipe$CookingBookInfo;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/AbstractCookingRecipe$CookingBookInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/crafting/AbstractCookingRecipe$CookingBookInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/AbstractCookingRecipe$CookingBookInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/crafting/AbstractCookingRecipe$CookingBookInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public CookingBookCategory category() {
/*  94 */       return this.category;
/*     */     }
/*     */     
/*     */     public String group() {
/*  94 */       return this.group;
/*     */     }
/*     */     
/*  98 */     public static final MapCodec<CookingBookInfo> MAP_CODEC = Recipe.BookInfo.mapCodec(CookingBookCategory.CODEC, CookingBookCategory.MISC, CookingBookInfo::new);
/*     */     
/* 100 */     public static final StreamCodec<RegistryFriendlyByteBuf, CookingBookInfo> STREAM_CODEC = Recipe.BookInfo.streamCodec((StreamCodec)CookingBookCategory.STREAM_CODEC, CookingBookInfo::new);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Factory<T extends AbstractCookingRecipe> {
/*     */     T create(Recipe.CommonInfo param1CommonInfo, AbstractCookingRecipe.CookingBookInfo param1CookingBookInfo, Ingredient param1Ingredient, ItemStackTemplate param1ItemStackTemplate, float param1Float, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\AbstractCookingRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */