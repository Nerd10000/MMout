/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetItemFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetItemFunction> MAP_CODEC;
/*    */   
/*    */   private final Holder<Item> item;
/*    */   
/*    */   static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)Item.CODEC.fieldOf("item").forGetter(())).apply((Applicative)i, SetItemFunction::new));
/*    */   }
/*    */   
/*    */   private SetItemFunction(List<LootItemCondition> predicates, Holder<Item> item) {
/* 21 */     super(predicates);
/* 22 */     this.item = item;
/*    */   }
/*    */   
/*    */   public MapCodec<SetItemFunction> codec() {
/* 27 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 32 */     return itemStack.transmuteCopy((ItemLike)this.item.value());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetItemFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */