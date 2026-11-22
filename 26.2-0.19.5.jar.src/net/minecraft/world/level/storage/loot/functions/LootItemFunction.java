/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextUser;
/*    */ 
/*    */ public interface LootItemFunction extends LootContextUser, BiFunction<ItemStack, LootContext, ItemStack> {
/*    */   MapCodec<? extends LootItemFunction> codec();
/*    */   
/*    */   static Consumer<ItemStack> decorate(BiFunction<ItemStack, LootContext, ItemStack> function, Consumer<ItemStack> output, LootContext context) {
/* 17 */     return drop -> output.accept(function.apply(drop, context));
/*    */   }
/*    */   
/*    */   public static interface Builder {
/*    */     LootItemFunction build();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\LootItemFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */