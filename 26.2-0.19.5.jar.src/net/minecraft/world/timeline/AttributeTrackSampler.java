/*    */ package net.minecraft.world.timeline;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.KeyframeTrack;
/*    */ import net.minecraft.util.KeyframeTrackSampler;
/*    */ import net.minecraft.world.attribute.EnvironmentAttributeLayer;
/*    */ import net.minecraft.world.attribute.LerpFunction;
/*    */ import net.minecraft.world.attribute.modifier.AttributeModifier;
/*    */ import net.minecraft.world.clock.ClockManager;
/*    */ import net.minecraft.world.clock.WorldClock;
/*    */ 
/*    */ public class AttributeTrackSampler<Value, Argument> implements EnvironmentAttributeLayer.TimeBased<Value> {
/*    */   private final Holder<WorldClock> clock;
/*    */   
/*    */   private final AttributeModifier<Value, Argument> modifier;
/*    */   
/*    */   private final KeyframeTrackSampler<Argument> argumentSampler;
/*    */   
/*    */   private final ClockManager clockManager;
/*    */   
/*    */   private int cachedTickId;
/*    */   
/*    */   private Argument cachedArgument;
/*    */   
/*    */   public AttributeTrackSampler(Holder<WorldClock> clock, Optional<Integer> periodTicks, AttributeModifier<Value, Argument> modifier, KeyframeTrack<Argument> argumentTrack, LerpFunction<Argument> argumentLerp, ClockManager clockManager) {
/* 25 */     this.clock = clock;
/* 26 */     this.modifier = modifier;
/* 27 */     this.clockManager = clockManager;
/* 28 */     this.argumentSampler = argumentTrack.bakeSampler(periodTicks, argumentLerp);
/*    */   }
/*    */   
/*    */   public Value applyTimeBased(Value baseValue, int cacheTickId) {
/* 33 */     if (this.cachedArgument == null || cacheTickId != this.cachedTickId) {
/* 34 */       this.cachedTickId = cacheTickId;
/* 35 */       this.cachedArgument = (Argument)this.argumentSampler.sample(this.clockManager.getTotalTicks(this.clock));
/*    */     } 
/* 37 */     return (Value)this.modifier.apply(baseValue, this.cachedArgument);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\timeline\AttributeTrackSampler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */