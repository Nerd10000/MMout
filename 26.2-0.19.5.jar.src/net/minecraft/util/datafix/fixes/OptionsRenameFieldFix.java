/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class OptionsRenameFieldFix extends DataFix {
/*    */   private final String fixName;
/*    */   
/*    */   private final String fieldFrom;
/*    */   
/*    */   private final String fieldTo;
/*    */   
/*    */   public OptionsRenameFieldFix(Schema outputSchema, boolean changesType, String fixName, String fieldFrom, String fieldTo) {
/* 14 */     super(outputSchema, changesType);
/* 15 */     this.fixName = fixName;
/* 16 */     this.fieldFrom = fieldFrom;
/* 17 */     this.fieldTo = fieldTo;
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped(this.fixName, getInputSchema().getType(References.OPTIONS), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\OptionsRenameFieldFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */