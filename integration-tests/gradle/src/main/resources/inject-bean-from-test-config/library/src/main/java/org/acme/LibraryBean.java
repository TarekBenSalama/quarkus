package org.acme;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LibraryBean implements LibraryBeanInterface {

    @Override
    public String getValue() {
        return "main";
    }
}
