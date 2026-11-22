/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class AttributesRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   private final UnaryOperator<String> renames;
/*    */   
/*    */   private final boolean oldDataComponentFormat;
/*    */   
/*    */   public AttributesRenameFix(Schema outputSchema, String name, UnaryOperator<String> renames) {
/* 20 */     this(outputSchema, name, renames, false);
/*    */   }
/*    */   
/*    */   public AttributesRenameFix(Schema outputSchema, String name, UnaryOperator<String> renames, boolean oldDataComponentFormat) {
/* 24 */     super(outputSchema, false);
/* 25 */     this.name = name;
/* 26 */     this.renames = renames;
/* 27 */     this.oldDataComponentFormat = oldDataComponentFormat;
/*    */   }
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 32 */     return TypeRewriteRule.seq(
/* 33 */         fixTypeEverywhereTyped(this.name + " (Components)", getInputSchema().getType(References.DATA_COMPONENTS), this.oldDataComponentFormat ? this::fixDataComponentsOld : this::fixDataComponents), new TypeRewriteRule[] { fixTypeEverywhereTyped(this.name + " (Entity)", getInputSchema().getType(References.ENTITY), this::fixEntity), 
/* 35 */           fixTypeEverywhereTyped(this.name + " (Player)", getInputSchema().getType(References.PLAYER), this::fixEntity) });
/*    */   }
/*    */   
/*    */   private Typed<?> fixDataComponents(Typed<?> components) {
/* 40 */     return components.update(DSL.remainderFinder(), componentData -> componentData.update("minecraft:attribute_modifiers", ()));
/*    */   }
/*    */   
/*    */   private Typed<?> fixDataComponentsOld(Typed<?> components) {
/* 51 */     return components.update(DSL.remainderFinder(), componentData -> componentData.update("minecraft:attribute_modifiers", ()));
/*    */   }
/*    */   
/*    */   private Typed<?> fixEntity(Typed<?> entity) {
/* 64 */     return entity.update(DSL.remainderFinder(), tag -> tag.update("attributes", ()));
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixIdField(Dynamic<?> dynamic) {
/* 75 */     return ExtraDataFixUtils.fixStringField(dynamic, "id", this.renames);
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixTypeField(Dynamic<?> dynamic) {
/* 79 */     return ExtraDataFixUtils.fixStringField(dynamic, "type", this.renames);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\AttributesRenameFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */