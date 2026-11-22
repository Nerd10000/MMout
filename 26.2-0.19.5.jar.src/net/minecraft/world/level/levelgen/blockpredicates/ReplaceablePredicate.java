/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class ReplaceablePredicate extends StateTestingPredicate {
/*    */   public static final MapCodec<ReplaceablePredicate> CODEC;
/*    */   
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).apply((Applicative)i, ReplaceablePredicate::new));
/*    */   }
/*    */   
/*    */   public ReplaceablePredicate(Vec3i offset) {
/* 12 */     super(offset);
/*    */   }
/*    */   
/*    */   protected boolean test(BlockState state) {
/* 17 */     return state.canBeReplaced();
/*    */   }
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 22 */     return BlockPredicateType.REPLACEABLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\ReplaceablePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */