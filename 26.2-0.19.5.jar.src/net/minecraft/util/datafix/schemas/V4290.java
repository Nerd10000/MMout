/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V4290 extends NamespacedSchema {
/*    */   public V4290(int versionKey, Schema parent) {
/* 22 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 27 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 28 */     schema.registerType(true, References.TEXT_COMPONENT, () -> DSL.or(DSL.or(DSL.constType(DSL.string()), DSL.list(References.TEXT_COMPONENT.in(schema))), DSL.optionalFields("extra", DSL.list(References.TEXT_COMPONENT.in(schema)), "separator", References.TEXT_COMPONENT.in(schema), "hoverEvent", (TypeTemplate)DSL.taggedChoice("action", DSL.string(), Map.of("show_text", DSL.optionalFields("contents", References.TEXT_COMPONENT.in(schema)), "show_item", DSL.optionalFields("contents", DSL.or(References.ITEM_STACK.in(schema), References.ITEM_NAME.in(schema))), "show_entity", DSL.optionalFields("type", References.ENTITY_NAME.in(schema), "name", References.TEXT_COMPONENT.in(schema)))))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V4290.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */