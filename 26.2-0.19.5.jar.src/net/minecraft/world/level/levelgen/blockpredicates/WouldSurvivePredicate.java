/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class WouldSurvivePredicate implements BlockPredicate {
/*    */   public static final MapCodec<WouldSurvivePredicate> CODEC;
/*    */   
/*    */   private final Vec3i offset;
/*    */   
/*    */   private final BlockState state;
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Vec3i.offsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(()), (App)BlockState.CODEC.fieldOf("state").forGetter(())).apply((Applicative)i, WouldSurvivePredicate::new));
/*    */   }
/*    */   
/*    */   protected WouldSurvivePredicate(Vec3i offset, BlockState state) {
/* 20 */     this.offset = offset;
/* 21 */     this.state = state;
/*    */   }
/*    */   
/*    */   public boolean test(WorldGenLevel level, BlockPos origin) {
/* 26 */     return this.state.canSurvive((LevelReader)level, origin.offset(this.offset));
/*    */   }
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 31 */     return BlockPredicateType.WOULD_SURVIVE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\WouldSurvivePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */