/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.RegistryCodecs;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MatchingBlocksPredicate extends StateTestingPredicate {
/*    */   public static final MapCodec<MatchingBlocksPredicate> CODEC;
/*    */   
/*    */   private final HolderSet<Block> blocks;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).and((App)RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("blocks").forGetter(())).apply((Applicative)i, MatchingBlocksPredicate::new));
/*    */   }
/*    */   
/*    */   public MatchingBlocksPredicate(Vec3i offset, HolderSet<Block> blocks) {
/* 20 */     super(offset);
/* 21 */     this.blocks = blocks;
/*    */   }
/*    */   
/*    */   protected boolean test(BlockState state) {
/* 26 */     return state.is(this.blocks);
/*    */   }
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 31 */     return BlockPredicateType.MATCHING_BLOCKS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\MatchingBlocksPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */