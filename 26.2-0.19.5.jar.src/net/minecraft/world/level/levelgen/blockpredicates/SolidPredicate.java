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
/*    */ @Deprecated
/*    */ public class SolidPredicate extends StateTestingPredicate {
/*    */   public static final MapCodec<SolidPredicate> CODEC;
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).apply((Applicative)i, SolidPredicate::new));
/*    */   }
/*    */   
/*    */   public SolidPredicate(Vec3i offset) {
/* 13 */     super(offset);
/*    */   }
/*    */   
/*    */   protected boolean test(BlockState state) {
/* 18 */     return state.isSolid();
/*    */   }
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 23 */     return BlockPredicateType.SOLID;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\SolidPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */