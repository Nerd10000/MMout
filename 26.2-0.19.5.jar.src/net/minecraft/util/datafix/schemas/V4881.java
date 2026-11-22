/*    */ package net.minecraft.util.datafix.schemas;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import java.util.Map;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class V4881 extends NamespacedSchema {
/*    */   public V4881(int versionKey, Schema parent) {
/* 11 */     super(versionKey, parent);
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
/* 16 */     Map<String, Supplier<TypeTemplate>> map = super.registerEntities(schema);
/* 17 */     schema.registerSimple(map, "minecraft:sulfur_cube");
/* 18 */     return map;
/*    */   }
/*    */   
/*    */   public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
/* 23 */     Map<String, Supplier<TypeTemplate>> map = super.registerBlockEntities(schema);
/* 24 */     schema.registerSimple(map, "minecraft:potent_sulfur");
/* 25 */     return map;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\schemas\V4881.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */