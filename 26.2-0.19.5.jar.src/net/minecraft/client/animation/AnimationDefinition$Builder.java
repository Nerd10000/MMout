/*    */ package net.minecraft.client.animation;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Builder {
/*    */   private final float length;
/*    */   
/* 21 */   private final Map<String, List<AnimationChannel>> animationByBone = Maps.newHashMap();
/*    */   
/*    */   private boolean looping;
/*    */   
/*    */   public static Builder withLength(float lengthInSeconds) {
/* 25 */     return new Builder(lengthInSeconds);
/*    */   }
/*    */   
/*    */   private Builder(float length) {
/* 29 */     this.length = length;
/*    */   }
/*    */   
/*    */   public Builder looping() {
/* 33 */     this.looping = true;
/* 34 */     return this;
/*    */   }
/*    */   
/*    */   public Builder addAnimation(String boneName, AnimationChannel animation) {
/* 38 */     ((List<AnimationChannel>)this.animationByBone.computeIfAbsent(boneName, k -> new ArrayList())).add(animation);
/* 39 */     return this;
/*    */   }
/*    */   
/*    */   public AnimationDefinition build() {
/* 43 */     return new AnimationDefinition(this.length, this.looping, this.animationByBone);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\animation\AnimationDefinition$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */