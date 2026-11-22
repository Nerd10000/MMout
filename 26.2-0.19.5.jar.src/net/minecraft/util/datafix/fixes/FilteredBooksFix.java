/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class FilteredBooksFix extends ItemStackTagFix {
/*    */   public FilteredBooksFix(Schema outputSchema) {
/*  9 */     super(outputSchema, "Remove filtered text from books", id -> (id.equals("minecraft:writable_book") || id.equals("minecraft:written_book")));
/*    */   }
/*    */   
/*    */   protected Typed<?> fixItemStackTag(Typed<?> tag) {
/* 14 */     return Util.writeAndReadTypedOrThrow(tag, tag.getType(), dynamic -> dynamic.remove("filtered_title").remove("filtered_pages"));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\FilteredBooksFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */