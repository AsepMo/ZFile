package com.store.data.root.command.util;

import java.io.IOException;

public class RootAccessDeniedException extends IOException {
    private static final long serialVersionUID = 9088998884166225540L;

    public RootAccessDeniedException() {
        super();
    }

    public RootAccessDeniedException(String detailMessage) {
        super(detailMessage);
    }

}
