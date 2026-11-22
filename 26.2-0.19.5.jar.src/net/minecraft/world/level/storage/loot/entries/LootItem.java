/*    */ package net.minecraft.world.level.storage.loot.entries;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class LootItem extends LootPoolSingletonContainer {
/*    */   public static final MapCodec<LootItem> MAP_CODEC;
/*    */   
/*    */   private final Holder<Item> item;
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Item.CODEC.fieldOf("name").forGetter(())).and(singletonFields(i)).apply((Applicative)i, LootItem::new));
/*    */   }
/*    */   
/*    */   private LootItem(Holder<Item> item, int weight, int quality, List<LootItemCondition> conditions, List<LootItemFunction> functions) {
/* 24 */     super(weight, quality, conditions, functions);
/* 25 */     this.item = item;
/*    */   }
/*    */   
/*    */   public MapCodec<LootItem> codec() {
/* 30 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void createItemStack(Consumer<ItemStack> output, LootContext context) {
/* 35 */     output.accept(new ItemStack(this.item));
/*    */   }
/*    */   
/*    */   public static LootPoolSingletonContainer.Builder<?> lootTableItem(ItemLike item) {
/* 39 */     return simpleBuilder((weight, quality, conditions, functions) -> new LootItem((Holder<Item>)item.asItem().builtInRegistryHolder(), weight, quality, conditions, functions));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\LootItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */