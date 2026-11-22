/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityShulkerBoxColorFix extends NamedEntityFix {
/*    */   public BlockEntityShulkerBoxColorFix(Schema outputSchema, boolean changesType) {
/*  9 */     super(outputSchema, changesType, "BlockEntityShulkerBoxColorFix", References.BLOCK_ENTITY, "minecraft:shulker_box");
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 14 */     return entity.update(DSL.remainderFinder(), tag -> tag.remove("Color"));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\BlockEntityShulkerBoxColorFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */