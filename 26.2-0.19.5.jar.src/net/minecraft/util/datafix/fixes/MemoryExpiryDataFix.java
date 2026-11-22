/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class MemoryExpiryDataFix extends NamedEntityFix {
/*    */   public MemoryExpiryDataFix(Schema schema, String entityType) {
/* 25 */     super(schema, false, "Memory expiry data fix (" + entityType + ")", References.ENTITY, entityType);
/*    */   }
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 30 */     return entity.update(DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 34 */     return input.update("Brain", this::updateBrain);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateBrain(Dynamic<?> input) {
/* 38 */     return input.update("memories", this::updateMemories);
/*    */   }
/*    */   
/*    */   private Dynamic<?> updateMemories(Dynamic<?> memories) {
/* 42 */     return memories.updateMapValues(this::updateMemoryEntry);
/*    */   }
/*    */   
/*    */   private Pair<Dynamic<?>, Dynamic<?>> updateMemoryEntry(Pair<Dynamic<?>, Dynamic<?>> memoryEntry) {
/* 46 */     return memoryEntry.mapSecond(this::wrapMemoryValue);
/*    */   }
/*    */   
/*    */   private Dynamic<?> wrapMemoryValue(Dynamic<?> dynamic) {
/* 50 */     return dynamic.createMap((Map)ImmutableMap.of(dynamic
/* 51 */           .createString("value"), dynamic));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\fixes\MemoryExpiryDataFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */