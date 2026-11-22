/*     */ package net.minecraft.world.level.levelgen.structure.placement;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public enum FrequencyReductionMethod implements StringRepresentable {
/* 151 */   DEFAULT("default", StructurePlacement::probabilityReducer),
/* 152 */   LEGACY_TYPE_1("legacy_type_1", StructurePlacement::legacyPillagerOutpostReducer),
/* 153 */   LEGACY_TYPE_2("legacy_type_2", StructurePlacement::legacyArbitrarySaltProbabilityReducer),
/* 154 */   LEGACY_TYPE_3("legacy_type_3", StructurePlacement::legacyProbabilityReducerWithDouble);
/*     */   
/*     */   public static final Codec<FrequencyReductionMethod> CODEC;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final StructurePlacement.FrequencyReducer reducer;
/*     */   
/*     */   static {
/* 157 */     CODEC = (Codec<FrequencyReductionMethod>)StringRepresentable.fromEnum(FrequencyReductionMethod::values);
/*     */   }
/*     */   
/*     */   FrequencyReductionMethod(String name, StructurePlacement.FrequencyReducer reducer) {
/* 163 */     this.name = name;
/* 164 */     this.reducer = reducer;
/*     */   }
/*     */   
/*     */   public boolean shouldGenerate(long seed, int salt, int sourceX, int sourceZ, float probability) {
/* 168 */     return this.reducer.shouldGenerate(seed, salt, sourceX, sourceZ, probability);
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 173 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\placement\StructurePlacement$FrequencyReductionMethod.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */