/*    */ package net.minecraft.client.renderer.block.dispatch.multipart;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface Condition {
/*    */   public static final Codec<Condition> CODEC;
/*    */   
/*    */   static {
/* 16 */     CODEC = Codec.recursive("condition", self -> {
/*    */           Codec<CombinedCondition> combinerCodec = Codec.simpleMap(CombinedCondition.Operation.CODEC, self.listOf(), StringRepresentable.keys((StringRepresentable[])CombinedCondition.Operation.values())).codec().comapFlatMap((), ());
/*    */           return Codec.either(combinerCodec, KeyValueCondition.CODEC).flatComapMap((), ());
/*    */         });
/*    */   }
/*    */   
/*    */   <O, S extends net.minecraft.world.level.block.state.StateHolder<O, S>> Predicate<S> instantiate(StateDefinition<O, S> paramStateDefinition);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\dispatch\multipart\Condition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */