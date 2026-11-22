/*     */ package net.minecraft.network.chat;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.io.File;
/*     */ import java.net.URI;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public interface ClickEvent {
/*     */   public static final Codec<ClickEvent> CODEC;
/*     */   
/*     */   Action action();
/*     */   
/*     */   static {
/*  21 */     CODEC = Action.CODEC.dispatch("action", ClickEvent::action, action -> action.codec);
/*     */   }
/*     */   
/*     */   public static final class OpenUrl extends Record implements ClickEvent {
/*     */     private final URI uri;
/*     */     
/*     */     public static final MapCodec<OpenUrl> CODEC;
/*     */     
/*     */     public OpenUrl(URI uri) {
/*  25 */       this.uri = uri;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$OpenUrl;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenUrl;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$OpenUrl;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenUrl;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$OpenUrl;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenUrl;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public URI uri() {
/*  25 */       return this.uri;
/*     */     }
/*     */     
/*     */     static {
/*  26 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.UNTRUSTED_URI.fieldOf("url").forGetter(OpenUrl::uri)).apply((Applicative)i, OpenUrl::new));
/*     */     }
/*     */     
/*     */     public ClickEvent.Action action() {
/*  32 */       return ClickEvent.Action.OPEN_URL;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class OpenFile extends Record implements ClickEvent {
/*     */     private final String path;
/*     */     
/*     */     public static final MapCodec<OpenFile> CODEC;
/*     */     
/*     */     public OpenFile(String path) {
/*  36 */       this.path = path;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$OpenFile;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenFile;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$OpenFile;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenFile;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$OpenFile;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$OpenFile;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String path() {
/*  36 */       return this.path;
/*     */     }
/*     */     
/*     */     static {
/*  37 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("path").forGetter(OpenFile::path)).apply((Applicative)i, OpenFile::new));
/*     */     }
/*     */     
/*     */     public OpenFile(File file) {
/*  42 */       this(file.toString());
/*     */     }
/*     */     
/*     */     public OpenFile(Path path) {
/*  47 */       this(path.toFile());
/*     */     }
/*     */     
/*     */     public File file() {
/*  51 */       return new File(this.path);
/*     */     }
/*     */     
/*     */     public ClickEvent.Action action() {
/*  56 */       return ClickEvent.Action.OPEN_FILE;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class RunCommand extends Record implements ClickEvent {
/*     */     private final String command;
/*     */     
/*     */     public static final MapCodec<RunCommand> CODEC;
/*     */     
/*     */     public RunCommand(String command) {
/*  60 */       this.command = command;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$RunCommand;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$RunCommand;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$RunCommand;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$RunCommand;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$RunCommand;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #60	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$RunCommand;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String command() {
/*  60 */       return this.command;
/*     */     }
/*     */     
/*     */     static {
/*  61 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.CHAT_STRING.fieldOf("command").forGetter(RunCommand::command)).apply((Applicative)i, RunCommand::new));
/*     */     }
/*     */     
/*     */     public ClickEvent.Action action() {
/*  67 */       return ClickEvent.Action.RUN_COMMAND;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class SuggestCommand extends Record implements ClickEvent {
/*     */     private final String command;
/*     */     
/*     */     public static final MapCodec<SuggestCommand> CODEC;
/*     */     
/*     */     public SuggestCommand(String command) {
/*  71 */       this.command = command;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #71	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$SuggestCommand;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String command() {
/*  71 */       return this.command;
/*     */     }
/*     */     
/*     */     static {
/*  72 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.CHAT_STRING.fieldOf("command").forGetter(SuggestCommand::command)).apply((Applicative)i, SuggestCommand::new));
/*     */     }
/*     */     
/*     */     public ClickEvent.Action action() {
/*  78 */       return ClickEvent.Action.SUGGEST_COMMAND;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ShowDialog extends Record implements ClickEvent {
/*     */     private final Holder<Dialog> dialog;
/*     */     
/*     */     public static final MapCodec<ShowDialog> CODEC;
/*     */     
/*     */     public ShowDialog(Holder<Dialog> dialog) {
/*  82 */       this.dialog = dialog;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$ShowDialog;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$ShowDialog;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$ShowDialog;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$ShowDialog;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$ShowDialog;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$ShowDialog;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Holder<Dialog> dialog() {
/*  82 */       return this.dialog;
/*     */     }
/*     */     
/*     */     static {
/*  83 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Dialog.CODEC.fieldOf("dialog").forGetter(ShowDialog::dialog)).apply((Applicative)i, ShowDialog::new));
/*     */     }
/*     */     
/*     */     public ClickEvent.Action action() {
/*  89 */       return ClickEvent.Action.SHOW_DIALOG;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ChangePage extends Record implements ClickEvent {
/*     */     private final int page;
/*     */     
/*     */     public static final MapCodec<ChangePage> CODEC;
/*     */     
/*     */     public ChangePage(int page) {
/*  93 */       this.page = page;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$ChangePage;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$ChangePage;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$ChangePage;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$ChangePage;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$ChangePage;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #93	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$ChangePage;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int page() {
/*  93 */       return this.page;
/*     */     }
/*     */     
/*     */     static {
/*  94 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.POSITIVE_INT.fieldOf("page").forGetter(ChangePage::page)).apply((Applicative)i, ChangePage::new));
/*     */     }
/*     */     
/*     */     public ClickEvent.Action action() {
/* 100 */       return ClickEvent.Action.CHANGE_PAGE;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class CopyToClipboard extends Record implements ClickEvent {
/*     */     private final String value;
/*     */     
/*     */     public static final MapCodec<CopyToClipboard> CODEC;
/*     */     
/*     */     public CopyToClipboard(String value) {
/* 104 */       this.value = value;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$CopyToClipboard;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$CopyToClipboard;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$CopyToClipboard;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$CopyToClipboard;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$CopyToClipboard;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$CopyToClipboard;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String value() {
/* 104 */       return this.value;
/*     */     }
/*     */     
/*     */     static {
/* 105 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("value").forGetter(CopyToClipboard::value)).apply((Applicative)i, CopyToClipboard::new));
/*     */     }
/*     */     
/*     */     public ClickEvent.Action action() {
/* 111 */       return ClickEvent.Action.COPY_TO_CLIPBOARD;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class Custom extends Record implements ClickEvent {
/*     */     private final Identifier id;
/*     */     
/*     */     private final Optional<Tag> payload;
/*     */     
/*     */     public static final MapCodec<Custom> CODEC;
/*     */     
/*     */     public Custom(Identifier id, Optional<Tag> payload) {
/* 115 */       this.id = id;
/* 115 */       this.payload = payload;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/ClickEvent$Custom;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$Custom;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/ClickEvent$Custom;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/ClickEvent$Custom;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/ClickEvent$Custom;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #115	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/ClickEvent$Custom;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Identifier id() {
/* 115 */       return this.id;
/*     */     }
/*     */     
/*     */     public Optional<Tag> payload() {
/* 115 */       return this.payload;
/*     */     }
/*     */     
/*     */     static {
/* 116 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Identifier.CODEC.fieldOf("id").forGetter(Custom::id), (App)ExtraCodecs.NBT.optionalFieldOf("payload").forGetter(Custom::payload)).apply((Applicative)i, Custom::new));
/*     */     }
/*     */     
/*     */     public ClickEvent.Action action() {
/* 124 */       return ClickEvent.Action.CUSTOM;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Action implements StringRepresentable {
/* 129 */     OPEN_URL("open_url", true, (String)ClickEvent.OpenUrl.CODEC),
/* 130 */     OPEN_FILE("open_file", false, (String)ClickEvent.OpenFile.CODEC),
/* 131 */     RUN_COMMAND("run_command", true, (String)ClickEvent.RunCommand.CODEC),
/* 132 */     SUGGEST_COMMAND("suggest_command", true, (String)ClickEvent.SuggestCommand.CODEC),
/* 133 */     SHOW_DIALOG("show_dialog", true, (String)ClickEvent.ShowDialog.CODEC),
/* 134 */     CHANGE_PAGE("change_page", true, (String)ClickEvent.ChangePage.CODEC),
/* 135 */     COPY_TO_CLIPBOARD("copy_to_clipboard", true, (String)ClickEvent.CopyToClipboard.CODEC),
/* 136 */     CUSTOM("custom", true, (String)ClickEvent.Custom.CODEC);
/*     */     
/* 139 */     public static final Codec<Action> UNSAFE_CODEC = (Codec<Action>)StringRepresentable.fromEnum(Action::values);
/*     */     
/* 140 */     public static final Codec<Action> CODEC = UNSAFE_CODEC.validate(Action::filterForSerialization);
/*     */     
/*     */     private final boolean allowFromServer;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final MapCodec<? extends ClickEvent> codec;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Action(String name, boolean allowFromServer, MapCodec<? extends ClickEvent> codec) {
/* 147 */       this.name = name;
/* 148 */       this.allowFromServer = allowFromServer;
/* 149 */       this.codec = codec;
/*     */     }
/*     */     
/*     */     public boolean isAllowedFromServer() {
/* 153 */       return this.allowFromServer;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 158 */       return this.name;
/*     */     }
/*     */     
/*     */     public MapCodec<? extends ClickEvent> valueCodec() {
/* 162 */       return this.codec;
/*     */     }
/*     */     
/*     */     public static DataResult<Action> filterForSerialization(Action action) {
/* 166 */       if (!action.isAllowedFromServer())
/* 167 */         return DataResult.error(() -> "Click event type not allowed: " + String.valueOf(action)); 
/* 169 */       return DataResult.success(action, Lifecycle.stable());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\ClickEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */