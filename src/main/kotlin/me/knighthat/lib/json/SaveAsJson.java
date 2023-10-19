package me.knighthat.lib.json;

import org.jetbrains.annotations.NotNull;

public interface SaveAsJson extends JsonSerializable {

    @NotNull
    String getDisplayName();

    @NotNull
    String getFileName();

    @NotNull
    String getFileExtension();

    default @NotNull String getFullName() {return getFileName() + "." + getFileExtension();}
}
