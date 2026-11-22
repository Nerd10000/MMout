/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public final class ShapedCraftingRecipeDisplay extends Record implements RecipeDisplay {
/*    */   private final int width;
/*    */   
/*    */   private final int height;
/*    */   
/*    */   private final List<SlotDisplay> ingredients;
/*    */   
/*    */   private final SlotDisplay result;
/*    */   
/*    */   private final SlotDisplay craftingStation;
/*    */   
/*    */   public static final MapCodec<ShapedCraftingRecipeDisplay> MAP_CODEC;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/ShapedCraftingRecipeDisplay;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int width() {
/* 13 */     return this.width;
/*    */   }
/*    */   
/*    */   public int height() {
/* 13 */     return this.height;
/*    */   }
/*    */   
/*    */   public List<SlotDisplay> ingredients() {
/* 13 */     return this.ingredients;
/*    */   }
/*    */   
/*    */   public SlotDisplay result() {
/* 13 */     return this.result;
/*    */   }
/*    */   
/*    */   public SlotDisplay craftingStation() {
/* 13 */     return this.craftingStation;
/*    */   }
/*    */   
/*    */   static {
/* 20 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.INT.fieldOf("width").forGetter(ShapedCraftingRecipeDisplay::width), (App)Codec.INT.fieldOf("height").forGetter(ShapedCraftingRecipeDisplay::height), (App)SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(ShapedCraftingRecipeDisplay::ingredients), (App)SlotDisplay.CODEC.fieldOf("result").forGetter(ShapedCraftingRecipeDisplay::result), (App)SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(ShapedCraftingRecipeDisplay::craftingStation)).apply((Applicative)i, ShapedCraftingRecipeDisplay::new));
/*    */   }
/*    */   
/* 28 */   public static final StreamCodec<RegistryFriendlyByteBuf, ShapedCraftingRecipeDisplay> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ShapedCraftingRecipeDisplay::width, ByteBufCodecs.VAR_INT, ShapedCraftingRecipeDisplay::height, SlotDisplay.STREAM_CODEC
/*    */       
/* 31 */       .apply(ByteBufCodecs.list()), ShapedCraftingRecipeDisplay::ingredients, SlotDisplay.STREAM_CODEC, ShapedCraftingRecipeDisplay::result, SlotDisplay.STREAM_CODEC, ShapedCraftingRecipeDisplay::craftingStation, ShapedCraftingRecipeDisplay::new);
/*    */   
/* 37 */   public static final RecipeDisplay.Type<ShapedCraftingRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   public ShapedCraftingRecipeDisplay(int width, int height, List<SlotDisplay> ingredients, SlotDisplay result, SlotDisplay craftingStation) {
/* 40 */     if (ingredients.size() != width * height)
/* 41 */       throw new IllegalArgumentException("Invalid shaped recipe display contents"); 
/*    */     this.width = width;
/*    */     this.height = height;
/*    */     this.ingredients = ingredients;
/*    */     this.result = result;
/*    */     this.craftingStation = craftingStation;
/*    */   }
/*    */   
/*    */   public RecipeDisplay.Type<ShapedCraftingRecipeDisplay> type() {
/* 47 */     return TYPE;
/*    */   }
/*    */   
/*    */   public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 52 */     return (this.ingredients.stream().allMatch(e -> e.isEnabled(enabledFeatures)) && super.isEnabled(enabledFeatures));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\ShapedCraftingRecipeDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */