/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class Builder {
/* 77 */   private final Set<ChatRestriction> restrictions = new HashSet<>();
/*    */   
/*    */   public Builder addRestriction(ChatRestriction restriction) {
/* 80 */     this.restrictions.add(restriction);
/* 81 */     return this;
/*    */   }
/*    */   
/*    */   public ChatAbilities build() {
/* 85 */     if (this.restrictions.isEmpty())
/* 86 */       return ChatAbilities.NO_RESTRICTIONS; 
/* 88 */     return new ChatAbilities(Set.copyOf(this.restrictions));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\ChatAbilities$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */