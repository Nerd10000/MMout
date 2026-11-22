/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ 
/*     */ public class SelectorResult implements ScoreHolderArgument.Result {
/*     */   private final EntitySelector selector;
/*     */   
/*     */   public SelectorResult(EntitySelector selector) {
/* 189 */     this.selector = selector;
/*     */   }
/*     */   
/*     */   public Collection<ScoreHolder> getNames(CommandSourceStack sender, Supplier<Collection<ScoreHolder>> wildcard) throws CommandSyntaxException {
/* 194 */     List<? extends Entity> entities = this.selector.findEntities(sender);
/* 195 */     if (entities.isEmpty())
/* 196 */       throw EntityArgument.NO_ENTITIES_FOUND.create(); 
/* 198 */     return (Collection)List.copyOf(entities);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ScoreHolderArgument$SelectorResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */