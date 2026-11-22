/*     */ package net.minecraft.commands.functions;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntLists;
/*     */ import java.util.List;
/*     */ import net.minecraft.commands.execution.UnboundEntryAction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class PlainTextEntry<T> implements MacroFunction.Entry<T> {
/*     */   private final UnboundEntryAction<T> compiledAction;
/*     */   
/*     */   public PlainTextEntry(UnboundEntryAction<T> compiledAction) {
/* 125 */     this.compiledAction = compiledAction;
/*     */   }
/*     */   
/*     */   public IntList parameters() {
/* 130 */     return IntLists.emptyList();
/*     */   }
/*     */   
/*     */   public UnboundEntryAction<T> instantiate(List<String> substitutions, CommandDispatcher<T> dispatcher, Identifier functionId) {
/* 135 */     return this.compiledAction;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\functions\MacroFunction$PlainTextEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */