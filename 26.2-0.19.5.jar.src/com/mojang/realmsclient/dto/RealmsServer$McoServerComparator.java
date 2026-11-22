/*     */ package com.mojang.realmsclient.dto;
/*     */ 
/*     */ import com.google.common.collect.ComparisonChain;
/*     */ import java.util.Comparator;
/*     */ import java.util.Objects;
/*     */ 
/*     */ public class McoServerComparator implements Comparator<RealmsServer> {
/*     */   private final String refOwner;
/*     */   
/*     */   public McoServerComparator(String owner) {
/* 310 */     this.refOwner = owner;
/*     */   }
/*     */   
/*     */   public int compare(RealmsServer server1, RealmsServer server2) {
/* 315 */     return ComparisonChain.start()
/* 316 */       .compareTrueFirst(server1.isSnapshotRealm(), server2.isSnapshotRealm())
/* 317 */       .compareTrueFirst((server1.state == RealmsServer.State.UNINITIALIZED), (server2.state == RealmsServer.State.UNINITIALIZED))
/* 318 */       .compareTrueFirst(server1.expiredTrial, server2.expiredTrial)
/* 319 */       .compareTrueFirst(Objects.equals(server1.owner, this.refOwner), Objects.equals(server2.owner, this.refOwner))
/* 320 */       .compareFalseFirst(server1.expired, server2.expired)
/* 321 */       .compareTrueFirst((server1.state == RealmsServer.State.OPEN), (server2.state == RealmsServer.State.OPEN))
/* 322 */       .compare(server1.id, server2.id).result();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsServer$McoServerComparator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */