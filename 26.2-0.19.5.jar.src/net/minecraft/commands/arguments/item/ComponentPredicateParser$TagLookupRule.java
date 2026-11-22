/*     */ package net.minecraft.commands.arguments.item;
/*     */ 
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.parsing.packrat.NamedRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.ResourceLookupRule;
/*     */ 
/*     */ class TagLookupRule<T, C, P> extends ResourceLookupRule<ComponentPredicateParser.Context<T, C, P>, T> {
/*     */   private TagLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) {
/* 168 */     super(idParser, context);
/*     */   }
/*     */   
/*     */   protected T validateElement(ImmutableStringReader reader, Identifier id) throws Exception {
/* 173 */     return (T)((ComponentPredicateParser.Context)this.context).forTagType(reader, id);
/*     */   }
/*     */   
/*     */   public Stream<Identifier> possibleResources() {
/* 178 */     return ((ComponentPredicateParser.Context)this.context).listTagTypes();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ComponentPredicateParser$TagLookupRule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */