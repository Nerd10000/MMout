/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*    */ 
/*    */ public class LootItemKilledByPlayerCondition implements LootItemCondition {
/* 11 */   private static final LootItemKilledByPlayerCondition INSTANCE = new LootItemKilledByPlayerCondition();
/*    */   
/* 12 */   public static final MapCodec<LootItemKilledByPlayerCondition> MAP_CODEC = MapCodec.unit(INSTANCE);
/*    */   
/*    */   public MapCodec<LootItemKilledByPlayerCondition> codec() {
/* 19 */     return MAP_CODEC;
/*    */   }
/*    */   
/*    */   public Set<ContextKey<?>> getReferencedContextParams() {
/* 24 */     return Set.of(LootContextParams.LAST_DAMAGE_PLAYER);
/*    */   }
/*    */   
/*    */   public boolean test(LootContext context) {
/* 29 */     return context.hasParameter(LootContextParams.LAST_DAMAGE_PLAYER);
/*    */   }
/*    */   
/*    */   public static LootItemCondition.Builder killedByPlayer() {
/* 33 */     return () -> INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemKilledByPlayerCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */