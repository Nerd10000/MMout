/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ 
/*    */ public class NotPredicate implements BlockPredicate {
/*    */   public static final MapCodec<NotPredicate> CODEC;
/*    */   
/*    */   private final BlockPredicate predicate;
/*    */   
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BlockPredicate.CODEC.fieldOf("predicate").forGetter(())).apply((Applicative)i, NotPredicate::new));
/*    */   }
/*    */   
/*    */   public NotPredicate(BlockPredicate predicate) {
/* 16 */     this.predicate = predicate;
/*    */   }
/*    */   
/*    */   public boolean test(WorldGenLevel level, BlockPos origin) {
/* 21 */     return !this.predicate.test(level, origin);
/*    */   }
/*    */   
/*    */   public BlockPredicateType<?> type() {
/* 26 */     return BlockPredicateType.NOT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\NotPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */