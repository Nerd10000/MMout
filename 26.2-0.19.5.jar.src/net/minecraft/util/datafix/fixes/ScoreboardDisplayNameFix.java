/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import net.minecraft.util.datafix.LegacyComponentDataFixUtils;
/*    */ 
/*    */ public class ScoreboardDisplayNameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public ScoreboardDisplayNameFix(Schema outputSchema, String name, DSL.TypeReference type) {
/* 17 */     super(outputSchema, false);
/* 18 */     this.name = name;
/* 19 */     this.type = type;
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 24 */     Type<?> inputType = getInputSchema().getType(this.type);
/* 25 */     OpticFinder<?> displayNameF = inputType.findField("DisplayName");
/* 27 */     OpticFinder<Pair<String, String>> textComponentF = DSL.typeFinder(getInputSchema().getType(References.TEXT_COMPONENT));
/* 28 */     return fixTypeEverywhereTyped(this.name, inputType, team -> team.updateTyped(displayNameF, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\ScoreboardDisplayNameFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */