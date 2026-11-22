/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class TridentAnimationFix extends DataComponentRemainderFix {
/*    */   public TridentAnimationFix(Schema outputSchema) {
/* 10 */     super(outputSchema, "TridentAnimationFix", "minecraft:consumable");
/*    */   }
/*    */   
/*    */   protected <T> Dynamic<T> fixComponent(Dynamic<T> input) {
/* 15 */     return input.update("animation", animation -> {
/*    */           String optional = animation.asString().result().orElse("");
/*    */           return "spear".equals(optional) ? animation.createString("trident") : animation;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\TridentAnimationFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */