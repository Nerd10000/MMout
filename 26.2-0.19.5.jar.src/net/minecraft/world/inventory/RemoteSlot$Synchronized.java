/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.network.HashedPatchMap;
/*    */ import net.minecraft.network.HashedStack;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class Synchronized implements RemoteSlot {
/*    */   private final HashedPatchMap.HashGenerator hasher;
/*    */   
/* 35 */   private ItemStack remoteStack = null;
/*    */   
/* 36 */   private HashedStack remoteHash = null;
/*    */   
/*    */   public Synchronized(HashedPatchMap.HashGenerator hasher) {
/* 39 */     this.hasher = hasher;
/*    */   }
/*    */   
/*    */   public void force(ItemStack outgoing) {
/* 44 */     this.remoteStack = outgoing.copy();
/* 45 */     this.remoteHash = null;
/*    */   }
/*    */   
/*    */   public void receive(HashedStack incoming) {
/* 50 */     this.remoteStack = null;
/* 51 */     this.remoteHash = incoming;
/*    */   }
/*    */   
/*    */   public boolean matches(ItemStack local) {
/* 56 */     if (this.remoteStack != null)
/* 57 */       return ItemStack.matches(this.remoteStack, local); 
/* 60 */     if (this.remoteHash != null && 
/* 61 */       this.remoteHash.matches(local, this.hasher)) {
/* 63 */       this.remoteStack = local.copy();
/* 64 */       return true;
/*    */     } 
/* 68 */     return false;
/*    */   }
/*    */   
/*    */   public void copyFrom(Synchronized other) {
/* 72 */     this.remoteStack = other.remoteStack;
/* 73 */     this.remoteHash = other.remoteHash;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\RemoteSlot$Synchronized.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */