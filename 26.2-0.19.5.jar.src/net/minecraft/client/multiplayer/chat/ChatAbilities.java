/*    */ package net.minecraft.client.multiplayer.chat;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.HashSet;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.server.permissions.Permission;
/*    */ import net.minecraft.server.permissions.PermissionSet;
/*    */ import net.minecraft.server.permissions.Permissions;
/*    */ 
/*    */ public class ChatAbilities {
/* 14 */   public static final ChatAbilities NO_RESTRICTIONS = new ChatAbilities(Set.of());
/*    */   
/*    */   private final Set<ChatRestriction> restrictionReasons;
/*    */   
/*    */   private final PermissionSet permissions;
/*    */   
/*    */   private final Predicate<GuiMessage> visibleMessagesFilter;
/*    */   
/*    */   private ChatAbilities(Set<ChatRestriction> restrictionReasons) {
/* 21 */     this.restrictionReasons = restrictionReasons;
/* 23 */     Set<Permission> permissionSet = new HashSet<>(Permissions.CHAT_PERMISSIONS);
/* 24 */     for (ChatRestriction restrictionReason : restrictionReasons)
/* 25 */       restrictionReason.modifyPermissions(permissionSet); 
/* 27 */     Objects.requireNonNull(Set.copyOf(permissionSet));
/* 27 */     this.permissions = Set.copyOf(permissionSet)::contains;
/* 28 */     this.visibleMessagesFilter = selectVisibleMessages(this);
/*    */   }
/*    */   
/*    */   private static Predicate<GuiMessage> selectVisibleMessages(ChatAbilities chatAbilities) {
/* 32 */     ImmutableSet.Builder<GuiMessageSource> visibleSourcesBuilder = ImmutableSet.builder();
/* 33 */     visibleSourcesBuilder.add(GuiMessageSource.SYSTEM_CLIENT);
/* 34 */     if (chatAbilities.canReceivePlayerMessages())
/* 35 */       visibleSourcesBuilder.add(GuiMessageSource.PLAYER); 
/* 37 */     if (chatAbilities.canReceiveSystemMessages())
/* 38 */       visibleSourcesBuilder.add(GuiMessageSource.SYSTEM_SERVER); 
/* 40 */     ImmutableSet<GuiMessageSource> visibleSources = visibleSourcesBuilder.build();
/* 41 */     return guiMessage -> visibleSources.contains(guiMessage.source());
/*    */   }
/*    */   
/*    */   public boolean hasAnyRestrictions() {
/* 45 */     return !this.restrictionReasons.isEmpty();
/*    */   }
/*    */   
/*    */   public Stream<ChatRestriction> restrictions() {
/* 49 */     return this.restrictionReasons.stream();
/*    */   }
/*    */   
/*    */   public PermissionSet permissions() {
/* 53 */     return this.permissions;
/*    */   }
/*    */   
/*    */   public boolean canSendMessages() {
/* 57 */     return this.permissions.hasPermission(Permissions.CHAT_SEND_MESSAGES);
/*    */   }
/*    */   
/*    */   public boolean canSendCommands() {
/* 61 */     return this.permissions.hasPermission(Permissions.CHAT_SEND_COMMANDS);
/*    */   }
/*    */   
/*    */   public boolean canReceivePlayerMessages() {
/* 65 */     return this.permissions.hasPermission(Permissions.CHAT_RECEIVE_PLAYER_MESSAGES);
/*    */   }
/*    */   
/*    */   public boolean canReceiveSystemMessages() {
/* 69 */     return this.permissions.hasPermission(Permissions.CHAT_RECEIVE_SYSTEM_MESSAGES);
/*    */   }
/*    */   
/*    */   public Predicate<GuiMessage> visibleMessagesFilter() {
/* 73 */     return this.visibleMessagesFilter;
/*    */   }
/*    */   
/*    */   public static class Builder {
/* 77 */     private final Set<ChatRestriction> restrictions = new HashSet<>();
/*    */     
/*    */     public Builder addRestriction(ChatRestriction restriction) {
/* 80 */       this.restrictions.add(restriction);
/* 81 */       return this;
/*    */     }
/*    */     
/*    */     public ChatAbilities build() {
/* 85 */       if (this.restrictions.isEmpty())
/* 86 */         return ChatAbilities.NO_RESTRICTIONS; 
/* 88 */       return new ChatAbilities(Set.copyOf(this.restrictions));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\chat\ChatAbilities.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */