/*      */ package net.minecraft.data.recipes.packs;
/*      */ 
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.crafting.Recipe;
/*      */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*      */ 
/*      */ public final class TrimTemplate extends Record {
/*      */   private final Item template;
/*      */   
/*      */   private final ResourceKey<TrimPattern> patternId;
/*      */   
/*      */   private final ResourceKey<Recipe<?>> recipeId;
/*      */   
/*      */   public TrimTemplate(Item template, ResourceKey<TrimPattern> patternId, ResourceKey<Recipe<?>> recipeId) {
/* 2810 */     this.template = template;
/* 2810 */     this.patternId = patternId;
/* 2810 */     this.recipeId = recipeId;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2810	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2810	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2810	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/data/recipes/packs/VanillaRecipeProvider$TrimTemplate;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public Item template() {
/* 2810 */     return this.template;
/*      */   }
/*      */   
/*      */   public ResourceKey<TrimPattern> patternId() {
/* 2810 */     return this.patternId;
/*      */   }
/*      */   
/*      */   public ResourceKey<Recipe<?>> recipeId() {
/* 2810 */     return this.recipeId;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\packs\VanillaRecipeProvider$TrimTemplate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */