package com.gneworks.common.enums;

import lombok.Getter;

@Getter
public enum Roles {
    CLIENT(1), ROOT(9);

    private final int value;

    private Roles(int value) {
        this.value = value;
    }

    public static String findByValue(int value) {
        for (Roles role : Roles.values()) {
            if (role.getValue() == value) {
                return role.name();
            }
        }
        return null;
    }
}