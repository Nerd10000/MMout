/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V3816 extends NamespacedSchema {
/*    */   public V3816(int versionKey, Schema parent) {
/* 12 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 17 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 18 */     schema.registerSimple(map, "minecraft:bogged");
/* 19 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V3816.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */