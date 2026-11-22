/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ public class CriteriaRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   private final String advancementId;
/*    */   
/*    */   private final UnaryOperator<String> conversions;
/*    */   
/*    */   public CriteriaRenameFix(Schema outputSchema, String name, String advancementId, UnaryOperator<String> conversions) {
/* 18 */     super(outputSchema, false);
/* 19 */     this.name = name;
/* 20 */     this.advancementId = advancementId;
/* 21 */     this.conversions = conversions;
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     return fixTypeEverywhereTyped(this.name, getInputSchema().getType(References.ADVANCEMENTS), input -> input.update(DSL.remainderFinder(), this::fixAdvancements));
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixAdvancements(Dynamic<?> tag) {
/* 30 */     return tag.update(this.advancementId, advancement -> advancement.update("criteria", ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\CriteriaRenameFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */