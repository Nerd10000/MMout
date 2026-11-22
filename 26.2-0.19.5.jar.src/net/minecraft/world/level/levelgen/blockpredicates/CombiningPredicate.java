/*    */ package net.minecraft.world.level.levelgen.blockpredicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public abstract class CombiningPredicate implements BlockPredicate {
/*    */   protected final List<BlockPredicate> predicates;
/*    */   
/*    */   protected CombiningPredicate(List<BlockPredicate> predicates) {
/* 13 */     this.predicates = predicates;
/*    */   }
/*    */   
/*    */   public static <T extends CombiningPredicate> MapCodec<T> codec(Function<List<BlockPredicate>, T> constructor) {
/* 17 */     return RecordCodecBuilder.mapCodec(i -> i.group((App)BlockPredicate.CODEC.listOf().fieldOf("predicates").forGetter(())).apply((Applicative)i, constructor));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blockpredicates\CombiningPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */