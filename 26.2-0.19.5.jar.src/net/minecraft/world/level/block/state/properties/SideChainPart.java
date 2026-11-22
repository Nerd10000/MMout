/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum SideChainPart implements StringRepresentable {
/*  6 */   UNCONNECTED("unconnected"),
/*  7 */   RIGHT("right"),
/*  8 */   CENTER("center"),
/*  9 */   LEFT("left");
/*    */   
/*    */   private final String name;
/*    */   
/*    */   SideChainPart(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 19 */     return getSerializedName();
/*    */   }
/*    */   
/*    */   public String getSerializedName() {
/* 24 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean isConnected() {
/* 28 */     return (this != UNCONNECTED);
/*    */   }
/*    */   
/*    */   public boolean isConnectionTowards(SideChainPart endPart) {
/* 32 */     return (this == CENTER || this == endPart);
/*    */   }
/*    */   
/*    */   public boolean isChainEnd() {
/* 36 */     return (this != CENTER);
/*    */   }
/*    */   
/*    */   public SideChainPart whenConnectedToTheRight() {
/* 40 */     switch (ordinal()) {
/*    */       default:
/* 40 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       case 3:
/*    */       
/*    */       case 1:
/*    */       case 2:
/*    */         break;
/*    */     } 
/* 40 */     return 
/*    */       
/* 42 */       CENTER;
/*    */   }
/*    */   
/*    */   public SideChainPart whenConnectedToTheLeft() {
/* 47 */     switch (ordinal()) {
/*    */       default:
/* 47 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       case 3:
/*    */         break;
/*    */     } 
/* 47 */     return 
/*    */       
/* 49 */       CENTER;
/*    */   }
/*    */   
/*    */   public SideChainPart whenDisconnectedFromTheRight() {
/* 54 */     switch (ordinal()) {
/*    */       default:
/* 54 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       case 3:
/*    */       
/*    */       case 1:
/*    */       case 2:
/*    */         break;
/*    */     } 
/* 54 */     return 
/*    */       
/* 56 */       RIGHT;
/*    */   }
/*    */   
/*    */   public SideChainPart whenDisconnectedFromTheLeft() {
/* 61 */     switch (ordinal()) {
/*    */       default:
/* 61 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       case 3:
/*    */         break;
/*    */     } 
/* 61 */     return 
/*    */       
/* 63 */       LEFT;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\properties\SideChainPart.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */