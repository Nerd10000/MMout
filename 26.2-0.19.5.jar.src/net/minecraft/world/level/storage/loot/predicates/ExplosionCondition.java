/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class ExplosionCondition implements LootItemCondition {
/* 12 */   private static final ExplosionCondition INSTANCE = new ExplosionCondition();
/*    */   
/* 13 */   public static final MapCodec<ExplosionCondition> MAP_CODEC = MapCodec.unit(INSTANCE);
/*    */   
/*    */   public MapCodec<ExplosionCondition> codec() {
/* 20 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 25 */     return Set.of(LootContextParams.EXPLOSION_RADIUS);
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 30 */     Float explosionRadius = (Float)context.getOptionalParameter(LootContextParams.EXPLOSION_RADIUS);
/* 31 */     if (explosionRadius != null) {
/* 32 */       RandomSource random = context.getRandom();
/* 33 */       float probability = 1.0F / explosionRadius.floatValue();
/* 34 */       return (random.nextFloat() <= probability);
/*    */     } 
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder survivesExplosion() {
/* 41 */     return () -> INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\ExplosionCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */