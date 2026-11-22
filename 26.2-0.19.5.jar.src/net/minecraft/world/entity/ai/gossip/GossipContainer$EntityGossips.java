/*    */ package net.minecraft.world.entity.ai.gossip;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ class EntityGossips {
/* 50 */   private final Object2IntMap<GossipType> entries = (Object2IntMap<GossipType>)new Object2IntOpenHashMap();
/*    */   
/*    */   public int weightedValue(Predicate<GossipType> types) {
/* 53 */     return this.entries.object2IntEntrySet()
/* 54 */       .stream()
/* 55 */       .filter(e -> types.test((GossipType)e.getKey()))
/* 56 */       .mapToInt(e -> e.getIntValue() * ((GossipType)e.getKey()).weight)
/* 57 */       .sum();
/*    */   }
/*    */   
/*    */   public Stream<GossipContainer.GossipEntry> unpack(UUID target) {
/* 61 */     return this.entries.object2IntEntrySet().stream().map(e -> new GossipContainer.GossipEntry(target, (GossipType)e.getKey(), e.getIntValue()));
/*    */   }
/*    */   
/*    */   public void decay() {
/* 65 */     ObjectIterator<Object2IntMap.Entry<GossipType>> it = this.entries.object2IntEntrySet().iterator();
/* 66 */     while (it.hasNext()) {
/* 67 */       Object2IntMap.Entry<GossipType> gossip = (Object2IntMap.Entry<GossipType>)it.next();
/* 68 */       int newValue = gossip.getIntValue() - ((GossipType)gossip.getKey()).decayPerDay;
/* 69 */       if (newValue < 2) {
/* 70 */         it.remove();
/*    */         continue;
/*    */       } 
/* 72 */       gossip.setValue(newValue);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 78 */     return this.entries.isEmpty();
/*    */   }
/*    */   
/*    */   public void makeSureValueIsntTooLowOrTooHigh(GossipType type) {
/* 82 */     int value = this.entries.getInt(type);
/* 83 */     if (value > type.max)
/* 84 */       this.entries.put(type, type.max); 
/* 86 */     if (value < 2)
/* 87 */       remove(type); 
/*    */   }
/*    */   
/*    */   public void remove(GossipType type) {
/* 92 */     this.entries.removeInt(type);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\gossip\GossipContainer$EntityGossips.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */