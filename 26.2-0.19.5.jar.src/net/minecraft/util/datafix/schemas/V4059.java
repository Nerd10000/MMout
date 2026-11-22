/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.SequencedMap;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V4059 extends NamespacedSchema {
/*    */   public V4059(int versionKey, Schema parent) {
/* 20 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public static SequencedMap<String, Supplier<TypeTemplate>> components(Schema schema) {
/* 24 */     SequencedMap<String, Supplier<TypeTemplate>> components = V3818_3.components(schema);
/* 25 */     components.remove("minecraft:food");
/* 26 */     components.put("minecraft:use_remainder", () -> References.ITEM_STACK.in(schema));
/* 28 */     components.put("minecraft:equippable", () -> DSL.optionalFields("allowed_entities", DSL.or(References.ENTITY_NAME.in(schema), DSL.list(References.ENTITY_NAME.in(schema)))));
/* 31 */     components.put("minecraft:sulfur_cube_content", () -> References.ITEM_STACK.in(schema));
/* 32 */     return components;
/*    */   }
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 37 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 38 */     schema.registerType(true, References.DATA_COMPONENTS, () -> DSL.optionalFieldsLazy(components(schema)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V4059.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */