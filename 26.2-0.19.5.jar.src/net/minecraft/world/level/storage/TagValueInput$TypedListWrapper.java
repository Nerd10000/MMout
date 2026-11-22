/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.AbstractIterator;
/*     */ import com.google.common.collect.Streams;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ 
/*     */ class TypedListWrapper<T> implements ValueInput.TypedInputList<T> {
/*     */   private final ProblemReporter problemReporter;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final ValueInputContextHelper context;
/*     */   
/*     */   private final Codec<T> codec;
/*     */   
/*     */   private final ListTag list;
/*     */   
/*     */   private TypedListWrapper(ProblemReporter problemReporter, String name, ValueInputContextHelper context, Codec<T> codec, ListTag list) {
/* 306 */     this.problemReporter = problemReporter;
/* 307 */     this.name = name;
/* 308 */     this.context = context;
/* 309 */     this.codec = codec;
/* 310 */     this.list = list;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 315 */     return this.list.isEmpty();
/*     */   }
/*     */   
/*     */   private void reportIndexUnwrapProblem(int index, Tag value, DataResult.Error<?> error) {
/* 319 */     this.problemReporter.report(new TagValueInput.DecodeFromListFailedProblem(this.name, index, value, error));
/*     */   }
/*     */   
/*     */   public Stream<T> stream() {
/* 324 */     return Streams.mapWithIndex(this.list.stream(), (value, index) -> {
/*     */           // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: getfield codec : Lcom/mojang/serialization/Codec;
/*     */           //   4: aload_0
/*     */           //   5: getfield context : Lnet/minecraft/world/level/storage/ValueInputContextHelper;
/*     */           //   8: invokevirtual ops : ()Lcom/mojang/serialization/DynamicOps;
/*     */           //   11: aload_1
/*     */           //   12: invokeinterface parse : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */           //   17: dup
/*     */           //   18: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */           //   21: pop
/*     */           //   22: astore #4
/*     */           //   24: iconst_0
/*     */           //   25: istore #5
/*     */           //   27: aload #4
/*     */           //   29: iload #5
/*     */           //   31: <illegal opcode> typeSwitch : (Lcom/mojang/serialization/DataResult;I)I
/*     */           //   36: lookupswitch default -> 64, 0 -> 74, 1 -> 89
/*     */           //   64: new java/lang/MatchException
/*     */           //   67: dup
/*     */           //   68: aconst_null
/*     */           //   69: aconst_null
/*     */           //   70: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */           //   73: athrow
/*     */           //   74: aload #4
/*     */           //   76: checkcast com/mojang/serialization/DataResult$Success
/*     */           //   79: astore #6
/*     */           //   81: aload #6
/*     */           //   83: invokevirtual value : ()Ljava/lang/Object;
/*     */           //   86: goto -> 114
/*     */           //   89: aload #4
/*     */           //   91: checkcast com/mojang/serialization/DataResult$Error
/*     */           //   94: astore #7
/*     */           //   96: aload_0
/*     */           //   97: lload_2
/*     */           //   98: l2i
/*     */           //   99: aload_1
/*     */           //   100: aload #7
/*     */           //   102: invokevirtual reportIndexUnwrapProblem : (ILnet/minecraft/nbt/Tag;Lcom/mojang/serialization/DataResult$Error;)V
/*     */           //   105: aload #7
/*     */           //   107: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */           //   110: aconst_null
/*     */           //   111: invokevirtual orElse : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */           //   114: areturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #325	-> 0
/*     */           //   #326	-> 74
/*     */           //   #327	-> 89
/*     */           //   #328	-> 96
/*     */           //   #329	-> 105
/*     */           //   #327	-> 114
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   81	8	6	success	Lcom/mojang/serialization/DataResult$Success;
/*     */           //   96	18	7	error	Lcom/mojang/serialization/DataResult$Error;
/*     */           //   24	90	4	selector0$temp	Lcom/mojang/serialization/DataResult;
/*     */           //   27	87	5	index$1	I
/*     */           //   0	115	0	this	Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
/*     */           //   0	115	1	value	Lnet/minecraft/nbt/Tag;
/*     */           //   0	115	2	index	J
/*     */           // Local variable type table:
/*     */           //   start	length	slot	name	signature
/*     */           //   81	8	6	success	Lcom/mojang/serialization/DataResult$Success<TT;>;
/*     */           //   96	18	7	error	Lcom/mojang/serialization/DataResult$Error<TT;>;
/*     */           //   0	115	0	this	Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper<TT;>;
/* 332 */         }).filter(Objects::nonNull);
/*     */   }
/*     */   
/*     */   public Iterator<T> iterator() {
/* 337 */     final ListIterator<Tag> iterator = this.list.listIterator();
/* 338 */     return (Iterator<T>)new AbstractIterator<T>() {
/*     */         protected T computeNext() {
/*     */           // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: getfield val$iterator : Ljava/util/ListIterator;
/*     */           //   4: invokeinterface hasNext : ()Z
/*     */           //   9: ifeq -> 162
/*     */           //   12: aload_0
/*     */           //   13: getfield val$iterator : Ljava/util/ListIterator;
/*     */           //   16: invokeinterface nextIndex : ()I
/*     */           //   21: istore_1
/*     */           //   22: aload_0
/*     */           //   23: getfield val$iterator : Ljava/util/ListIterator;
/*     */           //   26: invokeinterface next : ()Ljava/lang/Object;
/*     */           //   31: checkcast net/minecraft/nbt/Tag
/*     */           //   34: astore_2
/*     */           //   35: aload_0
/*     */           //   36: getfield this$0 : Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
/*     */           //   39: getfield codec : Lcom/mojang/serialization/Codec;
/*     */           //   42: aload_0
/*     */           //   43: getfield this$0 : Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
/*     */           //   46: getfield context : Lnet/minecraft/world/level/storage/ValueInputContextHelper;
/*     */           //   49: invokevirtual ops : ()Lcom/mojang/serialization/DynamicOps;
/*     */           //   52: aload_2
/*     */           //   53: invokeinterface parse : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Lcom/mojang/serialization/DataResult;
/*     */           //   58: dup
/*     */           //   59: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */           //   62: pop
/*     */           //   63: astore_3
/*     */           //   64: iconst_0
/*     */           //   65: istore #4
/*     */           //   67: aload_3
/*     */           //   68: iload #4
/*     */           //   70: <illegal opcode> typeSwitch : (Lcom/mojang/serialization/DataResult;I)I
/*     */           //   75: lookupswitch default -> 100, 0 -> 110, 1 -> 122
/*     */           //   100: new java/lang/MatchException
/*     */           //   103: dup
/*     */           //   104: aconst_null
/*     */           //   105: aconst_null
/*     */           //   106: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */           //   109: athrow
/*     */           //   110: aload_3
/*     */           //   111: checkcast com/mojang/serialization/DataResult$Success
/*     */           //   114: astore #5
/*     */           //   116: aload #5
/*     */           //   118: invokevirtual value : ()Ljava/lang/Object;
/*     */           //   121: areturn
/*     */           //   122: aload_3
/*     */           //   123: checkcast com/mojang/serialization/DataResult$Error
/*     */           //   126: astore #6
/*     */           //   128: aload_0
/*     */           //   129: getfield this$0 : Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper;
/*     */           //   132: iload_1
/*     */           //   133: aload_2
/*     */           //   134: aload #6
/*     */           //   136: invokevirtual reportIndexUnwrapProblem : (ILnet/minecraft/nbt/Tag;Lcom/mojang/serialization/DataResult$Error;)V
/*     */           //   139: aload #6
/*     */           //   141: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */           //   144: invokevirtual isPresent : ()Z
/*     */           //   147: ifeq -> 159
/*     */           //   150: aload #6
/*     */           //   152: invokevirtual partialValue : ()Ljava/util/Optional;
/*     */           //   155: invokevirtual get : ()Ljava/lang/Object;
/*     */           //   158: areturn
/*     */           //   159: goto -> 0
/*     */           //   162: aload_0
/*     */           //   163: invokevirtual endOfData : ()Ljava/lang/Object;
/*     */           //   166: areturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #341	-> 0
/*     */           //   #342	-> 12
/*     */           //   #343	-> 22
/*     */           //   #344	-> 35
/*     */           //   #345	-> 110
/*     */           //   #346	-> 116
/*     */           //   #348	-> 122
/*     */           //   #349	-> 128
/*     */           //   #350	-> 139
/*     */           //   #351	-> 150
/*     */           //   #355	-> 159
/*     */           //   #356	-> 162
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   116	6	5	success	Lcom/mojang/serialization/DataResult$Success;
/*     */           //   128	31	6	error	Lcom/mojang/serialization/DataResult$Error;
/*     */           //   22	137	1	index	I
/*     */           //   35	124	2	value	Lnet/minecraft/nbt/Tag;
/*     */           //   0	167	0	this	Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper$1;
/*     */           // Local variable type table:
/*     */           //   start	length	slot	name	signature
/*     */           //   116	6	5	success	Lcom/mojang/serialization/DataResult$Success<TT;>;
/*     */           //   128	31	6	error	Lcom/mojang/serialization/DataResult$Error<TT;>;
/*     */           //   0	167	0	this	Lnet/minecraft/world/level/storage/TagValueInput$TypedListWrapper$1;
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\TagValueInput$TypedListWrapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */