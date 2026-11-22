/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class Ticket {
/*    */   public static final MapCodec<Ticket> CODEC;
/*    */   
/*    */   private final TicketType type;
/*    */   
/*    */   private final int ticketLevel;
/*    */   
/*    */   private long ticksLeft;
/*    */   
/*    */   static {
/* 11 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BuiltInRegistries.TICKET_TYPE.byNameCodec().fieldOf("type").forGetter(Ticket::getType), (App)ExtraCodecs.NON_NEGATIVE_INT.fieldOf("level").forGetter(Ticket::getTicketLevel), (App)Codec.LONG.optionalFieldOf("ticks_left", Long.valueOf(0L)).forGetter(())).apply((Applicative)i, Ticket::new));
/*    */   }
/*    */   
/*    */   public Ticket(TicketType type, int ticketLevel) {
/* 22 */     this(type, ticketLevel, type.timeout());
/*    */   }
/*    */   
/*    */   private Ticket(TicketType type, int ticketLevel, long ticksLeft) {
/* 26 */     this.type = type;
/* 27 */     this.ticketLevel = ticketLevel;
/* 28 */     this.ticksLeft = ticksLeft;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 33 */     return this.type.hasTimeout() ? ("Ticket[" + 
/* 34 */       Util.getRegisteredName(BuiltInRegistries.TICKET_TYPE, this.type) + " " + this.ticketLevel + "] with " + this.ticksLeft + " ticks left ( out of" + this.type.timeout() + ")") : ("Ticket[" + 
/*    */       
/* 36 */       Util.getRegisteredName(BuiltInRegistries.TICKET_TYPE, this.type) + " " + this.ticketLevel + "] with no timeout");
/*    */   }
/*    */   
/*    */   public TicketType getType() {
/* 40 */     return this.type;
/*    */   }
/*    */   
/*    */   public int getTicketLevel() {
/* 44 */     return this.ticketLevel;
/*    */   }
/*    */   
/*    */   public void resetTicksLeft() {
/* 48 */     this.ticksLeft = this.type.timeout();
/*    */   }
/*    */   
/*    */   public void decreaseTicksLeft() {
/* 52 */     if (this.type.hasTimeout())
/* 53 */       this.ticksLeft--; 
/*    */   }
/*    */   
/*    */   public boolean isTimedOut() {
/* 58 */     return (this.type.hasTimeout() && this.ticksLeft < 0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\Ticket.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */