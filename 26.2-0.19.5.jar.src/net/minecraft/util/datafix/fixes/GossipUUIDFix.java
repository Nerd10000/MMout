/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class GossipUUIDFix extends NamedEntityFix {
/*    */   public GossipUUIDFix(Schema outputSchema, String entityName) {
/* 10 */     super(outputSchema, false, "Gossip for for " + entityName, References.ENTITY, entityName);
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 15 */     return entity.update(DSL.remainderFinder(), tag -> tag.update("Gossips", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\GossipUUIDFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */