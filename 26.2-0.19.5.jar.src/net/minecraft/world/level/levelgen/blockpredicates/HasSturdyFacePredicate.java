/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ public class HasSturdyFacePredicate implements BlockPredicate {
/*    */   private final Vec3i offset;
/*    */   
/*    */   private final Direction direction;
/*    */   
/*    */   public static final MapCodec<HasSturdyFacePredicate> CODEC;
/*    */   
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Vec3i.offsetCodec(16).optionalFieldOf("offset", Vec3i.ZERO).forGetter(()), (App)Direction.CODEC.fieldOf("direction").forGetter(())).apply((Applicative)i, HasSturdyFacePredicate::new));
/*    */   }
/*    */   
/*    */   public HasSturdyFacePredicate(Vec3i offset, Direction direction) {
/* 20 */     this.offset = offset;
/* 21 */     this.direction = direction;
/*    */   }
/*    */   
/*    */   public boolean test(WorldGenLevel level, BlockPos origin) {
/* 26 */     BlockPos testPosition = origin.offset(this.offset);
/* 27 */     return level.getBlockState(testPosition).isFaceSturdy((BlockGetter)level, testPosition, this.direction);
/*    */   }
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 32 */     return BlockPredicateType.HAS_STURDY_FACE;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\HasSturdyFacePredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */