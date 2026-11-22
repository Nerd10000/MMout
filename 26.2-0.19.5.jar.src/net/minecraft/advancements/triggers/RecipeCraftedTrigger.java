/*    */ package net.minecraft.advancements.triggers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.predicates.ContextAwarePredicate;
/*    */ import net.minecraft.advancements.predicates.ItemPredicate;
/*    */ import net.minecraft.advancements.predicates.entity.EntityPredicate;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public class RecipeCraftedTrigger extends SimpleCriterionTrigger<RecipeCraftedTrigger.TriggerInstance> {
/*    */   public Codec<TriggerInstance> codec() {
/* 21 */     return TriggerInstance.CODEC;
/*    */   }
/*    */   
/*    */   public void trigger(ServerPlayer player, ResourceKey<Recipe<?>> id, List<ItemStack> usedIngredients) {
/* 25 */     trigger(player, t -> t.matches(id, usedIngredients));
/*    */   }
/*    */   
/*    */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance {
/*    */     private final Optional<ContextAwarePredicate> player;
/*    */     
/*    */     private final ResourceKey<Recipe<?>> recipeId;
/*    */     
/*    */     private final List<ItemPredicate> ingredients;
/*    */     
/*    */     public static final Codec<TriggerInstance> CODEC;
/*    */     
/*    */     public TriggerInstance(Optional<ContextAwarePredicate> player, ResourceKey<Recipe<?>> recipeId, List<ItemPredicate> ingredients) {
/* 28 */       this.player = player;
/* 28 */       this.recipeId = recipeId;
/* 28 */       this.ingredients = ingredients;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/triggers/RecipeCraftedTrigger$TriggerInstance;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/RecipeCraftedTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/triggers/RecipeCraftedTrigger$TriggerInstance;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/advancements/triggers/RecipeCraftedTrigger$TriggerInstance;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/triggers/RecipeCraftedTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/advancements/triggers/RecipeCraftedTrigger$TriggerInstance;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public Optional<ContextAwarePredicate> player() {
/* 28 */       return this.player;
/*    */     }
/*    */     
/*    */     public ResourceKey<Recipe<?>> recipeId() {
/* 28 */       return this.recipeId;
/*    */     }
/*    */     
/*    */     public List<ItemPredicate> ingredients() {
/* 28 */       return this.ingredients;
/*    */     }
/*    */     
/*    */     static {
/* 33 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)Recipe.KEY_CODEC.fieldOf("recipe_id").forGetter(TriggerInstance::recipeId), (App)ItemPredicate.CODEC.listOf().optionalFieldOf("ingredients", List.of()).forGetter(TriggerInstance::ingredients)).apply((Applicative)i, TriggerInstance::new));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> craftedItem(ResourceKey<Recipe<?>> recipeId, List<ItemPredicate.Builder> predicates) {
/* 40 */       return CriteriaTriggers.RECIPE_CRAFTED.createCriterion(new TriggerInstance(Optional.empty(), recipeId, predicates.stream().map(ItemPredicate.Builder::build).toList()));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> craftedItem(ResourceKey<Recipe<?>> recipeId) {
/* 44 */       return CriteriaTriggers.RECIPE_CRAFTED.createCriterion(new TriggerInstance(Optional.empty(), recipeId, List.of()));
/*    */     }
/*    */     
/*    */     public static Criterion<TriggerInstance> crafterCraftedItem(ResourceKey<Recipe<?>> recipeId) {
/* 48 */       return CriteriaTriggers.CRAFTER_RECIPE_CRAFTED.createCriterion(new TriggerInstance(Optional.empty(), recipeId, List.of()));
/*    */     }
/*    */     
/*    */     private boolean matches(ResourceKey<Recipe<?>> id, List<ItemStack> usedIngredients) {
/* 52 */       if (id != this.recipeId)
/* 53 */         return false; 
/* 56 */       List<ItemStack> remaining = new ArrayList<>(usedIngredients);
/* 57 */       for (ItemPredicate predicate : this.ingredients) {
/* 58 */         boolean found = false;
/* 59 */         for (Iterator<ItemStack> iterator = remaining.iterator(); iterator.hasNext();) {
/* 60 */           if (predicate.test((ItemInstance)iterator.next())) {
/* 61 */             iterator.remove();
/* 62 */             found = true;
/*    */             break;
/*    */           } 
/*    */         } 
/* 66 */         if (!found)
/* 67 */           return false; 
/*    */       } 
/* 70 */       return true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\triggers\RecipeCraftedTrigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */