/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.advancements.triggers.Criterion;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public class SpecialRecipeBuilder {
/*    */   private RecipeUnlockAdvancementBuilder advancementBuilder;
/*    */   
/*    */   private final Supplier<Recipe<?>> factory;
/*    */   
/*    */   public SpecialRecipeBuilder(Supplier<Recipe<?>> factory) {
/* 19 */     this.factory = factory;
/*    */   }
/*    */   
/*    */   public static SpecialRecipeBuilder special(Supplier<Recipe<?>> factory) {
/* 23 */     return new SpecialRecipeBuilder(factory);
/*    */   }
/*    */   
/*    */   public SpecialRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/* 27 */     if (this.advancementBuilder == null)
/* 28 */       this.advancementBuilder = new RecipeUnlockAdvancementBuilder(); 
/* 30 */     this.advancementBuilder.unlockedBy(name, criterion);
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, String name) {
/* 35 */     save(output, ResourceKey.create(Registries.RECIPE, Identifier.parse(name)));
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/*    */     AdvancementHolder advancement;
/* 40 */     if (this.advancementBuilder != null) {
/* 41 */       advancement = this.advancementBuilder.build(output, id, RecipeCategory.MISC);
/*    */     } else {
/* 43 */       advancement = null;
/*    */     } 
/* 46 */     output.accept(id, this.factory.get(), advancement);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\SpecialRecipeBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */