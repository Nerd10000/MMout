/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public class V4771 extends NamespacedSchema {
/*    */   public V4771(int versionKey, Schema parent) {
/* 14 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> entityTypes, Map<String, Supplier<TypeTemplate>> blockEntityTypes) {
/* 19 */     super.registerTypes(schema, entityTypes, blockEntityTypes);
/* 20 */     schema.registerType(false, References.LEVEL, () -> References.LIGHTWEIGHT_LEVEL.in(schema));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V4771.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */