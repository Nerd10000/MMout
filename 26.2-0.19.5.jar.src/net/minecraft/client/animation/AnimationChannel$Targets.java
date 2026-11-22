/*    */ package net.minecraft.client.animation;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ 
/*    */ public class Targets {
/* 22 */   public static final AnimationChannel.Target POSITION = ModelPart::offsetPos;
/*    */   
/* 23 */   public static final AnimationChannel.Target ROTATION = ModelPart::offsetRotation;
/*    */   
/* 24 */   public static final AnimationChannel.Target SCALE = ModelPart::offsetScale;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\animation\AnimationChannel$Targets.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */