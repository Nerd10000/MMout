/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityWolfColorFix extends NamedEntityFix {
/*    */   public EntityWolfColorFix(Schema outputSchema, boolean changesType) {
/* 10 */     super(outputSchema, changesType, "EntityWolfColorFix", References.ENTITY, "minecraft:wolf");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 14 */     return input.update("CollarColor", color -> color.createByte((byte)(15 - color.asInt(0))));
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 19 */     return entity.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityWolfColorFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */