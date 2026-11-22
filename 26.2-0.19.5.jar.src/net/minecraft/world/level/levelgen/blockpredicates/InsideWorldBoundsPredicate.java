/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ public class InsideWorldBoundsPredicate implements BlockPredicate {
/*    */   public static final MapCodec<InsideWorldBoundsPredicate> CODEC;
/*    */   
/*    */   private final Vec3i offset;
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Vec3i.offsetCodec(16).optionalFieldOf("offset", BlockPos.ZERO).forGetter(())).apply((Applicative)i, InsideWorldBoundsPredicate::new));
/*    */   }
/*    */   
/*    */   public InsideWorldBoundsPredicate(Vec3i offset) {
/* 17 */     this.offset = offset;
/*    */   }
/*    */   
/*    */   public boolean test(WorldGenLevel worldGenLevel, BlockPos blockPos) {
/* 22 */     return worldGenLevel.isInsideBuildHeight(blockPos.offset(this.offset));
/*    */   }
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 27 */     return BlockPredicateType.INSIDE_WORLD_BOUNDS;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\InsideWorldBoundsPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */