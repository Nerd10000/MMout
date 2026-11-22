/*    */ package net.minecraft.commands.functions;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.UnboundEntryAction;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ class FunctionBuilder<T extends ExecutionCommandSource<T>> {
/* 17 */   private List<UnboundEntryAction<T>> plainEntries = new ArrayList<>();
/*    */   
/*    */   private List<MacroFunction.Entry<T>> macroEntries;
/*    */   
/* 19 */   private final List<String> macroArguments = new ArrayList<>();
/*    */   
/*    */   public void addCommand(UnboundEntryAction<T> command) {
/* 22 */     if (this.macroEntries != null) {
/* 23 */       this.macroEntries.add(new MacroFunction.PlainTextEntry<>(command));
/*    */     } else {
/* 25 */       this.plainEntries.add(command);
/*    */     } 
/*    */   }
/*    */   
/*    */   private int getArgumentIndex(String id) {
/* 30 */     int index = this.macroArguments.indexOf(id);
/* 31 */     if (index == -1) {
/* 32 */       index = this.macroArguments.size();
/* 33 */       this.macroArguments.add(id);
/*    */     } 
/* 35 */     return index;
/*    */   }
/*    */   
/*    */   private IntList convertToIndices(List<String> ids) {
/* 39 */     IntArrayList result = new IntArrayList(ids.size());
/* 40 */     for (String id : ids)
/* 41 */       result.add(getArgumentIndex(id)); 
/* 43 */     return (IntList)result;
/*    */   }
/*    */   
/*    */   public void addMacro(String command, int line, T compilationContext) {
/*    */     StringTemplate parseResults;
/*    */     try {
/* 49 */       parseResults = StringTemplate.fromString(command);
/* 50 */     } catch (Exception e) {
/* 51 */       throw new IllegalArgumentException("Can't parse function line " + line + ": '" + command + "'", e);
/*    */     } 
/* 54 */     if (this.plainEntries != null) {
/* 55 */       this.macroEntries = new ArrayList<>(this.plainEntries.size() + 1);
/* 56 */       for (UnboundEntryAction<T> plainEntry : this.plainEntries)
/* 57 */         this.macroEntries.add(new MacroFunction.PlainTextEntry<>(plainEntry)); 
/* 59 */       this.plainEntries = null;
/*    */     } 
/* 62 */     this.macroEntries.add(new MacroFunction.MacroEntry<>(parseResults, convertToIndices(parseResults.variables()), compilationContext));
/*    */   }
/*    */   
/*    */   public CommandFunction<T> build(Identifier id) {
/* 66 */     if (this.macroEntries != null)
/* 67 */       return new MacroFunction<>(id, this.macroEntries, this.macroArguments); 
/* 70 */     return new PlainTextFunction<>(id, this.plainEntries);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\functions\FunctionBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */