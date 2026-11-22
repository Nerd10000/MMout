/*    */ package net.minecraft.advancements.predicates;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ 
/*    */ public class Builder {
/* 41 */   private final ImmutableMap.Builder<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate> effectMap = ImmutableMap.builder();
/*    */   
/*    */   public static Builder effects() {
/* 44 */     return new Builder();
/*    */   }
/*    */   
/*    */   public Builder and(Holder<MobEffect> effect) {
/* 48 */     this.effectMap.put(effect, new MobEffectsPredicate.MobEffectInstancePredicate());
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public Builder and(Holder<MobEffect> effect, MobEffectsPredicate.MobEffectInstancePredicate predicate) {
/* 53 */     this.effectMap.put(effect, predicate);
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public MobEffectsPredicate build() {
/* 58 */     return new MobEffectsPredicate((Map<Holder<MobEffect>, MobEffectsPredicate.MobEffectInstancePredicate>)this.effectMap.build());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\MobEffectsPredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */