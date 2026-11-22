/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class FireResistantToDamageResistantComponentFix extends DataComponentRemainderFix {
/*    */   public FireResistantToDamageResistantComponentFix(Schema outputSchema) {
/*  8 */     super(outputSchema, "FireResistantToDamageResistantComponentFix", "minecraft:fire_resistant", "minecraft:damage_resistant");
/*    */   }
/*    */   
/*    */   protected <T> Dynamic<T> fixComponent(Dynamic<T> input) {
/* 13 */     return input.emptyMap().set("types", input.createString("#minecraft:is_fire"));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\FireResistantToDamageResistantComponentFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */