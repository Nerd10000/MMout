/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.util.JsonUtils;
/*    */ import java.time.Instant;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public final class Subscription extends Record {
/*    */   private final Instant startDate;
/*    */   
/*    */   private final int daysLeft;
/*    */   
/*    */   private final SubscriptionType type;
/*    */   
/*    */   public Subscription(Instant startDate, int daysLeft, SubscriptionType type) {
/* 15 */     this.startDate = startDate;
/* 15 */     this.daysLeft = daysLeft;
/* 15 */     this.type = type;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/Subscription;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/Subscription;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/Subscription;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/Subscription;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/Subscription;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/Subscription;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Instant startDate() {
/* 15 */     return this.startDate;
/*    */   }
/*    */   
/*    */   public int daysLeft() {
/* 15 */     return this.daysLeft;
/*    */   }
/*    */   
/*    */   public SubscriptionType type() {
/* 15 */     return this.type;
/*    */   }
/*    */   
/* 20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static Subscription parse(String json) {
/*    */     try {
/* 24 */       JsonObject jsonObject = LenientJsonParser.parse(json).getAsJsonObject();
/* 25 */       return new Subscription(
/* 26 */           JsonUtils.getDateOr("startDate", jsonObject), 
/* 27 */           JsonUtils.getIntOr("daysLeft", jsonObject, 0), 
/* 28 */           typeFrom(JsonUtils.getStringOr("subscriptionType", jsonObject, null)));
/* 30 */     } catch (Exception e) {
/* 31 */       LOGGER.error("Could not parse Subscription", e);
/* 33 */       return new Subscription(Instant.EPOCH, 0, SubscriptionType.NORMAL);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static SubscriptionType typeFrom(String subscriptionType) {
/*    */     try {
/* 38 */       if (subscriptionType != null)
/* 39 */         return SubscriptionType.valueOf(subscriptionType); 
/* 41 */     } catch (Exception exception) {}
/* 43 */     return SubscriptionType.NORMAL;
/*    */   }
/*    */   
/*    */   public enum SubscriptionType {
/* 47 */     NORMAL, RECURRING;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\Subscription.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */