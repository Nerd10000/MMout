/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*    */ 
/*    */ public class SetItemCountFunction extends LootItemConditionalFunction {
/*    */   public static final MapCodec<SetItemCountFunction> MAP_CODEC;
/*    */   
/*    */   private final NumberProvider count;
/*    */   
/*    */   private final boolean add;
/*    */   
/*    */   static {
/* 17 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)NumberProviders.CODEC.fieldOf("count").forGetter(()), (App)Codec.BOOL.optionalFieldOf("add", Boolean.valueOf(false)).forGetter(()))).apply((Applicative)i, SetItemCountFunction::new));
/*    */   }
/*    */   
/*    */   private SetItemCountFunction(List<LootItemCondition> predicates, NumberProvider count, boolean add) {
/* 26 */     super(predicates);
/* 27 */     this.count = count;
/* 28 */     this.add = add;
/*    */   }
/*    */   
/*    */   public MapCodec<SetItemCountFunction> codec() {
/* 33 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 38 */     super.validate(context);
/* 40 */     Validatable.validate(context, "count", (Validatable)this.count);
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 45 */     int base = this.add ? itemStack.getCount() : 0;
/* 46 */     itemStack.setCount(base + this.count.getInt(context));
/* 47 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setCount(NumberProvider count) {
/* 51 */     return simpleBuilder(conditions -> new SetItemCountFunction(conditions, count, false));
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> setCount(NumberProvider count, boolean add) {
/* 55 */     return simpleBuilder(conditions -> new SetItemCountFunction(conditions, count, add));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetItemCountFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */