/*     */ package net.minecraft.commands.arguments;
/*     */ 
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.nbt.CollectionTag;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ public class NbtPath {
/*     */   private final String original;
/*     */   
/*     */   private final Object2IntMap<NbtPathArgument.Node> nodeToOriginalPosition;
/*     */   
/*     */   private final NbtPathArgument.Node[] nodes;
/*     */   
/*     */   public static final Codec<NbtPath> CODEC;
/*     */   
/*     */   static {
/* 148 */     CODEC = Codec.STRING.comapFlatMap(string -> {
/*     */           try {
/*     */             NbtPath parsed = (new NbtPathArgument()).parse(new StringReader(string));
/*     */             return DataResult.success(parsed);
/* 153 */           } catch (CommandSyntaxException e) {
/*     */             return DataResult.error(());
/*     */           } 
/*     */         }NbtPath::asString);
/*     */   }
/*     */   
/*     */   public static NbtPath of(String string) throws CommandSyntaxException {
/* 161 */     return (new NbtPathArgument()).parse(new StringReader(string));
/*     */   }
/*     */   
/*     */   public NbtPath(String original, NbtPathArgument.Node[] nodes, Object2IntMap<NbtPathArgument.Node> nodeToOriginalPosition) {
/* 165 */     this.original = original;
/* 166 */     this.nodes = nodes;
/* 167 */     this.nodeToOriginalPosition = nodeToOriginalPosition;
/*     */   }
/*     */   
/*     */   public List<Tag> get(Tag tag) throws CommandSyntaxException {
/* 171 */     List<Tag> result = Collections.singletonList(tag);
/* 172 */     for (NbtPathArgument.Node node : this.nodes) {
/* 173 */       result = node.get(result);
/* 174 */       if (result.isEmpty())
/* 175 */         throw createNotFoundException(node); 
/*     */     } 
/* 178 */     return result;
/*     */   }
/*     */   
/*     */   public int countMatching(Tag tag) {
/* 182 */     List<Tag> result = Collections.singletonList(tag);
/* 183 */     for (NbtPathArgument.Node node : this.nodes) {
/* 184 */       result = node.get(result);
/* 185 */       if (result.isEmpty())
/* 186 */         return 0; 
/*     */     } 
/* 189 */     return result.size();
/*     */   }
/*     */   
/*     */   private List<Tag> getOrCreateParents(Tag tag) throws CommandSyntaxException {
/* 193 */     List<Tag> result = Collections.singletonList(tag);
/* 195 */     for (int i = 0; i < this.nodes.length - 1; i++) {
/* 196 */       NbtPathArgument.Node node = this.nodes[i];
/* 197 */       int next = i + 1;
/* 198 */       Objects.requireNonNull(this.nodes[next]);
/* 198 */       result = node.getOrCreate(result, this.nodes[next]::createPreferredParentTag);
/* 199 */       if (result.isEmpty())
/* 200 */         throw createNotFoundException(node); 
/*     */     } 
/* 203 */     return result;
/*     */   }
/*     */   
/*     */   public List<Tag> getOrCreate(Tag tag, Supplier<Tag> newTagValue) throws CommandSyntaxException {
/* 207 */     List<Tag> result = getOrCreateParents(tag);
/* 209 */     NbtPathArgument.Node lastNode = this.nodes[this.nodes.length - 1];
/* 210 */     return lastNode.getOrCreate(result, newTagValue);
/*     */   }
/*     */   
/*     */   private static int apply(List<Tag> targets, Function<Tag, Integer> operation) {
/* 214 */     return ((Integer)targets.stream().<Integer>map(operation).reduce(Integer.valueOf(0), (a, b) -> Integer.valueOf(a.intValue() + b.intValue()))).intValue();
/*     */   }
/*     */   
/*     */   public static boolean isTooDeep(Tag tag, int depth) {
/* 218 */     if (depth >= 512)
/* 219 */       return true; 
/* 221 */     if (tag instanceof CompoundTag) {
/* 221 */       CompoundTag compound = (CompoundTag)tag;
/* 222 */       for (Tag child : compound.values()) {
/* 223 */         if (isTooDeep(child, depth + 1))
/* 224 */           return true; 
/*     */       } 
/* 227 */     } else if (tag instanceof ListTag) {
/* 227 */       ListTag list = (ListTag)tag;
/* 228 */       for (Tag listEntry : list) {
/* 229 */         if (isTooDeep(listEntry, depth + 1))
/* 230 */           return true; 
/*     */       } 
/*     */     } 
/* 234 */     return false;
/*     */   }
/*     */   
/*     */   public int set(Tag tag, Tag toAdd) throws CommandSyntaxException {
/* 238 */     if (isTooDeep(toAdd, estimatePathDepth()))
/* 239 */       throw NbtPathArgument.ERROR_DATA_TOO_DEEP.create(); 
/* 241 */     Tag firstCopy = toAdd.copy();
/* 242 */     List<Tag> result = getOrCreateParents(tag);
/* 243 */     if (result.isEmpty())
/* 244 */       return 0; 
/* 247 */     NbtPathArgument.Node lastNode = this.nodes[this.nodes.length - 1];
/* 248 */     MutableBoolean usedFirstCopy = new MutableBoolean(false);
/* 249 */     return apply(result, t -> Integer.valueOf(lastNode.setTag(t, ())));
/*     */   }
/*     */   
/*     */   private int estimatePathDepth() {
/* 260 */     return this.nodes.length;
/*     */   }
/*     */   
/*     */   public int insert(int index, CompoundTag target, List<Tag> toInsert) throws CommandSyntaxException {
/* 264 */     List<Tag> toInsertCopy = new ArrayList<>(toInsert.size());
/* 265 */     for (Tag tag : toInsert) {
/* 266 */       Tag copy = tag.copy();
/* 267 */       toInsertCopy.add(copy);
/* 268 */       if (isTooDeep(copy, estimatePathDepth()))
/* 269 */         throw NbtPathArgument.ERROR_DATA_TOO_DEEP.create(); 
/*     */     } 
/* 272 */     Collection<Tag> targets = getOrCreate((Tag)target, ListTag::new);
/* 274 */     int modifiedCount = 0;
/* 275 */     boolean usedFirst = false;
/* 276 */     for (Tag targetTag : targets) {
/*     */       CollectionTag targetList;
/* 277 */       if (targetTag instanceof CollectionTag) {
/* 277 */         targetList = (CollectionTag)targetTag;
/*     */       } else {
/* 278 */         throw NbtPathArgument.ERROR_EXPECTED_LIST.create(targetTag);
/*     */       } 
/* 281 */       boolean modified = false;
/* 282 */       int actualIndex = (index < 0) ? (targetList.size() + index + 1) : index;
/* 283 */       for (Tag sourceTag : toInsertCopy) {
/*     */         try {
/* 285 */           if (targetList.addTag(actualIndex, usedFirst ? sourceTag.copy() : sourceTag)) {
/* 286 */             actualIndex++;
/* 287 */             modified = true;
/*     */           } 
/* 289 */         } catch (IndexOutOfBoundsException e) {
/* 290 */           throw NbtPathArgument.ERROR_INVALID_INDEX.create(Integer.valueOf(actualIndex));
/*     */         } 
/*     */       } 
/* 293 */       usedFirst = true;
/* 294 */       modifiedCount += modified ? 1 : 0;
/*     */     } 
/* 297 */     return modifiedCount;
/*     */   }
/*     */   
/*     */   public int remove(Tag tag) {
/* 301 */     List<Tag> result = Collections.singletonList(tag);
/* 303 */     for (int i = 0; i < this.nodes.length - 1; i++)
/* 304 */       result = this.nodes[i].get(result); 
/* 307 */     NbtPathArgument.Node lastNode = this.nodes[this.nodes.length - 1];
/* 308 */     Objects.requireNonNull(lastNode);
/* 308 */     return apply(result, lastNode::removeTag);
/*     */   }
/*     */   
/*     */   private CommandSyntaxException createNotFoundException(NbtPathArgument.Node node) {
/* 312 */     int index = this.nodeToOriginalPosition.getInt(node);
/* 313 */     return NbtPathArgument.ERROR_NOTHING_FOUND.create(this.original.substring(0, index));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 318 */     return this.original;
/*     */   }
/*     */   
/*     */   public String asString() {
/* 322 */     return this.original;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\NbtPathArgument$NbtPath.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */