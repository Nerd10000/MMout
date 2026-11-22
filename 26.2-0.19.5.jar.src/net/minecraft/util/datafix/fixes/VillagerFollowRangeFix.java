/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class VillagerFollowRangeFix extends NamedEntityFix {
/*    */   private static final double ORIGINAL_VALUE = 16.0D;
/*    */   
/*    */   private static final double NEW_BASE_VALUE = 48.0D;
/*    */   
/*    */   public VillagerFollowRangeFix(Schema outputSchema) {
/* 15 */     super(outputSchema, false, "Villager Follow Range Fix", References.ENTITY, "minecraft:villager");
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 20 */     return entity.update(DSL.remainderFinder(), VillagerFollowRangeFix::fixValue);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> tag) {
/* 24 */     return tag.update("Attributes", attributes -> tag.createList(attributes.asStream().map(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\VillagerFollowRangeFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */