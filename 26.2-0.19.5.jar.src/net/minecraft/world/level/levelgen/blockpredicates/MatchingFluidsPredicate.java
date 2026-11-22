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
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ public class MatchingFluidsPredicate extends StateTestingPredicate {
/*    */   public static final MapCodec<MatchingFluidsPredicate> CODEC;
/*    */   
/*    */   private final HolderSet<Fluid> fluids;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> stateTestingCodec(i).and((App)RegistryCodecs.homogeneousList(Registries.FLUID).fieldOf("fluids").forGetter(())).apply((Applicative)i, MatchingFluidsPredicate::new));
/*    */   }
/*    */   
/*    */   public MatchingFluidsPredicate(Vec3i offset, HolderSet<Fluid> fluids) {
/* 20 */     super(offset);
/* 21 */     this.fluids = fluids;
/*    */   }
/*    */   
/*    */   protected boolean test(BlockState state) {
/* 26 */     return state.getFluidState().is(this.fluids);
/*    */   }
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 31 */     return BlockPredicateType.MATCHING_FLUIDS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\MatchingFluidsPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */