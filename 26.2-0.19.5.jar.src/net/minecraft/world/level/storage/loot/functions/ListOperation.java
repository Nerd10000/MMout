/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public interface ListOperation {
/*  18 */   public static final MapCodec<ListOperation> UNLIMITED_CODEC = codec(2147483647);
/*     */   
/*     */   static MapCodec<ListOperation> codec(int maxSize) {
/*  21 */     return Type.CODEC.dispatchMap("mode", ListOperation::mode, e -> e.mapCodec).validate(op -> {
/*     */           if (op instanceof ReplaceSection) {
/*     */             ReplaceSection section = (ReplaceSection)op;
/*     */             if (section.size().isPresent()) {
/*     */               int size = ((Integer)section.size().get()).intValue();
/*     */               if (size > maxSize)
/*     */                 return DataResult.error(()); 
/*     */             } 
/*     */           } 
/*     */           return DataResult.success(op);
/*     */         });
/*     */   }
/*     */   
/*     */   default <T> List<T> apply(List<T> original, List<T> replacement) {
/*  36 */     return apply(original, replacement, 2147483647);
/*     */   }
/*     */   
/*     */   Type mode();
/*     */   
/*     */   <T> List<T> apply(List<T> paramList1, List<T> paramList2, int paramInt);
/*     */   
/*     */   public enum Type implements StringRepresentable {
/*  42 */     REPLACE_ALL("replace_all", ListOperation.ReplaceAll.MAP_CODEC),
/*  43 */     REPLACE_SECTION("replace_section", ListOperation.ReplaceSection.MAP_CODEC),
/*  44 */     INSERT("insert", ListOperation.Insert.MAP_CODEC),
/*  45 */     APPEND("append", ListOperation.Append.MAP_CODEC);
/*     */     
/*  47 */     public static final Codec<Type> CODEC = (Codec<Type>)StringRepresentable.fromEnum(Type::values);
/*     */     
/*     */     private final String id;
/*     */     
/*     */     private final MapCodec<? extends ListOperation> mapCodec;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Type(String id, MapCodec<? extends ListOperation> mapCodec) {
/*  53 */       this.id = id;
/*  54 */       this.mapCodec = mapCodec;
/*     */     }
/*     */     
/*     */     public MapCodec<? extends ListOperation> mapCodec() {
/*  58 */       return this.mapCodec;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/*  63 */       return this.id;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ReplaceAll implements ListOperation {
/*  68 */     public static final ReplaceAll INSTANCE = new ReplaceAll();
/*     */     
/*  69 */     public static final MapCodec<ReplaceAll> MAP_CODEC = MapCodec.unit(() -> INSTANCE);
/*     */     
/*     */     public ListOperation.Type mode() {
/*  76 */       return ListOperation.Type.REPLACE_ALL;
/*     */     }
/*     */     
/*     */     public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) {
/*  81 */       return replacement;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ReplaceSection extends Record implements ListOperation {
/*     */     private final int offset;
/*     */     
/*     */     private final Optional<Integer> size;
/*     */     
/*     */     public ReplaceSection(int offset, Optional<Integer> size) {
/*  85 */       this.offset = offset;
/*  85 */       this.size = size;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$ReplaceSection;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int offset() {
/*  85 */       return this.offset;
/*     */     }
/*     */     
/*     */     public Optional<Integer> size() {
/*  85 */       return this.size;
/*     */     }
/*     */     
/*  86 */     private static final Logger LOGGER = LogUtils.getLogger();
/*     */     
/*     */     public static final MapCodec<ReplaceSection> MAP_CODEC;
/*     */     
/*     */     static {
/*  88 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("offset", Integer.valueOf(0)).forGetter(ReplaceSection::offset), (App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("size").forGetter(ReplaceSection::size)).apply((Applicative)i, ReplaceSection::new));
/*     */     }
/*     */     
/*     */     public ReplaceSection(int offset) {
/*  94 */       this(offset, Optional.empty());
/*     */     }
/*     */     
/*     */     public ListOperation.Type mode() {
/*  99 */       return ListOperation.Type.REPLACE_SECTION;
/*     */     }
/*     */     
/*     */     public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) {
/* 104 */       int originalSize = original.size();
/* 105 */       if (this.offset > originalSize) {
/* 106 */         LOGGER.error("Cannot replace when offset is out of bounds");
/* 107 */         return original;
/*     */       } 
/* 110 */       ImmutableList.Builder<T> newList = ImmutableList.builder();
/* 111 */       newList.addAll(original.subList(0, this.offset));
/* 112 */       newList.addAll(replacement);
/* 113 */       int resumeIndex = this.offset + ((Integer)this.size.orElse(Integer.valueOf(replacement.size()))).intValue();
/* 114 */       if (resumeIndex < originalSize)
/* 115 */         newList.addAll(original.subList(resumeIndex, originalSize)); 
/* 117 */       ImmutableList immutableList = newList.build();
/* 118 */       if (immutableList.size() > maxSize) {
/* 119 */         LOGGER.error("Contents overflow in section replacement");
/* 120 */         return original;
/*     */       } 
/* 122 */       return (List<T>)immutableList;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Insert extends Record implements ListOperation {
/*     */     private final int offset;
/*     */     
/*     */     public Insert(int offset) {
/* 126 */       this.offset = offset;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #126	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$Insert;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int offset() {
/* 126 */       return this.offset;
/*     */     }
/*     */     
/* 127 */     private static final Logger LOGGER = LogUtils.getLogger();
/*     */     
/*     */     public static final MapCodec<Insert> MAP_CODEC;
/*     */     
/*     */     static {
/* 129 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("offset", Integer.valueOf(0)).forGetter(Insert::offset)).apply((Applicative)i, Insert::new));
/*     */     }
/*     */     
/*     */     public ListOperation.Type mode() {
/* 135 */       return ListOperation.Type.INSERT;
/*     */     }
/*     */     
/*     */     public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) {
/* 140 */       int originalSize = original.size();
/* 141 */       if (this.offset > originalSize) {
/* 142 */         LOGGER.error("Cannot insert when offset is out of bounds");
/* 143 */         return original;
/*     */       } 
/* 145 */       if (originalSize + replacement.size() > maxSize) {
/* 146 */         LOGGER.error("Contents overflow in section insertion");
/* 147 */         return original;
/*     */       } 
/* 150 */       ImmutableList.Builder<T> newList = ImmutableList.builder();
/* 151 */       newList.addAll(original.subList(0, this.offset));
/* 152 */       newList.addAll(replacement);
/* 153 */       newList.addAll(original.subList(this.offset, originalSize));
/* 154 */       return (List<T>)newList.build();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Append implements ListOperation {
/* 159 */     private static final Logger LOGGER = LogUtils.getLogger();
/*     */     
/* 161 */     public static final Append INSTANCE = new Append();
/*     */     
/* 162 */     public static final MapCodec<Append> MAP_CODEC = MapCodec.unit(() -> INSTANCE);
/*     */     
/*     */     public ListOperation.Type mode() {
/* 169 */       return ListOperation.Type.APPEND;
/*     */     }
/*     */     
/*     */     public <T> List<T> apply(List<T> original, List<T> replacement, int maxSize) {
/* 174 */       if (original.size() + replacement.size() > maxSize) {
/* 175 */         LOGGER.error("Contents overflow in section append");
/* 176 */         return original;
/*     */       } 
/* 178 */       return Stream.<T>concat(original.stream(), replacement.stream()).toList();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class StandAlone<T> extends Record {
/*     */     private final List<T> value;
/*     */     
/*     */     private final ListOperation operation;
/*     */     
/*     */     public StandAlone(List<T> value, ListOperation operation) {
/* 182 */       this.value = value;
/* 182 */       this.operation = operation;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #182	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone<TT;>;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #182	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone<TT;>;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #182	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/ListOperation$StandAlone<TT;>;
/*     */     }
/*     */     
/*     */     public List<T> value() {
/* 182 */       return this.value;
/*     */     }
/*     */     
/*     */     public ListOperation operation() {
/* 182 */       return this.operation;
/*     */     }
/*     */     
/*     */     public static <T> Codec<StandAlone<T>> codec(Codec<T> valueCodec, int maxSize) {
/* 184 */       return RecordCodecBuilder.create(i -> i.group((App)valueCodec.sizeLimitedListOf(maxSize).fieldOf("values").forGetter(()), (App)ListOperation.codec(maxSize).forGetter(())).apply((Applicative)i, StandAlone::new));
/*     */     }
/*     */     
/*     */     public List<T> apply(List<T> input) {
/* 191 */       return this.operation.apply(input, this.value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\ListOperation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */