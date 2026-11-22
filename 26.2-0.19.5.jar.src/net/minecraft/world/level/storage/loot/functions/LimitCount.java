/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.IntRange;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.Validatable;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class LimitCount extends LootItemConditionalFunction {
/*    */   public static final MapCodec<LimitCount> MAP_CODEC;
/*    */   
/*    */   private final IntRange limit;
/*    */   
/*    */   static {
/* 15 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and((App)IntRange.CODEC.fieldOf("limit").forGetter(())).apply((Applicative)i, LimitCount::new));
/*    */   }
/*    */   
/*    */   private LimitCount(List<LootItemCondition> predicates, IntRange limit) {
/* 22 */     super(predicates);
/* 23 */     this.limit = limit;
/*    */   }
/*    */   
/*    */   public MapCodec<LimitCount> codec() {
/* 28 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 33 */     super.validate(context);
/* 35 */     Validatable.validate(context, "limit", (Validatable)this.limit);
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 40 */     int newCount = this.limit.clamp(context, itemStack.getCount());
/* 41 */     itemStack.setCount(newCount);
/* 42 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> limitCount(IntRange limit) {
/* 46 */     return simpleBuilder(conditions -> new LimitCount(conditions, limit));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\LimitCount.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */