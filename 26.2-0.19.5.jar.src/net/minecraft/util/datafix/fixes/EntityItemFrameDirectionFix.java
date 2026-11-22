/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class EntityItemFrameDirectionFix extends NamedEntityFix {
/*    */   public EntityItemFrameDirectionFix(Schema outputSchema, boolean changesType) {
/* 10 */     super(outputSchema, changesType, "EntityItemFrameDirectionFix", References.ENTITY, "minecraft:item_frame");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 14 */     return input.set("Facing", input.createByte(direction2dTo3d(input.get("Facing").asByte((byte)0))));
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 19 */     return entity.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */   
/*    */   private static byte direction2dTo3d(byte dir) {
/* 23 */     switch (dir) {
/*    */       default:
/* 26 */         return 2;
/*    */       case 0:
/* 28 */         return 3;
/*    */       case 1:
/* 30 */         return 4;
/*    */       case 3:
/*    */         break;
/*    */     } 
/* 32 */     return 5;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\EntityItemFrameDirectionFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */