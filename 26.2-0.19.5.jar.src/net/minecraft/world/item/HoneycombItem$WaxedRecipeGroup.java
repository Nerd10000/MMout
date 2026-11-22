/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.data.recipes.RecipeCategory;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.WeatheringCopperCollection;
/*    */ 
/*    */ final class WaxedRecipeGroup extends Record {
/*    */   private final WeatheringCopperCollection<Block> block;
/*    */   
/*    */   private final Function<Block, Pair<RecipeCategory, String>> recipeIdProvider;
/*    */   
/*    */   private WaxedRecipeGroup(WeatheringCopperCollection<Block> block, Function<Block, Pair<RecipeCategory, String>> recipeIdProvider) {
/* 48 */     this.block = block;
/* 48 */     this.recipeIdProvider = recipeIdProvider;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/HoneycombItem$WaxedRecipeGroup;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/HoneycombItem$WaxedRecipeGroup;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/HoneycombItem$WaxedRecipeGroup;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/item/HoneycombItem$WaxedRecipeGroup;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/HoneycombItem$WaxedRecipeGroup;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/item/HoneycombItem$WaxedRecipeGroup;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public WeatheringCopperCollection<Block> block() {
/* 48 */     return this.block;
/*    */   }
/*    */   
/*    */   public Function<Block, Pair<RecipeCategory, String>> recipeIdProvider() {
/* 48 */     return this.recipeIdProvider;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\HoneycombItem$WaxedRecipeGroup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */