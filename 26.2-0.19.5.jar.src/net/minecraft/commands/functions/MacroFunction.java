/*     */ package net.minecraft.commands.functions;
/*     */ 
/*     */ import com.mojang.brigadier.CommandDispatcher;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntLists;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.FunctionInstantiationException;
/*     */ import net.minecraft.commands.execution.UnboundEntryAction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class MacroFunction<T extends ExecutionCommandSource<T>> implements CommandFunction<T> {
/*     */   private static final DecimalFormat DECIMAL_FORMAT;
/*     */   
/*     */   private static final int MAX_CACHE_ENTRIES = 8;
/*     */   
/*     */   private final List<String> parameters;
/*     */   
/*     */   static {
/*  34 */     DECIMAL_FORMAT = (DecimalFormat)Util.make(new DecimalFormat("#", DecimalFormatSymbols.getInstance(Locale.ROOT)), format -> format.setMaximumFractionDigits(15));
/*     */   }
/*     */   
/*  41 */   private final Object2ObjectLinkedOpenHashMap<List<String>, InstantiatedFunction<T>> cache = new Object2ObjectLinkedOpenHashMap(8, 0.25F);
/*     */   
/*     */   private final Identifier id;
/*     */   
/*     */   private final List<Entry<T>> entries;
/*     */   
/*     */   public MacroFunction(Identifier id, List<Entry<T>> entries, List<String> parameters) {
/*  47 */     this.id = id;
/*  48 */     this.entries = entries;
/*  49 */     this.parameters = parameters;
/*     */   }
/*     */   
/*     */   public Identifier id() {
/*  54 */     return this.id;
/*     */   }
/*     */   
/*     */   public InstantiatedFunction<T> instantiate(CompoundTag arguments, CommandDispatcher<T> dispatcher) throws FunctionInstantiationException {
/*  59 */     if (arguments == null)
/*  60 */       throw new FunctionInstantiationException(Component.translatable("commands.function.error.missing_arguments", new Object[] { Component.translationArg(id()) })); 
/*  62 */     List<String> parameterValues = new ArrayList<>(this.parameters.size());
/*  63 */     for (String argument : this.parameters) {
/*  64 */       Tag argumentValue = arguments.get(argument);
/*  65 */       if (argumentValue == null)
/*  66 */         throw new FunctionInstantiationException(Component.translatable("commands.function.error.missing_argument", new Object[] { Component.translationArg(id()), argument })); 
/*  68 */       parameterValues.add(stringify(argumentValue));
/*     */     } 
/*  71 */     InstantiatedFunction<T> cachedFunction = (InstantiatedFunction<T>)this.cache.getAndMoveToLast(parameterValues);
/*  72 */     if (cachedFunction != null)
/*  73 */       return cachedFunction; 
/*  75 */     if (this.cache.size() >= 8)
/*  76 */       this.cache.removeFirst(); 
/*  78 */     InstantiatedFunction<T> function = substituteAndParse(this.parameters, parameterValues, dispatcher);
/*  79 */     this.cache.put(parameterValues, function);
/*  80 */     return function;
/*     */   }
/*     */   
/*     */   private static String stringify(Tag tag) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_1
/*     */     //   7: iconst_0
/*     */     //   8: istore_2
/*     */     //   9: aload_1
/*     */     //   10: iload_2
/*     */     //   11: <illegal opcode> typeSwitch : (Lnet/minecraft/nbt/Tag;I)I
/*     */     //   16: tableswitch default -> 288, 0 -> 56, 1 -> 99, 2 -> 143, 3 -> 184, 4 -> 225, 5 -> 266
/*     */     //   56: aload_1
/*     */     //   57: checkcast net/minecraft/nbt/FloatTag
/*     */     //   60: astore_3
/*     */     //   61: aload_3
/*     */     //   62: invokevirtual value : ()F
/*     */     //   65: fstore #5
/*     */     //   67: fload #5
/*     */     //   69: fstore #6
/*     */     //   71: iconst_1
/*     */     //   72: ifeq -> 82
/*     */     //   75: fload #5
/*     */     //   77: fstore #4
/*     */     //   79: goto -> 87
/*     */     //   82: iconst_1
/*     */     //   83: istore_2
/*     */     //   84: goto -> 9
/*     */     //   87: getstatic net/minecraft/commands/functions/MacroFunction.DECIMAL_FORMAT : Ljava/text/DecimalFormat;
/*     */     //   90: fload #4
/*     */     //   92: f2d
/*     */     //   93: invokevirtual format : (D)Ljava/lang/String;
/*     */     //   96: goto -> 297
/*     */     //   99: aload_1
/*     */     //   100: checkcast net/minecraft/nbt/DoubleTag
/*     */     //   103: astore #5
/*     */     //   105: aload #5
/*     */     //   107: invokevirtual value : ()D
/*     */     //   110: dstore #8
/*     */     //   112: dload #8
/*     */     //   114: dstore #10
/*     */     //   116: iconst_1
/*     */     //   117: ifeq -> 127
/*     */     //   120: dload #8
/*     */     //   122: dstore #6
/*     */     //   124: goto -> 132
/*     */     //   127: iconst_2
/*     */     //   128: istore_2
/*     */     //   129: goto -> 9
/*     */     //   132: getstatic net/minecraft/commands/functions/MacroFunction.DECIMAL_FORMAT : Ljava/text/DecimalFormat;
/*     */     //   135: dload #6
/*     */     //   137: invokevirtual format : (D)Ljava/lang/String;
/*     */     //   140: goto -> 297
/*     */     //   143: aload_1
/*     */     //   144: checkcast net/minecraft/nbt/ByteTag
/*     */     //   147: astore #8
/*     */     //   149: aload #8
/*     */     //   151: invokevirtual value : ()B
/*     */     //   154: istore #10
/*     */     //   156: iload #10
/*     */     //   158: istore #11
/*     */     //   160: iconst_1
/*     */     //   161: ifeq -> 171
/*     */     //   164: iload #10
/*     */     //   166: istore #9
/*     */     //   168: goto -> 176
/*     */     //   171: iconst_3
/*     */     //   172: istore_2
/*     */     //   173: goto -> 9
/*     */     //   176: iload #9
/*     */     //   178: invokestatic valueOf : (I)Ljava/lang/String;
/*     */     //   181: goto -> 297
/*     */     //   184: aload_1
/*     */     //   185: checkcast net/minecraft/nbt/ShortTag
/*     */     //   188: astore #10
/*     */     //   190: aload #10
/*     */     //   192: invokevirtual value : ()S
/*     */     //   195: istore #12
/*     */     //   197: iload #12
/*     */     //   199: istore #13
/*     */     //   201: iconst_1
/*     */     //   202: ifeq -> 212
/*     */     //   205: iload #12
/*     */     //   207: istore #11
/*     */     //   209: goto -> 217
/*     */     //   212: iconst_4
/*     */     //   213: istore_2
/*     */     //   214: goto -> 9
/*     */     //   217: iload #11
/*     */     //   219: invokestatic valueOf : (I)Ljava/lang/String;
/*     */     //   222: goto -> 297
/*     */     //   225: aload_1
/*     */     //   226: checkcast net/minecraft/nbt/LongTag
/*     */     //   229: astore #12
/*     */     //   231: aload #12
/*     */     //   233: invokevirtual value : ()J
/*     */     //   236: lstore #15
/*     */     //   238: lload #15
/*     */     //   240: lstore #17
/*     */     //   242: iconst_1
/*     */     //   243: ifeq -> 253
/*     */     //   246: lload #15
/*     */     //   248: lstore #13
/*     */     //   250: goto -> 258
/*     */     //   253: iconst_5
/*     */     //   254: istore_2
/*     */     //   255: goto -> 9
/*     */     //   258: lload #13
/*     */     //   260: invokestatic valueOf : (J)Ljava/lang/String;
/*     */     //   263: goto -> 297
/*     */     //   266: aload_1
/*     */     //   267: checkcast net/minecraft/nbt/StringTag
/*     */     //   270: astore #15
/*     */     //   272: aload #15
/*     */     //   274: invokevirtual value : ()Ljava/lang/String;
/*     */     //   277: astore #17
/*     */     //   279: aload #17
/*     */     //   281: astore #16
/*     */     //   283: aload #16
/*     */     //   285: goto -> 297
/*     */     //   288: aload_0
/*     */     //   289: invokeinterface toString : ()Ljava/lang/String;
/*     */     //   294: goto -> 297
/*     */     //   297: areturn
/*     */     //   298: astore_1
/*     */     //   299: new java/lang/MatchException
/*     */     //   302: dup
/*     */     //   303: aload_1
/*     */     //   304: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   307: aload_1
/*     */     //   308: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   311: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #84	-> 0
/*     */     //   #86	-> 56
/*     */     //   #87	-> 99
/*     */     //   #88	-> 143
/*     */     //   #89	-> 184
/*     */     //   #90	-> 225
/*     */     //   #92	-> 266
/*     */     //   #93	-> 288
/*     */     //   #84	-> 297
/*     */     //   #92	-> 298
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   79	3	4	value	F
/*     */     //   87	12	4	value	F
/*     */     //   124	3	6	value	D
/*     */     //   132	11	6	value	D
/*     */     //   168	3	9	value	B
/*     */     //   176	8	9	value	B
/*     */     //   209	3	11	value	S
/*     */     //   217	8	11	value	S
/*     */     //   250	3	13	value	J
/*     */     //   258	8	13	value	J
/*     */     //   283	5	16	value	Ljava/lang/String;
/*     */     //   0	312	0	tag	Lnet/minecraft/nbt/Tag;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   62	65	298	java/lang/Throwable
/*     */     //   107	110	298	java/lang/Throwable
/*     */     //   151	154	298	java/lang/Throwable
/*     */     //   192	195	298	java/lang/Throwable
/*     */     //   233	236	298	java/lang/Throwable
/*     */     //   274	277	298	java/lang/Throwable
/*     */   }
/*     */   
/*     */   private static void lookupValues(List<String> values, IntList indicesToSelect, List<String> selectedValuesOutput) {
/*  98 */     selectedValuesOutput.clear();
/*  99 */     indicesToSelect.forEach(index -> selectedValuesOutput.add(values.get(index)));
/*     */   }
/*     */   
/*     */   private InstantiatedFunction<T> substituteAndParse(List<String> keys, List<String> values, CommandDispatcher<T> dispatcher) throws FunctionInstantiationException {
/* 103 */     List<UnboundEntryAction<T>> newEntries = new ArrayList<>(this.entries.size());
/* 104 */     List<String> entryArguments = new ArrayList<>(values.size());
/* 106 */     for (Entry<T> entry : this.entries) {
/* 107 */       lookupValues(values, entry.parameters(), entryArguments);
/* 108 */       newEntries.add(entry.instantiate(entryArguments, dispatcher, this.id));
/*     */     } 
/* 110 */     return new PlainTextFunction<>(id().withPath(id -> id + "/" + id), newEntries);
/*     */   }
/*     */   
/*     */   static class PlainTextEntry<T> implements Entry<T> {
/*     */     private final UnboundEntryAction<T> compiledAction;
/*     */     
/*     */     public PlainTextEntry(UnboundEntryAction<T> compiledAction) {
/* 125 */       this.compiledAction = compiledAction;
/*     */     }
/*     */     
/*     */     public IntList parameters() {
/* 130 */       return IntLists.emptyList();
/*     */     }
/*     */     
/*     */     public UnboundEntryAction<T> instantiate(List<String> substitutions, CommandDispatcher<T> dispatcher, Identifier functionId) {
/* 135 */       return this.compiledAction;
/*     */     }
/*     */   }
/*     */   
/*     */   static class MacroEntry<T extends ExecutionCommandSource<T>> implements Entry<T> {
/*     */     private final StringTemplate template;
/*     */     
/*     */     private final IntList parameters;
/*     */     
/*     */     private final T compilationContext;
/*     */     
/*     */     public MacroEntry(StringTemplate template, IntList parameters, T compilationContext) {
/* 146 */       this.template = template;
/* 147 */       this.parameters = parameters;
/* 148 */       this.compilationContext = compilationContext;
/*     */     }
/*     */     
/*     */     public IntList parameters() {
/* 153 */       return this.parameters;
/*     */     }
/*     */     
/*     */     public UnboundEntryAction<T> instantiate(List<String> substitutions, CommandDispatcher<T> dispatcher, Identifier functionId) throws FunctionInstantiationException {
/* 158 */       String command = this.template.substitute(substitutions);
/*     */       try {
/* 160 */         return CommandFunction.parseCommand(dispatcher, this.compilationContext, new StringReader(command));
/* 161 */       } catch (CommandSyntaxException e) {
/* 162 */         throw new FunctionInstantiationException(Component.translatable("commands.function.error.parse", new Object[] { Component.translationArg(functionId), command, e.getMessage() }));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static interface Entry<T> {
/*     */     IntList parameters();
/*     */     
/*     */     UnboundEntryAction<T> instantiate(List<String> param1List, CommandDispatcher<T> param1CommandDispatcher, Identifier param1Identifier) throws FunctionInstantiationException;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\functions\MacroFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */