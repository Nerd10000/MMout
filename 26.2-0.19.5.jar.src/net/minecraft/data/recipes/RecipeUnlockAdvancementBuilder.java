/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.advancements.Advancement;
/*    */ import net.minecraft.advancements.AdvancementHolder;
/*    */ import net.minecraft.advancements.AdvancementRequirements;
/*    */ import net.minecraft.advancements.AdvancementRewards;
/*    */ import net.minecraft.advancements.triggers.Criterion;
/*    */ import net.minecraft.advancements.triggers.RecipeUnlockedTrigger;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public class RecipeUnlockAdvancementBuilder {
/* 16 */   private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
/*    */   
/*    */   public void unlockedBy(String name, Criterion<?> criterion) {
/* 19 */     this.criteria.put(name, criterion);
/*    */   }
/*    */   
/*    */   public AdvancementHolder build(RecipeOutput output, ResourceKey<Recipe<?>> id, RecipeCategory category) {
/* 23 */     if (this.criteria.isEmpty())
/* 24 */       throw new IllegalStateException("No way of obtaining recipe " + String.valueOf(id.identifier())); 
/* 30 */     Advancement.Builder advancement = output.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(AdvancementRequirements.Strategy.OR);
/* 31 */     Objects.requireNonNull(advancement);
/* 31 */     this.criteria.forEach(advancement::addCriterion);
/* 33 */     return advancement.build(id.identifier().withPrefix("recipes/" + category.getFolderName() + "/"));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\RecipeUnlockAdvancementBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */