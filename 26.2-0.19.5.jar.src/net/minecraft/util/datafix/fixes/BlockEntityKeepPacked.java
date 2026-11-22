/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class BlockEntityKeepPacked extends NamedEntityFix {
/*    */   public BlockEntityKeepPacked(Schema schema, boolean changesType) {
/* 10 */     super(schema, changesType, "BlockEntityKeepPacked", References.BLOCK_ENTITY, "DUMMY");
/*    */   }
/*    */   
/*    */   private static Dynamic<?> fixTag(Dynamic<?> tag) {
/* 14 */     return tag.set("keepPacked", tag.createBoolean(true));
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 19 */     return entity.update(DSL.remainderFinder(), BlockEntityKeepPacked::fixTag);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\BlockEntityKeepPacked.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */