/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public final class ShapelessCraftingRecipeDisplay extends Record implements RecipeDisplay {
/*    */   private final List<SlotDisplay> ingredients;
/*    */   
/*    */   private final SlotDisplay result;
/*    */   
/*    */   private final SlotDisplay craftingStation;
/*    */   
/*    */   public static final MapCodec<ShapelessCraftingRecipeDisplay> MAP_CODEC;
/*    */   
/*    */   public ShapelessCraftingRecipeDisplay(List<SlotDisplay> ingredients, SlotDisplay result, SlotDisplay craftingStation) {
/* 12 */     this.ingredients = ingredients;
/* 12 */     this.result = result;
/* 12 */     this.craftingStation = craftingStation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/ShapelessCraftingRecipeDisplay;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public List<SlotDisplay> ingredients() {
/* 12 */     return this.ingredients;
/*    */   }
/*    */   
/*    */   public SlotDisplay result() {
/* 12 */     return this.result;
/*    */   }
/*    */   
/*    */   public SlotDisplay craftingStation() {
/* 12 */     return this.craftingStation;
/*    */   }
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(ShapelessCraftingRecipeDisplay::ingredients), (App)SlotDisplay.CODEC.fieldOf("result").forGetter(ShapelessCraftingRecipeDisplay::result), (App)SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(ShapelessCraftingRecipeDisplay::craftingStation)).apply((Applicative)i, ShapelessCraftingRecipeDisplay::new));
/*    */   }
/*    */   
/* 23 */   public static final StreamCodec<RegistryFriendlyByteBuf, ShapelessCraftingRecipeDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC
/* 24 */       .apply(ByteBufCodecs.list()), ShapelessCraftingRecipeDisplay::ingredients, SlotDisplay.STREAM_CODEC, ShapelessCraftingRecipeDisplay::result, SlotDisplay.STREAM_CODEC, ShapelessCraftingRecipeDisplay::craftingStation, ShapelessCraftingRecipeDisplay::new);
/*    */   
/* 30 */   public static final RecipeDisplay.Type<ShapelessCraftingRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   public RecipeDisplay.Type<ShapelessCraftingRecipeDisplay> type() {
/* 34 */     return TYPE;
/*    */   }
/*    */   
/*    */   public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 39 */     return (this.ingredients.stream().allMatch(e -> e.isEnabled(enabledFeatures)) && super.isEnabled(enabledFeatures));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\ShapelessCraftingRecipeDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */