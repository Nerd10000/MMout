/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function6;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.flag.FeatureFlagSet;
/*    */ 
/*    */ public final class FurnaceRecipeDisplay extends Record implements RecipeDisplay {
/*    */   private final SlotDisplay ingredient;
/*    */   
/*    */   private final SlotDisplay fuel;
/*    */   
/*    */   private final SlotDisplay result;
/*    */   
/*    */   private final SlotDisplay craftingStation;
/*    */   
/*    */   private final int duration;
/*    */   
/*    */   private final float experience;
/*    */   
/*    */   public static final MapCodec<FurnaceRecipeDisplay> MAP_CODEC;
/*    */   
/*    */   public FurnaceRecipeDisplay(SlotDisplay ingredient, SlotDisplay fuel, SlotDisplay result, SlotDisplay craftingStation, int duration, float experience) {
/* 11 */     this.ingredient = ingredient;
/* 11 */     this.fuel = fuel;
/* 11 */     this.result = result;
/* 11 */     this.craftingStation = craftingStation;
/* 11 */     this.duration = duration;
/* 11 */     this.experience = experience;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/FurnaceRecipeDisplay;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public SlotDisplay ingredient() {
/* 11 */     return this.ingredient;
/*    */   }
/*    */   
/*    */   public SlotDisplay fuel() {
/* 11 */     return this.fuel;
/*    */   }
/*    */   
/*    */   public SlotDisplay result() {
/* 11 */     return this.result;
/*    */   }
/*    */   
/*    */   public SlotDisplay craftingStation() {
/* 11 */     return this.craftingStation;
/*    */   }
/*    */   
/*    */   public int duration() {
/* 11 */     return this.duration;
/*    */   }
/*    */   
/*    */   public float experience() {
/* 11 */     return this.experience;
/*    */   }
/*    */   
/*    */   static {
/* 19 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("ingredient").forGetter(FurnaceRecipeDisplay::ingredient), (App)SlotDisplay.CODEC.fieldOf("fuel").forGetter(FurnaceRecipeDisplay::fuel), (App)SlotDisplay.CODEC.fieldOf("result").forGetter(FurnaceRecipeDisplay::result), (App)SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(FurnaceRecipeDisplay::craftingStation), (App)Codec.INT.fieldOf("duration").forGetter(FurnaceRecipeDisplay::duration), (App)Codec.FLOAT.fieldOf("experience").forGetter(FurnaceRecipeDisplay::experience)).apply((Applicative)i, FurnaceRecipeDisplay::new));
/*    */   }
/*    */   
/* 28 */   public static final StreamCodec<RegistryFriendlyByteBuf, FurnaceRecipeDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, FurnaceRecipeDisplay::ingredient, SlotDisplay.STREAM_CODEC, FurnaceRecipeDisplay::fuel, SlotDisplay.STREAM_CODEC, FurnaceRecipeDisplay::result, SlotDisplay.STREAM_CODEC, FurnaceRecipeDisplay::craftingStation, ByteBufCodecs.VAR_INT, FurnaceRecipeDisplay::duration, ByteBufCodecs.FLOAT, FurnaceRecipeDisplay::experience, FurnaceRecipeDisplay::new);
/*    */   
/* 38 */   public static final RecipeDisplay.Type<FurnaceRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   public RecipeDisplay.Type<FurnaceRecipeDisplay> type() {
/* 42 */     return TYPE;
/*    */   }
/*    */   
/*    */   public boolean isEnabled(FeatureFlagSet enabledFeatures) {
/* 47 */     return (this.ingredient.isEnabled(enabledFeatures) && fuel().isEnabled(enabledFeatures) && super.isEnabled(enabledFeatures));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\FurnaceRecipeDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */