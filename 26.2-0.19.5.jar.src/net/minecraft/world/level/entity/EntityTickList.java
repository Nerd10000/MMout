/*    */ package net.minecraft.world.level.entity;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class EntityTickList {
/* 13 */   private Int2ObjectMap<Entity> active = (Int2ObjectMap<Entity>)new Int2ObjectLinkedOpenHashMap();
/*    */   
/* 14 */   private Int2ObjectMap<Entity> passive = (Int2ObjectMap<Entity>)new Int2ObjectLinkedOpenHashMap();
/*    */   
/*    */   private Int2ObjectMap<Entity> iterated;
/*    */   
/*    */   private void ensureActiveIsNotIterated() {
/* 18 */     if (this.iterated == this.active) {
/* 19 */       this.passive.clear();
/* 20 */       for (ObjectIterator<Int2ObjectMap.Entry<Entity>> objectIterator = Int2ObjectMaps.fastIterable(this.active).iterator(); objectIterator.hasNext(); ) {
/* 20 */         Int2ObjectMap.Entry<Entity> entry = objectIterator.next();
/* 21 */         this.passive.put(entry.getIntKey(), entry.getValue());
/*    */       } 
/* 23 */       Int2ObjectMap<Entity> tmp = this.active;
/* 24 */       this.active = this.passive;
/* 25 */       this.passive = tmp;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void add(Entity entity) {
/* 30 */     ensureActiveIsNotIterated();
/* 31 */     this.active.put(entity.getId(), entity);
/*    */   }
/*    */   
/*    */   public void remove(Entity entity) {
/* 35 */     ensureActiveIsNotIterated();
/* 36 */     this.active.remove(entity.getId());
/*    */   }
/*    */   
/*    */   public boolean contains(Entity entity) {
/* 40 */     return this.active.containsKey(entity.getId());
/*    */   }
/*    */   
/*    */   public void forEach(Consumer<Entity> output) {
/* 44 */     if (this.iterated != null)
/* 46 */       throw new UnsupportedOperationException("Only one concurrent iteration supported"); 
/* 49 */     this.iterated = this.active;
/*    */     try {
/* 52 */       for (ObjectIterator<Entity> objectIterator = this.active.values().iterator(); objectIterator.hasNext(); ) {
/* 52 */         Entity entity = objectIterator.next();
/* 53 */         output.accept(entity);
/*    */       } 
/*    */     } finally {
/* 56 */       this.iterated = null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\entity\EntityTickList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */