/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class PrimedTntBlockStateFixer extends NamedEntityWriteReadFix {
/*    */   public PrimedTntBlockStateFixer(Schema outputSchema) {
/* 12 */     super(outputSchema, true, "PrimedTnt BlockState fixer", References.ENTITY, "minecraft:tnt");
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> renameFuse(Dynamic<T> input) {
/* 16 */     Optional<Dynamic<T>> fuseValue = input.get("Fuse").get().result();
/* 17 */     if (fuseValue.isPresent())
/* 18 */       return input.set("fuse", fuseValue.get()); 
/* 20 */     return input;
/*    */   }
/*    */   
/*    */   private static <T> Dynamic<T> insertBlockState(Dynamic<T> input) {
/* 24 */     return input.set("block_state", input.createMap(Map.of(input
/* 25 */             .createString("Name"), input.createString("minecraft:tnt"))));
/*    */   }
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> input) {
/* 31 */     return renameFuse(insertBlockState(input));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\PrimedTntBlockStateFixer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */