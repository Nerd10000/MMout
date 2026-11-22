/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class DiscardItem extends LootItemConditionalFunction {
/*    */   public static final MapCodec<DiscardItem> MAP_CODEC;
/*    */   
/*    */   static {
/* 12 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).apply((Applicative)i, DiscardItem::new));
/*    */   }
/*    */   
/*    */   protected DiscardItem(List<LootItemCondition> predicates) {
/* 17 */     super(predicates);
/*    */   }
/*    */   
/*    */   public MapCodec<DiscardItem> codec() {
/* 22 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/* 27 */     return ItemStack.EMPTY;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> discardItem() {
/* 31 */     return simpleBuilder(DiscardItem::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\DiscardItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */