/*     */ package net.minecraft.util.datafix;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import net.minecraft.util.datafix.fixes.BlockEntityCustomNameToComponentFix;
/*     */ import net.minecraft.util.datafix.fixes.NamedEntityWriteReadFix;
/*     */ 
/*     */ class null extends NamedEntityWriteReadFix {
/*     */   null(Schema outputSchema, boolean changesType, String name, DSL.TypeReference type, String entityName) {
/* 705 */     super(outputSchema, changesType, name, type, entityName);
/*     */   }
/*     */   
/*     */   protected <T> Dynamic<T> fix(Dynamic<T> input) {
/* 708 */     return BlockEntityCustomNameToComponentFix.fixTagCustomName(input);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\DataFixers$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */