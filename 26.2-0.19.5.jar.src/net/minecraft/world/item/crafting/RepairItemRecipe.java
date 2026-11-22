/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import com.google.common.collect.UnmodifiableIterator;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.tags.EnchantmentTags;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.enchantment.Enchantment;
/*    */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*    */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class RepairItemRecipe extends CustomRecipe {
/* 19 */   public static final RepairItemRecipe INSTANCE = new RepairItemRecipe();
/*    */   
/* 21 */   public static final MapCodec<RepairItemRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
/*    */   
/* 23 */   public static final StreamCodec<RegistryFriendlyByteBuf, RepairItemRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */   
/* 25 */   public static final RecipeSerializer<RepairItemRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);
/*    */   
/*    */   private static Pair<ItemStack, ItemStack> getItemsToCombine(CraftingInput input) {
/* 28 */     if (input.ingredientCount() != 2)
/* 29 */       return null; 
/* 31 */     ItemStack first = null;
/* 32 */     for (int i = 0; i < input.size(); i++) {
/* 33 */       ItemStack itemStack = input.getItem(i);
/* 34 */       if (!itemStack.isEmpty())
/* 37 */         if (first == null) {
/* 38 */           first = itemStack;
/*    */         } else {
/* 40 */           return canCombine(first, itemStack) ? Pair.of(first, itemStack) : null;
/*    */         }  
/*    */     } 
/* 43 */     return null;
/*    */   }
/*    */   
/*    */   private static boolean canCombine(ItemStack first, ItemStack second) {
/* 47 */     return (second.is(first.getItem()) && first
/* 48 */       .getCount() == 1 && second.getCount() == 1 && first
/* 49 */       .has(DataComponents.MAX_DAMAGE) && second.has(DataComponents.MAX_DAMAGE) && first
/* 50 */       .has(DataComponents.DAMAGE) && second.has(DataComponents.DAMAGE));
/*    */   }
/*    */   
/*    */   public boolean matches(CraftingInput input, Level level) {
/* 55 */     return (getItemsToCombine(input) != null);
/*    */   }
/*    */   
/*    */   public ItemStack assemble(CraftingInput input) {
/* 60 */     Pair<ItemStack, ItemStack> itemsToCombine = getItemsToCombine(input);
/* 61 */     if (itemsToCombine == null)
/* 62 */       return ItemStack.EMPTY; 
/* 65 */     ItemStack first = (ItemStack)itemsToCombine.getFirst();
/* 66 */     ItemStack second = (ItemStack)itemsToCombine.getSecond();
/* 69 */     int durability = Math.max(first.getMaxDamage(), second.getMaxDamage());
/* 71 */     int remaining1 = first.getMaxDamage() - first.getDamageValue();
/* 72 */     int remaining2 = second.getMaxDamage() - second.getDamageValue();
/* 73 */     int remaining = remaining1 + remaining2 + durability * 5 / 100;
/* 75 */     ItemStack itemStack = new ItemStack((ItemLike)first.getItem());
/* 76 */     itemStack.set(DataComponents.MAX_DAMAGE, Integer.valueOf(durability));
/* 77 */     itemStack.setDamageValue(Math.max(durability - remaining, 0));
/* 79 */     ItemEnchantments firstEnchants = EnchantmentHelper.getEnchantmentsForCrafting(first);
/* 80 */     ItemEnchantments secondEnchants = EnchantmentHelper.getEnchantmentsForCrafting(second);
/* 83 */     EnchantmentHelper.updateEnchantments(itemStack, newEnchantments -> {
/*    */           UnmodifiableIterator<Holder<Enchantment>> unmodifiableIterator = Sets.union(firstEnchants.keySet(), secondEnchants.keySet()).iterator();
/*    */           while (unmodifiableIterator.hasNext()) {
/*    */             Holder<Enchantment> enchantment = unmodifiableIterator.next();
/*    */             if (enchantment.is(EnchantmentTags.CURSE)) {
/*    */               int enchantLevel = Math.max(firstEnchants.getLevel(enchantment), secondEnchants.getLevel(enchantment));
/*    */               newEnchantments.set(enchantment, enchantLevel);
/*    */             } 
/*    */           } 
/*    */         });
/* 92 */     return itemStack;
/*    */   }
/*    */   
/*    */   public RecipeSerializer<RepairItemRecipe> getSerializer() {
/* 97 */     return SERIALIZER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RepairItemRecipe.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */