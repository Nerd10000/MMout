/*     */ package net.minecraft.commands.arguments.item;
/*     */ 
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.parsing.packrat.NamedRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.ResourceLookupRule;
/*     */ 
/*     */ class PredicateLookupRule<T, C, P> extends ResourceLookupRule<ComponentPredicateParser.Context<T, C, P>, P> {
/*     */   private PredicateLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) {
/* 200 */     super(idParser, context);
/*     */   }
/*     */   
/*     */   protected P validateElement(ImmutableStringReader reader, Identifier id) throws Exception {
/* 205 */     return (P)((ComponentPredicateParser.Context)this.context).lookupPredicateType(reader, id);
/*     */   }
/*     */   
/*     */   public Stream<Identifier> possibleResources() {
/* 210 */     return ((ComponentPredicateParser.Context)this.context).listPredicateTypes();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ComponentPredicateParser$PredicateLookupRule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */