/*      */ package net.minecraft.world.entity;
/*      */ 
/*      */ public enum MovementEmission {
/* 4194 */   NONE(false, false),
/* 4195 */   SOUNDS(true, false),
/* 4196 */   EVENTS(false, true),
/* 4197 */   ALL(true, true);
/*      */   
/*      */   private final boolean sounds;
/*      */   
/*      */   private final boolean events;
/*      */   
/*      */   MovementEmission(boolean sounds, boolean events) {
/* 4203 */     this.sounds = sounds;
/* 4204 */     this.events = events;
/*      */   }
/*      */   
/*      */   public boolean emitsAnything() {
/* 4208 */     return (this.events || this.sounds);
/*      */   }
/*      */   
/*      */   public boolean emitsEvents() {
/* 4212 */     return this.events;
/*      */   }
/*      */   
/*      */   public boolean emitsSounds() {
/* 4216 */     return this.sounds;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Entity$MovementEmission.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */