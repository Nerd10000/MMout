/*     */ package net.minecraft.util.worldupdate;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ 
/*     */ public class Builder {
/*     */   private final DataFixer dataFixer;
/*     */   
/*     */   private DataFixTypes dataFixType;
/*     */   
/*     */   private String type;
/*     */   
/*     */   private String folderName;
/*     */   
/* 287 */   private int defaultVersion = -1;
/*     */   
/*     */   private boolean recreateRegionFiles;
/*     */   
/* 289 */   private UpgradeProgress upgradeProgress = new UpgradeProgress.Noop();
/*     */   
/*     */   private CompoundTag dataFixContextTag;
/*     */   
/* 291 */   private Int2ObjectAVLTreeMap<RegionStorageUpgrader.TagModifier> tagModifiers = new Int2ObjectAVLTreeMap();
/*     */   
/*     */   public Builder(DataFixer dataFixer) {
/* 294 */     this.dataFixer = dataFixer;
/*     */   }
/*     */   
/*     */   public Builder setDataFixType(DataFixTypes dataFixType) {
/* 298 */     this.dataFixType = dataFixType;
/* 299 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setTypeAndFolderName(String name) {
/* 303 */     return setType(name).setFolderName(name);
/*     */   }
/*     */   
/*     */   public Builder setType(String type) {
/* 307 */     this.type = type;
/* 308 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setFolderName(String folderName) {
/* 312 */     this.folderName = folderName;
/* 313 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setDefaultVersion(int defaultVersion) {
/* 317 */     this.defaultVersion = defaultVersion;
/* 318 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setRecreateRegionFiles(boolean recreateRegionFiles) {
/* 322 */     this.recreateRegionFiles = recreateRegionFiles;
/* 323 */     return this;
/*     */   }
/*     */   
/*     */   public Builder trackProgress(UpgradeProgress upgradeProgress) {
/* 327 */     this.upgradeProgress = upgradeProgress;
/* 328 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setDataFixContextTag(CompoundTag dataFixContextTag) {
/* 332 */     this.dataFixContextTag = dataFixContextTag;
/* 333 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addTagModifier(int version, RegionStorageUpgrader.TagModifier tagModifier) {
/* 337 */     if (this.tagModifiers.containsKey(version))
/* 338 */       throw new IllegalStateException("Can't add two fixers for the same data version"); 
/* 340 */     this.tagModifiers.put(version, tagModifier);
/* 341 */     return this;
/*     */   }
/*     */   
/*     */   private Builder setTagModifiers(Int2ObjectAVLTreeMap<RegionStorageUpgrader.TagModifier> tagModifiers) {
/* 345 */     this.tagModifiers = tagModifiers;
/* 346 */     return this;
/*     */   }
/*     */   
/*     */   public Builder copy() {
/* 350 */     return (new Builder(this.dataFixer))
/* 351 */       .setDataFixType(this.dataFixType)
/* 352 */       .setType(this.type)
/* 353 */       .setFolderName(this.folderName)
/* 354 */       .setDefaultVersion(this.defaultVersion)
/* 355 */       .setRecreateRegionFiles(this.recreateRegionFiles)
/* 356 */       .trackProgress(this.upgradeProgress)
/* 357 */       .setDataFixContextTag(this.dataFixContextTag)
/* 358 */       .setTagModifiers(this.tagModifiers.clone());
/*     */   }
/*     */   
/*     */   public RegionStorageUpgrader build(int previousCopiesFileAmounts) {
/* 362 */     return new RegionStorageUpgrader(this.dataFixer, 
/*     */         
/* 364 */         Objects.<DataFixTypes>requireNonNull(this.dataFixType), 
/* 365 */         Objects.<String>requireNonNull(this.type), 
/* 366 */         Objects.<String>requireNonNull(this.folderName), this.defaultVersion, this.recreateRegionFiles, this.upgradeProgress, previousCopiesFileAmounts, this.dataFixContextTag, (Int2ObjectMap<RegionStorageUpgrader.TagModifier>)this.tagModifiers);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\worldupdate\RegionStorageUpgrader$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */