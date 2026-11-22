/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V3818_4 extends NamespacedSchema {
/*    */   public V3818_4(int versionKey, Schema parent) {
/* 16 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 21 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 24 */     schema.registerType(true, References.PARTICLE, () -> DSL.optionalFields("item", References.ITEM_STACK.in(schema), "block_state", References.BLOCK_STATE.in(schema)));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V3818_4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */