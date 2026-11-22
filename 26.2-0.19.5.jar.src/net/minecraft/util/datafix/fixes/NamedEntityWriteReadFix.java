/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.OpticFinder;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public abstract class NamedEntityWriteReadFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   private final String entityName;
/*    */   
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   public NamedEntityWriteReadFix(Schema outputSchema, boolean changesType, String name, DSL.TypeReference type, String entityName) {
/* 21 */     super(outputSchema, changesType);
/* 22 */     this.name = name;
/* 23 */     this.type = type;
/* 24 */     this.entityName = entityName;
/*    */   }
/*    */   
/*    */   public TypeRewriteRule makeRule() {
/* 29 */     Type<?> inputEntityType = getInputSchema().getType(this.type);
/* 30 */     Type<?> inputEntityChoiceType = getInputSchema().getChoiceType(this.type, this.entityName);
/* 32 */     Type<?> outputEntityType = getOutputSchema().getType(this.type);
/* 34 */     OpticFinder<?> entityF = DSL.namedChoice(this.entityName, inputEntityChoiceType);
/* 38 */     Type<?> patchedEntityType = ExtraDataFixUtils.patchSubType(inputEntityType, inputEntityType, outputEntityType);
/* 40 */     return fix(inputEntityType, outputEntityType, patchedEntityType, entityF);
/*    */   }
/*    */   
/*    */   private <S, T, A> TypeRewriteRule fix(Type<S> inputEntityType, Type<T> outputEntityType, Type<?> patchedEntityType, OpticFinder<A> choiceFinder) {
/* 44 */     return fixTypeEverywhereTyped(this.name, inputEntityType, outputEntityType, typed -> {
/*    */           if (typed.getOptional(choiceFinder).isEmpty())
/*    */             return ExtraDataFixUtils.cast(outputEntityType, typed); 
/*    */           Typed<?> fakeTyped = ExtraDataFixUtils.cast(patchedEntityType, typed);
/*    */           return Util.writeAndReadTypedOrThrow(fakeTyped, outputEntityType, this::fix);
/*    */         });
/*    */   }
/*    */   
/*    */   protected abstract <T> Dynamic<T> fix(Dynamic<T> paramDynamic);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\NamedEntityWriteReadFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */