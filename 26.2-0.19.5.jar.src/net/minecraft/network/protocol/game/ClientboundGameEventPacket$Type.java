/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*    */ 
/*    */ public class Type {
/* 15 */   private static final Int2ObjectMap<Type> TYPES = (Int2ObjectMap<Type>)new Int2ObjectOpenHashMap();
/*    */   
/*    */   private final int id;
/*    */   
/*    */   public Type(int id) {
/* 20 */     this.id = id;
/* 21 */     TYPES.put(id, this);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundGameEventPacket$Type.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */