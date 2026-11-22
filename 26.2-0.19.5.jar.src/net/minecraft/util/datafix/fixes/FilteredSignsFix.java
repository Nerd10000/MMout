/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ 
/*    */ public class FilteredSignsFix extends NamedEntityWriteReadFix {
/*    */   public FilteredSignsFix(Schema outputSchema) {
/*  8 */     super(outputSchema, false, "Remove filtered text from signs", References.BLOCK_ENTITY, "minecraft:sign");
/*    */   }
/*    */   
/*    */   protected <T> Dynamic<T> fix(Dynamic<T> input) {
/* 13 */     return input.remove("FilteredText1").remove("FilteredText2").remove("FilteredText3").remove("FilteredText4");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\FilteredSignsFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */