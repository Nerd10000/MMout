/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.MapItem;
/*    */ import net.minecraft.world.item.component.MapPostProcessing;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*    */ 
/*    */ public class MapExtendingRecipe extends CustomRecipe {
/*    */   public static final MapCodec<MapExtendingRecipe> MAP_CODEC;
/*    */   
/*    */   public static final StreamCodec<RegistryFriendlyByteBuf, MapExtendingRecipe> STREAM_CODEC;
/*    */   
/*    */   static {
/* 18 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Ingredient.CODEC.fieldOf("map").forGetter(()), (App)Ingredient.CODEC.fieldOf("material").forGetter(()), (App)ItemStackTemplate.CODEC.fieldOf("result").forGetter(())).apply((Applicative)i, MapExtendingRecipe::new));
/* 24 */     STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, o -> o.map, Ingredient.CONTENTS_STREAM_CODEC, o -> o.material, ItemStackTemplate.STREAM_CODEC, o -> o.result, MapExtendingRecipe::new);
/*    */   }
/*    */   
/* 31 */   public static final RecipeSerializer<MapExtendingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   private final ShapedRecipePattern pattern;
/*    */   
/*    */   private final Ingredient map;
/*    */   
/*    */   private final Ingredient material;
/*    */   
/*    */   private final ItemStackTemplate result;
/*    */   
/*    */   public MapExtendingRecipe(Ingredient map, Ingredient material, ItemStackTemplate result) {
/* 40 */     this.map = map;
/* 41 */     this.material = material;
/* 42 */     this.result = result;
/* 44 */     this.pattern = ShapedRecipePattern.of(
/* 45 */         Map.of(
/* 46 */           Character.valueOf('#'), material, 
/* 47 */           Character.valueOf('x'), map), new String[] { "###", "#x#", "###" });
/*    */   }
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 57 */     if (!this.pattern.matches(input))
/* 58 */       return false; 
/* 60 */     ItemStack map = findFilledMap(input);
/* 62 */     if (map.isEmpty())
/* 63 */       return false; 
/* 65 */     MapItemSavedData data = MapItem.getSavedData(map, level);
/* 66 */     if (data == null)
/* 67 */       return false; 
/* 70 */     if (data.isExplorationMap())
/* 71 */       return false; 
/* 74 */     return (data.scale < 4);
/*    */   }
/*    */   
/*    */   public ItemStack assemble(CraftingInput input) {
/* 79 */     ItemStack sourceMap = findFilledMap(input);
/* 81 */     ItemStack map = TransmuteRecipe.createWithOriginalComponents(this.result, sourceMap);
/* 82 */     map.set(DataComponents.MAP_POST_PROCESSING, MapPostProcessing.SCALE);
/* 83 */     return map;
/*    */   }
/*    */   
/*    */   private static ItemStack findFilledMap(CraftingInput input) {
/* 87 */     for (int i = 0; i < input.size(); i++) {
/* 88 */       ItemStack itemStack = input.getItem(i);
/* 89 */       if (itemStack.has(DataComponents.MAP_ID))
/* 90 */         return itemStack; 
/*    */     } 
/* 93 */     return ItemStack.EMPTY;
/*    */   }
/*    */   
/*    */   public RecipeSerializer<MapExtendingRecipe> getSerializer() {
/* 98 */     return SERIALIZER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\MapExtendingRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */