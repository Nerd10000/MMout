/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.datafix.PackedBitStorage;
/*     */ 
/*     */ public abstract class Section {
/*     */   protected static final String BLOCK_STATES_TAG = "BlockStates";
/*     */   
/*     */   protected static final String NAME_TAG = "Name";
/*     */   
/*     */   protected static final String PROPERTIES_TAG = "Properties";
/*     */   
/* 193 */   private final Type<Pair<String, Dynamic<?>>> blockStateType = DSL.named(References.BLOCK_STATE.typeName(), DSL.remainderType());
/*     */   
/* 194 */   protected final OpticFinder<List<Pair<String, Dynamic<?>>>> paletteFinder = DSL.fieldFinder("Palette", (Type)DSL.list(this.blockStateType));
/*     */   
/*     */   protected final List<Dynamic<?>> palette;
/*     */   
/*     */   protected final int index;
/*     */   
/*     */   protected PackedBitStorage storage;
/*     */   
/*     */   public Section(Typed<?> section, Schema inputSchema) {
/* 201 */     if (!Objects.equals(inputSchema.getType(References.BLOCK_STATE), this.blockStateType))
/* 202 */       throw new IllegalStateException("Block state type is not what was expected."); 
/* 205 */     Optional<List<Pair<String, Dynamic<?>>>> typedPalette = section.getOptional(this.paletteFinder);
/* 207 */     this.palette = (List<Dynamic<?>>)typedPalette.map(p -> (List)p.stream().map(Pair::getSecond).collect(Collectors.toList())).orElse(ImmutableList.of());
/* 209 */     Dynamic<?> tag = (Dynamic)section.get(DSL.remainderFinder());
/* 210 */     this.index = tag.get("Y").asInt(0);
/* 212 */     readStorage(tag);
/*     */   }
/*     */   
/*     */   protected void readStorage(Dynamic<?> tag) {
/* 216 */     if (skippable()) {
/* 217 */       this.storage = null;
/*     */     } else {
/* 219 */       long[] states = tag.get("BlockStates").asLongStream().toArray();
/* 220 */       int size = Math.max(4, DataFixUtils.ceillog2(this.palette.size()));
/* 221 */       this.storage = new PackedBitStorage(size, 4096, states);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Typed<?> write(Typed<?> section) {
/* 226 */     if (isSkippable())
/* 227 */       return section; 
/* 229 */     return section
/* 230 */       .update(DSL.remainderFinder(), tag -> tag.set("BlockStates", tag.createLongList(Arrays.stream(this.storage.getRaw()))))
/* 231 */       .set(this.paletteFinder, this.palette.stream().map(b -> Pair.of(References.BLOCK_STATE.typeName(), b)).collect(Collectors.toList()));
/*     */   }
/*     */   
/*     */   public boolean isSkippable() {
/* 235 */     return (this.storage == null);
/*     */   }
/*     */   
/*     */   public int getBlock(int pos) {
/* 239 */     return this.storage.get(pos);
/*     */   }
/*     */   
/*     */   protected static int getStateId(String blockName, boolean persistent, int distance) {
/* 243 */     return LeavesFix.LEAVES.get(blockName).intValue() << 5 | (persistent ? 16 : 0) | distance;
/*     */   }
/*     */   
/*     */   public int getIndex() {
/* 247 */     return this.index;
/*     */   }
/*     */   
/*     */   protected abstract boolean skippable();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\LeavesFix$Section.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */