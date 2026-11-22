/*    */ package net.minecraft.data.structures;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtUtils;
/*    */ import net.minecraft.server.packs.PackType;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.util.datafix.DataFixers;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class StructureUpdater implements SnbtToNbt.Filter {
/* 17 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 18 */   private static final String PREFIX = PackType.SERVER_DATA.getDirectory() + "/minecraft/structure/";
/*    */   
/*    */   public CompoundTag apply(String name, CompoundTag input) {
/* 22 */     if (name.startsWith(PREFIX))
/* 23 */       return update(name, input); 
/* 25 */     return input;
/*    */   }
/*    */   
/*    */   public static CompoundTag update(String name, CompoundTag tag) {
/* 29 */     StructureTemplate structureTemplate = new StructureTemplate();
/* 30 */     int fromVersion = NbtUtils.getDataVersion(tag, 500);
/* 31 */     int toVersion = 4882;
/* 32 */     if (fromVersion < 4882)
/* 33 */       LOGGER.warn("SNBT Too old, do not forget to update: {} < {}: {}", new Object[] { Integer.valueOf(fromVersion), Integer.valueOf(4882), name }); 
/* 35 */     CompoundTag updated = DataFixTypes.STRUCTURE.updateToCurrentVersion(DataFixers.getDataFixer(), tag, fromVersion);
/* 36 */     structureTemplate.load((HolderGetter)BuiltInRegistries.BLOCK, updated);
/* 37 */     return structureTemplate.save(new CompoundTag());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\structures\StructureUpdater.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */