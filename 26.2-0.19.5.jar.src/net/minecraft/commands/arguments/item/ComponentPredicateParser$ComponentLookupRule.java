/*     */ package net.minecraft.commands.arguments.item;
/*     */ 
/*     */ import com.mojang.brigadier.ImmutableStringReader;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.parsing.packrat.NamedRule;
/*     */ import net.minecraft.util.parsing.packrat.commands.ResourceLookupRule;
/*     */ 
/*     */ class ComponentLookupRule<T, C, P> extends ResourceLookupRule<ComponentPredicateParser.Context<T, C, P>, C> {
/*     */   private ComponentLookupRule(NamedRule<StringReader, Identifier> idParser, ComponentPredicateParser.Context<T, C, P> context) {
/* 184 */     super(idParser, context);
/*     */   }
/*     */   
/*     */   protected C validateElement(ImmutableStringReader reader, Identifier id) throws Exception {
/* 189 */     return (C)((ComponentPredicateParser.Context)this.context).lookupComponentType(reader, id);
/*     */   }
/*     */   
/*     */   public Stream<Identifier> possibleResources() {
/* 194 */     return ((ComponentPredicateParser.Context)this.context).listComponentTypes();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\ComponentPredicateParser$ComponentLookupRule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */