/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*    */ 
/*    */ public class RemoveEmptyItemInBrushableBlockFix extends NamedEntityWriteReadFix {
/*    */   public RemoveEmptyItemInBrushableBlockFix(Schema outputSchema) {
/* 11 */     super(outputSchema, false, "RemoveEmptyItemInSuspiciousBlockFix", References.BLOCK_ENTITY, "minecraft:brushable_block");
/*    */   }
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> input) {
/* 16 */     Optional<Dynamic<T>> item = input.get("item").result();
/* 17 */     if (item.isPresent() && isEmptyStack(item.get()))
/* 18 */       return input.remove("item"); 
/* 20 */     return input;
/*    */   }
/*    */   
/*    */   private static boolean isEmptyStack(Dynamic<?> item) {
/* 24 */     String id = NamespacedSchema.ensureNamespaced(item.get("id").asString("minecraft:air"));
/* 25 */     int count = item.get("count").asInt(0);
/* 26 */     return (id.equals("minecraft:air") || count == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\RemoveEmptyItemInBrushableBlockFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */