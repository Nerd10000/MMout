/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*    */ 
/*    */ public class Builder extends LootItemConditionalFunction.Builder<SetStewEffectFunction.Builder> {
/* 83 */   private final ImmutableList.Builder<SetStewEffectFunction.EffectEntry> effects = ImmutableList.builder();
/*    */   
/*    */   protected Builder getThis() {
/* 87 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withEffect(Holder<MobEffect> effect, NumberProvider duration) {
/* 91 */     this.effects.add(new SetStewEffectFunction.EffectEntry(effect, duration));
/* 92 */     return this;
/*    */   }
/*    */   
/*    */   public LootItemFunction build() {
/* 97 */     return new SetStewEffectFunction(getConditions(), (List<SetStewEffectFunction.EffectEntry>)this.effects.build());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\SetStewEffectFunction$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */