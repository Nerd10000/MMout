/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.block.state.StateHolder;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ interface ValueMatcher {
/*    */   public static final Codec<ValueMatcher> CODEC;
/*    */   
/*    */   public static final StreamCodec<ByteBuf, ValueMatcher> STREAM_CODEC;
/*    */   
/*    */   static {
/* 50 */     CODEC = Codec.either(StatePropertiesPredicate.ExactMatcher.CODEC, StatePropertiesPredicate.RangedMatcher.CODEC).xmap(Either::unwrap, matcher -> {
/*    */           if (matcher instanceof StatePropertiesPredicate.ExactMatcher) {
/*    */             StatePropertiesPredicate.ExactMatcher exact = (StatePropertiesPredicate.ExactMatcher)matcher;
/*    */             return Either.left(exact);
/*    */           } 
/*    */           if (matcher instanceof StatePropertiesPredicate.RangedMatcher) {
/*    */             StatePropertiesPredicate.RangedMatcher ranged = (StatePropertiesPredicate.RangedMatcher)matcher;
/*    */             return Either.right(ranged);
/*    */           } 
/*    */           throw new UnsupportedOperationException();
/*    */         });
/* 61 */     STREAM_CODEC = ByteBufCodecs.either(StatePropertiesPredicate.ExactMatcher.STREAM_CODEC, StatePropertiesPredicate.RangedMatcher.STREAM_CODEC).map(Either::unwrap, matcher -> {
/*    */           if (matcher instanceof StatePropertiesPredicate.ExactMatcher) {
/*    */             StatePropertiesPredicate.ExactMatcher exact = (StatePropertiesPredicate.ExactMatcher)matcher;
/*    */             return Either.left(exact);
/*    */           } 
/*    */           if (matcher instanceof StatePropertiesPredicate.RangedMatcher) {
/*    */             StatePropertiesPredicate.RangedMatcher ranged = (StatePropertiesPredicate.RangedMatcher)matcher;
/*    */             return Either.right(ranged);
/*    */           } 
/*    */           throw new UnsupportedOperationException();
/*    */         });
/*    */   }
/*    */   
/*    */   <T extends Comparable<T>> boolean match(StateHolder<?, ?> paramStateHolder, Property<T> paramProperty);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\StatePropertiesPredicate$ValueMatcher.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */