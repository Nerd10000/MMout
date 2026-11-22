/*    */ package net.minecraft.world.level.saveddata.maps;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ import net.minecraft.world.level.saveddata.SavedData;
/*    */ import net.minecraft.world.level.saveddata.SavedDataType;
/*    */ 
/*    */ public class MapIndex extends SavedData {
/*    */   private static final int NO_MAP_ID = -1;
/*    */   
/*    */   public static final Codec<MapIndex> CODEC;
/*    */   
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.optionalFieldOf("map", Integer.valueOf(-1)).forGetter(())).apply((Applicative)i, MapIndex::new));
/*    */   }
/*    */   
/* 17 */   public static final SavedDataType<MapIndex> TYPE = new SavedDataType(Identifier.withDefaultNamespace("maps/last_id"), MapIndex::new, CODEC, DataFixTypes.SAVED_DATA_MAP_INDEX);
/*    */   
/*    */   private int lastMapId;
/*    */   
/*    */   public MapIndex() {
/* 22 */     this(-1);
/*    */   }
/*    */   
/*    */   public MapIndex(int lastMapId) {
/* 26 */     this.lastMapId = lastMapId;
/*    */   }
/*    */   
/*    */   public MapId getNextMapId() {
/* 30 */     MapId id = new MapId(++this.lastMapId);
/* 31 */     setDirty();
/* 32 */     return id;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\saveddata\maps\MapIndex.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */