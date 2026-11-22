/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class EmptyLootItem extends LootPoolSingletonContainer {
/*    */   public static final MapCodec<EmptyLootItem> MAP_CODEC;
/*    */   
/*    */   static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> singletonFields(i).apply((Applicative)i, EmptyLootItem::new));
/*    */   }
/*    */   
/*    */   private EmptyLootItem(int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 17 */     super(weight, quality, conditions, functions);
/*    */   }
/*    */   
/*    */   public MapCodec<EmptyLootItem> codec() {
/* 22 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> output, LootContext context) {}
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> emptyItem() {
/* 30 */     return simpleBuilder(EmptyLootItem::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\EmptyLootItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */