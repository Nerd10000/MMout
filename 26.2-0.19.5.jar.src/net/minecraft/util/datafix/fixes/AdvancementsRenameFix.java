/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class AdvancementsRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   private final Function<String, String> renamer;
/*    */   
/*    */   public AdvancementsRenameFix(Schema outputSchema, boolean changesType, String name, Function<String, String> renamer) {
/* 15 */     super(outputSchema, changesType);
/* 16 */     this.name = name;
/* 17 */     this.renamer = renamer;
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 22 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.ADVANCEMENTS), input -> input.update(DSL.remainderFinder(), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\AdvancementsRenameFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */