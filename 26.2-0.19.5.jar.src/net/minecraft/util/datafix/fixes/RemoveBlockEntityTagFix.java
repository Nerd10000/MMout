/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFix;
/*     */ import com.mojang.datafixers.OpticFinder;
/*     */ import com.mojang.datafixers.TypeRewriteRule;
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.types.Type;
/*     */ import com.mojang.datafixers.types.templates.List;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*     */ import net.minecraft.util.datafix.schemas.NamespacedSchema;
/*     */ 
/*     */ public class RemoveBlockEntityTagFix extends DataFix {
/*     */   private final Set<String> blockEntityIdsToDrop;
/*     */   
/*     */   private final boolean useLegacyDataStructure;
/*     */   
/*     */   public RemoveBlockEntityTagFix(Schema outputSchema, Set<String> blockEntityIdsToDrop) {
/*  25 */     this(outputSchema, false, blockEntityIdsToDrop);
/*     */   }
/*     */   
/*     */   public RemoveBlockEntityTagFix(Schema outputSchema, boolean useLegacyDataStructure, Set<String> blockEntityIdsToDrop) {
/*  29 */     super(outputSchema, true);
/*  30 */     this.blockEntityIdsToDrop = blockEntityIdsToDrop;
/*  31 */     this.useLegacyDataStructure = useLegacyDataStructure;
/*     */   }
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  36 */     OpticFinder<String> blockEntityIdF = DSL.fieldFinder("id", NamespacedSchema.namespacedString());
/*  38 */     if (this.useLegacyDataStructure)
/*  39 */       return TypeRewriteRule.seq(
/*  40 */           createItemBlockEntityRemover(blockEntityIdF, "tag", "BlockEntityTag"), new TypeRewriteRule[] { createFallingBlockBlockEntityRemover(blockEntityIdF), 
/*  42 */             createStructureBlockEntityRemover(blockEntityIdF), 
/*  43 */             createUncheckedConverterHack() }); 
/*  46 */     return TypeRewriteRule.seq(
/*  47 */         createItemBlockEntityRemover(blockEntityIdF, "components", "minecraft:block_entity_data"), new TypeRewriteRule[] { createFallingBlockBlockEntityRemover(blockEntityIdF), 
/*  49 */           createStructureBlockEntityRemover(blockEntityIdF), 
/*  50 */           createChunkBlockEntityRemover(blockEntityIdF), 
/*  51 */           createUncheckedConverterHack() });
/*     */   }
/*     */   
/*     */   private TypeRewriteRule createItemBlockEntityRemover(OpticFinder<String> blockEntityIdF, String itemTagOrComponentKey, String itemBlockEntityDataKey) {
/*  56 */     Type<?> itemStackType = getInputSchema().getType(References.ITEM_STACK);
/*  57 */     OpticFinder<?> itemTagF = itemStackType.findField(itemTagOrComponentKey);
/*  58 */     OpticFinder<?> itemBlockEntityF = itemTagF.type().findField(itemBlockEntityDataKey);
/*  59 */     return fixTypeEverywhereTyped("ItemRemoveBlockEntityTagFix" + getOutputSchema().getVersionKey(), itemStackType, input -> input.updateTyped(itemTagF, ()));
/*     */   }
/*     */   
/*     */   private TypeRewriteRule createFallingBlockBlockEntityRemover(OpticFinder<String> blockEntityIdF) {
/*  67 */     Type<?> entityType = getInputSchema().getType(References.ENTITY);
/*  68 */     OpticFinder<?> fallingBlockF = DSL.namedChoice("minecraft:falling_block", getInputSchema().getChoiceType(References.ENTITY, "minecraft:falling_block"));
/*  69 */     OpticFinder<?> fallingBlockEntityTagF = fallingBlockF.type().findField("TileEntityData");
/*  70 */     return fixTypeEverywhereTyped("FallingBlockEntityRemoveBlockEntityTagFix" + getOutputSchema().getVersionKey(), entityType, input -> input.updateTyped(fallingBlockF, ()));
/*     */   }
/*     */   
/*     */   private TypeRewriteRule createStructureBlockEntityRemover(OpticFinder<String> blockEntityIdF) {
/*  78 */     Type<?> structureType = getInputSchema().getType(References.STRUCTURE);
/*  79 */     OpticFinder<?> blocksF = structureType.findField("blocks");
/*  80 */     OpticFinder<?> blockTypeF = DSL.typeFinder(((List.ListType)blocksF.type()).getElement());
/*  81 */     OpticFinder<?> blockNbtF = blockTypeF.type().findField("nbt");
/*  82 */     return fixTypeEverywhereTyped("StructureRemoveBlockEntityTagFix" + getOutputSchema().getVersionKey(), structureType, input -> input.updateTyped(blocksF, ()));
/*     */   }
/*     */   
/*     */   private TypeRewriteRule createChunkBlockEntityRemover(OpticFinder<String> blockEntityIdF) {
/*  92 */     Type<?> chunkType = getInputSchema().getType(References.CHUNK);
/*  93 */     OpticFinder<? extends List<?>> blockEntitiesF = chunkType.findField("block_entities");
/*  94 */     Type<?> blockEntityElementsType = ((List.ListType)blockEntitiesF.type()).getElement();
/*  95 */     OpticFinder<?> blockEntityTypeFinder = getInputSchema().getType(References.BLOCK_ENTITY).finder();
/*  97 */     Type<?> chunkTypeOut = getOutputSchema().getType(References.CHUNK);
/*  98 */     Type<List<?>> blockEntitiesTypeOut = chunkType.findField("block_entities").type();
/*  99 */     return fixTypeEverywhereTyped("BlockEntityChunkRemover" + getOutputSchema().getVersionKey(), chunkType, chunkTypeOut, input -> input.update(blockEntitiesF, blockEntitiesTypeOut, ()));
/*     */   }
/*     */   
/*     */   private TypeRewriteRule createUncheckedConverterHack() {
/* 118 */     return convertUnchecked("ItemRemoveBlockEntityTagFix - update block entity type" + getOutputSchema().getVersionKey(), getInputSchema().getType(References.BLOCK_ENTITY), getOutputSchema().getType(References.BLOCK_ENTITY));
/*     */   }
/*     */   
/*     */   private Typed<?> removeBlockEntity(Typed<?> tag, OpticFinder<?> blockEntityF, OpticFinder<String> blockEntityIdF, String blockEntityFieldName) {
/* 122 */     Optional<? extends Typed<?>> maybeBlockEntity = tag.getOptionalTyped(blockEntityF);
/* 123 */     if (maybeBlockEntity.isEmpty())
/* 124 */       return tag; 
/* 126 */     String blockEntityId = ((Typed)maybeBlockEntity.get()).getOptional(blockEntityIdF).orElse("");
/* 127 */     if (!this.blockEntityIdsToDrop.contains(blockEntityId))
/* 128 */       return tag; 
/* 130 */     return Util.writeAndReadTypedOrThrow(tag, tag.getType(), tagData -> tagData.remove(blockEntityFieldName));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\RemoveBlockEntityTagFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */