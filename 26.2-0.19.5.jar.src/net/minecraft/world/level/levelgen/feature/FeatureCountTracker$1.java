/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.google.common.cache.CacheLoader;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import org.apache.commons.lang3.mutable.MutableInt;
/*    */ 
/*    */ class null extends CacheLoader<ServerLevel, FeatureCountTracker.LevelData> {
/*    */   public FeatureCountTracker.LevelData load(ServerLevel level) {
/* 32 */     return new FeatureCountTracker.LevelData(Object2IntMaps.synchronize((Object2IntMap)new Object2IntOpenHashMap()), new MutableInt(0));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\FeatureCountTracker$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */