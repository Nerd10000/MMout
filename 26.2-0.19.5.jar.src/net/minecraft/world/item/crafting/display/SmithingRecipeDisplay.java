/*    */ package net.minecraft.world.item.crafting.display;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class SmithingRecipeDisplay extends Record implements RecipeDisplay {
/*    */   private final SlotDisplay template;
/*    */   
/*    */   private final SlotDisplay base;
/*    */   
/*    */   private final SlotDisplay addition;
/*    */   
/*    */   private final SlotDisplay result;
/*    */   
/*    */   private final SlotDisplay craftingStation;
/*    */   
/*    */   public static final MapCodec<SmithingRecipeDisplay> MAP_CODEC;
/*    */   
/*    */   public SmithingRecipeDisplay(SlotDisplay template, SlotDisplay base, SlotDisplay addition, SlotDisplay result, SlotDisplay craftingStation) {
/*  8 */     this.template = template;
/*  8 */     this.base = base;
/*  8 */     this.addition = addition;
/*  8 */     this.result = result;
/*  8 */     this.craftingStation = craftingStation;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/crafting/display/SmithingRecipeDisplay;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public SlotDisplay template() {
/*  8 */     return this.template;
/*    */   }
/*    */   
/*    */   public SlotDisplay base() {
/*  8 */     return this.base;
/*    */   }
/*    */   
/*    */   public SlotDisplay addition() {
/*  8 */     return this.addition;
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
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SlotDisplay.CODEC.fieldOf("template").forGetter(SmithingRecipeDisplay::template), (App)SlotDisplay.CODEC.fieldOf("base").forGetter(SmithingRecipeDisplay::base), (App)SlotDisplay.CODEC.fieldOf("addition").forGetter(SmithingRecipeDisplay::addition), (App)SlotDisplay.CODEC.fieldOf("result").forGetter(SmithingRecipeDisplay::result), (App)SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(SmithingRecipeDisplay::craftingStation)).apply((Applicative)i, SmithingRecipeDisplay::new));
/*    */   }
/*    */   
/* 25 */   public static final StreamCodec<RegistryFriendlyByteBuf, SmithingRecipeDisplay> STREAM_CODEC = StreamCodec.composite(SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::template, SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::base, SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::addition, SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::result, SlotDisplay.STREAM_CODEC, SmithingRecipeDisplay::craftingStation, SmithingRecipeDisplay::new);
/*    */   
/* 34 */   public static final RecipeDisplay.Type<SmithingRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   public RecipeDisplay.Type<SmithingRecipeDisplay> type() {
/* 38 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\display\SmithingRecipeDisplay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */