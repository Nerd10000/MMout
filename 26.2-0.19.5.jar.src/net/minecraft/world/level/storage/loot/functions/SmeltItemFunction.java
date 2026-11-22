/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ import net.minecraft.world.item.crafting.RecipeInput;
/*    */ import net.minecraft.world.item.crafting.RecipeType;
/*    */ import net.minecraft.world.item.crafting.SingleRecipeInput;
/*    */ import net.minecraft.world.item.crafting.SmeltingRecipe;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class SmeltItemFunction extends LootItemConditionalFunction {
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final boolean useInputCount;
/*    */   
/*    */   public static final MapCodec<SmeltItemFunction> MAP_CODEC;
/*    */   
/*    */   static {
/* 24 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)Codec.BOOL.optionalFieldOf("use_input_count", Boolean.valueOf(true)).forGetter(())).apply((Applicative)i, SmeltItemFunction::new));
/*    */   }
/*    */   
/*    */   private SmeltItemFunction(List<LootItemCondition> predicates, boolean useInputCount) {
/* 29 */     super(predicates);
/* 30 */     this.useInputCount = useInputCount;
/*    */   }
/*    */   
/*    */   public MapCodec<SmeltItemFunction> codec() {
/* 35 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 40 */     if (itemStack.isEmpty())
/* 41 */       return itemStack; 
/* 44 */     SingleRecipeInput input = new SingleRecipeInput(itemStack);
/* 45 */     Optional<RecipeHolder<SmeltingRecipe>> recipe = context.getLevel().recipeAccess().getRecipeFor(RecipeType.SMELTING, (RecipeInput)input, (Level)context.getLevel());
/* 46 */     if (recipe.isPresent()) {
/* 47 */       ItemStack result = ((SmeltingRecipe)((RecipeHolder)recipe.get()).value()).assemble(input);
/* 49 */       if (!result.isEmpty()) {
/* 50 */         int newCount = (this.useInputCount ? itemStack.count() : 1) * result.getCount();
/* 51 */         return result.copyWithCount(Math.min(newCount, result.getMaxStackSize()));
/*    */       } 
/*    */     } 
/* 55 */     LOGGER.warn("Couldn't smelt {} because there is no smelting recipe", itemStack);
/* 56 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> smelted() {
/* 60 */     return smelted(true);
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> smelted(boolean useInputCount) {
/* 64 */     return simpleBuilder(predicates -> new SmeltItemFunction(predicates, useInputCount));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SmeltItemFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */