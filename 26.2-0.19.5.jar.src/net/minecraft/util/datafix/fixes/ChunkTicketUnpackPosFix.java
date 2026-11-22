/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.IntStream;
/*    */ 
/*    */ public class ChunkTicketUnpackPosFix extends DataFix {
/*    */   private static final long CHUNK_COORD_BITS = 32L;
/*    */   
/*    */   private static final long CHUNK_COORD_MASK = 4294967295L;
/*    */   
/*    */   public ChunkTicketUnpackPosFix(Schema outputSchema) {
/* 15 */     super(outputSchema, false);
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 20 */     return fixTypeEverywhereTyped("ChunkTicketUnpackPosFix", getInputSchema().getType(References.SAVED_DATA_TICKETS), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ChunkTicketUnpackPosFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */