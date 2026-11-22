/*    */ package net.minecraft.advancements.predicates.entity;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.advancements.AdvancementProgress;
/*    */ 
/*    */ interface AdvancementPredicate extends Predicate<AdvancementProgress> {
/*    */   public static final Codec<AdvancementPredicate> CODEC;
/*    */   
/*    */   static {
/* 72 */     CODEC = Codec.either(PlayerPredicate.AdvancementDonePredicate.CODEC, PlayerPredicate.AdvancementCriterionsPredicate.CODEC).xmap(Either::unwrap, predicate -> {
/*    */           if (predicate instanceof PlayerPredicate.AdvancementDonePredicate) {
/*    */             PlayerPredicate.AdvancementDonePredicate done = (PlayerPredicate.AdvancementDonePredicate)predicate;
/*    */             return Either.left(done);
/*    */           } 
/*    */           if (predicate instanceof PlayerPredicate.AdvancementCriterionsPredicate) {
/*    */             PlayerPredicate.AdvancementCriterionsPredicate criterions = (PlayerPredicate.AdvancementCriterionsPredicate)predicate;
/*    */             return Either.right(criterions);
/*    */           } 
/*    */           throw new UnsupportedOperationException();
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\PlayerPredicate$AdvancementPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */