/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public interface SpawnCondition extends PriorityProvider.SelectorCondition<SpawnContext> {
/*    */   public static final Codec<SpawnCondition> CODEC;
/*    */   
/*    */   MapCodec<? extends SpawnCondition> codec();
/*    */   
/*    */   static {
/* 10 */     CODEC = BuiltInRegistries.SPAWN_CONDITION_TYPE.byNameCodec().dispatch(SpawnCondition::codec, c -> c);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\variant\SpawnCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */