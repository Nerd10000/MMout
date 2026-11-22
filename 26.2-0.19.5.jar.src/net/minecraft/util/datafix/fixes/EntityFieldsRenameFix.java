/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class EntityFieldsRenameFix extends NamedEntityFix {
/*    */   private final Map<String, String> renames;
/*    */   
/*    */   public EntityFieldsRenameFix(Schema outputSchema, String name, String entityType, Map<String, String> renames) {
/* 14 */     super(outputSchema, false, name, References.ENTITY, entityType);
/* 15 */     this.renames = renames;
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> data) {
/* 19 */     for (Map.Entry<String, String> entry : this.renames.entrySet())
/* 20 */       data = data.renameField(entry.getKey(), entry.getValue()); 
/* 22 */     return data;
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 27 */     return entity.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityFieldsRenameFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */