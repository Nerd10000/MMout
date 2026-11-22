/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Builder<T> {
/* 72 */   private final ImmutableList.Builder<Keyframe<T>> keyframes = ImmutableList.builder();
/*    */   
/* 73 */   private EasingType easing = EasingType.LINEAR;
/*    */   
/*    */   public Builder<T> addKeyframe(int ticks, T value) {
/* 76 */     this.keyframes.add(new Keyframe<>(ticks, value));
/* 77 */     return this;
/*    */   }
/*    */   
/*    */   public Builder<T> setEasing(EasingType easing) {
/* 81 */     this.easing = easing;
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public KeyframeTrack<T> build() {
/* 86 */     List<Keyframe<T>> keyframes = (List<Keyframe<T>>)KeyframeTrack.<T>validateKeyframes((List<Keyframe<T>>)this.keyframes.build()).getOrThrow();
/* 87 */     return new KeyframeTrack<>(keyframes, this.easing);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\KeyframeTrack$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */