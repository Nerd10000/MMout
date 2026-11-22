/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class LodestoneCompassComponentFix extends DataComponentRemainderFix {
/*    */   public LodestoneCompassComponentFix(Schema outputSchema) {
/* 10 */     super(outputSchema, "LodestoneCompassComponentFix", "minecraft:lodestone_target", "minecraft:lodestone_tracker");
/*    */   }
/*    */   
/*    */   protected <T> Dynamic<T> fixComponent(Dynamic<T> input) {
/* 15 */     Optional<Dynamic<T>> pos = input.get("pos").result();
/* 16 */     Optional<Dynamic<T>> dimension = input.get("dimension").result();
/* 17 */     input = input.remove("pos").remove("dimension");
/* 18 */     if (pos.isPresent() && dimension.isPresent())
/* 19 */       input = input.set("target", input.emptyMap()
/* 20 */           .set("pos", pos.get())
/* 21 */           .set("dimension", dimension.get())); 
/* 24 */     return input;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\LodestoneCompassComponentFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */