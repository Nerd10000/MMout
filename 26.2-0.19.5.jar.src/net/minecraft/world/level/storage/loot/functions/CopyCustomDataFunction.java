/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ 
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.commands.arguments.NbtPathArgument;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.Validatable;
/*     */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.nbt.NbtProviders;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class CopyCustomDataFunction extends LootItemConditionalFunction {
/*     */   public static final MapCodec<CopyCustomDataFunction> MAP_CODEC;
/*     */   
/*     */   private final NbtProvider source;
/*     */   
/*     */   private final List<CopyOperation> operations;
/*     */   
/*     */   private static final class CopyOperation extends Record {
/*     */     private final NbtPathArgument.NbtPath sourcePath;
/*     */     
/*     */     private final NbtPathArgument.NbtPath targetPath;
/*     */     
/*     */     private final CopyCustomDataFunction.MergeStrategy op;
/*     */     
/*     */     public static final Codec<CopyOperation> CODEC;
/*     */     
/*     */     private CopyOperation(NbtPathArgument.NbtPath sourcePath, NbtPathArgument.NbtPath targetPath, CopyCustomDataFunction.MergeStrategy op) {
/*  31 */       this.sourcePath = sourcePath;
/*  31 */       this.targetPath = targetPath;
/*  31 */       this.op = op;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyCustomDataFunction$CopyOperation;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public NbtPathArgument.NbtPath sourcePath() {
/*  31 */       return this.sourcePath;
/*     */     }
/*     */     
/*     */     public NbtPathArgument.NbtPath targetPath() {
/*  31 */       return this.targetPath;
/*     */     }
/*     */     
/*     */     public CopyCustomDataFunction.MergeStrategy op() {
/*  31 */       return this.op;
/*     */     }
/*     */     
/*     */     static {
/*  32 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)NbtPathArgument.NbtPath.CODEC.fieldOf("source").forGetter(CopyOperation::sourcePath), (App)NbtPathArgument.NbtPath.CODEC.fieldOf("target").forGetter(CopyOperation::targetPath), (App)CopyCustomDataFunction.MergeStrategy.CODEC.fieldOf("op").forGetter(CopyOperation::op)).apply((Applicative)i, CopyOperation::new));
/*     */     }
/*     */     
/*     */     public void apply(Supplier<Tag> target, Tag source) {
/*     */       try {
/*  40 */         List<Tag> sourceTags = this.sourcePath.get(source);
/*  41 */         if (!sourceTags.isEmpty())
/*  42 */           this.op.merge(target.get(), this.targetPath, sourceTags); 
/*  44 */       } catch (CommandSyntaxException commandSyntaxException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   static {
/*  50 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)NbtProviders.CODEC.fieldOf("source").forGetter(()), (App)CopyOperation.CODEC.listOf().fieldOf("ops").forGetter(()))).apply((Applicative)i, CopyCustomDataFunction::new));
/*     */   }
/*     */   
/*     */   private CopyCustomDataFunction(List<LootItemCondition> predicates, NbtProvider source, List<CopyOperation> operations) {
/*  59 */     super(predicates);
/*  60 */     this.source = source;
/*  61 */     this.operations = List.copyOf(operations);
/*     */   }
/*     */   
/*     */   public MapCodec<CopyCustomDataFunction> codec() {
/*  66 */     return MAP_CODEC;
/*     */   }
/*     */   
/*     */   public void validate(ValidationContext context) {
/*  71 */     super.validate(context);
/*  73 */     Validatable.validate(context, "source", (Validatable)this.source);
/*     */   }
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  78 */     Tag sourceTag = this.source.get(context);
/*  79 */     if (sourceTag == null)
/*  80 */       return itemStack; 
/*  83 */     MutableObject<CompoundTag> result = new MutableObject();
/*  84 */     Supplier<Tag> lazyTargetCopy = () -> {
/*     */         if (result.get() == null)
/*     */           result.setValue(((CustomData)itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)).copyTag()); 
/*     */         return (Tag)result.get();
/*     */       };
/*  90 */     this.operations.forEach(op -> op.apply(lazyTargetCopy, sourceTag));
/*  91 */     CompoundTag resultTag = (CompoundTag)result.get();
/*  92 */     if (resultTag != null)
/*  93 */       CustomData.set(DataComponents.CUSTOM_DATA, itemStack, resultTag); 
/*  96 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
/*     */     private final NbtProvider source;
/*     */     
/* 101 */     private final List<CopyCustomDataFunction.CopyOperation> ops = Lists.newArrayList();
/*     */     
/*     */     private Builder(NbtProvider source) {
/* 104 */       this.source = source;
/*     */     }
/*     */     
/*     */     public Builder copy(String sourcePath, String targetPath, CopyCustomDataFunction.MergeStrategy mergeStrategy) {
/*     */       try {
/* 109 */         this.ops.add(new CopyCustomDataFunction.CopyOperation(NbtPathArgument.NbtPath.of(sourcePath), NbtPathArgument.NbtPath.of(targetPath), mergeStrategy));
/* 110 */       } catch (CommandSyntaxException e) {
/* 111 */         throw new IllegalArgumentException(e);
/*     */       } 
/* 113 */       return this;
/*     */     }
/*     */     
/*     */     public Builder copy(String sourcePath, String targetPath) {
/* 117 */       return copy(sourcePath, targetPath, CopyCustomDataFunction.MergeStrategy.REPLACE);
/*     */     }
/*     */     
/*     */     protected Builder getThis() {
/* 122 */       return this;
/*     */     }
/*     */     
/*     */     public LootItemFunction build() {
/* 127 */       return new CopyCustomDataFunction(getConditions(), this.source, this.ops);
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static Builder copyData(NbtProvider source) {
/* 134 */     return new Builder(source);
/*     */   }
/*     */   
/*     */   public static Builder copyData(LootContext.EntityTarget source) {
/* 138 */     return new Builder(ContextNbtProvider.forContextEntity(source));
/*     */   }
/*     */   
/*     */   public enum MergeStrategy implements StringRepresentable {
/* 142 */     REPLACE("replace") {
/*     */       public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/* 145 */         path.set(target, (Tag)Iterables.getLast(sources));
/*     */       }
/*     */     },
/* 148 */     APPEND("append") {
/*     */       public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/* 151 */         List<Tag> targets = path.getOrCreate(target, ListTag::new);
/* 152 */         targets.forEach(tag -> {
/*     */               if (tag instanceof ListTag) {
/*     */                 ListTag listTag = (ListTag)tag;
/*     */                 sources.forEach(());
/*     */               } 
/*     */             });
/*     */       }
/*     */     },
/* 160 */     MERGE("merge") {
/*     */       public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/* 163 */         List<Tag> targets = path.getOrCreate(target, CompoundTag::new);
/* 164 */         targets.forEach(tag -> {
/*     */               if (tag instanceof CompoundTag) {
/*     */                 CompoundTag compoundTag = (CompoundTag)tag;
/*     */                 sources.forEach(());
/*     */               } 
/*     */             });
/*     */       }
/*     */     };
/*     */     
/* 177 */     public static final Codec<MergeStrategy> CODEC = (Codec<MergeStrategy>)StringRepresentable.fromEnum(MergeStrategy::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     MergeStrategy(String name) {
/* 184 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 189 */       return this.name;
/*     */     }
/*     */     
/*     */     public abstract void merge(Tag param1Tag, NbtPathArgument.NbtPath param1NbtPath, List<Tag> param1List) throws CommandSyntaxException;
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/*     */       path.set(target, (Tag)Iterables.getLast(sources));
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/*     */       List<Tag> targets = path.getOrCreate(target, ListTag::new);
/*     */       targets.forEach(tag -> {
/*     */             if (tag instanceof ListTag) {
/*     */               ListTag listTag = (ListTag)tag;
/*     */               sources.forEach(());
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */   
/*     */   enum null {
/*     */     public void merge(Tag target, NbtPathArgument.NbtPath path, List<Tag> sources) throws CommandSyntaxException {
/*     */       List<Tag> targets = path.getOrCreate(target, CompoundTag::new);
/*     */       targets.forEach(tag -> {
/*     */             if (tag instanceof CompoundTag) {
/*     */               CompoundTag compoundTag = (CompoundTag)tag;
/*     */               sources.forEach(());
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\functions\CopyCustomDataFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */