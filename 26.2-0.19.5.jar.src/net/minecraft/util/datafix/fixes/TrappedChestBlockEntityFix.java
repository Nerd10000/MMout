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
/*     */ import com.mojang.datafixers.types.templates.TaggedChoice;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TrappedChestBlockEntityFix extends DataFix {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int SIZE = 4096;
/*     */   
/*     */   private static final short SIZE_BITS = 12;
/*     */   
/*     */   public TrappedChestBlockEntityFix(Schema outputSchema, boolean changesType) {
/*  31 */     super(outputSchema, changesType);
/*     */   }
/*     */   
/*     */   public TypeRewriteRule makeRule() {
/*  36 */     Type<?> chunkType = getOutputSchema().getType(References.CHUNK);
/*  37 */     Type<?> levelType = chunkType.findFieldType("Level");
/*  38 */     Type<?> tileEntitiesType = levelType.findFieldType("TileEntities");
/*  39 */     if (!(tileEntitiesType instanceof List.ListType))
/*  40 */       throw new IllegalStateException("Tile entity type is not a list type."); 
/*  42 */     List.ListType<?> tileEntityListType = (List.ListType)tileEntitiesType;
/*  44 */     OpticFinder<? extends List<?>> tileEntitiesF = DSL.fieldFinder("TileEntities", (Type)tileEntityListType);
/*  46 */     Type<?> chunkType1 = getInputSchema().getType(References.CHUNK);
/*  48 */     OpticFinder<?> levelFinder = chunkType1.findField("Level");
/*  49 */     OpticFinder<?> sectionsFinder = levelFinder.type().findField("Sections");
/*  50 */     Type<?> sectionsType = sectionsFinder.type();
/*  51 */     if (!(sectionsType instanceof List.ListType))
/*  52 */       throw new IllegalStateException("Expecting sections to be a list."); 
/*  54 */     Type<?> sectionType = ((List.ListType)sectionsType).getElement();
/*  55 */     OpticFinder<?> sectionFinder = DSL.typeFinder(sectionType);
/*  57 */     return TypeRewriteRule.seq((new AddNewChoices(
/*  58 */           getOutputSchema(), "AddTrappedChestFix", References.BLOCK_ENTITY)).makeRule(), 
/*  59 */         fixTypeEverywhereTyped("Trapped Chest fix", chunkType1, chunk -> chunk.updateTyped(levelFinder, ())));
/*     */   }
/*     */   
/*     */   public static final class TrappedChestSection extends LeavesFix.Section {
/*     */     private IntSet chestIds;
/*     */     
/*     */     public TrappedChestSection(Typed<?> section, Schema inputSchema) {
/* 114 */       super(section, inputSchema);
/*     */     }
/*     */     
/*     */     protected boolean skippable() {
/* 119 */       this.chestIds = (IntSet)new IntOpenHashSet();
/* 121 */       for (int i = 0; i < this.palette.size(); i++) {
/* 122 */         Dynamic<?> paletteTag = this.palette.get(i);
/* 123 */         String blockName = paletteTag.get("Name").asString("");
/* 124 */         if (Objects.equals(blockName, "minecraft:trapped_chest"))
/* 125 */           this.chestIds.add(i); 
/*     */       } 
/* 129 */       return this.chestIds.isEmpty();
/*     */     }
/*     */     
/*     */     public boolean isTrappedChest(int block) {
/* 133 */       return this.chestIds.contains(block);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\TrappedChestBlockEntityFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */