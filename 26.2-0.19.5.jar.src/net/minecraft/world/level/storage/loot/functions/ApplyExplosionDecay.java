/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ApplyExplosionDecay extends LootItemConditionalFunction {
/*    */   public static final MapCodec<ApplyExplosionDecay> MAP_CODEC;
/*    */   
/*    */   static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).apply((Applicative)i, ApplyExplosionDecay::new));
/*    */   }
/*    */   
/*    */   private ApplyExplosionDecay(List<LootItemCondition> predicates) {
/* 17 */     super(predicates);
/*    */   }
/*    */   
/*    */   public MapCodec<ApplyExplosionDecay> codec() {
/* 22 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 27 */     Float explosionRadius = (Float)context.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS);
/* 29 */     if (explosionRadius != null) {
/* 30 */       RandomSource random = context.getRandom();
/* 32 */       float probability = 1.0F / explosionRadius.floatValue();
/* 33 */       int currentCount = itemStack.getCount();
/* 34 */       int resultCount = 0;
/* 35 */       for (int i = 0; i < currentCount; i++) {
/* 36 */         if (random.nextFloat() <= probability)
/* 37 */           resultCount++; 
/*    */       } 
/* 41 */       itemStack.setCount(resultCount);
/*    */     } 
/* 43 */     return itemStack;
/*    */   }
/*    */   
/*    */   public static LootItemConditionalFunction.Builder<?> explosionDecay() {
/* 47 */     return simpleBuilder(ApplyExplosionDecay::new);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ApplyExplosionDecay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */