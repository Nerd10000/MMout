/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class StonecutterRecipeDisplay extends Record implements RecipeDisplay {
/*    */   private final SlotDisplay input;
/*    */   
/*    */   private final SlotDisplay result;
/*    */   
/*    */   private final SlotDisplay craftingStation;
/*    */   
/*    */   public static final MapCodec<StonecutterRecipeDisplay> MAP_CODEC;
/*    */   
/*    */   public StonecutterRecipeDisplay(SlotDisplay input, SlotDisplay result, SlotDisplay craftingStation) {
/*  8 */     this.input = input;
/*  8 */     this.result = result;
/*  8 */     this.craftingStation = craftingStation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/StonecutterRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/StonecutterRecipeDisplay;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/StonecutterRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/StonecutterRecipeDisplay;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/StonecutterRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/StonecutterRecipeDisplay;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public SlotDisplay input() {
/*  8 */     return this.input;
/*    */   }
/*    */   
/*    */   public SlotDisplay result() {
/*  8 */     return this.result;
/*    */   }
/*    */   
/*    */   public SlotDisplay craftingStation() {
/*  8 */     return this.craftingStation;
/*    */   }
/*    */   
/*    */   static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("input").forGetter(StonecutterRecipeDisplay::input), (App)SlotDisplay.CODEC.fieldOf("result").forGetter(StonecutterRecipeDisplay::result), (App)SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(StonecutterRecipeDisplay::craftingStation)).apply((Applicative)i, StonecutterRecipeDisplay::new));
/*    */   }
/*    */   
/* 20 */   public static final StreamCodec<RegistryFriendlyByteBuf, StonecutterRecipeDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, StonecutterRecipeDisplay::input, SlotDisplay.STREAM_CODEC, StonecutterRecipeDisplay::result, SlotDisplay.STREAM_CODEC, StonecutterRecipeDisplay::craftingStation, StonecutterRecipeDisplay::new);
/*    */   
/* 27 */   public static final RecipeDisplay.Type<StonecutterRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   public RecipeDisplay.Type<StonecutterRecipeDisplay> type() {
/* 31 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\StonecutterRecipeDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */