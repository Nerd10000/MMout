/*    */ package net.minecraft.world.level.saveddata;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.datafix.DataFixTypes;
/*    */ 
/*    */ public class WanderingTraderData extends SavedData {
/*    */   public static final Codec<WanderingTraderData> CODEC;
/*    */   
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.optionalFieldOf("spawn_delay", Integer.valueOf(24000)).forGetter(()), (App)Codec.INT.optionalFieldOf("spawn_chance", Integer.valueOf(25)).forGetter(())).apply((Applicative)i, WanderingTraderData::new));
/*    */   }
/*    */   
/* 15 */   public static final SavedDataType<WanderingTraderData> TYPE = new SavedDataType<>(
/* 16 */       Identifier.withDefaultNamespace("wandering_trader"), WanderingTraderData::new, CODEC, DataFixTypes.SAVED_DATA_WANDERING_TRADER);
/*    */   
/*    */   private int spawnDelay;
/*    */   
/*    */   private int spawnChance;
/*    */   
/*    */   public WanderingTraderData() {
/* 26 */     this(24000, 25);
/*    */   }
/*    */   
/*    */   public WanderingTraderData(int spawnDelay, int spawnChance) {
/* 30 */     this.spawnDelay = spawnDelay;
/* 31 */     this.spawnChance = spawnChance;
/*    */   }
/*    */   
/*    */   public int spawnDelay() {
/* 35 */     return this.spawnDelay;
/*    */   }
/*    */   
/*    */   public void setSpawnDelay(int spawnDelay) {
/* 39 */     if (this.spawnDelay != spawnDelay) {
/* 40 */       this.spawnDelay = spawnDelay;
/* 41 */       setDirty(true);
/*    */     } 
/*    */   }
/*    */   
/*    */   public int spawnChance() {
/* 46 */     return this.spawnChance;
/*    */   }
/*    */   
/*    */   public void setSpawnChance(int spawnChance) {
/* 50 */     if (this.spawnChance != spawnChance) {
/* 51 */       this.spawnChance = spawnChance;
/* 52 */       setDirty(true);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\saveddata\WanderingTraderData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */