/*     */ package net.minecraft.client.gui.render;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiPredicate;
/*     */ import org.apache.commons.lang3.mutable.MutableInt;
/*     */ 
/*     */ public class DynamicAtlasAllocator<K> {
/*     */   private final int width;
/*     */   
/*     */   private final List<Slot> slots;
/*     */   
/*  20 */   private final Map<K, Slot> usedSlotByKey = new HashMap<>();
/*     */   
/*     */   private final BitSet freeSlots;
/*     */   
/*     */   public DynamicAtlasAllocator(int width, int height) {
/*  24 */     this.width = width;
/*  25 */     int size = width * height;
/*  26 */     this.slots = new ArrayList<>(size);
/*  27 */     for (int y = 0; y < height; y++) {
/*  28 */       for (int x = 0; x < width; x++)
/*  29 */         this.slots.add(new Slot(x, y)); 
/*     */     } 
/*  32 */     this.freeSlots = new BitSet(size);
/*  33 */     this.freeSlots.set(0, size);
/*     */   }
/*     */   
/*     */   public boolean reclaimSpaceFor(Set<K> keys) {
/*  37 */     int preexistingKeyCount = Sets.intersection(this.usedSlotByKey.keySet(), keys).size();
/*  38 */     if (preexistingKeyCount == keys.size())
/*  39 */       return true; 
/*  43 */     MutableInt needSpaceFor = new MutableInt(keys.size() - preexistingKeyCount);
/*  44 */     freeSlotIf((key, paramSlot) -> {
/*     */           if (needSpaceFor.intValue() == 0 || keys.contains(key))
/*     */             return false; 
/*     */           needSpaceFor.decrement();
/*     */           return true;
/*     */         });
/*  51 */     return (needSpaceFor.intValue() == 0);
/*     */   }
/*     */   
/*     */   public void endFrame() {
/*  55 */     freeSlotIf((paramObject, slot) -> slot.discardAfterFrame);
/*     */   }
/*     */   
/*     */   private void freeSlotIf(BiPredicate<K, Slot> predicate) {
/*  59 */     this.usedSlotByKey.entrySet().removeIf(entry -> {
/*     */           if (!predicate.test(entry.getKey(), entry.getValue()))
/*     */             return false; 
/*     */           Slot slot = (Slot)entry.getValue();
/*     */           this.freeSlots.set(slot.x + slot.y * this.width);
/*     */           slot.discardAfterFrame = false;
/*     */           return true;
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean hasSpaceForAll(Set<K> keys) {
/*  71 */     Sets.SetView setView = Sets.union(this.usedSlotByKey.keySet(), keys);
/*  72 */     return (setView.size() <= this.slots.size());
/*     */   }
/*     */   
/*     */   public Slot getOrAllocate(K key, boolean discardAfterFrame) {
/*  76 */     Slot usedSlot = this.usedSlotByKey.get(key);
/*  77 */     if (usedSlot != null) {
/*  78 */       usedSlot.discardAfterFrame |= discardAfterFrame;
/*  79 */       usedSlot.externalState = SlotState.READY;
/*  80 */       return usedSlot;
/*     */     } 
/*  83 */     int freeSlotIndex = this.freeSlots.nextSetBit(0);
/*  84 */     if (freeSlotIndex == -1)
/*  85 */       return null; 
/*  88 */     Slot freeSlot = this.slots.get(freeSlotIndex);
/*  89 */     freeSlot.externalState = freeSlot.fresh ? SlotState.EMPTY : SlotState.STALE;
/*  90 */     freeSlot.fresh = false;
/*  91 */     freeSlot.discardAfterFrame = discardAfterFrame;
/*  92 */     this.usedSlotByKey.put(key, freeSlot);
/*  93 */     this.freeSlots.clear(freeSlotIndex);
/*  94 */     return freeSlot;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int freeSlotCount() {
/*  99 */     return this.slots.size() - this.usedSlotByKey.size();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public Set<K> usedSlotKeys() {
/* 104 */     return Collections.unmodifiableSet(this.usedSlotByKey.keySet());
/*     */   }
/*     */   
/*     */   public static class Slot {
/*     */     private final int x;
/*     */     
/*     */     private final int y;
/*     */     
/*     */     private boolean fresh = true;
/*     */     
/*     */     private boolean discardAfterFrame;
/*     */     
/* 113 */     private DynamicAtlasAllocator.SlotState externalState = DynamicAtlasAllocator.SlotState.EMPTY;
/*     */     
/*     */     private Slot(int x, int y) {
/* 116 */       this.x = x;
/* 117 */       this.y = y;
/*     */     }
/*     */     
/*     */     public int x() {
/* 121 */       return this.x;
/*     */     }
/*     */     
/*     */     public int y() {
/* 125 */       return this.y;
/*     */     }
/*     */     
/*     */     public DynamicAtlasAllocator.SlotState state() {
/* 129 */       return this.externalState;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum SlotState {
/* 134 */     EMPTY, STALE, READY;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\DynamicAtlasAllocator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */