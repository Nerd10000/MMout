/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class RemoveGolemGossipFix extends NamedEntityFix {
/*    */   public RemoveGolemGossipFix(Schema outputSchema, boolean changesType) {
/* 10 */     super(outputSchema, changesType, "Remove Golem Gossip Fix", References.ENTITY, "minecraft:villager");
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 15 */     return entity.update(DSL.remainderFinder(), RemoveGolemGossipFix::fixValue);
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixValue(Dynamic<?> tag) {
/* 19 */     return tag.update("Gossips", gossips -> tag.createList(gossips.asStream().filter(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\RemoveGolemGossipFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */